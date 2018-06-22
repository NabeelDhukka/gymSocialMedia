package com.example.nabee.gymsocialmedia.excerciseLogging;

import java.util.List;

/**
 * Created by Nabee on 6/18/2018.
 */

public class exerciseStats {

    private String Exercises;
    double reps, weight;

    public exerciseStats() {

    }

    public String getExercises() {
        return Exercises;
    }

    public void setExercises(String exercises) {
        Exercises = exercises;
    }

    public double getReps() {
        return reps;
    }

    public void setReps(double reps) {
        this.reps = reps;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
