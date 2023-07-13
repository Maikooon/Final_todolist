import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TodoPanel extends JPanel {
    public TodoPanel() {
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(700, 500));

        // ボタン配置
        // JPanel buttonPanel = new JPanel();
        setLayout(new GridLayout(7, 2, 8, 10));

        Button showtodoButton = new Button("view Todo");
        showtodoButton.setPreferredSize(new Dimension(200, 50));
        showtodoButton.addActionListener( // 匿名クラスでアクションを指定
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Frame frame = (Frame) getParent();
                        CardLayout cardLayout = (CardLayout) frame.getLayout();
                        cardLayout.show(frame, "UserdataPanel"); // ページ名を指定して切り替え
                    }
                });
        add(showtodoButton);

        Button createtodoButton = new Button("add Todo");
        createtodoButton.setPreferredSize(new Dimension(200, 50));
        createtodoButton.addActionListener( // 匿名クラスでアクションを指定
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Frame frame = (Frame) getParent();
                        CardLayout cardLayout = (CardLayout) frame.getLayout();
                        cardLayout.show(frame, "CreateTodoPanel"); // ページ名を指定して切り替え
                    }
                });
        add(createtodoButton);

        JButton editButton = new JButton("Archive");
        editButton.setPreferredSize(new Dimension(200, 50));
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame frame = (Frame) getParent();
                CardLayout cardLayout = (CardLayout) frame.getLayout();
                cardLayout.show(frame, "ArchivePanel"); // ページ名を指定して切り替え
            }
        });
        add(editButton);

        Button backButton = new Button("back");
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.addActionListener(e -> {
            Frame frame = (Frame) getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.first(frame); // 先頭のページに切り替え
        });
        add(backButton);

    }

    public void makeTaskPanel(String panelTitle) {
        // titleに指定したタイトルテキストをつけてタスクパネルを作ります
        JPanel mainPanel = new JPanel();

        add(mainPanel);
    }

}
