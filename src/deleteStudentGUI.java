import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class deleteStudentGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7006419145019263239L;
	private static final String URL = "jdbc:mysql://localhost/school_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection connection;
    private PreparedStatement preparedStatement;

    private JComboBox<String> studentComboBox;
    private JButton deleteButton;

    public deleteStudentGUI() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JLabel studentLabel = new JLabel("Επιλογη Σπουδαστη:");
        studentComboBox = new JComboBox<>();
        deleteButton = new JButton("Διαγραφη Σπουδαστη");

        populateStudentComboBox();

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedStudent = (String) studentComboBox.getSelectedItem();
                int studentId = getStudentId(selectedStudent);
                try {
                    deleteStudent(studentId);
                    JOptionPane.showMessageDialog(null, "Ο σπουδαστης διαγραφηκε επιτυχως. *Αντιστοιχα διαγραφηκε και η καρτελα αυτου.");
                    populateStudentComboBox();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(studentLabel);
        panel.add(studentComboBox);
        panel.add(deleteButton);

        setTitle("Delete Student");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(panel);
        pack();
        setLocationRelativeTo(null);
    }

    private void populateStudentComboBox() {
        studentComboBox.removeAllItems();
        try {
            String query = "SELECT student_id, first_name, last_name FROM Students";
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int studentId = resultSet.getInt("student_id");
                String fullName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                studentComboBox.addItem(fullName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getStudentId(String fullName) {
        try {
            String[] parts = fullName.split(" ");
            String firstName = parts[0];
            String lastName = parts[1];
            String query = "SELECT student_id FROM Students WHERE first_name = ? AND last_name = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("student_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void deleteStudent(int studentId) throws SQLException {
        String deleteGradesQuery = "DELETE FROM Grades WHERE student_id = ?";
        String deleteStudentQuery = "DELETE FROM Students WHERE student_id = ?";
        preparedStatement = connection.prepareStatement(deleteGradesQuery);
        preparedStatement.setInt(1, studentId);
        preparedStatement.executeUpdate();
        preparedStatement = connection.prepareStatement(deleteStudentQuery);
        preparedStatement.setInt(1, studentId);
        preparedStatement.executeUpdate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new deleteStudentGUI().setVisible(true);
            }
        });
    }
}
