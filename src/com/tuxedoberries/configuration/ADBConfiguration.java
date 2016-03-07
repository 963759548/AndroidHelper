/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuxedoberries.configuration;

import com.tuxedoberries.utils.FileReader;
import com.tuxedoberries.utils.FileWriter;

/**
 *
 * @author jsilva
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
    public static final String DEFAULT_FILENAME = "screen_record.mp4";
    
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
    
    public static void saveConfiguration () {
        FileWriter writer = new FileWriter();
        writer.WriteFile(CONFIG_FILE, adbPath);
    }
    
    public static String getDefaultDevicePath () {
        return DEFAULT_FOLDER_LOCATION.concat(DEFAULT_FILENAME);
    }
    
    public static String getDefaultComputerPath () {
        return DEFAULT_DESTINATION_FOLDER.concat(DEFAULT_FILENAME);
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
