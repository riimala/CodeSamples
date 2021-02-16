import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JScrollPane;

public class UiAdder extends JFrame {

	public String userString;
	private JPanel contentPane;
	private JFrame frame;
	private static JFrame frame2;

	private Boolean closeWindow = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UiAdder frame = new UiAdder();
					frame.setVisible(true);
					frame2 = frame;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public UiAdder() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLException{
		Records r = new Records();
		ConnectionDB c = new ConnectionDB();
		Connection con = c.Initialize();
		System.out.println("Connection ..: " + con);
		String table = c.GetTable();					
		System.out.println("Connection: " + con +  ", table: " + table);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(91, 88, 268, 74);
		contentPane.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		scrollPane.setViewportView(textArea);

				
		JLabel lblTask = new JLabel("Add new task");
		lblTask.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTask.setBounds(91, 55, 135, 22);
		contentPane.add(lblTask);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {																					
					userString = textArea.getText();
					
					if (con != null && table != null ) {
						try {
							r.CreateRecord(table, con, userString);		
							if (frame2 != null) {
								frame2.dispose();
							}
							
							UiSwing u = new UiSwing();
							u.infoBox("Record Added", "Addition Operation" );
							u.Show();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							try {
								con.close();
							} catch (SQLException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
						}
					}				
			}
		});
		btnAdd.setBounds(91, 180, 89, 23);
		contentPane.add(btnAdd);
		
	}
}
