package com.erhanasikoglu.ceptv.bean.holder;

import com.erhanasikoglu.ceptv.bean.channel.ChannelResponse;

import java.util.List;

/**
 * @author erhanasikoglu
 */
public class ChannelResponseHolder {


    private List<ChannelResponse> channelResponseList;

    private ChannelResponseHolder() { }

    private static class ChannelResponseHolderInstance {
        public static final ChannelResponseHolder INSTANCE = new ChannelResponseHolder();
    }

    public static ChannelResponseHolder getInstance() {
        return ChannelResponseHolderInstance.INSTANCE;
    }

    public List<ChannelResponse> getChannelResponseList() {
        return channelResponseList;
    }

    public void setChannelResponseList(List<ChannelResponse> channelResponseList) {
        this.channelResponseList = channelResponseList;
    }
}
