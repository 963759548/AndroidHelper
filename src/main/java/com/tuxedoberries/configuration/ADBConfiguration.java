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
import java.nio.file.Path;
import java.nio.file.Paths;

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
     * Current Video Rate
     */
    public static int currentBitRate = 4000000;
    
    /**
     * Current configured record time
     */
    public static int currentRecordTime = 180;
    
    /**
     * Max video record time
     */
    public static final int MAX_RECORD_TIME = 180;
    
    /**
     * Max Bit Rate
     */
    public static final int MAX_RECORD_BIT_RATE = 10000000;
    
    /**
     * Default file name for a screen record
     */
    public static final String DEFAULT_SCREEN_RECORD_FILENAME = "screen_record_%d.mp4";
    
    /**
     * Default file name for a screen capture
     */
    public static final String DEFAULT_SCREEN_CAPTURE_FILENAME = "screen_capture_%d.png";
    
	/**
	 * Default folder location for a screen record file
	 */
	public static final String DEFAULT_SOURCE_FOLDER = "/sdcard/tmp";

	/**
	 * Computer default destination folder
	 */
	public static final String DEFAULT_DESTINATION_FOLDER = "transfers";
	
    
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
        return String.format(DEFAULT_SCREEN_RECORD_FILENAME, number);
    }
    
    public static String getDefaultScreenCaptureName() {
        return getDefaultScreenRecordName(0);
    }
    
    public static String getDefaultScreenCaptureName(int number) {
        return String.format(DEFAULT_SCREEN_CAPTURE_FILENAME, number);
    }
    
    public static String getDefaultDeviceScreenRecordPath (int index) {
        return String.format("%s/%s", DEFAULT_SOURCE_FOLDER, getDefaultScreenRecordName(index));
    }
    
    public static String getDefaultDeviceScreenRecordPath () {
    	return String.format("%s/%s", DEFAULT_SOURCE_FOLDER, getDefaultScreenRecordName());
    }
    
    public static String getDefaultDeviceScreenCapturePath (int index) {
    	return String.format("%s/%s", DEFAULT_SOURCE_FOLDER, getDefaultScreenCaptureName(index));
    }
    
    public static String getDefaultDeviceScreenCapturePath () {
    	return String.format("%s/%s", DEFAULT_SOURCE_FOLDER, getDefaultScreenCaptureName());
    }
    
    public static String getDefaultScreenRecordComputerPath (int index) {
        Path path = Paths.get(DEFAULT_DESTINATION_FOLDER, getDefaultScreenRecordName(index));
        return path.toString();
    }
    
    public static String getDefaultScreenRecordComputerPath () {
        Path path = Paths.get(DEFAULT_DESTINATION_FOLDER, getDefaultScreenRecordName());
        return path.toString();
    }
    
    public static String getDefaultScreenCaptureComputerPath (int index) {
        Path path = Paths.get(DEFAULT_DESTINATION_FOLDER, getDefaultScreenCaptureName(index));
        return path.toString();
    }
    
    public static String getDefaultScreenCaptureComputerPath () {
        Path path = Paths.get(DEFAULT_DESTINATION_FOLDER, getDefaultScreenCaptureName());
        return path.toString();
    }
    
}
