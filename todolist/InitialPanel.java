// Login画面 画面A

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

class InitialPanel extends Panel implements ActionListener {
    private Image img;

    InitialPanel() {
        try {
            // getImageは起動直後画像が出ないことがあるので、読み込みを待ってくれるこちらに変更
            img = ImageIO.read(new File("title.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(null);
        Button creatacountButton = new Button("login");
        creatacountButton.setBounds(250, 100, 200, 50);
        // addActionListenerを3パターンで登録したが大体同じ意味
        // ほかのクラスでは行数を節約できるラムダ方式を採用
        creatacountButton.addActionListener( // 匿名クラスでアクションを指定
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Frame frame = (Frame) getParent();
                        CardLayout cardLayout = (CardLayout) frame.getLayout();
                        cardLayout.show(frame, "LoginPanel"); // ページ名を指定して切り替え
                    }
                });
        // startButton.addActionListener(this); //
        // このクラス(TitlePanel)のactionPerformedでアクションを指定
        add(creatacountButton);


        Button settingButton = new Button("create account");
        settingButton.setBounds(250, 200, 200, 50);
        settingButton.addActionListener( // 匿名クラスでアクションを指定
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Frame frame = (Frame) getParent();
                        CardLayout cardLayout = (CardLayout) frame.getLayout();
                        cardLayout.show(frame, "CreateaccountPanel"); // ページ名を指定して切り替え
                    }
                });
        add(settingButton);

        // // quit
        // Button exitButton = new Button("quit");
        // exitButton.setBounds(250, 300, 200, 50);
        // exitButton.addActionListener(e -> System.exit(0)); // ラムダでアクションを指定
        // add(exitButton);
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