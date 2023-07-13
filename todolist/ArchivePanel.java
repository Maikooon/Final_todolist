
// //ここにアーカイブしたTodoを表示する    

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class ArchivePanel extends JPanel {
    private JTable table;
    private JButton backButton;

    public ArchivePanel() {
        setLayout(new BorderLayout());

        // テーブル作成
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ボタン作成
        // deleteButton = new JButton("Delete");
        // archiveButton = new JButton("Archive");
        backButton = new JButton("back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        ////////// back button
        backButton.addActionListener(e -> {
            Frame frame = (Frame) getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "TodoPanel"); // 一つ前のページに切り替え
        });
    }

    public void reloadData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // テーブルの行をクリア

        // CSVファイル読み込み
        try {
           // System.out.println("ssssssss");
            String csvFile = "Archive.csv";
            BufferedReader br = new BufferedReader(new FileReader(csvFile));

            String line;
            // DefaultTableModel model = new DefaultTableModel();
            table.setModel(model);

            // ヘッダー行を読み込んでテーブルモデルに追加
            if ((line = br.readLine()) != null) {
                String[] headers = line.split(",");
                for (String header : headers) {
                    model.addColumn(header);
                }
            }
            // データ行を読み込んでテーブルモデルに追加
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                model.addRow(data);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            reloadData(); // 画面が表示されるたびにデータをリロード
        }
    }

}