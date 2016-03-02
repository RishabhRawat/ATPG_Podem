package com.verification;

import java.util.HashMap;

/**
 * Created by risha on 02-03-2016.
 */
public class not_gate extends component{
    public not_gate(){
        inputs = 1;
        outputs = 1;
        input_wires = new Integer[1];
        output_wires = new Integer[1];
    }
    @Override
    public void propogate_controllability(HashMap list) {
        wire outputwire = (wire) list.get(output_wires[0]);
        wire input1 = (wire) list.get(input_wires[0]);
        outputwire.cc0 = input1.cc0 + 1;
        outputwire.cc1 = input1.cc1 + 1;
    }
}
