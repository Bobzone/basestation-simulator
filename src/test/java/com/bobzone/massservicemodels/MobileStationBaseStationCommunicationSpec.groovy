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
        def ms = new MobileStation(bs)
        when:
        ms.sendRequestToBaseStation();
        then:
        bs.channelList.get(0).busy
    }

    @Unroll
    def "MS can start to send multiple requests at fixed rate, BS handles them"() {
        given:
        def bs = new BaseStation()
        def ms = new MobileStation(bs)
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
        def ms = new MobileStation(bs);
        when:
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(8);
        executorService.scheduleAtFixedRate(ms, 1, 1, TimeUnit.SECONDS);
        Thread.sleep(5500)
        then:
        bs.channelList.get(0).busy
        bs.channelList.get(1).busy
        bs.channelList.get(2).busy
        bs.channelList.get(3).busy
        bs.queue.get(0) != null
    }

    @Unroll
    def "BS at fixed rate moves requests in queue to free channels"() {
        given:
        def bs = new BaseStation(4)
        def listOfSentRequests = new ArrayList<ServiceRequest>();
        def ms = new MobileStation(bs)
        for (int i = 0; i < 5; i++) {
            listOfSentRequests.add(ms.sendRequestToBaseStation());
        }
        when:
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(8);
        executorService.scheduleAtFixedRate(bs, 1, 1, TimeUnit.SECONDS);
        Thread.sleep(2000)
        listOfSentRequests.get(3).finish()
        Thread.sleep(3000)
        then:
        bs.channelList.get(0).busy
        bs.channelList.get(1).busy
        bs.channelList.get(2).busy
        bs.channelList.get(3).busy
        bs.queue.size() == 0
    }

    @Unroll
    def "BS at fixed rate moves requests in queue to free channels and requests finishing works in thread as well"() {
        given:
        def bs = new BaseStation(4)
        def requests = new ArrayList<ServiceRequest>()
//        def requests = [new ServiceRequest(1.2), new ServiceRequest(1.4),
//                        new ServiceRequest(1.5), new ServiceRequest(1.8),
//                        new ServiceRequest(1.2)]

        def ms = new MobileStation(bs)
        for (int i = 0; i < 5; i++) {
            requests.add(ms.sendRequestToBaseStation());
        }
//        for (ServiceRequest r : requests) {
//            bs.acceptRequest(r)
//            try {
//                def channel = bs.findRequest(r)
//                r.addPropertyChangeListener(channel)
//            } catch (NoSuchFieldException e) {
//                println e.getMessage()
//            }
//        }
        when:
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(8);
        executorService.scheduleAtFixedRate(bs, 0, 1, TimeUnit.SECONDS);
        executorService.schedule(requests[3], 2, TimeUnit.SECONDS);
        Thread.sleep(5000)
        then:
        bs.channelList.get(0).busy
        bs.channelList.get(1).busy
        bs.channelList.get(2).busy
        bs.channelList.get(3).busy
        bs.queue.size() == 0
    }
}