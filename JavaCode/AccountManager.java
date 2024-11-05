package ATMManagementSystem;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class AccountManager extends ValidationAndGeneration {
    private Connection connection;
    private Scanner scanner;
    AccountManager(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }


    public void credit_money(long account_number)throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if(account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and security_pin = ? ");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, account_number);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Rs."+amount+" credited Successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }else{
                    System.out.println("Invalid Security Pin!");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void debit_money(long account_number) throws SQLException, InsufficientBalanceException, MinBalRequiredException {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if(account_number!=0) {

                PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? and security_pin = ? ");
                preparedStatement2.setLong(1, account_number);
                preparedStatement2.setString(2, security_pin);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                if(resultSet2.next()){
                    double balance = resultSet2.getDouble("balance");
                    if (amount > balance){
                        throw new InsufficientBalanceException("Transaction Failed! Balance is not sufficient");
                    }
                    else if (balance - amount < 2000){
                        throw new MinBalRequiredException("Transaction Failed ! Balance goes down to Minimum required balance. ");
                    }
                }else{
                    System.out.println("Invalid Pin!");
                }

                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and security_pin = ? ");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount<=current_balance){
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, account_number);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Rs."+amount+" debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient Balance!");
                    }
                }else{
                    System.out.println("Invalid Pin!");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number = scanner.nextLong();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ? ");
                preparedStatement.setLong(1, sender_account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount<=current_balance){

                        // Write debit and credit queries
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                        String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";

                        // Debit and Credit prepared Statements
                        PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);

                        // Set Values for debit and credit prepared statements
                        creditPreparedStatement.setDouble(1, amount);
                        creditPreparedStatement.setLong(2, receiver_account_number);
                        debitPreparedStatement.setDouble(1, amount);
                        debitPreparedStatement.setLong(2, sender_account_number);
                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
                        int rowsAffected2 = creditPreparedStatement.executeUpdate();
                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                            System.out.println("Transaction Successful!");
                            System.out.println("Rs."+amount+" Transferred Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient Balance!");
                    }
                }else{
                    System.out.println("Invalid Security Pin!");
                }
            }else{
                System.out.println("Invalid account number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void getBalance(long account_number){
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance: "+balance);
            }else{
                System.out.println("Invalid Pin!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void showAccountInfo(long account_number){
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try{

            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT full_name FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement1.setLong(1, account_number);
            preparedStatement1.setString(2, security_pin);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            if(resultSet1.next()){
                String name = resultSet1.getString("full_name");
                System.out.println("==========================================================================================");
                System.out.println("\tFull Name: "+name);
            }else{
                System.out.println("Invalid Pin!");
            }

            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT email FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement2.setLong(1, account_number);
            preparedStatement2.setString(2, security_pin);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            if(resultSet2.next()){
                String email = resultSet2.getString("email");
                System.out.println("\tEmail: "+email);
            }else{
                System.out.println("Invalid Pin!");
            }

            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT phone_number FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement3.setLong(1, account_number);
            preparedStatement3.setString(2, security_pin);
            ResultSet resultSet3 = preparedStatement3.executeQuery();
            if(resultSet3.next()){
                long phone_number = resultSet3.getLong("phone_number");
                System.out.println("\tPhone Number: "+phone_number);
            }else{
                System.out.println("Invalid Pin!");
            }

            PreparedStatement preparedStatement4 = connection.prepareStatement("SELECT account_number FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement4.setLong(1, account_number);
            preparedStatement4.setString(2, security_pin);
            ResultSet resultSet4 = preparedStatement4.executeQuery();
            if(resultSet4.next()){
                account_number = resultSet4.getLong("account_number");
                System.out.println("\tAccount Number: "+account_number);
            }else{
                System.out.println("Invalid Pin!");
            }

            PreparedStatement preparedStatement5 = connection.prepareStatement("SELECT adhaar_number FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement5.setLong(1, account_number);
            preparedStatement5.setString(2, security_pin);
            ResultSet resultSet5 = preparedStatement5.executeQuery();
            if(resultSet5.next()){
                long adhaar_number = resultSet5.getLong("adhaar_number");
                System.out.println("\tAdhaar Number: "+adhaar_number);
            }else{
                System.out.println("Invalid Pin!");
            }

            PreparedStatement preparedStatement8 = connection.prepareStatement("SELECT card_number FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement8.setLong(1, account_number);
            preparedStatement8.setString(2, security_pin);
            ResultSet resultSet8 = preparedStatement8.executeQuery();
            if(resultSet8.next()){
                long card_number = resultSet8.getLong("card_number");
                System.out.println("\tATM Card Number: "+card_number);
            }else{
                System.out.println("Invalid Pin!");
            }

            PreparedStatement preparedStatement9 = connection.prepareStatement("SELECT status FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement9.setLong(1, account_number);
            preparedStatement9.setString(2, security_pin);
            ResultSet resultSet9 = preparedStatement9.executeQuery();
            if(resultSet9.next()){
                String status = resultSet9.getString("status");
                System.out.println("\tAccount Status: "+status);
            }else{
                System.out.println("Invalid Pin!");
            }

            PreparedStatement preparedStatement6 = connection.prepareStatement("SELECT pan_number FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement6.setLong(1, account_number);
            preparedStatement6.setString(2, security_pin);
            ResultSet resultSet6 = preparedStatement6.executeQuery();
            if(resultSet6.next()){
                String pan_number = resultSet6.getString("pan_number");
                System.out.println("\tPAN Number: "+pan_number);
            }else{
                System.out.println("Invalid Pin!");
            }

            PreparedStatement preparedStatement7 = connection.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement7.setLong(1, account_number);
            preparedStatement7.setString(2, security_pin);
            ResultSet resultSet7 = preparedStatement7.executeQuery();
            if(resultSet7.next()){
                double balance = resultSet7.getDouble("balance");
                System.out.println("\tBalance: "+balance);
                System.out.println("===========================================================================================");
            }else{
                System.out.println("Invalid Pin!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public void changePIN(long account_number) throws PINMismatchException {
        scanner.nextLine();
        String email = null, school_name = null, pet_name = null;
        long phone_number = 0;

        System.out.print("Enter Old Security Pin: ");
        String security_pin = scanner.nextLine();

        try {

            PreparedStatement preparedStatement0 = connection.prepareStatement("SELECT security_pin FROM Accounts WHERE account_number = ?");
            preparedStatement0.setLong(1, account_number);
            ResultSet resultSet0 = preparedStatement0.executeQuery();
            if (resultSet0.next()) {
                String stored_pin = resultSet0.getString("security_pin");
                if (!security_pin.equals(stored_pin)){
                    throw new PINMismatchException("Entered PIN is wrong.");
                }
            }

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT school_name FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                school_name = resultSet.getString("school_name");
            }

            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT pet_name FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement1.setLong(1, account_number);
            preparedStatement1.setString(2, security_pin);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            if (resultSet1.next()) {
                pet_name = resultSet1.getString("pet_name");
            }

            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT email FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement2.setLong(1, account_number);
            preparedStatement2.setString(2, security_pin);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            if (resultSet2.next()) {
                email = resultSet2.getString("email");
            }

            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT phone_number FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement3.setLong(1, account_number);
            preparedStatement3.setString(2, security_pin);
            ResultSet resultSet3 = preparedStatement3.executeQuery();
            if (resultSet3.next()) {
                phone_number = resultSet3.getLong("phone_number");
            }

            String new_PIN = null;
            try {
                new_PIN = getNewPIN(email, phone_number, school_name, pet_name);
            }
            catch (VerificationFailedException e){
                System.out.println("Error "+ e);
                return;
            }

            String updatePinQuery = "UPDATE accounts SET security_pin = ? WHERE account_number = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updatePinQuery);
            updateStatement.setString(1, new_PIN);     // First placeholder for the new PIN
            updateStatement.setLong(2, account_number);

            int rowsAffected = updateStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("PIN updated successfully.");
            } else {
                System.out.println("Account not found or PIN update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String getNewPIN(String Email, long mobileNumber, String school_name, String pet_name) throws VerificationFailedException{
        int count = 3;
        boolean phonevaildity = verifyEmailAndPhone(Email, mobileNumber);
        while (!phonevaildity && --count >= 0) {
            if (count == 0){
                throw new VerificationFailedException("The Account is Locked is 24 hours. Come after that. ");
            }
            else {
                System.out.printf("Verification for Email and Phone Failed! \n\tNumber of Attempts left to change PIN: %d .\n",count);
                System.out.print("Try Again.");
                phonevaildity = verifyEmailAndPhone(Email, mobileNumber);
            }
        }
        count = 3;
        boolean security = verifySecurityQuestions(school_name, pet_name);
        while (!security && --count>=0){
            if (count== 0){
                throw new VerificationFailedException("The Account is Locked is 24 hours. Come after that. ");
            }
            else {
                System.out.printf("Verification for Security Questions Failed! \n\tNumber of Attempts left to change PIN %d: .\n", count);
                System.out.print("Try Again.");
                security = verifySecurityQuestions(school_name, pet_name);
            }
        }
        System.out.println("Now Generate New PIN: ");
        String newPIN = setPIN();
        return newPIN;
    }

    public void lockATMCard(long account_number){
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        String email=null, school_name=null, pet_name=null;
        long phone_number=0;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT school_name FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                school_name = resultSet.getString("school_name");
            }

            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT pet_name FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement1.setLong(1, account_number);
            preparedStatement1.setString(2, security_pin);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            if(resultSet1.next()){
                pet_name = resultSet1.getString("pet_name");
            }

            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT email FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement2.setLong(1, account_number);
            preparedStatement2.setString(2, security_pin);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            if(resultSet2.next()){
                email = resultSet2.getString("email");
            }

            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT phone_number FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement3.setLong(1, account_number);
            preparedStatement3.setString(2, security_pin);
            ResultSet resultSet3 = preparedStatement3.executeQuery();
            if(resultSet3.next()){
                phone_number = resultSet3.getLong("phone_number");
            }

            if (verifyEmailAndPhone(email, phone_number) && verifySecurityQuestions(school_name, pet_name)){
                PreparedStatement preparedStatement0 = connection.prepareStatement(
                        "UPDATE Accounts SET status = 'locked' WHERE account_number = ? AND security_pin = ?"
                );
                preparedStatement0.setLong(1, account_number);
                preparedStatement0.setString(2, security_pin);

                int rowsUpdated = preparedStatement0.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Account locked successfully.");
                } else {
                    System.out.println("Invalid Pin or already locked.");
                }
            }
            else{
                System.out.println("Required Verification failed.");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void resetPin(long accountNumber) {
        scanner.nextLine();
        System.out.print("Enter your first school name: ");
        String schoolName = scanner.nextLine();
        System.out.print("Enter your favorite pet's name: ");
        String petName = scanner.nextLine();
        try {
            String verifyQuery = "SELECT * FROM accounts WHERE account_number = ? AND school_name = ? AND pet_name = ?";
            PreparedStatement verifyStatement = connection.prepareStatement(verifyQuery);
            verifyStatement.setLong(1, accountNumber);
            verifyStatement.setString(2, schoolName);
            verifyStatement.setString(3, petName);

            ResultSet resultSet = verifyStatement.executeQuery();
            if (resultSet.next()) {
                System.out.print("Enter your new PIN: ");
                String newPin = scanner.nextLine();

                String updatePinQuery = "UPDATE accounts SET security_pin = ? WHERE account_number = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updatePinQuery);
                updateStatement.setString(1, newPin);
                updateStatement.setLong(2, accountNumber);

                int rowsAffected = updateStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("PIN updated successfully.");
                }
                else {
                    System.out.println("Failed to update PIN. Please try again.");
                }
            }
            else {
                System.out.println("Verification failed. Security answers do not match.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void unlockATMCard(long account_number) {
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        String email = null, school_name = null, pet_name = null;
        long phone_number = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT school_name FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                school_name = resultSet.getString("school_name");
            }

            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT pet_name FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement1.setLong(1, account_number);
            preparedStatement1.setString(2, security_pin);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            if (resultSet1.next()) {
                pet_name = resultSet1.getString("pet_name");
            }
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT email FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement2.setLong(1, account_number);
            preparedStatement2.setString(2, security_pin);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            if (resultSet2.next()) {
                email = resultSet2.getString("email");
            }

            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT phone_number FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement3.setLong(1, account_number);
            preparedStatement3.setString(2, security_pin);
            ResultSet resultSet3 = preparedStatement3.executeQuery();
            if (resultSet3.next()) {
                phone_number = resultSet3.getLong("phone_number");
            }
            if (verifyEmailAndPhone(email, phone_number) && verifySecurityQuestions(school_name, pet_name)) {
                PreparedStatement preparedStatement0 = connection.prepareStatement(
                        "UPDATE accounts SET status = 'Active' WHERE account_number = ? AND security_pin = ?"
                );
                preparedStatement0.setLong(1, account_number);
                preparedStatement0.setString(2, security_pin);

                int rowsUpdated = preparedStatement0.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Account unLocked successfully.");
                } else {
                    System.out.println("Invalid Pin or already unlocked.");
                }
            } else {
                System.out.println("Required Verification failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


