package com.thomasdeanwhite.swingripper.guitree;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GuiNodeTest {

    @Test
    public void testDeserialise(){

        String tree = "[a" +
                "\n  [b" +
                "\n    [c" +
                "\n    ]" +
                "\n  ]" +
                "\n]";

        GuiTree cn = new GuiTree(tree);

        assertEquals(tree, cn.getRoot().exportChildren());

    }

    @Test
    public void testDeserialiseMultipleChildren(){

        String tree = "[a" +
                "\n  [b" +
                "\n    [c" +
                "\n    ]" +
                "\n    [d" +
                "\n    ]" +
                "\n  ]" +
                "\n]";

        GuiTree cn = new GuiTree(tree);

        assertEquals(tree, cn.getRoot().exportChildren());

    }

    @Test
    public void testDeserialiseMultipleChildrenCount(){

        String tree = "[a" +
                "\n  [b" +
                "\n    [c" +
                "\n    ]" +
                "\n    [d" +
                "\n    ]" +
                "\n  ]" +
                "\n]";

        GuiTree cn = new GuiTree(tree);

        assertEquals(2, cn.getRoot().getChildren().get(0).getChildrenCount());

    }

    @Test
    public void testGuiTree(){
        String tree = "[[tabs[togglebutton[button][combobox]][button[togglebutton][button][button][togglebutton]][label[togglebutton][button][combobox][tabs]]][togglebutton[list[combobox]]][list][list[10[togglebutton][togglebutton][tabs][combobox]][list[tree][tabs][tabs][combobox]]][label][combobox[10][combobox[list][togglebutton]][label[textfield][tree]][button[togglebutton][button]]]]";

        GuiTree cn = new GuiTree(tree);

        System.out.println(cn.toJson());
    }
}
