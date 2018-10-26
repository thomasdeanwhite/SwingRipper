package com.thomasdeanwhite.swingripper.guitree;

public class GuiTree {



    private GuiNode root;

    public GuiTree(GuiNode root){
        this.root = root;
    }

    public GuiTree(String source){
        root = GuiNode.nodeFromString(source);
    }



    public String export() {
        // serialise tree into String
        return root.exportChildren();
    }

    public GuiNode getRoot(){
        return root;
    }

    @Override
    public String toString() {
        // recursive print tree



        return root.printChildren();
    }

    public String toJson() {
        // recursive print tree

        return root.printChildrenJson();
    }
}
