import Controller.MainController;
import Player.Player;
import View.GamePanel;
import View.MainView;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



// Forditáshoz ha nem mukodne $env:PATH += ";C:\Program Files\Java\jdk-XXX ide a verzioszam\bin"

public class GameWindow extends JFrame {
    private static final String MENU_PANEL = "MenuPanel";
    private static final String GAME_PANEL = "View.GamePanel";
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private MainView mainView;
    private final GamePanel gamePanel;
    /**
     * Constructs a new `GameWindow` instance.
     * Initializes the main menu and game panel, and sets up the layout and window properties.
     */
    public GameWindow() {
        setTitle("Fungorium");
        setSize(1280, 720);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize CardLayout and container panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Initialize MainView and View.GamePanel
        gamePanel = new GamePanel(mainView); // MainView is null initially, set later
        mainView = new MainView(gamePanel);
        gamePanel.setMainView(mainView); // Update View.GamePanel with MainView reference

        // Add MainMenuPanel
        MainMenuPanel menuPanel = new MainMenuPanel(this);
        cardPanel.add(menuPanel, MENU_PANEL);

        // Add the card panel to the frame
        add(cardPanel);

        // Show the menu panel initially
        cardLayout.show(cardPanel, MENU_PANEL);
    }

    // Method to switch to View.GamePanel
    public void startGame() {
        // Create and add View.GamePanel if not already added
        gamePanel.draw();
        if (cardPanel.getComponentCount() == 1) {
            cardPanel.add(gamePanel, GAME_PANEL);
        }
        cardLayout.show(cardPanel, GAME_PANEL);
        mainView.handleCommand("/start"); // Send start command
    }

    // Methods for button actions

    /**
     * Loads a saved game from a file.
     */
    public void loadGame() {
       mainView.loadGame("src/Map.txt", "-p");
    }
    /**
     * Saves the current game state to a file.
     */
    public void saveGame() {
        mainView.saveGame("game.txt");
    }
    /**
     * Opens the settings dialog for configuring player settings.
     */
    public void openSettings() {
        JDialog settingsDialog = new JDialog(this, "Player Settings", true);
        settingsDialog.setSize(500, 500);
        settingsDialog.setLayout(new GridBagLayout());
        settingsDialog.setLocationRelativeTo(this);

        JLabel targetLabel = new JLabel("Target Point:");
        JTextField targetField = new JTextField(15);

        JLabel playerSelectLabel = new JLabel("Select Player:");
        JComboBox<Player> playerComboBox = new JComboBox<>();

        JLabel nameLabel = new JLabel("Player Name:");
        JTextField nameField = new JTextField(15);

        JLabel colorLabel = new JLabel("Color:");
        JButton colorButton = new JButton("Select Color");

        final Color[] selectedColor = {Color.WHITE}; // default color
        colorButton.setBackground(selectedColor[0]);
        colorButton.setForeground(Color.BLACK);

        colorButton.addActionListener(e -> {
            Color chosenColor = JColorChooser.showDialog(settingsDialog, "Choose Player Color", selectedColor[0]);
            if (chosenColor != null) {
                selectedColor[0] = chosenColor;
                colorButton.setBackground(chosenColor);
                colorButton.setText(String.format("RGB(%d, %d, %d)", chosenColor.getRed(), chosenColor.getGreen(), chosenColor.getBlue()));
            }
        });

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        DefaultListModel<String> playerListModel = new DefaultListModel<>();
        JList<String> playerList = new JList<>(playerListModel);
        JScrollPane listScrollPane = new JScrollPane(playerList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Load players
        List<Player> ent = mainView.getEntomologists();
        List<Player> myc = mainView.getMycologists();

        if (ent != null) {
            for (Player player : ent) {
                playerComboBox.addItem(player);
                playerListModel.addElement(String.format("%s (ID: %d, Type: Entomologist, Color: %s)", player.getName(), player.getID(), player.getColor()));
            }
        }
        if (myc != null) {
            for (Player player : myc) {
                playerComboBox.addItem(player);
                playerListModel.addElement(String.format("%s (ID: %d, Type: Mycologist, Color: %s)", player.getName(), player.getID(), player.getColor()));
            }
        }

        playerComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Player) {
                    Player p = (Player) value;
                    value = p.getName() + " (ID: " + p.getID() + ")";
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        // Load selected player's info
        playerComboBox.addActionListener(e -> {
            Player selected = (Player) playerComboBox.getSelectedItem();
            if (selected != null) {
                nameField.setText(selected.getName());

                // Try to parse hex color
                try {
                    Color parsedColor = Color.decode(selected.getColor());
                    selectedColor[0] = parsedColor;
                    colorButton.setBackground(parsedColor);
                    colorButton.setText(String.format("RGB(%d, %d, %d)", parsedColor.getRed(), parsedColor.getGreen(), parsedColor.getBlue()));
                } catch (Exception ex) {
                    selectedColor[0] = Color.WHITE;
                    colorButton.setBackground(Color.WHITE);
                    colorButton.setText("Select Color");
                }
            }
        });

        // Layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        settingsDialog.add(targetLabel, gbc);
        gbc.gridx = 1;
        settingsDialog.add(targetField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        settingsDialog.add(playerSelectLabel, gbc);
        gbc.gridx = 1;
        settingsDialog.add(playerComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        settingsDialog.add(nameLabel, gbc);
        gbc.gridx = 1;
        settingsDialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        settingsDialog.add(colorLabel, gbc);
        gbc.gridx = 1;
        settingsDialog.add(colorButton, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        settingsDialog.add(cancelButton, gbc);
        gbc.gridx = 1;
        settingsDialog.add(saveButton, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        settingsDialog.add(listScrollPane, gbc);

        // Save button logic
        saveButton.addActionListener(e -> {
            try {
                String targetText = targetField.getText();
                if (!targetText.isEmpty()) {
                    int targetPoint = Integer.parseInt(targetText);
                    if (targetPoint >= 0) {
                        MainController.setTargetPoint(targetPoint);
                    } else {
                        JOptionPane.showMessageDialog(settingsDialog, "Target point must be non-negative!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                Player selected = (Player) playerComboBox.getSelectedItem();
                if (selected != null) {
                    String newName = nameField.getText();
                    String newColorHex = String.format("#%02x%02x%02x",
                            selectedColor[0].getRed(),
                            selectedColor[0].getGreen(),
                            selectedColor[0].getBlue());

                    if (newName != null && !newName.trim().isEmpty()) {
                        selected.setName(newName);
                        selected.setColor(newColorHex);

                        // Refresh list
                        playerListModel.clear();
                        if (ent != null) {
                            for (Player p : ent) {
                                playerListModel.addElement(String.format("%s (ID: %d, Type: Entomologist, Color: %s)", p.getName(), p.getID(), p.getColor()));
                            }
                        }
                        if (myc != null) {
                            for (Player p : myc) {
                                playerListModel.addElement(String.format("%s (ID: %d, Type: Mycologist, Color: %s)", p.getName(), p.getID(), p.getColor()));
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(settingsDialog, "Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(settingsDialog, "Target point must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> settingsDialog.dispose());

        settingsDialog.setVisible(true);
    }




    public static void main(String[] args) {

            GameWindow window = new GameWindow();
            window.setVisible(true);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

/**
 * The `MainMenuPanel` class represents the main menu of the game.
 * It provides buttons for starting the game, loading, saving, opening settings, and exiting the application.
 */
class MainMenuPanel extends JPanel {
    /**
     * Constructs a new `MainMenuPanel` instance.
     *
     * @param gameWindow The `GameWindow` instance to interact with.
     */
    public MainMenuPanel(GameWindow gameWindow) {
        setLayout(new GridBagLayout());
        setBackground(Color.DARK_GRAY);

        // Create buttons
        JButton startButton = createMenuButton("Start");

        JButton loadButton = createMenuButton("Load");
        JButton saveButton = createMenuButton("Save");
        JButton settingsButton = createMenuButton("Settings");
        JButton exitButton = createMenuButton("Exit");
        // Add action listeners
        startButton.addActionListener(e -> gameWindow.startGame());

        loadButton.addActionListener(e -> gameWindow.loadGame());
        saveButton.addActionListener(e -> gameWindow.saveGame());
        settingsButton.addActionListener(e -> gameWindow.openSettings());
        exitButton.addActionListener(e -> System.exit(0));
        // Layout buttons vertically
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);
        gbc.ipadx = 100;
        gbc.ipady = 20;

        add(startButton, gbc);

        add(loadButton, gbc);
        add(saveButton, gbc);
        add(settingsButton, gbc);
        add(exitButton, gbc);
    }

    /**
     * Creates a styled button for the main menu.
     *
     * @param text The text to display on the button.
     * @return A `JButton` instance with the specified text and styling.
     */
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLUE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return button;
    }
}