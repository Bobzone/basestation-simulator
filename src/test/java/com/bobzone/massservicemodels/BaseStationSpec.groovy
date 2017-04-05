package com.bobzone.massservicemodels

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by epiobob on 2017-04-05.
 */
class BaseStationSpec extends Specification {

    @Unroll
    def "when base station is created, all its channels are free"() {
        when:
        def station = new BaseStation()
        then:
        station.channelList.every { Channel c -> (c.isBusy() == false) }
    }

    @Unroll
    def "when base station is being created and number of channels is given, proper number of channels is created and all of them are free"() {
        given:
        final numberOfChannels = 10
        when:
        def station = new BaseStation(numberOfChannels)
        then:
        for (int i = 0; i < numberOfChannels; i++) {
            station.channelList.get(i).isBusy() == false
        }
    }

//    @Unroll
//    TODO - def "base station takes incoming request and assigns it to a free channel"() {
//        given:
//        BaseStation bs = new BaseStation()
//        ServiceRequest sr = new ServiceRequest()
//        when:
//        bs.assignChannel(sr)
//        then:
//        assert bs.channelList.get(0).busy
//    }
}
