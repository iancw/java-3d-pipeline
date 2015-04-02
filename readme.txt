The lab is implemented in Java using Swing.  Three dimensional graphics are implemented by drawing onto a BufferedImage, then rendering that as a Swing panel.  It's added a few usability enhancements since lab1, most notably being that you can drag your mouse around in the panel and control the camera's position.  Movement in X and Y on the screen is translated directly into changes in X and Y position of the camera.  The camera's Z component can be adjusted using a mouse wheel.  I've also added the ability to toggle polygon filling and wireframe rendering.  Moving the camera around in wireframe mode is much faster with polygon filling off, so sometimes it's preferable to turn off polygon fill to find the right camera position.  I also added the ability to visualize scan conversion live.  It's much more useful when you step through the code, but I added a toggle in the gui so it's accessible without digging into code.

To add multiple models to a view, simply select multiple files in the file chooser using shift or ctrl-clicking.  The program can also be invoked with multiple model arguments, in which case models will be rotated by pi/8 degrees (which makes many of the models overlap).

Build requirements:
  * A JDK on the system path.  You can download the latest JDK from http://www.oracle.com/technetwork/java/javase/downloads/index.html.

To build:  The jar is built using Java's automated build system "ant."  You will need ant installed on your path to build.  You can obtain and from http://ant.apache.org/bindownload.cgi.

To run:
ant

Example 1: 
	java -jar lab4.jar models/house.d

Example 2: 
	java -jar lab4.jar models/house.d models/house.d models/cow.d

Example 3:
	java -jar lab4.jar 0,0,0 -1,3,-13 0,1,0 1 1 1000 models/house.d models/house.d models/cow.d

Operation:
  Edit settings using the interface, press apply to apply any changed settings and redraw the image.  Clicking and dragging in the canvas causes the camera position to move, holding shift while dragging moves the reference position, and holding alt while dragging moves the light position.  Shift-drag is inverted, and Alt-drag only works on the y axis.  This can be frusturating, but is enough to position items and view significant variation in lighting conditions dynamically.

  Material properties are set using the Ambient, Diffuse, Specular, and Specularity interface elements.  They impact color and illumination.  Each field takes three numbers, representing the values in Red, Green, and Blue channels respectively. 

  Sometimes models show up very small when intially loaded (the scaling isn't consistent between model files).  Decrease h to "zoom."  Often a value of 1 or less is necessary make an object fully fill the screen.
