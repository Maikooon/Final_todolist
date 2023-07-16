import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyWindow extends Frame {
    public static void main(String[] args) {
        MyWindow mw = new MyWindow();
        mw.setVisible(true);
    }

    MyWindow() {
        setTitle("Todo List");
        setSize(1000, 750);
        setLocationRelativeTo(null); // 画面中央に表示

        // 新しいページを追加する時は個々に書いていく
        CardLayout cardLayout = new CardLayout(); // addしたものが1ページずつ紙芝居のように表示されるレイアウト
        setLayout(cardLayout);

        TodoListPanel todoListPanel = new TodoListPanel(); // Todoリストパネルのインスタンスを作成
        DetailTodoPanel detailTodoPanel = new DetailTodoPanel(); // 詳細画面パネルのインスタンスを作成
        EditTodoPanel editTodoPanel = new EditTodoPanel();

        // Todoリストパネルに詳細画面パネルを関連付ける
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
        add(editTodoPanel, "EditTodoPanel"); // 引数に空の文字列を渡す

        addWindowListener(new WinListener());
    }

    class WinListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}
