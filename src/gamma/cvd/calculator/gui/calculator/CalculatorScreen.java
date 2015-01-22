/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamma.cvd.calculator.gui.calculator;

import gamma.cvd.calculator.CVDRiskData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Jack
 */
public class CalculatorScreen extends javax.swing.JFrame
{

    /**
     * Creates new form CalculatorScreen
     */
    public CalculatorScreen()
    {
        initComponents();
        groupGenderButtons();
        addListeners();
    }

    private void addListeners()
    {
        // Add listeners to update text field to corresponding slider value upon slider change. 
        addSliderChangeListener(sliderAge, txtAge);
        addSliderChangeListener(sliderBloodPressure, txtBloodPressure);
        addSliderChangeListener(sliderCholesterol, txtCholesterol);
        addSliderChangeListener(sliderHdl, txtHdl);
        addSliderChangeListener(sliderLdl, txtLdl);

        // Add listeners to update slider upon entering value corresponding in text field
        addTextChangeListener(sliderAge, txtAge);
        addTextChangeListener(sliderBloodPressure, txtBloodPressure);
        addTextChangeListener(sliderCholesterol, txtCholesterol);
        addTextChangeListener(sliderHdl, txtHdl);
        addTextChangeListener(sliderLdl, txtLdl);

        addRadioListeners(radioGenderMale);
        addRadioListeners(radioGenderFemale);
        addRadioListeners(radioDiabetes);
        addRadioListeners(radioSmoker);
        
        addComboChangeListener(comboBloodPressureType);
        addComboChangeListener(comboCholesterolMeasurement);
        addComboChangeListener(comboHdlcMeasurement);
        addComboChangeListener(comboLdlcMeasurement);
        
    }

    private void addRadioListeners(JRadioButton radioButton)
    {
        radioButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                calculateCvdRisk();
            }
        });
    }

    // Upon movement of the slider, will update the corresponding text field with its value. 

    private void addSliderChangeListener(JSlider slider, final JTextField field)
    {
        ChangeListener listener = new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent event)
            {
                // update text field when the slider value changes
                JSlider source = (JSlider) event.getSource();
                field.setText("" + source.getValue());
                calculateCvdRisk();
            }
        };

        slider.addChangeListener(listener);
    }

    private void addComboChangeListener(JComboBox comboBox)
    {
        comboBox.addActionListener(new ActionListener()
               {
                   @Override
                   public void actionPerformed(ActionEvent e)
                   {
                       calculateCvdRisk();
                   }
               });
    }
    
    private void calculateCvdRisk()
    {
        CVDRiskData model = new CVDRiskData();

        final String SYSTOLIC = "Systolic";
        final String MGDL_MEASUREMENT = "mg/dl";

        int bloodPressureSystolicMmHg = 0;
        int bloodPressureDiastolicMmHg = 0;

        String cholesterolType = null;
        String cholesterolMeasurement = null;
        int cholesterolValue = 0;

        // Check if blood pressure type is Systolic or Diastolic
        if (comboBloodPressureType.getSelectedItem().toString().equals(SYSTOLIC))
        {
            bloodPressureSystolicMmHg = Integer.parseInt(txtBloodPressure.getText());
        } else
        {
            bloodPressureDiastolicMmHg = Integer.parseInt(txtBloodPressure.getText());
        }

        model.setBloodPressure(bloodPressureSystolicMmHg, bloodPressureDiastolicMmHg);
        model.setAge(Integer.parseInt(txtAge.getText()));
        model.setIsDiabetic(radioDiabetes.isSelected());
        model.setIsSmoker(radioSmoker.isSelected());
        if (radioGenderFemale.isSelected())
        {
            model.setFemale();
        } else
        {
            model.setMale();
        }

        // Check if measurement is set to Mg/DL or not - set appropriate value.
        if (comboHdlcMeasurement.getSelectedItem().toString().equals(MGDL_MEASUREMENT))
        {
            model.setHdlMgDl(Integer.parseInt(txtHdl.getText()));
        } else
        {
            model.setHdlMmolL(Integer.parseInt(txtHdl.getText()));
        }

        if (!txtLdl.getText().equals("0") && !txtCholesterol.getText().equals("0"))
        {
            JOptionPane.showMessageDialog(this, "Both LDL-C and Cholesterol values entered, please only fill one field", "Argument Exception", JOptionPane.ERROR_MESSAGE);
            cholesterolMeasurement = MGDL_MEASUREMENT;
        } else if (!txtLdl.getText().equals("0"))
        {
            cholesterolType = CVDRiskData.LDL;
            cholesterolValue = Integer.parseInt(txtLdl.getText());
            cholesterolMeasurement = comboLdlcMeasurement.getSelectedItem().toString();
        } else
        {
            cholesterolType = CVDRiskData.CHOL;
            cholesterolValue = Integer.parseInt(txtCholesterol.getText());
            cholesterolMeasurement = comboLdlcMeasurement.getSelectedItem().toString();
        }

        if (cholesterolMeasurement.equals(MGDL_MEASUREMENT))
        {
            model.setCholesterolMgDl(cholesterolType, cholesterolValue);
        } else
        {
            model.setCholesterolMmolL(cholesterolType, cholesterolValue);
        }

        Integer score = model.calculateRiskScore();
        Integer risk = model.getRiskPercentage(score);
        lblCvdRisk.setText(risk.toString() + "%");

    }

    // Upon entering new text into a text field, will update the corresponding slider to that position
    private void addTextChangeListener(final JSlider slider, final JTextField field)
    {
        field.addKeyListener(new KeyListener()
        {

            @Override
            public void keyTyped(KeyEvent e)
            {
            }

            @Override
            public void keyPressed(KeyEvent e)
            {
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                int value = 0;
                try
                {
                    value = Integer.parseInt(field.getText());
                    slider.setValue(value);
                } catch (NumberFormatException x)
                {
                    // Supressed exception - If entering a non alphanumeric character, do not change value.
                }
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        panelCalculator = new javax.swing.JPanel();
        panelLdl = new javax.swing.JPanel();
        sliderLdl = new javax.swing.JSlider();
        lblLdl = new javax.swing.JLabel();
        txtLdl = new javax.swing.JTextField();
        comboLdlcMeasurement = new javax.swing.JComboBox();
        panelCholesterol = new javax.swing.JPanel();
        sliderCholesterol = new javax.swing.JSlider();
        lblCholesterol = new javax.swing.JLabel();
        txtCholesterol = new javax.swing.JTextField();
        comboCholesterolMeasurement = new javax.swing.JComboBox();
        panelAge = new javax.swing.JPanel();
        sliderAge = new javax.swing.JSlider();
        lblAge = new javax.swing.JLabel();
        txtAge = new javax.swing.JTextField();
        radioDiabetes = new javax.swing.JRadioButton();
        radioSmoker = new javax.swing.JRadioButton();
        lblCvdTenYearRisk = new javax.swing.JLabel();
        lblCvdRisk = new javax.swing.JLabel();
        btnSaveResult = new javax.swing.JButton();
        panelHdl = new javax.swing.JPanel();
        sliderHdl = new javax.swing.JSlider();
        lblHdl = new javax.swing.JLabel();
        txtHdl = new javax.swing.JTextField();
        comboHdlcMeasurement = new javax.swing.JComboBox();
        radioGenderMale = new javax.swing.JRadioButton();
        radioGenderFemale = new javax.swing.JRadioButton();
        lblGender = new javax.swing.JLabel();
        panelBloodPressure = new javax.swing.JPanel();
        sliderBloodPressure = new javax.swing.JSlider();
        lblBloodPressure = new javax.swing.JLabel();
        txtBloodPressure = new javax.swing.JTextField();
        comboBloodPressureType = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("NHS CVD Calculator - New Patient");

        panelCalculator.setBorder(javax.swing.BorderFactory.createTitledBorder("CVD Calculator"));

        sliderLdl.setMaximum(250);
        sliderLdl.setValue(0);

        lblLdl.setText("LDL-C");

        txtLdl.setText("0");
        txtLdl.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtLdlActionPerformed(evt);
            }
        });

        comboLdlcMeasurement.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "mg/dl", "mmol/L" }));
        comboLdlcMeasurement.setToolTipText("");

        sliderCholesterol.setMaximum(320);
        sliderCholesterol.setValue(0);

        lblCholesterol.setText("Cholesterol");

        txtCholesterol.setText("0");
        txtCholesterol.setToolTipText("");

        comboCholesterolMeasurement.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "mg/dl", "mmol/L" }));
        comboCholesterolMeasurement.setToolTipText("");
        comboCholesterolMeasurement.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                comboCholesterolMeasurementActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCholesterolLayout = new javax.swing.GroupLayout(panelCholesterol);
        panelCholesterol.setLayout(panelCholesterolLayout);
        panelCholesterolLayout.setHorizontalGroup(
            panelCholesterolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCholesterolLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCholesterolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCholesterolLayout.createSequentialGroup()
                        .addComponent(sliderCholesterol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(txtCholesterol, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelCholesterolLayout.createSequentialGroup()
                        .addComponent(lblCholesterol)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboCholesterolMeasurement, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelCholesterolLayout.setVerticalGroup(
            panelCholesterolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCholesterolLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCholesterol)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelCholesterolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sliderCholesterol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelCholesterolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCholesterol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboCholesterolMeasurement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        javax.swing.GroupLayout panelLdlLayout = new javax.swing.GroupLayout(panelLdl);
        panelLdl.setLayout(panelLdlLayout);
        panelLdlLayout.setHorizontalGroup(
            panelLdlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLdlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLdlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLdlLayout.createSequentialGroup()
                        .addComponent(sliderLdl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtLdl, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboLdlcMeasurement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblLdl))
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(panelLdlLayout.createSequentialGroup()
                .addComponent(panelCholesterol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelLdlLayout.setVerticalGroup(
            panelLdlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLdlLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblLdl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLdlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sliderLdl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelLdlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtLdl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboLdlcMeasurement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelCholesterol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        sliderAge.setMaximum(140);
        sliderAge.setValue(0);

        lblAge.setText("Age (Years)");

        txtAge.setText("0");

        javax.swing.GroupLayout panelAgeLayout = new javax.swing.GroupLayout(panelAge);
        panelAge.setLayout(panelAgeLayout);
        panelAgeLayout.setHorizontalGroup(
            panelAgeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAgeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAgeLayout.createSequentialGroup()
                        .addComponent(sliderAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(txtAge, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelAgeLayout.createSequentialGroup()
                        .addComponent(lblAge)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelAgeLayout.setVerticalGroup(
            panelAgeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAgeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblAge)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelAgeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sliderAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        radioDiabetes.setText("Diabetes");

        radioSmoker.setText("Smoker");
        radioSmoker.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                radioSmokerActionPerformed(evt);
            }
        });

        lblCvdTenYearRisk.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        lblCvdTenYearRisk.setText("10 Year CVD Risk");

        lblCvdRisk.setFont(new java.awt.Font("Calibri", 1, 48)); // NOI18N
        lblCvdRisk.setText("0%");

        btnSaveResult.setText("Save Result");

        sliderHdl.setMaximum(70);
        sliderHdl.setValue(0);

        lblHdl.setText("HDL-C");

        txtHdl.setText("0");

        comboHdlcMeasurement.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "mg/dl", "mmol/L" }));
        comboHdlcMeasurement.setToolTipText("");

        javax.swing.GroupLayout panelHdlLayout = new javax.swing.GroupLayout(panelHdl);
        panelHdl.setLayout(panelHdlLayout);
        panelHdlLayout.setHorizontalGroup(
            panelHdlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHdlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHdlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHdlLayout.createSequentialGroup()
                        .addComponent(sliderHdl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(txtHdl, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelHdlLayout.createSequentialGroup()
                        .addComponent(lblHdl)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboHdlcMeasurement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelHdlLayout.setVerticalGroup(
            panelHdlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHdlLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblHdl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelHdlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sliderHdl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelHdlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtHdl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboHdlcMeasurement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        radioGenderMale.setText("Male");

        radioGenderFemale.setText("Female");

        lblGender.setText("Gender");

        sliderBloodPressure.setMaximum(200);
        sliderBloodPressure.setValue(0);

        lblBloodPressure.setText("Blood Pressure (mm Hg)");

        txtBloodPressure.setText("0");

        comboBloodPressureType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Systolic", "Diastolic" }));

        javax.swing.GroupLayout panelBloodPressureLayout = new javax.swing.GroupLayout(panelBloodPressure);
        panelBloodPressure.setLayout(panelBloodPressureLayout);
        panelBloodPressureLayout.setHorizontalGroup(
            panelBloodPressureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBloodPressureLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBloodPressureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBloodPressureLayout.createSequentialGroup()
                        .addComponent(sliderBloodPressure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBloodPressure, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblBloodPressure))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(comboBloodPressureType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelBloodPressureLayout.setVerticalGroup(
            panelBloodPressureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBloodPressureLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblBloodPressure)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBloodPressureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sliderBloodPressure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBloodPressureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtBloodPressure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBloodPressureType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        javax.swing.GroupLayout panelCalculatorLayout = new javax.swing.GroupLayout(panelCalculator);
        panelCalculator.setLayout(panelCalculatorLayout);
        panelCalculatorLayout.setHorizontalGroup(
            panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCalculatorLayout.createSequentialGroup()
                .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCalculatorLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGender)
                            .addGroup(panelCalculatorLayout.createSequentialGroup()
                                .addComponent(radioGenderMale)
                                .addGap(18, 18, 18)
                                .addComponent(radioGenderFemale))))
                    .addGroup(panelCalculatorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelLdl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelHdl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelBloodPressure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSaveResult, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCvdTenYearRisk, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCalculatorLayout.createSequentialGroup()
                                .addComponent(lblCvdRisk)
                                .addGap(58, 58, 58))
                            .addGroup(panelCalculatorLayout.createSequentialGroup()
                                .addComponent(radioDiabetes)
                                .addGap(18, 18, 18)
                                .addComponent(radioSmoker)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCalculatorLayout.setVerticalGroup(
            panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCalculatorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblGender)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioGenderMale)
                    .addComponent(radioGenderFemale))
                .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCalculatorLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(panelAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelBloodPressure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelHdl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelLdl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelCalculatorLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radioDiabetes)
                            .addComponent(radioSmoker))
                        .addGap(28, 28, 28)
                        .addComponent(lblCvdTenYearRisk)
                        .addGap(8, 8, 8)
                        .addComponent(lblCvdRisk)
                        .addGap(18, 18, 18)
                        .addComponent(btnSaveResult)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Patient History"));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 473, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelCalculator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelCalculator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void radioSmokerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_radioSmokerActionPerformed
    {//GEN-HEADEREND:event_radioSmokerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioSmokerActionPerformed

    private void txtLdlActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtLdlActionPerformed
    {//GEN-HEADEREND:event_txtLdlActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLdlActionPerformed

    private void comboCholesterolMeasurementActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_comboCholesterolMeasurementActionPerformed
    {//GEN-HEADEREND:event_comboCholesterolMeasurementActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboCholesterolMeasurementActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(CalculatorScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(CalculatorScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(CalculatorScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(CalculatorScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new CalculatorScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSaveResult;
    private javax.swing.JComboBox comboBloodPressureType;
    private javax.swing.JComboBox comboCholesterolMeasurement;
    private javax.swing.JComboBox comboHdlcMeasurement;
    private javax.swing.JComboBox comboLdlcMeasurement;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblAge;
    private javax.swing.JLabel lblBloodPressure;
    private javax.swing.JLabel lblCholesterol;
    private javax.swing.JLabel lblCvdRisk;
    private javax.swing.JLabel lblCvdTenYearRisk;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblHdl;
    private javax.swing.JLabel lblLdl;
    private javax.swing.JPanel panelAge;
    private javax.swing.JPanel panelBloodPressure;
    private javax.swing.JPanel panelCalculator;
    private javax.swing.JPanel panelCholesterol;
    private javax.swing.JPanel panelHdl;
    private javax.swing.JPanel panelLdl;
    private javax.swing.JRadioButton radioDiabetes;
    private javax.swing.JRadioButton radioGenderFemale;
    private javax.swing.JRadioButton radioGenderMale;
    private javax.swing.JRadioButton radioSmoker;
    private javax.swing.JSlider sliderAge;
    private javax.swing.JSlider sliderBloodPressure;
    private javax.swing.JSlider sliderCholesterol;
    private javax.swing.JSlider sliderHdl;
    private javax.swing.JSlider sliderLdl;
    private javax.swing.JTextField txtAge;
    private javax.swing.JTextField txtBloodPressure;
    private javax.swing.JTextField txtCholesterol;
    private javax.swing.JTextField txtHdl;
    private javax.swing.JTextField txtLdl;
    // End of variables declaration//GEN-END:variables

    private void groupGenderButtons()
    {
        ButtonGroup genders = new ButtonGroup();
        genders.add(radioGenderMale);
        genders.add(radioGenderFemale);
    }

}
