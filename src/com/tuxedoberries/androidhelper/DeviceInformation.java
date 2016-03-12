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

/**
 *
 * @author Juan Silva
 */
public class DeviceInformation {
    
    public String Manufacturer;
    public String Model;
    public String Name;
    public String Platform;
    public String OSVersion;
    public String RAM;
    public String GPUInformation;
    public String ScreenWidth;
    public String ScreenHeight;
    
    public void Clear() {
        Manufacturer = "";
        Model = "";
        Name = "";
        Platform = "";
        OSVersion = "";
        RAM = "";
        GPUInformation = "";
        ScreenWidth = "";
        ScreenHeight = "";
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Manufacturer:\t");
        builder.append(Manufacturer);
        builder.append("\n");
        
        builder.append("Model:\t\t");
        builder.append(Model);
        builder.append("\n");
        
        builder.append("Name:\t\t");
        builder.append(Name);
        builder.append("\n");
        
        builder.append("Platform:\t");
        builder.append(Platform);
        builder.append("\n");
        
        builder.append("OS Version:\t");
        builder.append(OSVersion);
        builder.append("\n\n");
        
        builder.append("Platform Information: \t");
        String platformInfo = "http://system-on-a-chip.specout.com/?launch_filters=%5B%7B%22field%22%3A%22manufact_series_model_pregen%22%2C%22operator%22%3A%22LIKE%22%2C%22value%22%3A%22"+Platform+"%22%7D%5D&sort=__WEIGHT_DEFAULT:DESC";
        builder.append(platformInfo);
        builder.append("\n\n");
        
        builder.append("Model Information: \t");
        String modelInfo = "http://www.gsmarena.com/results.php3?sQuickSearch=yes&sName="+Model.replace(" ", "+");
        builder.append(modelInfo);
        builder.append("\n");
        
        return builder.toString();
    }
}
