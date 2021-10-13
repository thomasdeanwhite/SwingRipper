package com.thomasdeanwhite.swingripper.guitree;

import java.util.ArrayList;
import java.util.HashMap;

public class GuiLeaf extends GuiNode {

    private static final HashMap<String, GuiLeaf> NAMES = new HashMap<>();

    private GuiLeaf(String name){
        super(name);
    }

    public static GuiLeaf getLeaf(String name){
        GuiLeaf gl;

        if (!NAMES.containsKey(name)) {
            gl = new GuiLeaf(name);
            NAMES.put(name, gl);
        } else {
            gl = NAMES.get(name);
        }

        return gl;

    }
}
