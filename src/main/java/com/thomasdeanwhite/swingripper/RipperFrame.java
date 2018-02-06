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

    private static HashMap<Class, String> CLASS_MAP = new HashMap<>();

    static {
        CLASS_MAP.put(JButton.class, "button");
        CLASS_MAP.put(JTextField.class, "text_field");
        CLASS_MAP.put(JTextArea.class, "text_field");
        CLASS_MAP.put(JComboBox.class, "combo_box");
        CLASS_MAP.put(JToggleButton.class, "toggle_button");
        CLASS_MAP.put(JMenu.class, "menu");
        CLASS_MAP.put(JMenuItem.class, "menu_item");
        CLASS_MAP.put(JList.class, "list");
        CLASS_MAP.put(JTree.class, "tree");

        try {
            Class scrollbarClass = Class.forName("javax.swing.JScrollPane$ScrollBar");
            CLASS_MAP.put(scrollbarClass, "scroll_bar");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static String DEFAULT_DATA_DIR = "~/data/";

    public static HashMap<Integer, Component> components;

    DefaultMutableTreeNode root = new DefaultMutableTreeNode("com.thomasdeanwhite.swingripper.SwingRipper");


    JTree frames = new JTree(root);

    JButton refresh = new JButton("Refresh");

    JButton paint = new JButton("Paint");

    JButton rip = new JButton("Rip");

    JLabel status = new JLabel("Ready");

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
                status.setText("Updating...");
                refresh();
                RipperFrame.this.updateWindows();
                status.setText("Updating... Done!");
                refresh();
            }
        });

        paint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.setText("Painting...");
                refresh();
                RipperFrame.this.highlightComponent();
                status.setText("Painting... Done!");
                refresh();
            }
        });

        rip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    status.setText("Ripping...");
                    refresh();
                    RipperFrame.this.rip();
                    status.setText("Ripping... Done!");
                    refresh();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JPanel actionContainer = new JPanel();

        actionContainer.add(refresh);

        actionContainer.add(paint);

        actionContainer.add(rip);

        actionContainer.add(status);

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

    public void recursiveParse(Component c, int[] positions, int imageId, Statement stmt) throws SQLException {

        if(c instanceof Container) {
            Container conts = ((Container) c);

            for (Component cont : conts.getComponents()) {
                try {
                    recursiveParse(cont, positions, imageId, stmt);
                } catch (IllegalComponentStateException e){
                    //component is invisible!
                }
            }
        }

        if (c.getWidth() == 0 || c.getHeight() == 0 || !c.isVisible() || !c.isDisplayable() ||
                !c.isEnabled()){
            return;
        }

        String labelType = null;

        for (Class cls : CLASS_MAP.keySet()){
            if (cls.isInstance(c)){
                labelType = CLASS_MAP.get(cls);
            }
        }

        if (labelType == null){
            return;
        }

        if (c instanceof JTextField){
            if (!((JTextField)c).isEditable()){
                return;
            }
        }

        float[] dimensions = new float[]{
                (c.getLocationOnScreen().x-positions[0]-4)/(float)positions[2],
                (c.getLocationOnScreen().y-positions[1]-4)/(float)positions[3],
                ((c.getLocationOnScreen().x-positions[0]+8) + c.getWidth())/(float)positions[2],
                ((c.getLocationOnScreen().y-positions[1]+8) + c.getHeight())/(float)positions[3],
        };

        ResultSet rs=stmt.executeQuery(String.format("SELECT LabelTypeId FROM label_types WHERE LabelName='%s'", labelType));

        int labelTypeId = 0;

        if (rs.next()){
            labelTypeId = rs.getInt(1);
        } else {
            labelTypeId = stmt.executeUpdate(String.format("INSERT INTO label_types (LabelName) VALUES ('%s');", labelType));

            rs=stmt.executeQuery(String.format("SELECT LabelTypeId FROM label_types WHERE LabelName='%s'", labelType));

            if (rs.next()) {
                labelTypeId = rs.getInt(1);
            }
        }

        stmt.executeUpdate(String.format("INSERT INTO labels (Source, Confidence, ImageId, " +
                        "XMin, XMax, YMin, YMax, LabelType) VALUES ('%s', '%d', '%d', '%f', '%f', '%f', '%f'," +
                        "'%d');", "unverified", 0, imageId, dimensions[0], dimensions[2], dimensions[1],
                dimensions[3], labelTypeId));

    }


    public void rip() throws SQLException {
        Connection con = DriverManager.getConnection(
                    "jdbc:mysql://" + Preferences.LOCATION + ":" + Preferences.PORT + "/" + Preferences.DB,
                    Preferences.USER, Preferences.PASSWORD);

        String selected = getSelected().toString();
        Window[] windows = Window.getWindows();
        for (Window w : windows) {
            String title = "";

            // parse window
            Container cont = (Container)SwingUtilities.getRoot(w);

            if (cont instanceof JFrame){
                title = ((JFrame)cont).getTitle();
            } else if (cont instanceof JOptionPane){
                title = ((JOptionPane)cont).getMessage().toString();
            } else if (cont instanceof JDialog){
                title = ((JDialog)cont).getTitle();
            }

            title = SwingRipper.JAR_FILE + " - " + title;

            if (w.isVisible() && title.equals(selected)) {

                cont.repaint();

                try {
                    Robot robot = new Robot();

                    Point pos = cont.getLocationOnScreen();

                    int[] positions = new int[]{pos.x, pos.y, cont.getWidth(), cont.getHeight()};

                    BufferedImage screenshot = robot.createScreenCapture(new Rectangle(positions[0], positions[1],
                            positions[2], positions[3]));

                    String source = "localhost?application=" + title.replace(" ", "_").
                            replace(".", "");

                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery(String.format("SELECT * FROM images WHERE Source='%s'", source));

                    boolean exists = false;

                    while(rs.next()){
                        exists = true;
                    }

                    String dataset = "train";

                    float num = (float)Math.random();

                    if (num < 0.1){
                        dataset = "train";
                    } else if (num < 0.15) {
                        dataset = "validate";
                    }

                    if (!exists){

                        int maxImage = 0;

                        rs=stmt.executeQuery(String.format("SELECT * FROM images ORDER BY ImageID DESC;"));

                        rs.next();

                        maxImage = rs.getInt(1);

                        int imageId = (maxImage+1);

                        String filename = "images/" + imageId + ".png";

                        stmt.executeUpdate(String.format("INSERT INTO images (Subset, File, Width, Height, Source) " +
                                "VALUES ('%s', '%s', '%d', '%d', '%s');", dataset, filename,
                                screenshot.getWidth(), screenshot.getHeight(), source));


                        String output = dataLocation.getText();

                        if (!output.endsWith("/")){
                            output += "/";
                        }

                        output += filename;

                        ImageIO.write(screenshot, "png", new File(output));


                        for (Component c : cont.getComponents()){

                            recursiveParse(c, positions, imageId, stmt);
                        }
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

                String title = "";

                // parse window
                Container cont = (Container)SwingUtilities.getRoot(w);

                if (cont instanceof JFrame){
                    title = ((JFrame)cont).getTitle();
                } else if (cont instanceof JOptionPane){
                    title = ((JOptionPane)cont).getMessage().toString();
                } else if (cont instanceof JDialog){
                    title = ((JDialog)cont).getTitle();
                }

                if (!title.equals(TITLE)) {
                    DefaultMutableTreeNode window = new DefaultMutableTreeNode(SwingRipper.JAR_FILE + " - " + title);
                    root.add(window);

                    for (Component c : cont.getComponents()){

                        recursiveSearch(c, window);
                    }

                }
            }
        }
    }
}
