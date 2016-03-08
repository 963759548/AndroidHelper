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

import com.tuxedoberries.utils.FileReader;
import com.tuxedoberries.utils.FileWriter;

/**
 *
 * @author Juan Silva
 */
public class ADBConfiguration {
    
    private static final String CONFIG_FILE = "android_helper.config";
    
    /**
     * Folder where to find ADB
     */
    public static String adbPath = "adb";
    
    /**
     * Default file name for a screen record
     */
    public static final String DEFAULT_FILENAME = "screen_record_%d.mp4";
    
    /**
     * Default folder location for a screen record file
     */
    public static final String DEFAULT_FOLDER_LOCATION = "/sdcard/tmp/";
    
    /**
     * Computer default destination folder
     */
    public static final String DEFAULT_DESTINATION_FOLDER = "./transfers/";
    
    public static void loadConfiguration () {
        FileReader reader = new FileReader();
        if(!reader.fileExist(CONFIG_FILE))
            return;
        adbPath = reader.readFile(CONFIG_FILE);
    }
    
    public static void setAndSaveConfiguration (String path) {
        adbPath = path;
        saveConfiguration();
    }
    
    public static void saveConfiguration () {
        FileWriter writer = new FileWriter();
        writer.WriteFile(CONFIG_FILE, adbPath);
    }
    
    public static String getDefaultScreenRecordName() {
        return getDefaultScreenRecordName(0);
    }
    
    public static String getDefaultScreenRecordName(int number) {
        return String.format(DEFAULT_FILENAME, number);
    }
    
    public static String getDefaultDevicePath () {
        return DEFAULT_FOLDER_LOCATION.concat(getDefaultScreenRecordName());
    }
    
    public static String getDefaultComputerPath () {
        return DEFAULT_DESTINATION_FOLDER.concat(getDefaultScreenRecordName());
    }
    
    public static String buildADBCommand (String command) {
        return adbPath.concat(" ").concat(command);
    }
    
    public static String getDefaultScreenRecordCommand () {
        return String.format(ADBCommands.SCREEN_RECORD_COMMAND, getDefaultDevicePath());
    }
    
    public static String getDefaultPullCommand () {
        return String.format(ADBCommands.PULL_FILE_COMMAND, getDefaultDevicePath(), getDefaultComputerPath());
    }
}
