package ATMManagementSystem;

public class PINMismatchException extends Exception{
    PINMismatchException(String message){
        super(message);
    }
}
