// アプリ本体＆エントリーポイント

import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class MyWindow extends Frame {
    public static void main(String[] args) {
        MyWindow mw = new MyWindow();
        mw.setVisible(true);
    }

    MyWindow() {
        setTitle("Todo List");
        setSize(1000, 800);
        setLocationRelativeTo(null); // 画面中央に表示

        // 新しいページを追加する時は個々に書いていく
        CardLayout cardLayout = new CardLayout(); // addしたものが1ページずつ紙芝居のように表示されるレイアウト
        setLayout(cardLayout);

        add(new InitialPanel(), "InitialPanel");
        add(new SignUpPanel(), "SignUpPanel");
        add(new LoginPanel(), "LoginPanel");
        add(new TodoPanel(), "TodoPanel");
        add(new CreateTodoPanel(), "CreateTodoPanel");
        add(new ViewTodoPanel(), "ViewTodoPanel");
        add(new ArchivePanel(), "ArchivePanel");
        add(new UserdataPanel(), "UserdataPanel");

        addWindowListener(new WinListener());
    }

    class WinListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}
