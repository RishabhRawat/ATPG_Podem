package com.verification;

import java.util.HashMap;

/**
 * Created by risha on 02-03-2016.
 */
public class PO extends component {
    public PO(){
        inputs = 1;
        outputs = 0;
        input_wires = new Integer[1];
        output_wires = null;
    }
    @Override
    public void propogate_controllability(HashMap list) {
        global.cc_complete_count++;
    }
}
