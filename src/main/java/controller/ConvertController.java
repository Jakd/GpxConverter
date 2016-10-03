package controller;

import com.vividsolutions.jts.geom.Coordinate;
import util.GeoJsonParser;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Jakob on 03-Oct-16.
 */
public class ConvertController {

    private JButton convertButton;
    private JTextArea inputTextArea;
    private JTextArea resultTextArea;
    private Coordinate[] coordinates;

    public ConvertController(JButton convertButton, JTextArea inputTextArea, JTextArea resultTextArea) {
        this.convertButton = convertButton;
        this.inputTextArea = inputTextArea;
        this.resultTextArea = resultTextArea;

        convertButton.addActionListener(e -> {
            if (!inputTextArea.getText().isEmpty()) {
                parseGeoJson();
            }
        });
    }

    private void parseGeoJson() {
        try {
            GeoJsonParser geoJsonParser = new GeoJsonParser(inputTextArea.getText());
            coordinates = geoJsonParser.getCoordinates();
            showResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showResult() {
        resultTextArea.setText("");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("latitude,longitude,altitude,time\n");
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        gc.set(2016, 4, 12, 18, 24);
        long timeInMs = gc.getTimeInMillis();
        String date = "2016-08-02T";
        for (int i = 0; i < coordinates.length; i++) {
            stringBuilder.append(coordinates[i].y);
            stringBuilder.append(",");
            stringBuilder.append(coordinates[i].x);
            stringBuilder.append(",60,");
            stringBuilder.append(date);
            stringBuilder.append(sdf.format(new Date(timeInMs)));
            stringBuilder.append("Z");
            stringBuilder.append("\n");
            timeInMs += 5 * 1000;
        }

        resultTextArea.setText(stringBuilder.toString());

    }


}
