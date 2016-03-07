package com.verification.components;

import com.verification.wire;

import java.util.HashMap;

/**
 * Created by risha on 02-03-2016.
 */
public class fanout_gate extends component {
    public fanout_gate(int out){
        inputs = 1;
        outputs = out;
        input_wires = new Integer[1];
        output_wires = new Integer[out];
    }
    @Override
    public void propogate_controllability(HashMap list) {
        wire input = (wire) list.get(input_wires[0]);

        for (Integer output:output_wires) {
            wire outputwire = (wire) list.get(output);
            outputwire.cc0 = input.cc0;
            outputwire.cc1 = input.cc1;
        }
    }
}
