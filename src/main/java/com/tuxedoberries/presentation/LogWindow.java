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
package com.tuxedoberries.presentation;

import com.tuxedoberries.process.interfaces.IProcessOutputListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JScrollBar;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import javax.swing.WindowConstants;

/**
 *
 * @author Juan Silva
 */
public class LogWindow extends JFrame implements IProcessOutputListener {
    
    /**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 9011057462682054241L;
	private static final int WINDOW_WIDTH = 640;
    private static final int WINDOW_HEIGHT = 480;
    private JScrollPane jScrollPane1;
    private JTextArea jTextArea1;
    private JScrollBar verticalBar;
    private boolean autoScrollDown = true;
    private boolean allowClose = true;
    
    public LogWindow (boolean allowClose, boolean autoScroll) {
        this.allowClose = allowClose;
        this.autoScrollDown = autoScroll;
        
        initComponents();
        reposition();
    }
    
    private void initComponents () {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        verticalBar = jScrollPane1.getVerticalScrollBar();
        
        if(allowClose)
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        else
            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        jScrollPane1.setBackground(Color.BLACK);
        jTextArea1.setBackground(Color.BLACK);
        jTextArea1.setForeground(Color.WHITE);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setEditable(false);
        jScrollPane1.setViewportView(jTextArea1);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, WINDOW_WIDTH, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, WINDOW_HEIGHT, Short.MAX_VALUE)
        );

        pack();
    }
    
    private void reposition () {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int posX = dim.width - WINDOW_WIDTH;
        int posY = 0;
        setLocation(posX, posY);
    }
    
    public void onNewLine(String line) {
        this.jTextArea1.append(line);
        this.jTextArea1.append("\n");
        
        if(autoScrollDown) {
            verticalBar.setValue(verticalBar.getMaximum());
        }
    }
    
    public void clear () {
        this.jTextArea1.setText("");
    }
    
    public String getFullText () {
        return jTextArea1.getText();
    }
}
