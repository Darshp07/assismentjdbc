package swingj;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class assisment extends JFrame {
	JPanel pane = new JPanel();

	JLabel ID, FIRSTNAME, LASTNAME, EMAIL, MOBILE;
	JTextField FID, FFIRSTNAME, FLASTNAME, FEMAIL, FMOBILE;
	JButton INSERT, SEARCH, UPDATE, DELETE;

	String url = "jdbc:mysql://localhost:3306/assisment?useSSL=false&serverTimezone=UTC";
	String id = "root";
	String pass = "3306";

//	private String F_name, L_name, email, mobile;

	public assisment() {
		setSize(350, 350);
		setLocationRelativeTo(null);
		setTitle("My swing example");

		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		JPanel pan = new JPanel(new GridLayout(5, 2, 10, 10));
		ID = new JLabel("ID");
		FIRSTNAME = new JLabel("FIRSTNAME");
		LASTNAME = new JLabel("LASTNAME");
		EMAIL = new JLabel("EMAIL");
		MOBILE = new JLabel("MOBILE");

		FID = new JTextField();
		FFIRSTNAME = new JTextField();
		FLASTNAME = new JTextField();
		FEMAIL = new JTextField();
		FMOBILE = new JTextField();

		pan.add(ID);
		pan.add(FID);
		pan.add(FIRSTNAME);
		pan.add(FFIRSTNAME);
		pan.add(LASTNAME);
		pan.add(FLASTNAME);
		pan.add(EMAIL);
		pan.add(FEMAIL);
		pan.add(MOBILE);
		pan.add(FMOBILE);

		JPanel buttongrid = new JPanel(new GridLayout(2, 4, 10, 10));
		buttongrid.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

		INSERT = new JButton("INSERT");
		SEARCH = new JButton("SEARCH");
		UPDATE = new JButton("UPDATE");
		DELETE = new JButton("DELETE");

		buttongrid.add(INSERT);
		buttongrid.add(SEARCH);
		buttongrid.add(UPDATE);
		buttongrid.add(DELETE);

		pane.add(pan);
		add(pane);
		pane.add(buttongrid);

		setVisible(true);

		INSERT.addActionListener(e -> insert());
		SEARCH.addActionListener(e -> search());
		UPDATE.addActionListener(e -> update());
		DELETE.addActionListener(e -> delete());

	}

	public void insert() {

		String F_name = FFIRSTNAME.getText();
		String L_name = FLASTNAME.getText();
		String Email = FEMAIL.getText();
		String mobileno = FMOBILE.getText();

		if (!F_name.matches("[a-zA-Z]+") || !L_name.matches("[a-zA-Z]+")) {
			JOptionPane.showMessageDialog(this, "input only alphabet in First and last name ", "Error",
					JOptionPane.ERROR_MESSAGE, null);
			return;
		}

		if (!mobileno.matches("\\d{10}")) {
			JOptionPane.showMessageDialog(this, "Contact number must have exactly 10 digits.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!Email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+")) {
			JOptionPane.showMessageDialog(this, "your Email is not valid", "Error", JOptionPane.ERROR_MESSAGE);

			return;
		}

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection con = DriverManager.getConnection(url, id, pass);
			String query = "INSERT INTO assi (F_name,L_name,Email,mobileno ) VALUES (?, ?,?,?)";
			PreparedStatement pstmt = con.prepareStatement(query);

			pstmt.setString(1, F_name);
			pstmt.setString(2, L_name);
			pstmt.setString(3, Email);
			pstmt.setString(4, mobileno);
			pstmt.executeUpdate();
			con.close();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		FID.setText("");
		FFIRSTNAME.setText("");
		FLASTNAME.setText("");
		FEMAIL.setText("");
		FMOBILE.setText("");
	}

	public void search() {
		String findid = JOptionPane.showInputDialog(this, "enter search :", "search", JOptionPane.QUESTION_MESSAGE);

		try {
			Connection con = DriverManager.getConnection(url, id, pass);

			String query = "SELECT * FROM assi WHERE id=?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, findid);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String id = rs.getString("ID");
				String firstName = rs.getString("F_name");
				String lastName = rs.getString("L_name");
				String email = rs.getString("Email");
				String mobile = rs.getString("mobileno");

				FID.setText(id);
				FFIRSTNAME.setText(firstName);
				FLASTNAME.setText(lastName);
				FEMAIL.setText(email);
				FMOBILE.setText(mobile);

			} else {
				JOptionPane.showMessageDialog(this, "this data is not found", "Error", JOptionPane.ERROR_MESSAGE);
			}

			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		try {
			Connection con = DriverManager.getConnection(url, id, pass);

			String F_name = FFIRSTNAME.getText();
			String L_name = FLASTNAME.getText();
			String Email = FEMAIL.getText();
			String mobileno = FMOBILE.getText();
			String id = FID.getText();

			String query = "UPDATE assi SET F_name=?, L_name=?, Email=?, mobileno=? WHERE ID=?";
			PreparedStatement stpmt = con.prepareStatement(query);
			stpmt.setString(1, F_name);
			stpmt.setString(2, L_name);
			stpmt.setString(3, Email);
			stpmt.setString(4, mobileno);
			stpmt.setString(5, id);

			int rowsUpdated = stpmt.executeUpdate();

			if (rowsUpdated > 0) {
				JOptionPane.showMessageDialog(this, "Data updated successfully.", "Update",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "No data found for update.", "Update",
						JOptionPane.INFORMATION_MESSAGE);
			}

			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		FID.setText("");
		FFIRSTNAME.setText("");
		FLASTNAME.setText("");
		FEMAIL.setText("");
		FMOBILE.setText("");
	}

	public void delete() {
		String idToDelete = JOptionPane.showInputDialog(this, "Enter search criteria to delete:", "Delete",
				JOptionPane.QUESTION_MESSAGE);

		try {
			Connection con = DriverManager.getConnection(url, id, pass);
			String query = "DELETE FROM assi WHERE id=?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, idToDelete);

			int rowsDeleted = pstmt.executeUpdate();
			if (rowsDeleted > 0) {
				JOptionPane.showMessageDialog(this, "Record deleted successfully.", "Delete",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "No matching record found for deletion.", "Delete",
						JOptionPane.INFORMATION_MESSAGE);
			}

			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new assisment();

	}

}
