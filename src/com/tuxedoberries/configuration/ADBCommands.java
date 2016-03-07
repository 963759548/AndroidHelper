/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuxedoberries.configuration;

/**
 *
 * @author jsilva
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
     * Command to start recording the screen
     * The string needs to be formated with the file location
     */
    public static final String SCREEN_RECORD_COMMAND = "shell screenrecord --verbose %s";
    
    /**
     * Transfer a file from the device to this computer
     * The string needs to be formated with the device file location and this computer target location
     */
    public static final String PULL_FILE_COMMAND = "pull %s %s";
}
