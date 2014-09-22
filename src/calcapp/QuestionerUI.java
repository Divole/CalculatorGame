package calcapp;

import calcapp.Exceptions.AmountOfSymbolsExceededException;
import calcapp.Exceptions.StartWithMathematicalSymbolException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class QuestionerUI {
    private JTextField action;
    private CalcConnector  calcConnector;

    public QuestionerUI(CalcConnector calcConnector) {
        this.calcConnector = calcConnector;
    }
    public JPanel displayQeustionersScreen(String user){

        JPanel view2 = new JPanel();
        view2.setLayout(new BoxLayout(view2, BoxLayout.Y_AXIS));
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

    public String getAtcion(){
        return action.getText();
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
        final JButton submit = new JButton("Submit Action");

        final ActionValidator av = new ActionValidator();
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = getAtcion();
                try {
                    av.validateString(input);
                } catch (StartWithMathematicalSymbolException | AmountOfSymbolsExceededException e1) {
                    e1.printStackTrace();
                }
            }
        });
        wrapper.add(submit);
        return wrapper;
    }


}
