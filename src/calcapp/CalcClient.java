package calcapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CalcClient {
    private static CalcClient calcClient;
    private static CalcConnector calcConnector;
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
        calcConnector = new CalcConnector(calcClient);
        calcConnector.connect(9999, "Localhost");
        new Thread(calcConnector).start();
        System.out.println("connected to server");
    }
    public CalcConnector getConnector(){
        return  calcConnector;
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
                final String user = getName();
                submitNameButton.setEnabled(false);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        calcConnector.logIn(user);
                        displayScreen(user, calcConnector);
                    }
                });
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

    public void displayScreen(String user, CalcConnector calcConnector){
        System.out.println("display screen");
        if (CalcClient.calcConnector.getRole() == 1){
            QuestionerUI qUI = new QuestionerUI(calcConnector);
            questionerScreen = qUI.displayQeustionersScreen(user);
            frame.add(questionerScreen);
        }else if(CalcClient.calcConnector.getRole() == 2){
            QuestioneeUI qeustioneeUI = new QuestioneeUI(calcConnector);
            questioneeScreen = qeustioneeUI.displayQeustioneeScreen(user);
            frame.add(questioneeScreen);
        }
        frame.remove(view1);
        frame.revalidate();
    }


}
