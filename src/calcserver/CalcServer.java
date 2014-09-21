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
    private ArrayList<String> answers = null;
    public int index = 0;
    public int questioner = 0;
    private boolean isRunning = false;
    private boolean isBlocked = false;


    public CalcServer() throws IOException {
        int port = 9999;
        groups = new ArrayList<HashMap<Integer, MyThread>>();
        actions = null;
        answers = new ArrayList<String>();
        server_socket = new ServerSocket(port);
    }

    public ArrayList<HashMap<Integer, MyThread>> getGroups(){
        return groups;
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
                for(HashMap<Integer, MyThread> hm: groups){

                    if(hm.size() < 10){
                        System.out.println("EXISTING GROUP");
                        clients = hm;
                        size= true;
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

            MyThread p = new MyThread(server_socket.accept(), clients, actions, answers);
            new Thread(p).start();
            if (index == 0){
                p.setRole(1);
                questioner = index;
            } else{
                p.setRole(2);
            }
            clients.put(index,p);
            index++;
            System.out.println("Role is "+p.getRole());
            System.out.println("Group size "+clients.size());
            isBlocked = false;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class MyThread implements Runnable, CalcProtocol {
    private Socket client_socket;
    private String clientName;
    private HashMap<Integer,MyThread> clientList;
    private String actions;
    private ArrayList<String> answers;
    private int role;
    private boolean loggedIn = false;
    private String answer = null;
    private String action;

    public MyThread(Socket client_socket, HashMap<Integer,MyThread> clients, String actions, ArrayList<String> answers) {
        this.client_socket = client_socket;
        this.clientList = clients;
        this.actions = actions;
        this.answers = answers;
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
    private String getUserName(){
        return clientName;
    }
    @Override
    public void run() {
        boolean closed = false;
        try {
            ObjectInputStream in = new ObjectInputStream(client_socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(client_socket.getOutputStream());

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
                System.out.println("USER IS LOGGED IN");
                if (role == 1){
                    if(in.readUTF().equals(CalcProtocol.ACTION_CLIENT_SERVER)){
                        actions = in.readUTF();
                        System.out.println("ATION IS "+ actions);
                    }
                    waitForAnswer();

                }else if (role == 2){
                    waitForAction();
                    out.writeUTF(CalcProtocol.ACTION_SERVER_CLIENT);
                    out.flush();
                    out.writeUTF(actions);
                    out.flush();
                    if(in.readUTF().equals(CalcProtocol.ANSWER_CLIENT_SERVER)){

                        String answer = in.readUTF();
                        System.out.println("ANSWER MSG FROM CLIENT RECEIVED, ANSWER IS "+ answer);
                        answers.add(answer);
                        System.out.println(" ANSWER IS: "+answer);
                    }
                }

            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void waitForAction(){
        while (actions == null){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void waitForAnswer(){
        while (answers.isEmpty()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class MainClass{
    private static CalcServer server = null;

    public static void main(String[] args) {
        try {
            server = new CalcServer();
            server.runServer(true);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
