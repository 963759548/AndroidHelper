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
package com.tuxedoberries.mainloop;

import com.tuxedoberries.process.ProcessController;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan Silva
 */
public class CoreLoop implements Runnable, ILooper {
    
    private final HashSet<IUpdate> subscribers;
    private Logger logger;
    private boolean sleep = true;
    private long targetSleep = 42;
    
    public CoreLoop () {
        this(true, 42);
    }
    
    public CoreLoop (boolean sleep) {
        this(sleep, 42);
    }
    
    public CoreLoop (boolean sleep, long sleepMilis) {
        subscribers = new HashSet<IUpdate>();
        this.sleep = sleep;
        this.targetSleep = sleepMilis;
        createLogger();
    }
    
    public void run () {
        long currentDelta = 0;
        long startDelta = 0;
        while(true){
            startDelta = System.currentTimeMillis();
            // Update
            triggerUpdate (currentDelta);
            if(sleep)
                sleepMe();
            
            currentDelta = System.currentTimeMillis() - startDelta;
        }
    }
    
    private synchronized void triggerUpdate (long delta) {
        for (IUpdate subscriber : subscribers) {
            if(subscriber == null)
                continue;
            subscriber.Update(delta);
        }
    }
    
    public synchronized void subscribe(IUpdate updater) {
        if(subscribers.contains(updater))
            return;
        subscribers.add(updater);
    }
    
    public synchronized void unsubscribe(IUpdate updater) {
        if(!subscribers.contains(updater))
            return;
        subscribers.remove(updater);
    }
    
    public synchronized void clearSubscribers () {
        subscribers.clear();
    }
    
    private void sleepMe () {
        try {
            Thread.sleep(targetSleep);
        } catch (InterruptedException ex) {
            logger.log(Level.INFO, "Threas is interrupted", ex);
        }
    }
    
    private void createLogger () {
        if(logger == null) {
            String loggerName = String.format("[%d]%s", this.hashCode(), ProcessController.class.getName());
            logger = Logger.getLogger(loggerName);
        }
    }
}
