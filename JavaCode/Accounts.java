package ATMManagementSystem;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;


public class Accounts extends ValidationAndGeneration{
    private Connection connection;
    private Scanner scanner;
    public Accounts(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;

    }

    public long open_account(String email){
        if(!account_exist(email)) {
            String open_account_query = "INSERT INTO Accounts(account_number, full_name, email, balance, security_pin, phone_number, adhaar_number, pan_number, card_number, school_name, pet_name) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            scanner.nextLine();
            System.out.print("Enter Full Name: ");
            String full_name = scanner.nextLine();

            double balance = setAmount();
            //scanner.nextLine();

            String security_pin = setPIN();

            long phone_number = setPhoneNumber();

            long adhaar_number = setAdhaarNumber();

            scanner.nextLine();
            String pan_number = setPAN();

            System.out.print("Enter ATM Card Number: ");
            long card_number = scanner.nextLong();
            scanner.nextLine();

            System.out.println("\nAnswer the below Security Questions:");
            System.out.print("Enter First School Name: ");
            String school_name = scanner.nextLine();
            System.out.print("Enter Your Favorite Pet Name: ");
            String pet_name = scanner.nextLine();

            try {
                long account_number = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, full_name);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, security_pin);
                preparedStatement.setLong(6, phone_number);
                preparedStatement.setLong(7, adhaar_number);
                preparedStatement.setString(8, pan_number);
                preparedStatement.setLong(9, card_number);
                preparedStatement.setString(10, school_name);
                preparedStatement.setString(11, pet_name);


                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    return account_number;
                } else {
                    throw new RuntimeException("Account Creation failed!!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account Already Exist");

    }

    public long getAccount_number(String email) {
        String query = "SELECT account_number from Accounts WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("account_number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account Number Doesn't Exist!");
    }



    private long generateAccountNumber() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_number from Accounts ORDER BY account_number DESC LIMIT 1");
            if (resultSet.next()) {
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number+1;
            } else {
                return 100000000;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 100000000;
    }

    public boolean account_exist(String email){
        String query = "SELECT account_number from Accounts WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    public long setAdhaarNumber(){
        System.out.print("Enter Adhaar Number: ");
        long adhaar_number = scanner.nextLong();
        while (!isValidAdhaar(adhaar_number)){
            System.out.print("\nInvalid Adhaar Number. Enter again: ");
            adhaar_number = scanner.nextLong();
        }
        return adhaar_number;
    }


    public String setPAN(){
        System.out.print("Enter PAN Number: ");
        String pan_number = scanner.nextLine();
        while (!isValidPAN(pan_number)){
            System.out.print("Invalid PAN Number. Enter again: ");
            pan_number = scanner.nextLine();
        }
        return pan_number;
    }

    public long setPhoneNumber(){
        System.out.print("Enter Phone Number: ");
        long phone_number = scanner.nextLong();
        while (!isValidMobile(phone_number)){
            System.out.print("Invalid Phone Number. Enter again: ");
            phone_number = scanner.nextLong();
        }
        return phone_number;
    }

    public double setAmount(){
        System.out.print("Enter Initial Amount (Minimum amount: 2000): ");
        double balance = scanner.nextDouble();
        while (balance < 2000){
            System.out.print("The minimum amount should be 2000. \nEnter again the initial amount: ");
            balance = scanner.nextDouble();
        }
        return balance;
    }

}
