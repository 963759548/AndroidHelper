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
package com.tuxedoberries.mainloop;

/**
 *
 * @author jsilva
 */
public interface ILooper {
    
    /**
     * Subscribe to the loop
     * @param updater 
     */
    public void subscribe(IUpdate updater);
    
    /**
     * Unsubscribe from the loop
     * @param updater 
     */
    public void unsubscribe(IUpdate updater);
}