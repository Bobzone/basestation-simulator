package com.bobzone.massservicemodels

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by epiobob on 2017-04-07.
 */
class MobileStationSpec extends Specification {

    @Unroll
    def "when creating MobileStation its requestCreationInterval is generated"() {
        when:
        def station = new MobileStation(RNG.getPoisson(2.0))
        then:
        println "generated value was " + station.requestCreationInterval
        station.requestCreationInterval != null
    }

    @Unroll
    def "when mobile station generates service request it has callTime auto generated"() {
        given:
        def station = new MobileStation(RNG.getPoisson(3.0))
        when:
        def generatedRequest = station.generateServiceRequest(RNG.getGaussian(100.0, 5.0))
        then:
        generatedRequest.callLength != null
    }

    @Unroll
    def "when mobile station starts making calls it generates requests at fixed rate"() {
        given:
        def station = new MobileStation(1)
        when:
        station.startMakingCalls()
        Thread.sleep(2000)
        station.stopMakingCalls()
        then:
        station.request != null
    }
}
