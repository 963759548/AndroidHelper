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
package com.tuxedoberries.androidhelper;

import com.tuxedoberries.configuration.ADBCommands;
import com.tuxedoberries.configuration.ADBConfiguration;
import com.tuxedoberries.process.ProcessController;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Juan Silva
 */
public class DeviceInformationProcessController extends BaseProcessController {
    
    private static final String WINDOW_TITLE = "adb getprop";
    private static final String LOG_FILENAME = "Full Device Information.txt";
    private static final String SUMARY_LOG_FILENAME = "Device Information.txt";
    private DeviceInformation deviceInfo;
    
    @Override
    protected String getWindowTitle() {
        return WINDOW_TITLE;
    }

    @Override
    protected String getLogFileName() {
        return LOG_FILENAME;
    }

    @Override
    protected void doStartProcess() {
        if(deviceInfo == null){
            deviceInfo = new DeviceInformation();
        }
        deviceInfo.Clear();
        
        String devProps = ADBConfiguration.buildADBCommand(ADBCommands.DEVICE_PROPERTIES);
        ProcessController controller = getProcessController();
        // Start Device Properties
        controller.enqueueCommand(devProps);
    }

    @Override
    protected void doStopProcess() {
        ProcessController controller = getProcessController();
        controller.stop();
    }
    
    /**
     * Save the log of the process into the given file
     * @param logfile 
     */
    @Override
    protected void saveLog (String logfile) {
        StringBuilder builder = new StringBuilder();
        // Get Log
        String log = getProcessController().getProcessLog().getLog();
        String[] lines = log.split("\n");
        // Scan for data
        for(int i=0; i<lines.length; ++i) {
            filteredData(lines[i]);
        }
        
        builder.append("Device Information\n");
        builder.append(deviceInfo.toString());
        
        Path path = Paths.get(ADBConfiguration.DEFAULT_DESTINATION_FOLDER, SUMARY_LOG_FILENAME);
        fileWriter.WriteFile(path.toString(), builder.toString());
        super.saveLog(logfile);
    }
    
    private void filteredData (String line) {
        if(line == null)
            return;
        if(line.isEmpty())
            return;
        
        // Manufacturer
        if(line.contains("ro.product.manufacturer")) {
            String result = line.replace("[ro.product.manufacturer]:", "");
            result = result.replace("[", "");
            result = result.replace("]", "");
            deviceInfo.Manufacturer = result.toUpperCase().trim();
            return;
        }
        
        // Model
        if(line.contains("ro.product.model")) {
            String result = line.replace("[ro.product.model]:", "");
            result = result.replace("[", "");
            result = result.replace("]", "");
            deviceInfo.Model = result.toUpperCase().trim();
            return;
        }
        
        // Name
        if(line.contains("ro.product.name")) {
            String result = line.replace("[ro.product.name]:", "");
            result = result.replace("[", "");
            result = result.replace("]", "");
            deviceInfo.Name = result.toUpperCase().trim();
            return;
        }
        
        // SOC
        if(line.contains("ro.board.platform")) {
            String result = line.replace("[ro.board.platform]:", "");
            result = result.replace("[", "");
            result = result.replace("]", "");
            deviceInfo.Platform = result.toUpperCase().trim();
            return;
        }
        
        // Android Version
        if(line.contains("ro.build.version.release")) {
            String result = line.replace("[ro.build.version.release]:", "");
            result = result.replace("[", "");
            result = result.replace("]", "");
            deviceInfo.OSVersion = result.toUpperCase().trim();
        }
        
    }
}
