import beads.*;
AudioContext ac;
SamplePlayer sp;
Gain g;
Glide gainValue;
// Acknowledment:  COMP3419_2016_Lab08 

void playSoundEffect(String sourceFile) {
  ac= new AudioContext();
  try {
    sp = new SamplePlayer(ac, new Sample(sourceFile));
  } catch (Exception e ) {
    println("Cannot load the sound effect audio");
    e.printStackTrace();
    exit();
  }
  
  sp.setKillOnEnd(true);
  gainValue = new Glide(ac, 0.0, 20);
  g = new Gain(ac, 1, gainValue);
  g.addInput(sp);
  ac.out.addInput(g);
  ac.start(); // begin audio processing
  
  gainValue.setValue(0.9);
  sp.setToLoopStart();
  sp.start(); // play!
}