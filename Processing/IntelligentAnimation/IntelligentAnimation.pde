import ddf.minim.*;
import processing.video.*;
import java.util.*;
/************ Local objects ************/
Minim minim;
AudioOutput ao;
AudioRecorder ar;
Movie video;
PImage bg; // background
PImage fm; // current frame
Creator mc = new Creator();

PImage [] animtImgs = null;  // frames of an gif
String [] imgs = null;       // images names of movers.

ArrayList<BodyPoint> bp = new ArrayList<BodyPoint>();
LinkedList<PVector> yellows = new LinkedList<PVector>();
LinkedList<PVector> reds = new LinkedList<PVector>();
ArrayList<AnimationPoint> animations = new ArrayList<AnimationPoint>();
ArrayList<Mover> bodyParts = new ArrayList<Mover>();
ArrayList<Mover> freeMovers = new ArrayList<Mover>(); 
/************ Configuration ************/
int offsetX = 550, offsetY = 300; // offsets used to draw the new body on background.
int ballRadius = 30, connectRadius = 2;
int boxRed = 30;
int boxYellow = 40;
float[] pctRed = {0.75, 0.6, 0.53, 0.4, 0.25}; // least pct of REDs in a box to indicate a valid boxing
float[] pctYellow = {0.8, 0.7}; // least pct of YELLOWs in a box to indicate a valid boxing

String pathMoverImg, pathGIF, pathSound, pathBackground, 
       pathMonkey, pathExtraction, pathFinal;
int phase = 1, freeMoversN = 0;
int frameN = 0, bgCtr = 700, monkeyFrameN = 926; // FIXME
float duration = 31.0;  // hardcoded....
enum COLOR { RED, YELLOW, VOID };
enum TYPE { BALL, MOUSE_FOLLOWER, BODY};

/************ PARTYING PARTYING! ************/
void setup() {
  pathMoverImg = sketchPath("") + "Resources/IMG/";
  pathGIF = sketchPath("") + "Resources/GIF/";
  pathSound = sketchPath("") + "Resources/SOUND/";
  pathBackground = sketchPath("") + "BG/";
  pathMonkey = sketchPath("") + "MONKEY/";
  pathExtraction = sketchPath("") + "EXRT/";
  pathFinal = sketchPath("") + "FINAL/";
  
  size(1280, 720);
 // noFill();
  stroke(156, 217, 93);
  strokeWeight(4);
  frameRate(60);
  // load mover image names
  imgs = new File(pathMoverImg).list();
  // load animation frames;
  int ctr = new File(pathGIF).list().length;
  animtImgs = new PImage[ctr];
  for (int i = 0; i < ctr; ++i) animtImgs[i] = loadImage(pathGIF + nf(i + 1, 4) + ".gif");
  // play background video
  video = new Movie(this, sketchPath("Stage.avi"));
  video.frameRate(60);
  video.play();
  println("Start phase 1 : background capturing");
}

void draw() {

  if (video.available()) {
    float time = video.time();
    /**** PHASE 1 : Background saving ****/
    if (phase == 1) {
      if (time >= duration) {
        // Background frame saving finished, ready to go phase2
        phase = 2;
        bgCtr = frameN;
        frameN = 0;
        video = new Movie(this, sketchPath("Monkey.mov"));
        video.frameRate(60);
        video.play();
        println("Start phase 2 : red marker enhancement");
      } else {
        // Keep saving background frames;
        ++frameN;
        video.read();
        image(video, 0, 0);
        video.save(pathBackground + nf(frameN, 4) + ".tif");
      }
    } 

    /**** PHASE 2: Enhance red markers ****/
    else if (phase == 2) {
      if (time >= duration) {
        phase = 3;
        monkeyFrameN = frameN;
        frameN = 0;
        minim = new Minim(this);
        ao = minim.getLineOut();
        ar = minim.createRecorder(ao, "recording.wav");
        ar.beginRecord();
        println("Start phase 3 : new character rendering");
      } else {
        ++frameN;
        video.read();
        enhanceReds(video);
        video.save(pathMonkey + nf(frameN, 4) + ".tif");
      }
    }

    /************* PHASE 3: Segmentation and Simulation *************/
    else if (phase == 3) {
      // refresh
      background(0);
      ++frameN;
      if (frameN == monkeyFrameN) {
        ar.endRecord();
        ar.save();
        exit();
      }
 
      bg = loadImage(pathBackground + nf(frameN % bgCtr + 1, 4) + ".tif");
      fm = loadImage(pathMonkey + nf(frameN, 4) + ".tif");
      image(bg, 0, 0);
      reds.clear();
      yellows.clear();
      bodyParts.clear();
      bp.clear();
      
      // Store the coordinates of all the red/yellow pixels
      findMarkers(fm);
      // Locate and Initialize all segments
      initSegment(fm);
      fm.save(pathExtraction + nf(frameN, 4) + ".tif");
      // Build up the new character from body parts
      constructBody();
      // Draw moving objects, allowing them to interact with each other inc. monkey mimicker
      drawMovers();
      // Display special image animations like explosion
      displayAnimations();
      saveFrame(pathFinal + nf(frameN, 4) + ".tif");
    }
  }
}

void keyPressed() {

  switch (key) {
  case 'n' : // add new mover (ball)
    if (phase == 3 && imgs != null) {
      freeMovers.add(mc.createMover(TYPE.BALL));
      ++freeMoversN;
    }
    break;

  case 'm' : // add new mover that follows the mouse
    if (phase == 3 && imgs != null) {
      freeMovers.add(mc.createMover(TYPE.MOUSE_FOLLOWER));
      ++freeMoversN;
    }
    break;

  case 'c' : // clear all free movers
    freeMoversN = 0;
    freeMovers.clear();
    break;
  }
}

void findMarkers(PImage frame) { 
  for (int y = 0; y < frame.height; ++y) {
    for (int x = 0; x < frame.width; ++x) {
      int loc = y * frame.width + x;
      switch(getColor(frame.pixels[loc])) { 
      case RED:    reds.add(new PVector(x, y));    break;
      case YELLOW: yellows.add(new PVector(x, y)); break;
      case VOID: break;
      }
    }
  }
}

void initSegment(PImage frame) {
  initForColor(1, COLOR.YELLOW, frame);
  initForColor(5, COLOR.RED, frame);
}

// RED -> body parts (feet, hands, chest); YELLOW  -> face
void initForColor (int numToFind, COLOR c, PImage frame) {
  int boxSize = c == COLOR.RED ? boxRed : boxYellow;
  int boxArea = boxSize * boxSize;
  float [] tresholds = c == COLOR.RED ? pctRed : pctYellow;
  LinkedList<PVector> points = c == COLOR.RED ? reds : yellows;

  // if the size of points converges, it means we can't get anymore valid segmentation
  int prevSize = 0;
  L: while (numToFind > 0 && points.size() != prevSize ) {
    
    for (int i = 0; i < tresholds.length; ++i) {
      for (Iterator<PVector> it = points.iterator(); it.hasNext();) {
        PVector pt = it.next();
        if (isValidPoint(pt, frame)) {
          int ctr = 0;
          for (int dy = 0; dy < boxSize; ++dy){
            for (int dx = 0; dx < boxSize; ++dx) {
              int loc = int((dy + pt.y) * frame.width + (dx + pt.x));
              if (loc < frame.width * frame.height &&
                  getColor(frame.pixels[loc]) == c)
                  ++ctr;
            }
          }
          
          if ((float) ctr / boxArea >= tresholds[i]) {
            PVector center = extractCenter(frame, int(pt.x), int(pt.y), c); // which point red dots within this boundary box are marked invalid
            bp.add(new BodyPoint((int)center.x, (int)center.y, c));
            it.remove();
            if (--numToFind == 0) break L;
          }
        } else {
          it.remove();
        }
      }
    }
    prevSize = points.size();
  }
}

// find the segment center coordinate by averaging the foreground points
// and clear pixels by the way
PVector extractCenter(PImage frame, int startX, int startY, COLOR c) {
  float xsum = 0.0, ysum = 0.0;
  int ctr = 0;
  int boxSize = c == COLOR.RED ? boxRed : boxYellow;
  for (int dy = 0; dy < boxSize; ++dy) {
    for (int dx = 0; dx < boxSize; ++dx) {
      int loc = (dy + startY) * frame.width + (dx + startX);
      if (loc < frame.width * frame.height && getColor(frame.pixels[loc]) == c) {
        xsum += dx + startX;
        ysum += dy + startY;
        ++ctr;
        frame.pixels[loc] = -1; // indicate this pixel has been bounded
      }
    }
  }
  frame.updatePixels();
  return new PVector(xsum / ctr, ysum / ctr);
}

void constructBody() {
  // Find the centric point
  float minTotalDist = Float.MAX_VALUE;
  BodyPoint center = null;
  for (BodyPoint p1 : bp) {
    float totalDist = 0.0;
    for (BodyPoint p2 : bp) {
      totalDist += PVector.dist(p1, p2);
    }
    if (totalDist < minTotalDist) {
      minTotalDist = totalDist;
      center = p1;
    }
  }
  // Calculate the actual centroid.
  float xsum = 0.0, ysum = 0.0;
  for (BodyPoint p : bp) {
    xsum += p.x;
    ysum += p.y;
  }
  PVector actualCentroid = new PVector(xsum / bp.size(), ysum / bp.size());
  // tresholding and deciding
  PVector tmp = null;
  if (PVector.dist(center, actualCentroid) <= 20) {
    tmp = center;
    bp.remove(center);
  } else {
    tmp = actualCentroid;
  }
  // Draw chest
  ellipse(tmp.x - boxRed/2 + offsetX, tmp.y - boxRed/2 + offsetY, boxRed, boxRed);
  // Build!
  for (BodyPoint p : bp) {
    bodyParts.add(mc.createMover(TYPE.BODY, p, tmp));
  }
}

/*************** DRAWING SECTION ***************/
void drawMovers() {
  if (!bodyParts.isEmpty()) {
    for (Mover m : bodyParts)
      m.display();
  }

  if (!freeMovers.isEmpty()) {
    for (Mover m : freeMovers) {
      m.interact(); // at which point an explosion animation may occur
      m.move();
      m.display();
    }
  }
}

void displayAnimations() {
  for (Iterator<AnimationPoint> it = animations.iterator(); it.hasNext();) {
    AnimationPoint ap = it.next();
    ap.display();
    if (ap.finished()) it.remove();
  }
}

/*************** Helper methods ***************/

// Dilate all red pixels
void enhanceReds (PImage frame) {
  int [][] mask = {
    {1, 1, 1}, 
    {1, 1, 1}, 
    {1, 1, 1}
  };
  for (int y = 1; y < frame.height - 1; ++y) {
    for (int x = 1; x < frame.width - 1; ++x) {
      int centerLoc = y * frame.width + x;
      if (getColor(frame.pixels[centerLoc]) == COLOR.RED) {
        for (int dy = -1; dy <= 1; ++dy) {
          for (int dx = -1; dx <= 1; ++dx) {
            if (mask[dy + 1][dx + 1] == 1) {
              int loc = (y + dy) * frame.width + (x + dx);
              frame.pixels[loc] = color(255, 0, 0);
            }
          }
        }
      }
    }
  }
  frame.updatePixels();
}

COLOR getColor(color c) { // These magic numbers are chosen by tuning....
  if (phase >= 3 && c == color(255, 0, 0)) return COLOR.RED;

  int g = getGrayValue(c);
  if (red(c) >= 210) {
    if (g >= 185) {
      return COLOR.YELLOW;
    } else if (g >= 105) {
      return COLOR.RED;
    }
  } else if (red(c) >= 160 && g <= 85 && g > 60) {
      return COLOR.RED;
  }
  return COLOR.VOID;
}

boolean isValidPoint(PVector pt, PImage frame) {
  return frame.pixels[int(pt.y * frame.width + pt.x)] != -1;
}

int getGrayValue(color c) {
  return (int) (red(c) * 0.21 + green(c) *0.72 + blue(c) *0.07);
}