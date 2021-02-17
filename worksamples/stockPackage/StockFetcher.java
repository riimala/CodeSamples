package stockPackage;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.UIManager;

public class StockFetcher {

	private JFrame frame;
	private JTextField txtStock;
	DefaultTableModel model = new DefaultTableModel();
	private JTable table_2;

	
	//When starting creates a file and on exit destroys so by checking status you can determine is application already running.
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
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (!lock()) {
						 infoBox("Already running", "Application state");		
						 return;
					}
						lock();
						StockFetcher window = new StockFetcher();
						window.frame.setVisible(true);					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StockFetcher() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 0, 0);
		frame = new JFrame();						

		String[] columnNames = {"Symbol", "Open $", "Bid $", "Ask $", "Forward PE $", "Analyze in euros"};		
		model.setColumnIdentifiers(columnNames);			
				
		frame.setBounds(100, 100, 600, 357);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setSize(600, 200);
		frame.setVisible(true);
		frame.setTitle("Stock Table");
		frame.getContentPane().setLayout(null);
					
		frame.getContentPane().add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 59, 580, 248);
		frame.getContentPane().add(scrollPane);
		
		table_2 = new JTable();
		scrollPane.setViewportView(table_2);
		
		txtStock = new JTextField();
		txtStock.setBounds(173, 22, 110, 20);
		frame.getContentPane().add(txtStock);
		
		//panel.add(table);
		txtStock.setColumns(10);
		JLabel lblNewLabel = new JLabel("Give Symbol:");
		lblNewLabel.setBounds(80, 25, 94, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnFetch = new JButton("FETCH");
		btnFetch.setBounds(293, 19, 110, 23);
		frame.getContentPane().add(btnFetch);
		btnFetch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			HttpInterActions getStock = new HttpInterActions();
			try {
				ArrayList<String> stockList = getStock.FetchRequest(txtStock.getText());
				String symbol = stockList.get(0);
				String open = stockList.get(1);
				String bid = stockList.get(2);
				String ask = stockList.get(3);
				String fPE = stockList.get(4);
				double aboutEuro = Double.parseDouble(fPE) * 0.83;
				String analyze = "";
				if ( aboutEuro <= 10 ) {
					analyze = "Ok to buy";
				}else {
					analyze = "No point .. likely";
				}
				
				Object []data = {symbol, open, bid, ask, fPE, analyze};
				//model.addColumn(columnNames);
				model.addRow(data);
				//
				//scrollPane.setViewportView(table);								
				table_2.setModel(model);
				//frame.dispose();
				
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
		});

	}
	
	  public static void infoBox(String infoMessage, String titleBar)
	    {
		  JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	    }
}
