package com.bobzone.massservicemodels;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;


/**
 * Created by epiobob on 2017-04-05.
 */
public class ServiceRequest implements Runnable {
    private AtomicInteger ID_GENERATOR = new AtomicInteger(1000);

    PropertyChangeSupport support = new PropertyChangeSupport(this);
    ScheduledExecutorService finishRequestWorker = Executors.newSingleThreadScheduledExecutor();

    private String id;
    long callLength;

    public ServiceRequest(final double callLength) {
        this.callLength = RNG.getGaussian(MainUI.MEAN_INPUT_PARAM, MainUI.VARIATION_PARAM);
        id = String.valueOf(ID_GENERATOR.getAndIncrement());
        finishRequestWorker.schedule(this, this.callLength, SECONDS);
    }

    public ServiceRequest() {
        this.callLength = RNG.getGaussian(MainUI.MEAN_INPUT_PARAM, MainUI.VARIATION_PARAM);
        id = String.valueOf(ID_GENERATOR.getAndIncrement());
        finishRequestWorker.schedule(this, this.callLength, SECONDS);
    }

    void addPropertyChangeListener(final PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public void finish() {
        System.err.println("finish() method on request " + this);
        support.firePropertyChange("request", this, null);
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
