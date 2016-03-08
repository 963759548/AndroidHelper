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
import com.tuxedoberries.process.ProcessController;
import com.tuxedoberries.utils.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan Silva
 */
public class ProcessStateController {
    
    private ProcessController logcatProcess;
    private ProcessController screenRecordProcess;
    private Logger logger;
    private LogWindow logcatWindow;
    private LogWindow screenRecordWindow;
    private final FileWriter fileWriter;
    
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
        // Dispose Window
        logcatWindow.dispose();
        logcatWindow = null;
        
        // Stop Process
        logcatProcess.stop();
        // Unsubscribe Window
        logcatProcess.getLogger().subscribeOutput(logcatWindow);  
        logcatProcess.getErrorLogger().subscribeOutput(logcatWindow);
        // Save Log
        String log = logcatProcess.getLogger().getLog();
        fileWriter.WriteFile(ADBConfiguration.DEFAULT_DESTINATION_FOLDER.concat("log.txt"), log);
        logcatProcess = null;
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
        screenRecordWindow.setTitle("adb shell screenrecord");
        screenRecordWindow.setVisible(true);
        
        // Execute
        screenRecordProcess = new ProcessController();
        String createFolder = String.format(ADBCommands.CREATE_FOLDER, ADBConfiguration.DEFAULT_FOLDER_LOCATION);
        String command = ADBConfiguration.getDefaultScreenRecordCommand();
        screenRecordProcess.enqueueCommand(ADBConfiguration.buildADBCommand(createFolder));
        screenRecordProcess.enqueueCommand(ADBConfiguration.buildADBCommand(command));
        
        // Subscribe Window
        screenRecordProcess.getLogger().subscribeOutput(screenRecordWindow);  
        screenRecordProcess.getErrorLogger().subscribeOutput(screenRecordWindow);
    }
    
    public void stopScreenRecord () {
        if(!isScreenRecordRunning()){
            logger.log(Level.WARNING, "Screen Record is already stopped");
            return;
        }
        
        // Stop Currents
        screenRecordProcess.stop();
        String command = ADBConfiguration.getDefaultPullCommand();
        screenRecordProcess.enqueueCommand("echo 'Waiting record'");
        screenRecordProcess.enqueueCommand("sleep 3");
        screenRecordProcess.enqueueCommand(ADBConfiguration.buildADBCommand(command));
        screenRecordProcess.getLogger().subscribeOutput(screenRecordWindow);  
        screenRecordProcess.getErrorLogger().subscribeOutput(screenRecordWindow);
    }
    
    private void createLogger () {
        if(logger == null) {
            String loggerName = String.format("[%d]%s", this.hashCode(), ProcessController.class.getName());
            logger = Logger.getLogger(loggerName);
        }
    }
}
