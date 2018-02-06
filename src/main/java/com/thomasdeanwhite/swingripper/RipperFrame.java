package com.thomasdeanwhite.swingripper;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.sql.*;

public class RipperFrame extends JFrame {

    public static final String TITLE = "com.thomasdeanwhite.swingripper.SwingRipper - v0.0.1";

    public static String DEFAULT_DATA_DIR = "~/data/";

    public static HashMap<Integer, Component> components;

    DefaultMutableTreeNode root = new DefaultMutableTreeNode("com.thomasdeanwhite.swingripper.SwingRipper");


    JTree frames = new JTree(root);

    JButton refresh = new JButton("Refresh");

    JButton paint = new JButton("Paint");

    JButton rip = new JButton("Rip");

    JTextField dataLocation = new JTextField();

    public RipperFrame(){
        super(TITLE);
        setLayout(new BorderLayout());


        JPanel locationContainer = new JPanel();

        locationContainer.add(new JLabel("Data location:"));

        dataLocation.setPreferredSize(new Dimension(500, 30));
        locationContainer.add(dataLocation);

        add(locationContainer, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(frames);
        scroll.setPreferredSize(new Dimension(512, 512));
        add(scroll, BorderLayout.CENTER);

        setDefaultDataDir(DEFAULT_DATA_DIR);


        components = new HashMap<>();

        frames.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RipperFrame.this.updateWindows();
            }
        });

        paint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RipperFrame.this.highlightComponent();
            }
        });

        rip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    RipperFrame.this.rip();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JPanel actionContainer = new JPanel();

        actionContainer.add(refresh);

        actionContainer.add(paint);

        actionContainer.add(rip);

        add(actionContainer, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        setSize(768, 768);

        setVisible(true);
    }

    public void setDefaultDataDir(String dir){
        if (!dir.endsWith("/")){
            dir += "/";
        }
        dataLocation.setText(dir);
    }

    public DefaultMutableTreeNode getSelected(){
        DefaultMutableTreeNode selected = (DefaultMutableTreeNode) frames.getLastSelectedPathComponent();

        return selected;
    }

    public void highlightComponent(){
        String selected = getSelected().toString();

        int componentNum = Integer.parseInt(selected.split(" - ")[0]);

        Component c = components.get(componentNum);

        c.setBackground(Color.RED);

        c.invalidate();

    }


    public void rip() throws SQLException{
        Connection con = DriverManager.getConnection(
                    "jdbc:mysql://" + Preferences.LOCATION + ":" + Preferences.PORT + "/" + Preferences.DB,
                    Preferences.USER,Preferences.PASSWORD);

        String selected = getSelected().toString();
        Window[] windows = Window.getWindows();
        for (Window w : windows) {
            JFrame frame = (JFrame)SwingUtilities.getRoot(w);

            if (w.isVisible() && frame.getTitle().equals(selected)) {
                // rip the selected gui
                frame.toFront();

                frame.repaint();

                try {
                    Robot robot = new Robot();

                    Point pos = frame.getLocationOnScreen();

                    int[] positions = new int[]{pos.x, pos.y, frame.getWidth(), frame.getHeight()};

                    BufferedImage screenshot = robot.createScreenCapture(new Rectangle(positions[0], positions[1],
                            positions[2], positions[3]));

                    String source = "localhost?application=" + frame.getTitle().replace(" ", "_");

                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery(String.format("SELECT * FROM images WHERE Source='%s'", "lol"));

                    boolean exists = false;

                    while(rs.next()){
                        exists = true;
                    }

                    String dataset = "train";

                    float num = (float)Math.random();

                    if (num < 0.15){
                        dataset = "train";
                    } else if (num < 0.05) {
                        dataset = "validate";
                    }

                    if (!exists){

                        int maxImage = 0;

                        rs=stmt.executeQuery(String.format("SELECT * FROM images ORDER BY ImageID DESC"));

                        maxImage = rs.getInt(0);

                        String filename = "images/" + (maxImage+1) + ".png";

                        rs=stmt.executeQuery(String.format("INSERT INTO images (Subset, File, Width, Height, Source) " +
                                "VALUES ('%s', '%s', '%d', '%d', '%s');", dataset, filename,
                                screenshot.getWidth(), screenshot.getHeight(), source));

                        String output = dataLocation.getText();

                        if (!output.endsWith("/")){
                            output += "/";
                        }

                        output += filename;

                        ImageIO.write(screenshot, "png", new File(output));
                    }
                } catch (AWTException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return;
            }
        }
        System.err.println("Cannot find window " + selected + ". Is a top level window selected?");

    }

    public void refresh(){
        invalidate();
        repaint();
    }

    private int componentCount = 0;

    public void recursiveSearch(Component c, DefaultMutableTreeNode node){
        DefaultMutableTreeNode con = new DefaultMutableTreeNode(componentCount + " - " + c.getClass().toString());
        node.add(con);

        components.put(componentCount, c);

        componentCount++;


        if(c instanceof Container) {
            Container conts = ((Container) c);

            for (Component cont : conts.getComponents()) {
                recursiveSearch(cont, con);
            }
        }
    }

    public void updateWindows(){
        Window[] windows = Window.getWindows();

        componentCount = 0;
        components.clear();

        for (Window w : windows){
            if (w.isVisible()){
                // parse window
                JFrame frame = (JFrame)SwingUtilities.getRoot(w);

                if (!frame.getTitle().equals(TITLE)) {
                    DefaultMutableTreeNode window = new DefaultMutableTreeNode(frame.getTitle());
                    root.add(window);

                    for (Component c : frame.getComponents()){

                        recursiveSearch(c, window);
                    }

                }
            }
        }

        refresh();
    }
}
