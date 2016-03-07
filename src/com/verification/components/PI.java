package com.verification.components;

import com.verification.ConfictedImplicationException;
import com.verification.global;
import com.verification.wire;

import java.util.ArrayList;

public class PI extends component {
    public PI(){
        inputs = 0;
        outputs = 1;
        input_wires = null;
        output_wires = new Integer[1];
    }

    /**
     * @inheritDoc
     */
    @Override
    public void propogate_controllability() {
        wire outputwire = (wire) global.all_components.get(output_wires[0]);
        outputwire.cc0 = 1;
        outputwire.cc1 = 1;
        ((component)global.all_components.get(outputwire.outputgate_id)).propogate_controllability();
    }


    @Override
    public ArrayList<wire> imply() throws ConfictedImplicationException {
            throw new ConfictedImplicationException();
    }
}
