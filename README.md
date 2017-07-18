# marf_android_lib
android MARF library.
This is a try for make MARF works on Android.

# First: 
I have no experiences with MARF, so I can't garenteed that it's work as perfect as the PC version.

# How I made it work:
<ul>
  <li>The main problem is "javax.sound" package that it used by MARF; but in android it is not compete, so I use extern package from the project AndroidMaryTTS.</li>
  <li>The same with some other classes.</li>
</ul>

# Important note:
You will need to set "TrainingSet.WORKING_PATH" to your application path.
<br>
# Finally 
I test it with the SpeakerIdentApp in android 2.3.6 and android 6.0 and it work fine. 
