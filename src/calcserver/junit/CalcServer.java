package calcserver.junit;


public class CalcServer {

    public static int compareValues(int userGuess, int correctValue){

        if(userGuess == correctValue){
            return correctValue;

        }
        return 0;
    }

}
