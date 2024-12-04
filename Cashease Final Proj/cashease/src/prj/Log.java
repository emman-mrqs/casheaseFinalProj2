package prj;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Color;

public class Log extends JFrame {
    private JPanel contentPane;

    public Log(String username) {
        setTitle("Homepage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        
        contentPane = new JPanel();
        contentPane.setBackground(Color.LIGHT_GRAY); // Set a background color
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblWelcome = new JLabel("Welcome, " + username + "!");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 30));
        lblWelcome.setBounds(250, 200, 300, 50);
        contentPane.add(lblWelcome);
        
        setVisible(true);  // Ensure the frame is visible
    }
}
