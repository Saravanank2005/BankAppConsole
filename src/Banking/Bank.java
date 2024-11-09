package Banking;

import java.util.ArrayList;
import java.util.Scanner;

public class Bank {
    private ArrayList<Account> accounts;
    private Scanner scanner;
    private DataBase database;

    public Bank() {
        accounts = new ArrayList<>();
        scanner = new Scanner(System.in);
        database = new DataBase();
    }

    public void start() {
        while (true) {
            System.out.println("\n..........Welcome to the Banking Application................");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. View Transaction History");
            System.out.println("6. Transfer Funds");
            System.out.println("7. Generate Report");
            System.out.println("8. Update Account Details");
            System.out.println("9. Exit");
            System.out.print("Select an Option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> deposit();
                case 3 -> withdraw();
                case 4 -> viewAccount();
                case 5 -> viewTransactionHistory();
                case 6 -> transferFunds();
                case 7 -> generateReport();
                case 8 -> {
                    System.out.print("Enter Your Account ID: ");
                    int accountId = scanner.nextInt();
                    updateCustomerDetails(accountId);
                }
                case 9 -> {
                    System.out.println("Thank you for using the Banking Application. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid Choice, please select a valid option.");
            }
        }
    }

    private void createAccount() {
        long customerId;
        while (true) { // Keep asking until valid input
            System.out.print("Enter Customer ID (9 to 16 digits): ");
            customerId = scanner.nextLong();

            int customerIdLength = String.valueOf(customerId).length();
            if (customerIdLength >= 9 && customerIdLength <= 16) {
                break; // Valid input, exit loop
            } else {
                System.out.println("Customer ID must have between 9 and 16 digits. Please try again.");
            }
        }

        String phoneNumber;
        while (true) { // Keep asking until valid input
            System.out.print("Enter Phone Number (10 digits): ");
            scanner.nextLine(); // Consume newline
            phoneNumber = scanner.nextLine();
            if (phoneNumber.length() == 10) {
                break; // Valid input, exit loop
            } else {
                System.out.println("Invalid phone number. Please enter a 10-digit number.");
            }
        }

        String accountType;
        while (true) { // Keep asking until valid input
            System.out.print("Enter Account Type (Savings or Current): ");
            char accountTypeInput = scanner.next().charAt(0);
            accountType = switch (Character.toUpperCase(accountTypeInput)) {
                case 'S' -> "Savings";
                case 'C' -> "Current";
                default -> {
                    System.out.println("Invalid Account Type. Please enter 'S' for Savings or 'C' for Current.");
                    yield null; // Return null to re-prompt
                }
            };
            if (accountType != null) {
                break; // Valid input, exit loop
            }
        }

        System.out.print("Enter Customer Name: ");
        scanner.nextLine(); // Consume newline
        String name = scanner.nextLine();

        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        Account account = new Account(customerId, accountType, name, phoneNumber, address);
        database.insertAccount(account);

        System.out.println("Account Created Successfully! Your Account ID: " + account.getAccountId());
    }

    private void deposit() {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        Account account = findAccount(accountId);

        if (account != null) {
            System.out.print("Enter Amount to Deposit: ");
            double amount = scanner.nextDouble();
            account.deposit(amount);
            database.updateAccountBalance(account);
            System.out.println("Deposit Successful! New Balance: " + account.getBalance());
        } else {
            System.out.println("Account Not Found!");
        }
    }

    private void withdraw() {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        Account account = findAccount(accountId);

        if (account != null) {
            System.out.print("Enter Amount to Withdraw: ");
            double amount = scanner.nextDouble();
            if (account.withdraw(amount)) {
                database.updateAccountBalance(account);
                System.out.println("Withdrawal Successful! New Balance: " + account.getBalance());
            } else {
                System.out.println("Insufficient Funds.");
            }
        } else {
            System.out.println("Account Not Found!");
        }
    }

    private void viewAccount() {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        Account account = findAccount(accountId);

        if (account != null) {
            System.out.println("Account Details:");
            System.out.println("Account ID: " + account.getAccountId());
            System.out.println("Customer ID: " + account.getCustomerId());
            System.out.println("Name: " + account.getName());
            System.out.println("Phone Number: " + account.getPhoneNumber());
            System.out.println("Address: " + account.getAddress());
            System.out.println("Balance: " + account.getBalance());
            System.out.println("Account Type: " + account.getAccountType());
        } else {
            System.out.println("Invalid Account ID.");
        }
    }

    private void viewTransactionHistory() {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        Account account = findAccount(accountId);

        if (account != null) {
            account.printTransactionHistory();
        } else {
            System.out.println("Account Not Found!");
        }
    }

    private void transferFunds() {
        System.out.print("Enter Source Account ID: ");
        int sourceAccountId = scanner.nextInt();
        Account sourceAccount = findAccount(sourceAccountId);

        if (sourceAccount == null) {
            System.out.println("Source Account Not Found.");
            return;
        }

        System.out.print("Enter Destination Account ID: ");
        int destinationAccountId = scanner.nextInt();
        Account destinationAccount = findAccount(destinationAccountId);

        if (destinationAccount == null) {
            System.out.println("Destination Account Not Found!");
            return;
        }

        System.out.print("Enter Amount to Transfer: ");
        double amount = scanner.nextDouble();

        if (amount <= 0) {
            System.out.println("Transfer Amount must be Positive.");
            return;
        }

        if (sourceAccount.getBalance() < amount) {
            System.out.println("Insufficient Funds in Source Account. Available Balance: " + sourceAccount.getBalance());
            return;
        }

        sourceAccount.withdraw(amount);
        destinationAccount.deposit(amount);
        database.updateAccountBalance(sourceAccount);
        database.updateAccountBalance(destinationAccount);

        System.out.printf("Successfully Transferred %.2f from Account ID: %d to Account ID: %d.%n", amount, sourceAccountId, destinationAccountId);
    }

    private Account findAccount(int accountId) {
        for (Account account : accounts) {
            if (account.getAccountId() == accountId) {
                return account;
            }
        }
        return null;
    }

    private void generateReport() {
        System.out.println("------- Generating Reports -------");
        while (true) {
            System.out.println("1. Customer Details Report");
            System.out.println("2. Total Balance Report");
            System.out.println("3. Account Type Count Report");
            System.out.println("4. Exit");
            System.out.print("Select an Option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> displayCustomerReport();
                case 2 -> displayTotalBalance();
                case 3 -> displayAccountTypeCount();
                case 4 -> {
                    System.out.println("Exiting Report Generation...");
                    return;
                }
                default -> System.out.println("Invalid Choice. Please Try Again.");
            }
        }
    }

    private void displayCustomerReport() {
        System.out.printf("%-15s %-15s %-20s %-15s %-30s %-10s %-15s%n", "Account ID", "Customer ID", "Name", "Phone Number", "Address", "Balance", "Account Type");
        System.out.println("-------------------------------------------------------------------------------------------------------------");

        for (Account account : accounts) {
            System.out.printf("%-15s %-15s %-20s %-15s %-30s %-10.2f %-15s%n",
                    account.getAccountId(),
                    account.getCustomerId(),
                    account.getName(),
                    account.getPhoneNumber(),
                    account.getAddress(),
                    account.getBalance(),
                    account.getAccountType());
        }
    }

    private void displayTotalBalance() {
        double totalBalance = accounts.stream().mapToDouble(Account::getBalance).sum();
        System.out.println("Total Balance Across All Accounts: " + totalBalance);
    }

    private void displayAccountTypeCount() {
        long savingsCount = accounts.stream().filter(a -> a.getAccountType().equalsIgnoreCase("Savings")).count();
        long currentCount = accounts.stream().filter(a -> a.getAccountType().equalsIgnoreCase("Current")).count();

        System.out.println("Account Type Count Report:");
        System.out.println("Total Savings Accounts: " + savingsCount);
        System.out.println("Total Current Accounts: " + currentCount);
    }

    private void updateCustomerDetails(int accountId) {
        Account account = findAccount(accountId);

        if (account == null) {
            System.out.println("Account Not Found!");
            return;
        }

        System.out.println("Current Details:");
        System.out.printf("Name: %s%n", account.getName());
        System.out.printf("Phone Number: %s%n", account.getPhoneNumber());
        System.out.printf("Address: %s%n", account.getAddress());

        System.out.print("Update Name (Press Enter to Skip): ");
        scanner.nextLine();  // Consume newline
        String name = scanner.nextLine();

        System.out.print("Update Address (Press Enter to Skip): ");
        String address = scanner.nextLine();

        System.out.print("Update Phone Number (Press Enter to Skip): ");
        String phoneNumber = scanner.nextLine();

        if (!name.isEmpty()) {
            account.setName(name);
        }
        if (!address.isEmpty()) {
            account.setAddress(address);
        }
        if (!phoneNumber.isEmpty()) {
            account.setPhoneNumber(phoneNumber);
        }
        System.out.println("Customer Details Updated Successfully.");
    }
}
