// メインのゲーム画面 画面B

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


// class CreateacountPanel extends Panel {
//     CreateacountPanel() {
//        // final JTextField textField = new JTextField();
//         setLayout(null);

//         // MainPanelの中でさらにCardLayoutで表示の切り替え
//         Panel innerPanel = new Panel();
//         innerPanel.setBounds(50, 50, 600, 330);

//         TextField nameTextField = new TextField();
//         setLayout(new GridLayout(4, 2));
//         // CardLayout innerCardLayout = new CardLayout();
//         // innerPanel.setLayout(innerCardLayout);

//         nameTextField.setBounds(50, 400, 150, 10);
//         add(new JLabel("Username:", SwingConstants.LEFT));
//         // add(textField);
//         add(nameTextField);

//         TextField emailTextField = new TextField();
//         emailTextField.setBounds(210, 400, 150, 30);
//         add(new JLabel("email:", SwingConstants.LEFT));
//         add(emailTextField);

//         TextField passwordTextField = new TextField();
//         passwordTextField.setBounds(370, 400, 150, 30);
//         add(new JLabel("number:", SwingConstants.LEFT));
//         // passwordTextField.setEchoChar('*');
//         add(passwordTextField);

//         // ここで入力データを保存する
//         Button saveButton = new Button("Save");
//         saveButton.setBounds(50, 480, 100, 30);
//         saveButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 // 入力された情報を取得
//                 String name = nameTextField.getText();
//                 String email = emailTextField.getText();
//                 String password = passwordTextField.getText();

//                 // データベースに情報を保存
//                 // データベースに情報を保存
//                 try {
//                     Connection connection = DatabaseUtil.getConnection();
//                     String query = "INSERT INTO user (name, email, password) VALUES (?, ?, ?)";
//                     PreparedStatement statement = connection.prepareStatement(query);
//                     statement.setString(1, name);
//                     statement.setString(2, email);
//                     statement.setString(3, password);
//                     statement.executeUpdate();

//                     // データベースとの接続をクローズ
//                     statement.close();
//                     connection.close();
//                 } catch (SQLException ex) {
//                     ex.printStackTrace();
//                 }
//             }
//         });
//         add(saveButton);

//         // 本来はそれぞれのPanelクラスになりますが、サンプルのためただの色違いパネル

//         Button backButton = new Button("back");

//         backButton.setBounds(550, 10, 100, 25);
//         backButton.addActionListener(e -> {
//             Frame frame = (Frame) getParent();
//             CardLayout cardLayout = (CardLayout) frame.getLayout();
//             cardLayout.first(frame); // 先頭のページに切り替え
//         });
//         add(backButton);
//     }
// }

import java.io.FileWriter;
import java.io.IOException;

public class CreateaccountPanel extends JPanel {
    private JTextField numberTextField;
    private JTextField nameTextField;
    private JTextField emailTextField;
    private JTextField passwordTextField;
    private JTextField permitTextField;

    public CreateaccountPanel() {
        setLayout(new GridLayout(5, 2, 8, 10));
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.white);

        JLabel numberLabel = new JLabel("Number:");
        numberTextField = new JTextField();

        JLabel nameLabel = new JLabel("Name:");
        nameTextField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        emailTextField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordTextField = new JTextField();

        JLabel permitLabel = new JLabel("ShowPertmited(y or n ):");
        permitTextField = new JTextField();
        // JScrollPane pertmitScrollPane = new JScrollPane(permitTextArea);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new SaveButtonListener());

        JButton backButton = new JButton("back");
        // saveButton.addActionListener(new SaveButtonListener());

        //// backbutton
        // setLayout(null);
        // Button backButton = new Button("タイトルに戻る");
        backButton.setBounds(250, 300, 200, 50);
        backButton.addActionListener(e -> {
            Frame frame = (Frame) getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "InitialPanel"); // ページ名を指定して切り替え
        });
        
        add(numberLabel);
        add(numberTextField);
        add(nameLabel);
        add(nameTextField);
        add(emailLabel);
        add(emailTextField);
        add(passwordLabel);
        add(passwordTextField);
        add(permitLabel);
        add(permitTextField);
        add(saveButton);
        add(backButton);
    }

    class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String number = numberTextField.getText();
            String name = nameTextField.getText();
            String email = emailTextField.getText();
            String password = passwordTextField.getText();
            String permit = permitTextField.getText();

            try {
                FileWriter writer = new FileWriter("member.csv", true);
                writer.write(toCSV(number, name, email, password, permit));
                writer.write("\n");
                writer.close();

                JOptionPane.showMessageDialog(CreateaccountPanel.this, "Member saved successfully!");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(CreateaccountPanel.this, "Failed to save member.");
            }

            clearFields();
        }
    }

    String toCSV(String number, String name, String email, String password, String permit) {
        return String.format("%s,%s,%s,%s", number, name, email, password, permit);
    }

    private void clearFields() {
        numberTextField.setText("");
        nameTextField.setText("");
        emailTextField.setText("");
        permitTextField.setText("");
    }
}
