import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class registerStudent extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String URL = "jdbc:mysql://localhost/school_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";


    private Connection connection;
    private PreparedStatement preparedStatement;

    private JTextField nameField, lastNameField, dobField, addressField;
    private JComboBox<String> genderComboBox;
    private JButton addButton;

    public registerStudent() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JLabel nameLabel = new JLabel("Ονομα:");
        nameField = new JTextField(20);
        JLabel lastNameLabel = new JLabel("Επιθετο:");
        lastNameField = new JTextField(20);
        JLabel dobLabel = new JLabel("Ημερομηνια Γεννησης: *ΧΡ-MΗΝ-ΜΕΡ* ");
        dobField = new JTextField(20);
        JLabel genderLabel = new JLabel("Φυλο:");
        genderComboBox = new JComboBox<>();
        genderComboBox.addItem("Αρρεν");
        genderComboBox.addItem("Θυλη");
        JLabel addressLabel = new JLabel("Διευθυνση Κατοικιας:");
        addressField = new JTextField(20);
        addButton = new JButton("Εγγραφη");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    addStudent(nameField.getText(), lastNameField.getText(), dobField.getText(), (String) genderComboBox.getSelectedItem(), addressField.getText());
                    nameField.setText("");
                    lastNameField.setText("");
                    dobField.setText("");
                    genderComboBox.setSelectedIndex(0);
                    addressField.setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(dobLabel);
        panel.add(dobField);
        panel.add(genderLabel);
        panel.add(genderComboBox);
        panel.add(addressLabel);
        panel.add(addressField);
        panel.add(addButton);

        setTitle("Add Student");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(panel);
        pack();
        setLocationRelativeTo(null);
    }

    public void addStudent(String firstName, String lastName, String dob, String gender, String address) throws SQLException {
        String query = "INSERT INTO Students (first_name, last_name, date_of_birth, gender, address) VALUES (?, ?, ?, ?, ?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, lastName);
        preparedStatement.setString(3, dob);
        preparedStatement.setString(4, gender);
        preparedStatement.setString(5, address);
        preparedStatement.executeUpdate();
        System.out.println("Εγγραφηκατε επιτυχως");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new registerStudent().setVisible(true);
            }
        });
    }
}
