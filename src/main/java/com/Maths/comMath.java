package com.Maths;


public class comMath{

    public static final double dx = 0.0000001; // это по сути то на сколько большая нам нужна точность. (Типа всё что размером меньше чем dx это как бы уже и нету)
/*
    public static double rootNewtonian(ContinuousFunc func){
        double x;
        x = Newtonian(func);
        return x;
    }
*/
    public static double Newtonian(ContinuousFunc f){
        double x = 5; // захардкодил 5 потому что это близко к интересущему меня диапазону значений.
        double dfx = (f.f(x)-f.f(x+dx))/dx;
        double ret = x-f.f(x)/dfx;
        for(int i = 0; i<1000; i++){
            dfx = (f.f(x + dx) - f.f(x)) / (dx);
            ret = x - f.f(x) / dfx;
            if (Math.abs(x - ret) < dx) {
                return ret; }
            else {
                x = ret;
            }
        }
        return  ret;
    }
    //ну это я считаю тоже в духе чистого кода и микрометодов, 15 строчек это ещё нормально

    public static double inverse(ContinuousFunc f, double x){
        ContinuousFunc find = (a)->{return f.f(a)-x;};
        return Newtonian(find);
    }
    //0 строчек метод
}
