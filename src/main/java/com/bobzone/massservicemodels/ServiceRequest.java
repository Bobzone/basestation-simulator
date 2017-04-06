package com.bobzone.massservicemodels;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.UUID;

/**
 * Created by epiobob on 2017-04-05.
 */
public class ServiceRequest {

    PropertyChangeSupport support = new PropertyChangeSupport(this);

    private String id;

    public ServiceRequest() {
        id = UUID.randomUUID().toString();
    }

    void addPropertyChangeListener(final PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public void finish() {
        id = "";
//        support.firePropertyChange("id", id, ""); //-- not needed according to test written
        support.firePropertyChange("request", null, null);
        support.firePropertyChange("busy", null, false);
    }

    public String getId() {
        return id;
    }
}
