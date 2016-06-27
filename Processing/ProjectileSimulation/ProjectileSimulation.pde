final float FRICTION = -0.9;
final float SPRING = 0.2;
final float GRAVITY = 2.5;
final float fps = 60.0;

int frameCtr = 0;
int w = 1000;
int h = 530;
int tipX = 30;
int tipY = 30;
int radius = 15;
/*
 * phase 0: nothing happens....
 * phase 1: locating ball;
 * phase 2: deciding initial velocity
 * phase 4: waiting for fire
 * phase 5: ball travelling;
 */
int phase = 0;
Ball ball;

class Ball {
  PVector pos;
  PVector veloc;
  float mass;
  public Ball (PVector pos, PVector veloc, float mass) {
    this.pos = pos;
    this.veloc = veloc;
    this.mass = mass;
  }

  public void display() {
    fill(102, 178, 255);
    ellipse(pos.x, pos.y, radius * 2, radius * 2);
    fill(0);
  }

  public void move() {
    pos.add(veloc);
    if (reachedXedge(w)) {
      pos.x -= veloc.x;
      veloc.x *= FRICTION;
    }

    if (reachedYedge(h)) {
      pos.y -= veloc.y;
      veloc.y *= FRICTION;
    }

    if (abs(pos.y - (h - radius)) < 1 
      && abs(veloc.y) < 1.5
      && frameCtr % (fps/4) == 0) { // sliding on the ground, every 0.25s
      veloc.x *= abs(FRICTION);
    }
    // apply gravity
    veloc.y += GRAVITY;
  }

  public float getVelocityAngle() 
  { 
    return degrees(PVector.angleBetween(veloc, new PVector(1, 0)));
  }

  public boolean reachedXedge(int xrange) 
  { 
    return pos.x + radius > xrange || pos.x - radius < 0;
  }

  public boolean reachedYedge(int yrange) 
  { 
    return pos.y + radius > yrange || pos.y - radius < 0;
  }
}
/*************** MAIN ***************/
void setup() {
  size(1000, 600);
  frameRate(fps);
  noFill();
}

void mousePressed() {
  if (phase == 0) { // from default phase
    phase = 1; // to location initialization phase
  }
}

void mouseReleased() {
  if (phase == 1) { // from locating phase
    ball = new Ball(new PVector(mouseX, h - radius), new PVector(0, 0), 30);
    phase = 2; // switch to velocity initialization phase
  } else if (phase == 2) {
    PVector veloc = PVector.sub(new PVector(mouseX, mouseY), ball.pos);
    ball.veloc = veloc;
    phase = 3; // velocity set, waiting for order!
  }
}

void keyPressed() {
  switch(key) {
  case 'f' :
    phase = 4;
    break;

  case 'r' :
    phase = 0;
    break;
  }
}

// HELPER METHODs
void drawArrow(int cx, int cy, int len, float rad) {
  pushMatrix();
  translate(cx, cy);
  rotate(rad);
  line(0, 0, len, 0);
  line(len, 0, len - 3, -3);
  line(len, 0, len - 3, 3);
  popMatrix();
}

void displayRealtimeInfo(Ball b) {
  text("Object mass = " + b.mass + " radius = " + radius + "\n"
    + "PX = " + b.pos.x + "   PY = " + (h - b.pos.y) + "\n"
    + "VX = " + b.veloc.x + " VY = " + -b.veloc.y + "\n"
    + "Deg(v) = " + nf(b.getVelocityAngle(), 3, 2),
    tipX, tipY);
}

void draw() {
  background(255);
  smooth();
  line(0, h, width, h);
  fill(0);
  
  switch(phase) {
  case 0:
    text("Hello! Press and move your mouse to locate the object you want to throw", tipX, tipY);
    break;

  case 1: // location
    pushMatrix();
    translate(mouseX, h - radius);
    ellipse(0, 0, radius * 2, radius * 2);
    popMatrix();
    text("You are placing your object at: " + mouseX + " " + radius + "\n"
      + "Release mouse to confirm", tipX, tipY);
    break;

  case 2: // deciding velocity
    ball.display();
    PVector mouse = new PVector(mouseX, mouseY);
    PVector veloc = PVector.sub(mouse, ball.pos);
    float rad = PVector.angleBetween(veloc, new PVector(1, 0));
    drawArrow(int(ball.pos.x), int(ball.pos.y), int(veloc.mag()), -rad);
    text("Object will be shot at velocity : VX = " + veloc.x + " VY = " + -veloc.y
      + " Theta = " + degrees(rad) + "\n"
      + "Click to confirm", tipX, tipY);
    break;

  case 3: // waiting for order
    ball.display();
    displayRealtimeInfo(ball);
    text("Press 'F' to shoot out the object", 270, 30);
    drawArrow(int(ball.pos.x), int(ball.pos.y), int(ball.veloc.mag()), -radians(ball.getVelocityAngle()));
    break;

  case 4: // ball fried and traveling
    text("Press 'R' to reset the simulation", 270, 30);
    text(" <-- sry velocity display is incorrect due to some calculation erros :P", 150, 71);
    ++frameCtr;
    displayRealtimeInfo(ball);
    ball.display();
    ball.move();
    break;
  }
}