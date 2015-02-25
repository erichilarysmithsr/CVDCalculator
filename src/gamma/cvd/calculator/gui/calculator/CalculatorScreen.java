package gamma.cvd.calculator.gui.calculator;

import gamma.cvd.calculator.CVDPatient;
import gamma.cvd.calculator.CVDPatientDataParser;
import gamma.cvd.calculator.CVDRiskData;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

/**
 *
 * @author Jack
 */
public class CalculatorScreen extends javax.swing.JFrame {

    private CVDRiskData model;
    private CVDPatient patient;
    private CVDPatientDataParser patientParser;
    private CalculatorGraph graph;
    
    // Boolean to avoid event triggers interfering with certain values during init.
    private boolean firstLoad = true;

    /**
     * Creates new form CalculatorScreen
     */
    public CalculatorScreen(CVDPatient patient) {
        try {
            this.patient = patient;
            this.patientParser = new CVDPatientDataParser();
            initComponents();
            groupGenderButtons();
            groupCholesterolButtons();
            addListeners();
            this.graph = new CalculatorGraph(panelGraph, patient, comboStaticOption);                        
            graph.DrawRiskGraph();
            lblNamePlaceholder.setText(patient.getFirstName()+" "+patient.getLastName());
            if (patient.getRiskData().size() > 0) {
                comboCholesterolMeasurement.setSelectedIndex(1);
                comboHdlcMeasurement.setSelectedIndex(1);
                CVDRiskData lastResult = patient.getRiskData().get(patient.getRiskData().size() - 1);
                loadPatientsResults(lastResult);
                calculateCvdRisk();
                loadPreviousAssessmentDates();
            }

            firstLoad = false;
        } catch (SAXException | IOException | GeneralSecurityException | XPathExpressionException ex) {
            Logger.getLogger(CalculatorScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addListeners() {
        // Add listeners to update text field to corresponding slider value upon slider change. 
        addSliderChangeListener(sliderAge, txtAge);
        addSliderChangeListener(sliderBloodPressureDiastolic, txtBloodPressureDiastolic);
        addSliderChangeListener(sliderBloodPressureSystolic, txtBloodPressureSystolic);

        // Add listeners to update slider upon entering value corresponding in text field
        addTextChangeListener(sliderAge, txtAge);
        addTextChangeListener(sliderBloodPressureDiastolic, txtBloodPressureDiastolic);
        addTextChangeListener(sliderBloodPressureSystolic, txtBloodPressureSystolic);

        addMeasurementChangeListeners(sliderCholesterol, txtCholesterol, comboCholesterolMeasurement);
        addMeasurementChangeListeners(sliderHdl, txtHdl, comboHdlcMeasurement);

        addCheckboxChangeListener(checkboxDiabetes);
        addCheckboxChangeListener(checkboxSmoker);

        addRadioChangeListener(radioGenderMale);
        addRadioChangeListener(radioGenderFemale);
        addRadioChangeListener(radioCholesterol);
        addRadioChangeListener(radioLdlC);
    }

    private void addCheckboxChangeListener(JCheckBox checkbox) {
        checkbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model != null) {
                    calculateCvdRisk();
                }
            }
        });
    }

    private void addRadioChangeListener(JRadioButton radioButton) {
        radioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model != null) {
                    calculateCvdRisk();
                }
            }
        });
    }

    // Upon movement of the slider, will update the corresponding text field with its value. 
    private void addSliderChangeListener(JSlider slider, final JTextField field) {
        ChangeListener listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                // update text field when the slider value changes
                JSlider source = (JSlider) event.getSource();
                field.setText("" + source.getValue());
                if (model != null) {
                    calculateCvdRisk();
                }
            }
        };

        slider.addChangeListener(listener);
    }

    private void addComboChangeListener(JComboBox comboBox) {
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model != null) {

                    calculateCvdRisk();
                }
            }
        });
    }

    private void calculateCvdRisk() {
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

        cholesterolMeasurement
                = comboCholesterolMeasurement.getSelectedItem().toString();
        hdlMeasurement = comboHdlcMeasurement.getSelectedItem().toString();

        if (radioGenderFemale.isSelected()) {
            sex = CVDRiskData.FEMALE;
        } else {
            sex = CVDRiskData.MALE;
        }

        age = Integer.parseInt(txtAge.getText());

        if (radioLdlC.isSelected()) {
            cholesterolType = CVDRiskData.LDL;
        } else {
            cholesterolType = CVDRiskData.CHOL;
        }

        bloodPressureSystolicMmHg
                = Integer.parseInt(txtBloodPressureSystolic.getText());
        bloodPressureDiastolicMmHg
                = Integer.parseInt(txtBloodPressureDiastolic.getText());

        isDiabetic = checkboxDiabetes.isSelected();
        isSmoker = checkboxSmoker.isSelected();

        if (cholesterolMeasurement.equals(MMOL_MEASUREMENT)
                && hdlMeasurement.equals(MMOL_MEASUREMENT)) {
            cholesterolMmolL = Float.parseFloat(txtCholesterol.getText());
            hdlMmolL = Float.parseFloat(txtHdl.getText());
            model = new CVDRiskData(sex, age, cholesterolType, cholesterolMmolL,
                    hdlMmolL, bloodPressureSystolicMmHg,
                    bloodPressureDiastolicMmHg, isDiabetic, isSmoker,
                    LocalDate.now());
            Integer score = model.calculateRiskScore();
            Integer risk = model.getRiskPercentage(score);
            lblCvdRisk.setText(risk.toString() + "%");
        } else if (cholesterolMeasurement.equals(MGDL_MEASUREMENT)
                && hdlMeasurement.equals(MMOL_MEASUREMENT)) {
            cholesterolMgDl = Integer.parseInt(txtCholesterol.getText());
            hdlMmolL = Float.parseFloat(txtHdl.getText());
            model = new CVDRiskData(sex, age, cholesterolType, cholesterolMgDl,
                    hdlMmolL, bloodPressureSystolicMmHg,
                    bloodPressureDiastolicMmHg, isDiabetic, isSmoker,
                    LocalDate.now());
            Integer score = model.calculateRiskScore();
            Integer risk = model.getRiskPercentage(score);
            lblCvdRisk.setText(risk.toString() + "%");
        } else if (cholesterolMeasurement.equals(MMOL_MEASUREMENT)
                && hdlMeasurement.equals(MGDL_MEASUREMENT)) {
            cholesterolMmolL = Float.parseFloat(txtCholesterol.getText());
            hdlMgDl = Integer.parseInt(txtHdl.getText());
            model = new CVDRiskData(sex, age, cholesterolType, cholesterolMmolL,
                    hdlMgDl, bloodPressureSystolicMmHg,
                    bloodPressureDiastolicMmHg, isDiabetic, isSmoker,
                    LocalDate.now());
            Integer score = model.calculateRiskScore();
            Integer risk = model.getRiskPercentage(score);
            lblCvdRisk.setText(risk.toString() + "%");
        } else if (cholesterolMeasurement.equals(MGDL_MEASUREMENT)
                && hdlMeasurement.equals(MGDL_MEASUREMENT)) {
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
    private void addTextChangeListener(final JSlider slider, final JTextField field) {
        field.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int value = 0;
                try {
                    value = Integer.parseInt(field.getText());
                    slider.setValue(value);
                } catch (NumberFormatException x) {
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
        comboAssessmentDate = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        lblNamePlaceholder = new javax.swing.JLabel();
        panelHistory = new javax.swing.JPanel();
        comboStaticOption = new javax.swing.JComboBox();
        panelGraph = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("NHS CVD Calculator");

        panelCalculator.setBorder(javax.swing.BorderFactory.createTitledBorder("CVD Calculator"));

        sliderCholesterol.setMaximum(1000);
        sliderCholesterol.setMinimum(1);

        txtCholesterol.setText("1");
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboCholesterolMeasurement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
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
        sliderAge.setMinimum(1);

        lblAge.setText("Age (Years)");

        txtAge.setText("1");
        txtAge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAgeActionPerformed(evt);
            }
        });

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
        btnSaveResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveResultActionPerformed(evt);
            }
        });

        radioGenderMale.setText("Male");

        radioGenderFemale.setText("Female");

        lblGender.setText("Gender");

        checkboxSmoker.setText("Smoker");
        checkboxSmoker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxSmokerActionPerformed(evt);
            }
        });

        btnSummary.setText("View Patient Summary");
        btnSummary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSummaryActionPerformed(evt);
            }
        });

        sliderBloodPressureDiastolic.setMaximum(200);
        sliderBloodPressureDiastolic.setMinimum(1);

        lblBloodPressureDiastolic.setText("Blood Pressure Diastolic (mm Hg)");

        txtBloodPressureDiastolic.setText("1");

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

        sliderHdl.setMaximum(400);
        sliderHdl.setMinimum(1);

        lblHdl.setText("HDL-C");

        txtHdl.setText("1");

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
        sliderBloodPressureSystolic.setMinimum(1);

        lblBloodPressureSystolic.setText("Blood Pressure Systolic (mm Hg)");

        txtBloodPressureSystolic.setText("1");

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

        comboAssessmentDate.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Assessment Date" }));

        jLabel2.setText("Select a previous assesment");

        lblNamePlaceholder.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblNamePlaceholder.setText("Full name Placeholder");

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelCalculatorLayout.createSequentialGroup()
                        .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelCalculatorLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(comboAssessmentDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelCalculatorLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panelBloodPressureDiastolic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelCalculatorLayout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(checkboxSmoker)
                                .addGap(53, 53, 53)
                                .addComponent(checkboxDiabetes)))
                        .addGap(27, 27, 27)))
                .addContainerGap())
            .addGroup(panelCalculatorLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCalculatorLayout.createSequentialGroup()
                        .addComponent(lblGender)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelCalculatorLayout.createSequentialGroup()
                        .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNamePlaceholder)
                            .addGroup(panelCalculatorLayout.createSequentialGroup()
                                .addComponent(radioGenderMale)
                                .addGap(18, 18, 18)
                                .addComponent(radioGenderFemale)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panelCalculatorLayout.setVerticalGroup(
            panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCalculatorLayout.createSequentialGroup()
                .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelCalculatorLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(1, 1, 1)
                        .addComponent(comboAssessmentDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 171, Short.MAX_VALUE)
                        .addComponent(lblCvdTenYearRisk)
                        .addGap(8, 8, 8)
                        .addComponent(lblCvdRisk)
                        .addGap(22, 22, 22)
                        .addComponent(btnCalculateRisk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSaveResult)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSummary)
                        .addGap(50, 50, 50))
                    .addGroup(panelCalculatorLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(lblNamePlaceholder)
                        .addGap(18, 18, 18)
                        .addComponent(lblGender)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radioGenderMale)
                            .addComponent(radioGenderFemale))
                        .addGap(18, 18, 18)
                        .addComponent(panelAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panelLdl, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelHdl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(panelBloodPressureSystolic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)))
                .addComponent(panelBloodPressureDiastolic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelCalculatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkboxSmoker)
                    .addComponent(checkboxDiabetes))
                .addContainerGap())
        );

        panelHistory.setBorder(javax.swing.BorderFactory.createTitledBorder("Patient History"));

        comboStaticOption.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Risk", "Cholesterol/LDL-C", "HDL-C", "Blood Pressure (Systolic)", "Blood Pressure (Diastolic)" }));

        javax.swing.GroupLayout panelGraphLayout = new javax.swing.GroupLayout(panelGraph);
        panelGraph.setLayout(panelGraphLayout);
        panelGraphLayout.setHorizontalGroup(
            panelGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 662, Short.MAX_VALUE)
        );
        panelGraphLayout.setVerticalGroup(
            panelGraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 499, Short.MAX_VALUE)
        );

        jLabel1.setText("Select a health category to display");

        javax.swing.GroupLayout panelHistoryLayout = new javax.swing.GroupLayout(panelHistory);
        panelHistory.setLayout(panelHistoryLayout);
        panelHistoryLayout.setHorizontalGroup(
            panelHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHistoryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHistoryLayout.createSequentialGroup()
                        .addGap(0, 21, Short.MAX_VALUE)
                        .addComponent(panelGraph, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelHistoryLayout.createSequentialGroup()
                        .addGroup(panelHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(comboStaticOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelHistoryLayout.setVerticalGroup(
            panelHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHistoryLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(comboStaticOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelGraph, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelCalculator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        calculateCvdRisk();
        new PatientSummaryScreen(model, patient).setVisible(true);
    }//GEN-LAST:event_btnSummaryActionPerformed

    private void txtCholesterolActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_txtCholesterolActionPerformed
    {//GEN-HEADEREND:event_txtCholesterolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCholesterolActionPerformed

    private void btnCalculateRiskActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCalculateRiskActionPerformed
    {//GEN-HEADEREND:event_btnCalculateRiskActionPerformed
        calculateCvdRisk();
    }//GEN-LAST:event_btnCalculateRiskActionPerformed

    private void btnSaveResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveResultActionPerformed
        try {
            calculateCvdRisk();
            patientParser.addRiskDataToPatient(patient, model);
            graph.setCVDPatient(patient);
            graph.DrawRiskGraph();
            loadPreviousAssessmentDates();
            
            JOptionPane.showMessageDialog(this, "Assessment succesfully saved", "Assessment saved", JOptionPane.INFORMATION_MESSAGE);

        } catch (XPathExpressionException | TransformerException | GeneralSecurityException | IOException ex) {
            Logger.getLogger(CalculatorScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSaveResultActionPerformed

    private void txtAgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAgeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAgeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalculateRisk;
    private javax.swing.JButton btnSaveResult;
    private javax.swing.JButton btnSummary;
    private javax.swing.JCheckBox checkboxDiabetes;
    private javax.swing.JCheckBox checkboxSmoker;
    private javax.swing.JComboBox comboAssessmentDate;
    private javax.swing.JComboBox comboCholesterolMeasurement;
    private javax.swing.JComboBox comboHdlcMeasurement;
    private javax.swing.JComboBox comboStaticOption;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblAge;
    private javax.swing.JLabel lblBloodPressureDiastolic;
    private javax.swing.JLabel lblBloodPressureSystolic;
    private javax.swing.JLabel lblCvdRisk;
    private javax.swing.JLabel lblCvdTenYearRisk;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblHdl;
    private javax.swing.JLabel lblNamePlaceholder;
    private javax.swing.JPanel panelAge;
    private javax.swing.JPanel panelBloodPressureDiastolic;
    private javax.swing.JPanel panelBloodPressureSystolic;
    private javax.swing.JPanel panelCalculator;
    private javax.swing.JPanel panelCholesterol;
    private javax.swing.JPanel panelGraph;
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

    private void groupGenderButtons() {
        ButtonGroup genders = new ButtonGroup();
        genders.add(radioGenderMale);
        genders.add(radioGenderFemale);
    }

    private void groupCholesterolButtons() {
        ButtonGroup cholesterols = new ButtonGroup();
        cholesterols.add(radioCholesterol);
        cholesterols.add(radioLdlC);
    }

    private void loadPatientsResults(CVDRiskData result) {

        if (result.getSex() == CVDRiskData.MALE) {
            radioGenderMale.setSelected(true);
        } else {
            radioGenderFemale.setSelected(true);
        }

        if (result.getCholesterolType().equals(CVDRiskData.CHOL)) {
            radioCholesterol.setSelected(true);
        } else {
            radioLdlC.setSelected(true);
        }

        checkboxDiabetes.setSelected(result.isDiabetic());
        checkboxSmoker.setSelected(result.isSmoker());

        txtAge.setText(Integer.toString(result.getAge()));
        txtBloodPressureDiastolic.setText(Integer.toString(result.getBloodPressureDiastolicMmHg()));
        txtBloodPressureSystolic.setText(Integer.toString(result.getBloodPressureSystolicMmHg()));
        txtCholesterol.setText(Float.toString(result.getCholesterolMmolL()));
        txtHdl.setText(Float.toString(result.getHdlMmolL()));

        sliderAge.setValue(result.getAge());
        sliderBloodPressureDiastolic.setValue(result.getBloodPressureDiastolicMmHg());
        sliderBloodPressureSystolic.setValue(result.getBloodPressureSystolicMmHg());
        sliderCholesterol.setValue(Math.round(result.getCholesterolMmolL()));
        sliderHdl.setValue(Math.round(result.getHdlMmolL()));
    }

    private void loadPreviousAssessmentDates() {
        patient.getRiskData().stream().forEach((data) -> {
            comboAssessmentDate.addItem(data.getTestDate().toString() + ": " + data.getTestId());
        });

        comboAssessmentDate.addActionListener((ActionEvent e) -> {
            firstLoad = true;
            patient.getRiskData().stream().forEach((data) -> {
                if (comboAssessmentDate.getSelectedIndex() > 0)
                {
                    String selection = comboAssessmentDate.getSelectedItem().toString();
                    String[] selectionData = selection.split(":");

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate date = LocalDate.parse(selectionData[0], dtf);
                    int testId = Integer.parseInt(selectionData[1].trim());
                    if (date.equals(data.getTestDate()) && data.getTestId() == testId) {
                        loadPatientsResults(data);
                    }
                }
            firstLoad = false;
            
            });
        });

    }

    private void addMeasurementChangeListeners(JSlider slider, JTextField value, JComboBox measurement) {

        ChangeListener listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                boolean isMgdlMeasurement = false;

                if (!firstLoad) {
                    if (measurement.getSelectedItem().toString().equals("mg/dl")) {
                        isMgdlMeasurement = true;
                    }

                    if (isMgdlMeasurement) {
                        // update text field when the slider value changes
                        JSlider source = (JSlider) event.getSource();
                        value.setText("" + source.getValue());
                        if (model != null) {
                            calculateCvdRisk();
                        }
                    } else {
                        JSlider source = (JSlider) event.getSource();
                        int sliderValue = source.getValue();
                        Float actualValue = (float) sliderValue / 100;

                        value.setText(actualValue.toString());
                        if (model != null) {
                            calculateCvdRisk();
                        }
                    }
                }
            }
        };

        measurement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean isMgdlMeasurement = false;

                if (measurement.getSelectedItem().toString().equals("mg/dl")) {
                    isMgdlMeasurement = true;
                }

                if (isMgdlMeasurement && value.getText().contains(".")) {
                    // Mmol to mgdl conversion
                    int currentValue = slider.getValue();
                    float normalMmolValue = slider.getValue() / 100;
                    Integer newValue = Math.round(normalMmolValue / 0.0259f);

                    value.setText(newValue.toString());
                    slider.setValue(newValue);
                } else if (!isMgdlMeasurement && !value.getText().contains(".")) {
                    // Mgdl to Mmol conversion
                    int currentValue = slider.getValue();
                    Float newValue = currentValue * 0.0259f;
                    value.setText(newValue.toString());
                    slider.setValue(Math.round(newValue * 100));
                } else if (!isMgdlMeasurement && value.getText().contains(".")) {
                    // Load mmol slider
                    String currentValue = value.getText();
                    Float newValue = Float.parseFloat(currentValue);
                    int sliderValue = Math.round(newValue * 100);
                    slider.setValue(sliderValue);
                }

            }
        });

        slider.addChangeListener(listener);

        value.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int actualValue = 0;
                try {
                    boolean isMgdlMeasurement = false;

                    if (measurement.getSelectedItem().toString().equals("mg/dl")) {
                        isMgdlMeasurement = true;
                    }

                    if (!firstLoad) {
                        if (isMgdlMeasurement) {
                            Float floatValue = Float.parseFloat(value.getText());
                            actualValue = Math.round(floatValue);
                        } else {
                            Float floatValue = Float.parseFloat(value.getText());
                            actualValue = Math.round(floatValue * 100);
                        }

                        slider.setValue(actualValue);
                    }
                } catch (NumberFormatException x) {
                    // Supressed exception - If entering a non alphanumeric character, do not change value.
                }
            }
        });

    }

}
