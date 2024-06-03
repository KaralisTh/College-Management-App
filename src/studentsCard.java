import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class studentsCard extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String URL = "jdbc:mysql://localhost/school_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection connection;
    private PreparedStatement preparedStatement;
    private JComboBox<String> studentComboBox;
    private JTextArea studentInfoArea;

    public studentsCard() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        studentComboBox = new JComboBox<>();
        populateStudentComboBox(); 

        JButton searchButton = new JButton("Αναζητηση");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedStudent = (String) studentComboBox.getSelectedItem();
                try {
                    searchStudent(selectedStudent);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        studentInfoArea = new JTextArea(10, 40);
        studentInfoArea.setEditable(false);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Επιλογη Σπουδαστη:"));
        panel.add(studentComboBox);
        panel.add(searchButton);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(studentInfoArea), BorderLayout.CENTER);

        setTitle("Student Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void populateStudentComboBox() {
        try {
            String query = "SELECT first_name, last_name FROM Students";
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String fullName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                studentComboBox.addItem(fullName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchStudent(String fullName) throws SQLException {
        String[] nameParts = fullName.split(" ");
        String firstName = nameParts[0];
        String lastName = nameParts[1];
        String query = "SELECT * FROM Students WHERE first_name = ? AND last_name = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, lastName);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int studentId = resultSet.getInt("student_id");
            String dateOfBirth = resultSet.getString("date_of_birth");
            String gender = resultSet.getString("gender");
            String address = resultSet.getString("address");

            StringBuilder studentInfo = new StringBuilder();
            studentInfo.append("Student ID: ").append(studentId).append("\n");
            studentInfo.append("First Name: ").append(firstName).append("\n");
            studentInfo.append("Last Name: ").append(lastName).append("\n");
            studentInfo.append("Date of Birth: ").append(dateOfBirth).append("\n");
            studentInfo.append("Gender: ").append(gender).append("\n");
            studentInfo.append("Address: ").append(address).append("\n");

            studentInfo.append("\nAssigned Subjects:\n");
            studentInfo.append(getAssignedSubjects(studentId)).append("\n");

            studentInfo.append("\nGrades:\n");
            studentInfo.append(getGrades(studentId)).append("\n");

            studentInfoArea.setText(studentInfo.toString());
        } else {
            studentInfoArea.setText("Student not found.");
        }
    }

    private String getAssignedSubjects(int studentId) throws SQLException {
        StringBuilder assignedSubjects = new StringBuilder();
        String query = "SELECT Subjects.title FROM Grades " +
                       "INNER JOIN Subjects ON Grades.subject_id = Subjects.subject_id " +
                       "WHERE student_id = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, studentId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            assignedSubjects.append(resultSet.getString("title")).append("\n");
        }
        return assignedSubjects.toString();
    }

    private String getGrades(int studentId) throws SQLException {
        StringBuilder grades = new StringBuilder();
        String query = "SELECT Subjects.title, Grades.grade FROM Grades " +
                       "INNER JOIN Subjects ON Grades.subject_id = Subjects.subject_id " +
                       "WHERE student_id = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, studentId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String subjectName = resultSet.getString("title");
            double grade = resultSet.getDouble("grade");
            grades.append(subjectName).append(": ").append(grade).append("\n");
        }
        return grades.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new studentsCard().setVisible(true);
            }
        });
    }
}
