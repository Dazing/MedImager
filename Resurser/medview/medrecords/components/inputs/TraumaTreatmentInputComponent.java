package medview.medrecords.components.inputs;

import javax.swing.*;
import java.awt.*;

/**

 * Trauma treatment input component

 * @author Andreas Argirakis

 */

public class TraumaTreatmentInputComponent extends TeethInputComponent {

    public TraumaTreatmentInputComponent()
    {
        String[] teethButtons = { "13", "12", "11", "21", "22", "23",
                                  "43", "42", "41", "31", "32", "33"};
        setTeethButtons(teethButtons);
        buildGUI();
        refresh();

    }

    protected JPanel createInputPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel label;
        c.insets = new Insets(10,0,0,0);  //top padding
        c.weightx = 0.5;

        return panel;
    }



    public static void main(String[] args)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,1));
        TraumaTreatmentInputComponent debugComponent = new TraumaTreatmentInputComponent();
        panel.add(debugComponent);
        JFrame frame = new JFrame("Mineralization component test application");
		frame.getContentPane().add(panel);
		frame.pack();
        frame.show();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
}
