package gamma.cvd.calculator.gui.calculator;

import gamma.cvd.calculator.CVDPatient;
import gamma.cvd.calculator.CVDRiskData;
import gamma.cvd.calculator.gui.GuiUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDate;
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
    private CVDRiskData model;
    

    /**
     * Creates new form CalculatorScreen
     */
    public CalculatorScreen(CVDPatient patient)
    {
        GuiUtils.centerScreen(this);
        initComponents();
        groupGenderButtons();
        groupCholesterolButtons();
        addListeners();
    }

    private void addListeners()
    {
        // Add listeners to update text field to corresponding slider value upon slider change. 
        addSliderChangeListener(sliderAge, txtAge);
        addSliderChangeListener(sliderBloodPressureDiastolic, txtBloodPressureDiastolic);
        addSliderChangeListener(sliderCholesterol, txtCholesterol);
        addSliderChangeListener(sliderHdl, txtHdl);
        addSliderChangeListener(sliderBloodPressureSystolic, txtBloodPressureSystolic);
        
        // Add listeners to update slider upon entering value corresponding in text field
        addTextChangeListener(sliderAge, txtAge);
        addTextChangeListener(sliderBloodPressureDiastolic, txtBloodPressureDiastolic);
        addTextChangeListener(sliderBloodPressureSystolic, txtBloodPressureSystolic);
        addTextChangeListener(sliderCholesterol, txtCholesterol);
        addTextChangeListener(sliderHdl, txtHdl);


        addCheckboxChangeListener(checkboxDiabetes);
        addCheckboxChangeListener(checkboxSmoker);
        
        addRadioChangeListener(radioGenderMale);
        addRadioChangeListener(radioGenderFemale);
        addRadioChangeListener(radioCholesterol);
        addRadioChangeListener(radioLdlC);
        
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
                if (model != null)
                {
                    calculateCvdRisk();
                }
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
                if (model != null)
                {
                    calculateCvdRisk();
                }
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
                if (model != null)
                {
                    calculateCvdRisk();
                }
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
                        if (model != null)
                        {
                            calculateCvdRisk();
                        }
                   }
               });
    }
    
    private void calculateCvdRisk()
    {
        final String MGDL_MEASUREMENT = "mg/dl";
        final String MMOL_MEASUREMENT = "mmol/L";

        int bloodPressureSystolicMmHg;
        int bloodPressureDiastolicMmHg;

        String cholesterolType;
        String cholesterolMeasurement;
        String hdlMeasurement;
        float cholesterolMmolL;
        float hdlMmolL;
        int cholesterolMgDl;
        int hdlMgDl;
        int age;
        char sex;
        boolean isDiabetic;
        boolean isSmoker;

        cholesterolMeasurement =
                comboCholesterolMeasurement.getSelectedItem().toString();
        hdlMeasurement = comboHdlcMeasurement.getSelectedItem().toString();
        
        if (radioGenderFemale.isSelected())
        {
            sex = CVDRiskData.FEMALE;
        } else
        {
            sex = CVDRiskData.MALE;
        }
        
        age = Integer.parseInt(txtAge.getText());
        
        if (radioLdlC.isSelected())
        {
            cholesterolType = CVDRiskData.LDL;
        } 
        else
        {
            cholesterolType = CVDRiskData.CHOL;
        }
        
        bloodPressureSystolicMmHg =
                Integer.parseInt(txtBloodPressureSystolic.getText());
        bloodPressureDiastolicMmHg =
                Integer.parseInt(txtBloodPressureDiastolic.getText());
        
        isDiabetic = checkboxDiabetes.isSelected();
        isSmoker = checkboxSmoker.isSelected();
        
        if (cholesterolMeasurement.equals(MMOL_MEASUREMENT) &&
                hdlMeasurement.equals(MMOL_MEASUREMENT)) {
            cholesterolMmolL = Float.parseFloat(txtCholesterol.getText());
            hdlMmolL = Float.parseFloat(txtHdl.getText());
            model = new CVDRiskData(sex, age, cholesterolType, cholesterolMmolL,
                    hdlMmolL, bloodPressureSystolicMmHg,
                    bloodPressureDiastolicMmHg, isDiabetic, isSmoker,
                    LocalDate.now());
            Integer score = model.calculateRiskScore();
            Integer risk = model.getRiskPercentage(score);
            lblCvdRisk.setText(risk.toString() + "%");
        } else if (cholesterolMeasurement.equals(MGDL_MEASUREMENT) &&
                hdlMeasurement.equals(MMOL_MEASUREMENT)) {
            cholesterolMgDl = Integer.parseInt(txtCholesterol.getText());
            hdlMmolL = Float.parseFloat(txtHdl.getText());
            model = new CVDRiskData(sex, age, cholesterolType, cholesterolMgDl,
                    hdlMmolL, bloodPressureSystolicMmHg,
                    bloodPressureDiastolicMmHg, isDiabetic, isSmoker,
                    LocalDate.now());
            Integer score = model.calculateRiskScore();
            Integer risk = model.getRiskPercentage(score);
            lblCvdRisk.setText(risk.toString() + "%");
        } else if (cholesterolMeasurement.equals(MMOL_MEASUREMENT) &&
                hdlMeasurement.equals(MGDL_MEASUREMENT)) {
            cholesterolMmolL = Float.parseFloat(txtCholesterol.getText());
            hdlMgDl = Integer.parseInt(txtHdl.getText());
            model = new CVDRiskData(sex, age, cholesterolType, cholesterolMmolL,
                    hdlMgDl, bloodPressureSystolicMmHg,
                    bloodPressureDiastolicMmHg, isDiabetic, isSmoker,
                    LocalDate.now());
            Integer score = model.calculateRiskScore();
            Integer risk = model.getRiskPercentage(score);
            lblCvdRisk.setText(risk.toString() + "%");
        } else if (cholesterolMeasurement.equals(MGDL_MEASUREMENT) &&
                hdlMeasurement.equals(MGDL_MEASUREMENT)) {
            cholesterolMgDl = Integer.parseInt(txtCholesterol.getText());
            hdlMgDl = Integer.parseInt(txtHdl.getText());
            model = new CVDRiskData(sex, age, cholesterolType, cholesterolMgDl,
                    hdlMgDl, bloodPressureSystolicMmHg,
                    bloodPressureDiastolicMmHg, isDiabetic, isSmoker,
                    LocalDate.now());
            Integer score = model.calculateRiskScore();
            Integer risk = model.getRiskPercentage(score);
            lblCvdRisk.setText(risk.toString() + "%");
        }
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
    private void initComponents() {

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
        radioGenderMale = new javax.swing.JRadioButton();
        radioGenderFemale = new javax.swing.JRadioButton();
        lblGender = new javax.swing.JLabel();
        checkboxSmoker = new javax.swing.JCheckBox();
        btnSummary = new javax.swing.JButton();
        panelBloodPressureDiastolic = new javax.swing.JPanel();
        sliderBloodPressureDiastolic = new javax.swing.JSlider();
        lblBloodPressureDiastolic = new javax.swing.JLabel();
        txtBloodPressureDiastolic = new javax.swing.JTextField();
        panelHdl = new javax.swing.JPanel();
        sliderHdl = new javax.swing.JSlider();
        lblHdl = new javax.swing.JLabel();
        txtHdl = new javax.swing.JTextField();
        comboHdlcMeasurement = new javax.swing.JComboBox();
        panelBloodPressureSystolic = new javax.swing.JPanel();
        sliderBloodPressureSystolic = new javax.swing.JSlider();
        lblBloodPressureSystolic = new javax.swing.JLabel();
        txtBloodPressureSystolic = new javax.swing.JTextField();
        checkboxDiabetes = new javax.swing.JCheckBox();
        btnCalculateRisk = new javax.swing.JButton();
        panelHistory = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("NHS CVD Calculator");

        panelCalculator.setBorder(javax.swing.BorderFactory.createTitledBorder("CVD Calculator"));

        sliderCholesterol.setMaximum(320);
        sliderCholesterol.setValue(0);

        txtCholesterol.setText("0");
        txtCholesterol.setToolTipText("");
        txtCholesterol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCholesterolActionPerformed(evt);
            }
        });

        comboCholesterolMeasurement.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "mg/dl", "mmol/L" }));
        comboCholesterolMeasurement.setToolTipText("");
        comboCholesterolMeasurement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
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
                .addGap(0, 0, Short.MAX_VALUE))
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
                .addGap(136, 136, 136))
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

        radioGenderMale.setText("Male");

        radioGenderFemale.setText("Female");

        lblGender.setText("Gender");

        checkboxSmoker.setText("Smoker");
        checkboxSmoker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxSmokerActionPerformed(evt);
            }
        });

        btnSummary.setText("Patient Summary");
        btnSummary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSummaryActionPerformed(evt);
            }
        });

        sliderBloodPressureDiastolic.setMaximum(200);
        sliderBloodPressureDiastolic.setValue(0);

        lblBloodPressureDiastolic.setText("Blood Pressure Diastolic (mm Hg)");

        txtBloodPressureDiastolic.setText("0");

        javax.swing.GroupLayout panelBloodPressureDiastolicLayout = new javax.swing.GroupLayout(panelBloodPressureDiastolic);
        panelBloodPressureDiastolic.setLayout(panelBloodPressureDiastolicLayout);
        panelBloodPressureDiastolicLayout.setHorizontalGroup(
            panelBloodPressureDiastolicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBloodPressureDiastolicLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBloodPressureDiastolicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBloodPressureDiastolic)
                    .addGroup(panelBloodPressureDiastolicLayout.createSequentialGroup()
                        .addComponent(sliderBloodPressureDiastolic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBloodPressureDiastolic, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(152, Short.MAX_VALUE))
        );
        panelBloodPressureDiastolicLayout.setVerticalGroup(
            panelBloodPressureDiastolicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBloodPressureDiastolicLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblBloodPressureDiastolic)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBloodPressureDiastolicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sliderBloodPressureDiastolic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBloodPressureDiastolic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

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

        sliderBloodPressureSystolic.setMaximum(200);
        sliderBloodPressureSystolic.setValue(0);

        lblBloodPressureSystolic.setText("Blood Pressure Systolic (mm Hg)");

        txtBloodPressureSystolic.setText("0");

        javax.swing.GroupLayout panelBloodPressureSystolicLayout = new javax.swing.GroupLayout(panelBloodPressureSystolic);
        panelBloodPressureSystolic.setLayout(panelBloodPressureSystolicLayout);
        panelBloodPressureSystolicLayout.setHorizontalGroup(
            panelBloodPressureSystolicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBloodPressureSystolicLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBloodPressureSystolicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBloodPressureSystolicLayout.createSequentialGroup()
                        .addComponent(sliderBloodPressureSystolic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBloodPressureSystolic, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblBloodPressureSystolic))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        panelBloodPressureSystolicLayout.setVerticalGroup(
            panelBloodPressureSystolicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBloodPressureSystolicLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblBloodPressureSystolic)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBloodPressureSystolicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sliderBloodPressureSystolic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBloodPressureSystolic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        checkboxDiabetes.setText("Diabetes");
        checkboxDiabetes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxDiabetesActionPerformed(evt);
            }
        });

        btnCalculateRisk.setText("Calculate Risk");
        btnCalculateRisk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateRiskActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCalculatorLayout = new javax.swing.GroupLayout(panelCalculator);
        panelCalculator.setLayout(panelCalculatorLayout);
        panelCalculatorLayout.setHorizontalGroup(
            panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCalculatorLayout.createSequentialGroup()
                .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCalculatorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelCalculatorLayout.createSequentialGroup()
                                .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panelLdl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panelBloodPressureSystolic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panelHdl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(37, 37, 37)
                                .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblCvdTenYearRisk, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCalculatorLayout.createSequentialGroup()
                                                .addComponent(lblCvdRisk)
                                                .addGap(58, 58, 58)))
                                        .addComponent(btnCalculateRisk, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnSaveResult, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnSummary, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE))
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
                                .addComponent(panelBloodPressureDiastolic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelCalculatorLayout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(checkboxSmoker)
                                .addGap(53, 53, 53)
                                .addComponent(checkboxDiabetes)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
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
                .addComponent(panelAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCalculatorLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(panelLdl, javax.swing.GroupLayout.PREFERRED_SIZE, 105, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelHdl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(panelBloodPressureSystolic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))
                    .addGroup(panelCalculatorLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addComponent(lblCvdTenYearRisk)
                        .addGap(8, 8, 8)
                        .addComponent(lblCvdRisk)
                        .addGap(22, 22, 22)
                        .addComponent(btnCalculateRisk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSaveResult)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSummary)
                        .addGap(50, 50, 50)))
                .addComponent(panelBloodPressureDiastolic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkboxSmoker)
                    .addComponent(checkboxDiabetes))
                .addContainerGap())
        );

        panelHistory.setBorder(javax.swing.BorderFactory.createTitledBorder("Patient History"));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout panelHistoryLayout = new javax.swing.GroupLayout(panelHistory);
        panelHistory.setLayout(panelHistoryLayout);
        panelHistoryLayout.setHorizontalGroup(
            panelHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHistoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(365, Short.MAX_VALUE))
        );
        panelHistoryLayout.setVerticalGroup(
            panelHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHistoryLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelCalculator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelCalculator, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelHistory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3))
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

    private void txtCholesterolActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCholesterolActionPerformed
    {//GEN-HEADEREND:event_txtCholesterolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCholesterolActionPerformed

    private void btnCalculateRiskActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCalculateRiskActionPerformed
    {//GEN-HEADEREND:event_btnCalculateRiskActionPerformed
        calculateCvdRisk();
    }//GEN-LAST:event_btnCalculateRiskActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalculateRisk;
    private javax.swing.JButton btnSaveResult;
    private javax.swing.JButton btnSummary;
    private javax.swing.JCheckBox checkboxDiabetes;
    private javax.swing.JCheckBox checkboxSmoker;
    private javax.swing.JComboBox comboCholesterolMeasurement;
    private javax.swing.JComboBox comboHdlcMeasurement;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel lblAge;
    private javax.swing.JLabel lblBloodPressureDiastolic;
    private javax.swing.JLabel lblBloodPressureSystolic;
    private javax.swing.JLabel lblCvdRisk;
    private javax.swing.JLabel lblCvdTenYearRisk;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblHdl;
    private javax.swing.JPanel panelAge;
    private javax.swing.JPanel panelBloodPressureDiastolic;
    private javax.swing.JPanel panelBloodPressureSystolic;
    private javax.swing.JPanel panelCalculator;
    private javax.swing.JPanel panelCholesterol;
    private javax.swing.JPanel panelHdl;
    private javax.swing.JPanel panelHistory;
    private javax.swing.JPanel panelLdl;
    private javax.swing.JRadioButton radioCholesterol;
    private javax.swing.JRadioButton radioGenderFemale;
    private javax.swing.JRadioButton radioGenderMale;
    private javax.swing.JRadioButton radioLdlC;
    private javax.swing.JSlider sliderAge;
    private javax.swing.JSlider sliderBloodPressureDiastolic;
    private javax.swing.JSlider sliderBloodPressureSystolic;
    private javax.swing.JSlider sliderCholesterol;
    private javax.swing.JSlider sliderHdl;
    private javax.swing.JTextField txtAge;
    private javax.swing.JTextField txtBloodPressureDiastolic;
    private javax.swing.JTextField txtBloodPressureSystolic;
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
