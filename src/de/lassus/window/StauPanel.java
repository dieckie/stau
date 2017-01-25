package de.lassus.window;

import java.awt.*;

import javax.swing.JPanel;

import de.lassus.engine.*;

public class StauPanel extends JPanel {

    private static final long serialVersionUID = 2L;

    Engine engine;

    public StauPanel(Engine engine) {
        this.engine = engine;
        setPreferredSize(new Dimension(1280, 720));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        for(Car c : engine.getCars()) {
            Color col = c.getColor();
            g.setColor(col);
            int x = (int) ((c.getX() - c.getLength()) % engine.getRowWidth() * Engine.SCALE - Car.LENGTH * Engine.SCALE);
            int y = (int) ((getHeight() / Engine.ROWS) * Math.floor((c.getX() - c.getLength()) / engine.getRowWidth()) + getHeight() / Engine.ROWS / 2);
            int w = (int) (c.getLength() * Engine.SCALE);
            int h = (int) (c.getWidth() * Engine.SCALE);
            g.fillRect(x, y , w, h);
            g.setColor(Color.BLACK);
            g.drawString(c.getId() + "", (int) (x + ( w - g.getFontMetrics().stringWidth(c.getId() + "")) * 0.5), (int) (y + h * (2.5 / 3.0)));
        }
    }

    public double getInGameWidth() {
        return getWidth() / Engine.SCALE;
    }
}
