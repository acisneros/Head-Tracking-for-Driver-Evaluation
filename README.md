== a-Head of the Curve: Head tracking for Driver Evaluation ==

=== Expert Model Information ===

In this folder, you will find the ExpertModel.java file, which contains the logic for evaluating the driver’s performance. To run this file, open Console or command and:

1. Compile the java file with javac: javac ExpertModel.java
2. Run the file with the ExpertModel.txt file, and the test.txt file: java ExpertModel ExpertModel.txt test.txt

This will compare the test file output by the Kinect to the expert model. If the test file is close enough to the expert model, it will average the two, and the result becomes the new expert model. The old expert model file is simply overwritten. There is a copy of the original expert model file in the ExpertModelCreation folder, in case the file in this folder becomes corrupt, deleted, or skewed too far from the original due to repeated averaging.


=== Kinect Information ===

All of the code for the Kinect is contained in the FactTrackingBasics-WPF, Microsoft.Kinect.Toolkit, and Microsoft.Kinect.Toolkit.FaceTracking folders. To run this code, you must have Microsoft Visual Studio installed. Once installed, navigate to the FaceTrackingBasics folder, and click on the FaceTrackingBasics solution file (.suo) to open it. This will open all of the files in the Visual Studio environment. The file containing the X and Y variables and the code to output is found in the Microsoft.Kinect.Toolkit.FaceTracking folder, titles FaceTracker.cs. Scroll to the bottom of the file to see the logging code. 

To start the head movement logging, click on the play button next to the word “Debug” in the Visual Studio environment. A window will appear with the Kinect camera view. The camera works best if you are about 3-5 feet away from it. Adjust yourself towards and away from the camera until you see a yellow grid appear on your face. The grid indicates the Kinect is successfully tracking your face. If the grid disappears, the Kinect will log zeroes for the X and Y output. the logging is completed by closing the window.

== Other Information ==
The “Head Movement Script” picture file is slightly inaccurate; all directions that say 1 second were actually recorded as 2 seconds in the audio file. 