import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;

public class SwingApplication {
  private static String labelPrefix = "Number of button clicks: ";
  private int numClicks = 0;
  private JLabel label;

  class ButtonAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      numClicks++;
      label.setText(labelPrefix + numClicks);
    }
  }

  public Component createComponents() {
    label = new JLabel(labelPrefix + "0    ");
    JButton button = new JButton("I'm a Swing button!");

    ButtonAction buttonListener = new ButtonAction();
    button.addActionListener( buttonListener );

    JPanel pane = new JPanel();
    pane.setBorder(BorderFactory.createEmptyBorder( 30, 30, 10, 30 ));
    pane.setLayout(new GridLayout(0, 1));
    pane.add(button);
    pane.add(label);

    return pane;
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("SwingApplication");
    SwingApplication app = new SwingApplication();
    Component contents = app.createComponents();
    frame.getContentPane().add(contents, BorderLayout.CENTER);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
