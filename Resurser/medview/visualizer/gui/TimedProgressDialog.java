/*
 * TimedProgressDialog.java
 *
 * Created on den 7 juli 2003, 19:09
 *
 * $Id: TimedProgressDialog.java,v 1.4 2004/12/17 08:37:47 erichson Exp $
 *
 * $Log: TimedProgressDialog.java,v $
 * Revision 1.4  2004/12/17 08:37:47  erichson
 * Progress numbers also shown
 *
 * Revision 1.3  2004/10/21 12:27:48  erichson
 * Now gets description from progressObject.
 *
 * Revision 1.2  2004/02/24 20:15:15  erichson
 * Corrected a spelling mistake.
 *
 * Revision 1.1  2003/07/07 22:33:10  erichson
 * First check-in.
 *
 */

package medview.visualizer.gui;

import java.awt.*; // BorderLayout, Component
import java.awt.event.*; // ActionListener etc
import javax.swing.*; // Timer, JProgressBar


import medview.visualizer.data.*;

/**
 * Dialog containing a progress bar and OK/cancel buttons to monitor a ProgressObject.
 * Both buttons hide the component, but the cancel button also cancels the ProgressObject job.
 * 
 * This class is similar to ProgressMonitor, but has support for indeterminate progress, and is
 * also tailor made for ProgressObject jobs.
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class TimedProgressDialog implements ActionListener { 
    
    private static final int UPDATE_INTERVAL_MSEC = 1000;
    
    private static final String OK_CHOICE = "OK";
    private static final String CANCEL_CHOICE = "Cancel";
    private static final String[] dialogChoices = { OK_CHOICE, CANCEL_CHOICE };

    private JLabel messageLabel;
    private JPanel panel;
    private ProgressObject job;
    private JProgressBar progressBar;
    private Component parentComponent;
    // private String message;
    private JOptionPane optionPane;
    private JDialog dialog;
    private Timer timer;
    private boolean appendProgressNumbersToDescription = true;
    
    /**
     * Create a new progress dialog.
     */
    public TimedProgressDialog(ProgressObject job, Component parentComponent, String message)
    {
       this.job = job;                     
       this.parentComponent = parentComponent;
       //this.message = message;
       
       messageLabel = new JLabel(message);
       progressBar = new JProgressBar();
             
       panel = new JPanel();
       panel.setLayout(new BorderLayout());
       panel.add(progressBar,BorderLayout.CENTER);
       panel.add(messageLabel, BorderLayout.NORTH);
       // panel.add(buttonPanel, BorderLayout.SOUTH);

       optionPane = new JOptionPane( panel, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null , dialogChoices, OK_CHOICE);
       
       timer = new Timer(UPDATE_INTERVAL_MSEC, null);           
       timer.addActionListener(this);
       
       dialog = optionPane.createDialog(parentComponent, "Working");
       
       
    }

    /**
     * Respond to events from the Timer, i.e. update the progress bar
     */
    public void actionPerformed(ActionEvent ev) {
        int current = job.getProgress();
        int max = job.getProgressMax();
                        
        if (job.isIndeterminate()) {
            progressBar.setIndeterminate(true);
            ApplicationManager.debug("Progress bar updating: Indeterminate progress bar.");
        } else {
            ApplicationManager.debug("Progress bar updating with values min = " + job.getProgressMin() + " max = " + max + " current = " + current);
            progressBar.setIndeterminate(false);
            progressBar.setMinimum(job.getProgressMin());
            progressBar.setMaximum(max);
            progressBar.setValue(current); 
            String description = job.getDescription();
            
            if (appendProgressNumbersToDescription)
            {
                description += (" (" + current + "/" + max +")");
            }
            
            messageLabel.setText(description);
        }                                     
        
        // Finish up if the job has ended
        if ( !job.isRunning() || job.isCancelled()) {
            ApplicationManager.debug("Closing dialog because running="+job.isRunning()+" or isCancelled="+job.isRunning());
            timer.stop();
            timer = null;
            dialog.setVisible(false);
            dialog.dispose();            
        }            
    }

    /**
     * Show the progress dialog and waits. If cancel is chosen, the ProgressObject job is cancelled.
     * 
     * @return the dialog choice - OK_CHOICE or CANCEL_CHOICE
     */
    public Object show() {
        timer.start();
        dialog.show(); // blocks
        ApplicationManager.debug("Returned from dialog show()");
        Object dialogChoice = optionPane.getValue();
        if (dialogChoice == CANCEL_CHOICE) { // cancel 
            ApplicationManager.debug("Cancelling job b/c of cancel choice");
            job.cancel();                        
        } else {
            ApplicationManager.debug("That was NOT a cancel");
        }
        return dialogChoice;           
    }    
}
