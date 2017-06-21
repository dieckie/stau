package de.lassus.window;

import java.awt.*;

import javax.swing.JPanel;

import de.lassus.engine.*;

public class StauPanel extends JPanel {

    private static final long serialVersionUID = 2L;

    Engine engine;
    Dimension size = null;
    public static float scale;
    private float rowHeight;
    private float laneHeight;

    public StauPanel(Engine engine) {
        this.engine = engine;
        setPreferredSize(new Dimension(1280, 720));
        setMaximumSize(new Dimension(1280, 720));
        setMinimumSize(new Dimension(1280, 720));
        setDoubleBuffered(true);
        System.out.println(rowHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!getSize().equals(size)) {
            size = getSize();
            updateSize();
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());
        for(int i = 0; i < Engine.ROWS; i++) {
            g2.setColor(new Color(130, 130, 130));
            g2.fillRect(0, (int) ((i + 0.1) * rowHeight), getWidth(), (int) (0.9 * rowHeight));
            g2.setColor(Color.LIGHT_GRAY);
            for(int j = 0; j < Engine.LANES - 1; j++) {
                g2.fillRect(0, (int) ((i + 0.1) * rowHeight + ((j + 1) * (1 / (float) Engine.LANES) * 0.9) * rowHeight), getWidth(), (int) (rowHeight * 0.02));
            }
        }
        int i = 0;
        for(Car c : engine.getCars()) {
            Color col = c.getColor();
            g.setColor(col);
            int lane = Engine.LANES - 1 - c.getLane();
            int row = (int) Math.floor((c.getX() - c.getLength()) / engine.getRowWidth());
            float laneY = (float) ((row + 0.1) * rowHeight + lane * laneHeight);
            int x = (int) ((c.getX() - c.getLength()) % engine.getRowWidth() * scale - Car.LENGTH * scale);
            int h = (int) (laneHeight * 0.8);
            int y = (int) (laneY + 0.1 * laneHeight);
            int w = (int) (c.getLength() * scale);
            g2.fillRect(x, y, w, h);
            g2.setColor(Color.BLACK);
            g2.drawString(c.getId() + "", (int) (x + (w - g.getFontMetrics().stringWidth(c.getId() + "")) * 0.5), (int) (y + h * (2.2 / 3.0)));
        }

    }

    public void updateSize() {
        rowHeight = getHeight() / (float) (Engine.ROWS + 0.1);
        laneHeight = (float) (rowHeight * 0.9 / Engine.LANES);
        scale = (float) (getWidth() / (Engine.TRACKLENGTH - Car.LENGTH));

    }

    public double getInGameWidth() {
        return getWidth() / scale;
    }
}
