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
    }

    void addPropertyChangeListener(final PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public void finish() {
        support.firePropertyChange("id", id, "");
    }
}
