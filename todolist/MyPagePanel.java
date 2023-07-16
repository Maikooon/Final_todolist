import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

// マイページ画面

public class MyPagePanel extends JPanel {
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel accountTypeLabel;
    // ↓型の名前後で変える
    //private EditTodoPanel editPanel; // 編集画面のデータのセットに用いる

    public MyPagePanel() {
        // レイアウト部分：ボーダーレイアウトの中にグリッドレイアウト
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 40, 20, 40));

        // メインのパネル
        JPanel contentPanel = new JPanel(new GridLayout(9, 2, 5, 10)); // 9行目に追加

        JLabel idTitleLabel = new JLabel("ID:");
        idLabel = new JLabel();
        JLabel nameTitleLabel = new JLabel("Name:");
        nameLabel = new JLabel();
        JLabel emailTitleLabel = new JLabel("Title:");
        emailLabel = new JLabel();
        JLabel accountTypeTitleLabel = new JLabel("Content:");
        accountTypeLabel = new JLabel();

         // メインのパネルにラベルとフィールドを追加
        contentPanel.add(idTitleLabel);
        contentPanel.add(idLabel);
        contentPanel.add(nameTitleLabel);
        contentPanel.add(nameLabel);
        contentPanel.add(emailTitleLabel);
        contentPanel.add(emailLabel);
        contentPanel.add(accountTypeTitleLabel);
        contentPanel.add(accountTypeLabel);

        // Backボタンの生成
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        headerPanel.add(backButton, BorderLayout.WEST);

        // Editボタン・Logoutボタンの生成
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton editButton = new JButton("Edit");
        JButton logoutButton = new JButton("Logout");
        buttonPanel.add(editButton);
        buttonPanel.add(logoutButton);

        // 全体のレイアウト
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // ボタンのアクション設定
        backButton.addActionListener(e -> {
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(MyPagePanel.this);
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "TodoListPanel");
        });
        logoutButton.addActionListener(e -> {
            int option = showConfirmationDialog("Logout Confirmation", "Are you sure you want to log out??");
            if (option == JOptionPane.YES_OPTION) {
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(MyPagePanel.this);
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "LoginPanel");
            }
        });
        // editButton.addActionListener(e -> {
        //     String todoId = idLabel.getText();
        //     editPanel.loadTodoDetails(todoId);
        //     Frame frame = (Frame) SwingUtilities.getWindowAncestor(MyPagePanel.this);
        //     CardLayout cardLayout = (CardLayout) frame.getLayout();
        //     cardLayout.show(frame, "EditTodoPanel");
        //     editPanel.loadTodoDetails(todoId);
        // });
    }


    // Logoutボタンを押されたときのダイヤログの実装
    private int showConfirmationDialog(String title, String message) {
        return JOptionPane.showOptionDialog(this, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[] { "Yes", "Cancel" }, "Yes");
    }

    // 値のセットのためMyWindowで呼び出す
    // public void setEditlPanel(EditTodoPanel editPanel) {
    //     this.editPanel = editPanel;
    // }

}