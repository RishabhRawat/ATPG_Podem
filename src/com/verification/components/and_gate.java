package com.verification.components;

import com.verification.wire;

import java.util.HashMap;

/**
 * Created by risha on 02-03-2016.
 */
public final class and_gate extends component {
    public and_gate(){
        inputs = 2;
        outputs = 1;
        input_wires = new Integer[2];
        output_wires = new Integer[1];
    }

    /**
     * {@inheritDoc}
     * @param list
     */
    @Override
    public void propogate_controllability(HashMap list) {
        wire outputwire = (wire) list.get(output_wires[0]);
        wire input1 = (wire) list.get(input_wires[0]);
        wire input2 = (wire) list.get(input_wires[1]);
        if(input1.cc0 == -1 || input2.cc0 == -1)
            return;
        outputwire.cc0 = ((input1.cc0>input2.cc0)?input2.cc0:input1.cc0)+1;
        outputwire.cc1 = input1.cc1 + input2.cc1 + 1;
    }
}
