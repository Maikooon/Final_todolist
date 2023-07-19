
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;

// Login panel

class LoginPanel extends JPanel {
    private JTextField emailTextField;
    private JPasswordField passwordField;
    public static int user_id; // have userid

    public LoginPanel() {
        // layout
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(40, 50, 40, 60));

        // title label
        JLabel titleLabel = new JLabel("Log In");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

        // main
        JPanel contentPanel = new JPanel(new GridLayout(9, 2, 5, 10));

        JLabel emailLabel = new JLabel("Email: ");
        emailTextField = new JTextField();
        emailTextField.setPreferredSize(new Dimension(400, 50));
        JLabel passwordLabel = new JLabel("Password: ");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(400, 50));

        // add label and panel
        contentPanel.add(emailLabel);
        contentPanel.add(emailTextField);
        contentPanel.add(passwordLabel);
        contentPanel.add(passwordField);

        // back button and title
        JButton backButton; // back button
        backButton = new JButton("Back");
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(backButton, BorderLayout.WEST);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Login button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(200, 50));
        buttonPanel.add(loginButton);

        // layout
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 「Login」 button is pused
        // verify  email & password
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());

                if (checkLogin(email, password)) {
                    user_id = getUserIdByEmail(email); // get user id form email
                    // clear input field
                    emailTextField.setText("");
                    passwordField.setText("");
                    JOptionPane.showMessageDialog(LoginPanel.this, "Login successful!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    Frame frame = (Frame) SwingUtilities.getWindowAncestor(LoginPanel.this);
                    MyWindow myWindow = (MyWindow) frame;
                    myWindow.addPanelsAfterLogin();
                    CardLayout cardLayout = (CardLayout) frame.getLayout();
                    cardLayout.show(frame, "MainPanel");
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Invalid login credentials. Please try again.",
                            "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        backButton.addActionListener(e -> {
            Frame frame = (Frame) getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.first(frame);
        });
    }

    // verify email to password
 
    // hash -func
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean checkLogin(String email, String password) {
        try {
            String csvFile = "member.csv";
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4 && data[2].equals(email)) {
                    // to hash password
                    if (data[3].equals(hashPassword(password))) {
                        br.close();
                        return true;
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // get user id from email
    private int getUserIdByEmail(String email) {
        try {
            String csvFile = "member.csv";
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[2].equals(email)) {
                    br.close();
                    return Integer.parseInt(data[0]);
                }
            }
            br.close();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
