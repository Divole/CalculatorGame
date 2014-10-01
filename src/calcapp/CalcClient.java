package calcapp;

import calcapp.Exceptions.ActionChangeException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CalcClient {
    private JPanel view1;
    private JPanel questionerScreen;
    private JPanel questioneeScreen;
    private JFrame frame;
    private JTextField nameField;
    private JButton submitNameButton;
    private static CalcConnector calcConnector;
    QuestionerUI qUI;
    QuestioneeUI qeustioneeUI;

    public CalcClient(){
        startApp();
    }

    public static void main(String[] args) {
        new CalcClient();

    }
    public CalcConnector getConnector(){
        return calcConnector;
    }

    private void startApp(){


            calcConnector = new CalcConnector();
            calcConnector.connect();

        System.out.println("connected to server");
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
            final String user = getName();
            submitNameButton.setEnabled(false);
            try {
                calcConnector.logIn(user);
                displayScreen(user);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            }
        });

        view1.add(nameField);
        view1.add(submitNameButton);
        frame.setVisible(true);

    }
    private String getName(){
        String name;
        name = nameField.getText();
        return name;
    }

    public static CalcConnector getCalcConnector() {
        return calcConnector;
    }

    public void displayScreen(String user){
        System.out.println("display screen");
        frame.remove(view1);
        if (getCalcConnector().getClientRunnable().getRole() == 1){
            qUI = new QuestionerUI(getCalcConnector());
            questionerScreen = qUI.displayQeustionersScreen(user);
            frame.add(questionerScreen);
            frame.revalidate();
        }else if(getCalcConnector().getClientRunnable().getRole() == 2){
            qeustioneeUI = new QuestioneeUI(getCalcConnector());
            try {
                questioneeScreen = qeustioneeUI.displayQeustioneeScreen(user);
            } catch (ActionChangeException e) {
                qeustioneeUI.resetAction();
            }
            frame.add(questioneeScreen);
            frame.revalidate();
            System.out.println("display screen");
        }



    }

    public QuestionerUI getqUI() {
        return qUI;
    }

    public QuestioneeUI getQeustioneeUI() {
        return qeustioneeUI;
    }

}
