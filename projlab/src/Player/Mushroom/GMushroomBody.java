package Player.Mushroom;

import View.IGListener;

import java.awt.*;
import javax.swing.JComponent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GMushroomBody extends JComponent implements MouseListener {
    private String name;
    private boolean selected = false;
    private final IGListener iglistener; // Reference to coordinate selections
    private String color;
    // Constructor updated to take GamePanel reference
    /**
     * Constructs a new `GMushroomBody` instance.
     *
     * @param x     The x-coordinate of the mushroom body.
     * @param y     The y-coordinate of the mushroom body.
     * @param name  The name of the mushroom body.
     * @param igl   The listener to handle selection events.
     */
    public GMushroomBody(int x, int y, String name, IGListener igl, String col) {
        this.name = name;
        this.iglistener = igl;
        setBounds(x, y, 20, 20);
        addMouseListener(this); // Register itself as the listener
        color = col;
    }

    // Selection state setter
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    // Check selection state (optional, for external use)
    public boolean isSelected() {
        return selected;
    }

    /**
     * Paints the mushroom body component.
     * Draws a circular shape with a name inside it and highlights it if selected.
     *
     * @param g The `Graphics` object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
       // g.setColor(Color.GREEN);

       // g.setColor(Color.decode(color));
        g.setColor(Color.decode(color));
        g.fillOval(0, 0, getWidth(), getHeight() );
        g.setColor(Color.BLACK);
        g.drawOval(0, 0, getWidth() , getHeight() );
        if (selected) {
            g.setColor(Color.RED); // Selection indicator
            g.drawOval(0, 0, getWidth() , getHeight() );
        }
        // Draw name, adjust as needed
        Font font = new Font("Arial", Font.PLAIN, 10);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString(name, 3, 15);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Delegate selection management to GamePanel
        setSelected(!selected);
        iglistener.onGMushroomBody(this);

        System.out.println("Clicked on: " + name);
        // Toggle selection state
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}