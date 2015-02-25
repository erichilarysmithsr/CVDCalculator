/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamma.cvd.calculator.gui.calculator;

import gamma.cvd.calculator.CVDPatient;
import gamma.cvd.calculator.CVDRiskData;
import gamma.cvd.calculator.gui.GuiUtils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
 import javax.swing.ImageIcon;
import javax.swing.JFrame;
 
/**
 *
 * @author Jack
 */
public class PatientSummaryScreen extends javax.swing.JFrame {

    PatientSummaryScreen(CVDRiskData model, CVDPatient patient) {
        GuiUtils.centerScreen(this);
        initComponents();
        lblNamePlaceholder.setText(patient.getFirstName()+" "+patient.getLastName());
        DisplayHealthTips(model);
        DisplaySummary(model);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRiskSummary = new javax.swing.JPanel();
        lblRiskSummary = new javax.swing.JLabel();
        lblTenYearLikelihood = new javax.swing.JLabel();
        lblAvgRisk = new javax.swing.JLabel();
        lblYouAre = new javax.swing.JLabel();
        lblNamePlaceholder = new javax.swing.JLabel();
        lblRiskImg = new javax.swing.JLabel();
        panelTips = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtTips = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("NHS CVD Calculator - Patient Summary");

        panelRiskSummary.setBorder(javax.swing.BorderFactory.createTitledBorder("Risk"));

        lblRiskSummary.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblRiskSummary.setText("Risk Placeholder");

        lblTenYearLikelihood.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTenYearLikelihood.setText("<html>Likely to develop a heart disease<br>\nwithin the next  <u>10 Years</u>");

        lblAvgRisk.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblAvgRisk.setText("<html>The average % risk for your<br> age group is: <u>0%</u>");

        lblYouAre.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        lblYouAre.setText("You Are...");

        lblNamePlaceholder.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblNamePlaceholder.setText("Name Placeholder");

        javax.swing.GroupLayout panelRiskSummaryLayout = new javax.swing.GroupLayout(panelRiskSummary);
        panelRiskSummary.setLayout(panelRiskSummaryLayout);
        panelRiskSummaryLayout.setHorizontalGroup(
            panelRiskSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRiskSummaryLayout.createSequentialGroup()
                .addGroup(panelRiskSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRiskSummaryLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(panelRiskSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTenYearLikelihood, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAvgRisk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblRiskSummary)
                            .addComponent(lblNamePlaceholder)))
                    .addGroup(panelRiskSummaryLayout.createSequentialGroup()
                        .addGap(109, 109, 109)
                        .addComponent(lblYouAre))
                    .addGroup(panelRiskSummaryLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(lblRiskImg)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        panelRiskSummaryLayout.setVerticalGroup(
            panelRiskSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRiskSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNamePlaceholder)
                .addGap(14, 14, 14)
                .addComponent(lblRiskSummary)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblYouAre)
                .addGap(7, 7, 7)
                .addComponent(lblRiskImg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 254, Short.MAX_VALUE)
                .addComponent(lblTenYearLikelihood, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(lblAvgRisk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        panelTips.setBorder(javax.swing.BorderFactory.createTitledBorder("Tips to improve your score"));

        txtTips.setEditable(false);
        txtTips.setContentType("text/html"); // NOI18N
        jScrollPane1.setViewportView(txtTips);

        javax.swing.GroupLayout panelTipsLayout = new javax.swing.GroupLayout(panelTips);
        panelTips.setLayout(panelTipsLayout);
        panelTipsLayout.setHorizontalGroup(
            panelTipsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTipsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE))
        );
        panelTipsLayout.setVerticalGroup(
            panelTipsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTipsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelRiskSummary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTips, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelTips, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelRiskSummary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAvgRisk;
    private javax.swing.JLabel lblNamePlaceholder;
    private javax.swing.JLabel lblRiskImg;
    private javax.swing.JLabel lblRiskSummary;
    private javax.swing.JLabel lblTenYearLikelihood;
    private javax.swing.JLabel lblYouAre;
    private javax.swing.JPanel panelRiskSummary;
    private javax.swing.JPanel panelTips;
    private javax.swing.JEditorPane txtTips;
    // End of variables declaration//GEN-END:variables

    private void DisplayHealthTips(CVDRiskData model) {

        final double LDL_UPPER_BOUND = 3.37;
        final double CHOLESTEROL_UPPER_BOUND = 3.37;
        final double HDL_LOWER_BOUND = 1.30;
        final double DIASTOLIC_BP_UPPER_BOUND = 84;
        final String NEW_LINE = "<br>";
        final String HTML_FONT = "<font face=\"calibri\">";
        final String CURRENT_DIR = System.getProperty("user.dir");

        StringBuilder healthTips = new StringBuilder();
        // Enable HTML formatting
        healthTips.append("<html>");
        healthTips.append(HTML_FONT);

        if (model != null) {
            if (model.getCholesterolType().equals(CVDRiskData.LDL) && model.getCholesterolMmolL() > LDL_UPPER_BOUND) {
                healthTips.append("<b><u>Ldl tips</u></b>");
                healthTips.append(NEW_LINE);
                healthTips.append(LoadHealthTips(CURRENT_DIR + "\\resources\\tips\\ldl.txt"));
            } else if (model.getCholesterolMmolL() > CHOLESTEROL_UPPER_BOUND) {
                healthTips.append("<b><u>Cholesterol tips</u></b>");
                healthTips.append(NEW_LINE);
                healthTips.append(LoadHealthTips(CURRENT_DIR + "\\resources\\tips\\ldl.txt"));
            }

            if (model.getHdlMmolL() < HDL_LOWER_BOUND) {
                healthTips.append("<b><u>HDL tips</u></b>");
                healthTips.append(NEW_LINE);
                healthTips.append(LoadHealthTips(CURRENT_DIR + "\\resources\\tips\\hdl.txt"));
            }

            if (model.getBloodPressureDiastolicMmHg() > DIASTOLIC_BP_UPPER_BOUND) {
                healthTips.append("<b><u>Blood Pressure Tips</u></b>");
                healthTips.append(NEW_LINE);
                healthTips.append(LoadHealthTips(CURRENT_DIR + "\\resources\\tips\\bloodpressure.txt"));
            }

            if (model.isSmoker()) {
                healthTips.append("<b><u>Tips to quit smoking</u></b>");
                healthTips.append(NEW_LINE);
                healthTips.append(LoadHealthTips(CURRENT_DIR + "\\resources\\tips\\smoking.txt"));
            }
        }
        healthTips.append("</font>");

        txtTips.setText(healthTips.toString());

    }

    private String LoadHealthTips(String filename) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filename));
            String line;
            List<String> fileTips = new ArrayList<>();
            while ((line = in.readLine()) != null) {
                fileTips.add(line);
            }
            StringBuilder healthTips = new StringBuilder();
            final String NEW_LINE = "<br>";
            for (String tip : fileTips) {
                if (fileTips != null && !fileTips.isEmpty()) {
                    healthTips.append(" - ");
                    healthTips.append(tip);
                    healthTips.append(NEW_LINE);
                }
            }
            return healthTips.toString();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PatientSummaryScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PatientSummaryScreen.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(PatientSummaryScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    private void DisplaySummary(CVDRiskData model) {
        try {
            Integer averageRisk = getAverageRisk(model.getAge());
            lblAvgRisk.setText(lblAvgRisk.getText().replace("0%", Integer.toString(averageRisk) + "%"));
            
            Integer patientRisk = model.getRiskPercentage(model.calculateRiskScore());
            
            if (patientRisk > averageRisk + 1) {
                lblRiskSummary.setForeground(Color.RED);
                lblRiskSummary.setText("Above average risk");
            }
            
            if (patientRisk < averageRisk + 1 && patientRisk > averageRisk - 1) {
                lblRiskSummary.setForeground(Color.YELLOW);
                lblRiskSummary.setText("Average risk");
            }
            
            if (patientRisk < averageRisk - 1) {
                lblRiskSummary.setForeground(Color.GREEN);
                lblRiskSummary.setText("Below average risk");
            }
            
           
            final BufferedImage image = loadCorrectIcon(patientRisk); 
            
            Graphics g = image.getGraphics();
            g.setFont(g.getFont().deriveFont(30f));
            g.setColor(Color.black);
            g.drawString(patientRisk.toString()+"%", 100, 130);
            g.dispose();
            
            lblRiskImg.setIcon(new ImageIcon(image));
            lblRiskImg.revalidate();
            lblRiskImg.repaint();
        } catch (IOException ex) 
        {
            Logger.getLogger(PatientSummaryScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private int getAverageRisk(int age) {
        if (age >= 30 && age <= 34) {
            return 3;
        }

        if (age >= 35 && age <= 39) {
            return 5;
        }

        if (age >= 40 && age <= 44) {
            return 7;
        }

        if (age >= 45 && age <= 49) {
            return 11;
        }

        if (age >= 50 && age <= 54) {
            return 14;
        }

        if (age >= 55 && age <= 59) {
            return 16;
        }

        if (age >= 60 && age <= 64) {
            return 21;
        }

        if (age >= 65 && age <= 69) {
            return 25;
        }

        if (age >= 70) {
            return 30;
        }

        return 0;
    }

    private BufferedImage loadCorrectIcon(Integer risk) throws IOException {
        final String WORK_DIR = System.getProperty("user.dir");
        final String ICON_DIR = "/resources/icons/calculatorStaticHearts/";
        if (risk >= 0 && risk <= 5) 
        {
            return ImageIO.read(new File(WORK_DIR + ICON_DIR + "Perfect_Green_Heart.png"));
        } 
        else if (risk >= 6 && risk <= 15) 
        {
            return ImageIO.read(new File(WORK_DIR + ICON_DIR + "Good_Green_Heart.png"));
        }        
        else if (risk >= 16 && risk <= 29) 
        {
            return ImageIO.read(new File(WORK_DIR + ICON_DIR + "Average_Yellow_Heart.png"));
        } 
        else if (risk >= 30) 
        {
            return ImageIO.read(new File(WORK_DIR + ICON_DIR + "Bad_Red_Heart.png"));
        }       
        
        return ImageIO.read(new File(WORK_DIR + ICON_DIR + "Perfect_Green_Heart.png"));
    }

}
