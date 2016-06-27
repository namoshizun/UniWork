public abstract class Mover{
  PVector pos;
  PVector veloc;
  PShape shape;
  float diameter;
  int id;
  
  public Mover (PVector pos, PVector veloc, PShape shape, float diameter) {
    this.pos = pos;
    this.veloc = veloc;
    this.shape = shape;
    this.diameter = diameter;
    
    id = numMovers;
  }
  
  abstract public void display();
  abstract public void move();
  abstract public void interact(Mover other);

  // MAGIC! Movers are bouncing back if they touch each other.
  public void bounce(Mover other) {
    PVector ab = PVector.sub(this.pos, other.pos).normalize();
    PVector n = new PVector();
    n.set(ab);
    PVector u = PVector.sub(this.veloc, other.veloc);
    PVector un = componentVector(u, n);
    u.sub(un);

    while (this.pos.dist(other.pos) < (this.diameter/2 + other.diameter/2)) {
      this.pos.add(ab);
    }

    this.veloc = PVector.add(u, other.veloc);
    other.veloc = PVector.add(un, other.veloc);
  }

  PVector componentVector (PVector velocV, PVector directionV) {
    directionV.normalize();
    directionV.mult(velocV.dot(directionV));
    return directionV;
  }

  // HELPER METHODS
  public boolean reachedXedge (float xWidth) 
  { return (pos.x + diameter/2 >= xWidth / 2) || (pos.x - diameter/2 <= -xWidth / 2); }
  public boolean reachedYedge (float yWidth) 
  { return (pos.y + diameter/2 >= yWidth / 2) || (pos.y - diameter/2 <= -yWidth / 2); }
  public boolean reachedZedge (float zWidth) 
  { return (pos.z + diameter/2 >= zWidth / 2) || (pos.z - diameter/2 <= -zWidth / 2); }
}