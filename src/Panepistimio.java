import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Panepistimio {
		private static final String URL = "jdbc:mysql://localhost/school_db";
	    private static final String USER = "root";
	    private static final String PASSWORD = "";


	    private static Connection connection;
	    private static Statement statement;
	    private static ResultSet resultSet;

	    public static void main(String[] args) {
	        try {
	            connection = DriverManager.getConnection(URL, USER, PASSWORD);
	            System.out.println("Connected to the database");
	//
//	            // Add a new subject
//	            addSubject("Php");
//	            addSubject("Java");
//	            addSubject("Sql");
//	            addSubject("Html");
//	            addSubject("Python");
//	            addSubject("Wordpress");
//	            addSubject("C#");

	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (connection != null) connection.close();
	                if (statement != null) statement.close();
	                if (resultSet != null) resultSet.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    public static void addSubject(String title) throws SQLException {
	        String query = "INSERT INTO Subjects (title) VALUES (?)";
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, title);
	        preparedStatement.executeUpdate();
	        System.out.println("Subject added successfully");
	    }
	}
