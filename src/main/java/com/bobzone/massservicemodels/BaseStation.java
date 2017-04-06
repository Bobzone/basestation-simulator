package com.bobzone.massservicemodels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by epiobob on 2017-04-05.
 */
public class BaseStation {

    List<Channel> channelList = new ArrayList<>();
    private List<ServiceRequest> queue = new ArrayList<>();

    private void assignChannel(final ServiceRequest request) {
        try {
            channelList.get(0).setRequest(request);
            queue.remove(0);
        } catch (IllegalStateException ex){
            System.out.println(ex.getMessage() + " is currently busy. Leaving request in the queue.");
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
}
