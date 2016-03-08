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
public class MainLoop {
    
    /**
     * Singleton instance
     */
    private static MainLoop _instance;
    
    /**
     * Return the unique instance of this class
     * @return 
     */
    public static MainLoop getInstance () {
        if(_instance == null){
            _instance = new MainLoop();
        }
        return _instance;
    }
    
    private Thread _mainThread;
    private final CoreLoop _coreLoop;
    
    private MainLoop () {
        _coreLoop = new CoreLoop();
    }
    
    public static void start () {
        MainLoop loop = getInstance();
        if(loop._mainThread != null)
            return;
        
        // Start Thread
        loop._mainThread = new Thread(loop._coreLoop);
        loop._mainThread.start();
    }
    
    public static void stop () {
        MainLoop loop = getInstance();
        if(loop._mainThread == null)
            return;
        if(!loop._mainThread.isAlive()){
            loop._mainThread = null;
            return;
        }
        
        loop._mainThread.interrupt();
        loop._mainThread = null;
        loop._coreLoop.clearSubscribers();
    }
    
    public static ILooper getLooper () {
        return getInstance()._coreLoop;
    }
}
