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

    // TODO - to be implemented for user input
    private static final double LAMBDA_INPUT_PARAM = 2.0;

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

        for (int i = 0; i < 9; i++) {
            MobileStation ms = new MobileStation(RNG.getPoisson(2.0));
            ScheduledFuture schedFuture = executorService.scheduleAtFixedRate(ms, 1L, (long) RNG.getPoisson(LAMBDA_INPUT_PARAM), TimeUnit.SECONDS);
        }

        BaseStation bs = new BaseStation();
        ScheduledFuture schedFuture = executorService.scheduleAtFixedRate(bs, 1, 1, TimeUnit.SECONDS);
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
