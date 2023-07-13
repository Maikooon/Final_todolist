import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class LoginPanel extends JPanel {
    private JTextField nameTextField;
    private JTextField passwordTextField;
    public static String username; // ユーザー名を保持する変数


    public LoginPanel() {
        setLayout(null);

        nameTextField = new JTextField();
        nameTextField.setBounds(50, 400, 150, 30);
        add(new JLabel("Username:", SwingConstants.LEFT));
        add(nameTextField);

        passwordTextField = new JTextField();
        passwordTextField.setBounds(210, 400, 150, 30);
        add(new JLabel("Password:", SwingConstants.LEFT));
        add(passwordTextField);

        Button continueButton = new Button("Continue");
        continueButton.setBounds(50, 480, 100, 30);

        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                String password = passwordTextField.getText();

                if (checkLogin(name, password)) {
                    username = name; // 入力された名前をフィールドに代入

                    Frame frame = (Frame) getParent();
                    CardLayout cardLayout = (CardLayout) frame.getLayout();
                    cardLayout.show(frame, "TodoPanel"); // ページ名を指定して切り替え
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Invalid login credentials. Please try again.");
                }
            }
        });
        add(continueButton);

        Button backButton = new Button("Back");
        backButton.setBounds(550, 10, 100, 25);
        backButton.addActionListener(e -> {
            Frame frame = (Frame) getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.first(frame); // 先頭のページに切り替え
        });
        add(backButton);
    }

    private boolean checkLogin(String name, String password) {
        try {
            String csvFile = "member.csv";
            BufferedReader br = new BufferedReader(new FileReader(csvFile));

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4 && data[1].equals(name) && data[3].equals(password)) {
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
}
