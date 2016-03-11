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
package com.tuxedoberries.process;

import com.tuxedoberries.process.interfaces.IProcessLog;
import com.tuxedoberries.process.interfaces.IProcessOutputListener;

/**
 *
 * @author Juan Silva
 */
public class ProcessLog implements IProcessOutputListener, IProcessLog {
    
    private StringBuilder builder;
    
    public ProcessLog () {
        builder = new StringBuilder();
    }
    
    @Override
    public synchronized void onNewLine (String line) {
        builder.append(line);
        builder.append("\n");
    }
    
    public void startCommand (String command) {
        builder.append("------------------------------------------------\n");
        builder.append(command);
        builder.append("\n");
    }
    
    @Override
    public String getLog() {
        return builder.toString();
    }

    @Override
    public void clearLog() {
        builder = new StringBuilder();
    }
}
