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
public class ScreenRecordCommand extends BaseShellCommand implements ICommandRecord {

	/**
	 * Command to start recording the screen.
	 */
	public static final String SCREEN_RECORD_COMMAND = "screenrecord";

	private String destinationPath;
	private int timeLimit;
	private int bitRate;

	public void setPath(String path) {
		destinationPath = path;
	}

	public CommandType getType() {
		return CommandType.ScreenRecord;
	}

	public void setTimeLimit(int seconds) {
		this.timeLimit = seconds;
	}

	public void setBitRate(int bitRate) {
		this.bitRate = bitRate;
	}

	public String getCommand() {
		return format("%s --verbose --time-limit %d --bit-rate %d %s", SCREEN_RECORD_COMMAND, timeLimit, bitRate,
				destinationPath);
	}

}
