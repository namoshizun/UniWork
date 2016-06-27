public abstract class Mover {
  PVector pos;
  PVector veloc;
  PImage img;
  String name;
  int id;
  int diameter; //FIXME: fake, assuming that Mover is always a ball

  public Mover(PVector pos, PVector veloc, PImage img, int diameter, String name) {
    this.pos = pos;
    this.veloc = veloc;
    this.img = img;
    this.diameter = diameter;
    this.name = name;
  }
  abstract public void display();
  abstract public void move();

  public void interact() {
    PVector touch = null;
    // i = id+1 to avoid duplicate interaction
    for (int i = id + 1; i < freeMoversN; ++i) { 
      // 1, update velocity and position
      Mover other = freeMovers.get(i);
      PVector sub = PVector.sub(other.pos, pos);
      touch = PVector.add(pos, sub.div(2));

      if (pos.dist(other.pos) <= diameter) {
        float angle = atan2(sub.y, sub.x);
        float targetX = pos.x + cos(angle) * diameter;
        float targetY = pos.y + sin(angle) * diameter;
        float ax = targetX - other.pos.x;
        float ay = targetY - other.pos.y;
        veloc.x -= ax;
        veloc.y -=ay;
        other.veloc.x +=ax;
        other.veloc.y += ay;

        triggerSpecialEffects(other, touch);
      }
    }

    for (Mover p : bodyParts) {
      PVector tmp = PVector.add(p.pos, new PVector(offsetX, offsetY));
      if (pos.dist(tmp) <= diameter/2 + p.diameter/2) {
        touch = PVector.add(pos, PVector.sub(tmp, pos).div(2));
        triggerSpecialEffects(p, touch);
      }
    }
  }

  public void triggerSpecialEffects(Mover other, PVector posInteraction) {
    // create bombing event
    // PVector touch = PVector.add(pos, PVector.sub(other.pos, pos).div(2));
    animations.add(new AnimationPoint(round(posInteraction.x), round(posInteraction.y), animtImgs, 70));
    // exchange img
    if (other.img != null) {
      PImage tmp = img.copy();
      img = other.img.copy();
      other.img = tmp.copy();
    }
    // make it loud!
    playSoundEffect(pathSound + "explosion.wav");
  }

  // HELPER METHODS
  public boolean reachedXedge(float xWidth) {
    return (pos.x + diameter/2 > xWidth) || (pos.x - diameter/2 <0);
  }

  public boolean reachedYedge(float yHeight) {
    return (pos.y + diameter/2 > yHeight) || (pos.y - diameter/2 < 0);
  }
}