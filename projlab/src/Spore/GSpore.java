package Spore;

import javax.swing.*;
import java.awt.*;

public class GSpore extends JComponent {
    private final int x, y;
    private final String name;
    private final Spore spore;

    public GSpore(int x, int y, String name, Spore spore) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.spore = spore;
        setSize(20, 20);
        setLocation(x, y);
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (spore.isCompatible(new FissionSpore(0, null))) {
            g.setColor(Color.RED);
        };
        if (spore.isCompatible(new AcceleratorSpore(0, null))) {
            g.setColor(Color.BLUE);
        };
        if (spore.isCompatible(new SlowingSpore(0, null))) {
            g.setColor(Color.GREEN);
        };
        if (spore.isCompatible(new WeakeningSpore(0, null))) {
            g.setColor(Color.YELLOW);
        };
        if (spore.isCompatible(new ParalysingSpore(0, null))) {
            g.setColor(Color.ORANGE);
        };
        g.fillOval(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        g.drawOval(0, 0, getWidth(), getHeight() );
       // g.drawRect(0, 0, getWidth() , getHeight() );
        g.drawString(name, 8, 12);


    }
}
