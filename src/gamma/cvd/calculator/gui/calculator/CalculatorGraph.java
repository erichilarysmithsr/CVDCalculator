/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    private JPanel panel;
    private CVDPatient patient;
    private ChartPanel chartPanel;

    public CalculatorGraph(JPanel panel, CVDPatient patient, JComboBox selectedCategory) {
        this.panel = panel;
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
        panel.removeAll();
        panel.revalidate();

        JFreeChart lineChart = ChartFactory.createLineChart(
                label,
                "Assessment no.", "Recorded Value",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));

        panel.setLayout(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);
        panel.validate();
        panel.repaint();
    }

    private CategoryDataset GetRiskDataSet() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (patient.getRiskData().size() > 20) {
            for (int i = patient.getRiskData().size() - 20; i < patient.getRiskData().size(); i++) {
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

    private CategoryDataset GetCholesterolDataset() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (patient.getRiskData().size() > 20) {
            for (int i = patient.getRiskData().size() - 20; i < patient.getRiskData().size(); i++) {
                CVDRiskData data = patient.getRiskData().get(i);
                dataset.addValue(data.getCholesterolMmolL(), "Cholesterol MMoL", new Integer(i+1));
            }
        } else {
            for (int i = 0; i < patient.getRiskData().size(); i++) {
                CVDRiskData data = patient.getRiskData().get(i);
                dataset.addValue(data.getCholesterolMmolL(), "Cholesterol MMoL", new Integer(i+1));
            }
        }

        return dataset;
    }

    private CategoryDataset GetHdlcDataset() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (patient.getRiskData().size() > 20) {
            for (int i = patient.getRiskData().size() - 20; i < patient.getRiskData().size(); i++) {
                CVDRiskData data = patient.getRiskData().get(i);
                dataset.addValue(data.getHdlMmolL(), "Hdl MMoL", new Integer(i+1));
            }
        } else {
            for (int i = 0; i < patient.getRiskData().size(); i++) {
                CVDRiskData data = patient.getRiskData().get(i);
                dataset.addValue(data.getHdlMmolL(), "Hdl MMoL", new Integer(i+1));
            }
        }

        return dataset;
    }

    private CategoryDataset GetSystolicBloodPressureDataset() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (patient.getRiskData().size() > 20) {
            for (int i = patient.getRiskData().size() - 20; i < patient.getRiskData().size(); i++) {
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

    private CategoryDataset GetDiastolicBloodPressureDataset() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (patient.getRiskData().size() > 20) {
            for (int i = patient.getRiskData().size() - 20; i < patient.getRiskData().size(); i++) {
                CVDRiskData data = patient.getRiskData().get(i+1);
                dataset.addValue(data.getBloodPressureDiastolicMmHg(), "Blood Pressure (Diastolic) (MmHg)", new Integer(i));
            }
        } else {
            for (int i = 0; i < patient.getRiskData().size(); i++) {
                CVDRiskData data = patient.getRiskData().get(i+1);
                dataset.addValue(data.getBloodPressureDiastolicMmHg(), "Blood Pressure (Diastolic) (MmHg)", new Integer(i));
            }
        }

        return dataset;
    }

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
