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
import com.tuxedoberries.process.ProcessController;

/**
*
* @author Juan Silva
*/
public class ScreenCaptureProcessController extends BaseProcessController {
	
	private static final String WINDOW_TITLE = "adb shell screencapture";
    private static final String LOG_FILENAME = "Capture Log.txt";
    private int screenCaptureCount = 0;
    
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
        ProcessController controller = getProcessController();
        
        // Create Containing Folder
        String createFolderCommand = String.format(ADBCommands.CREATE_FOLDER, ADBConfiguration.DEFAULT_FOLDER_LOCATION);
        controller.enqueueCommand(ADBConfiguration.buildADBCommand(createFolderCommand));
        
        // Capture the screenshot
        String captureCommand = ADBConfiguration.getDefaultScreenCaptureCommand(screenCaptureCount);
        controller.enqueueCommand(ADBConfiguration.buildADBCommand(captureCommand));
     
        // Pull the file
        String pullCommand = ADBConfiguration.getDefaultPullScreenCaptureCommand(screenCaptureCount);
        controller.enqueueCommand(ADBConfiguration.buildADBCommand(pullCommand));
        ++screenCaptureCount;
    }

    @Override
    protected void doStopProcess() {
        ProcessController controller = getProcessController();
        controller.stop();
    }
}
