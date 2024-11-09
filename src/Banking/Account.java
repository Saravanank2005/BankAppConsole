package Banking;

import java.util.ArrayList;

public class Account {
    private static int accountCounter = 1;
    private int accountId;
    private long customerId;
    private String accountType;
    private double balance;
    private String name;
    private String phoneNumber;
    private String address;
    private ArrayList<String> transactionHistory;

    // Constructor to initialize a new account
    public Account(long customerId, String accountType, String name, String phoneNumber, String address) {
        this.accountId = accountCounter++;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = 0.0;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.transactionHistory = new ArrayList<>();
    }

    // Getters for account information
    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public int getAccountId() {
        return accountId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    // Method to deposit funds into the account
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactionHistory.add("Deposit: " + amount);
            System.out.printf("Successfully deposited %.2f. New balance: %.2f%n", amount, balance);
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    // Method to withdraw funds from the account
    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            transactionHistory.add("Withdraw: " + amount);
            System.out.printf("Successfully withdrew %.2f. New balance: %.2f%n", amount, balance);
            return true;
        } else {
            System.out.println("Insufficient funds or invalid amount. Transaction failed.");
            return false;
        }
    }

    // Method to print the transaction history
    public void printTransactionHistory() {
        System.out.println("Transaction History for Account ID: " + accountId);
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (String transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
    }

    // Setters for updating account information
    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setAccountId(int newAccountId) {
        this.accountId = newAccountId;
    }

    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Returns the transaction history as an ArrayList
    public ArrayList<String> getTransactionHistory() {
        return transactionHistory;
    }
}
