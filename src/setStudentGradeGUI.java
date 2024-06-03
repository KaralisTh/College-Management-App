import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class setStudentGradeGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String URL = "jdbc:mysql://localhost/school_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private Connection connection;
    private PreparedStatement preparedStatement;

    private JComboBox<String> studentComboBox;
    private JTextArea assignedSubjectsArea;
    private JTextField subjectIdField, gradeField;
    private JButton searchButton, editButton;

    public setStudentGradeGUI() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JLabel studentLabel = new JLabel("Επιλογη Σπουδαστη:");
        studentComboBox = new JComboBox<>();
        populateStudentComboBox();
        searchButton = new JButton("Αναζητηση");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedStudent = (String) studentComboBox.getSelectedItem();
                try {
                    showAssignedSubjects(selectedStudent);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        JLabel gradeLabel = new JLabel("Βαθμος:");
        gradeField = new JTextField(5);

        JPanel panel = new JPanel(new GridLayout(5, 5, 5, 5));
        panel.add(studentLabel);
        panel.add(studentComboBox);
        panel.add(searchButton);
        JLabel label = new JLabel("Μαθηματα Σπουδαστη:");
        panel.add(label);
        
        assignedSubjectsArea = new JTextArea(5, 20);
        assignedSubjectsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(assignedSubjectsArea); 
        panel.add(scrollPane);
        
        editButton = new JButton("Καταχωρηση");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int studentId = Integer.parseInt(((String) studentComboBox.getSelectedItem()).split("-")[0].trim());
                int subjectId = Integer.parseInt(subjectIdField.getText());
                double grade = Double.parseDouble(gradeField.getText());
                try {
                    editRecord(studentId, subjectId, grade);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel.add(editButton);
        
        JLabel subjectIdLabel = new JLabel("ID Μαθηματος:");
        panel.add(subjectIdLabel);
        subjectIdField = new JTextField(5);
        panel.add(subjectIdField);
        panel.add(new JLabel()); 
        panel.add(gradeLabel);
        panel.add(gradeField);

        setTitle("Edit Record");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().add(panel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    private void populateStudentComboBox() {
        try {
            String query = "SELECT student_id, first_name, last_name FROM Students";
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int studentId = resultSet.getInt("student_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                studentComboBox.addItem(studentId + " - " + firstName + " " + lastName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAssignedSubjects(String selectedStudent) throws SQLException {
        int studentId = Integer.parseInt(selectedStudent.split("-")[0].trim());
        String query = "SELECT Subjects.subject_id, Subjects.title FROM Grades " +
                       "INNER JOIN Subjects ON Grades.subject_id = Subjects.subject_id " +
                       "WHERE student_id = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, studentId);
        ResultSet resultSet = preparedStatement.executeQuery();

        StringBuilder assignedSubjects = new StringBuilder();
        while (resultSet.next()) {
            int subjectId = resultSet.getInt("subject_id");
            String subjectTitle = resultSet.getString("title");
            assignedSubjects.append("ID: ").append(subjectId).append(", Title: ").append(subjectTitle).append("\n");
        }
        assignedSubjectsArea.setText(assignedSubjects.toString());
    }

    private void editRecord(int studentId, int subjectId, double newGrade) throws SQLException {
        String query = "UPDATE Grades SET grade = ? WHERE student_id = ? AND subject_id = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setDouble(1, newGrade);
        preparedStatement.setInt(2, studentId);
        preparedStatement.setInt(3, subjectId);
        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows > 0) {
            System.out.println("Record updated successfully");
        } else {
            System.out.println("No record found for the given student and subject");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new setStudentGradeGUI().setVisible(true);
            }
        });
    }
}
