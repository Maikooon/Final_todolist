// タイトル画面 画面A

import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class TitlePanel extends Panel implements ActionListener {
    private Image img;

    TitlePanel() {
        try {
            // getImageは起動直後画像が出ないことがあるので、読み込みを待ってくれるこちらに変更
            img = ImageIO.read(new File("title.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(null);
        Button startButton = new Button("join member");
        startButton.setBounds(250, 100, 200, 50);
        // addActionListenerを3パターンで登録したが大体同じ意味
        // ほかのクラスでは行数を節約できるラムダ方式を採用
        startButton.addActionListener(this); // このクラス(TitlePanel)のactionPerformedでアクションを指定
        add(startButton);

        Button settingButton = new Button("add todo");
        settingButton.setBounds(250, 200, 200, 50);
        settingButton.addActionListener( // 匿名クラスでアクションを指定
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Frame frame = (Frame) getParent();
                        CardLayout cardLayout = (CardLayout) frame.getLayout();
                        cardLayout.show(frame, "SettingPanel"); // ページ名を指定して切り替え
                    }
                });
        add(settingButton);

        Button exitButton = new Button("quit");
        exitButton.setBounds(250, 300, 200, 50);
        exitButton.addActionListener(e -> System.exit(0)); // ラムダでアクションを指定
        add(exitButton);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) { // startButtonで呼ばれるメソッド
        Frame frame = (Frame) getParent();
        CardLayout cardLayout = (CardLayout) frame.getLayout();
        cardLayout.next(frame); // 次のページに切り替え
    }
}