public class BodyPart extends Mover {
  PVector centroid;
  
  public BodyPart (PVector pos, PVector veloc, PImage img, int diameter, String name, PVector centroid) {
    super (pos, veloc, img, diameter, name);
    this.centroid = centroid;
  }

  public void display () {
    pushMatrix();
    translate(pos.x - diameter/2 + offsetX, pos.y - diameter/2 + offsetY);
    PVector tmp = PVector.sub(centroid, pos);
    line(0, 0, tmp.x, tmp.y);
    if (img != null) 
       //ellipse(0, 0 , diameter, diameter);
      image(img, -diameter/2, -diameter/2, diameter, diameter);
    else
      ellipse(0, 0 , diameter, diameter);
    popMatrix();
  }

  public void move () {
    // A BodyMover is a fixed mover that do not have free movement
  }
}