package Player.Mushroom;

import View.IGListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GMushroomYarn extends JComponent implements MouseListener {
    private final int x1, y1, x2, y2;
    private final String name;
    private boolean selected = false;
    private boolean isConnectedToMushroom = true;
    private final IGListener iglistener;
    private String color;

    /**
     * Constructs a new `GMushroomYarn` instance.
     *
     * @param x1   The x-coordinate of the first endpoint.
     * @param y1   The y-coordinate of the first endpoint.
     * @param x2   The x-coordinate of the second endpoint.
     * @param y2   The y-coordinate of the second endpoint.
     * @param name The name of the yarn.
     * @param igl  The listener to handle selection events.
     */
    public GMushroomYarn(int x1, int y1, int x2, int y2, String name , IGListener igl, String col) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.name = name;
        this.iglistener = igl;
        this.color = col;
        setVisible(true);
        addMouseListener(this);
    }
    /**
     * Paints the yarn component.
     * Draws a line between two points and displays its name. If disconnected, a cross is drawn.
     *
     * @param g The `Graphics` object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        if(selected) {
            g2d.setColor(Color.RED);
        }
        else {
        g2d.setColor(Color.decode(color));
        }

        // Translate coordinates to component's local space
        int localX1 = x1 - getX();
        int localY1 = y1 - getY();
        int localX2 = x2 - getX();
        int localY2 = y2 - getY();

        // Adjust to center of Tecton nodes (assuming 50x50 size)
        localX1 += 25;
        localY1 += 25;
        localX2 += 25;
        localY2 += 25;

        int midX = (localX1 + localX2) / 2;
        int midY = (localY1 + localY2) / 2;
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(localX1, localY1, localX2, localY2);

        if (isConnectedToMushroom) {
            g2d.setColor(Color.BLACK);
            g2d.drawString(name, midX + 5, midY + 5);
        }
        else {
            double angle = Math.atan2(localY2 - localY1, localX2 - localX1) + Math.PI/2;
            int crossLength = 10;
            int x3 = midX + (int)(Math.cos(angle) * crossLength);
            int y3 = midY + (int)(Math.sin(angle) * crossLength);
            int x4 = midX - (int)(Math.cos(angle) * crossLength);
            int y4 = midY - (int)(Math.sin(angle) * crossLength);
            g2d.drawLine(x3, y3, x4, y4);
            g2d.setColor(Color.BLACK);
            g2d.drawString(name, midX + 5, midY + 15);
        }

        g2d.dispose();
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        // Delegate selection management to GamePanel
        setSelected(!selected);
       iglistener.onGMushroomYarn(this);
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

    public boolean isConnectedToMushroom() {
        return isConnectedToMushroom;
    }
    public void setConnectedToMushroom(boolean isConnectedToMushroom) {
        this.isConnectedToMushroom = isConnectedToMushroom;
        repaint();
    }
}