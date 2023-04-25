package real.health.GUI;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

public class CalendarPanel {
    private int month, year;
    private JLabel label;
    private JPanel panel;
    private ImageIcon image;

    public CalendarPanel(String currentDate) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(currentDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(210, 160));
        drawCalendar(month, year);

        image = new ImageIcon(panel.createImage(panel.getWidth(), panel.getHeight()));
    }

    public ImageIcon getImage() {
        return image;
    }

    private void drawCalendar(int month, int year) {
        String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December" };

        JLabel monthLabel = new JLabel(months[month]);
        monthLabel.setBounds(60, 0, 90, 25);
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(monthLabel);

        JLabel yearLabel = new JLabel(Integer.toString(year));
        yearLabel.setBounds(150, 0, 50, 25);
        yearLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(yearLabel);

        int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
            daysInMonth[1] = 29;
        }
        int firstDayOfWeek = new GregorianCalendar(year, month, 1).get(Calendar.DAY_OF_WEEK);

        for (int i = 0; i < 7; i++) {
            JLabel dayOfWeekLabel = new JLabel();
            dayOfWeekLabel.setHorizontalAlignment(SwingConstants.CENTER);
            dayOfWeekLabel.setVerticalAlignment(SwingConstants.CENTER);
            dayOfWeekLabel.setBounds(i * 30, 25, 30, 20);
            dayOfWeekLabel.setText(getDayOfWeekName(i));
            panel.add(dayOfWeekLabel);
        }

        int x = (firstDayOfWeek - 1) * 30;
        int y = 45;
        for (int i = 1; i <= daysInMonth[month]; i++) {
            JLabel dayOfMonthLabel = new JLabel();
            dayOfMonthLabel.setHorizontalAlignment(SwingConstants.CENTER);
            dayOfMonthLabel.setVerticalAlignment(SwingConstants.CENTER);
            dayOfMonthLabel.setBounds(x, y, 30, 20);
            dayOfMonthLabel.setText(Integer.toString(i));
            panel.add(dayOfMonthLabel);
            x += 30;
            if (x == 210) {
                x = 0;
                y += 20;
            }
        }
    }

    private String getDayOfWeekName(int dayOfWeek) {
        String[] daysOfWeek = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        return daysOfWeek[dayOfWeek];
    }
}
