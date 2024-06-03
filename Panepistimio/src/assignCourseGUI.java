import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class assignCourseGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String URL = "jdbc:mysql://localhost/school_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection connection;
    private PreparedStatement preparedStatement;
    private JComboBox<String> studentComboBox;
    private JComboBox<String> subjectComboBox;
    private JButton assignButton;

    public assignCourseGUI() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JLabel studentLabel = new JLabel("Επιλογη Σπουδαστη:");
        studentComboBox = new JComboBox<>();
        JLabel subjectLabel = new JLabel("Επιλογη Μαθηματος:");
        subjectComboBox = new JComboBox<>();
        assignButton = new JButton("Αναθεση Μαθηματος");

        populateStudentComboBox();

        populateSubjectComboBox();

        assignButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) studentComboBox.getSelectedItem();
                String subjectName = (String) subjectComboBox.getSelectedItem();
                try {
                    int studentId = getStudentId(studentName);
                    int subjectId = getSubjectId(subjectName);
                    assignSubject(studentId, subjectId);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(studentLabel);
        panel.add(studentComboBox);
        panel.add(subjectLabel);
        panel.add(subjectComboBox);
        panel.add(assignButton);

        setTitle("Assign Subject");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(panel);
        pack();
        setLocationRelativeTo(null);
    }

    private void populateStudentComboBox() {
        try {
            String query = "SELECT student_id, first_name, last_name FROM Students";
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

    private void populateSubjectComboBox() {
        try {
            String query = "SELECT subject_id, title FROM Subjects";
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String subjectName = resultSet.getString("title");
                subjectComboBox.addItem(subjectName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private int getStudentId(String studentName) throws SQLException {
        if (Character.isDigit(studentName.charAt(0))) {
            return Integer.parseInt(studentName);
        } else {
            String[] names = studentName.split("\\s+");
            String query = "SELECT student_id FROM Students WHERE first_name = ? AND last_name = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, names[0]);
            preparedStatement.setString(2, names[1]);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("student_id");
            } else {
                throw new SQLException("Το Id του φοιτητη δεν ειναι καταχωρημενο στη βαση δεδομενων .");
            }
        }
    }

    private int getSubjectId(String subjectName) throws SQLException {
        if (Character.isDigit(subjectName.charAt(0))) {
            return Integer.parseInt(subjectName);
        } else {
            String query = "SELECT subject_id FROM Subjects WHERE title = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, subjectName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("subject_id");
            } else {
                throw new SQLException("Το Id του μαθηματος δεν ειναι καταχωρημενο στη βαση δεδομενων .");
            
        }}}

    private void assignSubject(int studentId, int subjectId) throws SQLException {
    	String query = "INSERT INTO Grades (student_id, subject_id) VALUES (?, ?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, studentId);
        preparedStatement.setInt(2, subjectId);
        preparedStatement.executeUpdate();
        System.out.println("Το μαθημα με ID " + subjectId + " ανατεθηκε στον σπουδαστη με ID " + studentId + " επιτυχως");
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new assignCourseGUI().setVisible(true);
            }
        });
    }
}
