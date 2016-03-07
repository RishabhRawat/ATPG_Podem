package com.verification;

import com.verification.BranchnBound.BnBNode;

public final class wire {

    public int cc0 = -1;
    public int cc1 = -1;
    public Integer inputgate_id, outputgate_id;
    public Integer hashID;
    public wire(Integer id){
        hashID = id;
    }
    public global.FvLogic assignment = global.FvLogic.X;

    public BnBNode assignment_node;
}
