package ATMManagementSystem;

import java.util.Random;
import java.util.Scanner;

public class ValidationAndGeneration {
    Scanner scanner = new Scanner(System.in);
    public boolean verifyEmailAndPhone(String Email, long phoneNumber){
        int generatedOTP = generateOTP();
        System.out.printf("OTP successfully sent to %d and %s \n",phoneNumber, Email);
        System.out.print("Enter OTP: ");
        int OTPinput = scanner.nextInt();
        scanner.nextLine();
        if (OTPinput == generatedOTP)
            return true;
        return false;
    }
    public int generateOTP(){
        Random ran = new Random();
        int OTP = ran.nextInt(1000, 9999);
        System.out.printf("\nOTP generated is %d\n", OTP);
        return OTP;
    }

    public String setPIN(){
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();

        while (!isValidPIN(security_pin) || !validatePINComplexity(security_pin)){
            if (!isValidPIN(security_pin)){
                System.out.print("Not Valid PIN.\nEnter 4-Digit PIN only.");
                security_pin = scanner.nextLine();
            }
            else {
                System.out.print("PIN is very simple and easy to guess. \nEnter complex PIN which is hard to guess: ");
                security_pin = scanner.nextLine();
            }
        }
        return security_pin;
    }

    public boolean isValidPIN(String PIN){
        if (PIN.length() == 4)
            return true;
        return false;
    }

    public boolean validatePINComplexity(String PINKey){
        if (PINKey.charAt(0) == PINKey.charAt(1) &&  PINKey.charAt(2) == PINKey.charAt(3))
            return false;
        return !PINKey.equals("1234") && !PINKey.equals("9876");
    }

    public boolean verifySecurityQuestions(String school_name, String pet_name){
        System.out.print("Enter First School Name: ");
        String inputSchoolName = scanner.nextLine();
        System.out.print("Enter Your Favorite Pet Name: ");
        String inputPetName = scanner.nextLine();
        if (school_name.equals(inputSchoolName) && pet_name.equals(inputPetName))
            return true;
        return false;
    }

    public boolean isValidMobile(long mobile){
        String str = String.valueOf(mobile);
        return str.length() == 10 && (str.startsWith("9") || str.startsWith("8") || str.startsWith("7") || str.startsWith("6"));
    }

    public boolean isValidPAN(String pan) {
        return pan != null && pan.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    }


    public boolean isValidAdhaar(long adhaar){
        String str = String.valueOf(adhaar);
        if (str.length() == 12)
            return true;
        return false;
    }
}
