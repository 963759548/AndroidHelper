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

import com.tuxedoberries.mainloop.IUpdate;
import java.util.HashSet;

/**
 *
 * @author Juan Silva
 */
public class ProcessObserver implements IUpdate, IProcessObserver {

    private final HashSet<IProcessStartListener> _startListeners;
    private final HashSet<IProcessStopListener> _stopListeners;
    private IProcessStats _target;
    private boolean _wasRunning = false;
    
    public ProcessObserver () {
        _startListeners = new HashSet<>();
        _stopListeners = new HashSet<>();
    }
    
    public void setProcess(IProcessStats process) {
        _target = process;
    }
    
    @Override
    public void Update() {
        if(_target == null)
            return;
        
        if(_target.isRunning()) {
            executeWhenRunning();
        } else {
            executeWhenStop();
        }
        
        // Save State
        _wasRunning = _target.isRunning();
    }    
    
    private void executeWhenStop () {
        if(_wasRunning) {
            for (IProcessStopListener _stopListener : _stopListeners) {
                _stopListener.onProcessStopped(_target.getLastProcess());
            }
        }
    }
    
    private void executeWhenRunning () {
        if(!_wasRunning){
            for (IProcessStartListener _startListener : _startListeners) {
                _startListener.onProcessStarted(_target.getCurrentProcess());
            }
        }
    }
    
    @Override
    public void subscribeOnStart(IProcessStartListener listener) {
        if(_startListeners.contains(listener))
            return;
        _startListeners.add(listener);
    }
    
    @Override
    public void subscribeOnStop(IProcessStopListener listener) {
        if(_stopListeners.contains(listener))
            return;
        _stopListeners.add(listener);
    }
    
    @Override
    public void unsubscribeOnStart(IProcessStartListener listener) {
        if(!_startListeners.contains(listener))
            return;
        _startListeners.remove(listener);
    }
    
    @Override
    public void unsubscribeOnStop(IProcessStopListener listener) {
        if(!_stopListeners.contains(listener))
            return;
        _stopListeners.remove(listener);
    }
}
