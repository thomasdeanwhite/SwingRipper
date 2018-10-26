package com.thomasdeanwhite.swingripper;

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

public class FrameGenerator {

    private static String THEME = "";

    private enum Layout {
        FLOW, GRID, BORDER, CARD;
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

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private static int MAX_NUMBER_COMPONENTS = 25;
    private static int MAX_WIDTH = 720;
    private static int MAX_HEIGHT = 720;

    private static final int MAX_PADDING = 50;


    private static int MIN_NUM_COMPONENTS = 4;

    private static int MIN_WIDTH = 256;
    private static int MIN_HEIGHT = 256;

    public FrameGenerator() {

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

        float layoutChance = random.nextFloat();

        if (layoutChance < 0.4) {
            jframe.setLayout(new BorderLayout());
            LAYOUT = Layout.BORDER;
        } else if (layoutChance < 0.6) {
            jframe.setLayout(new FlowLayout());
            MAX_NUMBER_COMPONENTS = 5;
            LAYOUT = Layout.FLOW;
        } else {
            jframe.setLayout(new GridLayout(widgetsY, widgetsX, random.nextInt(MAX_PADDING), random.nextInt(MAX_PADDING)));
            LAYOUT = Layout.GRID;
        }

        int width = MIN_WIDTH + random.nextInt(MAX_WIDTH - MIN_WIDTH);

        if (width < widgetsX * 100)
            width = widgetsX * 100;

        int height = MIN_HEIGHT + random.nextInt(MAX_HEIGHT - MIN_HEIGHT);

        if (height < widgetsY * 100)
            height = widgetsY * 100;

        jframe.setSize(width, height);

        jframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Container contentPane = jframe.getContentPane();

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JComponent comp = null;

        while (components < MIN_NUM_COMPONENTS || (components < MAX_NUMBER_COMPONENTS && random.nextDouble() < Math.pow(0.98, components))) {
            String value = arrayList.get(components);
            float nextElement = random.nextFloat();
            int nextEle = 0;

            while (nextElement > PROBABILITIES[nextEle]){
                nextElement -= PROBABILITIES[nextEle];
                nextEle++;
            }

            nextEle = random.nextInt(11);
            switch (nextEle) {

                case 0: //button
                    Container cb = new Container();
                    cb.setLayout(new FlowLayout());

                    if (Math.random() < 0.3){
                        JLabel l = new JLabel(value);
                        l.setForeground(textColor);
                        cb.add(l);
                        break;
                    }
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

                    cb.add(b);
                    if (random.nextDouble() < 0.8) {
                        add(jframe, cb);
                        comp = b;
                    } else {
                        add(jframe, b);
                        comp = b;
                    }
                    break;
                case 1: //text field
                    Container cte = new Container();
                    cte.setLayout(new FlowLayout());
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

                        cte.add(jt);
                        if (random.nextDouble() < 0.8) {
                            add(jframe, cte);
                            comp = jt;
                        } else {
                            add(jframe, jt);
                            comp = jt;
                        }

                        add(jframe, jt);
                        comp = jt;
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

                        cte.add(jt);
                        if (random.nextDouble() < 0.8) {
                            add(jframe, cte);
                            comp = jt;
                        } else {
                            add(jframe, jt);
                            comp = jt;
                        }
                    }
                    break;
                case 2: //combo box
                    JComboBox jc = new JComboBox(RANDOM_WORDS);
                    jc.setMinimumSize(new Dimension(100, 25));
                    jc.setForeground(textColor);
                    jc.setSelectedIndex(random.nextInt(RANDOM_WORDS.length));
                    Container c = new Container();
                    c.setMinimumSize(new Dimension(100, 25));
                    c.setLayout(new FlowLayout());
                    c.add(jc);
                    add(jframe, c);
                    comp = jc;
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

                    if (Math.random() < 0.3){
                        JScrollPane jsp = new JScrollPane(tree);
                        add(jframe, tree);
                    } else {
                        add(jframe, tree);
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
                    JScrollPane listScroller = new JScrollPane(list);

                    listScroller.setMinimumSize(new Dimension(100, 100));

                    add(jframe, listScroller);

                    comp = listScroller;

                    if (random.nextDouble() < 0.5) {
                        int start = random.nextInt(10);
                        list.getSelectionModel().addSelectionInterval(start, start + random.nextInt(10));

                    }
                    if (listScroller.getSize().getHeight() > MIN_HEIGHT) {
                        listScroller.setSize((int) listScroller.getSize().getWidth(), MIN_HEIGHT / 3);
                    }


                    break;
                case 5: //radio buttons
                    Container co = new Container();
                    co.setLayout(new FlowLayout());
                    if (random.nextDouble() < 0.5) {
                        JRadioButton jrb = new JRadioButton(value);
                        co.add(jrb);
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
                        co.add(jtb);
                        if (random.nextDouble() < 0.5) {
                            jtb.setSelected(true);
                        }
                        comp = jtb;
                    }
                    add(jframe, co);
                    break;
                case 6: //menu item
                    if (random.nextDouble() < 0.5 / MAX_WIDGETS_X * MAX_WIDGETS_Y) {
                        continue;
                    }
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

//                        if (random.nextDouble() < 1.0/mList.size()){
//
//                            menu = new JMenu();
//                            jm.add(menu);
//                        }
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

                    tabs.setPreferredSize(new Dimension(100 + random.nextInt(300), 100 + random.nextInt(300)));


                    int listTab = random.nextInt(arrayList.size() - 21);
                    List<String> tList = arrayList.subList(listTab, listTab + random.nextInt(20));
                    int i = 0;
                    while (random.nextDouble() < 0.5 && i < tList.size()) {
                        tabs.addTab(tList.get(i++), getRandomComponent(textColor));
                    }


                    if (Math.random() < 0.3){
                       JScrollPane jsp = new JScrollPane(tabs);
                       add(jframe, jsp);
                    } else {
                        add(jframe, tabs);
                    }

                    comp = tabs;

                    break;
                case 8:
                    JLabel l = new JLabel(value);
                    l.setForeground(textColor);
                    add(jframe, l);
                    comp = l;
                    break;
                case 9:
                    JPanel jp = new JPanel();
                    add(jframe, jp);

                    comp = jp;
                    break;
                case 10:

                    Container cbs = new Container();
                    cbs.setLayout(new FlowLayout());

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
                        add(jframe, cbs);
                        comp = slider;
                    } else {
                        add(jframe, slider);
                        comp = slider;
                    }

            }

            if (textColor != null) {
                comp.setForeground(textColor);
                comp.setBackground(backgroundColor);
            }

            components++;
        }

        if (random.nextFloat() < 0.3) {
            jframe.pack();
        }

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
        }
    }

    public static Container getRandomComponent(Color textColor) {
        List<String> arrayList = Arrays.asList(RANDOM_WORDS);
        Collections.shuffle(arrayList);
        int components = 0;

        Container c = new Container();

        c.setLayout(new FlowLayout());

        c.setPreferredSize(new Dimension(random.nextInt(MAX_WIDTH), random.nextInt(MAX_HEIGHT)));

        while (components < MIN_NUM_COMPONENTS || (components < MAX_NUMBER_COMPONENTS && random.nextDouble() < Math.pow(0.95, components))) {
            String value = arrayList.get(components);
            float nextElement = random.nextFloat();
            int nextEle = 0;

            while (nextElement > PROBABILITIES[nextEle]){
                nextElement -= PROBABILITIES[nextEle];
                nextEle++;
            }

            nextEle = random.nextInt(11);

            switch (nextEle) {
                case 0: //button
                    JButton b = null;
                    if (random.nextDouble() < 0.8){
                        b = new JButton(value);
                    } else {
                        b = new JButton(new ImageIcon(icons.get(random.nextInt(icons.size()))));
                    }
                    b.setForeground(textColor);
                    c.add(b);
                    break;
                case 1: //text field
                    Container cte = new Container();
                    cte.setLayout(new FlowLayout());
                    if (random.nextDouble() < 0.3) {
                        JTextArea jt = new JTextArea(value);
                        jt.setMargin(new Insets(10, 10, 10, 10));
                        jt.setForeground(textColor);
                        double words = random.nextInt(8);

                        String sentence = "";

                        while (words > 0){
                            int word = random.nextInt(arrayList.size() - 21);
                            sentence += arrayList.get(word) + " ";
                            words -= random.nextDouble();
                        }

                        jt.setText(sentence);

                        c.add(jt);
                    } else {
                        JTextField jt = new JTextField(value);
                        jt.setForeground(textColor);
                        double words = random.nextInt(3);

                        String sentence = "";

                        if (random.nextDouble() < 0.7) {

                            while (words > 0) {
                                int word = random.nextInt(arrayList.size() - 21);
                                words -= random.nextDouble();
                                sentence += arrayList.get(word) + " ";
                            }

                            jt.setText(sentence);
                        }

                        c.add(jt);
                    }
                    break;
                case 2: //combo box
                    JComboBox jc = new JComboBox(RANDOM_WORDS);
                    jc.setForeground(textColor);
                    jc.setSelectedIndex(random.nextInt(RANDOM_WORDS.length));
                    c.add(jc);
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

                    c.add(tree);
                    break;
                case 4: //list
                    int listBeg = random.nextInt(arrayList.size() - 1);
                    List<String> nList = arrayList.subList(listBeg, listBeg + random.nextInt(arrayList.size() - listBeg));
                    String[] sts = new String[nList.size()];
                    nList.toArray(sts);
                    JList list = new JList(sts);
                    list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                    JScrollPane listScroller = new JScrollPane(list);
                    c.add(listScroller);

                    list.setForeground(textColor);

                    if (random.nextDouble() < 0.5) {
                        int start = random.nextInt(10);
                        list.getSelectionModel().addSelectionInterval(start, start + random.nextInt(10));

                    }
                    if (listScroller.getSize().getHeight() > MIN_HEIGHT) {
                        listScroller.setSize((int) listScroller.getSize().getWidth(), MIN_HEIGHT / 3);
                    }


                    break;
                case 5: //radio buttons
                    if (random.nextDouble() < 0.5) {
                        JRadioButton jrb = new JRadioButton(value);
                        jrb.setForeground(textColor);
                        c.add(jrb);
                        if (random.nextDouble() < 0.5) {
                            jrb.setSelected(true);
                        }
                    } else {
                        JCheckBox jtb = new JCheckBox(value);
                        c.add(jtb);
                        jtb.setForeground(textColor);
                        if (random.nextDouble() < 0.5) {
                            jtb.setSelected(true);
                        }
                    }
                    break;
                case 6: //tabs
                    JTabbedPane tabs = new JTabbedPane(TAB_PLACEMENTS[random.nextInt(TAB_PLACEMENTS.length)]);

                    tabs.setForeground(textColor);

                    int listTab = random.nextInt(arrayList.size() - 21);
                    List<String> tList = arrayList.subList(listTab, listTab + random.nextInt(20));
                    int i = 0;
                    while (random.nextDouble() < 0.2 && i < tList.size()) {
                        tabs.addTab(tList.get(i++), getRandomComponent(textColor));
                    }
                    c.add(tabs);
                    break;
                case 7:
                    JLabel l = new JLabel(value);
                    l.setForeground(textColor);
                    c.add(l);
                    break;
                case 9:
                    JPanel jp = new JPanel();
                    c.add(jp);
                case 10:

                    Container cbs = new Container();
                    cbs.setLayout(new FlowLayout());

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
                        c.add(cbs);
                    } else {
                        c.add(slider);
                    }

            }

            components++;
        }


        return c;
    }

    private static int[] TAB_PLACEMENTS = {JTabbedPane.LEFT, JTabbedPane.TOP, JTabbedPane.RIGHT, JTabbedPane.BOTTOM};

    public static void main(String args[]) {
        if (args.length < 2) {
            System.err.println("Must have args of length 2 (output directory, theme name)");
            System.exit(1);
        }

        FrameGenerator fg = new FrameGenerator();

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
