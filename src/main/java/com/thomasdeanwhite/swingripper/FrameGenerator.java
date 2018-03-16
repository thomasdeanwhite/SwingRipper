package com.thomasdeanwhite.swingripper;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FrameGenerator {

    private enum Layout {
            FLOW, GRID, BORDER, CARD;
    }

    private int widgetsX = 10;
    private int widgetsY = 10;

    private static String[] RANDOM_WORDS = new String[]{
            "knowledgeable","possessive","business","ajar","nose","awake","mouth","help","verdant","unsightly","digestion","type","enjoy","whip","obsequious","heap","beautiful","hateful","beg","flash","drown","scarce","fine","tickle","nonchalant","vague","loutish","fabulous","five","fearless","delicate","unfasten","opposite","bad","wasteful","practise","recess","axiomatic","shoes","yell","kick","next","chew","liquid","bashful","bedroom","superb","divergent","huge","doctor","moan","fortunate","bag","company","cake","voiceless",
            "copy","visitor","ladybug","serve","effect","fax","ethereal","periodic","lunchroom","joke","coach","star","grey","empty","clammy","teaching","laugh","unwieldy","form","influence","retire","groovy","colorful","temper","hook","reading","bump","melted","wren","roll","mask","tiresome","alarm","throne","trust","voyage","overrated","jar","common","language","prevent","grab","violent","handle","flowery","crook","purple","advertisement","machine","advise","decorate","hour","unused","regret","use","cup","roomy","suggest","irritate","sturdy","nut","window","juicy","mature","position","organic","leather","splendid","rub","boil","gruesome","hug","pickle","thread","nail","sneeze","sordid","abrasive","geese","bewildered","abaft","imagine","nimble","frog","grumpy","ants","front","serious","growth","paint","art","quixotic","car","adaptable","iron","error","slip","rabbit","decorous","yellow","versed","magical","scattered","branch","found","half","squash","volatile","injure","lethal","exercise","increase","parcel","finger","spotted","zipper","embarrassed",
            "afraid","bent","breakable","ill-fated","receive","ripe","care","dramatic","cool","panoramic","steady","lucky","cream","polish","look","fast","regret","broad","welcome","lively","club","horrible","instinctive","angle","rake","mice","mindless","second","remove","festive","form","fire","wool","clip","loose","trick","bleach","quill","lean","limit","faithful","jelly","dance","long","empty","lackadaisical","snotty","concern","unequaled","creator","same","ceaseless","weight","hungry","magic","square","fertile","frighten","dreary","craven","governor","hammer","assorted","ubiquitous","outgoing","elite","alleged","ludicrous","snow","wiggly","curious","yielding","press","pathetic","reign","well-made","bouncy","spiffy","government","relieved","busy","stop","spectacular",
            "calculate","tray","glue","overflow","imperfect","sigh","country","color","tearful","uttermost","arrange","fallacious","hobbies","scared","annoying","need","arrogant","door","troubled","loss","desk","obeisant","appliance","achiever","rod","test","obnoxious","wall","muddled","cobweb","automatic","imported","number","jittery","suppose","determined","side","glistening","advice","airplane","hope","team","handy","jail","separate","tasteful","employ","book","judicious","smash","box","soggy","cooperative","fact","dysfunctional","scorch","super","hilarious","exist","like","spray","nappy","resonant","secretary","gaze","hard","intelligent","realise","modern","support","approval","rare","reduce","van","witty","eggs","behave","flower","picture","mate","precious","rainy","domineering","easy","basin","close","range","quilt","spy","feeling","windy","pretty","sudden","care","match","insurance","crack","inform","vase","saw","charge","bomb","fat","move","activity","trouble","clean","time","damp","dazzling","brave","harmony","consist","time","key","certain","rapid","chubby","size","extra-large","abiding","icy","scold","call","substantial","gusty","eatable","need",
            "things","cut","moor","woozy","rough","nutritious","credit","ski","boorish","impress","develop","nostalgic","disastrous","happy","identify","wealth","glow","curvy","encourage","meat","test","curly","wriggle","dock","aromatic","giants","unwritten","class","kittens","ground","sock","skate","balance","tawdry","right","rock","efficient","orange","breath","unkempt","walk","baseball","rural","reproduce","elegant","creature","snow","basketball","spill","measure","plastic","eight","dapper","amusement","jeans","groan","kindhearted","oatmeal","unable","yam","lowly","fear","fish","story","vast","wiry","night","political","ear","mend","cover","madly","living","taste","ashamed","learned","didactic","nosy","shake","closed","bored","attract","dust","jumbled","provide","record","hesitant","apparel","dad","tiger","add","thunder","level","physical","boundary","aquatic","venomous","needy","zinc","cuddly","lip","wrench","amused","flesh","pin","toad","bulb","abject","rich","fuel","permit","painstaking","continue","slip","gaping","whimsical","twist","puzzled","bite","edge","murder","daffy","head","rejoice","necessary","scarf","protect","earth","solid","colour","snatch",
            "expert","fearful","clumsy","extend","bake","internal","ahead","underwear","committee","pull","sneeze","unique","open","sparkle","deceive","fence","flavor","understood","trucks","oil","peace","pull","veil","whip","pet","remind","aggressive","cannon","point","many","harmonious","truck","illegal","absorbing","tired","well-groomed","stone","memory","panicky","church","abounding","aftermath","malicious","premium","plucky","week","pour","discussion","stretch","quicksand","parsimonious","stretch","mammoth","nervous","heal","mass","entertaining","fool","numerous","nutty","victorious","frequent","work","stage","goofy","kindly","boundless","gainful","experience","muscle","keen","lock","squirrel","obtain","pizzas","cent","existence","agreeable","weary","apathetic","swim","sticks","educate","wave","duck","lamp","thankful","meeting","tremendous","icky","complain","peel","guarantee","combative","wrap","screeching","jaded","camp","painful","twist","frame","friends","recondite","observe","hand","alive","icicle","soak","absurd","ambitious","big","tie","dust","store","flower","rain","undesirable","vest","glass","chicken","wary","ruddy","obscene","servant","erect","woebegone","ready","encouraging","bikes","six","wander","hate","important","disturbed","air","fumbling","entertain","husky",
            "penitent","crow","avoid","previous","yarn","divide","seal","notebook","sisters","dynamic","great","zebra","snore","snakes","bitter","collar","interest","live","screw","jam","lavish","man","argument","invite","voracious","repeat","scale","present","gaudy","wooden","representative","subdued","exciting","impolite","ragged","follow","fly","jobless","alike","untidy","grate","stroke","unhealthy","own","cloistered","whistle","incredible","part","tour","breathe","hair","eminent","apparatus","hunt","cow","surprise","superficial","irritating","near","fry","sniff","squeeze","haircut","immense","dream","quizzical","frail","crack","supreme","destroy","cause","useful","polite","argue","ossified","mute","number","day","lovely","chickens","tent","black-and-white","describe","dangerous","rock","donkey","mountainous","aberrant","cute","property","distance","loaf","doubtful","knock","reaction","ugly","smile","card","writing","steadfast","sail","bead","blot","exultant","aback","free","rampant","toe","shiver","hanging","town","fluffy","robin",
            "whispering","offbeat","cold","face","pancake","cactus","beds","science","furtive","hospital","dislike","shock","mix","consider","blink","juvenile","stay","start","plant","crash","nest","society","land","sedate","root","majestic","cheer","carry","respect","wry","hole","secretive","push","applaud","warn","enormous","fresh","bed","shirt","occur","wail","stamp","moaning","spoon","thirsty","graceful","pointless","alluring","scrape","burst","slave","tongue","marked","harass","righteous","rely","accessible","unlock","clam","nine","toothsome","rate","earthquake","punish","amount","terrify","love","degree","remarkable","wrist","misty","mother","examine","space","unit","protective","squeal","milky","petite","oranges","doubt","cows","walk","strip","unruly","telephone","punch","muddle","cough","object","waves","boring","babies","melt","elfin","bumpy","pastoral","adventurous","jealous","white","ticket","overwrought","quartz","slippery","first","sore","bustling","roasted","cruel","angry","listen","spade","seat","addition","chop","dizzy","perfect","transport","powerful","acceptable","secret","young","helpful","unpack","different","trail","balance","fold","abashed","collect","deafening","smart","grade","deer","naive","can","crabby","discover","cross","border","tap","caring","winter","spiky","horse","amuck","hurt","tidy","pine","far","settle","sweltering","mountain","title","dare","industry","locket","calculating","lumber","haunt","efficacious","neck","nasty","somber","prickly","hill","end","relation","aboriginal","peep","thundering","sugar","increase","bury",
            "past","power","hushed","three","staking","dress","tick","drunk","sip","stitch","drink","education","partner","girl","bang","woman","visit","suspect","sack","high","uppity","boast","moldy","bow","simplistic","conscious","separate","sack","mighty","reply","swanky","clear","ignorant","thinkable","flagrant","cheap","reward","lopsided","frightened","film","cushion","cat","waste","play","paltry","cave","beam","drain","appear","imminent","rest","tenuous","exclusive","cry","word","coast","zany","smoke","slow","impulse","electric","gold","male","disagree","communicate","comparison","jump","ocean","back","detect","deadpan","frogs","dusty","offend","desert","trite","special","purpose","magnificent","hose","grandfather","coordinated","weather","jewel","smoggy","slow","improve","glamorous","voice","load","devilish","religion","string","successful","stamp","brash","paste","whisper","stranger","daughter","old","smile","zealous","rhetorical","clean","tart","diligent","approve","humorous","distinct","watch","careful","low","detail","floor","silk","skillful","interrupt","houses","cloth","flippant","mourn","squealing","cast","ban","flag","steep","psychotic","flat","compete",
            "comb","possible","dam","courageous","replace","leg","handsomely","black","celery","brass","utter","brake","functional","brief","shade","act","sweater","belong","person","trace","expand","lively","grape","interest","attack","doll","trouble","change","milk","overconfident","knowledge","lumpy","scare","drip","momentous","sable","cloudy","sea","arch","helpless","travel","nerve","sign","paddle","trot","left","treatment","paper","room","magenta","monkey","stem","rhythm","rings","queue","invention","look","talk","meaty","remember","spiteful","pause","threatening","attack","used","dolls","wet","shave","wide","cap","run","funny","low","short","gun","best","trashy","callous","horn","sofa","judge","damage","foolish","bridge","trap","volleyball","sulky","men","engine","poised","obsolete","anger","clever","willing","idea","merciful","trees","deranged","silly","fear","pencil","library","well-to-do","thrill","infamous",
            "river","plant","interesting","motionless","carve","tested","bore","badge","tremble","tin","complete","excellent","spooky","delirious","plot","far-flung","guard","berry","pie","zoom","healthy","name","knot","agonizing","shrug","neighborly","contain","observant","sponge","group","theory","accidental","wax","wish","slim","like","produce","worry","glove","abhorrent","false","spring","grip","hate","orange","last","synonymous","juice","open","office","aspiring","grieving","abusive","plant","sound","striped","spell","zephyr","rush","party","year"
    };

    private static Random random = new Random();

    static {
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
    }

    private static int MAX_NUMBER_COMPONENTS = 20;
    private static int MAX_WIDTH = 1024;
    private static int MAX_HEIGHT = 768;

    private static int MIN_NUM_COMPONENTS = 10;

    private static int MIN_WIDTH = 256;
    private static int MIN_HEIGHT = 256;

    public FrameGenerator(){
        widgetsX = 1 + random.nextInt(widgetsX);
        widgetsY = 1 + random.nextInt(widgetsY);
    }

    private static Layout LAYOUT = Layout.FLOW;;

    public JFrame generateRandomFrame(){
        List<String> arrayList = Arrays.asList(RANDOM_WORDS);
        Collections.shuffle(arrayList);
        JFrame jframe = new JFrame();

        int components = 0;

        switch (random.nextInt(3)){
            case 0:
                jframe.setLayout(new BorderLayout());
                LAYOUT = Layout.BORDER;
                break;
            case 1:
                jframe.setLayout(new FlowLayout());
                LAYOUT = Layout.FLOW;
                break;
            case 2:
                jframe.setLayout(new GridLayout(widgetsX, widgetsY, random.nextInt(10), random.nextInt(10)));
                LAYOUT = Layout.GRID;
                break;
        }

        jframe.setSize(MIN_WIDTH + random.nextInt(MAX_WIDTH-MIN_WIDTH),
                MIN_HEIGHT + random.nextInt(MAX_HEIGHT-MIN_HEIGHT));

        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container contentPane = jframe.getContentPane();

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        while (components < MIN_NUM_COMPONENTS || (components < MAX_NUMBER_COMPONENTS && random.nextDouble() < Math.pow(0.95, components))){
            String value = arrayList.get(components);
            switch(random.nextInt(8)){
                case 0: //button
                    JButton b = new JButton(value);
                    add(jframe, b);
                    break;
                case 1: //text field
                    if (random.nextDouble() < 0.3){
                        JTextArea jt = new JTextArea(value);
                        jt.setSize((MIN_WIDTH/3)+random.nextInt(MIN_WIDTH/3), (MIN_HEIGHT/3)+random.nextInt(MIN_HEIGHT/3));
                        add(jframe, jt);
                    } else {
                        JTextField jt = new JTextField(value);

                        add(jframe, jt);
                    }
                    break;
                case 2: //combo box
                    JComboBox jc = new JComboBox(RANDOM_WORDS);
                    jc.setSelectedIndex(random.nextInt(RANDOM_WORDS.length));
                    add(jframe, jc);
                    break;
                case 3: //tree
                    DefaultMutableTreeNode top =
                            new DefaultMutableTreeNode(value);
                    DefaultMutableTreeNode current = top;

                    List<String> sList = Arrays.asList(RANDOM_WORDS);

                    Collections.shuffle(sList);

                    for (int i = 0; i < arrayList.size(); i++){
                        DefaultMutableTreeNode n = new DefaultMutableTreeNode(sList.get(i));
                        double rand = random.nextDouble();

                        boolean changed = false;

                        current.add(n);

                        if (current != top){ // go up the tree
                            if (rand < 0.2){
                                current = current.getPreviousNode();
                                changed = true;
                            }
                        }

                        if (!changed){ // go down the tree
                            if (rand < 0.4){
                                current = n;
                            }
                        }
                    }
                    JTree tree = new JTree(top);

                    DefaultMutableTreeNode expand = top;
                    int crow = 0;
                    while(random.nextDouble() < 0.9 && expand.getChildCount() > 0) {
                        int row = random.nextInt(expand.getChildCount());
                        crow += row;
                        expand = (DefaultMutableTreeNode) expand.getChildAt(row);

                        tree.setSelectionRow(crow);
                        tree.expandRow(crow);
                        tree.updateUI();
                    }

                    add(jframe, tree);
                    break;
                case 4: //list
                    int listBeg = random.nextInt(arrayList.size()-1);
                    List<String> nList = arrayList.subList(listBeg, listBeg + random.nextInt(arrayList.size()-listBeg));
                    String[] sts = new String[nList.size()];
                    nList.toArray(sts);
                    JList list = new JList(sts);
                    list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                    JScrollPane listScroller = new JScrollPane(list);
                    add(jframe, listScroller);

                    if (random.nextDouble() < 0.5) {
                        int start = random.nextInt(10);
                        list.getSelectionModel().addSelectionInterval(start, start + random.nextInt(10));

                    }
                    if (listScroller.getSize().getHeight() > MIN_HEIGHT) {
                        listScroller.setSize((int) listScroller.getSize().getWidth(), MIN_HEIGHT / 3);
                    }


                    break;
                case 5: //radio buttons
                    if (random.nextDouble() < 0.5){
                        JRadioButton jrb = new JRadioButton(value);
                        add(jframe, jrb);
                        if (random.nextDouble() < 0.5){
                            jrb.setSelected(true);
                        }
                    } else {
                        JToggleButton jtb = new JToggleButton(value);
                        add(jframe, jtb);
                        if (random.nextDouble() < 0.5){
                            jtb.setSelected(true);
                        }
                    }
                    break;
                case 6: //menu item
                    int listMenu = random.nextInt(arrayList.size()-21);
                    List<String> mList = arrayList.subList(listMenu, listMenu + random.nextInt(20));
                    JMenuBar jm = new JMenuBar();
                    JMenu menu = new JMenu(value);

                    if (jframe.getJMenuBar() != null){
                        jm = jframe.getJMenuBar();
                    }
                    jm.add(menu);



                    for (int i = 0; i < mList.size(); i++){
                        int opt = random.nextInt(10);
                        JMenuItem jmi;
                        switch(opt) {
                            case 0:
                                jmi = new JCheckBoxMenuItem(mList.get(i));
                                break;
                            case 1:
                                jmi = new JRadioButtonMenuItem(mList.get(i));
                                break;
                            default:
                                jmi = new JMenuItem(mList.get(i));
                                break;
                        }

                        menu.add(jmi);

                        if (random.nextDouble() < 0.2){
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
                    JTabbedPane tabs = new JTabbedPane();

                    tabs.setPreferredSize(new Dimension(100, 100));

                    int listTab = random.nextInt(arrayList.size()-21);
                    List<String> tList = arrayList.subList(listTab, listTab + random.nextInt(20));
                    int i = 0;
                    while (random.nextDouble() < 0.5 && i < tList.size()){
                        tabs.addTab(tList.get(i++), getRandomComponent());
                    }

                    add(jframe, tabs);
                    break;

            }

            components++;
        }

        jframe.pack();
        return jframe;
    }

    private static final String[] BORDER_LAYOUTS = {BorderLayout.PAGE_START,
        BorderLayout.PAGE_END, BorderLayout.LINE_START, BorderLayout.LINE_END,
        BorderLayout.CENTER};

    public void add(JFrame jframe, JComponent jc){
        switch(LAYOUT) {
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

    public static Container getRandomComponent(){
        List<String> arrayList = Arrays.asList(RANDOM_WORDS);
        Collections.shuffle(arrayList);
        int components = 0;

        Container c = new Container();

        c.setLayout(new FlowLayout());

        c.setPreferredSize(new Dimension(100, 100));

        while (components < MIN_NUM_COMPONENTS || (components < MAX_NUMBER_COMPONENTS && random.nextDouble() < Math.pow(0.95, components))){
            String value = arrayList.get(components);
            switch(random.nextInt(7)){
                case 0: //button
                    JButton b = new JButton(value);
                    c.add(b);
                    break;
                case 1: //text field
                    if (random.nextDouble() < 0.3){
                        JTextArea jt = new JTextArea(value);
                        jt.setSize((MIN_WIDTH/3)+random.nextInt(MIN_WIDTH/3), (MIN_HEIGHT/3)+random.nextInt(MIN_HEIGHT/3));
                        c.add(jt);
                    } else {
                        JTextField jt = new JTextField(value);

                        c.add(jt);
                    }
                    break;
                case 2: //combo box
                    JComboBox jc = new JComboBox(RANDOM_WORDS);
                    jc.setSelectedIndex(random.nextInt(RANDOM_WORDS.length));
                    c.add(jc);
                    break;
                case 3: //tree
                    DefaultMutableTreeNode top =
                            new DefaultMutableTreeNode(value);
                    DefaultMutableTreeNode current = top;

                    List<String> sList = Arrays.asList(RANDOM_WORDS);

                    Collections.shuffle(sList);

                    for (int i = 0; i < arrayList.size(); i++){
                        DefaultMutableTreeNode n = new DefaultMutableTreeNode(sList.get(i));
                        double rand = random.nextDouble();

                        boolean changed = false;

                        current.add(n);

                        if (current != top){ // go up the tree
                            if (rand < 0.2){
                                current = current.getPreviousNode();
                                changed = true;
                            }
                        }

                        if (!changed){ // go down the tree
                            if (rand < 0.4){
                                current = n;
                            }
                        }
                    }
                    JTree tree = new JTree(top);

                    DefaultMutableTreeNode expand = top;
                    int crow = 0;
                    while(random.nextDouble() < 0.9 && expand.getChildCount() > 0) {
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
                    int listBeg = random.nextInt(arrayList.size()-1);
                    List<String> nList = arrayList.subList(listBeg, listBeg + random.nextInt(arrayList.size()-listBeg));
                    String[] sts = new String[nList.size()];
                    nList.toArray(sts);
                    JList list = new JList(sts);
                    list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                    JScrollPane listScroller = new JScrollPane(list);
                    c.add(listScroller);

                    if (random.nextDouble() < 0.5) {
                        int start = random.nextInt(10);
                        list.getSelectionModel().addSelectionInterval(start, start + random.nextInt(10));

                    }
                    if (listScroller.getSize().getHeight() > MIN_HEIGHT) {
                        listScroller.setSize((int) listScroller.getSize().getWidth(), MIN_HEIGHT / 3);
                    }


                    break;
                case 5: //radio buttons
                    if (random.nextDouble() < 0.5){
                        JRadioButton jrb = new JRadioButton(value);
                        c.add(jrb);
                        if (random.nextDouble() < 0.5){
                            jrb.setSelected(true);
                        }
                    } else {
                        JToggleButton jtb = new JToggleButton(value);
                        c.add(jtb);
                        if (random.nextDouble() < 0.5){
                            jtb.setSelected(true);
                        }
                    }
                    break;
                case 6: //tabs
                    JTabbedPane tabs = new JTabbedPane(TAB_PLACEMENTS[random.nextInt(TAB_PLACEMENTS.length)]);

                    int listTab = random.nextInt(arrayList.size()-21);
                    List<String> tList = arrayList.subList(listTab, listTab + random.nextInt(20));
                    int i = 0;
                    while (random.nextDouble() < 0.2 && i < tList.size()){
                        tabs.addTab(tList.get(i++), getRandomComponent());
                    }
                    c.add(tabs);
                    break;
            }

            components++;
        }

        return c;
    }

    private static int[] TAB_PLACEMENTS = {JTabbedPane.LEFT, JTabbedPane.TOP, JTabbedPane.RIGHT, JTabbedPane.BOTTOM};

    public static void main(String args[]){
        FrameGenerator fg = new FrameGenerator();

        JFrame jf = fg.generateRandomFrame();

        jf.setVisible(true);
    }

}
