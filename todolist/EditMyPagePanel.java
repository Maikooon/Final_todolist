import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

// MyPageの編集画面
// 画面の生成部分：MyPagePanelとほとんど同じ
// データの保存部分：SignUpPanelとほとんど同じ

public class EditMyPagePanel extends JPanel {
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JLabel accountTypeLabel;
    private JTextField nameTextField;
    private JRadioButton publicRadioButton;
    private JRadioButton privateRadioButton;

    public EditMyPagePanel() {
        // レイアウト部分：ボーダーレイアウトの中にグリッドレイアウト
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 40, 40, 40));

        // メインのパネル
        JPanel contentPanel = new JPanel(new GridLayout(7, 3, 5, 10));

        JLabel idTitleLabel = new JLabel("User ID:");
        idLabel = new JLabel();
        JLabel nameTitleLabel = new JLabel("Display Name:");
        nameLabel = new JLabel();
        nameTextField = new JTextField();
        JLabel emailTitleLabel = new JLabel("Email:");
        emailLabel = new JLabel();
        passwordLabel = new JLabel();
        JLabel accountTypeTitleLabel = new JLabel("Account Type:");
        accountTypeLabel = new JLabel();
        JLabel accountTypeLabel = new JLabel("Account Type:");
        publicRadioButton = new JRadioButton("Public");
        privateRadioButton = new JRadioButton("Private");

        // メインのパネルにラベルとフィールドを追加
        contentPanel.add(idTitleLabel);
        contentPanel.add(idLabel);
        contentPanel.add(new JLabel()); // レイアウト調整のための空のラベル
        contentPanel.add(nameTitleLabel);
        contentPanel.add(nameLabel);
        contentPanel.add(nameTextField);
        contentPanel.add(emailTitleLabel);
        contentPanel.add(emailLabel);
        contentPanel.add(new JLabel());
        contentPanel.add(accountTypeTitleLabel);
        contentPanel.add(accountTypeLabel);
        contentPanel.add(publicRadioButton);
        contentPanel.add(new JLabel());
        contentPanel.add(new JLabel());
        contentPanel.add(privateRadioButton);

        // Backボタンの生成
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        headerPanel.add(backButton, BorderLayout.WEST);

        // Saveボタンの生成
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(200, 50));
        buttonPanel.add(saveButton);

        // 全体のレイアウト
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadUserData();

        // ボタンアクション
        saveButton.addActionListener(new SaveButtonListener());
        backButton.addActionListener(e -> {
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(EditMyPagePanel.this);
            MyWindow myWindow = (MyWindow) frame;
            myWindow.addPanelsAfterLogin();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "MyPagePanel");
        });
    }

    // Saveボタンが押されたときの処理
    class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // フィールドの値が入力されていなかったら元のデータを代入
            int id = Integer.valueOf(idLabel.getText());
            String name = nameTextField.getText();
            name = (name == null || name.isEmpty()) ? nameLabel.getText() : name;
            String email = emailLabel.getText();
            String password = passwordLabel.getText();
            String accountType = publicRadioButton.isSelected() ? "Public" : "Private";
            accountType = (accountType == null || accountType.isEmpty()) ? accountTypeLabel.getText() : accountType;

            // バリデーションチェック
            if (!isValidName(name)) {
                JOptionPane.showMessageDialog(EditMyPagePanel.this, "Invalid name. Name should contain only alphabets and have a length between 3 and 20 characters.");
                return;
            }

            // member.csvから一度削除
            try {
                File memberFile = new File("member.csv");
                File tempFile = new File("temp.csv");

                BufferedReader br = new BufferedReader(new FileReader(memberFile));
                BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

                String line;
                boolean isFirstLine = true;
                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        bw.write(line);
                        bw.newLine();
                        isFirstLine = false;
                    } else {
                        String[] data = line.split(",");
                        if (!data[0].equals(String.valueOf(id))) {
                            bw.write(line);
                            bw.newLine();
                        }
                    }
                }
                br.close();
                bw.close();

                memberFile.delete(); // member.csvを更新した内容で置き換える
                if (!tempFile.renameTo(new File("member.csv"))) {
                    throw new IOException("Failed to rename temp.csv to member.csv");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // member.csvに再度保存
            try {
                FileWriter writer = new FileWriter("member.csv", true);
                writer.write(toCSV(id, name, email, password, accountType));
                writer.write("\n");
                writer.close();
                // 入力フィールドをクリア
                nameTextField.setText("");
                publicRadioButton.setSelected(false);
                privateRadioButton.setSelected(false);
                JOptionPane.showMessageDialog(null, "Saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(EditMyPagePanel.this);
                MyWindow myWindow = (MyWindow) frame;
                myWindow.addPanelsAfterLogin();
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "MyPagePanel");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to save.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Nameは3-20文字
        private boolean isValidName(String name) {
            return name.length() >= 3 && name.length() <= 20;
        }
    }

    // CSVに保存するためのデータ整形
    private String toCSV(int id, String name, String email, String password, String accountType) {
        return String.format("%d,%s,%s,%s,%s", id, name, email, password, accountType);
    }

    // member.csvからユーザデータを取得
    public void loadUserData() {
        String memberId = String.valueOf(LoginPanel.user_id);
        String memberFilePath = "member.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(memberFilePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data[0].equals(memberId)) {
                    idLabel.setText(data[0]);
                    nameLabel.setText(data[1]);
                    emailLabel.setText(data[2]);
                    passwordLabel.setText(data[3]);
                    accountTypeLabel.setText(data[4]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}