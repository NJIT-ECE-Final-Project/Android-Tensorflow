package com.njit.ece.senior_project.medical_sensor.data.SampleLoader;

import android.content.Context;

import com.njit.ece.senior_project.medical_sensor.data.SignalType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple class to load the data from the sample .txt files
 */
public class SampleDataLoader {

    private Context context;

    private String signalsFolder;

    private int timeSteps = 128;

    // the different types of signal to include in the full data set
    private final SignalType[] signalTypes = {
            SignalType.body_acc_x, SignalType.body_acc_y, SignalType.body_acc_z,
            SignalType.body_gyro_x, SignalType.body_gyro_y, SignalType.body_gyro_z,
            SignalType.total_acc_x, SignalType.total_acc_y, SignalType.total_acc_z};


    // one buffered reader per signal file
    BufferedReader[] signalReaders;



    public SampleDataLoader(Context context, String signalsFolder) throws IOException {

        this.context = context;

        this.signalsFolder = signalsFolder;

        this.signalReaders = new BufferedReader[signalTypes.length];

        for(int i = 0; i < signalTypes.length; i++) {

            InputStream signalFile = context.getAssets().open(signalsFolder + "/" + signalTypes[i].toString() + "_test.txt");
            signalReaders[i] = new BufferedReader(new InputStreamReader(signalFile));
        }
    }


    public float[][][] getNextRow() throws IOException {

        float[][][] dataSet = new float[1][timeSteps][signalTypes.length];

        int s = 0;
        for(BufferedReader signalReader : signalReaders) {
            String oneLine = signalReader.readLine();
            float[] lineData = parse(oneLine);
            for(int i = 0; i < lineData.length; i++) {
                dataSet[0][i][s] = lineData[i];
            }
            s++;
        }
        return dataSet;

    }

    public boolean hasNext() {
        // make sure all readers have another line
        for(BufferedReader r : signalReaders) {
            try {
                r.ready();
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    /*
    @Deprecated
    public float[][][] loadOneRowAllSignals() throws IOException {

        float[][][] dataSet = new float[1][timeSteps][signalTypes.length];

        for(int s = 0; s < signalTypes.length; s++) {
            // get the 128 values for the given signal type
            float[] oneSignal = loadOneRow(signalTypes[s]);
            // add all 128 value to the float array
            for(int i = 0; i < oneSignal.length; i++) {
                dataSet[0][i][s] = oneSignal[i];
            }
        }

        return dataSet;

    }


    @Deprecated
    private float[] loadOneRow(SignalType signalType) throws IOException {

        File signalFile = signalsFolder.toPath().resolve(signalType.toString() + "_test.txt").toFile();

        BufferedReader fileReader = new BufferedReader(new FileReader(signalFile));

        String oneLine = fileReader.readLine();

        // someone thought it was a good idea to delimit data with a variable number of spaces...
        // ... so this regex splits any number of spaces
        String[] dataPoints = oneLine.split("\\s+");

        // The next 15 lines of code would be a single line with streams..... I miss Java 8
        // filter out empty strings
        List<String> dataPointsFiltered = new ArrayList<>();
        for(String s : dataPoints) {
            if(!s.isEmpty()) {
                dataPointsFiltered.add(s);
            }
        }
        int l = 0;

        float[] dataPointsAsFloat = new float[dataPointsFiltered.size()];
        for(int i = 0; i < dataPointsFiltered.size(); i++) {
            dataPointsAsFloat[i] = Float.parseFloat(dataPointsFiltered.get(i));
        }

        return dataPointsAsFloat;
    }
    */
    private float[] parse(String oneLine) {
        // someone thought it was a good idea to delimit data with a variable number of spaces...
        // ... so this regex splits any number of spaces
        String[] dataPoints = oneLine.split("\\s+");

        // The next 15 lines of code would be a single line with streams..... I miss Java 8
        // filter out empty strings
        List<String> dataPointsFiltered = new ArrayList<>();
        for(String s : dataPoints) {
            if(!s.isEmpty()) {
                dataPointsFiltered.add(s);
            }
        }
        int l = 0;

        float[] dataPointsAsFloat = new float[dataPointsFiltered.size()];
        for(int i = 0; i < dataPointsFiltered.size(); i++) {
            dataPointsAsFloat[i] = Float.parseFloat(dataPointsFiltered.get(i));
        }

        return dataPointsAsFloat;
    }

    public int getTimeSteps() {
        return timeSteps;
    }

    public void setTimeSteps(int timeSteps) {
        this.timeSteps = timeSteps;
    }
}
