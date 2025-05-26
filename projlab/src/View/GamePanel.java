package View;

import Game.Map.GTecton;
import Game.Map.Tecton;
import Player.GInsect;
import Player.Insect;
import Player.Mushroom.*;

import Player.Player;
import Spore.GSpore;
import Spore.Spore;
import javax.swing.*;
import java.awt.*;
import java.util.*;


public class GamePanel extends JPanel implements IGListener {
    private Map<String, Tecton> tectonMap = new HashMap<>();
    private Map<int[], Tecton> tectonPosMap = new HashMap<>();
    private Map<String, MushroomBody> bodies = new HashMap<>();
    private Map<String, MushroomYarn> yarns = new HashMap<>();
    private Map<String , Insect> insects = new HashMap<>();
    private  HashMap<Tecton, GTecton> tectonToGraphic = new HashMap<>();
    private  HashMap<MushroomYarn, GMushroomYarn> yarnToGraphic = new HashMap<>();
    private  HashMap<MushroomBody, GMushroomBody> bodyToGraphic = new HashMap<>();
    private  HashMap<Insect, GInsect> insectToGraphic = new HashMap<>();
    private  HashMap<Spore, GSpore> sporeToGraphic = new HashMap<>();
    private  Set<Point> occupied = new HashSet<>();
    private MushroomBody selectedMushroomBody;
    private Insect selectedInsect;
    private MushroomYarn selectedMushroomYarn;
    private Tecton selectedTecton;
    private Player currentPlayer;
    private int round;
    private final int width = 1280;
    private final int height = 720;
    private MainView mainView;
    public GamePanel(MainView mainVie) {
        setPreferredSize(new Dimension(width, height));
        setLayout(null);
        setBackground(Color.cyan);
        mainView = mainVie;
    }

    /**
     * Sets up the Tectons on the game panel.
     * Initializes their graphical representations and adds them to the panel.
     */
    public void tectonSetup() {

        if (tectonPosMap.isEmpty()) return;

        for (Map.Entry<int[], Tecton> entry : tectonPosMap.entrySet()) {
            int[] gridPos = entry.getKey();
            Tecton tecton = entry.getValue();

            int x = gridPos[0];
            int y = gridPos[1];
            Point pixelPos = new Point(x, y);
            occupied.add(pixelPos);
            GTecton gTecton = new GTecton(x, y, getTecNameFromMap(tectonMap, tecton), this);
            tectonToGraphic.put(tecton, gTecton);

            if (tecton.hasBody()) {
                bodySetup(tecton, pixelPos);
            }
            if (tecton.getSpores() != null) {
                sporeSetup(tecton, pixelPos);
            }
            if (tecton.getInsect() != null) {
                insectSetup(tecton, pixelPos);
            }

            add(gTecton);
        }
    }

    /**
     * Sets up the Mushroom Bodies on the game panel.
     *
     * @param current    The `Tecton` associated with the Mushroom Body.
     * @param currentPos The position of the Mushroom Body.
     */
    private void bodySetup(Tecton current, Point currentPos) {
        GMushroomBody currentBody = new GMushroomBody(currentPos.x, currentPos.y, getBodNameFromMap(bodies, current.getBody()),this, current.getBody().getOwner().getColor());
        currentBody.setBounds(currentPos.x+25, currentPos.y, 25, 25);
        add(currentBody);
        bodyToGraphic.put(current.getBody(), currentBody);
    }

    /**
     * Sets up the Mushroom Yarns on the game panel.
     * Initializes their graphical representations and adds them to the panel.
     */
    public void yarnSetup()
    {
        if (yarns.isEmpty()) return;

        for (Map.Entry<String, MushroomYarn> entry : yarns.entrySet()) {

            MushroomYarn yarn = entry.getValue();

            Tecton source = yarn.getSourceTecton();
            Tecton target = yarn.getTargetTecton();

            // Get graphical components from the tecton-to-graphic map
            GTecton gSource = tectonToGraphic.get(source);
            GTecton gTarget = tectonToGraphic.get(target);

            if (gSource == null || gTarget == null) continue; // Skip if either is missing

            int x1 = gSource.getX();
            int y1 = gSource.getY();
            int x2 = gTarget.getX();
            int y2 = gTarget.getY();

            GMushroomYarn gYarn = new GMushroomYarn(x1, y1, x2, y2, getYarnNameFromMap(yarns,yarn), this, yarn.getBody().getOwner().getColor());
            gYarn.setBounds(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2) + 50, Math.abs(y1 - y2) + 50);

            yarnToGraphic.put(yarn, gYarn);
            add(gYarn);
        }

    }
    /**
     * Sets up the Insects on the game panel.
     *
     * @param current    The `Tecton` associated with the Insect.
     * @param currentPos The position of the Insect.
     */
    public void insectSetup(Tecton current, Point currentPos)
    {
        GInsect currentGInsect = new GInsect(currentPos.x, currentPos.y, getInsectNameFromMap(insects, current.getInsect()),this, current.getInsect().getOwner().getColor());
        currentGInsect.setBounds(currentPos.x + 25, currentPos.y+25, 25, 25);
        add(currentGInsect);
        insectToGraphic.put(current.getInsect(), currentGInsect);
    }


    /**
     * Sets up the Spores on the game panel.
     *
     * @param current    The `Tecton` associated with the Spores.
     * @param currentPos The position of the Spores.
     */
    public void sporeSetup(Tecton current, Point currentPos)
    {
        for (Spore spore : current.getSpores()) {
            GSpore currentGSpore = new GSpore(currentPos.x, currentPos.y, ""+current.getSpores().size(), spore);
            currentGSpore.setBounds(currentPos.x, currentPos.y, 20, 20);
            add(currentGSpore);
            sporeToGraphic.put(spore, currentGSpore);
        }

    }
    public void draw()
    {
        if(tectonMap.isEmpty())
        {
           // mainView.handleCommand("/load -p D:/BME/PROJLAB/projlab/projlab/TestFiles/Map.txt");
        }
        else {
            removeAll();
            yarnToGraphic.clear();
            bodyToGraphic.clear();
            insectToGraphic.clear();
            sporeToGraphic.clear();
            occupied.clear();
            tectonToGraphic.clear();

        }

        tectonMap = mainView.getTectons();
        bodies = mainView.getBodies();
        yarns = mainView.getYarns();
        insects = mainView.getInsects();
        tectonPosMap = mainView.getTectonPosMap();
        tectonSetup();
        yarnSetup();
        createSideBar();

    }
    /**
     * Paints the game panel, including the grid and connections between Tectons.
     *
     * @param g The `Graphics` object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(
                1,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL,
                0,
                new float[]{5},
                0
        ));
        //drawGridandCells(g);

        Set<String> drawnLines = new HashSet<>();
        for (Map.Entry<Tecton, GTecton> entry : tectonToGraphic.entrySet()) {
            Tecton t1 = entry.getKey();
            GTecton g1 = entry.getValue();
            Point p1 = new Point(g1.getX() + g1.getWidth() / 2, g1.getY() + g1.getHeight() / 2);

            for (Tecton t2 : t1.getNeighbours()) {
                GTecton g2 = tectonToGraphic.get(t2);
                if (g2 == null) continue;

                String key = System.identityHashCode(t1) < System.identityHashCode(t2)
                        ? t1.hashCode() + "-" + t2.hashCode()
                        : t2.hashCode() + "-" + t1.hashCode();
                if (drawnLines.contains(key)) continue;

                boolean sharesYarn = yarns.values().stream().anyMatch(y ->
                        (y.getSourceTecton() == t1 && y.getTargetTecton() == t2) ||
                                (y.getSourceTecton() == t2 && y.getTargetTecton() == t1)
                );
                if (sharesYarn) continue;

                drawnLines.add(key);
                Point p2 = new Point(g2.getX() + g2.getWidth() / 2, g2.getY() + g2.getHeight() / 2);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        g2d.dispose();
    }

    private void drawGridandCells(Graphics g) {
        int cellSize = 50;
        int rows = getHeight() / cellSize;
        int cols = getWidth() / cellSize;

        g.setColor(Color.LIGHT_GRAY);

// Draw horizontal grid lines
        for (int i = 0; i <= rows; i++) {
            g.drawLine(0, i * cellSize, cols * cellSize, i * cellSize);
        }

// Draw vertical grid lines
        for (int j = 0; j <= cols; j++) {
            g.drawLine(j * cellSize, 0, j * cellSize, rows * cellSize);
        }

// Mark the top-left corner (pixel coordinates)
        g.setColor(Color.RED);
        g.drawString("Top-left: (0, 0)", 5, 15); // 5px right, 15px down from top-left

// Optionally mark each grid cell with its top-left pixel position

        g.setColor(Color.BLUE);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = j * cellSize;
                int y = i * cellSize;
                g.drawString("(" + x + "," + y + ")", x + 2, y + 12);
            }
        }
    }

    private String getTecNameFromMap(Map<String, Tecton> map, Tecton value) {
        for (Map.Entry<String, Tecton> entry : map.entrySet()) {
            if (entry.getValue() == value) return entry.getKey();
        }
        return "?";
    }

    private String getBodNameFromMap(Map<String, MushroomBody> map, MushroomBody value) {
        for (Map.Entry<String, MushroomBody> entry : map.entrySet()) {
            if (entry.getValue() == value) return entry.getKey();
        }
        return "?";
    }
    private String getYarnNameFromMap(Map<String, MushroomYarn> map, MushroomYarn value) {
        for (Map.Entry<String, MushroomYarn> entry : map.entrySet()) {
            if (entry.getValue() == value) return entry.getKey();
        }
        return "?";
    }

    private String getInsectNameFromMap(Map<String, Insect> map, Insect value) {
        for (Map.Entry<String, Insect> entry : map.entrySet()) {
            if (entry.getValue() == value) return entry.getKey();
        }
        return "?";
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    private JButton createUniformButton(String text, JPanel sideBar) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(sideBar.getWidth(), button.getPreferredSize().height));
        return button;
    }


    /**
     * Creates a sidebar with buttons and labels for user interaction.
     */
    public void createSideBar()
    {
        JPanel sideBar = new JPanel();
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.setBackground(Color.LIGHT_GRAY);
        sideBar.setBounds(0, 0, 150, height);
        JLabel label;
        JLabel nameLabel;
        JLabel scoreLabel;
        JButton button1;
        JButton button2;
        JButton button3;
        JButton button4;
        if(currentPlayer != null) {
            label = new JLabel(round + ".- round - " );
             nameLabel = new JLabel(currentPlayer.getName(), SwingConstants.LEFT);
            try {
                Color playerColor = Color.decode(currentPlayer.getColor());
                nameLabel.setForeground(playerColor);
            } catch (Exception e) {
                nameLabel.setForeground(Color.BLACK); // fallback if color is invalid
            }
            if(currentPlayer.getInsects() != null) {
                button1 = createUniformButton("Move Insect", sideBar);
                button2 = createUniformButton("Eat Spore", sideBar);
                button3 = createUniformButton("Cut yarn", sideBar);
                button1.addActionListener(e ->{
                    if(selectedInsect != null && selectedTecton != null && selectedInsect.getOwner().equals(currentPlayer)) {
                        mainView.moveInsect(getInsectNameFromMap(insects,selectedInsect),getTecNameFromMap(tectonMap, selectedTecton));
                    }
                    else {
                        System.out.println("No insect or tecton selected");
                    }
                });
                button2.addActionListener(e -> {
                    if(selectedInsect != null && selectedInsect.getOwner().equals(currentPlayer)) {
                        mainView.eatSpore(getInsectNameFromMap(insects,selectedInsect));
                    }
                    else {
                        System.out.println("No insect selected");
                    }
                });
                button3.addActionListener(e -> {
                    if(selectedInsect!=null && selectedMushroomYarn != null  && selectedInsect.getOwner().equals(currentPlayer)) {
                        mainView.cutYarn(getInsectNameFromMap(insects,selectedInsect),getYarnNameFromMap(yarns,selectedMushroomYarn));
                    }
                    else {
                        System.out.println("No insect or yarn selected");
                    }
                });
                sideBar.add(Box.createVerticalStrut(5));
                sideBar.add(label);
                sideBar.add(Box.createVerticalStrut(5));
                sideBar.add(nameLabel);

                sideBar.add(Box.createVerticalStrut(5));
                sideBar.add(button1);
                sideBar.add(Box.createVerticalStrut(5));
                sideBar.add(button2);
                sideBar.add(Box.createVerticalStrut(5));
                sideBar.add(button3);
                sideBar.add(Box.createVerticalStrut(5));
            }
            else
            {
                button1 = createUniformButton("Grow Yarn", sideBar);
                button2 = createUniformButton("Grow Mushroom", sideBar);
                button3 = createUniformButton("Release Spore", sideBar);
                button4 = createUniformButton("Eat Insect", sideBar);

                button1.addActionListener(e ->{
                    if(selectedMushroomBody != null && selectedTecton != null  && selectedMushroomBody.getOwner().equals(currentPlayer)) {
                        mainView.growYarn(getBodNameFromMap(bodies,selectedMushroomBody),getTecNameFromMap(tectonMap, selectedTecton));
                    }
                    else {
                        System.out.println("No mushroom body or tecton selected");
                    }
                });
                button2.addActionListener(e -> {
                    if (selectedTecton != null )
                    {
                        mainView.growBody(getTecNameFromMap(tectonMap, selectedTecton));
                    }
                    else {
                        System.out.println("No tecton selected");
                    }
                });
                button3.addActionListener(e -> {
                    if(selectedMushroomBody != null && selectedTecton != null  && selectedMushroomBody.getOwner().equals(currentPlayer)) {
                        JDialog sporeTypeDialog = new JDialog();
                        sporeTypeDialog.setTitle("Choose Spore Type");
                        sporeTypeDialog.setLayout(new GridLayout(5, 1));
                        sporeTypeDialog.setSize(300, 200);
                        sporeTypeDialog.setLocationRelativeTo(null);

                        JButton acceleratorButton = new JButton("Accelerator Spore (Blue)");
                        JButton fissionButton = new JButton("Fission Spore (Red)");
                        JButton paralysingButton = new JButton("Paralysing Spore (Orange)");
                        JButton weakeningButton = new JButton("Weakening Spore (Yellow)");
                        JButton slowingButton = new JButton("Slowing Spore (Green)");

                        acceleratorButton.addActionListener(sporeEvent -> {
                            mainView.releaseSpore(getBodNameFromMap(bodies, selectedMushroomBody), "a",getTecNameFromMap(tectonMap, selectedTecton) );
                            sporeTypeDialog.dispose();
                        });
                        fissionButton.addActionListener(sporeEvent -> {
                            mainView.releaseSpore(getBodNameFromMap(bodies, selectedMushroomBody), "f",getTecNameFromMap(tectonMap, selectedTecton) );
                            sporeTypeDialog.dispose();
                        });
                        paralysingButton.addActionListener(sporeEvent -> {
                            mainView.releaseSpore(getBodNameFromMap(bodies, selectedMushroomBody), "p",getTecNameFromMap(tectonMap, selectedTecton) );
                            sporeTypeDialog.dispose();
                        });
                        weakeningButton.addActionListener(sporeEvent -> {
                            mainView.releaseSpore(getBodNameFromMap(bodies, selectedMushroomBody), "w",getTecNameFromMap(tectonMap, selectedTecton) );
                            sporeTypeDialog.dispose();
                        });
                        slowingButton.addActionListener(sporeEvent -> {
                            mainView.releaseSpore(getBodNameFromMap(bodies, selectedMushroomBody), "s",getTecNameFromMap(tectonMap, selectedTecton) );
                            sporeTypeDialog.dispose();
                        });

                        sporeTypeDialog.add(acceleratorButton);
                        sporeTypeDialog.add(fissionButton);
                        sporeTypeDialog.add(paralysingButton);
                        sporeTypeDialog.add(weakeningButton);
                        sporeTypeDialog.add(slowingButton);

                        sporeTypeDialog.setModal(true);
                        sporeTypeDialog.setVisible(true);
                    }
                    else {
                        System.out.println("No mushroom body or tecton selected");
                    }
                });
                button4.addActionListener(e -> {
                    if(selectedMushroomYarn!=null  && selectedMushroomYarn.getBody().getOwner().equals(currentPlayer))
                    {
                        mainView.eatInsectYarn(getYarnNameFromMap(yarns,selectedMushroomYarn));
                    }
                    else {
                        System.out.println("No mushroom yarn selected");
                    }
                });
                sideBar.add(Box.createVerticalStrut(5));
                sideBar.add(label);
                sideBar.add(Box.createVerticalStrut(5));
                sideBar.add(nameLabel);
                sideBar.add(Box.createVerticalStrut(5));
                sideBar.add(button1);
                sideBar.add(Box.createVerticalStrut(5));
                sideBar.add(button2);
                sideBar.add(Box.createVerticalStrut(5));
                sideBar.add(button3);
                sideBar.add(Box.createVerticalStrut(5));
                sideBar.add(button4);
                sideBar.add(Box.createVerticalStrut(5));
            }
            scoreLabel = new JLabel("Score: " + currentPlayer.getScore());

        }
        else
        {
            label = new JLabel("No player");
            scoreLabel = new JLabel("Score: 0");
            sideBar.add(label);
        }
        JButton button5 = createUniformButton("Next Player", sideBar);
        button5.addActionListener(e -> mainView.triggerEvent(false,true));
        JButton button6 = createUniformButton("Help", sideBar);
        button6.addActionListener(e ->printHelp());
        sideBar.add(button5);
        sideBar.add(Box.createVerticalStrut(5));
        sideBar.add(scoreLabel);
        sideBar.add(Box.createVerticalStrut(5));
        sideBar.add(button6);
        add(sideBar);
    }

    private void printHelp() {
        String helpText = """
        Segítség:
        Gombász:
        - Grow Yarn - Egy gombatest és egy tektont kiválasztva az adott gombatest 
        megpróbál fonalat növeszteni a kijelölt tektonra.
        - Grow Mushroom - Egy tektont kiválasztva az adott tektonra gombatest nőhet, 
        ha adottak a feltételek (Van a tektonon legalább 2 spóra, nőhet gombatest a tektonon, nincs még gombatest a tektonon)
        - Release Spore - Egy gombatestet és egy tektont kiválasztva az adott gombatest megpróbál 
        spórát kiadni a kijelölt tektonra. A spóra típusát egy felugró ablakból lehet kiválasztani - Fontos, egy tektonon csak ugyanolyan típusú spórák lehetnek!
        - Eat Insect - Egy fonalat kiválasztva a gombász megpróbálja megenni a rajta lévő rovart. 
        
        Rovarász:
        - Move Insect - Egy rovart és egy tektont kiválasztva a rovar megpróbál átmozogni.
        - Eat Spore - Egy rovart kiválasztva a rovar megpróbálja megenni a tektonján lévő spórát.
        - Cut Yarn - Egy rovart és egy fonalat kiválasztva a rovar megpróbálja elvágni a fonalat.
        
        Általános:
        - Next Player - Kör befejezése
        - Minden entitásnak (Gombatest, Rovar) alapértelmezetten 2 akciópontja van egy körben.
        - Minden akció 1 akciópontba kerül.
        
        Cél:
        - A gombászok gombatestek lerakásával kapnak pontot, míg a rovarászok spórák elfogyasztásával.
        - A beállításokban beállított pontszám elérése után a játék véget ér. (Alapértelmezetten 50). 
        """;

        JOptionPane.showMessageDialog(null, helpText, "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    public void removeYarn(MushroomYarn key) {
        GMushroomYarn gYarn = yarnToGraphic.get(key);
        if (gYarn != null) {
            remove(gYarn);
            yarnToGraphic.remove(key);
            Update();
        }
    }

    public void updateYarnConnectionState(MushroomYarn yarn) {
        GMushroomYarn gYarn = yarnToGraphic.get(yarn);
        if (gYarn != null) {
            gYarn.setConnectedToMushroom(yarn.isConnectedToMushroom());
        }
    }

    void Update()
    {
        repaint();
        revalidate();
        draw();
    }

    public void playerChanged(Player currentPlayer, int i) {
        this.currentPlayer = currentPlayer;
        round = i;
        Update();
       // System.out.println("Current player: " + currentPlayer.getName());
        // Update the UI or perform any other actions based on the current player
    }
    public void selectMushroomBody(GMushroomBody clickedBody) {
        boolean alreadySelected = (selectedMushroomBody != null &&
                bodyToGraphic.get(selectedMushroomBody) == clickedBody);

        // Deselect all other yarns
        for (GMushroomBody gBodyEntry : bodyToGraphic.values()) {
            if (gBodyEntry.isSelected()) {
                gBodyEntry.setSelected(false);
            }
        }

        if (!alreadySelected) {
            clickedBody.setSelected(true);
            selectedMushroomBody = null;
            // Model and view synchronization
            for (Map.Entry<MushroomBody, GMushroomBody> entry : bodyToGraphic.entrySet()) {
                if (entry.getValue() == clickedBody) {
                    selectedMushroomBody = entry.getKey();
                    break;
                }
            }
        } else {
            selectedMushroomBody = null;
        }

        repaint();
    }

    public void selectInsect(GInsect clickedInsect) {
        boolean alreadySelected = (selectedInsect != null &&
                insectToGraphic.get(selectedInsect) == clickedInsect);

        // Deselect all other insects
        for (GInsect gInsectEntry : insectToGraphic.values()) {
            if (gInsectEntry.isSelected()) {
                gInsectEntry.setSelected(false);
            }
        }

        if (!alreadySelected) {
            clickedInsect.setSelected(true);
            selectedInsect = null;
            // Model and view synchronization
            for (Map.Entry<Insect, GInsect> entry : insectToGraphic.entrySet()) {
                if (entry.getValue() == clickedInsect) {
                    selectedInsect = entry.getKey();
                    break;
                }
            }
        } else {
            selectedInsect = null;
        }

        repaint();
    }

    public void selectMushroomYarn(GMushroomYarn clickedMushroomYarn) {
        boolean alreadySelected = (selectedMushroomYarn != null &&
                yarnToGraphic.get(selectedMushroomYarn) == clickedMushroomYarn);

        // Deselect all other yarns
        for (GMushroomYarn gYarnEntry : yarnToGraphic.values()) {
            if (gYarnEntry.isSelected()) {
                gYarnEntry.setSelected(false);
            }
        }

        if (!alreadySelected) {
            clickedMushroomYarn.setSelected(true);
            selectedMushroomYarn = null;
            // Model and view synchronization
            for (Map.Entry<MushroomYarn, GMushroomYarn> entry : yarnToGraphic.entrySet()) {
                if (entry.getValue() == clickedMushroomYarn) {
                    selectedMushroomYarn = entry.getKey();
                    break;
                }
            }
        } else {
            selectedMushroomYarn = null;
        }

        repaint();
    }

    public void selectTecton(GTecton clickedTecton) {
        boolean alreadySelected = (selectedTecton != null &&
                tectonToGraphic.get(selectedTecton) == clickedTecton);

        // Deselect all other tectons
        for (GTecton gTecton : tectonToGraphic.values()) {
            if (gTecton.isSelected()) {
                gTecton.setSelected(false);
            }
        }

        if (!alreadySelected) {
            clickedTecton.setSelected(true);
            selectedTecton = null;
            // Model and view synchronization
            for (Map.Entry<Tecton, GTecton> entry : tectonToGraphic.entrySet()) {
                if (entry.getValue() == clickedTecton) {
                    selectedTecton = entry.getKey();
                    break;
                }
            }
        } else {
            selectedTecton = null;
        }

        repaint();
    }


    public void onViewChanged()
    {
        Update();
    }


    public void gameOver(Player player) {
        JOptionPane.showMessageDialog(this, "Game Over! Player " + player.getName() + " wins!");
        System.exit(0);

    }

    @Override
    public void onGTecton(GTecton gTecton){
        selectTecton(gTecton);
    }
    @Override
    public void onGMushroomBody(GMushroomBody gmb){
        selectMushroomBody(gmb);
    }@Override
    public void onGMushroomYarn(GMushroomYarn gmy){
        selectMushroomYarn(gmy);
    }@Override
    public void onGInsect(GInsect gi){
        selectInsect(gi);
    }

}
