package com.bobzone.massservicemodels;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.jsoup.Connection;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by epiobob on 2017-04-07.
 */
@SpringComponent
@UIScope
public class MobileStation {

    private BaseStation connectedBaseStation;
    private ServiceRequest request;
    double requestCreationInterval;

//    public MobileStation(final double requestCreationInterval, final ScheduledExecutorService callingService){
//        this.requestCreationInterval = requestCreationInterval;
//        this.callingService = callingService;
//    }

    public MobileStation(BaseStation baseStationToConnect, final double requestCreationInterval) {
        this.connectedBaseStation = baseStationToConnect;
        this.requestCreationInterval = requestCreationInterval;
    }

    public MobileStation(final double requestCreationInterval) {
        this.requestCreationInterval = requestCreationInterval;
    }

    public ServiceRequest generateServiceRequest(final double callLength) {
        request = new ServiceRequest(callLength);
        return request;
    }

    public void sendRequestToBaseStation() {
        if (null != connectedBaseStation) {
            final ServiceRequest serviceRequest = new ServiceRequest();
            connectedBaseStation.acceptRequest(serviceRequest);
        } else {
            throw new NullPointerException("Attempt to send request, but given MS has no connected BS.");
        }
    }
}
