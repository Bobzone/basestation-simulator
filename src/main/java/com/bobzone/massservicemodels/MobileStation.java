package com.bobzone.massservicemodels;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by epiobob on 2017-04-07.
 */
@SpringComponent
@UIScope
public class MobileStation implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(MobileStation.class);
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(10);

    private String id;
    private BaseStation connectedBaseStation;
    private ServiceRequest request;

    public MobileStation(BaseStation baseStationToConnect) {
        id = String.valueOf(ID_GENERATOR.getAndIncrement());
        connectedBaseStation = baseStationToConnect;
        log.info("Created new {} with {} as connection endpoint.", this, connectedBaseStation);
    }

    public ServiceRequest generateServiceRequest(final double callLength) {
        log.info("{} generated Service Request on demand.", this);
        request = new ServiceRequest(callLength);
        return request;
    }

    public ServiceRequest sendRequestToBaseStation() {
        log.info("{} sent request to BaseStation {}.", this, connectedBaseStation);
        if (null != connectedBaseStation) {
            final ServiceRequest serviceRequest = new ServiceRequest();
            serviceRequest.addPropertyChangeListener(connectedBaseStation);
            connectedBaseStation.acceptRequest(serviceRequest);
            return serviceRequest;
        } else {
            throw new NullPointerException("Attempt to send request, but given MS has no connected BS.");
        }
    }

    @Override
    public void run() {
        sendRequestToBaseStation();
    }

    public void setConnectedBaseStation(final BaseStation connectedBaseStation) {
        this.connectedBaseStation = connectedBaseStation;
    }

    @Override
    public String toString() {
        return "MobileStation{" +
                "id='" + id + '\'' +
                '}';
    }
}
