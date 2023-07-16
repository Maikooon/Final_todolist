import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// 各画面を制御する
// addしたものが1ページずつ紙芝居のように表示されるレイアウト
// 起動時に全てのPanelが生成される（→TodoListが最新でない問題）

public class MyWindow extends Frame {
    public static void main(String[] args) {
        MyWindow mw = new MyWindow();
        mw.setVisible(true);
    }

    MyWindow() {
        setTitle("Todo List");
        setSize(1000, 750);
        setLocationRelativeTo(null); // 画面中央に表示

        CardLayout cardLayout = new CardLayout();
        setLayout(cardLayout);

        // 値の受け渡しをする画面同士の制御
        TodoListPanel todoListPanel = new TodoListPanel();
        DetailTodoPanel detailTodoPanel = new DetailTodoPanel();
        EditTodoPanel editTodoPanel = new EditTodoPanel();
        todoListPanel.setDetailPanel(detailTodoPanel);
        detailTodoPanel.setEditlPanel(editTodoPanel);

        add(new InitialPanel(), "InitialPanel");
        add(new SignUpPanel(), "SignUpPanel");
        add(new LoginPanel(), "LoginPanel");
        add(new MainPanel(), "MainPanel");
        add(new CreateTodoPanel(), "CreateTodoPanel");
        add(new ArchivePanel(), "ArchivePanel");
        add(todoListPanel, "TodoListPanel");
        add(detailTodoPanel, "DetailTodoPanel");
        add(editTodoPanel, "EditTodoPanel");

        addWindowListener(new WinListener());
    }

    // 画面を閉じたらプログラム終了
    class WinListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}
