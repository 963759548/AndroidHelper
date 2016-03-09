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
    private Thread _mainThreadNoSleep;
    private final CoreLoop _coreLoopNoSleep;
    
    private MainLoop () {
        _coreLoop = new CoreLoop();
        _coreLoopNoSleep = new CoreLoop(false);
    }
    
    public static void start () {
        MainLoop loop = getInstance();
        loop.doStart();
    }
    
    private void doStart () {
        if(_mainThread != null)
            return;
        
        // Start Thread
        _mainThread = new Thread(_coreLoop);
        _mainThread.start();
        // Start No Sleep
        _mainThreadNoSleep = new Thread(_coreLoopNoSleep);
        _mainThreadNoSleep.start();
    }
    
    public static void stop () {
        MainLoop loop = getInstance();
        loop.doStop();
    }
    
    private void doStop () {
        if(_mainThread == null)
            return;
        
        // Stop Thread
        _mainThread.interrupt();
        _mainThread = null;
        _coreLoop.clearSubscribers();
        // Stop No Sleep
        _mainThreadNoSleep.interrupt();
        _mainThreadNoSleep = null;
        _coreLoopNoSleep.clearSubscribers();
    }
    
    public static ILooper getLooper () {
        return getInstance()._coreLoop;
    }
    
    public static ILooper getLooperNoSleep () {
        return getInstance()._coreLoopNoSleep;
    }
}
