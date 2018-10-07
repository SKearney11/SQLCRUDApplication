/**
 * SQLApp.java
 * 
 * Created by: 
 * Sean Kearney 20072151
 * 
 * Description:
 * App with GUI to connect to an SQL database and display, add, delete, and update the database.
 * 
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class SQLApp implements ActionListener{
	
	private JButton btnPrevious, btnNext, btnDelete, btnClear, btnAdd, btnUpdate;	
	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private final String userName = "root";
	private final String password = "";
	private final String serverName = "localhost";
	private final int portNumber = 3307;
	private final String dbName = "Assignment1";
	private final String tableName = "Employee";
	
	Statement s = null;
	ResultSet rs = null;
	/**
	 * Get a new database connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);
		//the "?useSSL=False needs to be here to avoid an error/warning with MYSQL on my computer"
		conn = DriverManager.getConnection("jdbc:mysql://"
				+ this.serverName + ":" + this.portNumber + "/" + this.dbName+"?useSSL=false",
				connectionProps);

		return conn;
	}

	/**
	 * Run a SQL command which does not return a recordset:
	 * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
	 * 
	 * @throws SQLException If something goes wrong
	 */
	
	public boolean executeUpdate(Connection conn, String command) throws SQLException {
	    Statement stmt = null;
	    try {
	        stmt = conn.createStatement();
	        stmt.executeUpdate(command); // This will throw a SQLException if it fails
	        return true;
	    } finally {
	    	// This will run whether we throw an exception or not
	        if (stmt != null) { stmt.close(); }
	    }
	}
	
	/**
	 * Connect to MySQL and do some stuff.
	 * @throws SQLException 
	 */
	public void run() throws SQLException {

		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
			s = conn.createStatement ();
			s.executeQuery ("SELECT * " +	"FROM Employee");
			rs = s.getResultSet ();		
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}	
	}
	
	/**
	 * Connect to the DB and do some stuff
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		SQLApp app = new SQLApp();
		app.run();
		app.startGui();
		app.frame.setVisible(true);
	}	
	
	/**
	 * Load the next row from the result set if it exists
	 * @param rs
	 */
	private void loadNext(ResultSet rs){
		try {
			if (!rs.next()) {
				//return to current row. this saves the user having to press previous button twice.
				rs.previous();
				return;
			}
			textField.setText(rs.getString(1));
			textField_1.setText(rs.getString(2));
			textField_2.setText(rs.getString(3));
			textField_3.setText(rs.getString(4));
			textField_4.setText(rs.getString(5));
			textField_5.setText(rs.getString(6));
		}catch(SQLException e){
			System.out.println("Cant load next values");
		}
	}
	
	/**
	 * Load the previous row from the result set if it exists
	 * @param rs
	 */
	private void loadPrevious(ResultSet rs){
		try {
			if (!rs.previous()) {
				rs.next();
				return;
			}
			textField.setText(rs.getString(1));
			textField_1.setText(rs.getString(2));
			textField_2.setText(rs.getString(3));
			textField_3.setText(rs.getString(4));
			textField_4.setText(rs.getString(5));
			textField_5.setText(rs.getString(6));
		}catch(SQLException e){
			System.out.println("Cant load previous values");
		}
	}
	
	/**
	 * Build the GUI
	 * @throws SQLException
	 */
	public void startGui() throws SQLException {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblSsn = new JLabel("SSN:");
		panel.add(lblSsn);
		
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(10);
		
		JLabel lblDob = new JLabel("DOB");
		panel.add(lblDob);
		
		textField_1 = new JTextField();
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblName = new JLabel("Name");
		panel.add(lblName);
		
		textField_2 = new JTextField();
		panel.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblAddress = new JLabel("Address");
		panel.add(lblAddress);
		
		textField_3 = new JTextField();
		panel.add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblSalary = new JLabel("Salary");
		panel.add(lblSalary);
		
		textField_4 = new JTextField();
		panel.add(textField_4);
		textField_4.setColumns(10);
		
		JLabel lblGender = new JLabel("Gender");
		panel.add(lblGender);
		
		textField_5 = new JTextField();
		panel.add(textField_5);
		textField_5.setColumns(10);
		
		btnAdd = new JButton("Add");
		panel.add(btnAdd);
		
		btnDelete = new JButton("Delete");
		panel.add(btnDelete);
		
		btnUpdate = new JButton("Update");
		panel.add(btnUpdate);
		
		btnClear = new JButton("Clear");
		panel.add(btnClear);
		
		btnPrevious = new JButton("Previous");
		panel.add(btnPrevious);
		
		btnNext = new JButton("Next");
		panel.add(btnNext);
		
		loadNext(rs);
		
		btnClear.addActionListener(this);
		btnNext.addActionListener(this);
		btnAdd.addActionListener(this);
		btnPrevious.addActionListener(this);
		btnDelete.addActionListener(this);
		btnUpdate.addActionListener(this);
	}
	
	/**
	 * Respond to button presses
	 */
	public void actionPerformed(ActionEvent e) {
    	//Set all buttons to an empty string
		if (e.getSource() == btnClear) {
    		textField.setText("");
    		textField_1.setText("");
    		textField_2.setText("");
    		textField_3.setText("");
    		textField_4.setText("");
    		textField_5.setText("");
    	}
		
		//Call the loadNext function
    	if (e.getSource() == btnNext) {
    		loadNext(rs);
    	}
    	//Call the loadPrevious function
    	if (e.getSource() == btnPrevious) {
    		loadPrevious(rs);
    	}
    	//Send the appropriate SQL query to the executeUpdate function
    	if (e.getSource() == btnAdd) {
    		String command = "INSERT INTO Employee (Ssn, DOB, Name, Address, Salary, Sex)\n" + 
    				"VALUES ('"+textField.getText()+"', '"+textField_1.getText()+"','"+textField_2.getText()+"','"+textField_3.getText()+"','"+textField_4.getText()+"','"+textField_5.getText()+"');";
    	
    		try {
				executeUpdate(getConnection(), command);
				//Call run to refresh the result set
				run();
			} catch (SQLException e1) {
				System.out.println("Error Adding Entry");
				e1.printStackTrace();
			}
    	}
    	//Send the appropriate SQL query to the executeUpdate function
    	if (e.getSource() == btnDelete) {
    		String command = "DELETE FROM Employee \n WHERE Ssn ="+textField.getText()+";";
    		try {
				executeUpdate(getConnection(), command);
				run();
			} catch (SQLException e1) {
				System.out.println("Error Deleting Entry");
				e1.printStackTrace();
			}
    	}
    	//Send the appropriate SQL query to the executeUpdate function
    	if (e.getSource() == btnUpdate) {
    		//String command = "DELETE FROM Employee \n WHERE Ssn ="+textField.getText()+";";
    		String command = "UPDATE Employee\nSET DOB = '"+textField_1.getText()+"', Name = '"+textField_2.getText()+"', Address = '"+textField_3.getText()+"', Salary = '"+textField_4.getText()+"', Sex = '"+textField_5.getText()+"'\nWHERE Ssn = '"+ textField.getText()+"';";
    		try {
				executeUpdate(getConnection(), command);
				run();
			} catch (SQLException e1) {
				System.out.println("Error Updating Entry");
				e1.printStackTrace();
			}
    	}
	}
}