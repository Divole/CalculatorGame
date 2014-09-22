package calcserver;

import common.CalcProtocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class CalcServer {
    private ServerSocket server_socket = null;
    public ArrayList<HashMap<Integer, MyThread>> groups = null;
    public HashMap<Integer, MyThread> clients = null;
    private String actions = null;
    public int index = 0;
    public int questioner = 0;
    private boolean isRunning = false;
    private boolean isBlocked = false;


    public CalcServer() throws IOException {
        int port = 9999;
        groups = new ArrayList<HashMap<Integer, MyThread>>();
        actions = null;
        server_socket = new ServerSocket(port);
    }

    private void closeClients() throws IOException {

        for (HashMap<Integer, MyThread> g: groups){
            for (MyThread cl: g.values()){
                 cl.getClient_socket().close();
            }
        }
    }

    public ArrayList<HashMap<Integer, MyThread>> getGroups(){
        return groups;
    }

    public String getAction(){
        return actions;
    }

    public void runServer(boolean run){
        isRunning = run;
        try {
            while (isRunning){
                if(!isBlocked){
                    System.out.println("_______________________________________________________");
                    System.out.println("...server running...");
                    Runnable connectionLaunched = new Runnable() {
                        @Override
                        public void run() {
                            createConnection();
                        }
                    };
                    new Thread(connectionLaunched).start();
                }
                Thread.sleep(500);
            }
            server_socket.close();
            closeClients();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setRunning(boolean running){
        isRunning = running;
    }

    public void createConnection(){
        isBlocked = true;
        try {
            System.out.println("_______________________________________________________");
            if (groups.isEmpty()){
                clients = new HashMap<Integer, MyThread>();
                groups.add(clients);
                System.out.println("NEW GROUP");
            }else{
                boolean size = false;

                for(HashMap<Integer, MyThread> g: groups){

                    if(g.size() < 10){
                        System.out.println("EXISTING GROUP");
                        clients = g;
                        size = true;
                        break;
                    }
                }
                if (!size){
                    System.out.println("GROUP IS FULL, NEW GROUP CREATED");
                    index = 0;
                    clients = new HashMap<Integer, MyThread>();
                    groups.add(clients);
                }
            }

            MyThread p = new MyThread(server_socket.accept(),clients);
            new Thread(p).start();
            if (index == 0){
                p.setRole(1);
                questioner = index;
            } else{
                p.setRole(2);
            }
            clients.put(index,p);
            index++;
//            System.out.println("Role is "+p.getRole());
//            System.out.println("Group size "+clients.size());
            isBlocked = false;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class MyThread implements Runnable, CalcProtocol {
    private Socket client_socket;
    private String clientName;
    private HashMap<Integer, MyThread> clients;
    private int role = 0;
    private boolean loggedIn = false;
    private String action = null;
    private String answer = null;
    ObjectInputStream in;
    ObjectOutputStream out;

    public MyThread(Socket client_socket,HashMap<Integer, MyThread> clients) {
        this.client_socket = client_socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(client_socket.getInputStream());
            out = new ObjectOutputStream(client_socket.getOutputStream());

            Thread.sleep(500);
            String protocolMessage = in.readUTF();

            if(protocolMessage.equals(CalcProtocol.LOGIN)){
                String userName = in.readUTF();
                setName(userName);
                out.writeUTF(CalcProtocol.ROLE);
                out.flush();
                out.writeUTF(String.valueOf(getRole()));
                out.flush();
                loggedIn = true;
            }
            if (loggedIn){
                if (role == 1){
                    if(in.readUTF().equals(CalcProtocol.ACTION_CLIENT_SERVER)){
                        action = in.readUTF();
                        sendActionToOthers(action); //this method also triggers the method sendActionToClient() within each specific thread
                    }
                }else if(role==2){
                    synchronized (this){
                         this.wait();
                    }
                    sendActionToClient();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }



    private void sendActionToOthers(String action){
        for (MyThread cl: clients.values()){
            if(cl.getRole() == 2){
                cl.setAction(action);
                synchronized (cl){
                    cl.notify();
                }
            }
        }
    }

    private void sendActionToClient() throws IOException {
        out.writeUTF(CalcProtocol.ACTION_SERVER_CLIENT);
        out.flush();
        out.writeUTF(getAction());
        out.flush();
    }

    public void setAction(String string){
        action = string;
    }
    String getAction() {
        return action;
    }
    String getAnswer() {
        return answer;
    }
    void setAnswer(String answer) {
        this.answer = answer;
    }
    public void setRole(int role){
        this.role = role;
    }
    public int getRole(){
        return role;
    }
    public void setName(String name){
        this.clientName =  name;
    }
    public String getUserName(){
        return clientName;
    }
    Socket getClient_socket() {
        return client_socket;
    }
}

class MainClass{
    private static CalcServer server = null;

    public static void main(String[] args) {
        try {
            server = new CalcServer();
            server.runServer(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
