package com.bobzone.massservicemodels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by epiobob on 2017-04-05.
 */
public class Channel {

    private String id;
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1000);
    private boolean busy;
    private ServiceRequest request;

    public Channel(){
        this.id = String.valueOf(ID_GENERATOR.getAndIncrement());
        request = null;
        this.busy = false;
    }

    public boolean isBusy() {
        return busy;
    }

    public void freeChannel(){
        request = null;
        busy = false;
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
    public String toString() {
        return "Channel{" +
                "ID:" + id +
                '}';
    }
}
