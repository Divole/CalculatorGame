package calcserver;

import calcapp.ClientRunnable;
import calcapp.CalcConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class CalcServerTest {
    private CalcServer calcServer = null;
    Integer[] expected_roles = null;
    String[] names = null;
    Integer[] roles = null;
    String[] result_names = null;
    ArrayList<HashMap<Integer, MyThread>> groups = null;
    CalcConnector[] clients;

    @Before
    public void setUp() throws Exception {
        clients = new CalcConnector[14];
        expected_roles = new Integer[]{1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2};
        names = new String[]{"Dovile", "Cynthia", "Sam", "Bruno", "Oscar", "Graham", "Bob", "Maria", "George", "Mat", "Ramunas", "Bart", "Lucia", "Norah"};
        startServer();
        for (int i = 0; i < clients.length; i++){
            CalcConnector calcConnector = new CalcConnector();
            calcConnector.connect();
            calcConnector.logIn(names[i]);
            clients[i]= calcConnector;
        }
        roles = new Integer[14];
        result_names = new String[14];
        int i = 0;
        groups = calcServer.getGroups();
        for (HashMap<Integer, MyThread> g: groups){
            for (MyThread cl: g.values()){
                roles[i]=  cl.getRole();
                result_names[i]=cl.getUserName();
                i++;
            }
        }
    }

    @Test
    public void groupCreationTest() throws IOException, InterruptedException {
        //test if the number of created groups is correct
        int num_groups = groups.size();
        assertEquals(2, num_groups);
    }

    @Test
    public void roleAssignmentTest() throws IOException, InterruptedException {
        //test if the roles to the clients are assigned correctly
        System.out.println(Arrays.toString(expected_roles));
        System.out.println(Arrays.toString(roles));
        assertArrayEquals(expected_roles, roles);
    }
    @Test
    public void nameAssignmentTest() throws IOException, InterruptedException {
        //test if the names of the client are being saved
        assertArrayEquals(names,result_names);
    }
    @Test
    public void serverReceiveAction() throws IOException, InterruptedException {
        // test if each thread on the server receives action
        CalcConnector questioner = clients[0];
        String expected_action = "23-4+567";
        questioner.submitAction(expected_action);
        Thread.sleep(1000);
        for(MyThread cl: groups.get(0).values()){
            assertEquals(expected_action, cl.getAction());
        }
    }
    @Test
    public void serverConnectionTest6() throws IOException, InterruptedException {
        // test if clients receives the action from the server
        CalcConnector questioner = clients[0];
        String expected_action = "23-4+567";
        questioner.submitAction(expected_action);
        Thread.sleep(1000);
        ArrayList<String> result = new ArrayList<>();
        for (int i = 1; i <= 10; i++){
            CalcConnector cc = clients[i];
            assertEquals( expected_roles[i], (Integer)cc.getClientRunnable().getRole());

            if (cc.getClientRunnable().getRole()!= 1){
                System.out.println( "TEST ACTION "+ cc.getClientRunnable().getAction()+", Name: "+ cc.getClientRunnable().getName());
                assertEquals(expected_action, cc.getClientRunnable().getAction());
                result.add(cc.getClientRunnable().getAction());
            }
        }
        assertEquals(9, result.size());
    }


    @After
    public void tearDown() throws Exception {
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