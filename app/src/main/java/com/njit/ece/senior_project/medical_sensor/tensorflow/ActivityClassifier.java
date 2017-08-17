package com.njit.ece.senior_project.medical_sensor.tensorflow;

import android.content.Context;

import com.njit.ece.senior_project.medical_sensor.data.util.ArrayHelper;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

/**
 * Determines the activity being performed given the set of inertial signals
 */
public class ActivityClassifier {

    static {
        System.loadLibrary("tensorflow_inference");
    }


    private TensorFlowInferenceInterface inferenceInterface;
    private static final String INPUT_NODE = "X";
    private static final String[] OUTPUT_NODES = {"y_"};
    private static final String OUTPUT_NODE = "y_";
    private static final long[] INPUT_SIZE = {1, 128, 9};
    private static final int OUTPUT_SIZE = 6;

    public enum Activity {
        WALKING(1),
        WALKING_UPSTAIRS(2),
        WALKING_DOWNSTAIRS(3),
        SITTING(4),
        STANDING(5),
        LAYING(6);

        private int num;

        Activity(int num) {
            this.num = num;
        }

        // numerical code for the Activity
        public int getNum() {
            return num;
        }

    }

    // why can't Java just let you convert a number to a enum???
    private Activity activityFromInt(int n) {
        switch(n) {
            case 1:
                return Activity.WALKING;
            case 2:
                return Activity.WALKING_UPSTAIRS;
            case 3:
                return Activity.WALKING_DOWNSTAIRS;
            case 4:
                return Activity.SITTING;
            case 5:
                return Activity.STANDING;
            case 6:
                return Activity.LAYING;
            default:
                throw new IllegalArgumentException();
        }
    }


    public ActivityClassifier(Context context, String savedModelLocation) {
        // create the session from the Bundle
        // load the model Bundle and run the data
        init(context, savedModelLocation);
    }

    private void init(Context context, String savedModelLocation) {
        inferenceInterface = new TensorFlowInferenceInterface(context.getAssets(), savedModelLocation);
    }


    public Activity classify(float[][][] signals) {

        float[] inputFlat = ArrayHelper.flatten(signals);

        float[] probDistribution = new float[OUTPUT_SIZE];
        inferenceInterface.feed(INPUT_NODE, inputFlat, INPUT_SIZE);
        inferenceInterface.run(OUTPUT_NODES);
        inferenceInterface.fetch(OUTPUT_NODE, probDistribution);


        // find the most likely classification from the probability distribution
        int maxIndex = 0;
        float maxVal = 0;
        for(int i = 0; i < probDistribution.length; i++) {
            if(probDistribution[i] > maxVal) {
                maxIndex = i;
                maxVal = probDistribution[maxIndex];
            }
        }

        return activityFromInt(maxIndex + 1 /*arrays start at 0...*/);

    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        inferenceInterface.close();
    }
}

