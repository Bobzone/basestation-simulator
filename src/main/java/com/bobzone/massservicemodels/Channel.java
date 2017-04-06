package com.bobzone.massservicemodels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by epiobob on 2017-04-05.
 */
public class Channel implements PropertyChangeListener{

    private boolean busy;
    private ServiceRequest request;

    public Channel(){
        request = null;
        this.busy = false;
    }

    public boolean isBusy() {
        return busy;
    }

    public ServiceRequest getRequest() {
        return request;
    }

    public void setRequest(final ServiceRequest request) {
        if (this.request == null && !busy) {
            this.request = request;
            busy = true;
        } else {
            throw new IllegalStateException("Channel " + this.toString() + " is already processing a request.");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        request = null;
        busy = false;
    }
}
