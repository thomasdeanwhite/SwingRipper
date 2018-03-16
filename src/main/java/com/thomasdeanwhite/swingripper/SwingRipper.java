package com.thomasdeanwhite.swingripper;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.SQLException;
import java.util.ArrayList;
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

        String classPath = attrs.getValue("Class-Path");

        URLClassLoader child = null;
        try {

            System.out.println("Using jar: " + path.getAbsolutePath());

            URL url = new URL("file:///" + path.getAbsolutePath().replace(" ", "%20"));

            ArrayList<URL> urls = new ArrayList<>();

            if (classPath != null && classPath.length() > 0) {

                System.out.print("Enhancing Class-Path: ");
                String[] jars = classPath.split(" ");

                for (String s : jars) {
                    File f = new File(s.replace("./", ""));
                    System.out.println(f.getAbsolutePath());
                    URL jarUrl = new URL("file:///" +  f.getAbsolutePath().replace(" ", "%20"));
                    urls.add(jarUrl);
                }

                System.out.println();
            }

            urls.add(url);

            URL[] urlArray = new URL[urls.size()];

            urls.toArray(urlArray);

            child = new URLClassLoader(urlArray, SwingRipper.class.getClassLoader());
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
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {

                            Process p = new ProcessBuilder("java", "-jar " + jar).start();

                            String line = null;

                            BufferedReader input =
                                    new BufferedReader
                                            (new InputStreamReader(p.getInputStream()));
                            while ((line = input.readLine()) != null) {
                                System.out.println(line);
                            }
                            input.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
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
                            if (param.length() > 0) {
                                ripperFrame.rip(param);
                            } else {
                                ripperFrame.rip();
                            }
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
                        break;
                    default:
                        System.err.println("Unrecognised command: " + cmd);
                }

                ripperFrame.refresh();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static RipperFrame ripperFrame;




}
