package com.verification;

/**
 * Created by risha on 02-03-2016.
 */
public final class wire {

    public int cc0 = -1;
    public int cc1 = -1;
    public Integer inputgate_id, outputgate_id;
    public Integer wire_id;
    public wire(Integer id){
        wire_id = id;
    }
    public global.FvLogic assignment = global.FvLogic.X;
    /**
     * "" means unmodified value
     */
    public String assignment_stack_hash = "";
}
