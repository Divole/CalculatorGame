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
    private JTextField answer_textField;
    private CalcConnector calcConnector;

    public QuestioneeUI(CalcConnector calcConnector) {
        this.calcConnector = calcConnector;
    }

    private String getAnswer(){
        return answer_textField.getText();
    }

    public JPanel displayQeustioneeScreen(String user){

        view3 = new JPanel();
        view3.setLayout(new BoxLayout(view3, BoxLayout.Y_AXIS));
        JLabel userName = new JLabel(user);
        JPanel actionPanel = new JPanel();
        answer_textField = new JTextField("");
        answer_textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();  // ignore event
                }
            }
        });
        answer_textField.setColumns(15);
        view3.add(userName);
        view3.add(Box.createVerticalGlue());
        actionPanel.add(answer_textField);
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
                    String actionString = getAnswer()+ num;
                    answer_textField.setText(actionString);
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
                System.out.println("SUBMIT ANSWER TRIGGERED");
                String input = getAnswer();
                calcConnector.submitAnswer(getAnswer());


            }
        });
        wrapper.add(submit);
        return wrapper;

    }
}
