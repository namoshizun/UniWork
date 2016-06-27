// Utilize the Video extension for Processing
import processing.video.*;
import java.util.*;
Movie movie;
PImage prevFrame;
/*
 * Displacement vector:
 * displacement_X[x][y] represents the x_vector of the displacement 
 * of the grid centered at (x,y).
 * 
 * displacement_Y[x][y] represents the y_vector of the displacement 
 * of the grid centered at (x,y).
 */
int [][] displacement_X;
int [][] displacement_Y;
int numGrids_X;
int numGrids_Y;

int frameNum = 1;
int K = 9; // block size
int radius = K/2;

int phase = 2;
float duration;
int mHeight = 0;
int mWidth = 0;
/*
 * The frames of the original moive will be saved under directroy: /FRAMES
 * The frames with grids highlighted and motion vector
 * are saved under directory: /MOTION_FRAMES
 */
 
void setup() {
  size(1280, 720);
  
  frameRate(60);
  movie = new Movie(this, sketchPath("pingpang.mov"));
  movie.frameRate(60);
  movie.play();
  
  // Capture the first original frame image to read its property
  movie.read(); 
  image(movie, 0, 0);
  movie.save(sketchPath("") + "FRAMES/" + nf(frameNum, 4) + ".png");
  ++frameNum;
  
  // initialize the displacement vector
  mWidth = movie.width;
  mHeight = movie.height;
  numGrids_X = mWidth / K;
  numGrids_Y = mHeight / K;
  displacement_X = new int[numGrids_Y][numGrids_X];
  displacement_Y = new int[numGrids_Y][numGrids_X];
  duration = movie.duration();
}

void draw() {
  // current play progress
  float time = movie.time();
  
  if (movie.available()) {
    
    if(time + 0.1 >= duration) {
      if(phase == 1) {
        // frame saving finished, then enable motion estimation.
        phase = 2;
        frameNum = 1;   
        movie = new Movie(this, sketchPath("monkey.avi"));
        movie.frameRate(120);
        movie.play();
      } else {
        exit();
      }
    }
    
    movie.read();
    if(phase == 1){
      image(movie, 0, 0);
      text(String.format("Phase - %d - %.2f%%", phase, 100 * time/duration), 100, 80);
      movie.save(sketchPath("") + "FRAMES/" + nf(frameNum, 4) + ".png");
      ++frameNum;
      return;
    }
    
    // At phase 2, jump the first frame since it has no previous frame for reference
    if(frameNum == 1) {
      ++frameNum;
      return;
    }
    
    // load the previous frame, compare with the current frame and update the motion vectors.
    prevFrame = loadImage(sketchPath("") + "FRAMES/" + nf(frameNum - 1, 4) + ".png");
    updateMotionVector(prevFrame, movie);
    
    // plot the image with motion vector lines, and then save the processed frame
    image(movie, 0, 0);
    plotMotionVector();
    saveFrame(sketchPath("") + "MOTION_FRAMES/" + nf(frameNum, 4) + ".png");
    ++frameNum;
    
    // reset the displacement vector
    displacement_X = new int[mHeight][mWidth];
    displacement_Y = new int[mHeight][mWidth];
  }
}

void updateMotionVector(PImage prevFrame, PImage currFrame) { //<>//
  
  for(int i = 0; i < numGrids_Y; ++i) {
    for(int j = 0; j < numGrids_X; ++j) {
      int minSSD = Integer.MAX_VALUE;
      
      // get the location of the grid(x,y) on previous frame
      int px = radius + K * j;
      int py = radius + K * i;
      
      // remember the best B(i + 1)
      int best_x = px;
      int best_y = py;
      
      // Search the surrounding 9*9 block area for the best match
      for(int dy = -2; dy <= 2; ++dy) {
        for(int dx = -2; dx <= 2; ++dx) {
          // get the location of the search grid(x', y') on current frame
          int cx = px + dx * K;
          int cy = py + dy * K;
          
          // avoid ArrayIndexOutOfBound
          if(cx < 0 || cy < 0 || cx >= mWidth || cy >= mHeight) break;
      
          // compute SSD
          int ssd = 0;
          for(int ddy = -1 * radius; ddy <= radius; ++ddy) {
            for(int ddx = -1 * radius; ddx <= radius; ++ddx) {
              // get previous color;
              int ploc = (py + ddy) * mWidth + (px + ddx);
              color p = prevFrame.pixels[ploc];  // FIXME
              // get current color; 
              int cloc = (cy + ddy) * mWidth + (cx + ddx);
              color c = currFrame.pixels[cloc]; // FIXME //<>//
              ssd += Math.pow(getGreyValue(red(c), green(c), blue(c)) - getGreyValue(red(p), green(p), blue(p)), 2.0);
            //  ssd += Math.pow(getColorSum(red(c), green(c), blue(c)) - getColorSum(red(p), green(p), blue(p)), 2.0);
            }
          }
          ssd = (int)Math.sqrt(ssd);
          if(ssd >= 1100 && ssd < minSSD){ // '1350' is a magic number
            // update the position record for the best match
            minSSD = ssd;
            best_x = cx;
            best_y = cy;
          }
        }
      }
      // The location of the best match grid has been found.
      // update the displacement vector;
      displacement_X[i][j] = best_x - px;
      displacement_Y[i][j] = best_y - py;
    }
  }
}

void plotMotionVector() {
  
  for(int i = 0; i < numGrids_Y; ++i){
    for(int j = 0; j < numGrids_X; ++j){
        int dx = displacement_X[i][j];
        int dy = displacement_Y[i][j];
        int x = radius + K * j;
        int y = radius + K * i;
        stroke(255, 255, 255);
        line(x, y, x + dx, y + dy);
    }
  }
}

int getColorSum(float r, float g, float b) {
  return (int) (r + g + b);
}

int getGreyValue (float r, float g, float b){
  return (int) (0.21267 * r + 0.715160 * g + 0.072169 * b);
}

// Useless haha
void movieEvent(Movie m){
}