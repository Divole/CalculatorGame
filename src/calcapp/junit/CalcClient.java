package calcapp.junit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalcClient {
    private static CalcClient calcClient;
    private JPanel view1;
    private JPanel questionerScreen;
    private JPanel questioneeScreen;
    private JFrame frame;
    private JTextField nameField;
    private JButton submitNameButton;
    private int role;


    public CalcClient(){
        startApp();
    }

    public static void main(String[] args) {
        calcClient = new CalcClient();

    }
    private void startApp(){
        frame =  new JFrame("Calculator Game");
        frame.setSize(300, 400);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        view1 = new JPanel();
        view1.setLayout(new FlowLayout());
        frame.add(view1);
        nameField = new JTextField("");
        nameField.setColumns(15);
        submitNameButton = new JButton("Submit Name");
        submitNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = getName();
                frame.remove(view1);
                if (getRole() == 1){
                    QuestionerUI qUI = new QuestionerUI();
                    questionerScreen = qUI.displayQeustionersScreen(user);
                    frame.add(questionerScreen);
                }else if(getRole() == 2){
                    QuestioneeUI qeustioneeUI = new QuestioneeUI();
                    questioneeScreen = qeustioneeUI.displayQeustioneeScreen(user);
                    frame.add(questioneeScreen);

                }
                frame.revalidate();
            }
        });

        view1.add(nameField);
        view1.add(submitNameButton);
        //--------------------------------------------------------
        JPanel rolePanel = new JPanel(new FlowLayout());
        JRadioButton questioner = new JRadioButton("Questioner", false);
        questioner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRole(1);
                System.out.println("Role is set to"+getRole());
            }
        });
        JRadioButton questionee = new JRadioButton("Questionee", false);
        questionee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRole(2);
                System.out.println("Role is set to"+getRole());
            }
        });
        rolePanel.add(questioner);
        rolePanel.add(questionee);
        view1.add(rolePanel);
        //--------------------------------------------------------
        frame.setVisible(true);

    }
    private String getName(){
        String name;
        name = nameField.getText();
        System.out.println(name);
        return name;
    }
    private void setRole(int role){
        this.role = role;
    }
    private int getRole(){
        return role;
    }


}
