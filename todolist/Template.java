import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.Panel;

class ShowPanel extends Panel {
    ShowPanel() {
        setLayout(null);
        Button backButton = new Button("タイトルに戻る");
        backButton.setBounds(250, 300, 200, 50);
        backButton.addActionListener(e -> {
            Frame frame = (Frame) getParent().getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.first(frame); // ページ名を指定して切り替え
        });
        add(backButton);
    }
}
