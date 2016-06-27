import java.util.*;
import java.io.*;



List<Mover> movers = new ArrayList<Mover>();
Mover robot;
String[] textures;
int numMovers = 0;
int frameCtr = 0;
float boxSize = 0.0;

final int BALL_RADIUS = 20;
final float GRAVITY = 0.3;
final float RATIO = 0.6325;
final float FRICTION = -0.9;
final float SPRING = 0.2;


void setup() {
  size(700, 700, P3D);
  noFill();
  boxSize = width * RATIO;
  File resource = new File(sketchPath("") + "Textures");
  textures = resource.list();
}

void mouseClicked() {
  // Randomly choose a texture map from texture pool
  int rIdx = (int) random(textures.length);
  PImage ballTexture = loadImage(sketchPath("") + "Textures/" + textures[rIdx]);
  PShape ballShape = createShape(SPHERE, BALL_RADIUS);
  ballShape.setTexture(ballTexture);
  ballShape.setStroke(false);
  
  // add the newly created ball to the local list. 
  PVector pos = new PVector(mouseX - width/2, mouseY - height/2, 0);
  PVector veloc = new PVector(2, 0, random(6) + 1.0);
  float mass = random(5) + 1.0;
  movers.add(new Ball(pos, veloc, ballShape, BALL_RADIUS * 2, mass));
  ++numMovers;
}

void keyPressed() {
  
  switch (key) {
  case 'w':// crawl along negative z-axis
  if(robot != null) robot.veloc.z -= 1.0;
  break;
  
  case 's': // crawl along postive z-axis
  if(robot != null) robot.veloc.z += 1.0;
  break;
  
  case 'a': // crawl along negative x-axis
  if(robot != null) robot.veloc.x -= 1.0;
  break;
  
  case 'd': // crawl along positive x-axis
  if(robot != null) robot.veloc.x += 1.0;
  break;
  
  // Make spider robot jump along negative y-axis
  case 'j':
  if(robot != null) robot.veloc.y -= -12.0;
  break;
  
  // Add a random spider robot
  case 'k':
  if (robot == null) {
    robot = new Robot(new PVector(random(-boxSize/2, boxSize/2), boxSize/2, random(-boxSize/2, boxSize/2)),
            new PVector(0.0, 0.0, 0.0), createShape(SPHERE, 7), 7 * 2);
    movers.add(robot); 
    ++numMovers;
  }
  break;
  
  // Remove the robot
  case 'l' :
  movers.remove(robot);
  robot = null;
  break;
  }
}

void draw() {
  background(220);
  // translate to have (0, 0, 0) be the center of space.
  translate(width / 2, height / 2);
  // Draw the box space in which the ball(s) will be bouncing. 
  box(boxSize);
  
  for (Mover m : movers) {
    m.move();
    checkCollision();
    m.display();
  }
  ++frameCtr;
}

void checkCollision() {
  for (int i = 0; i < movers.size(); ++i) {
    Mover m1 = movers.get(i);
    for (int j = i + 1; j < movers.size(); ++j) {
      Mover m2 = movers.get(j);
      if (m1.pos.dist(m2.pos) <= m1.diameter/2 + m2.diameter/2) {
        m1.interact(m2);
      }
    }
  }
}