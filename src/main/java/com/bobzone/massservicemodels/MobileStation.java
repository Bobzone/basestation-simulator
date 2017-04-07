package com.bobzone.massservicemodels;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

/**
 * Created by epiobob on 2017-04-07.
 */
@SpringComponent
@UIScope
public class MobileStation {

    double requestCreationInterval;

    public MobileStation(final double requestCreationInterval) {
        this.requestCreationInterval = requestCreationInterval;
    }

    public ServiceRequest generateServiceRequest(final double callLength){
        return new ServiceRequest(callLength);
    }
}
