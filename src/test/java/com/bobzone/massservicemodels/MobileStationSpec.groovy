package com.bobzone.massservicemodels

import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

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
    def "Try to send request, but MobileStation was created without specifying connected Base Station"() {
        given:
        def bs = new BaseStation()
        def ms = new MobileStation(2.0)
        when:
        ms.sendRequestToBaseStation();
        then:
        thrown NullPointerException
    }
    @Unroll
    def "When sending single request to a certain BaseStation, requests appear on the BS"() {
        given:
        def bs = new BaseStation()
        def ms = new MobileStation(bs, 2.0)
        when:
        ms.sendRequestToBaseStation();
        then:
        bs.channelList.get(0).busy
    }

    @Unroll
    def "MS can start to send multiple requests at fixed rate, BS handles them"() {
        given:
        def bs = new BaseStation()
        def ms = new MobileStation(bs, 2.0)
        when:
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(8);
        executorService.scheduleAtFixedRate(ms, 1, 1, TimeUnit.SECONDS);
        Thread.sleep(3000)
        then:
        bs.channelList.get(0).busy
        bs.channelList.get(1).busy
        bs.channelList.get(2).busy
    }
}
