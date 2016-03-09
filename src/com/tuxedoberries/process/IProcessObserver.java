/*
 * Copyright (C) 2016 jsilva
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

/**
 *
 * @author jsilva
 */
public interface IProcessObserver {
    
    /**
     * Subscribe when the process has just started
     * @param listener 
     */
    public void subscribeOnStart(IProcessStartListener listener);
    
    /**
     * Subscribe then the process has just stopped
     * @param listener 
     */
    public void subscribeOnStop(IProcessStopListener listener);
    
    /**
     * Unsubscribe when the process has just started
     * @param listener 
     */
    public void unsubscribeOnStart(IProcessStartListener listener);
    
    /**
     * Unsubscribe then the process has just stopped
     * @param listener 
     */
    public void unsubscribeOnStop(IProcessStopListener listener);
}
