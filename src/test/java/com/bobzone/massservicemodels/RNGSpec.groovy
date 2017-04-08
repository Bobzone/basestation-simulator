package com.bobzone.massservicemodels

import spock.lang.Specification
import spock.lang.Unroll

import static com.bobzone.massservicemodels.RNG.getPoisson

/**
 * Created by epiobob on 2017-04-07.
 */
class RNGSpec extends Specification {

    @Unroll
    def "generating poisson dist with given lambda works"() {
        when:
        double LAMBDA = 15.0
        then:
        for (int idx = 1; idx <= 100000; ++idx) {
            def poisson = getPoisson(LAMBDA)
            println poisson
            assert poisson != null
        }
    }

    @Unroll
    def "generating normal distribution with mean 100 and variance 5 works"() {
        when:
        double MEAN = 100.0f;
        double VARIANCE = 5.0f;
        then:
        for (int idx = 1; idx <= 100000; ++idx) {
            def gaussian = RNG.getGaussian(MEAN, VARIANCE)
//            println("Generated : " + gaussian);
            assert gaussian > 0
        }
    }

}
