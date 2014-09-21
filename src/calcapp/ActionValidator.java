package calcapp;

import calcapp.Exceptions.AmountOfSymbolsExceededException;
import calcapp.Exceptions.StartWithMathematicalSymbolException;

public class ActionValidator {

    public ActionValidator() {}

    public void validateString(String string) throws StartWithMathematicalSymbolException, AmountOfSymbolsExceededException {
        String[] numbers = string.split("-|\\+|\\*|\\/");
        char c = string.charAt(0);
        if(string.startsWith("+") || string.startsWith("*") || string.startsWith("/")){
            throw new StartWithMathematicalSymbolException("You should not start your mathematical function with mathematical symbols");
        }else if(numbers.length > 4){
            throw new AmountOfSymbolsExceededException("Yoou can not use more than three mathematical symbols");
        }
    }
}
