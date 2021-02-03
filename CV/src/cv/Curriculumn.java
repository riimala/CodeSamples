package cv;
import java.sql.*;
import java.util.Vector;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;



public class Curriculumn {

	private JFrame frmRecordFetcher;
	private String protocol = "jdbc:";
	private String server = "mysql://localhost:3306/";
	private String db = "test";
	private String un = "test";
	private String ps = "test";
	private JTable table;
	private JTextField tBID;

	private DefaultTableModel model;
	private JLabel lblNroRecords;
	private JLabel lblNewLabel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Curriculumn window = new Curriculumn();
					window.frmRecordFetcher.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 */
	public Curriculumn() throws SQLException {
		initialize();
		Connection();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 */

	private void Connection() throws SQLException {
		System.out.println("Connection method");

		//1.Get connection "jdbc:mysql://localhost:3306/test"
		Connection myConn = DriverManager.getConnection(protocol + server + db, un, ps);
		if (myConn.isClosed()) {
			JOptionPane.showMessageDialog(null, "No connection","Error",
					JOptionPane.ERROR_MESSAGE);			
		}
		
		//Without this row would be replaced and new one wouldn't be added
		if (model == null) {
			model = new DefaultTableModel();
		}

		try {
			//sql osio jossa haetaan recordi id kentän perusteella
			int idValue = Integer.valueOf(tBID.getText());
			System.out.println("ID is " + idValue);
			String sql = "select * from taulu where id = " + idValue;
			System.out.println("SQL is " + sql);
			PreparedStatement ps = myConn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();									
			FillTable(table, rs, myConn, model);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());			
		}
		
		/*
		table = new JTable();
		table.setModel(model);		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setFillsViewportHeight(true);

		*/				
	}

	public void FillTable(JTable table, ResultSet rs, Connection myConn, DefaultTableModel model)
	{
		System.out.println("FillTable");
	    try
	    {
	    	int count = 0;
			String[] columnNames = {"ID", "Nimi"};
			model.setColumnIdentifiers(columnNames);
						
	        /*To remove previously added rows
	        while(table.getRowCount() > 0) 
	        {
	            model.removeRow(0);
	        }
	        */
	        			
	        int columns = rs.getMetaData().getColumnCount();
	        
			int i =0;

			if(rs.next())
			{
				
				int id = rs.getInt("ID");
				String nimi = rs.getString("Nimi");
				System.out.println("if rs.next() --> ID is " + id + ", nimi is " + nimi);
				
				Object[] row = new Object[columns];
	            for (i = 1; i <= columns; i++)
	            {  
	                row[i - 1] = rs.getObject(i);
	            }
	            model.addRow(row); //Add row to end 
				//model.insertRow(rs.getRow()-1, row); //Add row to start						
			}
			
			if ( i < 1) {
				JOptionPane.showMessageDialog(null, "No Record Found","Error",
						JOptionPane.ERROR_MESSAGE);
			}
			table.setModel(model);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			table.setFillsViewportHeight(true);
	        rs.close();
	        myConn.close();
	    }
	    catch(SQLException e)
	    {
	    	e.getMessage();
	    }		


	}
	
		
	private void initialize() {
		frmRecordFetcher = new JFrame();
		frmRecordFetcher.setTitle("Record Fetcher");
		frmRecordFetcher.setBounds(100, 100, 818, 566);
		frmRecordFetcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRecordFetcher.getContentPane().setLayout(null);

		JLabel lblRecord = new JLabel("Record");
		lblRecord.setBounds(67, 61, 46, 14);
		frmRecordFetcher.getContentPane().add(lblRecord);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(228, 82, 306, 232);
		frmRecordFetcher.getContentPane().add(scrollPane_2);

		//model = new DefaultTableModel();
		
		table = new JTable();
		scrollPane_2.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Nimi"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		
		JButton btnLoad = new JButton("Search");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnLoad.setBounds(67, 141, 89, 23);
		frmRecordFetcher.getContentPane().add(btnLoad);

		tBID = new JTextField();
		tBID.setBounds(67, 86, 86, 20);
		frmRecordFetcher.getContentPane().add(tBID);
		tBID.setColumns(10);
		
		lblNewLabel = new JLabel("Fetch record based on id (currently two records..)");
		lblNewLabel.setBounds(230, 337, 306, 14);
		frmRecordFetcher.getContentPane().add(lblNewLabel);
		}
}
