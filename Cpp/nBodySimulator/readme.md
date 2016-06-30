# N-body simulator

##Introduction
This is a Planetarium/N-body Simulation program originially completed as an assignment of the USYD course INFO3220(Oject oriented design). It is developed using Qt and i would love to keep building it to make it a nice looking simulator  :)  

Design patterns: Sigleton, Factory, Composite, Memento, Visitor

##Installation
Download all necessary files and open .pro in Qt Creator, remember to place config.txt under the working directory.

##Usage
<img src = "https://raw.githubusercontent.com/namoshizun/myBase/master/Cpp/nBodySimulator/usage.png"/>

##Config
Default simulation settings as well as planet information can be customised in config.txt file.
Note that there are two types of headings for simulation objects;<br />
<br />
**System Object**
Begin with [solarsystem/galaxy/cluster]
_Format_: <br />
  name = blah <br />
  parent = parent_name // optional <br />
  // pos and vec are only applicable to solarsystems or galaxies
  position_x  = position is relative to its parent system, so is the velocity  <br />
  position_y  = set to 0 means the system will start moving from the center of view, which can be modifed during simulation  <br />
  velocity_x  = .. <br />
  velocity_y  = .. <br />

**Primitive Body Object**:
Begin with [planet/star/blackhole] <br />
_Format_ : <br />
  name = ... <br />
  position_x = also relative to its parent system <br />
  position_y = ... <br />
  velocity_x = ... <br />
  velocity_y = ...  <br />
  radius = eg. 1.185e6<br />
  mass = eg.0.0146e24<br />
  color = hex color code  eg. #FF6600 //dark orange<br />
  parent = ...<br />

##TODO
  - conversion between 3D and 2D by button, may need to implement strategy design pattern
  - display planet trajectories
  - apply texture?
