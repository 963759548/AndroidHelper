# Android Helper

Android Helper is a small, yet useful, tool aimed for QA or developers who want to gather the device log, screen record, screenshots and device information. With one click you can get all the information that you need with ease.

![Main Window](/docs/Main.png)

## Configuration

Double click the java Jar file; a window will appear. The first and only parameter that the application need is the path of you [adb](http://developer.android.com/tools/help/adb.html) executable. You can enter the path manually or click the *choose* button. After that you can click Save and you are all set.

![File Chooser](/docs/FileChooser.png)

## Usage

The application can be configured to gather the following data from the device while you execute your apps. Everything will be executed in parallel and you can set to see the actual output in different windows or hide the windows.
1. Device Log
2. Screen Record
2. Screen Capture
3. Device Information

![Run Example](/docs/RunExample.png)

### Device Log

Executes [*logcat*](http://developer.android.com/tools/help/logcat.html) so you can get all the output from the standard output and standard error from the device.

### Screen Record

Executes [*shell screenrecord*](http://developer.android.com/tools/help/shell.html#screenrecord) that will record the display with no sound and only for 3 minutes tops. Gladly this application once the time limit has reached it will start to record another video as many times as you want until you click stop (Well the limit is 20 videos which is about 1 hour, plenty of time).

### Screen Capture

Executes [*shell screencap*](http://developer.android.com/tools/help/shell.html#screencap) that will take a screenshot of a device display. You can press the *Capture Screen* button at any time and get the picture taken.

### Device Information

Executes [*shell getprop*](http://adbshell.com/commands/adb-shell-getprop) to get the properties of the system like manufacturer, model, platform name, OS version among others.

## Results

After the execution all the data will be stored in *transfers* folder. Be aware that further executions will replace any current file inside this folder.

![Result Example](/docs/FilesExample.png)

### Version
0.10.15b

### Copyright
Copyright (c) [Juan Silva](mailto:juanssl@gmail.com) All rights reserved
