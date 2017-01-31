package medview.medrecords.components.inputs;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.ArrayList;

/**

 * Mineralization input component

 * @author Andreas Argirakis

 */

public class TeethInputComponent extends JPanel {

    private final static int PREV_NEXT_XSIZE = 60;
    private final static int PREV_NEXT_YSIZE = 30;
    protected int CurrentTooth;

    private final static Color SELECTED_COLOR = new Color(170,170,170);
    private ArrayList<String> teethButtons;
    private ArrayList<Boolean> teethButtonsEnabled;



    private ArrayList<JPanel> inputPanels;
    private JLabel labelCurrentTooth;
    private JPanel middlePanel;
    private JPanel topPanel;
    private JPanel bottomPanel;

    /** Creates a new instance of MucosInputComponent */

    public TeethInputComponent() {

        super(new BorderLayout());

    }

    public void init()
    {
        CurrentTooth=-1;
        do
        {
            CurrentTooth++;
        }
        while(!teethButtonsEnabled.get(CurrentTooth));
    }
    protected void buildGUI()
    {
        init();

        JButton prevbutton;
        prevbutton = new JButton("<");
        prevbutton.setAlignmentY(CENTER_ALIGNMENT);
        Font labelFont = prevbutton.getFont().deriveFont(Font.BOLD, 30);
        prevbutton.setFont(labelFont);
        JPanel panelPrev = new JPanel();
        panelPrev.setLayout(new BoxLayout(panelPrev,BoxLayout.X_AXIS));
        panelPrev.add(prevbutton);

        prevbutton.setPreferredSize(new Dimension(PREV_NEXT_XSIZE,PREV_NEXT_YSIZE));
        prevbutton.addActionListener(
        new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                changeTooth(true);  // code to execute when next button is pressed
            }
        }
        );

        JButton nextbutton = new JButton(">");
        nextbutton.setAlignmentY(CENTER_ALIGNMENT);

        nextbutton.setFont(labelFont);

        JPanel panelNext = new JPanel();
        panelNext.setLayout(new BoxLayout(panelNext,BoxLayout.X_AXIS));
        panelNext.add(nextbutton);
        nextbutton.setMinimumSize(new Dimension(PREV_NEXT_XSIZE,PREV_NEXT_YSIZE));
        nextbutton.addActionListener(
        new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                changeTooth(false);  // code to execute when prev button is pressed
            }
        }
        );
        setLayout(new BorderLayout());
        topPanel = createTopPanel();
        bottomPanel = createBottomPanel();
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
        add(panelPrev, BorderLayout.WEST);
        add(panelNext, BorderLayout.EAST);
        middlePanel = createMiddlePanel();
        add(middlePanel, BorderLayout.CENTER);


        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        refresh();
    }
    private JPanel createTopPanel()
    {
        JButton button;
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,teethButtons.size()/2));
        for(int i = 0; i<teethButtons.size()/2; i++)
        {
            button = new JButton(teethButtons.get(i));
            button.addActionListener(createButtonListener(i));
            panel.add(button);
        }
        return panel;
    }

    private JPanel createMiddlePanel()
    {
        JPanel panel = new JPanel();
        //panel.setMinimumSize(new Dimension(400,300));
        //panel.setLayout(new GridLayout(1,3));
        panel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        panel.add(topPanel);
        labelCurrentTooth = new JLabel(teethButtons.get(CurrentTooth));
        Font labelFont = labelCurrentTooth.getFont().deriveFont(Font.BOLD, 30);
        labelCurrentTooth.setFont(labelFont);
        topPanel.add(labelCurrentTooth);
        panel.add(topPanel,BorderLayout.NORTH);


        panel.add(inputPanels.get(CurrentTooth),BorderLayout.CENTER);
        panel.add(new JPanel(),BorderLayout.SOUTH); //dummy
        return panel;
    }

    private JPanel createBottomPanel()
    {
        JButton button;
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,teethButtons.size()/2));
        for(int i = teethButtons.size()/2; i<teethButtons.size(); i++)
        {
            button = new JButton(teethButtons.get(i));
            button.addActionListener(createButtonListener(i));
            panel.add(button);
        }
        return panel;
    }

    protected JPanel createInputPanel()
    {
        return new JPanel();
    }

    private void changeTooth(boolean prev)
    {
        middlePanel.remove(inputPanels.get(CurrentTooth));
        if(prev)
        {
            do
            {
                CurrentTooth--;
                if(CurrentTooth<0)
                {
                    CurrentTooth=teethButtons.size()-1;
                }
            }
            while(!teethButtonsEnabled.get(CurrentTooth));
        }
        else
        {
            do
            {
                CurrentTooth++;
                if(CurrentTooth>teethButtons.size()-1)
                {
                    CurrentTooth=0;
                }
            }
            while(!teethButtonsEnabled.get(CurrentTooth));
        }
        middlePanel.add(inputPanels.get(CurrentTooth),BorderLayout.CENTER);
        refresh();
    }


    protected void refresh()
    {
        labelCurrentTooth.setText(teethButtons.get(CurrentTooth));

        middlePanel.repaint();
        //go through all the textfields and set the teethbuttons to bold if they have entered data

        for(int i = 0; i < teethButtons.size(); i++)
        {
            JButton button;
            Font font;
            if(i<teethButtons.size()/2)
            {
                button = (JButton)topPanel.getComponent(i);
            }
            else
            {
                button = (JButton)bottomPanel.getComponent(i-teethButtons.size()/2);
            }
            if(!teethButtonsEnabled.get(i))
            {
                button.setEnabled(false);
            }
            if(i==CurrentTooth)
            {
                button.setBackground(SELECTED_COLOR);
            }
            else
            {
                button.setBackground(null);
            }
            if(hasData(i))
            {
                font = button.getFont().deriveFont(Font.BOLD);
                button.setFont(font);
                button.setForeground(new Color(50,50,255));
            }
            else
            {
                font = button.getFont().deriveFont(Font.PLAIN);
                button.setFont(font);
                button.setForeground(new Color(0,0,0));
            }
        }
    }

    //this method should be overridded by the inheriting class
    protected boolean hasData(int tooth)
    {
        return false;
    }
    
    private ActionListener createButtonListener(int i)
    {
        final int b = i;
        return new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    middlePanel.remove(inputPanels.get(CurrentTooth));
                    CurrentTooth=b;
                    middlePanel.add(inputPanels.get(CurrentTooth),BorderLayout.CENTER);
                    refresh();
                }
            };
    }

    //Accessor methods follows

    public int getCurrentTooth() {
        return CurrentTooth;
    }

    public void setCurrentTooth(int currentTooth) {
        CurrentTooth = currentTooth;
    }

    public ArrayList<String> getTeethButtons() {
        return teethButtons;
    }

    public int getNumberOfTeethButtons() {
        return teethButtons.size();
    }

    public void setEnabledToothButton(int idx, boolean enable)
    {
        teethButtonsEnabled.set(idx,enable);
    }
    public boolean isToothButtonEnabled(int idx)
    {
        return teethButtonsEnabled.get(idx);
    }

    //sets the teeth buttons and sets all to enabled
    public void setTeethButtons(String[] teethButtons)
    {
        this.teethButtons = new ArrayList();
        this.inputPanels = new ArrayList();
        this.teethButtonsEnabled = new ArrayList();
        for(int i = 0; i < teethButtons.length; i++)
        {
            JPanel inputPanel = createInputPanel();
            inputPanel.setBorder(   BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder(""),
                                BorderFactory.createEmptyBorder(5,5,5,5)));
            this.inputPanels.add(inputPanel);
            this.teethButtons.add(teethButtons[i]);
            this.teethButtonsEnabled.add(true);
        }
    }



    public ArrayList<JPanel> getInputPanels()
    {
        return inputPanels;
    }
}
