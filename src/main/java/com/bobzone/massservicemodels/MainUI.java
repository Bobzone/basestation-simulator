package com.bobzone.massservicemodels;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

/**
 * Created by epiobob on 2017-04-04.
 */
@SpringUI
@Theme("valo")
public class MainUI extends UI {

    private HorizontalLayout mainLayout;

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
