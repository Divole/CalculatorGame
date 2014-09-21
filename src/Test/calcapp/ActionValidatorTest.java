package calcapp;

import calcapp.Exceptions.AmountOfSymbolsExceededException;
import calcapp.Exceptions.StartWithMathematicalSymbolException;
import org.junit.Before;
import org.junit.Test;


public class ActionValidatorTest {
    private ActionValidator av;
    @Before
    public void setUp() throws Exception {
        av = new ActionValidator();
    }
    @Test(expected = StartWithMathematicalSymbolException.class)
    public void actionStartsWithMathematicalSign() throws StartWithMathematicalSymbolException, AmountOfSymbolsExceededException {
            av.validateString("*456");
    }
    @Test(expected = AmountOfSymbolsExceededException.class)
    public void tooManySignsTest() throws StartWithMathematicalSymbolException, AmountOfSymbolsExceededException {
        av.validateString("456-5+78*45-5");
    }
    @Test
    public void correctString() throws StartWithMathematicalSymbolException, AmountOfSymbolsExceededException {
        av.validateString("456-5+78-1");
    }
}
