package view;


import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTextArea geoJSONTextArea;
    private JTextArea resultTextArea;
    private JButton convertButton;

    public MainFrame() {
        super("GeoJSON to GPX converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(425, 450);
        setResizable(false);
        createGUI();

    }

    private void createGUI() {
        geoJSONTextArea = new JTextArea(10, 30);
        geoJSONTextArea.setLineWrap(true);
        resultTextArea = new JTextArea(10, 30);
        resultTextArea.setEditable(false);
        convertButton = new JButton("Convert");
        JScrollPane topScrollPane = new JScrollPane(geoJSONTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane bottomScrollPane = new JScrollPane(resultTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(topScrollPane);
        add(convertButton);
        add(bottomScrollPane);
    }

    public JTextArea getGeoJSONTextArea() {
        return geoJSONTextArea;
    }

    public JTextArea getResultTextArea() {
        return resultTextArea;
    }

    public JButton getConvertButton() {
        return convertButton;
    }
}
