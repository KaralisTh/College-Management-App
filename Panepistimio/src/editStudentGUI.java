import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class editStudentGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String URL = "jdbc:mysql://localhost/school_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private Connection connection;
    private PreparedStatement preparedStatement;
    private JComboBox<String> studentComboBox;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField addressField;
    private JButton saveChangesButton;

    public editStudentGUI() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        studentComboBox = new JComboBox<>();
        studentComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                populateFields();
            }
        });

        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        addressField = new JTextField(20);
        saveChangesButton = new JButton("Save Changes");
        saveChangesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveChanges();
            }
        });
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("Επιλογη Σπουδαστη:"));
        panel.add(studentComboBox);
        panel.add(new JLabel("Ονομα:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Επιθετο:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Διευθυνση Κατοικιας:"));
        panel.add(addressField);
        panel.add(saveChangesButton);

        setTitle("Edit Student Records");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);

        populateStudentComboBox();
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

    private void populateFields() {
        String selectedStudent = (String) studentComboBox.getSelectedItem();
        int studentId = Integer.parseInt(selectedStudent.split("-")[0].trim());
        try {
            String query = "SELECT first_name, last_name, address FROM Students WHERE student_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                firstNameField.setText(resultSet.getString("first_name"));
                lastNameField.setText(resultSet.getString("last_name"));
                addressField.setText(resultSet.getString("address"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveChanges() {
        String selectedStudent = (String) studentComboBox.getSelectedItem();
        int studentId = Integer.parseInt(selectedStudent.split("-")[0].trim());
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        
        try {
            String query = "UPDATE Students SET first_name = ?, last_name = ?, address = ? WHERE student_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, address);
            preparedStatement.setInt(4, studentId);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Οι αλλαγες πραγματοποιηθηκαν επιτυχως.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Προβλημα στις αλλαγες που πραγματοποιειται ,  παρακαλω ελεγξτε για τυχον αστοχιες.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new editStudentGUI().setVisible(true);
            }
        });
    }
}
