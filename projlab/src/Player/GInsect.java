package Player;

import View.IGListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GInsect extends JComponent implements MouseListener {

    private final String name;
    private boolean selected = false;
    private final IGListener iglistener;
    private String color;
    /**
     * Constructs a new `GInsect` instance.
     *
     * @param x     The x-coordinate of the insect.
     * @param y     The y-coordinate of the insect.
     * @param name  The name of the insect.
     * @param igl   The listener to handle selection events.
     */

    public GInsect(int x, int y, String name, IGListener igl, String col) {

        this.name = name;
        this.iglistener = igl;
        this.color = col;
        setSize(25, 25);
        setLocation(x, y);
        setVisible(true);
        addMouseListener(this);
    }

    /**
     * Paints the insect component.
     * Draws a circular shape with a name inside it and highlights it if selected.
     *
     * @param g The `Graphics` object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.decode(color));
        g.fillOval(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);
        g.drawOval(0, 0, getWidth() , getHeight() );
        if (selected) {
            g.setColor(Color.RED); // Selection indicator
            g.drawOval(0, 0, getWidth() , getHeight() );
        }
        Font font = new Font("Arial", Font.PLAIN, 10);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString(name, 5, 10);  // Draw name inside the box
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Delegate selection management to GamePanel
        setSelected(!selected);
        iglistener.onGInsect(this);
        System.out.println("Clicked on: " + name);
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    // Check selection state (optional, for external use)
    public boolean isSelected() {
        return selected;
    }

}
