package net.albertogarrido.classifieds.util;

import java.util.Random;

/**
 * Created by AlbertoGarrido on 4/21/16.
 */
public class Util {

    public static int getRandomIntWithMax(int max){
        Random randomGenerator = new Random();
        return randomGenerator.nextInt(max);
    }
}
