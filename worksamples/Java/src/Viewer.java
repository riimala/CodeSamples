
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.BevelBorder;

public class Viewer extends ConnectionDB{

	private JFrame frame;
	private JTable table;
	private JButton closeButton;
	
	private Connection cRet;
	private static String t;
	
	DefaultTableModel model;
	private ResultSet rs;
	
	/**
	 * @throws SQLException 
	 * @wbp.parser.entryPoint
	 */
	public Viewer(String t, Connection conn2) throws SQLException {		
	
		int id;
		String name;
		Date dateCreated;	
		
		JTable table = new JTable();
		JFrame frame = new JFrame();
		closeButton = new JButton();
		
		closeButton.setText("Close");
		closeButton.setVisible(true);
		closeButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
	            frame.dispose();  
	        }  
	    });  
		
		frame.setLayout(new FlowLayout());		
		frame.setBounds(400, 200, 800, 600);
		
		String[] columnNames = {"Id", "Name", "Date Created"};
		model = new DefaultTableModel();
		model.setColumnIdentifiers(columnNames);
		
		JScrollPane scrollPane = new JScrollPane(table);
		frame.add(scrollPane);
		frame.add(closeButton);
		
		String query = "SELECT ID, Nimi, date_created FROM " + t;
		Statement st = conn2.createStatement();
		rs = st.executeQuery(query);
		        		
		try {
			while(rs.next()) {
				id = rs.getInt("ID");
		        name = rs.getString("Nimi");
		        dateCreated = rs.getDate("date_created");     
				
				Object[] data = 
						{id, name, dateCreated};
				
				model.addRow(data);
			}
					
			table.setModel(model);
			table.setBounds(150,200,750, 500);
			table.setPreferredScrollableViewportSize(new Dimension(500,100));
			table.setFillsViewportHeight(true);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(600, 200);
			frame.setVisible(true);
			frame.setTitle("My Table");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLException {				
		
		ConnectionDB c = new ConnectionDB();
		Connection conn2 = c.Initialize();	
		String t = c.GetTable();
		Ui userInterface = new Ui();
		userInterface.start(t, conn2);
	}
}
