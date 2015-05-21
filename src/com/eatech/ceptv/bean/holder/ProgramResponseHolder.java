package com.eatech.ceptv.bean.holder;

import com.eatech.ceptv.bean.channel.ProgramResponse;

import java.util.List;


/**
 * @author erhanasikoglu
 */
public class ProgramResponseHolder {


    private List<ProgramResponse> programResponseList;

    private ProgramResponseHolder() { }

    private static class ProgramResponseHolderInstance {
        public static final ProgramResponseHolder INSTANCE = new ProgramResponseHolder();
    }

    public static ProgramResponseHolder getInstance() {
        return ProgramResponseHolderInstance.INSTANCE;
    }

    public List<ProgramResponse> getProgramResponseList() {
        return programResponseList;
    }

    public void setProgramResponseList(List<ProgramResponse> programResponseList) {
        this.programResponseList = programResponseList;
    }
}
