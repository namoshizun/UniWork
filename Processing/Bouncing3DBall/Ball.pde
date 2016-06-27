public class Ball extends Mover{
  int id;
  float mass;
  float spinSpeed;
  
  public Ball (PVector pos, PVector veloc, PShape shape, float diameter, float mass) {
    super(pos, veloc, shape, diameter);
   
    this.mass = mass;
    this.spinSpeed = 10.0;
  }
  
  public void display() {
    pushMatrix();
    translate(pos.x , pos.y, pos.z);
   // rotateX(frameCtr * PI * (veloc.x / 1000.0));
   // rotateY(frameCtr * PI * (veloc.y / 1000.0));
   // rotateZ(frameCtr * PI * (veloc.z / 1000.0));
    shape(shape);
    popMatrix();
  }

  public void move() {
    pos.add(veloc);
    
    // bounce back if the ball reaches any boundary
    if (reachedXedge(boxSize)) {
      pos.x = pos.x - veloc.x;
      veloc.x = veloc.x * FRICTION;
    }
    if (reachedYedge(boxSize)) {
      pos.y = pos.y - veloc.y;
      veloc.y = veloc.y * FRICTION;
      
      if (Math.abs(veloc.y) <= 0.15) {
        veloc.x = veloc.x * 0.99; // friction coefficient
        veloc.z = veloc.z * 0.99;
      }
    }
    if (reachedZedge(boxSize)) {
      pos.z = pos.z - veloc.z;
      veloc.z = veloc.z * FRICTION;
    }
 
    // update velocity vector according to gravity
    veloc.y = veloc.y + GRAVITY;
  }

  public void interact(Mover other) {
    bounce(other);
  }
}