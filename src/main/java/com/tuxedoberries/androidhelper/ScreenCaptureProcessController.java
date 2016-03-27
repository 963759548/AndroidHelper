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

import com.tuxedoberries.configuration.ADBConfiguration;
import com.tuxedoberries.configuration.command.CommandFactory;
import com.tuxedoberries.configuration.command.CommandType;
import com.tuxedoberries.configuration.command.ICommand;
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
		ICommand makeDirectory = CommandFactory.getInstance().createCommand(CommandType.MakeDirectory,
				ADBConfiguration.DEFAULT_SOURCE_FOLDER);
        controller.enqueueCommand(makeDirectory.getCommand());
        
        // Capture the screenshot
        String sourcePath = ADBConfiguration.getDefaultDeviceScreenCapturePath(screenCaptureCount);
        ICommand screenCaptureCommand = CommandFactory.getInstance().createCommand(CommandType.ScreenCapture, sourcePath);
        controller.enqueueCommand(screenCaptureCommand.getCommand());
     
        // Pull the file
    	String destinationPath = ADBConfiguration.getDefaultScreenCaptureComputerPath(screenCaptureCount);
    	ICommand pullCommand = CommandFactory.getInstance().createCommand(CommandType.Pull, sourcePath, destinationPath);
        controller.enqueueCommand(pullCommand.getCommand());
        
        // Remove the file
        ICommand removeCommand = CommandFactory.getInstance().createCommand(CommandType.RemoveFile, sourcePath);
        controller.enqueueCommand(removeCommand.getCommand());
        
        ++screenCaptureCount;
    }

    @Override
    protected void doStopProcess() {
        ProcessController controller = getProcessController();
        controller.stop();
    }
}
