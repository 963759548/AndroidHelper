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
package com.tuxedoberries.configuration;

/**
 *
 * @author Juan Silva
 */
public class ADBCommands {
    
    /**
     * Command for start ADB server
     */
    public static final String START_COMMAND = "start-server";
    
    /**
     * Command for list all android devices connected
     */
    public static final String DEVICE_LIST_COMMAND = "devices -l";
    
    /**
     * Command for get the device log
     */
    public static final String LOGCAT_COMMAND = "logcat";
    
    /**
     * Command to start recording the screen.
     * This is the solo command, no arguments.
     */
    public static final String SCREEN_RECORD_SOLO_COMMAND = "screenrecord";
    /**
     * Command to start recording the screen
     * The string needs to be formated with the file location
     */
    public static final String SCREEN_RECORD_COMMAND = "shell screenrecord --time-limit %d --verbose %s";
    
    /**
     * Command to create a folder in the device
     * The string needs to be formated with the folder path
     */
    public static final String CREATE_FOLDER = "shell mkdir %s";
    
    /**
     * Command to delete a folder in the device
     * The string needs to be formated with the folder path
     */
    public static final String DELETE_FOLDER = "shell rm -rf %s";
    
    /**
     * Transfer a file from the device to this computer.
     * This is the solo command, no arguments.
     */
    public static final String PULL_FILE_SOLO_COMMAND = "pull";
    
    /**
     * Transfer a file from the device to this computer
     * The string needs to be formated with the device file location and this computer target location
     */
    public static final String PULL_FILE_COMMAND = "pull %s %s";
    
    /**
     * Remove a specific file from the device
     * The string needs to be formated with the device file location
     */
    public static final String REMOVE_FILE_COMMAND = "shell rm %s";
}
