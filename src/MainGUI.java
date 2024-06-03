import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6L;
	private JTabbedPane tabbedPane;

    public MainGUI() {
        setTitle("Συστημα Διαχειρισης Πανεπιστημιου");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 530));
        
        tabbedPane = new JTabbedPane();
        createAddPanel();
        createEditPanel();
        createDeletePanel();
        createAssignCoursePanel();
        createSetStudentGradePanel();
        createStudentsCardPanel();
        
        add(tabbedPane);
        pack();
        setLocationRelativeTo(null);
    }

    private void createAddPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        registerStudent registerStudentFrame = new registerStudent();
        panel.add(registerStudentFrame.getContentPane(), BorderLayout.CENTER);
        tabbedPane.addTab("Εγγραφη Σπουδαστη", panel);
    }

    private void createEditPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        editStudentGUI editStudentFrame = new editStudentGUI();
        panel.add(editStudentFrame.getContentPane(), BorderLayout.CENTER);
        tabbedPane.addTab("Επεξεργασια Σπουδαστη", panel);
    }

    private void createDeletePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        deleteStudentGUI deleteStudentFrame = new deleteStudentGUI();
        panel.add(deleteStudentFrame.getContentPane(), BorderLayout.CENTER);
        tabbedPane.addTab("Διαγραφη Σπουδαστη", panel);
    }

    private void createAssignCoursePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        assignCourseGUI assignCourseFrame = new assignCourseGUI();
        panel.add(assignCourseFrame.getContentPane(), BorderLayout.CENTER);
        tabbedPane.addTab("Αναθεση Μαθηματος", panel);
    }

    private void createSetStudentGradePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        setStudentGradeGUI setStudentGradeFrame = new setStudentGradeGUI();
        panel.add(setStudentGradeFrame.getContentPane(), BorderLayout.CENTER);
        tabbedPane.addTab("Αναθεση Βαθμων", panel);
    }

    private void createStudentsCardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        studentsCard studentsCardFrame = new studentsCard();
        panel.add(studentsCardFrame.getContentPane(), BorderLayout.CENTER);
        tabbedPane.addTab("Καρτελα Σπουδαστη", panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainGUI().setVisible(true);
            }
        });
    }
}
