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
package com.tuxedoberries.configuration.command;

/**
 *
 * @author Juan Silva
 */
public class ScreenCaptureCommand extends BaseShellCommand implements ICommandPath {

	/**
	 * Command to capture the screen.
	 */
	public static final String SCREEN_CAPTURE_COMMAND = "screencap";

	private String destinationPath;

	/**
	 * Set the path that this Command needs
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		destinationPath = path;
	}

	/**
	 * Get the type of this command
	 * 
	 * @return Type of this command
	 */
	public CommandType getType() {
		return CommandType.ScreenCapture;
	}

	/**
	 * Get the full string command
	 * 
	 * @return full command as string
	 */
	public String getCommand() {
		return format("%s %s", SCREEN_CAPTURE_COMMAND, destinationPath);
	}
}
