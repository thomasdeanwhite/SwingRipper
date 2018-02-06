package com.thomasdeanwhite.swingripper;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class SwingRipper {

    public static String JAR_FILE = "";

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Args should be of length 1.");
        }
        String jar = args[0];

        File path = new File(jar);

        JAR_FILE = jar.substring(jar.lastIndexOf("/")+1);

        if (!path.exists()) {
            throw new IllegalArgumentException("args[0] should be a valid jar file");
        }

        JarFile jarfile = null;
        Manifest manifest = null;
        try {
            jarfile = new JarFile(jar);
            manifest = jarfile.getManifest();
        } catch (IOException e) {
            throw new IllegalArgumentException("args[0] should be a valid jar file");

        }

        Attributes attrs = manifest.getMainAttributes();
        String mainClass = attrs.getValue("Main-Class");

        URLClassLoader child = null;
        try {
            URL url = new URL("file:///" + path.getAbsolutePath());
            child = new URLClassLoader(new URL[]{url}, SwingRipper.class.getClassLoader());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        System.out.println("Executing main method from class " + mainClass);

        try {
            Class classToLoad = Class.forName(mainClass, true, child);
            final Method method = classToLoad.getMethod("main", String[].class);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Object result = method.invoke(null, new Object[]{new String[]{}});

                        System.out.println("Method returned with exit status: " + result);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });

            t.start();

            ripperFrame = new RipperFrame();

            if (args.length >= 2){
                ripperFrame.setDefaultDataDir(args[1]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String input = "";

        Scanner scanner = new Scanner(System.in);

        while (!(input=scanner.nextLine()).equals("q")){
            try {
                String[] command = input.split(" ");

                String cmd = command[0];

                String param = "";

                if (command.length > 1) {
                    param = command[1];
                }

                switch (cmd) {
                    case "refresh":
                        ripperFrame.updateWindows();
                        break;
                    case "rip":
                        try {
                            ripperFrame.rip();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "select":
                        ripperFrame.frames.getSelectionModel().clearSelection();
                        try {
                            int row = Integer.parseInt(param);
                            ripperFrame.frames.addSelectionRow(row);
                            ripperFrame.frames.expandRow(row);
                        } catch (NumberFormatException e) {
                            System.err.println(e.getMessage());
                        }
                }

                ripperFrame.refresh();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static RipperFrame ripperFrame;




}
