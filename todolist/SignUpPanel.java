// SignUp画面
// ユーザがフィールドにユーザ情報を入れたらそれを取得してバリデーションの確認をし、OKだったらCSVファイルにユーザ情報を保存

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.regex.Pattern;

public class SignUpPanel extends JPanel {
    private JTextField nameTextField;
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JPasswordField passwordConfirmField;
    private JRadioButton publicRadioButton;
    private JRadioButton privateRadioButton;

    // ラベルとボタンの配置
    public SignUpPanel() {
        setLayout(new GridLayout(7, 2, 8, 10));
        setPreferredSize(new Dimension(400, 400));

        JLabel nameLabel = new JLabel("Display Name:");
        nameTextField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        emailTextField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JLabel passwordConfirmLabel = new JLabel("Password Confirm:");
        passwordConfirmField = new JPasswordField();

        JLabel accountTypeLabel = new JLabel("Account Type:");
        publicRadioButton = new JRadioButton("Public");
        privateRadioButton = new JRadioButton("Private");

        ButtonGroup accountTypeButtonGroup = new ButtonGroup();
        accountTypeButtonGroup.add(publicRadioButton);
        accountTypeButtonGroup.add(privateRadioButton);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(new signUpButtonListener());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            Frame frame = (Frame) getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "InitialPanel");
        });

        add(nameLabel);
        add(nameTextField);
        add(emailLabel);
        add(emailTextField);
        add(passwordLabel);
        add(passwordField);
        add(passwordConfirmLabel);
        add(passwordConfirmField);
        add(accountTypeLabel);
        add(publicRadioButton);
        add(new JLabel()); // レイアウトを調整するための空のラベル
        add(privateRadioButton);
        add(signUpButton);
        add(backButton);
    }

    // 「Sign Up」ボタンが押されたときの処理（フィールドの値を取得してバリデーションチェックをする）
    class signUpButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = nameTextField.getText();
            String email = emailTextField.getText();
            String password = new String(passwordField.getPassword());
            String passwordConfirm = new String(passwordConfirmField.getPassword());
            String accountType = publicRadioButton.isSelected() ? "Public" : "Private";

            // バリデーションチェック
            if (!isValidName(name)) {
                JOptionPane.showMessageDialog(SignUpPanel.this, "Invalid name. Name should contain only alphabets and have a length between 3 and 20 characters.");
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(SignUpPanel.this, "Invalid email.");
                return;
            }

            if (!isUniqueEmail(email)) {
                JOptionPane.showMessageDialog(SignUpPanel.this, "Email address already exists. Please choose a different email.");
                return;
            }

            if (!isValidPassword(password)) {
                JOptionPane.showMessageDialog(SignUpPanel.this, "Invalid password. Password should contain 8 to 20 characters, at least one letter and one digit.");
                return;
            }

            if (!password.equals(passwordConfirm)) { // PasswordとConfirm Passwordが一致するか確認
                JOptionPane.showMessageDialog(SignUpPanel.this, "Passwords do not match.");
                return;
            }

            if (!isAccountTypeSelected()) {
                JOptionPane.showMessageDialog(SignUpPanel.this, "Please select an account type.");
                return;
            }

            // IDの付与
            int id = assignId();

            // ユーザ情報の保存
            String userData = String.format("%d,%s,%s,%s,%s", id, name, email, password, accountType);
            saveUserData(userData);

            JOptionPane.showMessageDialog(SignUpPanel.this, "Member saved successfully!");
            clearFields();
        }
    }

    // Nameは3-20文字
    private boolean isValidName(String name) {
        return Pattern.matches("^[A-Za-z]{3,20}$", name);
    }

    // Emailは正式な形で
    private boolean isValidEmail(String email) {
        return Pattern.matches("^[\\w\\.-]+@[\\w\\.-]+\\.\\w+$", email);
    }

    // Emailは他のユーザと被らない
    private boolean isUniqueEmail(String email) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("member.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[2].equals(email)) {
                    br.close();
                    return false; // 同じEmailが既に存在する場合はfalseを返す
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true; // ユニークなEmailの場合はtrueを返す
    }

    // パスワードは8-20文字で英数字を混ぜる
    private boolean isValidPassword(String password) {
        return Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,20}$", password);
    }

    // アカウントタイプは選択必須
    private boolean isAccountTypeSelected() {
        return publicRadioButton.isSelected() || privateRadioButton.isSelected();
    }

    // 現在のユーザの中で最もidの値が大きいユーザのidに1を足したidを新規ユーザに付与する（1から昇順にidを付与する）
    private int assignId() {
        int maxId = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader("member.csv"));
            String line;
            boolean isFirstLine = true; // ヘッダ行をスキップするためのフラグ
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // ヘッダ行をスキップ
                }
                String[] data = line.split(",");
                if (data.length >= 1) {
                    int id = Integer.parseInt(data[0]);
                    if (id > maxId) {
                        maxId = id;
                    }
                }
            }
            br.close();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return maxId + 1;
    }

    // CSVファイルにユーザデータを保存
    private void saveUserData(String userData) {
        try {
            FileWriter writer = new FileWriter("member.csv", true);
            writer.write(userData + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(SignUpPanel.this, "Failed to save member.");
        }
    }

    // フィールドの値を空にする
    private void clearFields() {
        nameTextField.setText("");
        emailTextField.setText("");
        passwordField.setText("");
        passwordConfirmField.setText("");
        publicRadioButton.setSelected(false);
        privateRadioButton.setSelected(false);
    }
}
