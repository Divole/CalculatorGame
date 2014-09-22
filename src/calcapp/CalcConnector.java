package calcapp;

import common.CalcProtocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class CalcConnector{
    Socket client;
    ObjectInputStream in;
    ObjectOutputStream out;
    ClientRunnable clientRunnable = null;
    int port = 9999;
    String ip = "Localhost";

    public CalcConnector() {
    }

    public void connect() {
        try {
            client = new Socket();
            client.connect(new InetSocketAddress(ip, port));
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
            clientRunnable = new ClientRunnable(in,out,client);
            new Thread(clientRunnable).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void logIn(final String user_name) throws InterruptedException {
        //This method will be called when user click submit user name button
        try {
            out.writeUTF(CalcProtocol.LOGIN);
            out.flush();
            out.writeUTF(user_name);
            out.flush();
            clientRunnable.setName(user_name);
            String response = in.readUTF();
            if (response.equals(CalcProtocol.ROLE)){
                clientRunnable. setRole(Integer.parseInt(in.readUTF()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void submitAction(String action){
        //This method will be called when user click submit action button
        try {
            out.writeUTF(CalcProtocol.ACTION_CLIENT_SERVER);
            out.flush();
            out.writeUTF(action);
            out.flush();
            clientRunnable.setSubmitAction(true);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void submitAnswer(String answer){
        //This method will be called when user click submit answer button
        try {
            out.writeUTF(CalcProtocol.ANSWER_CLIENT_SERVER);
            out.flush();
            out.writeUTF(answer);
            out.flush();
            clientRunnable.setSubmitAnswer(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ClientRunnable getClientRunnable() {
        return clientRunnable;
    }

    public void setClientRunnable(ClientRunnable clientRunnable) {
        this.clientRunnable = clientRunnable;
    }
}
