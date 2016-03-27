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
public class SleepCommand implements ICommandSleep {

	public static final String SLEEP_COMMAND = "sleep";

	private int seconds;

	/**
	 * Get the type of this command
	 * 
	 * @return Type of this command
	 */
	public CommandType getType() {
		return CommandType.Sleep;
	}

	/**
	 * Set the sleep time
	 * 
	 * @param time
	 */
	public void setSleepTime(int time) {
		this.seconds = time;
	}

	/**
	 * Get the full string command
	 * 
	 * @return full command as string
	 */
	public String getCommand() {
		return String.format("%s %d", SLEEP_COMMAND, seconds);
	}
}
