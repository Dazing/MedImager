/*
 * MessagePanel.java
 *
 * 
 * Created on den 1 juli 2003, 19:45
 *
 * $Id: MessagePanel.java,v 1.3 2003/07/03 23:43:05 erichson Exp $
 *
 * $Log: MessagePanel.java,v $
 * Revision 1.3  2003/07/03 23:43:05  erichson
 * Added ERROR: prefix to criticalMessage() messages
 *
 * Revision 1.2  2003/07/02 16:07:06  erichson
 * Optimized component (removed unnecessary vector), fixed clear button, fixed initial size
 *
 * Revision 1.1  2003/07/02 00:22:21  erichson
 * First check-in
 *
 */

package medview.visualizer.gui;

import java.util.*;

import java.awt.*; // BorderLayout
import java.awt.event.*; // ActionEvent
import javax.swing.*;
import javax.swing.event.*;

/**
 * Floater component which receives messages (normal and critical) and displays them in a textarea.
 * It can be cleared and closed.
 * When receiving critical messages, it will dispatch stateChanged events for the encapsulating component
 * to show itself (raising frames, etc)
 *
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class MessagePanel extends FloaterComponent {
    
    //private java.util.Vector messageVector;
    private Vector changeListeners;
    
    private JTextArea textArea;
    
    /** Creates a new instance of MessagePanel */
    public MessagePanel() {
        super();
        //messageVector = new Vector();        
        textArea = new JTextArea(15, 80); // default size
        changeListeners = new Vector();
        initComponents();
    }
    
    private void initComponents() {
        removeAll();
        
        JButton clearButton = new JButton(new AbstractAction("Clear") {
            public void actionPerformed(ActionEvent ae) {
                clearMessages();
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(clearButton);
        
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.SOUTH);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
                                
    }
    /*
    private void updateTextComponent() {
        StringBuffer buffer = new StringBuffer();
        for (Iterator it = messageVector.iterator(); it.hasNext();) {
            String nextString = (String) it.next();
            buffer.append(nextString + "\n");
        }
        textArea.setText(buffer.toString());
    }*/
    
    /** Add a critical message. Fires a statechanged event to listeners, which should force the message to the attention of the user.
     * @param message the critical message
     */
    public void criticalMessage(String message) {
        addMessage("ERROR: " + message);
        fireStateChanged();
    }
    
    /**
     * Normal message - do not fire event
     */
    public void message(String message) {
        addMessage(message);
    }
    
    /**
     * Does not fire state changed, since this should only happen for critical messages
     */
    private void addMessage(String message) {        
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("HH:mm:ss");
        String timestamp = format.format(new Date()); // current time       
        //messageVector.add(timestamp + ' ' + message);
        //updateTextComponent();        
        textArea.append(message + "\n");
    }
    
    private void clearMessages() {
        //messageVector.clear();
        //updateTextComponent();
        textArea.setText("");
    }
    
    /**
     * Add a change listener.
     * Change Listeners are fired when the message panel is updated. The proper response to this is to
     * display the message panel when critical messages occur.
     */
    public void addChangeListener(ChangeListener cl) {
        changeListeners.add(cl);
    }
    
    public void removeChangeListener(ChangeListener cl) {
        changeListeners.remove(cl);
    }
    
    /**
     * Fire a ChangeEvent. 
     * @see #addChangeListener
     */
    private  void fireStateChanged() {
        for (Iterator it = changeListeners.iterator(); it.hasNext(); ) {
            ChangeListener nextListener = (ChangeListener) it.next();
            nextListener.stateChanged(new ChangeEvent(this));
        }
    }
    
    public int getFloaterType() {
        return Floater.FLOATER_TYPE_MESSAGES;
    }
}
