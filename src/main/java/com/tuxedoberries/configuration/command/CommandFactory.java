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

import java.util.HashMap;

/**
 * Command Factory
 * 
 * @author Juan Silva
 */
public class CommandFactory {

	/**
	 * Single Instance
	 */
	private static CommandFactory _instance;

	/**
	 * Single Access to instance
	 * 
	 * @return
	 */
	public static CommandFactory getInstance() {
		if (_instance == null) {
			_instance = new CommandFactory();
		}
		return _instance;
	}

	private HashMap<CommandType, ICommand> commandSet;

	/**
	 * Private Constructor
	 */
	private CommandFactory() {
		commandSet = new HashMap<CommandType, ICommand>();

		// Create All
		commandSet.put(CommandType.LogCat, new LogcatCommand());
		commandSet.put(CommandType.LogCatClear, new LogcatClearCommand());
		commandSet.put(CommandType.DeviceList, new DeviceListCommand());
		commandSet.put(CommandType.StartServer, new StartServerCommand());
		commandSet.put(CommandType.ScreenCapture, new ScreenCaptureCommand());
		commandSet.put(CommandType.MakeDirectory, new MKDirCommand());
		commandSet.put(CommandType.RemoveFile, new RemoveFileCommand());
		commandSet.put(CommandType.RemoveFolder, new RemoveFolderCommand());
		commandSet.put(CommandType.ScreenRecord, new ScreenRecordCommand());
		commandSet.put(CommandType.Pull, new PullCommand());
		commandSet.put(CommandType.GetProperties, new GetPropertiesCommand());
		commandSet.put(CommandType.DumpSystem, new DumpSystemCommand());
	}

	/**
	 * Create the given command type. The command will not need any other
	 * parameter to work.
	 * 
	 * @param type
	 * @return The given command. Return null if the command needs more
	 *         arguments
	 */
	public ICommand createCommand(CommandType type) {
		if (commandSet.containsKey(type)) {
			return commandSet.get(type);
		}

		return null;
	}

	public ICommand createCommand(CommandType type, String path) {
		if (commandSet.containsKey(type)) {
			ICommandPath command = (ICommandPath) commandSet.get(type);
			command.setPath(path);
			return command;
		}

		return null;
	}
	
	public ICommand createCommand(CommandType type, String path, int seconds, int bitRate) {
		if (commandSet.containsKey(type)) {
			ICommandRecord command = (ICommandRecord) commandSet.get(type);
			command.setPath(path);
			command.setBitRate(bitRate);
			command.setTimeLimit(seconds);
			return command;
		}

		return null;
	}

	/**
	 * Create the given command type. The command will need a source path and a
	 * destination path.
	 * 
	 * @param type
	 * @param source
	 * @param destination
	 * @return The given command. Return null if the command needs more
	 *         arguments
	 */
	public ICommand createCommand(CommandType type, String source, String destination) {
		if (commandSet.containsKey(type)) {
			ICommandTransfer command = (ICommandTransfer) commandSet.get(type);
			command.setPath(source);
			command.setDestinationPath(destination);
			return command;
		}

		return null;
	}
}
