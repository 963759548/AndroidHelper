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
package com.tuxedoberries.process.interfaces;

import com.tuxedoberries.process.CommandData;

/**
 *
 * @author jsilva
 */
public interface IEnqueue {
    
    /**
     * Enqueue the command to be executed as soon as the precious process is finished
     * @param command 
     */
    public void enqueueCommand(String command);
    
    /**
     * Enqueue the command to be executed as soon as the precious process is finished
     * @param command
     * @param env 
     */
    public void enqueueCommand(String command, String[] env);
    
    /**
     * Enqueue the command to be executed as soon as the precious process is finished
     * @param data 
     */
    public void enqueueCommand(CommandData data);
}
