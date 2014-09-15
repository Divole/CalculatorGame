package calcapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Dovile
 * Date: 15-09-14
 * Time: 10:56
 * To change this template use File | Settings | File Templates.
 */
public class QuestioneeUI {
    private JPanel view3;
    private JTextField answer;

    public QuestioneeUI() {
    }

    public JPanel displayQeustioneeScreen(String user){

        view3 = new JPanel();
        view3.setLayout(new BoxLayout(view3, BoxLayout.Y_AXIS));
        JLabel userName = new JLabel(user);
        JPanel actionPanel = new JPanel();
        answer = new JTextField("");
        answer.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();  // ignore event
                }
            }
        });
        answer.setColumns(15);
        view3.add(userName);
        view3.add(Box.createVerticalGlue());
        actionPanel.add(answer);
        view3.add(actionPanel);
        view3.add(createKeyboard());
        view3.add(Box.createVerticalGlue());
        view3.add(createActionSubmit());
        return view3;
    }

    private JPanel createKeyboard(){
        JPanel wrapper = new JPanel(new FlowLayout());
        JPanel keyboard = new JPanel(new GridLayout(3,4));

        for(int i = 9; i >= 0; i--){
            JButton key = new JButton(""+i);
            keyboard.add(key);
            final int num = i;
            key.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String actionString = answer.getText()+ num;
                    answer.setText(actionString);
                }
            });
        }
        wrapper.add(keyboard);
        return wrapper;
    }

    private JPanel createActionSubmit(){
        JPanel wrapper = new JPanel(new FlowLayout());
        JButton submit = new JButton("Submit Answer");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view3.add(new JLabel("Correct Answer"));
                view3.revalidate();
            }
        });
        wrapper.add(submit);
        return wrapper;

    }
}
