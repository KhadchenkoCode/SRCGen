package com.SRCGEN;

public class SimpleLayout{
    int reps;
    int sets;
    float ARI; // relative intensity
    float kg;
    Exercise exc; // а может enum
    public SimpleLayout(){};
    public SimpleLayout(Exercise exercise){
        this.exc = exercise;
    };
    public SimpleLayout(Exercise exercise, float ARI){
        this.exc = exercise;
        this.ARI = ARI;
    };
    public void calculateSets(){

    }
    public String toString(){

        String split = " ";
        String ret = "";
        String ratio = String.format("%.1f", (this.kg/exc.RM1)*100);
        String kgStr = String.format("%.1f", (this.kg));
        kgStr = adjustWeight(kg, 2.5f); //2.5f потому что самые маленький шаг блинов в зале обычно 1.25кг и если с каждой стороны добавить 1.25кг то это и будет шаг 2.5кг
        ret += "[kg "+kgStr+"]"+ratio+"%"+split+"r["+this.reps+"]"+split+ "s["+this.sets+"] ";

        return ret;

    }
    public String adjustWeight(float kg, float round){
        int impostor = (int)(kg/round);
        float amogus = impostor*round;
        return String.valueOf(amogus);
    }




    public void intakeVolume(float volume, float RPE){
        kg = exc.RM1*ARI;
        reps = Main.repsRPE(ARI, 8);
        sets = (int)(volume/(kg*reps));
        if (sets == 0) sets = 1;
    }


    public void intakeVolumeU6(float volume, int RPE){
        kg = exc.RM1*ARI;
        reps = Main.repsRPE(ARI, RPE);
        if(reps>6){reps = 6;}
        sets = (int)(volume/(kg*reps));

        if (sets == 0) sets = 1;
    }
    public void intakeVolumeU6V2(float volume, float RPE){
        kg = exc.RM1*ARI;
        reps = Main.repsRPE(ARI, RPE);
        if(reps>6){reps = 6;}
        sets = (int)(volume/(kg*reps));
        if (sets == 0) sets = 1;
        if(sets>6){
            sets = 6;
        }
    }


    public float volume(){
        return this.kg*this.reps*this.sets;

    }
    public boolean valid(){
        boolean ret = this.volume()>0;
        ret = ret&& this.reps>0;
        ret = ret&&this.sets>0;
        ret = ret&&this.kg>0;
        return ret;
    }

    public float RPE(){

        return 0;

    }
}

