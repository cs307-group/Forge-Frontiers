package com.forgefrontier.forgefrontier.fishing;

import java.util.ArrayList;

public class FishModifier {
    private ArrayList<Double> rodLevel;
    private ArrayList<Double> statLevel;

    public FishModifier(ArrayList<Double> rodLevel, ArrayList<Double> statLevel) {
        this.rodLevel = rodLevel;
        this.statLevel = statLevel;
    }

    public ArrayList<Double> getRodLevel() {
        return rodLevel;
    }

    public void setRodLevel(ArrayList<Double> rodLevel) {
        this.rodLevel = rodLevel;
    }

    public ArrayList<Double> getStatLevel() {
        return statLevel;
    }

    public void setStatLevel(ArrayList<Double> statLevel) {
        this.statLevel = statLevel;
    }

    public double getRodModifier(int level) {
        if (level < 0) level = 0;
        if (level >= rodLevel.size()) return rodLevel.get(rodLevel.size()-1);
        else {
            return rodLevel.get(level);
        }
    }
    public double getLevelModifier(int level) {
        if (level < 0) level = 0;
        if (level > statLevel.size()) return statLevel.get(statLevel.size()-1);
        else {
            return statLevel.get(level);
        }
    }

}
