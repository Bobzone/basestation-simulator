package com.bobzone.massservicemodels;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by epiobob on 2017-04-05.
 */
@SpringComponent
@UIScope
public class BaseStation implements Runnable{

    List<Channel> channelList = new ArrayList<>();
    private List<ServiceRequest> queue = new ArrayList<>();

    private void assignChannel(final ServiceRequest request) {
        try {
            findFreeChannel().setRequest(request);
            queue.remove(request);
        } catch (IllegalStateException ex) {
            System.out.println(ex.getMessage() + " is currently busy. Leaving request in the queue.");
        } catch (NoSuchFieldException e) {
            System.out.println(e.getMessage() + " Leaving request in the queue.");
        }
    }

    public BaseStation() {
        for (int i = 0; i < 8; i++) {
            channelList.add(new Channel());
        }
    }

    public BaseStation(int numberOfChannels) {
        for (int i = 0; i < numberOfChannels; i++) {
            channelList.add(new Channel());
        }
    }

    public void acceptRequest(final ServiceRequest request) {
        queue.add(request);
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

    @Override
    public void run() {

    }
}
