package calcserver;

import calcapp.CalcClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class CalcServerTest {
    private CalcServer calcServerMock = null;
    @Before
    public void setUp() throws Exception {
        calcServerMock = Mockito.mock(CalcServer.class);
        for(int i = 0; i<11; i++){
            new CalcClient();
        }
    }
    @Test
    public void serverConnectionTest(){
        Mockito.verifyZeroInteractions(calcServerMock);
    }

}