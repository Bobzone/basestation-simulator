package com.bobzone.massservicemodels;

import com.vaadin.annotations.Push;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by epiobob on 2017-04-05.
 */
@SpringComponent
@UIScope
public class BaseStation implements Runnable, PropertyChangeListener {
    private static final Logger log = LoggerFactory.getLogger(BaseStation.class);
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    private String id;
    protected List<Channel> channelList = new ArrayList<>();
    protected List<ServiceRequest> queue = new ArrayList<>();
    private volatile int currentQueueSize;

    private void assignChannel(final ServiceRequest request) {
        try {
            final Channel freeChannel = findFreeChannel();
            request.startFinishWorker();
            freeChannel.setRequest(request);
            queue.remove(request);
            currentQueueSize = queue.size();
            log.info("Free {} found. {} has assigned channel and is removed from {} queue. Current queue size: {}", freeChannel, request, this, currentQueueSize);
        } catch (IllegalStateException ex) {
            log.info(ex.getMessage() + " is currently busy. Leaving request in the queue.");
        } catch (NoSuchFieldException e) {
            log.info(e.getMessage() + " Leaving request in the queue.");
        }
    }

    public BaseStation() {
        log.info("Created default BaseStation with 8 channels.");
        id = String.valueOf(ID_GENERATOR.getAndIncrement());
        for (int i = 0; i < 8; i++) {
            channelList.add(new Channel());
        }
    }

    public BaseStation(int numberOfChannels) {
        log.info("Created BaseStation with {} channels. ", numberOfChannels);
        id = String.valueOf(ID_GENERATOR.getAndIncrement());
        for (int i = 0; i < numberOfChannels; i++) {
            channelList.add(new Channel());
        }
    }

    public void acceptRequest(final ServiceRequest request) {
        log.info("Accepting {}. Request lands on queue, BS checks if any channel is free. Current queue size: {}", request, currentQueueSize);
        queue.add(request);
        currentQueueSize = queue.size();
        assignChannel(request);
    }

    private Channel findFreeChannel() throws NoSuchFieldException {
        for (Channel c : channelList) {
            if (!c.isBusy()) {
                return c;
            }
        }
        throw new NoSuchFieldException("All channels are currently busy.");
    }

    protected synchronized void assignChannelFromQueue() throws NoSuchFieldException {
        final Channel freeChannel = findFreeChannel();
        if (queue.size() != 0) {
            final ServiceRequest request = queue.get(0);
            request.startFinishWorker();
            freeChannel.setRequest(request);
            queue.remove(request);
            currentQueueSize = queue.size();
            log.info("Periodic channel assignment moved {} from queue to {}. Current queue size: {}", request, freeChannel, queue.size());

            queue = buildNewQueue();
        } else {
            throw new NoSuchFieldException("There are no requests waiting in the queue. Periodic channel assignment did nothing.");
        }
    }

    //    TODO - probably TERRIBLY inefficient. But it works.
    private List<ServiceRequest> buildNewQueue() {
        log.debug("Moved all items in queue to the left.");
        List<ServiceRequest> newQueue = new ArrayList<>();

        for (ServiceRequest sr : queue) {
            if (sr != null) {
                newQueue.add(sr);
            }
        }

        return newQueue;
    }

    @Override
    public void run() {
        log.debug("Periodic channel assignment running. Items in queue: {}", queue.size());
        try {
            assignChannelFromQueue();
        } catch (NoSuchFieldException e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ServiceRequest requestSendingEvent = (ServiceRequest) evt.getOldValue();
        for (Channel c : channelList) {
            if (c.getRequest() == requestSendingEvent) {
                c.freeChannel();
            }
        }
    }

    @Override
    public String toString() {
        return "BaseStation{" +
                "id='" + id + '\'' +
                '}';
    }

    public String getCurrentQueueSize() {
        return String.valueOf(currentQueueSize);
    }

    public String getNumberOfFreeChannels() {
        final long count = channelList.stream().filter(channel -> !channel.isBusy()).count();
        return String.valueOf(count);
    }

    public String getChannelListSize(){
        return String.valueOf(channelList.size());
    }

}
