package com.bobzone.massservicemodels

import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


/**
 * Created by epiobob on 2017-04-08.
 */
class MobileStationBaseStationCommunicationSpec extends Specification {
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

    @Unroll
    def "if MS sends too much requests to BS, they will be queued on BS"() {
        given:
        def bs = new BaseStation(4)
        def ms = new MobileStation(bs, 2.0)
        when:
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(8);
        executorService.scheduleAtFixedRate(ms, 1, 1, TimeUnit.SECONDS);
        Thread.sleep(5000)
        then:
        bs.channelList.get(0).busy
        bs.channelList.get(1).busy
        bs.channelList.get(2).busy
        bs.channelList.get(3).busy
        bs.queue.get(0) != null
    }
}