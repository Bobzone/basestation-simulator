package com.bobzone.massservicemodels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by epiobob on 2017-04-05.
 */
public class BaseStation {

    List<Channel> channelList = new ArrayList<>();

    public void assignChannel(final ServiceRequest sr) {
//        channelList.get(0).setRequest();
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
}
