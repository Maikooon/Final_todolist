import javax.swing.*;
import javax.swing.border.EmptyBorder;

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
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 40, 40, 40));

        JPanel contentPanel = new JPanel(new GridLayout(9, 2, 5, 10)); 

        JLabel emailLabel = new JLabel("Email: ");
        emailTextField = new JTextField();
        emailTextField.setPreferredSize(new Dimension(400, 50));
        JLabel passwordLabel = new JLabel("Password: ");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(400, 50));

        contentPanel.add(emailLabel);
        contentPanel.add(emailTextField);
        contentPanel.add(passwordLabel);
        contentPanel.add(passwordField);

        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        headerPanel.add(backButton, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(200, 50));
        buttonPanel.add(loginButton);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 「Login」ボタンが押されたときの処理（フィールドの値を取得してemailとpasswordの組み合わせが正しいかチェック）
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());

                if (checkLogin(email, password)) {
                    user_id = getUserIdByEmail(email); // 入力されたEmailからユーザーIDを取得してフィールドに代入
                    JOptionPane.showMessageDialog(LoginPanel.this, "Login successful!");
                    Frame frame = (Frame) getParent();
                    CardLayout cardLayout = (CardLayout) frame.getLayout();
                    cardLayout.show(frame, "MainPanel"); // ページ名を指定して切り替え
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Invalid login credentials. Please try again.");
                }
            }
        });
        backButton.addActionListener(e -> {
            Frame frame = (Frame) getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.first(frame); // 先頭のページに切り替え
        });
    }

    // emailとpasswordの組み合わせが正しいかチェック
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

    // 入力されたEmailからユーザーIDを取得
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
