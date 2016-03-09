package com.verification.BranchnBound;

import com.verification.components.PI;
import com.verification.global;
import com.verification.wire;

import java.util.Stack;

/**
 * The Branch and Bound tree implemented as a stack
 */
public class BnBStack extends Stack<BnBNode> {
    /**
     * Inserts a new node to the stack corrosponding to a assignment at input
     * @param input the PI being assigned
     * @param inputValue the value being assigned
     * @return the inserted node
     */
    private wire faultSite = null;

    private BnBNode insertNode(PI input, global.FvLogic inputValue){
        BnBNode node = new BnBNode(input,inputValue);
        push(node);
        return node;
    }

    private void swap(){
        BnBNode topNode = peek();
        if(topNode.flag){
            topNode.active = false;
            pop();
            swap();
        }
        else {
            topNode.flag = true;
            topNode.assignedValue = global.FvLogic.not(topNode.assignedValue);
            //TODO: implement further implication
        }
    }

    public BnBStack(wire faultSite) {
        this.faultSite = faultSite;
    }

    public void execute() {

    }
}
