package com.SRCGEN;

import java.util.Comparator;

public class Exercise {

    float stimulus_ratio;
    compoundness compd; //enum
    String name;
    String targetGroup;
    float RM1;

    public Exercise (float stimulus_ratio, compoundness compd, String name, String targetGroup, float RM1) {
        this.stimulus_ratio = stimulus_ratio;
        this.compd = compd;
        this.name = name;
        this.targetGroup = targetGroup;
        this.RM1 = RM1;
    }


    public static final Comparator<Exercise> stimulus = (e1, e2) -> {
        if (e1.stimulus_ratio < e2.stimulus_ratio){
            return 1;
        } else if (e1.stimulus_ratio == e2.stimulus_ratio){
            return 0;
        } else {
            return -1;
        }
    };

    public String toString(){
        return  name+" \t\t"+targetGroup+" \t"+compd +" \t>"+stimulus_ratio+"< \t"+RM1;
    }

}
