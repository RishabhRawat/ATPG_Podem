package com.verification;

import com.verification.components.PI;

/**
 * Created by risha on 07-03-2016.
 */
public class BnBTree{

    private class TreeNode {
        private PI assignedInput;
        public global.FvLogic assignedValue;
        public boolean flag = false;
        public boolean active = true;
        public TreeNode(BnBTree parentTree, PI input, global.FvLogic inputValue){
            parent = parentTree;
            assignedInput = input;
            assignedValue = inputValue;
        }
        public PI getAssignedInput() {
            return assignedInput;
        }
        private BnBTree parent;
    }

    private TreeNode node = null;
    private BnBTree[] child = new BnBTree[2];
    public boolean isRoot(){
        return node==null;
    }
    private BnBTree(TreeNode newNode){
        node = newNode;
    }

    public void addPI(PI input, global.FvLogic inputValue) throws InvalidOperationException {
        if(child[0]!=null)
            throw new InvalidOperationException();
        TreeNode newNode = new TreeNode(input, inputValue);
        child[0] = new BnBTree(newNode);
    }

    public void switchNode(){
        node.active = false;
        if(node.flag){

        }
    }



}