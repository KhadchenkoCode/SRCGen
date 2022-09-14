package com.SRCGEN;

import java.util.ArrayList;

public class TrainingCourse{
ArrayList<Week> weeks;

    public String toString(){
        return "";
    }
    public TrainingCourse(){

    };

    public TrainingCourse(int weeks_number){
    this.weeks = new ArrayList<Week>(weeks_number);
        for (int i = 0; i <this.weeks.size() ; i++) {
        Week tmp = new Week();
        tmp.number = i+1;
            this.weeks.add(i,tmp);
        }
    };
    public TrainingCourse(int weeks_number, int muscleGroups){
        this.weeks = new ArrayList<Week>(weeks_number);
        this.weeks.ensureCapacity(weeks_number);
        for (int i = 0; i <weeks_number; i++) {
            Week tmp = new Week(muscleGroups);
            tmp.number = i+1;
            this.weeks.add(i,tmp);
        }
    };


    public void getValues(String path, ValueInstruction vl){
        ArrayList<String> strings = Main.lines(path);
        ArrayList<Float> values = new ArrayList<Float>(strings.size());
        for (int i = 0; i<strings.size(); i++) {
            float tmp = Float.parseFloat(strings.get(i));
            values.add(i, tmp);
        }
        for (int i = 0; i < weeks.size(); i++) {
            Week WeekPointer = weeks.get(i);
            vl.insertValue(WeekPointer, values.get(i));
        }
    }


    public void prepare(){

        for (Week w: this.weeks) {
               w.prepare();
        }

    }
}
