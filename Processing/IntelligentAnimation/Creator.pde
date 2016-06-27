public class Creator {

  public Creator() {}
  
  public Mover createMover(TYPE t, Object... materials) {
    switch (t) {
    case BALL :
    return new Ball ( 
        new PVector(int (random(40, bg.width - 40)), int (random(40, bg.height - 40))), // pos
        new PVector(random(-30, 10) + 20, random(-30, 10) + 20), // veloc
        loadImage(pathMoverImg + imgs[(int) random(imgs.length)]), //  apperance
        ballRadius * 2, "Ball");// diameter, name

    case MOUSE_FOLLOWER :
    return new MouseFollower (
        new PVector(mouseX, mouseY), 
        new PVector(0, 0), 
        loadImage(pathMoverImg + imgs[(int) random(imgs.length)]), 
        ballRadius * 2, "MouseBall");
        
    case BODY :
    BodyPoint p = (BodyPoint) materials[0];
    PVector centroid = (PVector) materials[1];
    PImage look = p.c == COLOR.RED ? null : loadImage(sketchPath("") + "Face.png");
    int diameter = p.c == COLOR.RED ? boxRed : boxYellow;
    return new BodyPart(
        new PVector(p.x, p.y), // pos
        new PVector(0, 0), // no speed
        look, diameter, "Body", centroid);

    default: return null;
    }
  }
}