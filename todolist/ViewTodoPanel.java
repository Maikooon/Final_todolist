//package a;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ViewTodoPanel extends JPanel {
    private List<String[]> todos;
    private JTable todoTable;

    public ViewTodoPanel() {
    }


    public ViewTodoPanel(List<String[]> todos) {
        this.todos = todos;
        setLayout(new BorderLayout());

        Button backButton = new Button("back");
        backButton.setBounds(250, 300, 200, 50);
        backButton.addActionListener(e -> {
            Frame frame = (Frame) getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "TodoPanel"); // 一つ前のページに切り替え
        });
        add(backButton, BorderLayout.SOUTH);

        // データを表示するテーブルを作成
        DefaultTableModel model = new DefaultTableModel();
        todoTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(todoTable);
        add(scrollPane, BorderLayout.CENTER);

        // CSVデータをテーブルに追加
        for (String[] todo : todos) {
            model.addRow(todo);
        }
    }

   // Userdata.table;
}
