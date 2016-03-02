package com.verification;

import java.util.HashMap;

/**
 * Created by risha on 02-03-2016.
 */
public class PI extends component {
    public PI(){
        inputs = 0;
        outputs = 1;
        input_wires = null;
        output_wires = new Integer[1];
    }
    @Override
    public void propogate_controllability(HashMap list) {
        wire outputwire = (wire) list.get(output_wires[0]);
        outputwire.cc0 = 1;
        outputwire.cc1 = 1;
        ((component)list.get(outputwire.outputgate_id)).propogate_controllability(list);
    }
}
