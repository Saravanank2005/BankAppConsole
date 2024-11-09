package Banking;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private Connection connection;

    // Constructor to initialize the database connection
    public DataBase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "saran@2005");
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    // Method to insert a new account into the database
    public void insertAccount(Account account) {
        String sql = "INSERT INTO accounts (customer_id, name, phone_number, address, account_type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setLong(1, account.getCustomerId());
            pstm.setString(2, account.getName());
            pstm.setString(3, account.getPhoneNumber());
            pstm.setString(4, account.getAddress());
            pstm.setString(5, account.getAccountType());
            pstm.executeUpdate();
            System.out.println("Account inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error inserting account: " + e.getMessage());
        }
    }

    // Method to update the account balance in the database
    public void updateAccountBalance(Account account) {
        String query = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, account.getBalance());
            statement.setInt(2, account.getAccountId());
            statement.executeUpdate();
            System.out.println("Account balance updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating balance: " + e.getMessage());
        }
    }

    // Method to retrieve all accounts from the database
    public ArrayList<Account> getAllAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts";
        
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int accountId = resultSet.getInt("account_id");
                long customerId = resultSet.getLong("customer_id");
                String accountType = resultSet.getString("account_type");
                double balance = resultSet.getDouble("balance");
                String name = resultSet.getString("name");
                String phoneNumber = resultSet.getString("phone_number");
                String address = resultSet.getString("address");

                Account account = new Account(customerId, accountType, name, phoneNumber, address);
                account.setAccountId(accountId);
                account.deposit(balance); // To initialize balance in account object
                accounts.add(account);
            }
            System.out.println("Accounts retrieved successfully.");
        } catch (SQLException e) {
            System.out.println("Error retrieving accounts: " + e.getMessage());
        }
        return accounts;
    }

    // Method to update account details such as name, phone number, and address
    public void updateAccountDetails(Account account) {
        String query = "UPDATE accounts SET name = ?, phone_number = ?, address = ? WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, account.getName());
            preparedStatement.setString(2, account.getPhoneNumber());
            preparedStatement.setString(3, account.getAddress());
            preparedStatement.setInt(4, account.getAccountId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Account details updated successfully.");
            } else {
                System.out.println("Account not found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating account details: " + e.getMessage());
        }
    }

    // Method to close the database connection
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
