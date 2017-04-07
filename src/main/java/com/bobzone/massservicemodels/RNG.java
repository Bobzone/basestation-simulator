package com.bobzone.massservicemodels;

import java.util.Random;

/**
 * Created by epiobob on 2017-04-07.
 */
public class RNG {

    public static int getPoisson(double lambda) {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do {
            k++;
            p *= Math.random();
        } while (p > L);

        return k - 1;
    }

    public static double getGaussian(double aMean, double aVariance) {
        Random fRandom = new Random();
        return aMean + fRandom.nextGaussian() * aVariance;
    }
}
