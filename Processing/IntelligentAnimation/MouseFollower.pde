public class MouseFollower extends Mover {

  public MouseFollower (PVector pos, PVector veloc, PImage img, int diameter, String name) {
    super (pos, veloc, img, diameter, name);
    id = freeMoversN;
  }

  public void display () {
    pushMatrix();
    translate(pos.x - diameter/2, pos.y - diameter/2);
    image(img, 0, 0, diameter, diameter);
    text("I'm controlled by mouse!", -1, -1);
    popMatrix();
  }

  public void move () {
    PVector diff = PVector.sub(new PVector(mouseX, mouseY), pos);
    veloc = diff.mult(0.6);
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
  }
}