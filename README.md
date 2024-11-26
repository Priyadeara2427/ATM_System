# <img src="https://github.com/user-attachments/assets/0b5627f2-60cd-413e-abe8-4cd6a232ce00" alt="image" width="30"/> Virtual ATM System.

🎉Welcome to the ATM System! This Java-based application allows you to manage user accounts, perform transactions, and ensure the security of financial data. 

This project aims at creating a scenario that depicts ATM operations in a program environment with the help of JAVA. The system also provides capability for performing various banking transactions including checking on balance, depositing and withdrawal of cash. Besides, if any user attempts to access the Virtual ATM, he or she must be authenticated and granted a password to proceed with the operations. The project also seeks to show how an ATM responds to secure and efficient transaction management and also the feedback the machine gives to the user through transaction log. 

## 🚀 Getting Started
Follow these steps to set up and run the project on your local machine:

1. **Clone the Repository**: 
   ```shell
   git clone https://github.com/YourUsername/ATMSystemRepo.git
2. **Compile the Java Code**:

   ```shell
   javac *.java
3. **Run the Application**:

   ```shell
   java ATMApp

# 📦 Features
1. User Registration and Login.
2. Account Creation
3. Debit and Credit Transactions
4. Money Transfer Between Accounts
5. Balance Inquiry
6. Change or Reset PIN
7. Robust Security Measures - Lock or Unlock ATM Card
8. Display of Account Inoformation. 

# 🌐 Technologies used
- 💻 Java (Visual Studio Code) 
- JDBC (Java Database Connectivity)
- MySQL (or your preferred database system)

# 🗂 Modules
- **ATMApp** : It acts as Main section for user interation.
- **User** : It handles the registration and login for the ATM system.
- **Account** : It takes care of the opening of new account.
- **Validation And Generation** : It is mostly responsible for varifying and generating OTP and other securtity measure.
- **Account Manager** : This is the core part of the project which includes all the operation defined in this system.

# 💡HOW IT WORKS
The ATM system is structured to facilitate secure banking transactions through a series of well-defined processes, ensuring that users can perform operations efficiently while maintaining security. 

The following describes the system's workflow:
User Interaction and Authentication
1.	Start Screen: When a user approaches the Virtual ATM system, they are greeted with a welcome message and prompted to login or register. For registration, the user is asked account details like name, email, phone number, Aadhar number, pan number, etc. 
2.	PIN Entry: Once the account is created and logged in successfully though email and password This step is crucial for authenticating the user and ensuring that only registered user can get into the system. 
Main Menu Operations
After successful authentication, users are presented with a menu of banking options, which typically includes:
•	Withdraw Money
•	Deposit Money
•	Check Account Balance
•	Fund Transfer
•	Show Account Information
•	Change PIN
•	Forgot PIN
•	Lock ATM Card
•	Unlock ATM card
•	Exit

# 📝 Description
The user can navigate the menu by entering the corresponding number for their chosen operation.
Transaction Processes
1. **Withdraw Money**: The user selects the "Withdraw Money" option and is prompted to enter the amount they wish to withdraw and the security pin. The system verifies if the requested amount is less than or equal to the current account balance. If sufficient funds are available, the transaction proceeds; otherwise, the user is informed of insufficient funds. Upon successful verification, the system deducts the specified amount from the account balance, records the transaction, and dispenses the cash.

2. **Deposit Money**: The user selects the "Deposit Money" option and enters the amount they wish to deposit and security PIN for safety. The system adds the deposited amount to the current account balance and records the transaction for audit purposes.

3. **Check Account Balance**:	When the user selects the "Check Account Balance" option, the system asks security PIN and retrieves and displays the current balance of the user's account, allowing them to verify their available funds.

4. **Change PIN**: The user selects the "Change PIN" option and must first enter their current PIN for verification. After verification, the user is prompted to enter a new PIN. The system checks for valid criteria (e.g., length, complexity) to ensure the new PIN is secure. Upon successful entry and verification, the system updates the stored PIN in the database, enhancing the account's security.

5. **Fund Transfer**: The user is prompted to enter receiver account number to transfer the money. With successful PIN verification, the fund gets transferred and reflects on the balance of both the user. 

6. **Forgot PIN**: The user is asked to enter the security questions and OTP sent to registered mobile number and email. Upon successful  verification, the user enters the new PIN. This new PIN is stored in database replacing the older one.

7. **Lock ATM Card**: In case of the card is lost or login credentials are leaked , the user gets options to lock its ATM card number. The user is asked the security questions, PIN, and OTP sent to mobile number and email. Upon successful verification, the account is locked. Otherwise, the user gets verification failed exception message. 

8. **Unlock ATM Card**: In case of the card was lost or login credentials were leaked, and the user had locked its ATM card, then the user has option to unlock its ATM card. The user is asked the security questions, PIN, and OTP sent to mobile number and email. Upon successful verification, the account is unlocked. Otherwise, the user gets verification failed exception message. 

9. **Show Account Information**: The user gets an option to view their account details by entering the PIN. This shows the details like name, account number, Aadhar number, email, mobile number, balance, account status.

10. **Logout and Exit** Once the user has completed their transactions, they can select the "Exit" option to log out of the system. The ATM thanks the user for their visit.
 
# 🛡️ Security Measures
Throughout the entire process, various security measures are in place to protect user data and prevent unauthorized access:
- **Unique Email**: Each user account is opened with unique email.
-	**PIN Lockout**: After a predefined number of unsuccessful login attempts, the account is locked to prevent unauthorized access.
- **Minimum Balance Limits**: Limits can be set on minimum amounts to prevent the account not null. 
- **Secure Storage**: Sensitive data such as PINs should be stored securely using encryption techniques to protect against data breaches.


## 📌Scopes
- **User Authentication**: The system includes a basic user authentication mechanism, allowing users to access their accounts securely.
-	**Transaction Management**: Users can perform operations such as balance inquiry, cash deposit, and withdrawal, reflecting real ATM functionalities.
-	**Transaction Logs**: The system maintains transaction logs for each user session, providing transparency and a record of activities.
-	**Modular Design**: The application is built using object-oriented principles, making it easy to extend or integrate additional features.
-	**Database Integration**: Connecting to a database to manage multiple users, real-time balance updates, and transaction histories for each user.
-	**Verification Handled carefully**: Pins and security Questions are asked whenever user tries to change PIN, lock the ATM card, unlock the ATM card, or forgot PIN.


## 🔒Limitations
-	**Limited Security Features**: While basic authentication is provided, advanced security mechanisms (such as encryption) are not implemented in this version.
-	**Single-User Session**: The system is designed to handle one user at a time, which limits its real-world usability.
-	**Fixed Account Balance**: It operates under a simplified balance management system without real-time integration with banking data.
-	**No Physical Cash Handling**: The virtual system does not simulate physical cash handling, which is a key part of real-world ATM operations.


# 👤 Author
- Priya Verma
