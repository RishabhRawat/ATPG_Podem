package com.verification.components;

import com.verification.ConfictedImplicationException;
import com.verification.global;
import com.verification.wire;

import java.util.ArrayList;

public class PO extends component {
    public PO(){
        inputs = 1;
        outputs = 0;
        input_wires = new Integer[1];
        output_wires = null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void propogate_controllability() {
        global.cc_complete_count++;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ArrayList<wire> imply() throws ConfictedImplicationException {
        //TODO: worry about what happens here
        return null;
    }
}
