public class BodyPoint extends PVector {
  COLOR c;
  
  public BodyPoint(int x, int y, COLOR c) {
    super(x, y);
    this.c = c;
  }
}

// GIF animation
public class AnimationPoint extends PVector {
  int imgSize;
  int imgNum; // total number of images of this animation
  PImage[] frames;
  int frameCtr;
  boolean isFinished;

  public AnimationPoint(int x, int y, PImage[] frames, int imgSize) {
    super(x, y);
    this.frames = frames;
    this.imgNum = frames.length;
    this.imgSize = imgSize;
    this.frameCtr = 0;
    this.isFinished = false;
  }

  // display the animation frame, control the lifetime of itself
  public void display() {
    image(frames[frameCtr % imgNum], x - imgSize/2, y - imgSize/2, imgSize, imgSize);
    ++frameCtr;

    // the animation is completed, revemoe self from the list.
    if (frameCtr == imgNum) {
      this.isFinished = true;
    }
  }

  public boolean finished() {
    return this.isFinished;
  }
}