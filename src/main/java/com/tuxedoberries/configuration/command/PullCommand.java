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
 * @author Juan Silva
 */
public class PullCommand extends BaseADBCommand implements ICommandTransfer {

	/**
	 * Transfer a file from the device to this computer.
	 */
	public static final String PULL_FILE_SOLO_COMMAND = "pull";

	private String sourcePath;
	private String destinationPath;

	/**
	 * Set the source path where to retrieve the file
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		sourcePath = path;
	}

	/**
	 * Set the destination path where to put the retrieved file
	 * 
	 * @param path
	 */
	public void setDestinationPath(String path) {
		destinationPath = path;
	}

	/**
	 * Get the type of this command
	 * 
	 * @return Type of this command
	 */
	public CommandType getType() {
		return CommandType.Pull;
	}

	/**
	 * Get the full string command
	 * 
	 * @return full command as string
	 */
	public String getCommand() {
		return format("%s %s %s", PULL_FILE_SOLO_COMMAND, sourcePath, destinationPath);
	}
}
