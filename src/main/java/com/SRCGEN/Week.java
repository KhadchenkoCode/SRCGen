package com.SRCGEN;

import java.util.ArrayList;

public class Week{
    int number;

    public ArrayList<MuscleGroupWeekly> groupsLoad;

    public Week(int muscleGroups){

        this.groupsLoad = new ArrayList<MuscleGroupWeekly>(muscleGroups);
        for(int i =0; i<muscleGroups; i++){
            MuscleGroupWeekly mgw = new MuscleGroupWeekly();
            groupsLoad.add(mgw);
        }

    }
    public Week(){
    }
    public void prepare(){

        System.out.println(this.number+" <- Week ------------- ");
        for (int i = 0; i<3; i++) {
            MuscleGroupWeekly mgw = this.groupsLoad.get(i);
            if(i==0){
                mgw.muscleGroup = "benchPress";
            }
            if(i==1){
                mgw.muscleGroup="squat";
            }
            if(i==2){
                mgw.muscleGroup="pullUp";
            }

            mgw.copyExercises();
            if(mgw.volume!=0&mgw.muscleGroup!=null){
                mgw.prepare();
                System.out.println(mgw.toString());
            }
            i++;

        }

    }
}
