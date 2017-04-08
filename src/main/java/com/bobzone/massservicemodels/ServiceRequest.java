package com.bobzone.massservicemodels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger log = LoggerFactory.getLogger(MobileStation.class);
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(100);

    PropertyChangeSupport support = new PropertyChangeSupport(this);
    ScheduledExecutorService finishRequestWorker = Executors.newSingleThreadScheduledExecutor();

    private String id;
    long callLength;

    public ServiceRequest(final double callLength) {
        this.callLength = RNG.getGaussian(MainUI.MEAN_INPUT_PARAM, MainUI.VARIATION_PARAM);
        id = String.valueOf(ID_GENERATOR.getAndIncrement());
        log.info("Created new {} with generated call length {}", this, this.callLength);
    }

    public ServiceRequest() {
        this.callLength = RNG.getGaussian(MainUI.MEAN_INPUT_PARAM, MainUI.VARIATION_PARAM);
        id = String.valueOf(ID_GENERATOR.getAndIncrement());
        log.info("Created new {} with generated call length {}", this, this.callLength);
    }

    void addPropertyChangeListener(final PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public void finish() {
        log.info("{} is finished. Sending END_REQUEST event.", this);
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

    public void startFinishWorker() {
        finishRequestWorker.schedule(this, this.callLength, SECONDS);
    }
}
