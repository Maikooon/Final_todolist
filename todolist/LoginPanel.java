import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class LoginPanel extends JPanel {
    private JTextField emailTextField;
    private JPasswordField passwordField;
    public static int user_id; // ユーザーIDを保持する変数

    public LoginPanel() {
        setLayout(null);

        emailTextField = new JTextField();
        emailTextField.setBounds(50, 400, 150, 30);
        add(new JLabel("Email:", SwingConstants.LEFT));
        add(emailTextField);

        passwordField = new JPasswordField();
        passwordField.setBounds(210, 400, 150, 30);
        add(new JLabel("Password:", SwingConstants.LEFT));
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(50, 480, 100, 30);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());

                if (checkLogin(email, password)) {
                    user_id = getUserIdByEmail(email); // 入力されたEmailからユーザーIDを取得してフィールドに代入

                    Frame frame = (Frame) getParent();
                    CardLayout cardLayout = (CardLayout) frame.getLayout();
                    cardLayout.show(frame, "TodoPanel"); // ページ名を指定して切り替え
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Invalid login credentials. Please try again.");
                }
            }
        });
        add(loginButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(550, 10, 100, 25);
        backButton.addActionListener(e -> {
            Frame frame = (Frame) getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.first(frame); // 先頭のページに切り替え
        });
        add(backButton);
    }

    private boolean checkLogin(String email, String password) {
        try {
            String csvFile = "member.csv";
            BufferedReader br = new BufferedReader(new FileReader(csvFile));

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4 && data[2].equals(email) && data[3].equals(password)) {
                    br.close();
                    return true;
                }
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

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
