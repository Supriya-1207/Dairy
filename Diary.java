package Diaryy;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Diary {

    // Store login credentials (default + newly created)
    private static String currentUsername = "user";
    private static String currentPassword = "1234";

    public static void main(String[] args) {
        openLoginWindow();
    }

    private static void openLoginWindow() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 150);
        loginFrame.setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        loginFrame.add(loginPanel, BorderLayout.CENTER);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginPanel.add(loginButton);

        JButton createAccountButton = new JButton("Create an Account");
        loginPanel.add(createAccountButton);

        loginButton.addActionListener(e -> {
            String enteredUsername = usernameField.getText();
            String enteredPasswordString = new String(passwordField.getPassword());

            if (enteredUsername.equals(currentUsername) && enteredPasswordString.equals(currentPassword)) {
                openDiaryApp();
                loginFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Login Failed. Try again.");
            }
        });

        createAccountButton.addActionListener(e -> createAccount(loginFrame));

        loginFrame.setVisible(true);
    }

    private static void openDiaryApp() {
        JFrame diaryFrame = new JFrame("Diary Application");
        diaryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        diaryFrame.setSize(500, 400);

        JPanel panel = new JPanel(new FlowLayout());

        JLabel lmood = new JLabel("Set your mood:");
        String[] moods = {"Relaxed", "Grateful", "Contented", "Loved", "Hopeful", "Inspired", "Joyful", "Calm"};
        JComboBox<String> smood = new JComboBox<>(moods);

        JCheckBox amCheckBox = new JCheckBox("AM");
        JCheckBox pmCheckBox = new JCheckBox("PM");

        JTextArea textArea = new JTextArea(10, 30);
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveDiaryEntry(textArea, (String) smood.getSelectedItem(),
                amCheckBox.isSelected(), pmCheckBox.isSelected()));

        panel.add(lmood);
        panel.add(smood);
        panel.add(amCheckBox);
        panel.add(pmCheckBox);
        panel.add(scrollPane);
        panel.add(saveButton);

        diaryFrame.getContentPane().add(panel);
        diaryFrame.setVisible(true);
    }

    private static void createAccount(JFrame loginFrame) {
        JFrame accountFrame = new JFrame("Create an Account");
        accountFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        accountFrame.setSize(300, 150);

        JPanel accountPanel = new JPanel(new GridLayout(3, 2));
        accountFrame.add(accountPanel, BorderLayout.CENTER);

        JLabel usernameLabel = new JLabel("New Username:");
        JTextField usernameField = new JTextField();
        accountPanel.add(usernameLabel);
        accountPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("New Password:");
        JPasswordField passwordField = new JPasswordField();
        accountPanel.add(passwordLabel);
        accountPanel.add(passwordField);

        JButton createAccountButton = new JButton("Create Account");
        accountPanel.add(createAccountButton);

        createAccountButton.addActionListener(e -> {
            String newUsername = usernameField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim();

            if (newUsername.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(accountFrame, "Please enter both username and password.");
                return;
            }

            // Store new credentials in memory
            currentUsername = newUsername;
            currentPassword = newPassword;

            JOptionPane.showMessageDialog(accountFrame, "Account created successfully! You can now log in.");
            accountFrame.dispose();
        });

        accountFrame.setVisible(true);
    }

    private static void saveDiaryEntry(JTextArea textArea, String mood, boolean am, boolean pm) {
        String content = textArea.getText();
        if (!content.isEmpty()) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Diary Entry");

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            fileChooser.setSelectedFile(new java.io.File("diary_entry_" + now.format(formatter) + ".txt"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                    writer.write("Date: " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
                    writer.write("Mood: " + mood + "\n");
                    writer.write("Time of Day: " + (am ? "AM " : "") + (pm ? "PM" : "") + "\n");
                    writer.write("Entry:\n" + content);
                    JOptionPane.showMessageDialog(null, "Diary entry saved successfully!");
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error occurred while saving the diary entry.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Diary entry is empty. Nothing to save.");
        }
    }
}
