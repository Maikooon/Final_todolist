
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.Arrays;

public class UserdataPanel extends JPanel {
    private JTable table;
    private JButton deleteButton;
    private JButton archiveButton;
    private JButton backButton;

    

    public void deleteFromFile(String fileName, String[] rowData) {
        try {
            File inputFile = new File(fileName);
            File tempFile = new File("temp.csv");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (!Arrays.equals(data, rowData)) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            reader.close();
            writer.close();

            // 一時ファイルを元のファイルにリネームする
            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
            } else {
                throw new IOException("元のファイルの削除に失敗しました。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     public void reloadData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // テーブルの行をクリア

        try {
            String csvFile = "todos.csv";
            BufferedReader br = new BufferedReader(new FileReader(csvFile));

            String line;
            String[] headers = null; // ヘッダー行を格納する変数
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String username = data[0];
                String permit = data[1];
                if (isFirstLine || username.equals(LoginPanel.email) || permit.equals("y")) {
                    if (headers == null) {
                        headers = Arrays.copyOfRange(data, 2, data.length);
                        model.setColumnIdentifiers(headers); // ヘッダー行を追加
                    } else {
                        String[] rowData = Arrays.copyOfRange(data, 2, data.length);
                        model.addRow(rowData);
                    }

                }
                isFirstLine = false;
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UserdataPanel() {
        setLayout(new BorderLayout());

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        deleteButton = new JButton("Delete");
        archiveButton = new JButton("Archive");
        backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(archiveButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    String[] rowData = new String[model.getColumnCount()];
                    for (int i = 0; i < model.getColumnCount(); i++) {
                        rowData[i] = model.getValueAt(selectedRow, i).toString();
                    }
                    System.out.println("mmmmm");
                    deleteFromFile("todos.csv", rowData);
                    model.removeRow(selectedRow);
                }
            }
        });

        archiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    String[] rowData = new String[model.getColumnCount()];
                    for (int i = 0; i < model.getColumnCount(); i++) {
                        rowData[i] = model.getValueAt(selectedRow, i).toString();
                    }

                    writeToFile("Archive.csv", rowData);
                    deleteFromFile("todos.csv", rowData);
                    model.removeRow(selectedRow);
                }
            }
        });

        backButton.addActionListener(e -> {
            Frame frame = (Frame) getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "TodoPanel");
        });
    }

    private void writeToFile(String fileName, String[] rowData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            for (int i = 0; i < rowData.length; i++) {
                writer.write(rowData[i]);
                if (i < rowData.length - 1) {
                    writer.write(",");
                }
            }
            writer.newLine();
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
