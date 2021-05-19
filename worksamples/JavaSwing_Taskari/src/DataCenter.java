import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.util.Date;
import java.time.LocalDate;
import javax.swing.ListSelectionModel;
import java.awt.Insets;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.border.CompoundBorder;

public class DataCenter extends JFrame {

	private JPanel contentPane;
	private JTextField txtDue;
	private static JFrame frame2;
	private int idrivi;
	DefaultTableModel model;
	private JTable table;
	private Connection conn2;
	private String t;

	private static boolean lock() {
		try {
			final File file = new File("bpmdj.lock");
			if (file.createNewFile()) {
				file.deleteOnExit();
				return true;
			}
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	// Tarkista pendaavat taskit ja heitä ne message boxiin joka aukaistaan kun
	// applikaatio käynistyy

	public void checkPending(/*String table, Connection conn2*/) {
		int count = 0;
		System.out.println("checkPending function");
		String query = "SELECT * FROM " + t;
		// create the java statement
		try {
			LocalDate today = LocalDate.now();
			PreparedStatement st = conn2.prepareStatement(query);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("ID");
				String taski = rs.getString("Taski");
				String taski2 = taski.replaceAll("(.{80})", "$1\n");
				LocalDate dateSql = rs.getDate("date_created").toLocalDate();
				String status = rs.getString("done");
				if (dateSql.isAfter(today) && status.equals("Ei tehty")) {
					System.out.println(taski2 + ", pending date: " + dateSql + ", " + status);
					infoBox(id + ", " + taski2 + ", " + dateSql + ", " + status, "Pendaavat");
					count++;
				}
			}
			System.out.println("There are " + count + " records pending (not done) in " + table);
		} catch (Exception e) {
			System.out.println("Check XAMPP mysql module");
			e.printStackTrace();
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (!lock()) {
						infoBox("Appi käynnissä", "Sovelluksen tila");
						System.exit(ABORT);
					}
					lock();
					DataCenter frame = new DataCenter();
					frame.checkPending();
					frame.setVisible(true);
					frame2 = frame;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	}

	public static int confirmation() {
		// JFrame frame;
		Object[] options = { "Suorita", "Peruuta" };
		int n = JOptionPane.showOptionDialog(frame2, "Edetäänkö?", "Vahvista toimenpide", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, // the titles of buttons
				options[0]); // default button title
		return n;
	}

	/**
	 * Create the frame.
	 * 
	 * @throws SQLException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */

	public int getId(int id) {
		return id;
	}

	public DataCenter()
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, SQLException {

		/*ConnectionDB c = new ConnectionDB();
		Connection conn2 = c.Initialize();
		String t = c.GetTable();*/
		ConnectionDB c = new ConnectionDB();
		this.conn2 = c.Initialize();
		this.t = c.GetTable();

		setTitle("Taskari");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 980, 455);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setAutoscrolls(true);
		scrollPane_1.setBounds(81, 52, 530, 89);
		contentPane.add(scrollPane_1);

		JTextArea txtAreaTaski = new JTextArea();
		scrollPane_1.setViewportView(txtAreaTaski);
		txtAreaTaski.setBorder(UIManager.getBorder("TextArea.border"));
		txtAreaTaski.setMargin(new Insets(2, 10, 2, 2));
		txtAreaTaski.setLineWrap(true);
		txtAreaTaski.setWrapStyleWord(true);

		JLabel lblNewLabel = new JLabel("Taski");
		lblNewLabel.setBounds(81, 27, 100, 14);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Menness\u00E4");
		lblNewLabel_1.setBounds(631, 21, 119, 14);
		contentPane.add(lblNewLabel_1);

		LocalDate thisDate = LocalDate.now();
		txtDue = new JTextField(thisDate.toString());
		txtDue.setBounds(631, 52, 126, 22);
		contentPane.add(txtDue);
		txtDue.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Hoidettu");
		lblNewLabel_2.setBounds(795, 21, 126, 14);
		contentPane.add(lblNewLabel_2);

		JCheckBox cbDone = new JCheckBox("Done");
		cbDone.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		cbDone.setBounds(795, 52, 97, 24);
		contentPane.add(cbDone);

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] {}));
		table.setAutoCreateRowSorter(true);

		table.setFillsViewportHeight(true);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UpdateFields(txtAreaTaski, txtDue, cbDone);
			}
		});
		table.setColumnSelectionAllowed(true);

		// table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// table.setSurrendersFocusOnKeystroke(true);

		String[] columnNames = { "Id", "Taskit", "Merkitsevä päivä", "Hoidettu" };
		model = new DefaultTableModel();
		model.setColumnIdentifiers(columnNames);

		String query = "SELECT * FROM " + t;
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);

		try {
			while (rs.next()) {
				int id = rs.getInt("ID");
				String name = rs.getString("Taski");
				Date dateCreated = rs.getDate("date_created");
				String done = rs.getString("done");
				System.out.println(id + ", " + name + ", " + dateCreated + ", " + done);
				Object[] data = { id, name, dateCreated, done };
				model.addRow(data);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			System.out.println("Recordit tuotu kannasta ja pitäisi näkyä taulussa");
			table.setModel(model);
			table.setBounds(150, 200, 750, 500);
			table.setPreferredScrollableViewportSize(new Dimension(500, 500));
			table.setFillsViewportHeight(true);
			table.setVisible(true);
			System.out.println("Column count: " + table.getColumnCount());
			setTableLayout();
		}

		JButton btnNew = new JButton("+");
		btnNew.setToolTipText("T\u00E4yt\u00E4 boxit ja klikkaa lis\u00E4t\u00E4ksesi tietokantaan.");
		btnNew.addMouseListener(new MouseAdapter() {
			String done = "";

			@Override
			public void mouseClicked(MouseEvent e) {
				try {

					int ret = confirmation();
					if (ret == JOptionPane.YES_OPTION) {

						String userString = txtAreaTaski.getText();

						// Get Date
						String d = txtDue.getText();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						java.util.Date langDate;

						try {
							langDate = sdf.parse(d);
							java.sql.Date sqlDate = new java.sql.Date(langDate.getTime());
							// checkbox handling
							if (cbDone.isSelected()) {
								done = "Tehty";
							} else {
								done = "Ei tehty";
							}

							Records r = new Records();
							if (conn2 != null && table != null) {
								try {
									r.CreateRecord(t, conn2, userString, sqlDate, done);
									/*
									 * if (frame2 != null) { frame2.dispose(); }
									 */
									model.setRowCount(0);
									ReadRecords(t, conn2);
									Tyhjaa(txtAreaTaski, txtDue, cbDone);
									infoBox("Recordi luotu", "Recording lisäys");

								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}

						} catch (ParseException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

					} else {
						infoBox("Peruutettu", "Recordin lisäys");
					}
				}

				finally {
					System.out.println("finally block recordi lisätty suoritettu");
				}

			}
		}

		);
		btnNew.setBounds(81, 332, 46, 23);
		contentPane.add(btnNew);

		JButton btnPoista = new JButton("-");
		btnPoista.setToolTipText("Poista tietokannasta.");
		btnPoista.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int ret = confirmation();
				if (ret == JOptionPane.YES_OPTION) {

					Records r = new Records();
					int row = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());

					System.out.println("Poistettava rivi " + row);
					if (row > 0) {
						r.DeleteRecords(t, conn2, row);
						model.setRowCount(0);
						try {
							ReadRecords(t, conn2);
							Tyhjaa(txtAreaTaski, txtDue, cbDone);
							infoBox("Recordi tuhottu", "Deletointi");
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					else {
						infoBox("Toiminto peruutettu", "Deletointi");
					}

				}
			}
		}

		);
		btnPoista.setBounds(150, 332, 37, 23);
		contentPane.add(btnPoista);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		scrollPane.setBounds(81, 169, 773, 102);
		contentPane.add(scrollPane);

		scrollPane.setViewportView(table);

		JButton btnPaivita = new JButton("P\u00E4ivit\u00E4");
		btnPaivita.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Records r = new Records();
				String taski = txtAreaTaski.getText();
				// java.sql.Date due = (java.sql.Date) table.getValueAt(table.getSelectedRow(),
				// 2);
				String due = txtDue.getText();
				java.sql.Date pvm = Date.valueOf(due);
				String done = "";
				if (cbDone.isSelected()) {
					done = "Tehty";
				} else {
					// table.getValueAt(table.getSelectedRow(), 3).toString();
					done = "Ei tehty";
				}

				try {
					r.UpdateRecords(t, conn2, idrivi, taski, pvm, done);
					// model.setRowCount(0);
					ReadRecords(t, conn2);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnPaivita.setBounds(210, 332, 89, 23);
		contentPane.add(btnPaivita);

		// checkPending(t, conn2);
	}

	public void UpdateFields(JTextArea txtAreaTaski, JTextField txtDue, JCheckBox cbDone) {

		int id = (int) table.getValueAt(table.getSelectedRow(), 0);
		this.idrivi = id;

		String taski = table.getValueAt(table.getSelectedRow(), 1).toString();
		String due = table.getValueAt(table.getSelectedRow(), 2).toString();
		String done = table.getValueAt(table.getSelectedRow(), 3).toString();

		System.out.println("UpdateFields functio: " + idrivi + ", " + taski + ", " + due + ", " + done);

		// Päivitä kentät
		txtAreaTaski.setText(taski);

		txtDue.setText("" + due);

		if (done.equals("Ei tehty")) {
			cbDone.setSelected(false);
		} else {
			cbDone.setSelected(true);
		}

	}

	public void Tyhjaa(JTextArea txtAreaTaski, JTextField txtDue, JCheckBox cbDone) {
		txtAreaTaski.setText("");
		// txtDue.setText("");
		cbDone.setSelected(false);
	}

	public void setTableLayout() {
		// table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(600);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(50);

	}

	public void ReadRecords(String t, Connection conn2) throws SQLException {
		model = new DefaultTableModel();
		String[] columnNames = { "Id", "Taski", "PVM", "Done" };
		model.setColumnIdentifiers(columnNames);

		model.setRowCount(0);
		String query = "SELECT * FROM " + t;
		Statement st = conn2.createStatement();
		ResultSet rs = st.executeQuery(query);

		while (rs.next()) {
			int id = rs.getInt("ID");
			String taski = rs.getString("Taski");
			String pvm = rs.getString("date_created");
			String done = rs.getString("done");

			Object o[] = { id, taski, pvm, done };
			model.addRow(o);
		}

		table.setModel(model);
		setTableLayout();

		table.setFillsViewportHeight(true);

		/*
		 * table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 * table.getColumnModel().getColumn(0).setPreferredWidth(10);
		 * table.getColumnModel().getColumn(1).setPreferredWidth(400);
		 * table.getColumnModel().getColumn(2).setPreferredWidth(100);
		 * table.getColumnModel().getColumn(3).setPreferredWidth(50);
		 * table.setModel(model); table.setFillsViewportHeight(true);
		 */
	}
}
