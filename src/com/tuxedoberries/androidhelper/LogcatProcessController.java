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
public class LogcatProcessController extends BaseProcessController {

    private static final String WINDOW_TITLE = "adb logcat";
    private static final String LOG_FILENAME = "Logcat.txt";
    
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
        String command = ADBConfiguration.buildADBCommand(ADBCommands.LOGCAT_COMMAND);
        
        ProcessController controller = getProcessController();
        // Clear the Logs
        controller.enqueueCommand(command.concat(" -c"));
        // Start Logcat
        controller.enqueueCommand(command);
    }

    @Override
    protected void doStopProcess() {
        ProcessController controller = getProcessController();
        controller.stop();
    }
    
}
