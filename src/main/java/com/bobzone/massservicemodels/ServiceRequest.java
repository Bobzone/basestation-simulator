package com.bobzone.massservicemodels;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by epiobob on 2017-04-05.
 */
public class ServiceRequest implements Runnable {
    private AtomicInteger ID_GENERATOR = new AtomicInteger(1000);

    PropertyChangeSupport support = new PropertyChangeSupport(this);

    private String id;
    double callLength;

    public ServiceRequest(final double callLength) {
        this.callLength = callLength;
//        id = UUID.randomUUID().toString();
        id = String.valueOf(ID_GENERATOR.getAndIncrement());
    }

    public ServiceRequest() {
        id = UUID.randomUUID().toString();
    }

    void addPropertyChangeListener(final PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public void finish() {
//        support.firePropertyChange("id", id, ""); //-- not needed according to test written
        support.firePropertyChange("request", null, null);
        support.firePropertyChange("busy", null, false);
        System.err.println("finish() method on request " + this);
        id = "";
    }

    public String getId() {
        return id;
    }

    @Override
    public void run() {
        finish();
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "id='" + id + '\'' +
                '}';
    }
}
