public class Ball extends Mover {

  public Ball (PVector pos, PVector veloc, PImage img, int diameter, String name) {
    super (pos, veloc, img, diameter, name);
    id = freeMoversN;
  }

  public void display () {
    pushMatrix();
    translate(pos.x - diameter/2, pos.y - diameter/2);
    image(img, 0, 0, diameter, diameter);
    popMatrix();
  }

  public void move () {
    pos.add(veloc);

    // bounce bakc if the ball reaches any boundary
    if (reachedXedge(bg.width)) {
      pos.x -= veloc.x;
      veloc.x *= -1;
    }

    if (reachedYedge(bg.height)) {
      pos.y -= veloc.y;
      veloc.y *= -1;
    }

    // Randomize the velocity
    veloc.x = random(-20, 20);
    veloc.y = random(-20, 20);
  }
}