package Game.Map;

import View.IGListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GTecton extends JComponent implements MouseListener {
    private final int x, y;
    private final String name;
    private boolean selected = false;
    private final IGListener gtectonlistener; // Reference to coordinate selections

    /**
     * Constructs a new `GTecton` instance.
     *
     * @param xPos          The x-coordinate of the GTecton.
     * @param yPos          The y-coordinate of the GTecton.
     * @param name          The name of the GTecton.
     * @param tectonListener The listener to handle selection events.
     */
    public GTecton(int xPos, int yPos, String name, IGListener tectonListener) {
        this.x = xPos;
        this.y = yPos;
        this.name = name;
        this.gtectonlistener = tectonListener;
        if(x%50 !=0 || y%50 !=0){
            setSize(25, 50);
        }else {
            setSize(50, 50);
        }

        setLocation(x, y);
        setVisible(true);
        addMouseListener(this); // Register itself as the listener

    }
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    // Check selection state (optional, for external use)
    public boolean isSelected() {
        return selected;
    }

    /**
     * Paints the GTecton component.
     * Draws a rectangle with a name inside it and highlights it if selected.
     *
     * @param g The `Graphics` object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);
        if(selected) {
            g.setColor(Color.RED); // Selection indicator
        }
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.setColor(Color.BLACK);
        if(x%50 !=0 || y%50 !=0) {
            g.drawString(name, 5, 30);
        }else{
        g.drawString(name, 5, 40); } // Draw name inside the box
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        // Delegate selection management to GamePanel
        setSelected(!selected);
        gtectonlistener.onGTecton(this);

        System.out.println("Clicked on: " + name);
        // Toggle selection state
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
