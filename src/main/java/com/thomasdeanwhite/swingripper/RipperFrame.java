package com.thomasdeanwhite.swingripper;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.sql.*;
import java.util.Properties;

public class RipperFrame extends JFrame {

    public static final String TITLE = "com.thomasdeanwhite.swingripper.SwingRipper - v0.0.1";

    private static HashMap<Class, String> CLASS_MAP = new HashMap<>();

    static {
        CLASS_MAP.put(JButton.class, "button");
        CLASS_MAP.put(JTextField.class, "text_field");
        CLASS_MAP.put(JTextArea.class, "text_field");
        CLASS_MAP.put(JComboBox.class, "combo_box");
        CLASS_MAP.put(JToggleButton.class, "toggle_button");
        CLASS_MAP.put(JMenuBar.class, "menu");
        CLASS_MAP.put(JMenuItem.class, "menu_item");
        CLASS_MAP.put(JList.class, "list");
        CLASS_MAP.put(JTree.class, "tree");
        CLASS_MAP.put(JSlider.class, "slider");

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

    DefaultTreeModel tree = new DefaultTreeModel(root);

    JTree frames = new JTree(tree);

    JButton refresh = new JButton("Refresh");

    JButton paint = new JButton("Paint");

    JButton rip = new JButton("Rip");

    JButton walk = new JButton("Walk");

    JLabel status = new JLabel("Ready");

    JTextField dataLocation = new JTextField();

    public RipperFrame() {
        super(TITLE);
        setLayout(new BorderLayout());


        JPanel locationContainer = new JPanel();

        locationContainer.add(new JLabel("Data location:"));

        dataLocation.setPreferredSize(new Dimension(500, 30));

        dataLocation.setText("/home/thomas/work/GuiImages/public");

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

        walk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    status.setText("Walking...");
                    refresh();
                    RipperFrame.this.rip();
                    status.setText("Walking... Done!");
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

    public void setDefaultDataDir(String dir) {
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        dataLocation.setText(dir);
    }

    public DefaultMutableTreeNode getSelected() {
        DefaultMutableTreeNode selected = (DefaultMutableTreeNode) frames.getLastSelectedPathComponent();

        return selected;
    }

    public void highlightComponent() {
        String selected = getSelected().toString();

        int componentNum = Integer.parseInt(selected.split(" - ")[0]);

        Component c = components.get(componentNum);

        c.setBackground(Color.RED);

        c.invalidate();

    }

    public void recursiveParse(Component c, int[] positions, int imageId, Statement stmt) throws SQLException {
        if (!c.isVisible() || c.getLocation().x >= c.getParent().getWidth() || c.getLocation().y > c.getParent().getHeight()){
            return;
        }
        if (c instanceof Container) {
            if (c instanceof JTabbedPane) {
                JTabbedPane pane = (JTabbedPane) c;
                int count = pane.getTabCount();
                for (int i = 0; i < count; i++) {
                    Rectangle bounds = pane.getBoundsAt(i);
                    Rectangle r = new Rectangle(bounds.x + pane.getLocationOnScreen().x - 4,
                            bounds.y + pane.getLocationOnScreen().y - 4,
                            bounds.width + 8, bounds.height + 8);

                    float[] dimensions = new float[]{
                            (r.getLocation().x - positions[0] - 4) / (float) positions[2],
                            (r.getLocation().y - positions[1] - 4) / (float) positions[3],
                            (float) (((r.getLocation().x - positions[0] + 8) + r.getWidth()) / (float) positions[2]),
                            (float) (((r.getLocation().y - positions[1] + 8) + r.getHeight()) / (float) positions[3]),
                    };

                    String labelType = "tabs";

                    ResultSet rs = stmt.executeQuery(String.format("SELECT LabelTypeId FROM label_types WHERE LabelName='%s'", labelType));

                    int labelTypeId = 0;

                    if (rs.next()) {
                        labelTypeId = rs.getInt(1);
                    } else {
                        labelTypeId = stmt.executeUpdate(String.format("INSERT INTO label_types (LabelName) VALUES ('%s');", labelType));

                        rs = stmt.executeQuery(String.format("SELECT LabelTypeId FROM label_types WHERE LabelName='%s'", labelType));

                        if (rs.next()) {
                            labelTypeId = rs.getInt(1);
                        }
                    }

                    String query = String.format("INSERT INTO labels (Source, Confidence, ImageId, " +
                                    "XMin, XMax, YMin, YMax, LabelType) VALUES ('%s', '%d', '%d', '%f', '%f', '%f', '%f'," +
                                    "'%d');", "unverified", 0, imageId, dimensions[0], dimensions[2], dimensions[1],
                            dimensions[3], labelTypeId);

                    stmt.executeUpdate(query);

                }
            }
            Container conts = ((Container) c);

            for (Component cont : conts.getComponents()) {
                try {
                    recursiveParse(cont, positions, imageId, stmt);
                } catch (IllegalComponentStateException e) {
                    //component is invisible!
                }
            }
        }

        if (c.getWidth() == 0 || c.getHeight() == 0 || !c.isVisible() || !c.isDisplayable() ||
                !c.isEnabled()) {
            return;
        }

        String labelType = null;

        for (Class cls : CLASS_MAP.keySet()) {
            if (cls.isInstance(c)) {
                labelType = CLASS_MAP.get(cls);
            }
        }

        if (labelType == null) {
            return;
        }

        if (c instanceof JTextField) {
            if (!((JTextField) c).isEditable()) {
                return;
            }
        }

        int x = c.getLocationOnScreen().x;
        int y = c.getLocationOnScreen().y;

        int width = c.getWidth();
        int height = c.getHeight();

        if (width > c.getParent().getWidth()){
            width = c.getParent().getWidth();
        }

        if (height > c.getParent().getHeight()){
            height = c.getParent().getHeight();
        }

        float[] dimensions = new float[]{
                (x - positions[0]) / (float) positions[2],
                (y - positions[1]) / (float) positions[3],
                ((x - positions[0]) + width) / (float) positions[2],
                ((y - positions[1]) + height) / (float) positions[3],
        };

        ResultSet rs = stmt.executeQuery(String.format("SELECT LabelTypeId FROM label_types WHERE LabelName='%s'", labelType));

        int labelTypeId = 0;

        if (rs.next()) {
            labelTypeId = rs.getInt(1);
        } else {
            labelTypeId = stmt.executeUpdate(String.format("INSERT INTO label_types (LabelName) VALUES ('%s');", labelType));

            rs = stmt.executeQuery(String.format("SELECT LabelTypeId FROM label_types WHERE LabelName='%s'", labelType));

            if (rs.next()) {
                labelTypeId = rs.getInt(1);
            }
        }

        String query = String.format("INSERT INTO labels (Source, Confidence, ImageId, " +
                        "XMin, XMax, YMin, YMax, LabelType) VALUES ('%s', '%d', '%d', '%f', '%f', '%f', '%f'," +
                        "'%d');", "unverified", 0, imageId, dimensions[0], dimensions[2], dimensions[1],
                dimensions[3], labelTypeId);

        stmt.executeUpdate(query);

    }


    public void rip() throws SQLException {
        rip("");
    }

    public void rip(String extension) throws SQLException {
        String selected = getSelected().toString();
        Window[] windows = Window.getWindows();
        for (Window w : windows) {
            String title = "";

            // parse window
            Container cont = (Container) SwingUtilities.getRoot(w);

            if (cont instanceof JFrame) {
                title = ((JFrame) cont).getTitle();
            } else if (cont instanceof JOptionPane) {
                title = ((JOptionPane) cont).getMessage().toString();
            } else if (cont instanceof JDialog) {
                title = ((JDialog) cont).getTitle();
            }

            title = SwingRipper.JAR_FILE + " - " + title;

            if (w.isVisible() && title.equals(selected)) {
                rip(extension, cont, "swing", "application=");
            }

        }
    }

    public void rip(String extension, Container cont, String dataSource, String fileLocation) throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://" + Preferences.LOCATION + ":" + Preferences.PORT + "/" + Preferences.DB,
                Preferences.USER, Preferences.PASSWORD);
        rip(extension, cont, dataSource, fileLocation, con);
        con.close();
    }

    public void rip(String extension, Container cont, String dataSource, String fileLocation, Connection con) throws SQLException {
        rip(extension, cont, dataSource, fileLocation, con, "default");
    }

    public void rip(String extension, Container cont, String dataSource, String fileLocation, Connection con, String theme) throws SQLException {
        String title = "";

        if (cont instanceof JFrame) {
            title = ((JFrame) cont).getTitle();
        } else if (cont instanceof JOptionPane) {
            title = ((JOptionPane) cont).getMessage().toString();
        } else if (cont instanceof JDialog) {
            title = ((JDialog) cont).getTitle();
        }

        title = SwingRipper.JAR_FILE + " - " + title;

        cont.repaint();

        try {
            Robot robot = new Robot();

            Point pos = cont.getLocationOnScreen();

            int[] positions = new int[]{pos.x, pos.y, cont.getWidth(), cont.getHeight()};

            BufferedImage screenshot = robot.createScreenCapture(new Rectangle(positions[0], positions[1],
                    positions[2], positions[3]));

            String property = System.getProperty("swing.defaultlaf");

            if (property != null && property.length() > 0) {
                extension += "-" + property;
            }

            if (extension.length() > 0 && !(extension.startsWith("-"))) {
                extension = "-" + extension;
            }

            String source = "localhost?" + fileLocation + title.replace(" ", "_").
                    replace(".", "").replace("'", ".apos.") + extension;

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM images WHERE Source='%s'", source));

            boolean exists = false;

            while (rs.next()) {
                exists = true;
            }

            String dataset = getRandomDataset();



            if (!exists) {

                int maxImage = 0;

                rs = stmt.executeQuery(String.format("SELECT * FROM images ORDER BY ImageID DESC;"));

                rs.next();

                try {
                    maxImage = rs.getInt(1);
                } catch (SQLException e){
                    System.out.println(e.getMessage());
                    // no rows in table
                    maxImage = 0;
                }
                int imageId = (maxImage + 1);

                String filename = "images/" + imageId + ".png";

                stmt.executeUpdate(String.format("INSERT INTO images (Subset, File, Width, Height, Source, Dataset, Theme) " +
                                "VALUES ('%s', '%s', '%d', '%d', '%s', '%s', '%s');", dataset, filename,
                        screenshot.getWidth(), screenshot.getHeight(), source, dataSource, theme));


                String output = dataLocation.getText();

                if (!output.endsWith("/")) {
                    output += "/";
                }

                output += filename;

                ImageIO.write(screenshot, "png", new File(output));


                for (Component c : cont.getComponents()) {
                    recursiveParse(c, positions, imageId, stmt);
                }
            }
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRandomDataset(){

        String dataset = "train";

        float num = (float) Math.random();

        if (num < 0.1) {
            dataset = "test";
        } else if (num < 0.2) {
            dataset = "validate";
        }
        return dataset;
    }

    public void refresh() {
        invalidate();
        repaint();
    }

    private int componentCount = 0;

    public void recursiveSearch(Component c, DefaultMutableTreeNode node) {
        DefaultMutableTreeNode con = new DefaultMutableTreeNode(componentCount + " - " + c.getClass().toString());
        node.add(con);

        components.put(componentCount, c);

        componentCount++;


        if (c instanceof Container) {
            Container conts = ((Container) c);

            if (c instanceof JTabbedPane) {
                JTabbedPane pane = (JTabbedPane) c;
                int count = pane.getTabCount();
                for (int i = 0; i < count; i++) {
                    String tab = pane.getTitleAt(i);
                    Rectangle bounds = pane.getBoundsAt(i);
                    bounds = new Rectangle(bounds.x + pane.getLocationOnScreen().x,
                            bounds.y + pane.getLocationOnScreen().y,
                            bounds.width, bounds.height);
                }
            }

            for (Component cont : conts.getComponents()) {
                recursiveSearch(cont, con);
            }
        }
    }

    public void updateWindows() {
        Window[] windows = Window.getWindows();

        componentCount = 0;
        components.clear();

        root.removeAllChildren();

        for (Window w : windows) {
            if (w.isVisible()) {

                String title = "";

                // parse window
                Container cont = (Container) SwingUtilities.getRoot(w);

                if (cont instanceof JFrame) {
                    title = ((JFrame) cont).getTitle();
                } else if (cont instanceof JOptionPane) {
                    title = ((JOptionPane) cont).getMessage().toString();
                } else if (cont instanceof JDialog) {
                    title = ((JDialog) cont).getTitle();
                }

                if (!title.equals(TITLE)) {
                    DefaultMutableTreeNode window = new DefaultMutableTreeNode(SwingRipper.JAR_FILE + " - " + title);
                    root.add(window);


                    for (Component c : cont.getComponents()) {

                        recursiveSearch(c, window);
                    }

                }
            }
        }
        tree.reload();
        frames.collapseRow(0);
    }
}
