package com.thomasdeanwhite.swingripper.guitree;

import com.mysql.jdbc.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class GuiNode {

    private static final ArrayList<String> NAMES = new ArrayList<>();

    private ArrayList<GuiNode> children;

    private int nameIndex;

    public GuiNode(String name){
        if (!NAMES.contains(name)){
            NAMES.add(name);
        }
        nameIndex = NAMES.indexOf(name);
        children = new ArrayList<>();
    }

    public String getName(){
        return NAMES.get(nameIndex);
    }

    public void addChild(GuiNode child){
        children.add(child);
    }

    public ArrayList<GuiNode> getChildren() {
        return children;
    }

    public int getChildrenCount() {
        return children.size();
    }

    public String exportChildren(int indentation){
        String out = "";

        for (int i = 0; i < indentation; i++) out = out + "  ";

        out += "[" + getName();

        for (GuiNode c : children){
            out += "\n" + c.exportChildren(indentation+1);
        }

        out += "\n";

        for (int i = 0; i < indentation; i++) out = out + "  ";

        out += "]";

        return out;
    }

    public String exportChildren(){
        return exportChildren(0);
    }

    public String printChildren(){
        return exportChildren();
    }

    public static GuiNode nodeFromString(String serialisedNode){
        String nodeString = serialisedNode
                .substring(serialisedNode.indexOf("[")+1)
                .replaceAll("\\s", "");

        nodeString = nodeString.substring(0, nodeString.lastIndexOf("]"));

        String name = nodeString;
        if (name.contains("[")) {
            name = nodeString.substring(0, nodeString.indexOf("["));
            nodeString = nodeString.substring(nodeString.indexOf("["));
        } else {
            nodeString = "";
        }
        GuiNode gn = new GuiNode(name);

        if (nodeString.length() > 0){
            int index = 0;

            ArrayList<String> children = new ArrayList<>();

            int lastChild = 0;

            for (int i = 0; i < nodeString.length(); i++){
                if (nodeString.substring(i, i+1).equals("[")){
                    index++;
                } else if (nodeString.substring(i, i+1).equals("]")){
                    index--;

                    if (index == 0){
                        children.add(nodeString.substring(lastChild, i+1));
                        lastChild = i+1;
                    }

                }
            }

            if (index != 0){
                throw new IllegalArgumentException("Invalid Tree to parse! " +
                        (index > 0?  "Missing a bracket.":"Too many brackets."));
            }

            for (String s : children){
                gn.addChild(nodeFromString(s));
            }

        }

        return gn;
    }


    public String printChildrenJson() {
        String out = "";


        out += "{\"name\":\"" + getName() + "\"";

        if (children.size() > 0){
            out = out + ",\"values\":[";
        }

        for (GuiNode c : children){
            out += "\n" + c.printChildrenJson();
        }

        if (children.size() > 0){
            out = out.substring(0, out.length()-1);
            out = out + "]";
        }

        out += "},";

        return out;
    }
}
