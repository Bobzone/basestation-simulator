package com.bobzone.massservicemodels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by epiobob on 2017-04-05.
 */
public class Channel implements PropertyChangeListener{

    private boolean busy;
    private ServiceRequest request;

    public Channel() {
        this.busy = false;
    }

    public boolean isBusy() {
        return busy;
    }

    public ServiceRequest getRequest() {
        return request;
    }

    public void setRequest(final ServiceRequest request) {
        this.request = request;
        busy = true;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        request = null;
        busy = false;
    }
}
