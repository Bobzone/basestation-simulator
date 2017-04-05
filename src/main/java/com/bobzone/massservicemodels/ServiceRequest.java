package com.bobzone.massservicemodels;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Created by epiobob on 2017-04-05.
 */
public class ServiceRequest {

    PropertyChangeSupport support = new PropertyChangeSupport(this);

    private String id;

    public ServiceRequest() {
        id = "randomlygeneratedid";
    }

    void addPropertyChangeListener(final PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public void finish() {
        id = "";
        support.firePropertyChange("id", id, "");
        support.firePropertyChange("request", null, null);
        support.firePropertyChange("busy", null, false);
    }

    public String getId() {
        return id;
    }
}
