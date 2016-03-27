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

import com.tuxedoberries.configuration.command.CommandFactory;
import com.tuxedoberries.configuration.command.CommandType;
import com.tuxedoberries.configuration.command.ICommand;
import com.tuxedoberries.process.ProcessController;

/**
 *
 * @author Juan Silva
 */
public class LogcatProcessController extends BaseProcessController {

	private static final String WINDOW_TITLE = "adb logcat";
	private static final String LOG_FILENAME = "Device Log.txt";

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
		// Clear the Logs
		ICommand logcatClearCommand = CommandFactory.getInstance().createCommand(CommandType.LogCatClear);
		controller.enqueueCommand(logcatClearCommand.getCommand());
		// Start Logcat
		ICommand logcatCommand = CommandFactory.getInstance().createCommand(CommandType.LogCat);
		controller.enqueueCommand(logcatCommand.getCommand());
	}

	@Override
	protected void doStopProcess() {
		ProcessController controller = getProcessController();
		controller.stop();
	}

}
