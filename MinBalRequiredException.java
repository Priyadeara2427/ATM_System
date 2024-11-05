package ATMManagementSystem;

public class MinBalRequiredException extends Exception{
    public MinBalRequiredException(String message){
        super(message);
    }
}
