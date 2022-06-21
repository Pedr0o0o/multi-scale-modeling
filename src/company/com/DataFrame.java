package company.com;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class DataFrame extends JFrame implements ActionListener
{

    JPanel pPanel = new JPanel();

    JButton bStart = new JButton("Start");

    JLabel lXwidth = new JLabel("SzerokośćX:");
    JLabel lYheight = new JLabel("WysokośćY:");
    JLabel lNeighbourhood = new JLabel("Typ sąsiedztwa");
    JLabel lGrainsStartAmount = new JLabel("Ilość zarodków");
    JLabel lHomoDivision = new JLabel("Ilość podziałów przestrzeni: x y");
    JLabel lGrainsStartGenTyp = new JLabel("Typ generacji zarodków");
    JLabel lBoundaryConditions = new JLabel("Warunki brzegowe: periodyczne");
    JLabel lLoadPicure = new JLabel("Załaduj obrazek mostka");
    JLabel lMonteCarloIndependent = new JLabel("Monte Carlo i ilośc przejść");
    //JLabel lMonteCarloBasedOn = new JLabel("MC na bazie modelu");

    JLabel lSRX = new JLabel("Rekrystalizacja");

    JTextField tXwidth = new JTextField();
    JTextField tYheight = new JTextField();
    JTextField tGrainStartAmount = new JTextField();
    JTextField tHomoDivisionX = new JTextField();
    JTextField tHomoDivisionY = new JTextField();
    JTextField tMCnStepsIndependent = new JTextField();
    //JTextField tMCnStepsBasedOn = new JTextField();


    JComboBox cbNeighbourhood = new JComboBox();
    JComboBox cbGrainsStartGenType = new JComboBox();

    JCheckBox chbBoundaryCondition = new JCheckBox();
    JCheckBox chbLoadPicture = new JCheckBox();
    JCheckBox chbMonteCarloIndependent = new JCheckBox();
    //JCheckBox chbMonteCarloBasedOn = new JCheckBox();

    JCheckBox chbSRX = new JCheckBox();


    public DataFrame()
    {
        setSize(400, 700);
        setTitle("Grain Growth");
        add(pPanel);

        setVisible(true);
        pPanel.setLayout(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //dodanie elementów do panelu
        pPanel.add(bStart);
        pPanel.add(lXwidth);
        pPanel.add(lYheight);
        pPanel.add(lNeighbourhood);
        pPanel.add(lGrainsStartAmount);
        pPanel.add(lGrainsStartGenTyp);
        pPanel.add(lHomoDivision);
        pPanel.add(lBoundaryConditions);
        pPanel.add(lLoadPicure);
        pPanel.add(lMonteCarloIndependent);
       // pPanel.add(lMonteCarloBasedOn);
        pPanel.add(lSRX);
        pPanel.add(tXwidth);
        pPanel.add(tYheight);
        pPanel.add(tGrainStartAmount);
        pPanel.add(tHomoDivisionX);
        pPanel.add(tHomoDivisionY);
        pPanel.add(tMCnStepsIndependent);
        //pPanel.add(tMCnStepsBasedOn);
        pPanel.add(cbNeighbourhood);
        pPanel.add(cbGrainsStartGenType);
        pPanel.add(chbBoundaryCondition);
        pPanel.add(chbLoadPicture);
        pPanel.add(chbMonteCarloIndependent);
        //pPanel.add(chbMonteCarloBasedOn);
        pPanel.add(chbSRX);

        //rozmieszczenie elementów na planszy
        bStart.setBounds(100, 555, 200, 40);

        lXwidth.setBounds(15, 5, 200, 30);
        lYheight.setBounds(15, 55, 200, 30);
        lNeighbourhood.setBounds(15, 155, 200, 30);
        lGrainsStartAmount.setBounds(15, 105, 200, 30);
        lGrainsStartGenTyp.setBounds(15, 205, 200, 30);
        lHomoDivision.setBounds(15, 255, 200, 30);
        lBoundaryConditions.setBounds(15, 305, 200, 30);
        lLoadPicure.setBounds(15,355,200,20);
        lMonteCarloIndependent.setBounds(15,405,200,20);
        //lMonteCarloBasedOn.setBounds(15,455,200,20);
        lSRX.setBounds(15,505,200,20);

        tXwidth.setBounds(215, 5, 50, 20);
        tYheight.setBounds(215, 55, 50, 20);
        tGrainStartAmount.setBounds(215, 105, 50, 20);
        tMCnStepsIndependent.setBounds(240,405,50,20);
        //tMCnStepsBasedOn.setBounds(240,455,50,20);
        cbNeighbourhood.setBounds(215, 155, 100, 20);
        cbGrainsStartGenType.setBounds(215, 205, 100, 20);
        tHomoDivisionX.setBounds(215, 255, 50, 20);
        tHomoDivisionY.setBounds(270,255, 50, 20);
        chbBoundaryCondition.setBounds(215, 305, 20, 20);
        chbLoadPicture.setBounds(215,355,20,20);
        chbMonteCarloIndependent.setBounds(215,405,20,20);
        //chbMonteCarloBasedOn.setBounds(215,455,20,20);
        chbSRX.setBounds(215,505,20,20);

        cbGrainsStartGenType.addItem("Losowe");
        cbGrainsStartGenType.addItem("Jednorodne");
        cbNeighbourhood.addItem("Moore");
        cbNeighbourhood.addItem("Hexagonal");


        bStart.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object f = e.getSource();

        if(f == bStart)
        {

            int z = Integer.parseInt(tGrainStartAmount.getText());


            GrainGrowth GG = new GrainGrowth(z);

            //OBRAZEK MOSTKA
            if (chbLoadPicture.isSelected())
            {

                GG.getDimensions();

                //zczytywanie obrazka
                try {
                    GG.compArea();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                //typy zarodkowania
                if (cbGrainsStartGenType.getSelectedItem().equals("Losowe"))
                {
                    GG.randNuc();
                }
                else if (cbGrainsStartGenType.getSelectedItem().equals("Jednorodne")) {
                    int hx = Integer.parseInt(tHomoDivisionX.getText());
                    int hy = Integer.parseInt(tHomoDivisionY.getText());
                    GG.homoNuc(hx, hy);
                }

                //typ sąsiedztwa
                if (cbNeighbourhood.getSelectedItem().equals("Moore")) {
                    GG.grainGrowthMoore(chbBoundaryCondition.isSelected());
                } else if (cbNeighbourhood.getSelectedItem().equals("Hexagonal")) {
                    try {
                        GG.grainGrowthHex(chbBoundaryCondition.isSelected());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                //monte carlo
                if (chbMonteCarloIndependent.isSelected()) {
                    int nSteps = Integer.parseInt(tMCnStepsIndependent.getText());
                    GG.nucStartMC();
                    GG.monteCarlo(nSteps, chbLoadPicture.isSelected());
                }

            }
            //WŁASNA PROSTOKĄTNA PRZESTRZEŃ
            else {
                int x = Integer.parseInt(tXwidth.getText());
                int y = Integer.parseInt(tYheight.getText());
                GG.setSizeX(x);
                GG.setSizeY(y);
                GG.initializeArea();

                //zarodki start
                if (cbGrainsStartGenType.getSelectedItem().equals("Losowe"))
                {
                    GG.randNuc();
                }
                else if (cbGrainsStartGenType.getSelectedItem().equals("Jednorodne"))
                {
                    int hx = Integer.parseInt(tHomoDivisionX.getText());
                    int hy = Integer.parseInt(tHomoDivisionY.getText());
                    GG.homoNuc(hx, hy);
                }

//typ sąsiedztwa
                if (cbNeighbourhood.getSelectedItem().equals("Moore")) {

                    GG.grainGrowthMoore(chbBoundaryCondition.isSelected());

                } else if (cbNeighbourhood.getSelectedItem().equals("Hexagonal")) {

                    try {
                        GG.grainGrowthHex(chbBoundaryCondition.isSelected());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }
                //monte carlo
                if (chbMonteCarloIndependent.isSelected())
                {
                    int nSteps = Integer.parseInt(tMCnStepsIndependent.getText());
                    GG.nucStartMC();
                    GG.monteCarlo(nSteps, chbLoadPicture.isSelected());
                }
            }


            //WIZUALIZACJA
            try {
                GG.print();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


            //SRX
            if(chbSRX.isSelected()) {
                PicFrame pF = new PicFrame();

                try {
                    GG.SRX(chbLoadPicture.isSelected());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    GG.print();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                PicFrame pFSRX = new PicFrame();
                pFSRX.setTitle("SRX");
            }
        }
    }


}
