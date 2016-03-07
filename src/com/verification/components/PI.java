package com.verification.components;

import com.verification.global;
import com.verification.wire;

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
    public void propogate_controllability() {
        wire outputwire = (wire) global.all_components.get(output_wires[0]);
        outputwire.cc0 = 1;
        outputwire.cc1 = 1;
        ((component)global.all_components.get(outputwire.outputgate_id)).propogate_controllability(global.all_components);
    }
}
