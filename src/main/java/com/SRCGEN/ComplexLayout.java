package com.SRCGEN;
import java.util.ArrayList;
public class ComplexLayout{
    Exercise exercise ;
    ArrayList<SimpleLayout> components;

    float targetIntensity;  // целевые значения
    float targetVolume;

    float volumeLeft;   //вспомогательное поле которое можно будет удалить но это не точно



    float realVolume; //значения куда попадут реальные данные (если пытаться подобрать точно то будут значения весов с 5 знаками после запятой а в жизни такого не бывает)
    float realInstenity;

    //Я хочу сделать поле в котором будет лежать переопределяемая функция которая вызывается внутри методов объекта и влияет на какую-то логику
    //похожий ход я хочу сделать как для некоторой логики на уровне ComplexLayout
    //так и на уровне класса MuscleSession
    //но это совершенно разные типы функций, та которая переопределяется для класса ComplexLayout поможет считать нагрузку в пределах одного упражнения в одной тренировке,
    //а та которая в MuscleSession нужна для переопределения логики количества и типов тренировок в неделе.
    // Как лучше сделать: написать 2 отдельных интерфейса в котором лежит по одному абстрактному методу, или засунуть все такие абстрактные методы в интерфейс LayoutsPattern?
    //скриншот этого коментария уже был в телеге

    public void checkValues(){

        realVolume = RealVolume(components);
        realInstenity = RealPartialVolume(components)/realVolume;

    }
    public static float esteemIntensity(ArrayList<SimpleLayout> layouts){
        return RealPartialVolume(layouts)/RealVolume(layouts);
    }

    public static float RealPartialVolume(ArrayList<SimpleLayout>layouts){
        float nominator = 0;
        for (int i = 0; i <layouts.size() ; i++) {
            SimpleLayout layout = layouts.get(i);
            nominator+= layout.volume()*layout.kg/layout.exc.RM1;
        }
        return nominator;
    }
    public static float RealVolume(ArrayList<SimpleLayout>layouts){
        float sum = 0;
        for (int i = 0; i <layouts.size() ; i++) {
            SimpleLayout layout = layouts.get(i);
           sum+= layout.volume();
        }
        return sum;
    }



    public String toString(){
        checkValues();
        String ret = this.exercise.name+" ";

        String gap = "";
        for(int i = this.exercise.name.length(); i<15; i++){
            gap+=" ";
        }
        ret+=gap;
        for (SimpleLayout simpleLayout :components) {

            ret+= simpleLayout.toString() +"\t";

        }
        String gap2 = "";
        for(int i = ret.length(); i<150; i++){
            gap2+=" ";
        }ret+=gap2+"\t";
        ret+="RM="+exercise.RM1+"  r="+this.realVolume +" tg = "+this.targetVolume +" intensity= "+realInstenity;
        return ret;
    }


    public void generateSets(){
        volumeLeft=targetVolume;
        SimpleLayout finalLoad = new SimpleLayout(exercise);
        finalLoad.ARI = targetIntensity;
        float targetFinalVolume = 0.75f*targetVolume;
        float current_intensity = 0.3f;
        int targetReps = 6;
        System.out.println("err");
        for(int i = 0; i<5; i++){
            SimpleLayout nextWarmupLoad = new SimpleLayout(exercise);
            nextWarmupLoad.sets=1;
            nextWarmupLoad.reps=targetReps;
            nextWarmupLoad.kg = exercise.RM1*current_intensity;
            volumeLeft -= nextWarmupLoad.sets*nextWarmupLoad.reps*nextWarmupLoad.kg;
            components.add(nextWarmupLoad);
            current_intensity+=0.15f;
            targetReps-=1;
            if(targetIntensity-current_intensity<0.1){break;}
        }
        // ну значит так веса с каждым подходом повышаются, а кпш может идти в любую сторону
        // ну пусть может просто тупо шаги по 15% интенсивности добавлять, делать подход на 5-4 раз,
        // и так вплоть до целевой интенсивности и на нее вышли и выполнили на ней тоннажж

    }



    private void generateWarmup(int lastWarmupReps) {
        this.volumeLeft = this.targetVolume;
        float target = targetIntensity + 0.15f;
        int targetReps = 6;
        float current_intensity = 0.3f;
        int imax = (int) ((target - 0.3f) / 0.1f);
        for (int i = 0; i < imax; i++) {
            SimpleLayout nextWarmupLoad = new SimpleLayout(exercise);
            nextWarmupLoad.sets = 1;

            nextWarmupLoad.reps = targetReps;
            nextWarmupLoad.kg = exercise.RM1 * current_intensity;
            volumeLeft -=nextWarmupLoad.volume();
            if(volumeLeft<0){
                return;
            }
            components.add(nextWarmupLoad);
            current_intensity += 0.1f;
            //if(targetIntensity-current_intensity<0.1){break;}
            if (imax - i < targetReps - lastWarmupReps){
                targetReps -= 1;
            }
        }
    }

    public void generateFullLoad(){
        volumeLeft = targetVolume;
        int target_reps = Main.repsRPE(targetIntensity, 8);
        generateWarmup(target_reps);
        float correction = 2f;
        //SimpleLayout finalLoad= generateTargetLoad(correction, volumeLeft);

        ArrayList<SimpleLayout> targetLoads =  targetLoads = generateTargetLayoutsV3(correction, volumeLeft);

        float error = Math.abs(targetVolume-RealVolume(components)-RealVolume(targetLoads));;
        float prevError = error;


        while(volumeLeft>0){
            correction-= 0.05f;
            targetLoads = generateTargetLayoutsV3(correction, volumeLeft);
            error = Math.abs(targetVolume-RealVolume(components)-RealVolume(targetLoads));;

            targetLoads = generateTargetLayoutsV3(correction, volumeLeft);
            if(error>prevError||correction<0.5f){
                break;
            }else {
                prevError=error;
            }
        }

        targetLoads = generateTargetLayoutsV3(correction+0.05f, volumeLeft);
        for (SimpleLayout s: targetLoads) {
            components.add(s);
        }
        checkValues();
    }

    //подбирает целевую нагрузку по методу в аргументе и по заданным значениям объема и интенсивности
    //возможно аргументы targetVolume и targetIntensity лишние, и надо туда просто засовывать значения из полей
    // ведь после целевых подходов выполнение ComplexLayout закачнивается
    public ArrayList<SimpleLayout> generateAdjustedTargetLoads(float targetVolume, float targetIntensity){
        ArrayList<SimpleLayout> targetLoads = new ArrayList<>();
        float error = Math.abs(targetVolume-RealVolume(components)-RealVolume(targetLoads));;
        float prevError = error;
        float correction = 2f;
        float volumeLeftMethod = targetVolume;
        //логика цикла подлежит пересмотру.
        while(volumeLeftMethod>0){
            correction-= 0.05f;
            // generateTargetLayoursV3 - вместо него должен быть метод в том или ином виде получаемый из аргументов функции, и вот про это и был вопрос в сообщении телеге (Ради чего песня писалась)
            targetLoads = generateTargetLayoutsV3(correction, volumeLeftMethod);
            error = Math.abs(targetVolume-RealVolume(components)-RealVolume(targetLoads));;

            targetLoads = generateTargetLayoutsV3(correction, volumeLeftMethod);
            if(error>prevError||correction<0.5f){
                break; //если ошибка увеличилась значит дальше она будет только расти значит это самая меньшая значит выходим из цикла
            }else {
                prevError=error;
            }
            targetLoads = generateTargetLayoutsV3(correction+0.05f, volumeLeft);
            for (SimpleLayout s: targetLoads) {
                components.add(s);
            }
        }

        return targetLoads;
    }

    //дефолтный метод уже в принципе и не нужен особо
    public ArrayList<SimpleLayout> generateTargetLayoutsDefault(float correction, float volumeArg){
        ArrayList<SimpleLayout> ret = new ArrayList<>();
        if(volumeArg<=0){
            return  ret;
        }


        float target = volumeArg*correction;
        SimpleLayout finalLoad = new SimpleLayout(exercise, this.targetIntensity+0.15f);
        finalLoad.intakeVolumeU6(target, 8);

        if(finalLoad.sets>6){
            System.out.println("HERE");
        }
        ret.add(finalLoad);
        return ret;

    }


    public ComplexLayout(){
        this.components = new ArrayList<SimpleLayout>();
    }
    public  ComplexLayout(Exercise exc,float targetIntensity, float targetVolume){
        this.exercise = exc;
        this.components = new ArrayList<SimpleLayout>();
        this.targetIntensity = targetIntensity;
        this.targetVolume = targetVolume;
        this.volumeLeft=targetVolume;
    }


    
    public ArrayList<SimpleLayout> generateTargetLayoutsV2(float correction, float volumeArg){

        ArrayList<SimpleLayout> ret = new ArrayList<>();
        if(volumeArg<=0){
            return  ret;
        }
        float target = volumeArg*correction;
        SimpleLayout finalLoad = new SimpleLayout(exercise, this.targetIntensity+0.15f);
        finalLoad.intakeVolumeU6V2(target, 6);
        if(!finalLoad.valid()){

        }
        ret.add(finalLoad);

        target-=finalLoad.volume();
        if(target>0){
            target+=finalLoad.volume();
            finalLoad.sets-=2;
            target-=finalLoad.volume();
            SimpleLayout finalLoadNext = new SimpleLayout(exercise, this.targetIntensity+0.2f);
            finalLoadNext.intakeVolumeU6V2(target, 5);
            if(77.5f<finalLoadNext.kg&&finalLoadNext.kg<82.5f){
                System.out.print("");
            }
            if(finalLoadNext.valid()){
                ret.add(finalLoadNext);
            }
        }
        return ret;

    }

    // то же такой себе метод, потому что захардкодено и логика достаточно примитивная
    //а вот будет метод часть логики которого принимается как его аргумент вот тогда заживём так сказать
    public ArrayList<SimpleLayout> generateTargetLayoutsV3(float correction, float volumeArg){
        float RPExhaust = 0;
        ArrayList<SimpleLayout> ret = new ArrayList<>();
        if(volumeArg<=0){
            return  ret;
        }
        float target = volumeArg*correction;
        SimpleLayout finalLoad = new SimpleLayout(exercise, this.targetIntensity+0.15f);
        finalLoad.intakeVolumeU6V2(target, 7);
        RPExhaust+=exhaust(finalLoad);
        ret.add(finalLoad);

        target-=finalLoad.volume();
        if(target>0){
            target+=finalLoad.volume();
            finalLoad.sets-=2;
            target-=finalLoad.volume();
            SimpleLayout finalLoadNext = new SimpleLayout(exercise, this.targetIntensity+0.2f);
            finalLoadNext.intakeVolumeU6V2(target, 7-RPExhaust);

            if(finalLoadNext.valid()){
                ret.add(finalLoadNext);
            }
        }
        return ret;

    }

    //субъективная оценка насколько устанет человек после выполнения нагрузки, настраивается имперически
    public static float exhaust(SimpleLayout simpleLayout){
        float ret = 0;
        ret += Main.rpeSet(simpleLayout.reps, simpleLayout.ARI);
        ret/=100;
        ret *= simpleLayout.sets;

        return  ret;
    }

}
