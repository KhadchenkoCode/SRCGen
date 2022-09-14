package  com.SRCGEN;

import com.Maths.ContinuousFunc;
import com.Maths.comMath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Stream;

public class Main{
    public static HashSet<Exercise> ExercisesSet;
    public static final String target = "target";
    public static int maxReps;
    public static void main (String[] args) {


      UnitTests();

        System.out.println("hello world");
        maxReps=6;

        initialiseExercises();

        TrainingCourse SRC0 = new TrainingCourse(12, 3);
        //0 - bench
        //1 - squat
        //2 - pullup

        ValueInstruction volumeBench = (Week w, float f)-> w.groupsLoad.get(0).volume = f;

        ValueInstruction ARIbench = (Week w, float f)-> w.groupsLoad.get(0).ARI = f;
        String benchVolumeSource = "G:\\Programs\\CODE\\JAVA\\JavaCodes\\SRCGen\\numberSource\\volume14bench.txt";
        SRC0.getValues(benchVolumeSource, volumeBench);
        String benchARISource = "G:\\Programs\\CODE\\JAVA\\JavaCodes\\SRCGen\\numberSource\\ARI14bench.txt";
        SRC0.getValues(benchARISource, ARIbench);



        SRC0.prepare();


        // Надо выдумать графики на мезоцикл, относительно ПМ.
        //
        // в недельной нагрузке на мышцу выделяется структура тренировок (пример 1 лёгкая 1 тяжелая, или 1 лёгкая 1 средняя, всё в зависимости от цифр мезоцикла конкретно в эту неделю)
        // на каждую тренировку в микроцикле выделяются проценты и тоннаж к которым должна сойтись нагрузка в ней.
        // в классе TrainingSession(или где?) берутся упражнения и раскладки подгоняются под нужные цифры, так же тут проверяется чтобы раскладки не были похожи на то что было прошлый раз.
        // потом эти TrainingSession попадают в MuscleGroupWeekly, и дальше до уровня Week.
        // в Week выбираются дни недели куда это запихнуть.
        // результат выводится пользователю, осталось ввести свои ПМ, оно посчитает проценты можно идти тренить.
    }


    public static float RPEreverse(float ratio, int reps){

        try{
            if(0<ratio||ratio<1){
                System.out.println(ratio+"<ratio reps>"+reps);

                throw  new  IllegalArgumentException();
            }
        } catch ( IllegalArgumentException e){

            e.printStackTrace();
            System.out.println("args: ratio was "+ratio+" rpe was "+reps);
            return (0);
        }
        return 0;


    }
    static ArrayList<String> lines(String path){
        ArrayList<String> ret = new ArrayList<String>();
        Path path1 = Paths.get(path);

        try (Stream<String> lines = Files.lines(path1)) {
            lines.forEach(s->ret.add(s));

        } catch (IOException ex) {
            System.out.println(ex);
        }
        return  ret;
    }

    static void initialiseExercises(){
        Main.ExercisesSet = new HashSet<Exercise>();
        {
            //
            String g1 = "benchPress";
            Exercise benchPress = new Exercise(1,compoundness.TARGET, g1, g1, 95);
            Exercise  dumbbellPress = new Exercise(0.8f, compoundness.SPEC, "dumbbellPress", g1, 60);
            Exercise standPress = new Exercise(0.81f, compoundness.AUX, "standPress", g1, 55);
            Exercise inclinedBench = new Exercise(0.9f, compoundness.SPEC, "inclinedBench",g1, 75);
            Exercise frenchPress = new Exercise(0.5f, compoundness.AUX, "frenchPress",g1, 50);
            Exercise blockTricep = new Exercise(0.5f, compoundness.AUX, "blockTricep", g1, 45);
            Exercise dumbbellDeltsFront = new Exercise(0.3f, compoundness.AUX, "dumbbellDeltsFront",g1, 20);
            // значения взяты с интернета
            ExercisesSet.add(benchPress);
            ExercisesSet.add(dumbbellPress);
            ExercisesSet.add(standPress);
            ExercisesSet.add(inclinedBench);
            ExercisesSet.add(frenchPress);
            ExercisesSet.add(blockTricep);
            ExercisesSet.add(dumbbellDeltsFront);
            //пока-что только для жима лёжа чтобы не подгонять всё сразу
            //
        }
        {
            Exercise squat = new Exercise(1.2f, compoundness.TARGET, "squat", "squat", 125);
            ExercisesSet.add(squat);
            // и тд такая же логика c упражнениями на присед
        }
    }

    //to be implemented
    public static float rpeSet(int reps, float ratio){
        float maxReps = (float) maxReps(ratio);
        return 10 - (maxReps- reps);
        /*пример: сделали 5 раз а могли 10 ну ладно пусть будет RPE 5
        на малоповторке (от 1 до 6) это канает вообще хорошо
       а сейчас цель малоптоворка
        а многоптовторку можно и втолкать в ворота силой через коэфициенты поправки, типа чем более многоповторно тем медленнее отдаляемся от RPE 10
         но пока пофиг, все равно RPE достаточно субъективная вещь че хотим то и определяем
                */
    }
    public static int repsRPE(float ratio, float rpe){
        try {

            if(ratio<0||1<ratio|| rpe<0||10<rpe){
            throw new IllegalArgumentException();
            }
        }catch (IllegalArgumentException e){
            System.out.println("ERROR repsRPE: ratio = "+ratio+ " rpe = "+rpe);
        }
        double MRFW = maxReps(ratio); // 76% можно поднять на 10 раз
        float rir = 10-rpe; // rpe 8, rir = 2
        int reps = (int)(MRFW-rir); // будет поднятие 76% на 8 раз
        if(reps<1){
            reps = 0;
        }
        return reps;
    }
    public static float ratioRPE(float rpe, int reps){
        float variable = reps-(10-rpe);
        double MWFR = maxWeight(variable);
        float cast = (float)MWFR;
        return cast;
    }
    //to be implemented


    public static double maxReps(double weight){ // weight это процент (0 до 1) а не кг
        ContinuousFunc MWFR = (x)-> maxWeight(x);
        return comMath.inverse(MWFR, weight);
    }

    public static double maxWeight(double reps){
        double x = reps;
        double result =  -0.0003*x*x*x + 0.0052*x*x - 0.0553*x + 1.0484;
        //формула получаемая эмпирическим путём просто по набору значений которые наблюдаются в реальности у конкретного атлета
        return  result;
    }

    public static void UnitTests(){
        System.out.println("ratio rpe  8, 8 = ");
        float weightTest = ratioRPE(8, 8);
        System.out.println(weightTest);

        if(false) for(int i = 1; i<15; i++){ // RPE base methods test
            double weight = maxWeight(i);
            double reverseReps = maxReps(weight);
            System.out.println("MaxWeight("+i+") = "+weight +" \n  MaxReps ("+weight+") = " + reverseReps);
        }

        if(true){
            System.out.print("           ");

            for(int i = 1; i<=10; i++){
                System.out.print(" "+i+"    ");
            }
            System.out.println();
            for(float i = 4; i<10; i++){
                System.out.print("weight "+i/10);
                for(int j = 1; j<= 10; j++){
                    float reps = repsRPE((i/10), j);
                    System.out.print(" "+reps+" ");
                }
                System.out.println();
            }
        } // SET estimation methods test



    }
   // Вот это Дядя Боб будет рад таким методам
}
