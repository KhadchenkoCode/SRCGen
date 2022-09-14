package com.SRCGEN;

import java.util.ArrayList;
import java.util.HashMap;

public class MuscleSession{
    String muscleGroup;
    float volume; // тоннаж
    float volumeLeft;
    float volumeFB;// тоннаж Ф+Б
    float ARI; // УОИ (average relative intensity)
    int   reps; // КПШ
    String type; // heavy medium easy

    HashMap<compoundness, Float> exercise_type_distribution; //отследить где сколько???
    ArrayList<ComplexLayout> exercises;
    //несколько complex layout

    ArrayList<Exercise> exercises_by_stimulus;

    //скорее всего нафиг
    private ComplexLayout make_exercise(Exercise exc){
        ComplexLayout ret = new ComplexLayout();
        SimpleLayout layoutMainCompound = new SimpleLayout();
        return  ret;
    }

    public void callExercises(){
        this.exercises=exerciseGroup(1, this.ARI, compoundness.TARGET);
        System.out.println('\n'+this.type);
        int i = 0;
        for (ComplexLayout layout: this.exercises) {

            layout.generateFullLoad();
            System.out.println(layout.toString());

            i++;
            if(i%15==0||i==0){
                System.out.println("v="+layout.realVolume+" I="+layout.realInstenity);
            }

        }
        System.out.println(this.type+'\n');
    }


    //ArrayList<ComplexLayout> ret = new ArrayList<ComplexLayout>();
    // пример, 0.3 на тяжелое и тд
    public   ArrayList<ComplexLayout> exerciseGroup(float volumeRatio, float ARI, compoundness type){   // пример, 0.5 на основу и чуть повыше интенсивность в среднем, 0.35 на офп, 0.15  на сфп или чет такое
        ArrayList<ComplexLayout> ret = new ArrayList<ComplexLayout>();
        float targetRatio = 0;
        float specRatio = 0;
        float auxRatio = 0;
 // лёгкие - техническое мастерство, и "базовая такая нагрузка", веса от 45 до 55%
        // средняя от 55% до 65%, техника тоже.
        // тяжелая ОИ > 65%, до 85%
        {
            if(this.type=="heavy"){
                this.ARI=ARI+0.1f;
                targetRatio = 0.65f;
                specRatio = 0.2f;
                auxRatio = 0.15f;

            }
            if(this.type == "medium"){

                    this.ARI=ARI-0.1f;
                targetRatio = 0.5f;
                specRatio = 0.25f;
                auxRatio = 0.25f;
            }
            if(this.type == "easy"){

                this.ARI=ARI-0.2f;
                targetRatio = 0.45f;
                specRatio = 0.25f;
                auxRatio = 0.3f;
            }
            // вот эта ветка, а может и вся функция целиком должна наследоваться откуда-то и быть переопределяемой и расширяемой, но пока это первая сырая версия поэтому лежит внутри класса
            // возможно надо сделать чтобы interface объект с нужной функцией передавался снаружи.
        }

        //Heavy = 0.65Target+ 0.2spec+0.15aux
        //Medium = 0.5Target+0.25spec+0.25aux
        //easy = 0.45Target+0.25spec+0.3aux
        //надо циклить больше меньше в while до тех пор пока цифры не сойдутся и тогда ливнуть с цикла

        //alternative heavy example: 0.25target+0.2spec+0.2target+0.15aux+0.2target
        //тут  объем в целевом сделан не сразу залпом весь а разделен по частям

        Exercise target = findExercise(compoundness.TARGET, 0);
        Exercise spec1 = findExercise(compoundness.SPEC, 0);
        Exercise aux1 = findExercise(compoundness.AUX, 0); // Метод findExercise можно пересмотреть мне эта реализация временная

        ComplexLayout targetLayout1 = new ComplexLayout(target, this.ARI, volume*targetRatio);
     //   ComplexLayout targetLayout2 = new ComplexLayout(target, this.ARI, volume*targetRatio*0.4f);

        ComplexLayout spec1Layout = new ComplexLayout(spec1, this.ARI, volume*specRatio);

        ComplexLayout aux1Layout = new ComplexLayout(aux1, this.ARI, volume*auxRatio);

        ret.add(targetLayout1);
       // ret.add(targetLayout2);
        ret.add(spec1Layout);
        ret.add(aux1Layout);

        return ret;
    }

    private Exercise findExercise(compoundness type, int which){ //suboptimal performance wise but this is not performance critical

        for(int i = 0; i<this.exercises_by_stimulus.size(); i++){
            Exercise e = this.exercises_by_stimulus.get(i);
            if(e.compd==type){
                if(which == 0){
                    return e;
                }else{
                    which--;
                }
            }
        }

        for (Exercise e: this.exercises_by_stimulus) {
        boolean muscle = e.targetGroup.equals(this.muscleGroup);
        boolean type_comp = e.compd.equals(type);
         if(muscle&&type_comp){
             return e;
         }
        }
        return null;
    }

}
