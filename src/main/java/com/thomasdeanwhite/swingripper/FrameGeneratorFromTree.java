package com.thomasdeanwhite.swingripper;

import com.thomasdeanwhite.swingripper.guitree.GuiNode;
import com.thomasdeanwhite.swingripper.guitree.GuiTree;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class FrameGeneratorFromTree {

    private static String THEME = "";

    private enum Layout {
        FLOW, GRID, BORDER, CARD, GRID_BAG;
    }

    private static final float[] PROBABILITIES = new float[]{
            0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f
    };


//    private static final float[] PROBABILITIES = new float[]{
//            0.429f, 0.134f, 0.058f, 0.043f, 0.048f, 0.16f, 0.032f, 0.038f, 0.058f
//    };

    private final int MAX_WIDGETS_X = 5;
    private final int MAX_WIDGETS_Y = 5;

    private static ArrayList<BufferedImage> icons = new ArrayList<BufferedImage>();


    private int widgetsX;
    private int widgetsY;

    private static String[] RANDOM_WORDS = new String[]{
            "knowledgeable", "possessive", "business", "ajar", "nose", "awake", "mouth", "help", "verdant", "unsightly", "digestion", "type", "enjoy", "whip", "obsequious", "heap", "beautiful", "hateful", "beg", "flash", "drown", "scarce", "fine", "tickle", "nonchalant", "vague", "loutish", "fabulous", "five", "fearless", "delicate", "unfasten", "opposite", "bad", "wasteful", "practise", "recess", "axiomatic", "shoes", "yell", "kick", "next", "chew", "liquid", "bashful", "bedroom", "superb", "divergent", "huge", "doctor", "moan", "fortunate", "bag", "company", "cake", "voiceless",
            "copy", "visitor", "ladybug", "serve", "effect", "fax", "ethereal", "periodic", "lunchroom", "joke", "coach", "star", "grey", "empty", "clammy", "teaching", "laugh", "unwieldy", "form", "influence", "retire", "groovy", "colorful", "temper", "hook", "reading", "bump", "melted", "wren", "roll", "mask", "tiresome", "alarm", "throne", "trust", "voyage", "overrated", "jar", "common", "language", "prevent", "grab", "violent", "handle", "flowery", "crook", "purple", "advertisement", "machine", "advise", "decorate", "hour", "unused", "regret", "use", "cup", "roomy", "suggest", "irritate", "sturdy", "nut", "window", "juicy", "mature", "position", "organic", "leather", "splendid", "rub", "boil", "gruesome", "hug", "pickle", "thread", "nail", "sneeze", "sordid", "abrasive", "geese", "bewildered", "abaft", "imagine", "nimble", "frog", "grumpy", "ants", "front", "serious", "growth", "paint", "art", "quixotic", "car", "adaptable", "iron", "error", "slip", "rabbit", "decorous", "yellow", "versed", "magical", "scattered", "branch", "found", "half", "squash", "volatile", "injure", "lethal", "exercise", "increase", "parcel", "finger", "spotted", "zipper", "embarrassed",
            "afraid", "bent", "breakable", "ill-fated", "receive", "ripe", "care", "dramatic", "cool", "panoramic", "steady", "lucky", "cream", "polish", "look", "fast", "regret", "broad", "welcome", "lively", "club", "horrible", "instinctive", "angle", "rake", "mice", "mindless", "second", "remove", "festive", "form", "fire", "wool", "clip", "loose", "trick", "bleach", "quill", "lean", "limit", "faithful", "jelly", "dance", "long", "empty", "lackadaisical", "snotty", "concern", "unequaled", "creator", "same", "ceaseless", "weight", "hungry", "magic", "square", "fertile", "frighten", "dreary", "craven", "governor", "hammer", "assorted", "ubiquitous", "outgoing", "elite", "alleged", "ludicrous", "snow", "wiggly", "curious", "yielding", "press", "pathetic", "reign", "well-made", "bouncy", "spiffy", "government", "relieved", "busy", "stop", "spectacular",
            "calculate", "tray", "glue", "overflow", "imperfect", "sigh", "country", "color", "tearful", "uttermost", "arrange", "fallacious", "hobbies", "scared", "annoying", "need", "arrogant", "door", "troubled", "loss", "desk", "obeisant", "appliance", "achiever", "rod", "test", "obnoxious", "wall", "muddled", "cobweb", "automatic", "imported", "number", "jittery", "suppose", "determined", "side", "glistening", "advice", "airplane", "hope", "team", "handy", "jail", "separate", "tasteful", "employ", "book", "judicious", "smash", "box", "soggy", "cooperative", "fact", "dysfunctional", "scorch", "super", "hilarious", "exist", "like", "spray", "nappy", "resonant", "secretary", "gaze", "hard", "intelligent", "realise", "modern", "support", "approval", "rare", "reduce", "van", "witty", "eggs", "behave", "flower", "picture", "mate", "precious", "rainy", "domineering", "easy", "basin", "close", "range", "quilt", "spy", "feeling", "windy", "pretty", "sudden", "care", "match", "insurance", "crack", "inform", "vase", "saw", "charge", "bomb", "fat", "move", "activity", "trouble", "clean", "time", "damp", "dazzling", "brave", "harmony", "consist", "time", "key", "certain", "rapid", "chubby", "size", "extra-large", "abiding", "icy", "scold", "call", "substantial", "gusty", "eatable", "need",
            "things", "cut", "moor", "woozy", "rough", "nutritious", "credit", "ski", "boorish", "impress", "develop", "nostalgic", "disastrous", "happy", "identify", "wealth", "glow", "curvy", "encourage", "meat", "test", "curly", "wriggle", "dock", "aromatic", "giants", "unwritten", "class", "kittens", "ground", "sock", "skate", "balance", "tawdry", "right", "rock", "efficient", "orange", "breath", "unkempt", "walk", "baseball", "rural", "reproduce", "elegant", "creature", "snow", "basketball", "spill", "measure", "plastic", "eight", "dapper", "amusement", "jeans", "groan", "kindhearted", "oatmeal", "unable", "yam", "lowly", "fear", "fish", "story", "vast", "wiry", "night", "political", "ear", "mend", "cover", "madly", "living", "taste", "ashamed", "learned", "didactic", "nosy", "shake", "closed", "bored", "attract", "dust", "jumbled", "provide", "record", "hesitant", "apparel", "dad", "tiger", "add", "thunder", "level", "physical", "boundary", "aquatic", "venomous", "needy", "zinc", "cuddly", "lip", "wrench", "amused", "flesh", "pin", "toad", "bulb", "abject", "rich", "fuel", "permit", "painstaking", "continue", "slip", "gaping", "whimsical", "twist", "puzzled", "bite", "edge", "murder", "daffy", "head", "rejoice", "necessary", "scarf", "protect", "earth", "solid", "colour", "snatch",
            "expert", "fearful", "clumsy", "extend", "bake", "internal", "ahead", "underwear", "committee", "pull", "sneeze", "unique", "open", "sparkle", "deceive", "fence", "flavor", "understood", "trucks", "oil", "peace", "pull", "veil", "whip", "pet", "remind", "aggressive", "cannon", "point", "many", "harmonious", "truck", "illegal", "absorbing", "tired", "well-groomed", "stone", "memory", "panicky", "church", "abounding", "aftermath", "malicious", "premium", "plucky", "week", "pour", "discussion", "stretch", "quicksand", "parsimonious", "stretch", "mammoth", "nervous", "heal", "mass", "entertaining", "fool", "numerous", "nutty", "victorious", "frequent", "work", "stage", "goofy", "kindly", "boundless", "gainful", "experience", "muscle", "keen", "lock", "squirrel", "obtain", "pizzas", "cent", "existence", "agreeable", "weary", "apathetic", "swim", "sticks", "educate", "wave", "duck", "lamp", "thankful", "meeting", "tremendous", "icky", "complain", "peel", "guarantee", "combative", "wrap", "screeching", "jaded", "camp", "painful", "twist", "frame", "friends", "recondite", "observe", "hand", "alive", "icicle", "soak", "absurd", "ambitious", "big", "tie", "dust", "store", "flower", "rain", "undesirable", "vest", "glass", "chicken", "wary", "ruddy", "obscene", "servant", "erect", "woebegone", "ready", "encouraging", "bikes", "six", "wander", "hate", "important", "disturbed", "air", "fumbling", "entertain", "husky",
            "penitent", "crow", "avoid", "previous", "yarn", "divide", "seal", "notebook", "sisters", "dynamic", "great", "zebra", "snore", "snakes", "bitter", "collar", "interest", "live", "screw", "jam", "lavish", "man", "argument", "invite", "voracious", "repeat", "scale", "present", "gaudy", "wooden", "representative", "subdued", "exciting", "impolite", "ragged", "follow", "fly", "jobless", "alike", "untidy", "grate", "stroke", "unhealthy", "own", "cloistered", "whistle", "incredible", "part", "tour", "breathe", "hair", "eminent", "apparatus", "hunt", "cow", "surprise", "superficial", "irritating", "near", "fry", "sniff", "squeeze", "haircut", "immense", "dream", "quizzical", "frail", "crack", "supreme", "destroy", "cause", "useful", "polite", "argue", "ossified", "mute", "number", "day", "lovely", "chickens", "tent", "black-and-white", "describe", "dangerous", "rock", "donkey", "mountainous", "aberrant", "cute", "property", "distance", "loaf", "doubtful", "knock", "reaction", "ugly", "smile", "card", "writing", "steadfast", "sail", "bead", "blot", "exultant", "aback", "free", "rampant", "toe", "shiver", "hanging", "town", "fluffy", "robin",
            "whispering", "offbeat", "cold", "face", "pancake", "cactus", "beds", "science", "furtive", "hospital", "dislike", "shock", "mix", "consider", "blink", "juvenile", "stay", "start", "plant", "crash", "nest", "society", "land", "sedate", "root", "majestic", "cheer", "carry", "respect", "wry", "hole", "secretive", "push", "applaud", "warn", "enormous", "fresh", "bed", "shirt", "occur", "wail", "stamp", "moaning", "spoon", "thirsty", "graceful", "pointless", "alluring", "scrape", "burst", "slave", "tongue", "marked", "harass", "righteous", "rely", "accessible", "unlock", "clam", "nine", "toothsome", "rate", "earthquake", "punish", "amount", "terrify", "love", "degree", "remarkable", "wrist", "misty", "mother", "examine", "space", "unit", "protective", "squeal", "milky", "petite", "oranges", "doubt", "cows", "walk", "strip", "unruly", "telephone", "punch", "muddle", "cough", "object", "waves", "boring", "babies", "melt", "elfin", "bumpy", "pastoral", "adventurous", "jealous", "white", "ticket", "overwrought", "quartz", "slippery", "first", "sore", "bustling", "roasted", "cruel", "angry", "listen", "spade", "seat", "addition", "chop", "dizzy", "perfect", "transport", "powerful", "acceptable", "secret", "young", "helpful", "unpack", "different", "trail", "balance", "fold", "abashed", "collect", "deafening", "smart", "grade", "deer", "naive", "can", "crabby", "discover", "cross", "border", "tap", "caring", "winter", "spiky", "horse", "amuck", "hurt", "tidy", "pine", "far", "settle", "sweltering", "mountain", "title", "dare", "industry", "locket", "calculating", "lumber", "haunt", "efficacious", "neck", "nasty", "somber", "prickly", "hill", "end", "relation", "aboriginal", "peep", "thundering", "sugar", "increase", "bury",
            "past", "power", "hushed", "three", "staking", "dress", "tick", "drunk", "sip", "stitch", "drink", "education", "partner", "girl", "bang", "woman", "visit", "suspect", "sack", "high", "uppity", "boast", "moldy", "bow", "simplistic", "conscious", "separate", "sack", "mighty", "reply", "swanky", "clear", "ignorant", "thinkable", "flagrant", "cheap", "reward", "lopsided", "frightened", "film", "cushion", "cat", "waste", "play", "paltry", "cave", "beam", "drain", "appear", "imminent", "rest", "tenuous", "exclusive", "cry", "word", "coast", "zany", "smoke", "slow", "impulse", "electric", "gold", "male", "disagree", "communicate", "comparison", "jump", "ocean", "back", "detect", "deadpan", "frogs", "dusty", "offend", "desert", "trite", "special", "purpose", "magnificent", "hose", "grandfather", "coordinated", "weather", "jewel", "smoggy", "slow", "improve", "glamorous", "voice", "load", "devilish", "religion", "string", "successful", "stamp", "brash", "paste", "whisper", "stranger", "daughter", "old", "smile", "zealous", "rhetorical", "clean", "tart", "diligent", "approve", "humorous", "distinct", "watch", "careful", "low", "detail", "floor", "silk", "skillful", "interrupt", "houses", "cloth", "flippant", "mourn", "squealing", "cast", "ban", "flag", "steep", "psychotic", "flat", "compete",
            "comb", "possible", "dam", "courageous", "replace", "leg", "handsomely", "black", "celery", "brass", "utter", "brake", "functional", "brief", "shade", "act", "sweater", "belong", "person", "trace", "expand", "lively", "grape", "interest", "attack", "doll", "trouble", "change", "milk", "overconfident", "knowledge", "lumpy", "scare", "drip", "momentous", "sable", "cloudy", "sea", "arch", "helpless", "travel", "nerve", "sign", "paddle", "trot", "left", "treatment", "paper", "room", "magenta", "monkey", "stem", "rhythm", "rings", "queue", "invention", "look", "talk", "meaty", "remember", "spiteful", "pause", "threatening", "attack", "used", "dolls", "wet", "shave", "wide", "cap", "run", "funny", "low", "short", "gun", "best", "trashy", "callous", "horn", "sofa", "judge", "damage", "foolish", "bridge", "trap", "volleyball", "sulky", "men", "engine", "poised", "obsolete", "anger", "clever", "willing", "idea", "merciful", "trees", "deranged", "silly", "fear", "pencil", "library", "well-to-do", "thrill", "infamous",
            "river", "plant", "interesting", "motionless", "carve", "tested", "bore", "badge", "tremble", "tin", "complete", "excellent", "spooky", "delirious", "plot", "far-flung", "guard", "berry", "pie", "zoom", "healthy", "name", "knot", "agonizing", "shrug", "neighborly", "contain", "observant", "sponge", "group", "theory", "accidental", "wax", "wish", "slim", "like", "produce", "worry", "glove", "abhorrent", "false", "spring", "grip", "hate", "orange", "last", "synonymous", "juice", "open", "office", "aspiring", "grieving", "abusive", "plant", "sound", "striped", "spell", "zephyr", "rush", "party", "year"
    };

    private static Random random = new Random();

//    static {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (UnsupportedLookAndFeelException e) {
//            e.printStackTrace();
//        }
//    }

    private static int MAX_NUMBER_COMPONENTS = 25;
    private static int MAX_WIDTH = 1024;
    private static int MAX_HEIGHT = 1024;

    private static GridBagConstraints c = new GridBagConstraints();

    private static final int MAX_PADDING = 50;


    private static int MIN_NUM_COMPONENTS = 4;

    private static int MIN_WIDTH = 256;
    private static int MIN_HEIGHT = 256;

    public FrameGeneratorFromTree() {

        File[] iconFiles = new File("png").listFiles();

        for (File f : iconFiles){
            if (f.getName().toLowerCase().endsWith(".png")){
                try {
                    BufferedImage buttonIcon = ImageIO.read(f);
                    icons.add(buttonIcon);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private static Layout LAYOUT = Layout.FLOW;

    private Color textColor = null;
    private Color backgroundColor = null;

    public JComponent generateComponent(int componentType, List<String> arrayList, JFrame jframe, int components){

        JComponent comp = null;

        String value = arrayList.get(components);

        randomizeC();


        switch (componentType) {

            case 0: //button
                JComponent cb = new JPanel();
                cb.setLayout(new GridBagLayout());

                JButton b = null;


                if (random.nextDouble() < 0.8){
                    b = new JButton(value);
                } else {
                    b = new JButton(new ImageIcon(icons.get(random.nextInt(icons.size()))));
                    if (Math.random() < 0.5){
                        b.setBorder(BorderFactory.createEmptyBorder());
                        b.setContentAreaFilled(false);
                    }
                }
                b.setForeground(textColor);

                comp = b;
                cb.add(b, c);
                if (random.nextDouble() < 0.8) {
                    comp = cb;
                }
                break;
            case 1: //text field
                JComponent cte = new JPanel();
                cte.setLayout(new GridBagLayout());
                if (random.nextDouble() < 0.3) {
                    JTextArea jt = new JTextArea(value);
                    jt.setForeground(textColor);
                    jt.setMargin(new Insets(10, 10, 10, 10));

                    double words = random.nextInt(8);

                    String sentence = "";

                    while (words > 0){
                        int word = random.nextInt(arrayList.size() - 21);
                        sentence += arrayList.get(word) + " ";
                        words -= random.nextDouble();
                    }

                    jt.setText(sentence);

                    jt.setMinimumSize(new Dimension(100, 100));

                    cte.add(jt, c);

                    comp = jt;


                    if (random.nextDouble() < 0.8) {
                        comp = cte;
                    }

                    comp.setMinimumSize(new Dimension(100, 100));
                } else {
                    JTextField jt = new JTextField(value);
                    double words = random.nextInt(3);

                    jt.setForeground(textColor);

                    String sentence = "";

                    if (random.nextDouble() < 0.7) {

                        while (words > 0) {
                            int word = random.nextInt(arrayList.size() - 21);
                            words -= random.nextDouble();
                            sentence += arrayList.get(word) + " ";
                        }

                        jt.setText(sentence);
                    }

                    cte.add(jt, c);
                    if (random.nextDouble() < 0.8) {
                        comp = cte;
                    } else {
                        comp = jt;
                    }
                }
                break;
            case 2: //combo box
                JComboBox jc = new JComboBox(RANDOM_WORDS);
                jc.setMinimumSize(new Dimension(100, 25));
                jc.setForeground(textColor);
                jc.setSelectedIndex(random.nextInt(RANDOM_WORDS.length));
                JPanel cc = new JPanel();
                cc.setMinimumSize(new Dimension(100, 25));
                cc.setLayout(new GridBagLayout());
                cc.add(jc, c);
                if (Math.random() < 0.5){
                    comp = cc;
                } else {
                    comp = jc;
                }
                break;
            case 3: //tree
                DefaultMutableTreeNode top =
                        new DefaultMutableTreeNode(value);
                DefaultMutableTreeNode current = top;

                List<String> sList = Arrays.asList(RANDOM_WORDS);

                Collections.shuffle(sList);

                for (int i = 0; i < arrayList.size(); i++) {
                    DefaultMutableTreeNode n = new DefaultMutableTreeNode(sList.get(i));
                    double rand = random.nextDouble();

                    boolean changed = false;

                    current.add(n);

                    if (current != top) { // go up the tree
                        if (rand < 0.2) {
                            current = current.getPreviousNode();
                            changed = true;
                        }
                    }

                    if (!changed) { // go down the tree
                        if (rand < 0.4) {
                            current = n;
                        }
                    }
                }
                JTree tree = new JTree(top);

                tree.setMinimumSize(new Dimension(100, 100));

                tree.setForeground(textColor);

                DefaultMutableTreeNode expand = top;
                int crow = 0;
                while (random.nextDouble() < 0.9 && expand.getChildCount() > 0) {
                    int row = random.nextInt(expand.getChildCount());
                    crow += row;
                    expand = (DefaultMutableTreeNode) expand.getChildAt(row);

                    tree.setSelectionRow(crow);
                    tree.expandRow(crow);
                    tree.updateUI();
                }
                comp = tree;

                break;
            case 4: //list
                int listBeg = random.nextInt(arrayList.size() - 1);
                List<String> nList = arrayList.subList(listBeg, listBeg + random.nextInt(arrayList.size() - listBeg));
                String[] sts = new String[nList.size()];
                nList.toArray(sts);
                JList list = new JList(sts);
                list.setMinimumSize(new Dimension(100, 100));
                list.setForeground(textColor);
                list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

                comp = list;

                if (random.nextDouble() < 0.5) {
                    int start = random.nextInt(10);
                    list.getSelectionModel().addSelectionInterval(start, start + random.nextInt(10));

                }


                break;
            case 5: //radio buttons
                JPanel co = new JPanel();
                co.setLayout(new GridBagLayout());
                if (random.nextDouble() < 0.5) {
                    JRadioButton jrb = new JRadioButton(value);
                    co.add(jrb, c);
                    jrb.setForeground(textColor);
                    jrb.setMinimumSize(new Dimension(100, 25));
                    if (random.nextDouble() < 0.5) {
                        jrb.setSelected(true);
                    }
                    comp = jrb;
                } else {
                    JCheckBox jtb = new JCheckBox(value);
                    jtb.setForeground(textColor);
                    jtb.setMinimumSize(new Dimension(100, 25));
                    co.add(jtb, c);
                    if (random.nextDouble() < 0.5) {
                        jtb.setSelected(true);
                    }
                    comp = jtb;
                }
                break;
            case 6: //menu item
                int listMenu = random.nextInt(arrayList.size() - 21);
                List<String> mList = arrayList.subList(listMenu, listMenu + random.nextInt(20));
                JMenuBar jm = new JMenuBar();
                JMenu menu = new JMenu(value);

                jm.setForeground(textColor);

                if (jframe.getJMenuBar() != null) {
                    jm = jframe.getJMenuBar();
                }
                jm.add(menu);

                comp = jm;


                for (int i = 0; i < mList.size(); i++) {
                    int opt = random.nextInt(10);
                    JMenuItem jmi;
                    switch (opt) {
                        case 0:
                            jmi = new JCheckBoxMenuItem(mList.get(i));
                            if (random.nextDouble() < 0.5) {
                                ((JCheckBoxMenuItem) jmi).setState(true);
                            }
                            break;
                        case 1:
                            boolean selected = false;
                            if (random.nextDouble() < 0.5) {
                                selected = true;
                            }
                            jmi = new JRadioButtonMenuItem(mList.get(i), selected);
                            break;
                        default:
                            jmi = new JMenuItem(mList.get(i));
                            break;
                    }

                    jmi.setForeground(textColor);

                    menu.add(jmi);

                    if (random.nextDouble() < 0.2) {
                        menu.addSeparator();
                    }

                }
                jframe.setJMenuBar(jm);

                break;
            case 7: //tabs
                JTabbedPane tabs = new JTabbedPane(TAB_PLACEMENTS[random.nextInt(TAB_PLACEMENTS.length)]);

                float num = random.nextFloat();

                if (num < 0.7)
                    tabs.setTabPlacement(JTabbedPane.TOP);
                else if (num < 0.8)
                    tabs.setTabPlacement(JTabbedPane.LEFT);
                else if (num < 0.9)
                    tabs.setTabPlacement(JTabbedPane.RIGHT);
                else
                    tabs.setTabPlacement(JTabbedPane.BOTTOM);

                tabs.setForeground(textColor);

                //tabs.setPreferredSize(new Dimension(100 + random.nextInt(300), 100 + random.nextInt(300)));


                int listTab = random.nextInt(arrayList.size() - 10);
                List<String> tList = arrayList.subList(listTab, listTab + 6);
                int i = 0;
                String rtree = randomTree(5, 2, 2, true, 1);

                for (GuiNode gn : new GuiTree(rtree).getRoot().getChildren()) {
                    tabs.addTab(tList.get(i++),
                            createTree(gn, arrayList, jframe, components + 1));
                }


                comp = tabs;


                break;
            case 8:
                JLabel l = new JLabel(value);
                l.setForeground(textColor);
                comp = l;
                break;
            case 9:
                JPanel jp = new JPanel();

                comp = jp;
                break;
            case 10:

                Container cbs = new Container();
                cbs.setLayout(new GridBagLayout());

                int maxVal = 2 + random.nextInt(30);

                JSlider slider = new JSlider(Math.random() < 0.5 ? JSlider.HORIZONTAL : JSlider.VERTICAL,
                        0, maxVal, random.nextInt(maxVal));

                slider.setMajorTickSpacing(random.nextInt(maxVal));
                slider.setMinorTickSpacing(random.nextInt(20));
                slider.setPaintTicks(random.nextBoolean());
                slider.setPaintLabels(random.nextBoolean());

                List<String> slList = Arrays.asList(RANDOM_WORDS);

                Collections.shuffle(slList);
                Hashtable labelTable = new Hashtable();
                int counter = 0;
                while (Math.random() < 0.5){
                    //Create the label table
                    labelTable.put( random.nextInt(maxVal), new JLabel(slList.get(counter++)) );
                    slider.setLabelTable( labelTable );
                }

                cbs.add(slider);
                if (random.nextDouble() < 0.8) {
                    comp = slider;
                } else {
                    comp = slider;
                }
                break;

            case 11: //scroll
                String rtrees = randomTree(1, 5, 5, true, 1);

                JComponent jcs = createTree(new GuiTree(rtrees).getRoot().getChildren().get(0), arrayList, jframe, components + 1);

                JScrollPane jsp = new JScrollPane(jcs);
                comp = jsp;

                if (jsp.getLocation().getX() + jsp.getSize().getHeight() > jframe.getHeight()) {
                    jsp.setSize((int) jsp.getSize().getWidth(), (int) (jframe.getHeight() - jsp.getLocation().getX()));
                }


                break;
        }

        if (comp == null){
            System.out.println("Couldn't created comp " + componentType);
        }

        return comp;
    }

    public JComponent createTree(GuiNode node, List<String> arrayList, JFrame jframe, int components){
        JComponent jc = generateComponent(Integer.parseInt(node.getName()), arrayList, jframe, components);

        jc.setMinimumSize(jc.getSize());

        //jc.setLayout(new GridBagLayout());

        for (GuiNode gn : node.getChildren()){
            jc.add(createTree(gn, arrayList, jframe, components), c);
        }

        return jc;
    }

    public String randomTree(int x, int y, int z, boolean replaceMenus, int minComponents){
        String tree = "[";

        int mx = random.nextInt(x)+minComponents;
        for (int i = 0; i < mx; i++){
            tree += "[" + random.nextInt(11);
            int mj = random.nextInt(y);
            for (int j = 0; j < mj; j++){
                tree += "[" + random.nextInt(11);
                int mk = random.nextInt(z);
                for (int k = 0; k < mk; k++){
                    tree += "[" + random.nextInt(12) + "]";
                }
                tree += "]";
            }
            tree += "]";
        }

        tree += "]";

        while(replaceMenus && (tree.contains("6") || tree.contains("9"))){
            tree = tree.replace("6", ""+random.nextInt(11));
            tree = tree.replace("9", ""+random.nextInt(11));
        }

        return tree;
    }

    public JFrame generateRandomFrame() {

        boolean dark = false;

        widgetsX = 1 + random.nextInt(MAX_WIDGETS_X);
        widgetsY = 1 + random.nextInt(MAX_WIDGETS_Y);

        List<String> arrayList = Arrays.asList(RANDOM_WORDS);
        Collections.shuffle(arrayList);
        JFrame jframe = new JFrame(arrayList.get(0));
        textColor = null;

//        if (random.nextDouble() < 0.3) {
//            if (random.nextDouble() < 0.5) {
//                textColor = Color.DARK_GRAY;
//            } else {
//                textColor = Color.BLACK;
//            }
//            jframe.getContentPane().setForeground(textColor);
//
//        }

        if (random.nextDouble() < 0.1) {
//            // enable dark theme
//            dark = true;
//            if (random.nextDouble() < 0.5) {
//                backgroundColor = Color.DARK_GRAY;
//            } else {
//                backgroundColor = Color.BLACK;
//            }
//
//            if (random.nextDouble() < 0.5) {
//                textColor = Color.lightGray;
//            } else {
//                textColor = Color.WHITE;
//            }
//
//            jframe.getContentPane().setBackground(backgroundColor);
//            jframe.getContentPane().setForeground(textColor);
//
//            //dark = true;
        }

        if (textColor == null){
            textColor = jframe.getForeground();
        }

        int components = 0;

        float layoutChance = random.nextFloat()+1;

        if (layoutChance < 0.3) {
            jframe.setLayout(new BorderLayout());
            LAYOUT = Layout.BORDER;
        } else if (layoutChance < 0.4) {
            jframe.setLayout(new FlowLayout());
            MAX_NUMBER_COMPONENTS = 5;
            LAYOUT = Layout.FLOW;
        } else if (layoutChance < 0.7) {
            jframe.setLayout(new GridLayout(widgetsY, widgetsX, random.nextInt(MAX_PADDING), random.nextInt(MAX_PADDING)));
            LAYOUT = Layout.GRID;
        } else {
            jframe.setLayout(new GridBagLayout());
            LAYOUT = Layout.GRID_BAG;
        }

        int width = MIN_WIDTH + random.nextInt(MAX_WIDTH - MIN_WIDTH);

        int height = MIN_HEIGHT + random.nextInt(MAX_HEIGHT - MIN_HEIGHT);


        jframe.setSize(width, height);

        jframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Container contentPane = jframe.getContentPane();

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JComponent comp = null;

        String tree = randomTree(7, 5, 5, true, 4);

        System.out.println(tree);

        //String tree = "[10[10[0][1]][2][3][10[4][5]]]";

        GuiTree gt = new GuiTree(tree);

        for (GuiNode gn : gt.getRoot().getChildren()) {


            if (random.nextDouble() < 0.5){
                c.fill = GridBagConstraints.HORIZONTAL;
            } else {
                c.fill = GridBagConstraints.VERTICAL;
            }

            randomizeC();

            JComponent jc = createTree(gn, arrayList, jframe, components);


            add(jframe, jc);


//            if (textColor != null) {
//                jc.setForeground(textColor);
//                jc.setBackground(backgroundColor);
//            }

            components+= gn.getChildrenCount();
        }

        // add menu
        if (Math.random() < 0.2) {
            int comps = random.nextInt(6)+1;
            for (int i = 0; i < comps; i++) {
                generateComponent(6, arrayList, jframe, components+i);
            }
        }


//        if (random.nextFloat() < 0.3) {
//            jframe.pack();
//        }


        //jframe.setMinimumSize(jframe.getSize());

        jframe.pack();

        jframe.invalidate();
        jframe.repaint();

        jframe.setVisible(true);

//        if (random.nextDouble() < 0.2 && jframe.getJMenuBar() != null){
//            JMenuItem jmi = jframe.getJMenuBar().getMenu(random.nextInt(jframe.getJMenuBar().getMenuCount()));
//            //jmi.setArmed(true);
//            MenuElement me = jmi.getSubElements()[random.nextInt(jmi.getSubElements().length)];
//            MenuSelectionManager.defaultManager().setSelectedPath(new MenuElement[]{jframe.getJMenuBar(), jmi,
//                    me});
//            jmi.setArmed(true);
//            me.getComponent().setLocation(jmi.getLocationOnScreen().x,
//                    jmi.getLocationOnScreen().y + jmi.getHeight());
//            me.getComponent().setVisible(true);
//            jmi.doClick();
//            jmi.setVisible(true);
//
//            //jmi.setEnabled(true);
//        }

        return jframe;
    }

    private static void randomizeC() {
        c.gridx = random.nextInt(5);
        c.gridy = random.nextInt(5);

        c.weightx = random.nextDouble();
        c.weighty = random.nextDouble();


        c.ipadx = random.nextInt(20);
        c.ipady = random.nextInt(20);
    }

    private static final String[] BORDER_LAYOUTS = {BorderLayout.NORTH,
            BorderLayout.SOUTH, BorderLayout.EAST, BorderLayout.WEST, BorderLayout.CENTER};

    public void add(JFrame jframe, Container jc) {
        int x = (int) (jframe.getWidth() * 0.15) + random.nextInt(200);
        int y = (int) (jframe.getWidth() * 0.15) + random.nextInt(200);

        jc.setMinimumSize(new Dimension(x, y));
        jc.setPreferredSize(new Dimension(x, y));

        jc.setForeground(textColor);

        if (jc.getParent() != null && (jc.getParent().getWidth() < x || jc.getParent().getHeight() < y)) {
            jc.getParent().setPreferredSize(new Dimension(x, y));
        }

        switch (LAYOUT) {
            case FLOW:
                jframe.add(jc);
                break;
            case GRID:
                jframe.add(jc);//, random.nextInt(widgetsX), random.nextInt(widgetsY));
                break;
            case BORDER:
                jframe.add(jc, BORDER_LAYOUTS[random.nextInt(BORDER_LAYOUTS.length)]);
                break;
            case GRID_BAG:
                jframe.add(jc, c);
        }
    }

    private static int[] TAB_PLACEMENTS = {JTabbedPane.LEFT, JTabbedPane.TOP, JTabbedPane.RIGHT, JTabbedPane.BOTTOM};

    public static void main(String args[]) {
        if (args.length < 2) {
            System.err.println("Must have args of length 2 (output directory, theme name)");
            System.exit(1);
        }

        FrameGeneratorFromTree fg = new FrameGeneratorFromTree();

        int frames = 5000;

        RipperFrame rf = new RipperFrame();

        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://" + Preferences.LOCATION + ":" + Preferences.PORT + "/" + Preferences.DB,
                    Preferences.USER, Preferences.PASSWORD);


            String dir = args[0];
            THEME = args[1];

            rf.dataLocation.setText(dir);

            for (int i = 0; i < frames; i++) {
                JFrame jf = fg.generateRandomFrame();

//                if (random.nextDouble() < 0.3) {
//                    JMenuBar jm = jf.getJMenuBar();
//
//                    if (jm != null) {
//
//                        try {
//                            Thread.sleep(150);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        JMenu menu = jm.getMenu(random.nextInt(jm.getMenuCount()));
//
//                        MenuSelectionManager.defaultManager().setSelectedPath(
//                                new MenuElement[]{
//                                        menu, menu.getPopupMenu()
//                                }
//                        );
//                    }
//                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if (i == 0) {
                    try {
                        System.out.println("30 seconds to validate that stuff is working!");
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //jf.setVisible(true);


                try {
                    rf.rip(i + "-jframe-" + THEME, jf, "synthetic", "synthetic", con, THEME);
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.exit(10);
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                jf.dispose();

                System.out.print("\r" + (i + 1) + "/" + frames + " frames generated.");
            }
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

}
