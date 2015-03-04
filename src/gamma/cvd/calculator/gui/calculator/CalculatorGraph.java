package gamma.cvd.calculator.gui.calculator;

import gamma.cvd.calculator.CVDPatient;
import gamma.cvd.calculator.CVDRiskData;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Jack
 */
public class CalculatorGraph {

    private CVDPatient patient;
    private ChartPanel lineChart;
    private final JPanel chartPanel;
    private final static int MAX_DATA_POINTS = 20; 

    public CalculatorGraph(JPanel panel, CVDPatient patient, JComboBox selectedCategory) {
        this.chartPanel = panel;
        this.patient = patient;
        addListeners(selectedCategory);
    }

    public void setCVDPatient(CVDPatient patient) {
        this.patient = patient;
    }

    public void DrawRiskGraph() {
        CategoryDataset dataset = GetRiskDataSet();
        DrawLinegraph(dataset, "Risk");
    }

    public void DrawCholesterolGraph() {
        CategoryDataset dataset = GetCholesterolDataset();
        DrawLinegraph(dataset, "Cholesterol MmoL");
    }

    public void DrawHdlcGraph() {
        CategoryDataset dataset = GetHdlcDataset();
        DrawLinegraph(dataset, "Hdlc MmoL");
    }

    public void DrawDiastolicBloodPressureGraph() {
        CategoryDataset dataset = GetDiastolicBloodPressureDataset();
        DrawLinegraph(dataset, "Blood Pressure (Diastolic) (MmHg)");
    }

    public void DrawSystolicBloodPressureGraph() {
        CategoryDataset dataset = GetSystolicBloodPressureDataset();
        DrawLinegraph(dataset, "Blood Pressure (Systolic) (MmHg)");
    }

    
    private void DrawLinegraph(CategoryDataset dataset, String label) {
        // Removes chart from current panel.
        chartPanel.removeAll();
        chartPanel.revalidate();

        // Redraw new chart with dataset
        JFreeChart lineChart = ChartFactory.createLineChart(
                label,
                "Assessment no.", "Recorded Value",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        this.lineChart = new ChartPanel(lineChart);
        this.lineChart.setPreferredSize(new java.awt.Dimension(560, 367));

        // Repaint panel to update. 
        chartPanel.setLayout(new BorderLayout());
        chartPanel.add(this.lineChart, BorderLayout.CENTER);
        chartPanel.validate();
        chartPanel.repaint();
    }

    // Based on risk data, generates a set of data points for 'Risk' category
    private CategoryDataset GetRiskDataSet() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (patient.getRiskData().size() > MAX_DATA_POINTS) {
            for (int i = patient.getRiskData().size() - MAX_DATA_POINTS; i < patient.getRiskData().size(); i++) {
                CVDRiskData data = patient.getRiskData().get(i);
                int score = data.calculateRiskScore();
                dataset.addValue(data.getRiskPercentage(score), "Risk", new Integer(i+1));
            }
        } else {
            for (int i = 0; i < patient.getRiskData().size(); i++) {
                CVDRiskData data = patient.getRiskData().get(i);
                int score = data.calculateRiskScore();
                dataset.addValue(data.getRiskPercentage(score), "Risk", new Integer(i+1));
            }
        }

        return dataset;

    }

    // Based on risk data, generates a set of data points for 'Cholesterol' category
    private CategoryDataset GetCholesterolDataset() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (patient.getRiskData().size() > MAX_DATA_POINTS) {
            for (int i = patient.getRiskData().size() - MAX_DATA_POINTS; i < patient.getRiskData().size(); i++) {
                CVDRiskData data = patient.getRiskData().get(i);
                dataset.addValue(data.getCholesterolMmolL(), "Cholesterol (MMoL)", new Integer(i+1));
            }
        } else {
            for (int i = 0; i < patient.getRiskData().size(); i++) {
                CVDRiskData data = patient.getRiskData().get(i);
                dataset.addValue(data.getCholesterolMmolL(), "Cholesterol (MMoL)", new Integer(i+1));
            }
        }

        return dataset;
    }

    
    // Based on risk data, generates a set of data points for 'Hdlc' category
    private CategoryDataset GetHdlcDataset() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (patient.getRiskData().size() > MAX_DATA_POINTS) {
            for (int i = patient.getRiskData().size() - MAX_DATA_POINTS; i < patient.getRiskData().size(); i++) {
                CVDRiskData data = patient.getRiskData().get(i);
                dataset.addValue(data.getHdlMmolL(), "Hdl cholesterol (MMoL)", new Integer(i+1));
            }
        } else {
            for (int i = 0; i < patient.getRiskData().size(); i++) {
                CVDRiskData data = patient.getRiskData().get(i);
                dataset.addValue(data.getHdlMmolL(), "Hdl cholesterol (MMoL)", new Integer(i+1));
            }
        }

        return dataset;
    }

    // Based on risk data, generates a set of data points for 'Systolic Blood pressure' category
    private CategoryDataset GetSystolicBloodPressureDataset() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (patient.getRiskData().size() > MAX_DATA_POINTS) {
            for (int i = patient.getRiskData().size() - MAX_DATA_POINTS; i < patient.getRiskData().size(); i++) {
                CVDRiskData data = patient.getRiskData().get(i);
                dataset.addValue(data.getBloodPressureSystolicMmHg(), "Blood Pressure (Systolic) (MmHg)", new Integer(i+1));
            }
        } else {
            for (int i = 0; i < patient.getRiskData().size(); i++) {
                CVDRiskData data = patient.getRiskData().get(i);
                dataset.addValue(data.getBloodPressureSystolicMmHg(), "Blood Pressure (Systolic) (MmHg)", new Integer(i+1));
            }
        }

        return dataset;
    }

    // Based on risk data, generates a set of data points for 'Diastolic Blood pressure' category    
    private CategoryDataset GetDiastolicBloodPressureDataset() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (patient.getRiskData().size() > MAX_DATA_POINTS) {
            for (int i = patient.getRiskData().size() - MAX_DATA_POINTS; i < patient.getRiskData().size(); i++) {
                CVDRiskData data = patient.getRiskData().get(i);
                dataset.addValue(data.getBloodPressureDiastolicMmHg(), "Blood Pressure (Diastolic) (MmHg)", new Integer(i+1));
            }
        } else {
            for (int i = 0; i < patient.getRiskData().size(); i++) {
                CVDRiskData data = patient.getRiskData().get(i);
                dataset.addValue(data.getBloodPressureDiastolicMmHg(), "Blood Pressure (Diastolic) (MmHg)", new Integer(i+1));
            }
        }

        return dataset;
    }
    
    // Add listener to category combobox so that if it changes, it draws the new category on the linechart.
    private void addListeners(JComboBox selectedCategory) {
        selectedCategory.addActionListener((ActionEvent e) -> {
            String category = selectedCategory.getSelectedItem().toString();

            switch (category) {
                case "Risk":
                    DrawRiskGraph();
                    break;
                case "Cholesterol/LDL-C":
                    DrawCholesterolGraph();
                    break;
                case "HDL-C":
                    DrawHdlcGraph();
                    break;
                case "Blood Pressure (Systolic)":
                    DrawSystolicBloodPressureGraph();
                    break;
                case "Blood Pressure (Diastolic)":
                    DrawDiastolicBloodPressureGraph();
                    break;
                default:
                    DrawRiskGraph();
                    break;
            }
        });

    }
}
