package com.bobzone.massservicemodels

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by epiobob on 2017-04-05.
 */
class ChannelSpec extends Specification {

    @Unroll
    def "you can set incoming request to a concrete channel"() {
        given:
        def channel = new Channel()
        def request = new ServiceRequest()
        when:
        channel.setRequest(request)
        then:
        channel.getRequest() != null
    }

    @Unroll
    def "when request was set to channel, channel is busy"() {
        given:
        def channel = new Channel()
        def request = new ServiceRequest()
        when:
        channel.setRequest(request)
        then:
        channel.isBusy()
    }

    @Unroll
    def "when request is processed and finished channel is free again"() {
        given:
        def channel = new Channel()
        def request = new ServiceRequest()
        when:
        channel.setRequest(request)
        request.finish();
        then:
        channel.isBusy()
    }

//    @Unroll
//    def "you can assign only one request to one channel at the same time"() {
//        given:
//        def channel = new Channel()
//        def request = new ServiceRequest()
//        def request2 = new ServiceRequest()
//        when:
//        channel.setRequest(request)
//        channel.setRequest(request2)
//        then:
//        thrown IllegalStateException
//        channel.getRequest() == request
//    }
}
