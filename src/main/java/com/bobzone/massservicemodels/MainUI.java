package com.bobzone.massservicemodels;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.*;

/**
 * Created by epiobob on 2017-04-04.
 */
@SpringUI
@Theme("valo")
public class MainUI extends UI {

    private HorizontalLayout mainLayout;

//    TODO - read about autowiring this for your lab
//    @Autowired
//    BaseStation baseStation;
//
//    @Autowired
//    MobileStation mobileStation;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setupLayouts();
        addHeader();
        addRunningTimer();
        addButtons();
        addProcessingQueue();

//      Request queueing / handling backend
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(8);

//        for (int i = 0; i < 9; i++) {
//            MobileStation ms = new MobileStation(RNG.getPoisson(2.0));
//            ScheduledFuture schedFuture = executorService.scheduleAtFixedRate(ms, (long) ms.requestCreationInterval, (long) ms.requestCreationInterval, TimeUnit.SECONDS);
//        }
//
//        BaseStation bs = new BaseStation();
//        ScheduledFuture schedFuture = executorService.scheduleAtFixedRate(bs, 1, 1, TimeUnit.SECONDS);

//        ExecutorService consumer = new ThreadPoolExecutor(1,4,30, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(100));

//        ScheduledExecutorService requestHandlingService = new ScheduledThreadPoolExecutor(1);

//        ExecutorService producer = Executors.newSingleThreadExecutor();

//        ScheduledExecutorService requestGeneratorService = new ScheduledThreadPoolExecutor(1);

//        Runnable mobileStation = new MobileStation(RNG.getPoisson(2.0), requestHandlingService);
//        requestGeneratorService.submit(mobileStation);

    }

    private void addProcessingQueue() {
    }

    private void addButtons() {
        Button buttonStart = new Button("Start");
        Button buttonStop = new Button("Stop");

        mainLayout.addComponent(buttonStart);
        mainLayout.addComponent(buttonStop);
    }

    private void addRunningTimer() {
    }

    private void addHeader() {
        Label welcomeLabel = new Label("Base station simulator");
        mainLayout.addComponent(welcomeLabel);
    }

    private void setupLayouts() {
        mainLayout = new HorizontalLayout();
        setContent(mainLayout);
    }
}
