package com.bobzone.massservicemodels

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by epiobob on 2017-04-05.
 */
class BaseStationSpec extends Specification {

    @Unroll
    def "Basic constructor for BaseStation created a station with 8 channels inside"() {
        when:
        def station = new BaseStation()
        then:
        station.channelList.size() == 8
    }


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

    @Unroll
    def "If all channels are busy, incoming ServiceRequests are put on a BaseStation processing queue"() {
        given:
        def station = new BaseStation()
        def requestAlreadyAssigned = new ServiceRequest()
        def newRequest = new ServiceRequest()
        station.channelList.forEach { it -> it.setRequest(new ServiceRequest()) };
        when:
        station.acceptRequest(newRequest);
        then:
        station.queue.get(0) == newRequest
    }

    @Unroll
    def "When two ServiceRequests come to BaseStation and all channels on BS are busy, they are both put on queue"() {
        given:
        def station = new BaseStation()
        def requestAlreadyAssigned = new ServiceRequest()
        station.channelList.forEach { it -> it.setRequest(new ServiceRequest()) };
        def request = new ServiceRequest()
        def request2 = new ServiceRequest()
        when:
        station.acceptRequest(request);
        station.acceptRequest(request2)
        then:
        station.queue.get(0) == request
        station.queue.get(1) == request2
    }

    @Unroll
    def "base station takes incoming request and assigns it to a free channel"() {
        given:
        BaseStation bs = new BaseStation()
        ServiceRequest sr = new ServiceRequest()
        when:
        bs.acceptRequest(sr)
        then:
        assert bs.channelList.get(0).busy
    }

    @Unroll
    def "when base station accepts incoming request and assigns a channel to it, request is not present in BS queue anymore"() {
        given:
        BaseStation bs = new BaseStation()
        ServiceRequest sr = new ServiceRequest()
        when:
        bs.acceptRequest(sr)
        then:
        bs.queue.stream().noneMatch { it -> it == sr }
    }

    @Unroll
    def "when base station has one free channel but two requests come, one gets assigned to a free channel, the other one stays in queue"() {
        given:
        BaseStation bs = new BaseStation()
        for (int i = 1; i < bs.channelList.size(); i++) {
            bs.channelList[i].setRequest(new ServiceRequest());
        }
        def request = new ServiceRequest()
        def request2 = new ServiceRequest()
        when:
        bs.acceptRequest(request)
        bs.acceptRequest(request2)
        then:
        bs.queue.stream().noneMatch { it -> it == request }
        bs.queue.stream().anyMatch { it -> it == request2 }
    }

    @Unroll
    def "when base station channels are busy without any particular order, newly incoming requests are assigned to spare free channels"() {
        given:
        def station = new BaseStation()
        def request1 = new ServiceRequest()
        def request2 = new ServiceRequest()
        def request3 = new ServiceRequest()
        def request4 = new ServiceRequest()
        station.channelList[1].setRequest(new ServiceRequest())
        station.channelList[2].setRequest(new ServiceRequest())
        station.channelList[5].setRequest(new ServiceRequest())
        station.channelList[7].setRequest(new ServiceRequest())
        when:
        station.acceptRequest(request1)
        station.acceptRequest(request2)
        station.acceptRequest(request3)
        station.acceptRequest(request4)
        then:
        station.channelList[0].getRequest() == request1
        station.channelList[3].getRequest() == request2
        station.channelList[4].getRequest() == request3
        station.channelList[6].getRequest() == request4
    }

    @Unroll
    def "test 1"() {
        given:
        def bs = new BaseStation();
        when:
        bs.assignChannelFromQueue()
        then:
        thrown NoSuchFieldException
    }
}
