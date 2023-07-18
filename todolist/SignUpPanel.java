import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.regex.Pattern;
//for fash
import java.security.MessageDigest;

// SignUp画面

public class SignUpPanel extends JPanel {

    private JTextField nameTextField;
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JPasswordField passwordConfirmField;
    private JRadioButton publicRadioButton;
    private JRadioButton privateRadioButton;

    public SignUpPanel() {
        // レイアウト部分：ボーダーレイアウトの中にグリッドレイアウト
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 40, 40, 40));

        // タイトルラベルの作成
        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

        // メインのパネル
        JPanel contentPanel = new JPanel(new GridLayout(9, 2, 5, 10));
        JLabel SpaceLabel = new JLabel("");
        // nameTextField = new JTextField();
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
        ButtonGroup accountTypeGroup = new ButtonGroup();
        accountTypeGroup.add(publicRadioButton);
        accountTypeGroup.add(privateRadioButton);

        // メインのパネルにラベルとフィールドを追加
        contentPanel.add(SpaceLabel);
        contentPanel.add(new JLabel());
        contentPanel.add(nameLabel);
        contentPanel.add(nameTextField);
        contentPanel.add(emailLabel);
        contentPanel.add(emailTextField);
        contentPanel.add(passwordLabel);
        contentPanel.add(passwordField);
        contentPanel.add(passwordConfirmLabel);
        contentPanel.add(passwordConfirmField);
        contentPanel.add(accountTypeLabel);
        contentPanel.add(publicRadioButton);
        contentPanel.add(new JLabel()); // レイアウトを調整するための空のラベル
        contentPanel.add(privateRadioButton);

        // Backボタンとタイトルを含むパネル
        JButton backButton; // Backボタンの定義
        backButton = new JButton("Back");
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(backButton, BorderLayout.WEST);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Sign Upボタンの生成
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setPreferredSize(new Dimension(200, 50));
        buttonPanel.add(signUpButton);

        // 全体のレイアウト
        // setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // ボタンアクション
        signUpButton.addActionListener(new signUpButtonListener());
        backButton.addActionListener(e -> {
            Frame frame = (Frame) getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "InitialPanel");
        });
    }

    // Sign Upボタンが押されたときの処理（フィールドの値を取得してバリデーションチェック）
    class signUpButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // 入力された情報を取得
            String name = nameTextField.getText();
            String email = emailTextField.getText();
            String password = new String(passwordField.getPassword());
            String passwordConfirm = new String(passwordConfirmField.getPassword());
            String accountType = publicRadioButton.isSelected() ? "Public" : "Private";

            // バリデーションチェック
            if (!isValidName(name)) {
                JOptionPane.showMessageDialog(SignUpPanel.this,
                        "Invalid name. Name should be between 3 and 30 characters.");
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(SignUpPanel.this, "Invalid email.");
                return;
            }

            if (!isUniqueEmail(email)) {
                JOptionPane.showMessageDialog(SignUpPanel.this,
                        "Email address already exists. Please choose a different email.");
                return;
            }

            if (!isValidPassword(password)) {
                JOptionPane.showMessageDialog(SignUpPanel.this,
                        "Invalid password. Password should contain 8 to 20 characters, at least one letter and one digit.");
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
            clearFields();
            JOptionPane.showMessageDialog(SignUpPanel.this, "Member saved successfully!");
            Frame frame = (Frame) getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "LoginPanel");
        }
    }

    // Nameは3-20文字
    private boolean isValidName(String name) {
        return name.length() >= 3 && name.length() <= 20;
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

    // hash-function for passoword
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

    // パスワードは8-20文字で英数字を混ぜる,のちにHash関数で検証
    private boolean isValidPassword(String password) {
        String hashedPassword = hashPassword(password);
       // System.out.println(hashedPassword);
        return Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,20}$", hashedPassword);
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
