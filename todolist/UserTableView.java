import java.sql.*;

public class UserTableView {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String username = "myuser";
        String password = "mypassword";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT * FROM users";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String userName = resultSet.getString("name");
                    String userEmail = resultSet.getString("email");

                    System.out.println("User ID: " + userId);
                    System.out.println("Name: " + userName);
                    System.out.println("Email: " + userEmail);
                    System.out.println("--------------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
