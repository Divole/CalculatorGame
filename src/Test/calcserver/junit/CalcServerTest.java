package calcserver.junit;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Dovile
 * Date: 11-09-14
 * Time: 12:04
 * To change this template use File | Settings | File Templates.
 */
public class CalcServerTest {
    public CalcServer calcServer;
    @Before
    public void setUp() throws Exception {
           calcServer = new CalcServer();
    }

    @Test
    public void testCompareValues() throws Exception {
        int input1 = 345;
        int input2 = 346;
        int expected = 345;
        assertEquals(expected, calcServer.compareValues(input1, input2));

    }
}
