package calcapp;

import common.CalcProtocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class CalcConnector implements Runnable, CalcProtocol {
    private static CalcConnector instance = new CalcConnector();
    Socket client;
    ObjectInputStream in;
    ObjectOutputStream out;
    private CalcClient calcClient;
    private int role;

    public CalcConnector() {
    }

    public CalcConnector(CalcClient calcClient) {
        this.calcClient = calcClient;
    }
    public static CalcConnector getInstance(){
        return instance;
    }

    public void connect(int port, String ip) {
        try {
            client = new Socket();
            client.connect(new InetSocketAddress(ip, port));

            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
    }

    public void logIn(final String userName){
        try {
            out.writeUTF(CalcProtocol.LOGIN);
            out.flush();
            out.writeUTF(userName);
            out.flush();
            String response = in.readUTF();
            if (response.equals(CalcProtocol.ROLE)){
                this.setRole(Integer.parseInt(in.readUTF()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void submitAction(String action){
        try {
            out.writeUTF(CalcProtocol.ACTION_CLIENT_SERVER);
            out.flush();
            out.writeUTF(action);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void submitAnswer(String answer){
        try {
            out.writeUTF(CalcProtocol.ANSWER_CLIENT_SERVER);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!sending to the server");
            out.flush();
            out.writeUTF(answer);
            out.flush();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!sending to the server");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setRole(int role){
        System.out.println("set role "+role);
        this.role = role;

    }
    public  int getRole(){
        System.out.println("get role "+role);
        return role;
    }
    public void closeConnection(){
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
