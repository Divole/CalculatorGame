package calcapp;

import calcapp.Exceptions.ActionChangeException;
import common.CalcProtocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



public class ClientRunnable implements Runnable, CalcProtocol {
    private Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String name;// user name of a player
    private int role = 0;// role of a player(1=questioner, 2=questionee)
    private String action = null;// action that has to be received from the server
    private String answer = null;// answer that has to be sent
    private String ratio = null;// a ratio of submitted answers
    private String result = null; // a result of your answer(correct or not correct)
    private Boolean submitAction = false;//indicates if the ACTION has been submitted to the server or not
    private Boolean submitAnswer = false;//indicates if the ANSWER has been submitted to the server or not

    public ClientRunnable(ObjectInputStream in, ObjectOutputStream out, Socket client){
        this.client = client;
        this.in = in;
        this.out = out;
    }
    @Override
    public void run(){
        try {
            while (role == 0){
                Thread.sleep(500);
            }

            if (getRole()==1){
                while (!getSubmitAction()){
                    Thread.sleep(500);
                }
                //TODO: implement receiving ratio
                if (in.readUTF().equals(CalcProtocol.RATIO)){
                    System.out.println("ClientRunnable: RATIO is coming");
                    setRatio(in.readUTF());
                }

            }else if (getRole()==2){
                String response = in.readUTF();
                if (response.equals(CalcProtocol.ACTION_SERVER_CLIENT)){
                    String ac = in.readUTF();
                    setAction(ac);

//                    throw new ActionChangeException();

//                    while (!submitAnswer){
//                        Thread.sleep(500);
//                    }
                    String result = in.readUTF();
                    if (result.equals(CalcProtocol.CORRECT_RESULT)){
                        setResult(CalcProtocol.CORRECT_RESULT);
                    }else if(result.equals(CalcProtocol.INCORRECT_RESULT)){
                        setResult(CalcProtocol.INCORRECT_RESULT);
                    }
                    System.out.println("ClientRunnable: result received - "+result);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(int role){
        this.role = role;

    }
    public  int getRole(){
        return role;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public Boolean getSubmitAction() {
        return submitAction;
    }

    public Boolean getSubmitAnswer() {
        return submitAnswer;
    }

    public void setSubmitAnswer(Boolean submitAnswer) {
        this.submitAnswer = submitAnswer;
    }

    public void setSubmitAction(Boolean submitAction) {
        this.submitAction = submitAction;
    }

    public void closeConnection(){
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
