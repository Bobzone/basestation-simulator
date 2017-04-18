package com.bobzone.massservicemodels;

import com.vaadin.annotations.Theme;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public static double LAMBDA_INPUT_PARAM;
    public static double MEAN_INPUT_PARAM;
    public static double VARIATION_PARAM;

    private HorizontalLayout mainLayout;
    private VerticalLayout mainLayoutLeft;
    private VerticalLayout mainLayoutRight;
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
    private ScheduledExecutorService refreshUIWorker = Executors.newScheduledThreadPool(4);

    GraphGenerator graphGenerator = new GraphGenerator();

    private Label sizeOfListLabel;
    private Label freeChannelsLabel;
    private Label simulationTimer;

    private long simulationStartTime;

    private List<Channel> requestsInChannels = new ArrayList<Channel>();
    private HorizontalLayout monitorLayout;
    private Label requestIdLabel;
    private Label callTimeRemaningLabel;
    private Grid<Channel> grid;

    private int counter = 0;
    private List<Integer> clientsInQueue = new ArrayList<>();
    private List<Double> dataForGraph1 = new ArrayList<>();
    private List<Double> timeCounter = new ArrayList<>();
    private Image image;
    private FileResource resource;

    private Label meanQueueLabel;
    private Double aDouble;
    private TextField text1;
    private TextField text2;
    private TextField text3;

    //    TODO - read about autowiring this for your lab
//    @Autowired
//    BaseStation baseStation;
    private BaseStation bs;
//
//    @Autowired
//    MobileStation mobileStation;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        bs = new BaseStation();
        setupLayouts();
        addHeader();
        addButtonsAndTimer();
        addParameterControls();
        addCountersForVariousData();
        addServiceRequestHandlingMonitor();
//        addFirstGraph();
    }

    private void addParameterControls() {
        Label info1 = new Label("LAMBDA Parameter:");
        text1 = new TextField();
        text1.setRequiredIndicatorVisible(true);
        final HorizontalLayout components1 = new HorizontalLayout(info1, text1);
        Label info2 = new Label("MEAN Parameter:");
        text2 = new TextField();
        final HorizontalLayout components2 = new HorizontalLayout(info2, text2);
        Label info3 = new Label("VARIATION Parameter:");
        text3 = new TextField();
        final HorizontalLayout components3 = new HorizontalLayout(info3, text3);
        final VerticalLayout verticalLayout = new VerticalLayout(components1, components2, components3);
        mainLayoutLeft.addComponent(verticalLayout);
    }

    private void addFirstGraph() {
        resource = new FileResource(new File("C:\\Users\\epiobob\\Documents\\Dev\\simulator\\meanQueueSize - start.png"));
        image = new Image("", resource);
        mainLayout.addComponent(image);
    }

    private void addServiceRequestHandlingMonitor() {
        grid = new Grid<>();
        grid.addColumn(Channel::toString);
        grid.addColumn(x -> {
            if (null == x.getRequest()) {
                return "No request on this channel";
            } else
                return x.getRequest().getId();
        });
        grid.addColumn(x -> {
            if (null == x.getRequest()) {
                return "--";
            } else {
                return x.getRequest().getCallLength();
            }
        });
        grid.setItems(bs.channelList);
        mainLayoutRight.addComponent(grid);
    }

    private void addCountersForVariousData() {
        Label infoLabel = new Label("Free channels:");
        freeChannelsLabel = new Label(bs.getChannelListSize());
        freeChannelsLabel.setWidth("100px");

        Label infoLabel2 = new Label("Mean requests in queue:");
        meanQueueLabel = new Label("0");
        meanQueueLabel.setWidth("100px");

        Label infoLabel3 = new Label("Items in queue:");
        sizeOfListLabel = new Label("0");
        sizeOfListLabel.setWidth("100px");

        VerticalLayout queueMonitorLayout = new VerticalLayout(infoLabel, freeChannelsLabel, infoLabel2, meanQueueLabel, infoLabel3, sizeOfListLabel);
        mainLayoutLeft.addComponent(queueMonitorLayout);
    }

    private void addButtonsAndTimer() {
        Button buttonStart = new Button("Start");
        simulationTimer = new Label("00:00:00");
        Button buttonStop = new Button("Stop");

        buttonStart.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                LAMBDA_INPUT_PARAM = Double.valueOf(text1.getValue());
                MEAN_INPUT_PARAM = Double.valueOf(text2.getValue());
                VARIATION_PARAM = Double.valueOf(text3.getValue());
                log.info("Simulation started by user.");
                simulationStartTime = System.currentTimeMillis();
                UI.getCurrent().setPollInterval(1000);
                startBackend();
            }
        });
        buttonStop.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                log.info("Simulation ended by user.");
                UI.getCurrent().setPollInterval(-1);
                stopBackend();
            }
        });

        final HorizontalLayout controls = new HorizontalLayout(buttonStart, simulationTimer, buttonStop);
        mainLayoutLeft.addComponent(controls);
    }

    private void addHeader() {
        Label welcomeLabel = new Label("Base station simulator");
        mainLayoutLeft.addComponent(welcomeLabel);
    }

    private void setupLayouts() {
        mainLayout = new HorizontalLayout();
        setContent(mainLayout);
//        mainLayout.setSizeFull();
        mainLayoutLeft = new VerticalLayout();
        mainLayoutRight = new VerticalLayout();
        mainLayout.addComponent(mainLayoutLeft);
        mainLayout.addComponent(mainLayoutRight);
    }

    private void startBackend() {
        ScheduledFuture schedFutureBaseStationTasks = executorService.scheduleAtFixedRate(bs, 1, 1, TimeUnit.SECONDS);

        for (int i = 0; i < 9; i++) {
            MobileStation ms = new MobileStation(bs);
            ScheduledFuture schedFutureMobileStationTasks = executorService.scheduleAtFixedRate(ms, 1L, (long) RNG.getPoisson(LAMBDA_INPUT_PARAM), TimeUnit.SECONDS);
        }
        executorService.scheduleAtFixedRate(UIRefresher, 1, 1L, TimeUnit.SECONDS);
    }

    private Runnable UIRefresher = new Runnable() {
        @Override
        public void run() {
            final long millis = System.currentTimeMillis() - simulationStartTime;

            sizeOfListLabel.setValue(bs.getCurrentQueueSize());
            freeChannelsLabel.setValue(bs.getNumberOfFreeChannels());

            long second = (millis / 1000) % 60;
            long minute = (millis / (1000 * 60)) % 60;
            long hour = (millis / (1000 * 60 * 60)) % 24;
            String time = String.format("%02d:%02d:%02d", hour, minute, second);
            simulationTimer.setValue(time);

            bs.channelList.forEach(channel -> {
                if (channel.getRequest() != null) {
                    channel.getRequest().decrementCallLength();
                }
            });
            grid.setItems(bs.channelList);

            countMean();

            meanQueueLabel.setValue(String.valueOf(aDouble / counter));
        }
    };

    private void stopBackend() {
        executorService.shutdown();
        refreshUIWorker.shutdown();
    }

    private void countMean() {
        // counting mean length of queue
        clientsInQueue.add(bs.queue.size());
        counter++;
        timeCounter.add((double) counter);
        final Optional<Double> reduce = clientsInQueue.stream().map(Integer::doubleValue).reduce((aDouble, aDouble2) -> aDouble + aDouble2);
        aDouble = reduce.get();
        dataForGraph1.add(aDouble / counter);
        try {
            graphGenerator.generateGraph("meanQueueSize", "Mean queue size",
                    "Time of simulation", "Requests in queue", "Requests",
                    timeCounter, dataForGraph1);
        } catch (IOException e) {
        }
    }
}
