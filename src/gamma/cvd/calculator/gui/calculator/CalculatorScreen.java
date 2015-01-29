package gamma.cvd.calculator.gui.calculator;

import gamma.cvd.calculator.CVDRiskData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
    private CVDRiskData model = new CVDRiskData();


    /**
     * Creates new form CalculatorScreen
     */
    public CalculatorScreen()
    {
        initComponents();
        groupGenderButtons();
        groupCholesterolButtons();
        addListeners();
    }

    private void addListeners()
    {
        // Add listeners to update text field to corresponding slider value upon slider change. 
        addSliderChangeListener(sliderAge, txtAge);
        addSliderChangeListener(sliderBloodPressure, txtBloodPressure);
        addSliderChangeListener(sliderCholesterol, txtCholesterol);
        addSliderChangeListener(sliderHdl, txtHdl);

        // Add listeners to update slider upon entering value corresponding in text field
        addTextChangeListener(sliderAge, txtAge);
        addTextChangeListener(sliderBloodPressure, txtBloodPressure);
        addTextChangeListener(sliderCholesterol, txtCholesterol);
        addTextChangeListener(sliderHdl, txtHdl);


        addCheckboxChangeListener(checkboxDiabetes);
        addCheckboxChangeListener(checkboxSmoker);
        
        addRadioChangeListener(radioGenderMale);
        addRadioChangeListener(radioGenderFemale);
        addRadioChangeListener(radioCholesterol);
        addRadioChangeListener(radioLdlC);
        
        addComboChangeListener(comboBloodPressureType);
        addComboChangeListener(comboCholesterolMeasurement);
        addComboChangeListener(comboHdlcMeasurement);
        
    }

    private void addCheckboxChangeListener(JCheckBox checkbox)
    {
        checkbox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                calculateCvdRisk();
            }
        });
    }

    private void addRadioChangeListener(JRadioButton radioButton)
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
        model.setIsDiabetic(checkboxDiabetes.isSelected());
        model.setIsSmoker(checkboxSmoker.isSelected());
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
        
        cholesterolValue = Integer.parseInt(txtCholesterol.getText());
        cholesterolMeasurement = comboCholesterolMeasurement.getSelectedItem().toString();
            
        if (radioLdlC.isSelected())
        {
            cholesterolType = CVDRiskData.LDL;
        } 
        else
        {
            cholesterolType = CVDRiskData.CHOL;
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
        panelCholesterol = new javax.swing.JPanel();
        sliderCholesterol = new javax.swing.JSlider();
        txtCholesterol = new javax.swing.JTextField();
        comboCholesterolMeasurement = new javax.swing.JComboBox();
        radioCholesterol = new javax.swing.JRadioButton();
        radioLdlC = new javax.swing.JRadioButton();
        panelAge = new javax.swing.JPanel();
        sliderAge = new javax.swing.JSlider();
        lblAge = new javax.swing.JLabel();
        txtAge = new javax.swing.JTextField();
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
        checkboxSmoker = new javax.swing.JCheckBox();
        checkboxDiabetes = new javax.swing.JCheckBox();
        btnSummary = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("NHS CVD Calculator");

        panelCalculator.setBorder(javax.swing.BorderFactory.createTitledBorder("CVD Calculator"));

        sliderCholesterol.setMaximum(320);
        sliderCholesterol.setValue(0);

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
                .addComponent(sliderCholesterol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCholesterol, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(comboCholesterolMeasurement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCholesterolLayout.setVerticalGroup(
            panelCholesterolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCholesterolLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCholesterolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sliderCholesterol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelCholesterolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCholesterol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboCholesterolMeasurement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        radioCholesterol.setText("Cholesterol");

        radioLdlC.setText("LDL-C");

        javax.swing.GroupLayout panelLdlLayout = new javax.swing.GroupLayout(panelLdl);
        panelLdl.setLayout(panelLdlLayout);
        panelLdlLayout.setHorizontalGroup(
            panelLdlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLdlLayout.createSequentialGroup()
                .addGroup(panelLdlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelCholesterol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelLdlLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(radioCholesterol)
                        .addGap(20, 20, 20)
                        .addComponent(radioLdlC)))
                .addGap(0, 18, Short.MAX_VALUE))
        );
        panelLdlLayout.setVerticalGroup(
            panelLdlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLdlLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelLdlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioCholesterol)
                    .addComponent(radioLdlC))
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

        checkboxSmoker.setText("Smoker");
        checkboxSmoker.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                checkboxSmokerActionPerformed(evt);
            }
        });

        checkboxDiabetes.setText("Diabetes");
        checkboxDiabetes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                checkboxDiabetesActionPerformed(evt);
            }
        });

        btnSummary.setText("Patient Summary");
        btnSummary.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSummaryActionPerformed(evt);
            }
        });

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
                        .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCalculatorLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(checkboxDiabetes)
                                .addGap(18, 18, 18)
                                .addComponent(checkboxSmoker))
                            .addGroup(panelCalculatorLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnSaveResult, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCvdTenYearRisk, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCalculatorLayout.createSequentialGroup()
                                        .addComponent(lblCvdRisk)
                                        .addGap(58, 58, 58))
                                    .addComponent(btnSummary, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 2, Short.MAX_VALUE)))))
                .addGap(10, 10, 10))
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
                .addGap(14, 14, 14)
                .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(checkboxSmoker)
                        .addComponent(checkboxDiabetes)))
                .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCalculatorLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelBloodPressure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelHdl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelLdl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelCalculatorLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(lblCvdTenYearRisk)
                        .addGap(8, 8, 8)
                        .addComponent(lblCvdRisk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSaveResult)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSummary)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Patient History"));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 462, Short.MAX_VALUE)
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

    private void comboCholesterolMeasurementActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_comboCholesterolMeasurementActionPerformed
    {//GEN-HEADEREND:event_comboCholesterolMeasurementActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboCholesterolMeasurementActionPerformed

    private void checkboxSmokerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_checkboxSmokerActionPerformed
    {//GEN-HEADEREND:event_checkboxSmokerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkboxSmokerActionPerformed

    private void checkboxDiabetesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_checkboxDiabetesActionPerformed
    {//GEN-HEADEREND:event_checkboxDiabetesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkboxDiabetesActionPerformed

    private void btnSummaryActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSummaryActionPerformed
    {//GEN-HEADEREND:event_btnSummaryActionPerformed
                new PatientSummaryScreen(model).setVisible(true);  
    }//GEN-LAST:event_btnSummaryActionPerformed

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
    private javax.swing.JButton btnSummary;
    private javax.swing.JCheckBox checkboxDiabetes;
    private javax.swing.JCheckBox checkboxSmoker;
    private javax.swing.JComboBox comboBloodPressureType;
    private javax.swing.JComboBox comboCholesterolMeasurement;
    private javax.swing.JComboBox comboHdlcMeasurement;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblAge;
    private javax.swing.JLabel lblBloodPressure;
    private javax.swing.JLabel lblCvdRisk;
    private javax.swing.JLabel lblCvdTenYearRisk;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblHdl;
    private javax.swing.JPanel panelAge;
    private javax.swing.JPanel panelBloodPressure;
    private javax.swing.JPanel panelCalculator;
    private javax.swing.JPanel panelCholesterol;
    private javax.swing.JPanel panelHdl;
    private javax.swing.JPanel panelLdl;
    private javax.swing.JRadioButton radioCholesterol;
    private javax.swing.JRadioButton radioGenderFemale;
    private javax.swing.JRadioButton radioGenderMale;
    private javax.swing.JRadioButton radioLdlC;
    private javax.swing.JSlider sliderAge;
    private javax.swing.JSlider sliderBloodPressure;
    private javax.swing.JSlider sliderCholesterol;
    private javax.swing.JSlider sliderHdl;
    private javax.swing.JTextField txtAge;
    private javax.swing.JTextField txtBloodPressure;
    private javax.swing.JTextField txtCholesterol;
    private javax.swing.JTextField txtHdl;
    // End of variables declaration//GEN-END:variables

    private void groupGenderButtons()
    {
        ButtonGroup genders = new ButtonGroup();
        genders.add(radioGenderMale);
        genders.add(radioGenderFemale);
    }
    
    private void groupCholesterolButtons()
    {
        ButtonGroup cholesterols = new ButtonGroup();
        cholesterols.add(radioCholesterol);
        cholesterols.add(radioLdlC);
    }

}
