package company.com;

import company.com.PicPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PicFrame extends JFrame
{

    public PicFrame() {
        super("Result");

        JPanel picPanel = new PicPanel();
        add(picPanel);

        pack();
        setVisible(true);
    }
}