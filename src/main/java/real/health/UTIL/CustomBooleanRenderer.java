package real.health.UTIL;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomBooleanRenderer extends DefaultTableCellRenderer {
    private ImageIcon goodIcon;
    private ImageIcon badIcon;

    public CustomBooleanRenderer() {
        super();
        try {
            InputStream greenInputStream = getClass().getResourceAsStream("good_range.png");
            InputStream redInputStream = getClass().getResourceAsStream("bad_range.jpg");
            Image greenImage = ImageIO.read(greenInputStream);
            Image redImage = ImageIO.read(redInputStream);

            int width = 16;
            int height = 16;
            
            goodIcon = new ImageIcon(greenImage.getScaledInstance(width, height, Image.SCALE_SMOOTH));
            badIcon = new ImageIcon(redImage.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        }
        catch (IOException IO) {
            IO.printStackTrace();
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof Boolean && value != null) {
            boolean flag = (Boolean) value;
            if (flag) {
                setIcon(badIcon);
            }
            else {
                setIcon(goodIcon);
            }
            setText("");
        }
        else {
            setIcon(null);
            setText("");
        }
        setHorizontalAlignment(JLabel.CENTER);
        return this;
    }
    
}
