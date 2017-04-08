package com.bobzone.massservicemodels;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

/**
 * Created by epiobob on 2017-04-07.
 */
@SpringComponent
@UIScope
public class MobileStation implements Runnable {

    private BaseStation connectedBaseStation;
    private ServiceRequest request;

    public MobileStation(BaseStation baseStationToConnect){
        connectedBaseStation = baseStationToConnect;
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

    @Override
    public void run() {
        sendRequestToBaseStation();
    }

    public void setConnectedBaseStation(final BaseStation connectedBaseStation) {
        this.connectedBaseStation = connectedBaseStation;
    }
}
