package com.bobzone.massservicemodels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.UUID;

/**
 * Created by epiobob on 2017-04-05.
 */
public class Channel implements PropertyChangeListener{

    private String id;
    private boolean busy;
    private ServiceRequest request;

    public Channel(){
        this.id = UUID.randomUUID().toString();
        request = null;
        this.busy = false;
    }

    public boolean isBusy() {
        return busy;
    }

    public ServiceRequest getRequest() {
        return request;
    }

    public void setRequest(final ServiceRequest request) throws IllegalStateException {
        if (this.request == null && !busy) {
            this.request = request;
            busy = true;
        } else {
            throw new IllegalStateException("Request{ID:" + request.getId() + "} demanding service from " + toString() + " but the channel was busy. Request kept in queue.");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        request = null;
        busy = false;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "ID:" + id +
                '}';
    }
}
