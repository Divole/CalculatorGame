package calcserver;

import calcapp.CalcConnector;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CalcServerTest {
    private CalcServer calcServer = null;

    //The purpose of this test is to validate the number of clients connected to the server
    //conditions:
    //server has to be running
    //one player has to connect the server
//    @Test
//    public void serverConnectionTest() throws IOException, InterruptedException {
//        startServer();
//        new CalcConnector().connect(9999, "Localhost");
//        ArrayList<HashMap<Integer, MyThread>> groups = calcServer.getGroups();
//        HashMap<Integer, MyThread> g = groups.get(0);
//        int group_size = g.size();
//        assertEquals(1, group_size);
//        calcServer.setRunning(false);
//        Thread.sleep(1000); //wait for a server to shut down
//    }

    //The purpose of this test is to validate the group creation
    //for every 10 clients has to be new group created
//    @Test
//    public void serverConnectionTest2() throws IOException, InterruptedException {
//        startServer();
//        for (int i = 0; i<=11; i++){
//            new CalcConnector().connect(9999, "Localhost");
//        }
//        ArrayList<HashMap<Integer, MyThread>> groups = calcServer.getGroups();
//        int num_groups = groups.size();
//        assertEquals(2, num_groups);
//        calcServer.setRunning(false);
//        Thread.sleep(1000);// wait for a server to shut down
//    }
    // Purpose of this test is to figure out if the roles for each client are assigned properly

    @Test
    public void serverConnectionTest3() throws IOException, InterruptedException {
        Integer[] expected = {1,2,2,2,2,2,2,2,2,2,1,2,2,2};
        startServer();
        for (int i = 0; i<=14; i++){
            new CalcConnector().connect(9999, "Localhost");
        }
        Integer[] roles = new Integer[14];
        int i = 0;
        ArrayList<HashMap<Integer, MyThread>> groups = calcServer.getGroups();
        for (HashMap<Integer, MyThread> g: groups){
            for (MyThread cl: g.values()){
                roles[i]=  cl.getRole();
            }

        }
        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(roles));
        assertArrayEquals(expected, roles);

        calcServer.setRunning(false);
        Thread.sleep(1000);// wait for a server to shut down
    }

    private void startServer() throws IOException {
        calcServer = new CalcServer();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                calcServer.runServer(true);
            }
        };
        new Thread(r).start();
    }


}