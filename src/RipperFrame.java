import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class RipperFrame extends JFrame {

    public static final String TITLE = "SwingRipper - v0.0.1";

    public static HashMap<Integer, Component> components;

    DefaultMutableTreeNode root = new DefaultMutableTreeNode("SwingRipper");


    JTree frames = new JTree(root);

    JButton refresh = new JButton("Refresh");

    JButton paint = new JButton("Paint");

    JButton rip = new JButton("Rip");

    public RipperFrame(){
        super(TITLE);
        setLayout(new FlowLayout());
        JScrollPane scroll = new JScrollPane(frames);
        scroll.setPreferredSize(new Dimension(512, 512));
        add(scroll);

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
                RipperFrame.this.rip();
            }
        });

        add(refresh);

        add(paint);

        add(rip);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        setSize(768, 768);

        setVisible(true);
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


    public void rip(){
        String selected = getSelected().toString();
        Window[] windows = Window.getWindows();
        for (Window w : windows) {
            JFrame frame = (JFrame)SwingUtilities.getRoot(w);

            if (w.isVisible() && frame.getTitle().equals(selected)) {
                // rip the selected gui
                frame.toFront();

                frame.repaint();

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
