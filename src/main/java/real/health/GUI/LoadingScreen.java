package real.health.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class LoadingScreen extends JDialog {

    private JProgressBar progressBar;
    private JLabel progressLabel;
    private Random random;
    private Runnable onComplete;

    public LoadingScreen(Window parent, Runnable onComplete) {
        super(parent, ModalityType.APPLICATION_MODAL);

        setUndecorated(true); // Remove title bar and window decorations
        setPreferredSize(new Dimension(300, 100)); // Set preferred size of loading screen window
        setLocationRelativeTo(parent); // Center window on screen

        this.onComplete = onComplete;

        // Create and customize JLabel to display loading message
        JLabel loadingLabel = new JLabel("Loading, please wait...");
        loadingLabel.setHorizontalAlignment(JLabel.CENTER);
        loadingLabel.setVerticalAlignment(JLabel.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        getContentPane().add(loadingLabel, BorderLayout.NORTH);

        // Create and customize JProgressBar to show progress of loading process
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        getContentPane().add(progressBar, BorderLayout.SOUTH);

        // Initialize progress label
        progressLabel = new JLabel();
        getContentPane().add(progressLabel, BorderLayout.CENTER);
        pack();
        setVisible(false);

        random = new Random();
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int progress = progressBar.getValue();
                if (progress < 100) {
                    progress = progress + random.nextInt(10);
                    progressBar.setValue(progress);
                    progressLabel.setText("Loading... " + progress + "%");
                } else {
                    progressLabel.setText("Loaded successfully!");
                    ((Timer) e.getSource()).stop();
                    hideLoadingScreen();
                    onComplete.run();
                }
            }
        });

        timer.start();
    }

    public void showLoadingScreen() {
        setVisible(true);
    }

    public void hideLoadingScreen() {
        setVisible(false);
    }

    public void setProgress(int progress) {
        progressBar.setValue(progress);
    }
}
