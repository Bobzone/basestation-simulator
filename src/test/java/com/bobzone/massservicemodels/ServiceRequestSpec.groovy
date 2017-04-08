package com.bobzone.massservicemodels

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by epiobob on 2017-04-05.
 */
class ServiceRequestSpec extends Specification {

    @Unroll
    def "when ServiceRequest is created it works"() {
        when:
        def request = new ServiceRequest()
        then:
        request != null
    }

    @Unroll
    def "when ServiceRequest is created it has a random id"() {
        when:
        def request = new ServiceRequest()
        then:
        request.id != null;
    }

    @Unroll
    def "when ServiceRequest is finished, id is set to empty"() {
        given:
        def station = new BaseStation()
        def ms = new MobileStation(station)
        def request = ms.sendRequestToBaseStation()
        when:
        request.finish()
        then:
        request.getId() == ""
    }
}
