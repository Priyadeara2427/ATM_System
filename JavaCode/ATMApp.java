package ATMManagementSystem;

import java.sql.*;
import java.util.Scanner;

import java.util.Scanner;

public class ATMApp {
    private static final String url = "jdbc:mysql://localhost:3306/atm_system";
    private static final String username = "root";
    private static final String password = "mysql@123";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner =  new Scanner(System.in);
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            String email;
            long account_number;
            long adhaar_number;
            long phone_number;
            String pan_number;

            while(true){
                System.out.println("============ WELCOME TO VIRTUAL ATM SYSTEM =============");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice: ");
                int choice1 = scanner.nextInt();
                switch (choice1){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if(email != null){
                            System.out.println("\nUser Successfully Logged In!");
                            if(!accounts.account_exist(email)){
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                if(scanner.nextInt() == 1) {
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is: " + account_number);
                                }else{
                                    break;
                                }
                            }
                            account_number = accounts.getAccount_number(email);
                            int choice2 = 0;
                            while (choice2 != 10) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Show Account Information");
                                System.out.println("6. Change PIN");
                                System.out.println("7. Forgot PIN");
                                System.out.println("8. Lock ATM Card");
                                System.out.println("9. Unlock ATM Card");
                                System.out.println("10. Log Out");
                                System.out.println("Enter your choice: ");
                                choice2 = scanner.nextInt();
                                switch (choice2) {
                                    case 1:
                                        try{
                                            accountManager.debit_money(account_number);
                                        }
                                        catch (InsufficientBalanceException | MinBalRequiredException e){
                                            System.out.println("Error: "+e);
                                        }
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        accountManager.showAccountInfo(account_number);
                                        break;
                                    case 6:
                                        try {
                                            accountManager.changePIN(account_number);
                                        }catch (PINMismatchException e){
                                            System.out.println("Error "+e);
                                        }
                                        break;
                                    case 7:
                                        accountManager.resetPin(account_number);
                                        break;
                                    case 8:
                                        accountManager.lockATMCard(account_number);
                                        break;
                                    case 9:
                                        accountManager.unlockATMCard(account_number);
                                        break;
                                    case 10:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }

                        }
                        else{
                            System.out.println("Incorrect Email or Password!");
                        }
                    case 3:
                        System.out.println("\nTHANK YOU FOR USING VIRTUAL ATM SYSTEM!!!");
                        System.out.println("Exiting System!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
        }catch (SQLException e ){
            System.out.println("Error "+ e);
        }
    }
}
