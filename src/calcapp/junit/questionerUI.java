package calcapp.junit;

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
 * Time: 10:04
 * To change this template use File | Settings | File Templates.
 */
public class QuestionerUI {
    private JPanel view2;
    private JTextField action;

    public QuestionerUI() {
    }
    public JPanel displayQeustionersScreen(String user){

        view2 = new JPanel();
        view2.setLayout(new BoxLayout(view2,BoxLayout.Y_AXIS));
        JLabel userName = new JLabel(user);
        JPanel actionPanel = new JPanel();
        action = new JTextField("");
        action.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();  // ignore event
                }
            }
        });
        action.setColumns(15);
        view2.add(userName);
        view2.add(Box.createVerticalGlue());
        actionPanel.add(action);
        view2.add(actionPanel);
        view2.add(createKeyboard());
        view2.add(Box.createVerticalGlue());
        view2.add(createActionSubmit());
        return view2;
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
                    String actionString = action.getText()+ num;
                    action.setText(actionString);
                }
            });
        }
        wrapper.add(keyboard);

        JPanel symbols = new JPanel();
        String[] symbolKeys = new String[4];
        symbolKeys[0]="+";
        symbolKeys[1]="-";
        symbolKeys[2]="*";
        symbolKeys[3]="/";

        for(int i = 0; i < symbolKeys.length ; i++){
            JButton key = new JButton(symbolKeys[i]);
            symbols.add(key);
            final String operator =  symbolKeys[i];
            key.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String actionString = action.getText()+ operator;
                    action.setText(actionString);

                }
            });
        }
        wrapper.add(symbols);
        return wrapper;
    }

    private JPanel createActionSubmit(){
        JPanel wrapper = new JPanel(new FlowLayout());
        JButton submit = new JButton("Submit Action");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view2.add(new JLabel("1/0"));
                view2.revalidate();
            }
        });
        wrapper.add(submit);
        return wrapper;

    }


}
