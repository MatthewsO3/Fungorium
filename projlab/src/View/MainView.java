package View;

import Controller.MainController;
import Game.Map.Tecton;
import Game.Map.DegradableTecton;
import Game.Map.LifewireTecton;
import Player.Insect;
import Player.Mushroom.MushroomBody;
import Player.Mushroom.MushroomYarn;
import Player.Entomologist;
import Player.Mycologist;
import Player.Player;
import Spore.Spore;
import Spore.AcceleratorSpore;
import Spore.FissionSpore;
import Spore.ParalysingSpore;
import Spore.WeakeningSpore;
import Spore.SlowingSpore;


import javax.swing.*;
import java.io.*;
import java.util.*;


/**
 * The `MainView` class serves as the main entry point for the game application.
 * It handles user input, command execution, and game state management.
 * This class also provides methods for adding, modifying, and listing game objects.
 */
public class MainView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    public GamePanel getGamePanel() {
        return gamePanel;
    }

    private GamePanel gamePanel;
    private Map<String, Tecton> tectons = Collections.synchronizedMap(new HashMap<>());
    private Map<int[], Tecton> tectonPos = Collections.synchronizedMap(new HashMap<>());
    private Map<String, MushroomBody> bodies = Collections.synchronizedMap(new HashMap<>());
    private Map<String, MushroomYarn> yarns = Collections.synchronizedMap(new HashMap<>());
    private Map<String, Insect> insects = new LinkedHashMap<>();
    private Map<String, Spore> spores = new LinkedHashMap<>();

    // Lists to store players of different types
    private List<Player> mycologists = new ArrayList<>();
    private List<Player> entomologists = new ArrayList<>();

    // Flag to enable or disable random behavior
    private boolean randomEnabled = true;

    // Console log to store messages for logging purposes
    private static List<String> consoleLog = new ArrayList<>();

    public static MainController getController() {
        return controller;
    }

    // Main controller for the game
    public static MainController controller;

    // Reader for handling console input
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public MainView( GamePanel gamePanel) {

        this.gamePanel = gamePanel;
        controller = new MainController(this);
    }
    /**
     * Reads a line of input from the console.
     *
     * @return The input line as a string, or null if an error occurs.
     */
    public String getInput() {
        String line = null;
        try
        {

            line = reader.readLine();
        }
        catch (IOException e)
        {

            println("[ERROR] Input error: " + e.getMessage());
        }
        return line;
    }

    /**
     * Processes a single command input by the user.
     *
     * @param input The command input as a string.
     */
    public void handleCommand(String input) {
        if (input.isEmpty()) return;

        // Split the input into command and arguments
        String[] parts = input.trim().split("\\s+");
        String cmd = parts[0].toLowerCase();
        Map<String, String> opts = new LinkedHashMap<>();
        List<String> args = new ArrayList<>();

        // Parse options and arguments
        for (int i = 1; i < parts.length; i++) {
            String p = parts[i];
            if (p.startsWith("-")) {
                String key = p.substring(1);
                String val = (i + 1 < parts.length && !parts[i + 1].startsWith("-")) ? parts[++i] : null;
                opts.put(key, val);
            } else {
                args.add(p);
            }
        }

        // Handle commands starting with "/"
        if (cmd.startsWith("/")) {
            cmd = cmd.substring(1);
            switch (cmd) {

                case "addt": addTecton(opts.get("n"), opts.get("t"), opts.get("c"), opts.get("m"), opts.get("x"), opts.get("y")); break;
                case "adde": addEntomologist(opts.get("i"), opts.get("n"), opts.get("c")); break;
                case "addm": addMycologist(opts.get("i"), opts.get("n"),opts.get("c")); break;
                case "addmb": addMushroomBody(opts.get("n"), opts.get("o"), opts.get("s")); break;
                case "addmy": addMushroomYarn(opts.get("n"), opts.get("b"), opts.get("s"), opts.get("t")); break;
                case "addi": addInsect(opts.get("n"), opts.get("o"), opts.get("s")); break;
                case "adds": addSpore(args.isEmpty() ? null : args.get(0), opts.get("t"), opts.get("c"), opts.get("s")); break;
                case "altt": altTecton(args.isEmpty() ? null : args.get(0), opts.get("c"), opts.get("b"), opts.get("m"), opts.get("n"), opts.get("i")); break;
                case "alti": altInsect(args.isEmpty() ? null : args.get(0), opts.get("c"), opts.get("s"), opts.get("t"), opts.get("o"), opts.get("m"), opts.get("a"), opts.get("e")); break;
                case "altmb": altBody(args.isEmpty() ? null : args.get(0), opts.get("l"), opts.get("c"), opts.get("a"), opts.get("s"), opts.get("d"), opts.get("g")); break;
                case "altmy": altMushroomYarn(args.isEmpty() ? null : args.get(0), opts.get("c"), opts.get("l")); break;
                case "save": saveGame(args.isEmpty() ? null : args.get(0)); break;
                case "load": loadGame(args.isEmpty() ? null : args.get(0), opts.get("s")); break;
                case "trig": triggerEvent(opts.containsKey("nr"), opts.containsKey("np")); break;
                case "rand": toggleRandom(opts.containsKey("d"), opts.containsKey("e")); break;
                case "list": listObjects(opts); break;
                case "help": printHelp(); break;
                case "log": writeLog(args.isEmpty() ? null : args.get(0)); break;
                case "start": MainController.StartGame(); break;
                case "exe": execute(args.isEmpty() ? null : args.get(0)); break;
                default: println("Unknown building command: /" + cmd);

            }
        } else {

            // Handle player commands
            switch (cmd) {
                case "cut": cutYarn(args.isEmpty() ? null : args.get(0), opts.get("my")); break;
                case "release": releaseSpore(args.isEmpty() ? null : args.get(0), opts.get("s"), opts.get("t")); break;
                case "eats": eatSpore(args.isEmpty() ? null : args.get(0)); break;
                case "eati": eatInsectYarn(args.isEmpty() ? null : args.get(0)); break;
                case "growy": growYarn(args.isEmpty() ? null : args.get(0), opts.get("t")); break;
                case "growb": growBody(args.isEmpty() ? null : args.get(0)); break;
                case "move": moveInsect(args.isEmpty() ? null : args.get(0), opts.get("t")); break;
                default: println("Unknown player command: " + cmd);
            }
        }
    }

    /**
     * Alters the properties of a MushroomBody object based on the provided parameters.
     *
     * @param s     The name of the MushroomBody to alter. If null, an error message is printed.
     * @param l     The new lifetime of the MushroomBody. If null, the lifetime is not changed.
     * @param c     The new spore released count of the MushroomBody. If null, the count is not changed.
     * @param a     The new action point value of the MushroomBody. If null, the value is not changed.
     * @param cool  The new spore cooldown value of the MushroomBody. If null, the cooldown is not changed.
     * @param dead  The new dead status of the MushroomBody. If null, the status is not changed.
     * @param g     The new fully grown status of the MushroomBody. If null, the status is not changed.
     */
    private void altBody(String s, String l, String c, String a, String cool, String dead, String g) {
        if (s == null) {
            println("altmb: missing [name]");
            return;
        }
        MushroomBody mb = bodies.get(s);
        if (mb == null) {
            println("MushroomBody not found: " + s);
            return;
        }
        if (l != null) {
            try {
                int lifeTime = Integer.parseInt(l);
                mb.setLifeTime(lifeTime);
            } catch (NumberFormatException e) {
                println("Invalid lifetime: " + l);
            }
        }
        if(c!=null)
        {
            try {
                int sporeReleasedCount = Integer.parseInt(c);
                mb.setSporeReleasedCount(sporeReleasedCount);
            } catch (NumberFormatException e) {
                println("Invalid spore released count: " + c);
            }
        }
        if(a != null)
        {
            try {
                int actionPoint = Integer.parseInt(a);
                mb.setActionPoint(actionPoint);
            } catch (NumberFormatException e) {
                println("Invalid action point: " + a);
            }
        }
        if(cool != null)
        {
            try {
                int sporeCooldowns = Integer.parseInt(cool);
                mb.setSporeCooldowns(sporeCooldowns);
            } catch (NumberFormatException e) {
                println("Invalid spore cooldowns: " + cool);
            }
        }
        if(dead != null)
        {
            try {
                boolean isDead = Boolean.parseBoolean(dead);
                mb.setIsDead(isDead);
            } catch (NumberFormatException e) {
                println("Invalid dead status: " + dead);
            }
        }
        if(g != null)
        {
            try {
                boolean isFullGrown = Boolean.parseBoolean(g);
                mb.setFullyGrown(isFullGrown);
            } catch (NumberFormatException e) {
                println("Invalid full grown status: " + g);
            }
        }

    }

    /**
     * Executes a series of commands from a specified file.
     *
     * This method reads a file line by line and processes each line as a command
     * using the `handleCommand` method. It is useful for batch processing of commands
     * from a script file.
     *
     * @param filename The name of the file containing the commands to execute.
     *                 If null, an error message is printed and the method returns.
     */
    private void execute(String filename) {
        if (filename == null) {
            println("exe: missing [filename]");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    handleCommand(line.trim());
                }
            }
        } catch (IOException e) {
            println("Error executing file: " + e.getMessage());
        }
    }

    /**
     * Adds a new Tecton object to the game.
     * <p>
     * This method creates a Tecton based on the specified type and configures its properties
     * such as whether it can grow a MushroomBody and the maximum number of MushroomYarns it can hold.
     * The created Tecton is then added to the `tectons` map with the given name as the key.
     *
     * @param name        The name of the Tecton to be added. Must not be null.
     * @param type        The type of the Tecton. Valid values are:
     *                    "d" for DegradableTecton, "l" for LifewireTecton, and "t" for a generic Tecton.
     * @param canGrowFlag A flag indicating whether the Tecton can grow a MushroomBody ("y" for yes, "n" for no).
     *                    If null, the property is not set.
     * @param maxYarnsStr The maximum number of MushroomYarns the Tecton can hold, as a string.
     *                    If null, the property is not set. Must be a valid integer if provided.
     * @param x
     * @param y
     */
    private  void addTecton(String name, String type, String canGrowFlag, String maxYarnsStr, String x, String y) {
        if (name == null || type == null) {
            println("addTecton: missing -n [name] or -t [type]");
            return;
        }
        Tecton t;
        switch (type.toLowerCase()) {
            case "d": t = new DegradableTecton(); break;
            case "l": t = new LifewireTecton(); break;
            case "t": t = new Tecton(); break;
            default:
                println("Invalid tecton type: " + type + " (use d, l, or t)");
                return;
        }
        if (canGrowFlag != null) {
            t.setCanGrowBody("y".equalsIgnoreCase(canGrowFlag));
        }
        if (maxYarnsStr != null) {
            try {
                int maxYarns = Integer.parseInt(maxYarnsStr);
                t.setMaxYarns(maxYarns);
            } catch (NumberFormatException e) {
                println("Invalid max yarns: " + maxYarnsStr);
                return;
            }
        }
        tectons.put(name, t);
        tectonPos.put(new int[]{Integer.parseInt(x), Integer.parseInt(y)}, t);
        //println("Added Tecton: " + name + " (Type: " + type + ")");
    }

    /**
     * Adds a new Entomologist to the game.
     * <p>
     * This method creates an Entomologist object using the provided ID and name,
     * and adds it to the list of entomologists. If the ID or name is null, an error
     * message is printed, and the method returns. If the ID is not a valid integer,
     * an error message is printed.
     *
     *
     * @param idStr The ID of the Entomologist as a string. Must be a valid integer.
     * @param name  The name of the Entomologist. Must not be null.
     * @param color The color associated with the Entomologist. Must not be null.
     */
    public  void addEntomologist( String idStr, String name, String color) {
        if (idStr == null || name == null || color == null) {
            println("addEntomologist: missing -i [id] or -n [name]");
            return;
        }
        try {
            int id = Integer.parseInt(idStr);
            Entomologist e = new Entomologist(id, name, color);
            entomologists.add(e);
           // println("Added Entomologist: " + name + " (ID: " + id + ")");
        } catch (NumberFormatException ex) {
            println("Invalid entomologist ID: " + idStr);
        }
    }

    /**
     * Adds a new Mycologist to the game.
     * <p>
     * This method creates a Mycologist object using the provided ID and name,
     * and adds it to the list of mycologists. If the ID or name is null, an error
     * message is printed, and the method returns. If the ID is not a valid integer,
     * an error message is printed.
     *
     * @param idStr The ID of the Mycologist as a string. Must be a valid integer.
     * @param name  The name of the Mycologist. Must not be null.
     * @param c    The color associated with the Mycologist. Must not be null.
     */
    public  void addMycologist(String idStr, String name, String c) {
        if (idStr == null || name == null || c == null) {
            println("addMycologist: missing -i [id] or -n [name]");
            return;
        }
        try {
            int id = Integer.parseInt(idStr);
            Mycologist m = new Mycologist(id, name,c);
            mycologists.add(m);
           // println("Added Mycologist: " + name + " (ID: " + id + ")");
        } catch (NumberFormatException ex) {
            println("Invalid mycologist ID: " + idStr);
        }
    }

    /**
     * Adds a new MushroomBody to the game.
     *
     * This method creates a MushroomBody object using the provided name, mycologist ID,
     * and tecton name. It associates the MushroomBody with the specified Mycologist
     * and Tecton, and stores it in the `bodies` map. If any of the required parameters
     * are null, or if the Mycologist or Tecton cannot be found, an error message is printed.
     *
     * @param name       The name of the MushroomBody to be added. Must not be null.
     * @param mycIdStr   The ID of the Mycologist as a string. Must be a valid integer.
     * @param tectonName The name of the Tecton where the MushroomBody will be placed. Must not be null.
     */
    private  void addMushroomBody(String name, String mycIdStr, String tectonName) {
        if (name == null || mycIdStr == null || tectonName == null) {
            println("addMushroomBody: missing -n [name], -o [mycologist id], or -s [tecton]");
            return;
        }
        try {
            int mycId = Integer.parseInt(mycIdStr);
            Mycologist myc = findMycologist(mycId);
            Tecton t = tectons.get(tectonName);
            if (myc == null) {
                println("Mycologist not found: " + mycId);
                return;
            }
            if (t == null) {
                println("Tecton not found: " + tectonName);
                return;
            }
            MushroomBody body = new MushroomBody(myc, t);
            myc.addMushroomBody(body);
            t.setBody(body);
            bodies.put(name, body);
         //   println("Added MushroomBody: " + name);
        } catch (NumberFormatException ex) {
            println("Invalid mycologist ID: " + mycIdStr);
        }
    }

    /**
     * Adds a new MushroomYarn to the game.
     *
     * This method creates a MushroomYarn object using the provided name, associated MushroomBody,
     * source Tecton, and target Tecton. It validates the existence of the specified body, source,
     * and target Tectons before creating the yarn. If any of the required parameters are null,
     * or if the specified body or Tectons cannot be found, an error message is printed.
     *
     * @param name        The name of the MushroomYarn to be added. Must not be null.
     * @param bodyName    The name of the MushroomBody associated with the yarn. Must not be null.
     * @param sourceName  The name of the source Tecton. Must not be null.
     * @param targetName  The name of the target Tecton. Must not be null.
     */
    private  void addMushroomYarn(String name, String bodyName, String sourceName, String targetName) {
        if (name == null || bodyName == null || sourceName == null || targetName == null) {
            println("addMushroomYarn: missing -n [name], -b [body], -s [source], or -t [target]");
            return;
        }
        MushroomBody body = bodies.get(bodyName);
        Tecton src = tectons.get(sourceName);
        Tecton tgt = tectons.get(targetName);
        if (body == null) {
            println("MushroomBody not found: " + bodyName);
            return;
        }
        if (src == null) {
            println("Source Tecton not found: " + sourceName);
            return;
        }
        if (tgt == null) {
            println("Target Tecton not found: " + targetName);
            return;
        }
        MushroomYarn yarn = new MushroomYarn(body, tgt, src,true);

        yarns.put(name, yarn);
       // println("Added MushroomYarn: " + name);
    }

    /**
     * Adds a new Insect to the game.
     *
     * This method creates an Insect object using the provided name, entomologist ID,
     * and tecton name. It validates the existence of the specified entomologist
     * and tecton before creating the insect. If any of the required parameters are null,
     * or if the specified entomologist or tecton cannot be found, an error message is printed.
     *
     * @param name        The name of the Insect to be added. Must not be null.
     * @param entIdStr    The ID of the Entomologist as a string. Must be a valid integer.
     * @param tectonName  The name of the Tecton where the Insect will be placed. Must not be null.
     */
    private  void addInsect(String name, String entIdStr, String tectonName) {
        if (name == null || entIdStr == null || tectonName == null) {
            println("addInsect: missing -n [name], -o [entomologist id], or -s [tecton]");
            return;
        }
        try {
            int entId = Integer.parseInt(entIdStr);
            Entomologist e = findEntomologist(entId);
            Tecton t = tectons.get(tectonName);
            if (e == null) {
                println("Entomologist not found: " + entId);
                return;
            }
            if (t == null) {
                println("Tecton not found: " + tectonName);
                return;
            }
            new Insect(e, t);
         //   println("Added Insect: " + name);
        } catch (NumberFormatException ex) {
            println("Invalid entomologist ID: " + entIdStr);
        }
    }

    /**
     * Adds a new Spore to the game.
     *
     * This method creates a Spore object based on the specified type and assigns it to a Tecton.
     * The Spore is then added to the `spores` map and the specified Tecton. If any of the required
     * parameters are null, or if the Tecton cannot be found, an error message is printed.
     * If the calorie value is not a valid integer, an error message is printed.
     *
     * @param name        The name of the Spore to be added. Must not be null.
     * @param typeCode    The type of the Spore. Valid values are:
     *                    "a" for AcceleratorSpore, "f" for FissionSpore,
     *                    "p" for ParalysingSpore, "w" for WeakeningSpore,
     *                    and "s" for SlowingSpore.
     * @param calStr      The calorie value of the Spore as a string. Must be a valid integer.
     * @param tectonName  The name of the Tecton where the Spore will be placed. Must not be null.
     */
    private  void addSpore(String name, String typeCode, String calStr, String tectonName) {
        if (name == null || typeCode == null || calStr == null || tectonName == null) {
            println("addSpore: missing [name], -t [type], -c [calories], or -s [tecton]");
            return;
        }
        try {
            int cal = Integer.parseInt(calStr);
            Tecton t = tectons.get(tectonName);
            if (t == null) {
                println("Tecton not found: " + tectonName);
                return;
            }
            Spore spore;
            switch (typeCode.toLowerCase()) {
                case "a": spore = new AcceleratorSpore(cal, t); break;
                case "f": spore = new FissionSpore(cal, t); break;
                case "p": spore = new ParalysingSpore(cal, t); break;
                case "w": spore = new WeakeningSpore(cal, t); break;
                case "s": spore = new SlowingSpore(cal, t); break;
                default:
                    println("Unknown spore type: " + typeCode + " (use a, f, p, w, or s)");
                    return;
            }
            t.addSporeToTecton(spore);
            spores.put(name, spore);
           // println("Added Spore: " + name);
        } catch (NumberFormatException ex) {
            println("Invalid calorie value: " + calStr);
        }
    }

    /**
     * Alters the properties of a Tecton object based on the provided parameters.
     *
     * This method updates the specified Tecton with new properties such as its ability
     * to grow a MushroomBody, the associated MushroomBody, the maximum number of MushroomYarns,
     * neighboring Tectons, and the insect movement count. If any of the provided parameters
     * are invalid or the Tecton cannot be found, appropriate error messages are printed.
     *
     * @param name          The name of the Tecton to alter. Must not be null.
     * @param canGrowFlag   A flag indicating whether the Tecton can grow a MushroomBody ("y" for yes, "n" for no).
     *                      If null, this property is not updated.
     * @param bodyName      The name of the MushroomBody to associate with the Tecton. If null, this property is not updated.
     * @param maxYarnsStr   The maximum number of MushroomYarns the Tecton can hold, as a string.
     *                      If null, this property is not updated. Must be a valid integer if provided.
     * @param neighbourName The name of a neighboring Tecton to add. If null, this property is not updated.
     *                      The neighbor must not be the same as the Tecton being altered.
     * @param insectm       A flag indicating whether to increment the insect movement count for the Tecton.
     *                      If null, this property is not updated.
     */
    private  void altTecton(String name, String canGrowFlag, String bodyName, String maxYarnsStr, String neighbourName, String insectm) {
        if (name == null) {
            println("altTecton: missing [name]");
            return;
        }
        Tecton t = tectons.get(name);

        if (t == null) {
            println("Tecton not found: " + name);
            return;
        }
        if (canGrowFlag != null) {
            t.setCanGrowBody("y".equalsIgnoreCase(canGrowFlag));
        }
        if (bodyName != null) {
            MushroomBody mb = bodies.get(bodyName);
            if (mb != null) {
                t.setBody(mb);
            } else {
                println("MushroomBody not found: " + bodyName);
            }
        }
        if (maxYarnsStr != null) {
            try {
                int maxYarns = Integer.parseInt(maxYarnsStr);
                t.setMaxYarns(maxYarns);
            } catch (NumberFormatException e) {
                println("Invalid max yarns: " + maxYarnsStr);
            }
        }
        if (neighbourName != null) {
            Tecton neighbour = tectons.get(neighbourName);
            if (neighbour != null && neighbour != t) {
                t.addNeighbour(neighbour);
            } else {
                println("Invalid or same neighbour Tecton: " + neighbourName);
            }
        }
        if (insectm != null) {
            t.incInsectMoveThrough();
        }
       // println("Updated Tecton: " + name);
    }

    /**
     * Alters the properties of an Insect object based on the provided parameters.
     *
     * This method updates the specified Insect with new properties such as its ability
     * to cut MushroomYarns, its stunned status, the Tecton it is located on, its score,
     * maximum action points, current action points, and the remaining effect time.
     * If any of the provided parameters are invalid or the Insect cannot be found,
     * appropriate error messages are printed.
     *
     * @param name         The name of the Insect to alter. Must not be null.
     * @param canCutFlag   A flag indicating whether the Insect can cut MushroomYarns ("n" to disable cutting).
     *                     If null, this property is not updated.
     * @param stunnedFlag  A flag indicating whether the Insect is stunned ("y" to stun).
     *                     If null, this property is not updated.
     * @param tectonName   The name of the Tecton to move the Insect to. If null, this property is not updated.
     * @param score        The score to increment for the Insect's owner, as a string.
     *                     If null, this property is not updated. Must be a valid integer if provided.
     * @param maxAc        The maximum action points for the Insect, as a string.
     *                     If null, this property is not updated. Must be a valid integer if provided.
     * @param acP          The current action points for the Insect, as a string.
     *                     If null, this property is not updated. Must be a valid integer if provided.
     * @param effectTime   The remaining effect time for the Insect, as a string.
     *                     If null, this property is not updated. Must be a valid integer if provided.
     */
    private  void altInsect(String name, String canCutFlag, String stunnedFlag, String tectonName, String score, String maxAc, String acP, String effectTime) {
        if (name == null) {
            println("altInsect: missing [name]");
            return;
        }
        Insect i = insects.get(name);
        if (i == null) {
            println("Insect not found: " + name);
            return;
        }
        if (canCutFlag != null && "n".equalsIgnoreCase(canCutFlag)) {
            i.disableYarnCutting();
        }
        if (stunnedFlag != null && "y".equalsIgnoreCase(stunnedFlag)) {
            i.setStunned();
        }
        if (tectonName != null) {
            Tecton t = tectons.get(tectonName);
            if (t != null) {
                i.moveToTecton(t);
            } else {
                println("Tecton not found: " + tectonName);
            }
        }
        if(score != null)
        {
            try {
                int scoreValue = Integer.parseInt(score);
                i.getOwner().incScore(scoreValue);
            } catch (NumberFormatException e) {
                println("Invalid score: " + score);
            }
        }
        if(maxAc != null)
        {
            try {
                int maxActionPoint = Integer.parseInt(maxAc);
                i.setMaxActionPoint(maxActionPoint);

            } catch (NumberFormatException e) {
                println("Invalid max action point: " + maxAc);
            }
        }
        if(acP != null)
        {
            try {
                int actionPoint = Integer.parseInt(acP);
                i.setActionPoint(actionPoint);
            } catch (NumberFormatException e) {
                println("Invalid action point: " + acP);
            }
        }
        if(effectTime != null)
        {
            try {
                int effectRemainingTime = Integer.parseInt(effectTime);
                i.setEffectRemainingTime(effectRemainingTime);
            } catch (NumberFormatException e) {
                println("Invalid effect remaining time: " + effectTime);
            }
        }
    }

    /**
     * Alters the properties of a MushroomYarn object based on the provided parameters.
     *
     * This method updates the specified MushroomYarn with new properties such as its
     * connection status and lifetime. If the provided name is null or the MushroomYarn
     * cannot be found, appropriate error messages are printed. If the lifetime value
     * is invalid, an error message is also printed.
     *
     * @param name      The name of the MushroomYarn to alter. Must not be null.
     * @param connFlag  A flag indicating whether the MushroomYarn should be disconnected.
     *                  If not null, the MushroomYarn is set to disconnected.
     * @param lifeStr   The new lifetime of the MushroomYarn as a string. If not null,
     *                  it must be a valid integer.
     */
    private  void altMushroomYarn(String name, String connFlag, String lifeStr) {
        if (name == null) {
            println("altMushroomYarn: missing [name]");
            return;
        }
        MushroomYarn y = yarns.get(name);
        if (y == null) {
            println("Yarn not found: " + name);
            return;
        }
        if (connFlag != null) {
            y.setDisconnected();
        }
        if (lifeStr != null) {
            try {
                int lifeTime = Integer.parseInt(lifeStr);
                y.setLifeTime(lifeTime);
            } catch (NumberFormatException e) {
                println("Invalid lifetime: " + lifeStr);
            }
        }
        println("Updated MushroomYarn: " + name);
    }

    /**
     * Saves the current game state to a file.
     *
     * This method serializes the current game state into a file specified by the filename.
     * If the filename is null, an error message is printed, and the method returns.
     * If an I/O error occurs during the saving process, an error message is printed.
     * After successfully saving the game state, the application exits.
     *
     * @param filename The name of the file where the game state will be saved. Must not be null.
     */
    public void saveGame(String filename) {
        if (filename == null) {
            println("save: missing [filename]");
            return;
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            GameState state = new GameState(this);
            oos.writeObject(state);
            println("Saving to " + filename);
            System.exit(0);
        } catch (IOException e) {
            println("Error saving: " + e.getMessage());
        }


    }

    /**
     * Loads the game state from a file or executes commands from a script file.
     *
     * This method handles two modes of loading:
     * 1. If the `-s` option is provided, it deserializes the game state from a file.
     * 2. If the `-p` option is provided, it reads and executes commands from a script file.
     * If neither option is specified or the options are invalid, an error message is printed.
     *
     * @param opts A map of options where:
     *             - "s" specifies the filename for deserialization.
     *             - "p" specifies the filename for the script file.
     */
    public void loadGame(String filename, String opts) {
        //String filename = null;
        if (opts.isEmpty()) {
            println("load: missing [filename]");
            return;
        }
        if (opts.equals( "-s")) {
           // filename = opts.get("s");
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                GameState state = (GameState) ois.readObject();
                this.tectons = new LinkedHashMap<>(state.getTectons());
                this.bodies = new LinkedHashMap<>(state.getBodies());
                this.yarns = new LinkedHashMap<>(state.getYarns());
                this.insects = new LinkedHashMap<>(state.getInsects());
                this.spores = new LinkedHashMap<>(state.getSpores());
                this.mycologists = new ArrayList<>(state.getMycologists());
                this.entomologists = new ArrayList<>(state.getEntomologists());
                this.randomEnabled = state.isRandomEnabled();
                println("Loading " + filename);
            } catch (Exception e) {
                println("Error loading: " + e.getMessage());
            }
        } else if (opts.equals( "-p")) {
          //  filename = opts.get("p");
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String cmd;
                while ((cmd = reader.readLine()) != null) {
                    if (!cmd.trim().isEmpty()) {
                        handleCommand(cmd.trim());
                    }
                }
                println("Loaded " + filename);
            } catch (IOException e) {
                println("Error loading script: " + e.getMessage());
            }
        } else {
            println("load: specify -p (commands) or -s (serialization)");
        }
    }

    /**
     * Triggers specific game events based on the provided flags.
     *
     * This method allows advancing the game to the next round or switching to the next player.
     * If the `nextRound` flag is true, the game progresses to the next round.
     * If the `nextPlayer` flag is true, the game switches to the next player.
     *
     * @param nextRound  A boolean flag indicating whether to advance to the next round.
     * @param nextPlayer A boolean flag indicating whether to switch to the next player.
     */
    public  void triggerEvent(boolean nextRound, boolean nextPlayer) {
        if (nextRound) {
            MainController.NextRound();

        }
        if (nextPlayer) {
            MainController.NextPlayer(MainController.getCurrentPlayer());
        }
    }

    /**
     * Toggles the random behavior of the game.
     *
     * This method enables or disables random behavior based on the provided flags.
     * If the `disable` flag is true, random behavior is turned off.
     * If the `enable` flag is true, random behavior is turned on.
     * Appropriate messages are printed to indicate the current state.
     *
     * @param disable A boolean flag indicating whether to disable random behavior.
     * @param enable  A boolean flag indicating whether to enable random behavior.
     */
    private  void toggleRandom(boolean disable, boolean enable) {
        if (disable) {
            randomEnabled = false;
            println("Random disabled");
        }
        if (enable) {
            randomEnabled = true;
            println("Random enabled");
        }
    }

    /**
     * Lists various game objects based on the provided options.
     *
     * This method prints the names or details of game objects such as Tectons,
     * MushroomBodies, MushroomYarns, Spores, Insects, Entomologists, and Mycologists
     * depending on the keys present in the `opts` map. Each key corresponds to a
     * specific type of game object:
     * - "t": Lists all Tectons.
     * - "mb": Lists all MushroomBodies.
     * - "my": Lists all MushroomYarns.
     * - "s": Lists all Spores grouped by their associated Tectons.
     * - "i": Lists all Insects.
     * - "ent": Lists all Entomologists.
     * - "myc": Lists all Mycologists.
     *
     * @param opts A map of options where each key specifies the type of game object to list.
     */
    private  void listObjects(Map<String, String> opts) {
        if (opts.containsKey("t")) {

            println("Tectons: " + tectons.keySet());
        }
        if (opts.containsKey("mb")) {
            println("Bodies: " + bodies.keySet());
        }
        if (opts.containsKey("my")) {
            println("Yarns: " + yarns.keySet());
        }
        if (opts.containsKey("s")) {
            println("Spores per tecton:");
            for (Map.Entry<String, Tecton> entry : tectons.entrySet()) {
                ArrayList<Spore> spores = entry.getValue().getSpores();
                println(entry.getKey() + ": " + (spores != null ? spores : "[]"));
            }
        }
        if (opts.containsKey("i")) {
            println("Insects: " + insects.keySet());
        }
        if (opts.containsKey("ent")) {
            println("Entomologists: " + getEntNames());
        }
        if (opts.containsKey("myc")) {
            println("Mycologists: " + getMycoNames());
        }
    }

    /**
     * Prints the help menu for the game commands.
     *
     * This method displays a list of available building and player commands
     * along with their respective syntax and options. It provides guidance
     * on how to use the commands to interact with the game.
     */
    private  void printHelp() {
        println("Building Commands:");
        println("/addt -n [name] -t [d|l|t] -c [y/n] -m [maxYarns]");
        println("/adde -i [id] -n [name]");
        println("/addm -i [id] -n [name]");
        println("/addmb -n [name] -o [mycologist id] -s [tecton]");
        println("/addmy -n [name] -b [body] -s [source] -t [target]");
        println("/addi -n [name] -o [entomologist id] -s [tecton]");
        println("/adds [name] -t [a|f|p|s|w] -c [calories] -s [tecton]");
        println("/altt [name] -c [y/n] -b [body] -m [maxYarns] -n [neighbour] -i [insectMoveThrough]");
        println("/alti [name] -c [y/n] -s [y/n] -t [tecton] -o [score] -m [maxActionPoint] -a [actionPoint] -e [effectRemainignTime]");
        println("/altmy [name] -c [y/n] -l [lifetime] ");
        println("/altmb [name] -l [lifetime] -c [sporeReleasedCount] -a [actionPoint] -s [sporeCooldowns] -d [dead] -g [fullGrown]");
        println("/save [file]");
        println("/load [-p|-s] [file] ");
        println("/trig [-nr|-np]");
        println("/rand [-d|-e]");
        println("/list [-t -mb -my -s -i -ent -myc]");
        println("/help");
        println("/log [file]");
        println("\nPlayer Commands:");
        println("cut [insect] -my [yarn]");
        println("release [body] -s [a|f|p|s|w] -t [tecton]");
        println("eats [insect]");
        println("eati [yarn]");
        println("growy [body] -t [tecton]");
        println("growb [tecton]");
        println("move [insect] -t [tecton]");
    }

    /**
     * Writes the console log to a specified file.
     *
     * This method iterates through the `consoleLog` list and writes each log entry
     * to the specified file. If the filename is null, an error message is printed.
     * If an I/O error occurs during the writing process, an error message is printed.
     *
     * @param filename The name of the file where the log will be written. Must not be null.
     */
    private  void writeLog(String filename) {
        if (filename == null) {
            println("log: missing [filename]");
            return;
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (String line : consoleLog) {
                pw.println(line);
            }
            println("Log written to " + filename);
        } catch (IOException e) {
            println("Error writing log: " + e.getMessage());
        }
    }

    /**
     * Cuts a specified MushroomYarn using an Insect.
     *
     * This method attempts to cut a MushroomYarn identified by its name using an Insect
     * identified by its name. It validates the existence of both the Insect and the MushroomYarn
     * before proceeding. If the cut operation is successful, it logs the change in the Insect's
     * action points.
     *
     * @param insectName The name of the Insect attempting to cut the MushroomYarn. Must not be null.
     * @param yarnName   The name of the MushroomYarn to be cut. Must not be null.
     */
    public void cutYarn(String insectName, String yarnName) {

        if (insectName == null || yarnName == null) {
            println("cut: missing [insect] or -my [yarn]");
            return;
        }
        Insect i = insects.get(insectName);
        int currPoint = i.getActionPoint();
        MushroomYarn y = yarns.get(yarnName);
        boolean isCon = y.isConnectedToMushroom();
        if (i == null) {
            println("Insect not found: " + insectName);
            return;
        }
        if (y == null) {
            println("Yarn not found: " + yarnName);
            return;
        }
        if(i.cutYarn(y))
        {
            for (Map.Entry<String, Insect> entry : insects.entrySet()) {
                if (entry.getValue().equals(i)) {
                    println("["+entry.getKey() + "]: " +  "actionPoint: "+ currPoint + " -> " + (i.getActionPoint()));
                    break;
                }
            }
        }
    }

    /**
     * Releases a spore from a specified MushroomBody to a specified Tecton.
     *
     * This method attempts to release a spore of the given type from the specified MushroomBody
     * to the specified Tecton. It validates the existence of the MushroomBody and Tecton before
     * proceeding. If the release is successful, it logs the changes in the MushroomBody's spore
     * released count and action points, as well as the addition of the spore to the Tecton.
     * If the release fails, an appropriate error message is printed.
     *
     * @param bodyName   The name of the MushroomBody releasing the spore. Must not be null.
     * @param typeCode   The type of the spore to release. Must not be null.
     * @param tectonName The name of the Tecton where the spore will be released. Must not be null.
     */
    public void releaseSpore(String bodyName, String typeCode, String tectonName) {
        if (bodyName == null || typeCode == null || tectonName == null) {
            println("release: missing [body], -s [type], or -t [tecton]");
            return;
        }
        MushroomBody mb = bodies.get(bodyName);
        Tecton t = tectons.get(tectonName);
        if (mb == null) {
            println("Body not found: " + bodyName);
            return;
        }
        if (t == null) {
            println("Tecton not found: " + tectonName);
            return;
        }
        int oldc = mb.getSporeReleasedCount();
        int olda = mb.getActionPoint();
        if(mb.releaseSpore(t, typeCode.toLowerCase()))
        {
            println("["+ bodyName + "]: sporeReleasedCount: " + oldc + " -> " + (mb.getSporeReleasedCount()));
            println("["+ bodyName + "]: actionPoint: " + olda + " -> " + (mb.getActionPoint()));
            for (Map.Entry<String, Tecton> entry : tectons.entrySet()) {
                if (entry.getValue().equals(t)) {
                    println("["+entry.getKey() + "]: spores " +  "added: [spore" + (t.getSpores().size()+1) + "]");
                    break;
                }
            }

        }
        else
        {
            println("["+ bodyName + "]: Can't release spore");
        }

    }

    /**
     * Allows an Insect to eat a Spore and updates its properties accordingly.
     *
     * This method checks if the specified Insect exists and attempts to make it eat a Spore.
     * If successful, it logs changes to the Insect's action points, maximum action points,
     * effect remaining time, stunned status, and its ability to cut MushroomYarns.
     * If the Insect is not found or the name is null, an appropriate error message is printed.
     *
     * @param insectName The name of the Insect attempting to eat the Spore. Must not be null.
     */
    public void eatSpore(String insectName) {
        if (insectName == null) {
            println("eats: missing [insect]");
            return;
        }
        Insect i = insects.get(insectName);
        if (i == null) {
            println("Insect not found: " + insectName);
            return;
        }
        int apoint = i.getActionPoint();
        int mpoint = i.getMaxActionPoint();
        int ef = i.getEffectRemainingTime();
        boolean stun = i.isStunned();
        boolean cut = i.checkCanCutYarn();
        if(i.eatSpore())
        {
            println("["+ insectName + "]: actionPoint: " + apoint + " -> " + (i.getActionPoint()));
            if(mpoint !=i.getMaxActionPoint() ) {
            println("["+ insectName + "]: maxActionPoint: " + mpoint + " -> " + (i.getMaxActionPoint()));
            }
            if(ef != i.getEffectRemainingTime()) {
                println("[" + insectName + "]: effectRemainingTime: " + ef + " -> " + (i.getEffectRemainingTime()));
            }
            if(i.isStunned() != stun)
            {
                println("[" + insectName + "]: isStunned: " + stun + " -> " + (i.isStunned()));
            }
            if(i.checkCanCutYarn() != cut)
            {
                println("[" + insectName + "]: canCutYarn: " + cut + " -> " + (i.checkCanCutYarn()));
            }

        }


    }

    /**
     * Allows a MushroomYarn to consume an Insect if possible.
     *
     * This method checks if the specified MushroomYarn exists and attempts to make it
     * consume an Insect. If the yarn name is null or the MushroomYarn cannot be found,
     * an appropriate error message is printed. If the consumption fails, a failure
     * message is logged.
     *
     * @param yarnName The name of the MushroomYarn attempting to eat an Insect. Must not be null.
     */
    public void eatInsectYarn(String yarnName) {
        if (yarnName == null) {
            println("eati: missing [yarn]");
            return;
        }
        MushroomYarn y = yarns.get(yarnName);
        if (y == null) {
            println("Yarn not found: " + yarnName);
            return;
        }
        if(!y.eatInsect())
        {
            println("["+ yarnName + "]: Can't eat insect");
        }
    }

    /**
     * Initiates the growth of a MushroomYarn from a specified MushroomBody to a specified Tecton.
     *
     * This method validates the existence of the MushroomBody and Tecton before attempting
     * to grow the MushroomYarn. If either the body or the Tecton is not found, an error message
     * is printed. If the growth operation fails, an appropriate failure message is logged.
     *
     * @param bodyName   The name of the MushroomBody initiating the yarn growth. Must not be null.
     * @param tectonName The name of the Tecton where the yarn will grow. Must not be null.
     */
    public void growYarn(String bodyName, String tectonName) {
        if (bodyName == null || tectonName == null) {
            println("growy: missing [body] or -t [tecton]");
            return;
        }
        MushroomBody mb = bodies.get(bodyName);
        Tecton t = tectons.get(tectonName);
        if (mb == null) {
            println("Body not found: " + bodyName);
            return;
        }
        if (t == null) {
            println("Tecton not found: " + tectonName);
            return;
        }
        if(!mb.initiateMushroomYarnGrowth(t))
        {
            println("["+ bodyName + "]: Can't grow yarn to " + tectonName);
        }
    }

    /**
     * Initiates the growth of a MushroomBody on a specified Tecton.
     *
     * This method attempts to grow a MushroomBody on the specified Tecton. It validates
     * the existence of the Tecton and checks if a Mycologist is available. If a Mycologist
     * is found, it iterates through their MushroomBodies to initiate growth on the Tecton.
     * If no Mycologist is available or the growth cannot be initiated, appropriate error
     * messages are logged.
     *
     * @param tectonName The name of the Tecton where the MushroomBody will grow. Must not be null.
     */
    public void growBody(String tectonName) {
        if (tectonName == null) {
            println("growb: missing [tecton]");
            return;
        }
        Tecton t = tectons.get(tectonName);
        if (t == null) {
            println("Tecton not found: " + tectonName);
            return;
        }
        Mycologist myc = (Mycologist) mycologists.stream().findFirst().orElse(null);
        if (myc == null) {
            println("No Mycologist available");
            return;
        }
        for (MushroomBody body : myc.getBodies()) {
            if (body.initiateMushroomBodyGrowth(t, true)) {
                return;

            }
        }
        println("Can't grow new Mushroom!");

    }

    /**
     * Moves an Insect to a specified Tecton.
     *
     * This method attempts to move an Insect identified by its name to a Tecton identified by its name.
     * It validates the existence of both the Insect and the Tecton before proceeding. If the move is
     * successful, it logs the changes in the Insect's action points, the source Tecton, and the target
     * Tecton. If the move fails, an appropriate error message is printed.
     *
     * @param insectName The name of the Insect to move. Must not be null.
     * @param tectonName The name of the Tecton to move the Insect to. Must not be null.
     */
    public void moveInsect(String insectName, String tectonName) {
        if (insectName == null || tectonName == null) {
            println("move: missing [insect] or -t [tecton]");
            return;
        }
        Insect i = insects.get(insectName);
        Tecton t = tectons.get(tectonName);
        if (i == null) {
            println("Insect not found: " + insectName);
            return;
        }
        if (t == null) {
            println("Tecton not found: " + tectonName);
            return;
        }
        String oldt ="";
        for (Map.Entry<String, Tecton> entry : tectons.entrySet()) {
            if (entry.getValue().equals(i.getSourceTecton())) {
                oldt = entry.getKey();
            }
            }

        if(i.moveToTecton(t))
        {
            println("["+ insectName + "]: actionPoint: " + (i.getActionPoint()+1) + " -> " + (i.getActionPoint()));
            println("["+ oldt + "]: insect: ["+ insectName +"] -> [null]");
            println("["+insectName + "]: sourceTecton: [" + oldt + "] -> ["+ tectonName+"]");
            println("["+ tectonName + "]: insect: [null] -> ["+ insectName+"]");
            SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> gamePanel.onViewChanged()));


        }
        else
        {
            if(insects.containsKey(insectName))
            {
                println("["+ insectName + "]: Can't move to " + tectonName);
            }
        }

    }

    /**
     * Finds an Entomologist by their unique ID.
     *
     * This method searches the list of entomologists for an Entomologist
     * with the specified ID. If a match is found, it returns the Entomologist
     * object; otherwise, it returns null.
     *
     * @param id The unique identifier of the Entomologist to find.
     * @return The Entomologist with the specified ID, or null if not found.
     */
    private Entomologist findEntomologist(int id) {
        return (Entomologist) entomologists.stream()
                .filter(e -> e.getID() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds a Mycologist by their unique ID.
     *
     * This method searches the list of mycologists for a Mycologist
     * with the specified ID. If a match is found, it returns the Mycologist
     * object; otherwise, it returns null.
     *
     * @param id The unique identifier of the Mycologist to find.
     * @return The Mycologist with the specified ID, or null if not found.
     */
    private Mycologist findMycologist(int id) {
        return (Mycologist) mycologists.stream()
                .filter(m -> m.getID() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the names of all Entomologists.
     *
     * This method iterates through the list of entomologists and collects their names
     * into a new list, which is then returned. Each entomologist is represented as a
     * `Player` object, and their name is obtained using the `getName` method.
     *
     * @return A list of strings containing the names of all entomologists.
     */
    private List<String> getEntNames() {
        List<String> names = new ArrayList<>();
        for (Player e : entomologists) {
            names.add(e.getName());
        }
        return names;
    }

    /**
     * Retrieves the names of all Mycologists.
     *
     * This method iterates through the list of mycologists and collects their names
     * into a new list, which is then returned. Each mycologist is represented as a
     * `Player` object, and their name is obtained using the `getName` method.
     *
     * @return A list of strings containing the names of all mycologists.
     */
    private List<String> getMycoNames() {
        List<String> names = new ArrayList<>();
        for (Player m : mycologists) {
            names.add(m.getName());
        }
        return names;
    }

    /**
     * Copies the state from another `MainView` instance.
     *
     * This method assigns the internal state of the provided `MainView` object
     * to the current instance. It copies the maps and lists of game objects,
     * as well as the random behavior flag.
     *
     * @param o The `MainView` instance to copy the state from. Must not be null.
     */
    private void copyFrom(MainView o) {
        this.tectons = o.tectons;
        this.bodies = o.bodies;
        this.yarns = o.yarns;
        this.insects = o.insects;
        this.mycologists = o.mycologists;
        this.entomologists = o.entomologists;
        this.randomEnabled = o.randomEnabled;
    }

    /**
     * Prints a message to the console and logs it.
     *
     * This method outputs the provided message to the standard output
     * and appends it to the console log for later use.
     *
     * @param msg The message to be printed and logged. Must not be null.
     */
    public static void println(String msg) {
        System.out.println(msg);
        log(msg);
    }

    /**
     * Logs a message to the console log.
     *
     * This method adds the provided message to the `consoleLog` list for later use.
     *
     * @param msg The message to be logged. Must not be null.
     */
    private static void log(String msg) {
        consoleLog.add(msg);
    }

    /**
     * Retrieves the map of Tectons in the game.
     *
     * This method returns a map where the keys are the names or identifiers
     * of the Tectons, and the values are the corresponding `Tecton` objects.
     *
     * @return A map containing the Tectons in the game.
     */
    public Map<String, Tecton> getTectons() {
        return tectons;
    }

    /**
     * Retrieves the map of MushroomBodies in the game.
     *
     * This method returns a map where the keys are the names or identifiers
     * of the MushroomBodies, and the values are the corresponding `MushroomBody` objects.
     *
     * @return A map containing the MushroomBodies in the game.
     */
    public Map<String, MushroomBody> getBodies() {
        return bodies;
    }

    /**
     * Retrieves the map of MushroomYarns in the game.
     *
     * This method returns a map where the keys are the names or identifiers
     * of the MushroomYarns, and the values are the corresponding `MushroomYarn` objects.
     *
     * @return A map containing the MushroomYarns in the game.
     */
    public Map<String, MushroomYarn> getYarns() {
        return yarns;
    }

    /**
     * Retrieves the map of Spores in the game.
     *
     * This method returns a map where the keys are the names or identifiers
     * of the Spores, and the values are the corresponding `Spore` objects.
     *
     * @return A map containing the Spores in the game.
     */
    public Map<String, Spore> getSpores() {
        return spores;
    }

    /**
     * Retrieves the map of Insects in the game.
     *
     * This method returns a map where the keys are the names or identifiers
     * of the Insects, and the values are the corresponding `Insect` objects.
     *
     * @return A map containing the Insects in the game.
     */
    public Map<String, Insect> getInsects() {
        return insects;
    }

    /**
     * Retrieves the list of Mycologists in the game.
     *
     * This method returns a list of `Player` objects representing the Mycologists.
     *
     * @return A list containing the Mycologists in the game.
     */
    public List<Player> getMycologists() {
        return mycologists;
    }

    /**
     * Retrieves the list of Entomologists in the game.
     *
     * This method returns a list of `Player` objects representing the Entomologists.
     *
     * @return A list containing the Entomologists in the game.
     */
    public List<Player> getEntomologists() {
        return entomologists;
    }

    /**
     * Checks if random behavior is enabled in the game.
     *
     * This method returns the current state of the `randomEnabled` flag,
     * which determines whether random behavior is active.
     *
     * @return `true` if random behavior is enabled, `false` otherwise.
     */
    public boolean isRandomEnabled() {
        return randomEnabled;
    }

    public Map<int[], Tecton> getTectonPosMap() {
        return tectonPos;
    }
}