/*
 * Copyright (C) 2016 Juan Silva <juanssl@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tuxedoberries.androidhelper;

import com.tuxedoberries.configuration.ADBCommands;
import com.tuxedoberries.configuration.ADBConfiguration;
import com.tuxedoberries.presentation.LogWindow;
import com.tuxedoberries.process.IProcessStartListener;
import com.tuxedoberries.process.IProcessStopListener;
import com.tuxedoberries.process.ProcessController;
import com.tuxedoberries.utils.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan Silva
 */
public class ProcessStateController implements IProcessStartListener, IProcessStopListener {
    
    private ProcessController logcatProcess;
    private ProcessController screenRecordProcess;
    private Logger logger;
    private LogWindow logcatWindow;
    private LogWindow screenRecordWindow;
    private final FileWriter fileWriter;
    private int videoCount = 0;
    
    public ProcessStateController () {
        createLogger();
        fileWriter = new FileWriter();
    }
    
    public boolean isLogcatRunning () {
        if(logcatProcess == null)
            return false;
        return logcatProcess.isRunning();
    }
    
    public void startLogcatCapture () {
        if(isLogcatRunning()){
            logger.log(Level.INFO, "Logcat is already running");
            return;
        }
        
        // Open Window
        logcatWindow = new LogWindow(false, true);
        logcatWindow.setTitle("adb logcat");
        logcatWindow.setVisible(true);
        
        // Execute
        logcatProcess = new ProcessController();
        String command = ADBConfiguration.buildADBCommand(ADBCommands.LOGCAT_COMMAND);
        // Clear the Logs
        logcatProcess.enqueueCommand(command.concat(" -c"));
        logcatProcess.enqueueCommand(command);
        // Subscribe Window
        logcatProcess.getLogger().subscribeOutput(logcatWindow);  
        logcatProcess.getErrorLogger().subscribeOutput(logcatWindow);
    }
    
    public void stopLogcatCapture () {
        if(!isLogcatRunning()){
            logger.log(Level.INFO, "Logcat is already stopped");
            return;
        }
        // Stop Process
        logcatProcess.stop();
        // Unsubscribe Window
        logcatProcess.getLogger().subscribeOutput(logcatWindow);  
        logcatProcess.getErrorLogger().subscribeOutput(logcatWindow);
        // Save Log
        String log = logcatWindow.getFullText();
        fileWriter.WriteFile(ADBConfiguration.DEFAULT_DESTINATION_FOLDER.concat("Log.txt"), log);
        logcatProcess = null;
        
        // Dispose Window
        logcatWindow.dispose();
        logcatWindow = null;
    }
    
    public boolean isScreenRecordRunning () {
        if(screenRecordProcess == null)
            return false;
        return screenRecordProcess.isRunning();
    }
    
    public void startScreenRecord () {
        if(isScreenRecordRunning()){
            logger.log(Level.INFO, "Screen Record is already running");
            return;
        }
        screenRecordWindow = new LogWindow(false, true);
        screenRecordWindow.setSize(640, 250);
        screenRecordWindow.setLocation(screenRecordWindow.getLocation().x, 480);
        screenRecordWindow.setTitle("adb shell screenrecord");
        screenRecordWindow.setVisible(true);
        
        // Set some variables
        videoCount = 0;
        
        // Execute
        screenRecordProcess = new ProcessController();
        String createFolder = String.format(ADBCommands.CREATE_FOLDER, ADBConfiguration.DEFAULT_FOLDER_LOCATION);
        screenRecordProcess.execute(ADBConfiguration.buildADBCommand(createFolder));
        for(int i=1; i<=20; ++i) {
            String command = ADBConfiguration.getDefaultScreenRecordCommand(i);
            screenRecordProcess.enqueueCommand(ADBConfiguration.buildADBCommand(command));
            screenRecordProcess.enqueueCommand("sleep 2");
        }
        
        // Subscribe Window
        screenRecordProcess.getLogger().subscribeOutput(screenRecordWindow);  
        screenRecordProcess.getErrorLogger().subscribeOutput(screenRecordWindow);
        screenRecordProcess.getObserver().subscribeOnStart(this);
        screenRecordProcess.getObserver().subscribeOnStop(this);
    }
    
    public void stopScreenRecord () {
        if(!isScreenRecordRunning()){
            logger.log(Level.WARNING, "Screen Record is already stopped");
            return;
        }
        
        // Stop Currents
        screenRecordWindow.onNewLine("Waiting to finish last recording");
        screenRecordProcess.clearQueue();
        screenRecordProcess.enqueueCommand("sleep 2");
        for(int i=1; i<=videoCount; ++i){
            String pullCommand = ADBConfiguration.getDefaultPullCommand(i);
            String rmCommand = ADBConfiguration.getDefaultRemoveCommand(i);
            screenRecordProcess.enqueueCommand(ADBConfiguration.buildADBCommand(pullCommand));
            screenRecordProcess.enqueueCommand(ADBConfiguration.buildADBCommand(rmCommand));
            screenRecordProcess.enqueueCommand("sleep 2");
        }
    }

    @Override
    public void onProcessStarted(String process) {
        if(process.contains(ADBCommands.SCREEN_RECORD_SOLO_COMMAND)){
            videoCount++;
        }
    }
    
    @Override
    public void onProcessStopped(String process) {
        if(process.contains(ADBCommands.PULL_FILE_SOLO_COMMAND)){
            --videoCount;
            if(videoCount <= 0){
                if(screenRecordWindow != null){
                    // Save Log
                    String log = screenRecordWindow.getFullText();
                    fileWriter.WriteFile(ADBConfiguration.DEFAULT_DESTINATION_FOLDER.concat("ScrenRecordLog.txt"), log);

                    // Dispose Window
                    screenRecordWindow.dispose();
                    screenRecordWindow = null;                    
                }
            }
        }
    }
    
    private void createLogger () {
        if(logger == null) {
            String loggerName = String.format("[%d]%s", this.hashCode(), ProcessController.class.getName());
            logger = Logger.getLogger(loggerName);
        }
    }

}
