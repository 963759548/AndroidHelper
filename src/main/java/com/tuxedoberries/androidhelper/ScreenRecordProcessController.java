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

/**
 *
 * @author Juan Silva
 */
public class ScreenRecordProcessController extends BaseProcessController {

    private static final String WINDOW_TITLE = "adb shell screenrecord";
    private static final String LOG_FILENAME = "Screen Record Log.txt";
    private int videoCount = 0;
    
    @Override
    protected String getWindowTitle() {
        return WINDOW_TITLE;
    }

    @Override
    protected String getLogFileName() {
        return LOG_FILENAME;
    }

    @Override
    protected void doStartProcess() {
        String createFolder = String.format(ADBCommands.CREATE_FOLDER, ADBConfiguration.DEFAULT_FOLDER_LOCATION);
        // Reset
        videoCount = 0;
        
        ProcessController controller = getProcessController();
        // Create Containing Folder
        controller.execute(ADBConfiguration.buildADBCommand(createFolder));
        for(int i=1; i<=20; ++i) {
            String command = ADBConfiguration.getDefaultScreenRecordCommand(i, ADBConfiguration.currentRecordTime);
            // Execute Record
            controller.enqueueCommand(ADBConfiguration.buildADBCommand(command));
            // Wait a bit for process to properly finish
            controller.enqueueCommand("sleep 2");
        }
    }

    @Override
    protected void doStopProcess() {
        ProcessController controller = getProcessController();
        
        // Clear the queue
        controller.getQueue().clear();
        if(logWindow != null){
            logWindow.onNewLine("Waiting for record to stop");
        }

        // Wait a bit
        controller.enqueueCommand("sleep 2");
        for(int i=1; i<=videoCount; ++i){
            String pullCommand = ADBConfiguration.getDefaultPullScreenRecordCommand(i);
            String rmCommand = ADBConfiguration.getDefaultRemoveCommand(i);
            // Pull File
            controller.enqueueCommand(ADBConfiguration.buildADBCommand(pullCommand));
            // Remove file
            controller.enqueueCommand(ADBConfiguration.buildADBCommand(rmCommand));
            // Wait a bit
            controller.enqueueCommand("sleep 2");
        }
        
        // Delete Temp Folder
        String deleteFolder = String.format(ADBCommands.DELETE_FOLDER, ADBConfiguration.DEFAULT_FOLDER_LOCATION);
        controller.enqueueCommand(ADBConfiguration.buildADBCommand(deleteFolder));
    }
    
    @Override
    protected void processStartedProcess (String process) {
        if(process.contains(ADBCommands.SCREEN_RECORD_SOLO_COMMAND)){
            videoCount++;
        }
    }
    
    @Override
    protected void processLogWindow(LogWindow logWindow) {
        logWindow.setSize(640, 250);
        logWindow.setLocation(logWindow.getLocation().x, 480);
    }
}
