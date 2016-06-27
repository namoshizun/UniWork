// Control the robot to avoid bouncing balls.
public class Robot extends Mover{

  float health;
  
  public Robot (PVector pos, PVector veloc, PShape shape, float diameter) {
    super(pos, veloc, shape, diameter);
    this.health = 100.0;
  }
  
  public void display() {
    pushMatrix();
    translate(pos.x , pos.y, pos.z);
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