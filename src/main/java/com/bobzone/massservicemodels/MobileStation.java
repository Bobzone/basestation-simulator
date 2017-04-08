package com.bobzone.massservicemodels;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by epiobob on 2017-04-07.
 */
@SpringComponent
@UIScope
public class MobileStation implements Runnable {

    private final ScheduledExecutorService callingService;
    double requestCreationInterval;
    private ServiceRequest request;

//    public MobileStation(final double requestCreationInterval, final ScheduledExecutorService callingService){
//        this.requestCreationInterval = requestCreationInterval;
//        this.callingService = callingService;
//    }

    public MobileStation(final double requestCreationInterval) {
        this.requestCreationInterval = requestCreationInterval;
        callingService = Executors.newSingleThreadScheduledExecutor();
    }

    public ServiceRequest generateServiceRequest(final double callLength) {
        request = new ServiceRequest(callLength);
        return request;
    }

    public void startMakingCalls() {
        callingService.scheduleAtFixedRate(this, (long) requestCreationInterval, (long) requestCreationInterval, TimeUnit.SECONDS);
    }

    public void stopMakingCalls() {
        callingService.shutdown();
    }

    @Override
    public void run() {
        if (request != null) {
            request.finish();
        }
        generateServiceRequest(RNG.getGaussian(30.0, 5.0));
        System.out.println(request.toString());
    }
}
