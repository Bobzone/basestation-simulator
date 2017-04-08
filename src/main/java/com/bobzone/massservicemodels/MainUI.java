package com.bobzone.massservicemodels;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by epiobob on 2017-04-04.
 */
@SpringUI
@Theme("valo")
public class MainUI extends UI {
    private static final Logger log = LoggerFactory.getLogger(MainUI.class);
    // TODO - to be implemented for user input
    public static final double LAMBDA_INPUT_PARAM = 15.0;
    public static final double MEAN_INPUT_PARAM = 30.0;
    public static final double VARIATION_PARAM = 5.0;

    private HorizontalLayout mainLayout;
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(8);
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

    }

    private void addProcessingQueue() {
    }

    private void addButtons() {
        Button buttonStart = new Button("Start");
        Button buttonStop = new Button("Stop");

        buttonStart.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                log.info("Simulation started by user.");
                startBackend();
            }
        });
        buttonStop.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                log.info("Simulation ended by user.");
                stopBackend();
            }
        });

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

    private void startBackend() {
//      Request queueing / handling backend
        BaseStation bs = new BaseStation();
        ScheduledFuture schedFutureBaseStationTasks = executorService.scheduleAtFixedRate(bs, 1, 1, TimeUnit.SECONDS);

        for (int i = 0; i < 9; i++) {
//            MobileStation ms = new MobileStation(RNG.getPoisson(2.0));
            MobileStation ms = new MobileStation(bs);
            ScheduledFuture schedFutureMobileStationTasks = executorService.scheduleAtFixedRate(ms, 1L, (long) RNG.getPoisson(LAMBDA_INPUT_PARAM), TimeUnit.SECONDS);
        }
    }

    private void stopBackend() {
        executorService.shutdown();
    }
}
