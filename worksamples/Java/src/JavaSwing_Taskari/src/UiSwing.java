import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;


import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

public class UiSwing {

	private JFrame frmTaskariAdmin;
	private JTextField txtRowToBeDeleted;
	public String userString; 
	

	
	private static boolean lock()
	 {
	   try
	    {
	        final File file=new File("bpmdj.lock");
	        if (file.createNewFile())
	        {
	            file.deleteOnExit();
	            return true;
	        }
	        return false;
	    }
	    catch (IOException e)
	    {
	        return false;
	    }
	}

	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, SQLException {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {					
					 if (!lock()) {
						 infoBox("Already running", "Application state");						 
					 }
					 else {
						 lock();
						 UiSwing window = new UiSwing();
						 window.frmTaskariAdmin.setVisible(true);
					 }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UiSwing() {
		initialize();
	}
	
	public void Show() {
		frmTaskariAdmin.setVisible(true);	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		try {

			ConnectionDB c = new ConnectionDB();
			Connection conn2 = c.Initialize();
			String t = c.GetTable();
			
			frmTaskariAdmin = new JFrame();
			frmTaskariAdmin.setTitle("Taskari Admin");
			frmTaskariAdmin.setBounds(100, 100, 450, 300);
			frmTaskariAdmin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frmTaskariAdmin.getContentPane().setLayout(null);

			JRadioButton rdbtnRead = new JRadioButton("Read");
			
			rdbtnRead.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {

					try {
						Viewer v = new Viewer(t, conn2);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					rdbtnRead.setSelected(false);
				}
			});
			
			rdbtnRead.setBounds(178, 76, 120, 23);
			rdbtnRead.setToolTipText("Show current data");
			rdbtnRead.setVerticalAlignment(SwingConstants.TOP);
			frmTaskariAdmin.getContentPane().add(rdbtnRead);
			
			JRadioButton rdbtnAdder = new JRadioButton("Add");

			rdbtnAdder.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					UiAdder a;
					try {
						a = new UiAdder();
						a.setVisible(true);
						frmTaskariAdmin.dispose();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					rdbtnAdder.setSelected(false);
				}
			});

			
			rdbtnAdder.setBounds(177, 39, 120, 23);
			rdbtnAdder.setVerticalAlignment(SwingConstants.TOP);
			frmTaskariAdmin.getContentPane().add(rdbtnAdder);

			JRadioButton rdbtnQuit = new JRadioButton("Quit");
			rdbtnQuit.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.exit(0);
				}
			});
			rdbtnQuit.setBounds(362, 7, 120, 23);
			rdbtnQuit.setVerticalAlignment(SwingConstants.TOP);
			frmTaskariAdmin.getContentPane().add(rdbtnQuit);

			
			txtRowToBeDeleted = new JTextField();
			txtRowToBeDeleted.setBounds(241, 122, 45, 20);
			frmTaskariAdmin.getContentPane().add(txtRowToBeDeleted);
			txtRowToBeDeleted.setColumns(10);

			
			JRadioButton rdbtnDel = new JRadioButton("Delete");
			
			rdbtnDel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Records r = new Records();
					int row = Integer.valueOf(txtRowToBeDeleted.getText());		
					System.out.println("row to be deleted " + row);
					r.DeleteRecords(t, conn2,row );
					infoBox("Record deleted", "Deletion Operation");
					rdbtnDel.setSelected(false);					
				}
			});
			
			rdbtnDel.setBounds(178, 122, 120, 23);
			frmTaskariAdmin.getContentPane().add(rdbtnDel);
			
			JRadioButton rdbtnDropDB = new JRadioButton("Drop DB");
			rdbtnDropDB.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				int result = JOptionPane.showConfirmDialog(frmTaskariAdmin,"Sure? You want to destroy db?", "Critical Confirmation",
			               JOptionPane.YES_NO_OPTION,
			               JOptionPane.QUESTION_MESSAGE);
			            if(result == JOptionPane.YES_OPTION){
							Records r = new Records();
							try {
								r.DropDB(t, conn2);
								infoBox("Database destroyed", "Deletion Operation");
							} catch (SQLException e1) {
								infoBox("Database destroying failed", "Failure");
								e1.printStackTrace();								
							}			            			          			            
			            }else if (result == JOptionPane.NO_OPTION){
			            	infoBox("Database removal cancelled", "Deletion Operation");
			            }				
			            rdbtnDropDB.setSelected(false);            
			}
		});
			
			rdbtnDropDB.setBounds(178, 159, 120, 23);
			frmTaskariAdmin.getContentPane().add(rdbtnDropDB);


		} catch (Exception e) {
			System.out.println("No database connection");
			e.printStackTrace();
		}

	}
	
	  public static void infoBox(String infoMessage, String titleBar)
	    {
		  JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	    }
	
	public void setString(String userString) {
		this.userString = userString;
	}

	public String getUserRow() {
		return this.userString;
	}

}
