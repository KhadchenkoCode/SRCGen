package com.SRCGEN;
import java.util.ArrayList;

public class MuscleGroupWeekly{
    String muscleGroup;
    int session_counts;
    float volume; // тоннаж
    float volumeFB;// тоннаж Ф+Б
    float ARI; // УОИ (average relative intensity)
    int reps; // КПШ
    ArrayList<ComplexLayout>  layouts;
    ArrayList<MuscleSession> sessions;

    ArrayList<Exercise> exercises_by_stimulus;

    /*
    // а может всё-таки так? типа лучше потому что на 1 уровень больше а это лучше декомпозиция и расширяемость
    //а если сгенерировать заранее ВЕСЬ объём на мышце в микроцикле, а потом его рассортировать по  тренировочным сессиям этой мышцы и упорядочить.
*/
    public void generateLayouts(LayoutsPattern pattern){
        // взяли из принятой функции тренировки, внутри них выдеилили структуру
        layouts = pattern.MakePatterns(volumeFB, ARI);
        for (ComplexLayout s: layouts) {
            s.generateSets(); // Распределяет нагрузку внутри тренировки
        }
    }
    public ArrayList<MuscleSession> ExampleSesssions(){ // нафига он public? ладно пофиг
        ArrayList<MuscleSession> patternSesssions = new ArrayList<MuscleSession>(3);

        float medium_ari = this.ARI;
        float heavy_ari = this.ARI + 0.075f;
        float easy_ari = this.ARI-0.075f;

        float volume_shift = 0.075f;

        float heavy_volume_ratio = 0.25f+volume_shift;
        float easy_volume_ratio = 0.25f-volume_shift;

        MuscleSession heavy = new MuscleSession();
        heavy.muscleGroup=this.muscleGroup;
        heavy.volume = this.volume*heavy_volume_ratio;
        heavy.ARI = heavy_ari;
        heavy.type = "heavy";
        heavy.exercises_by_stimulus = this.exercises_by_stimulus;
        //раздал по формуле
        //могу прямо тут раздать упражнения вовнутрь, а могу чето придумать
        //
        MuscleSession medium = new MuscleSession();
        medium.muscleGroup=this.muscleGroup;
        medium.volume = this.volume*0.5f;
        medium.ARI = this.ARI;
        medium.type = "medium";
        medium.exercises_by_stimulus = this.exercises_by_stimulus;

        MuscleSession easy = new MuscleSession();
        easy.muscleGroup=this.muscleGroup;
        easy.volume = this.volume*easy_volume_ratio;
        easy.ARI = easy_ari;
        easy.type = "easy";
        easy.exercises_by_stimulus = this.exercises_by_stimulus;

        patternSesssions.add(heavy);
        patternSesssions.add(medium);
        patternSesssions.add(easy);
        return patternSesssions;
    }

    public void configureSessions(){
        for (MuscleSession s: sessions) {
            //s.make_exercise(); // Распределяет нагрузку внутри тренировки
            System.out.println(this.muscleGroup);
            s.callExercises();
            //s.exerciseGroup(0.3f, 0.7f, compoundness.TARGET); уже метод так не работает
        }
    }

    public void prepare(){
        this.sessions = ExampleSesssions();
        configureSessions();
    }
    public String toString(){
        String ret = this.muscleGroup+" ";
        ret+="Тоннаж \t"+ this.volume +"\t УОИ " +this.ARI;
        return ret;

    }
    public MuscleGroupWeekly(){
        layouts = new ArrayList<>();
        sessions = new ArrayList<>();
        exercises_by_stimulus = new ArrayList<>();


        for(int i = 0; i<exercises_by_stimulus.size(); i++){
            Exercise e = exercises_by_stimulus.get(i);
            System.out.println(e.toString());
        };
    }
    public void copyExercises(){
        for (Exercise e: Main.ExercisesSet) {
            if(e.targetGroup==this.muscleGroup){exercises_by_stimulus.add(e);}
        }
        exercises_by_stimulus.sort(Exercise.stimulus);
        for (MuscleSession ms: sessions) {
            ms.muscleGroup=this.muscleGroup;
            ms.exercises_by_stimulus=this.exercises_by_stimulus;

        }
    }
}
