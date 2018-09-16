package com.example.nabee.gymsocialmedia.excerciseLogging;

/**
 * Created by Nabee on 9/5/2018.
 */

public class getOverviewStats {


    private String name, sets, reps, weight, dist,time;



    public getOverviewStats(){

    }

    public getOverviewStats(String name, String sets, String reps, String weight) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
