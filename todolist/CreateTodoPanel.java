
// todo　を新規作成する画面
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class CreateTodoPanel extends Panel {
    private JTextField titleTextField;
    private JTextArea contentTextArea;
    private JTextField createdTextField;
    private JTextField updatedTextField;
    private JTextField deadlineTextField;
    private JTextField priorityTextField;

    ////////////////////////////////// usenameからPermitを得る関数,Todoを
    private String getPermitFromMember(String username) {
        try {
            String csvFile = "member.csv";
            BufferedReader br = new BufferedReader(new FileReader(csvFile));

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2 && data[1].equals(username)) {
                    br.close();
                    return data[4]; // permit from member.csv
                }
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // 該当する番号が見つからない場合はnullを返す
    }
    ///////////////////////////////////////////////////////

    public CreateTodoPanel() {
        setLayout(new GridLayout(7, 2, 8, 10));
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.white);

        // creator
        // JLabel creatorLabel = new JLabel("creator:");
        // titleTextField = new JTextField();

        // タイトル
        JLabel titleLabel = new JLabel("Title:");
        titleTextField = new JTextField();

        // 内容
        JLabel contentLabel = new JLabel("Content:");
        contentTextArea = new JTextArea();
        JScrollPane contentScrollPane = new JScrollPane(contentTextArea);

        // 作成日時
        JLabel createdLabel = new JLabel("Created:");
        createdTextField = new JTextField();

        // 更新日時
        JLabel updatedLabel = new JLabel("Updated:");
        updatedTextField = new JTextField();

        // 締め切り日時
        JLabel deadlineLabel = new JLabel("Deadline:");
        deadlineTextField = new JTextField();

        // 優先度
        JLabel priorityLabel = new JLabel("Priority:");
        priorityTextField = new JTextField();

        // ボタン
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new SaveButtonListener());

        Button backButton = new Button("back");
        backButton.addActionListener(e -> {
            Frame frame = (Frame) getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.previous(frame); // 先頭のページに切り替え
        });

        // テキストボックスをパネルに追加
        // add(creatorLabel);
        add(titleLabel);
        add(titleTextField);
        add(contentLabel);
        add(contentScrollPane);
        add(createdLabel);
        add(createdTextField);
        add(updatedLabel);
        add(updatedTextField);
        add(deadlineLabel);
        add(deadlineTextField);
        add(priorityLabel);
        add(priorityTextField);
        add(saveButton);
        add(backButton);
    }

    class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // 入力された情報を取得
            String title = titleTextField.getText();
            String content = contentTextArea.getText();
            String created = createdTextField.getText();
            String updated = updatedTextField.getText();
            String deadline = deadlineTextField.getText();
            String priority = priorityTextField.getText();

            // ここまは実行されてる、おｋ

            System.out.println(getPermitFromMember(LoginPanel.username));

            // CSVファイルに保存
            try {
                FileWriter writer = new FileWriter("todos.csv", true);
                writer.write(toCSV(LoginPanel.username, getPermitFromMember(LoginPanel.username), title, content,
                        created, updated,
                        deadline, priority));
                writer.write("\n");
                writer.close();
                //System.out.println("mmmmmmmm");

                // 保存完了のメッセージを表示
                System.out.println("保存しました。");
            } catch (IOException ex) {
                ex.printStackTrace();
                // 保存エラーのメッセージを表示
                System.out.println("保存に失敗しました。");
            }

            // 入力フィールドをクリア
            titleTextField.setText("");
            contentTextArea.setText("");
            createdTextField.setText("");
            updatedTextField.setText("");
            deadlineTextField.setText("");
            priorityTextField.setText("");
        }
    }

    // String toCSV(String username, String permit, String title, String content,
    // String created, String updated,
    // String deadline,
    // String priority) {
    // return String.format("%s,%s,%s,%s,%s,%s,%s,%s", username, title, content,
    // created, updated, deadline, priority);
    // }

    String toCSV(String username, String permit, String title, String content, String created, String updated,
            String deadline, String priority) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", username, permit, title, content, created, updated, deadline,
                priority);
    }
}

    // private class SaveButtonListener implements ActionListener {
    // public void actionPerformed(ActionEvent e) {
    // String title = titleTextField.getText();
    // String content = contentTextArea.getText();
    // String created = createdTextField.getText();
    // String updated = updatedTextField.getText();
    // String deadline = deadlineTextField.getText();
    // String priority = priorityTextField.getText();

    // // Create an instance of DisplayTodoPanel and pass the input data
    // DisplayTodoPanel displayPanel = new DisplayTodoPanel(title, content, created,
    // updated, deadline, priority);

    // // Display the panel in a new frame or replace the current panel in your
    // // existing frame
    // JFrame frame = new JFrame("Display Todo");
    // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    // frame.getContentPane().add(displayPanel);
    // frame.pack();
    // frame.setVisible(true);

    // // 保存完了メッセージを表示するダイアログを表示
    // JOptionPane.showMessageDialog(CreateTodoPanel.this, "Todo saved
    // successfully!");

    // // 入力フィールドをクリアする
    // clearFields();
    // }
    // }

    // class SaveButtonListener implements ActionListener {
    // public void actionPerformed(ActionEvent e) {
    // // 入力された情報を取得
    // String title = titleTextField.getText();
    // String content = contentTextArea.getText();
    // String created = createdTextField.getText();
    // String updated = updatedTextField.getText();
    // String deadline = deadlineTextField.getText();
    // String priority = priorityTextField.getText();

    // // CSVファイルに保存
    // try {
    // FileWriter writer = new FileWriter("todos.csv", true);
    // writer.write(toCSV(title, content, created, updated, deadline, priority));
    // writer.write("\n");
    // writer.close();

    // // 保存完了のメッセージを表示
    // System.out.println("保存しました。");
    // } catch (IOException ex) {
    // ex.printStackTrace();
    // // 保存エラーのメッセージを表示
    // System.out.println("保存に失敗しました。");
    // }

    // // 入力フィールドをクリア
    // titleTextField.setText("");
    // contentTextArea.setText("");
    // createdTextField.setText("");
    // updatedTextField.setText("");
    // deadlineTextField.setText("");
    // priorityTextField.setText("");
    // // }
    // }

    // String toCSV(String title, String content, String created, String updated,
    // String deadline, String priority) {
    // return String.format("%s,%s,%s,%s,%s,%s", title, content, created, updated,
    // deadline, priority);
    // }

    // private void saveToDatabase(String title, String content, String created,
    // String updated, String deadline,
    // String priority) {
    // // データベースに接続して保存処理を行うコードを追加する
    // // ここでは、サンプルとしてデータの出力のみ行います
    // System.out.println("Title: " + title);
    // System.out.println("Content: " + content);
    // System.out.println("Created: " + created);
    // System.out.println("Updated: " + updated);
    // System.out.println("Deadline: " + deadline);
    // System.out.println("Priority: " + priority);
    // }

    // private void clearFields() {
    // titleTextField.setText("");
    // contentTextArea.setText("");
    // createdTextField.setText("");
    // updatedTextField.setText("");
    // deadlineTextField.setText("");
    // priorityTextField.setText("");
    // }
