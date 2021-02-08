import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Ui {
	
	public void start(String table, Connection conn2) throws SQLException {
		Records r = new Records();
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Commands:\nRead\nAdd\nDropDB\nDel\nCount\nQuit\n\nYour choice, Sir:");
		
		while(scan.hasNextLine()) {
		String command = scan.nextLine();
		
		switch (command) {
		case "Quit":
			break;
		case "quit":
			break;
		case "Read":
			r.ReadRecords(table, conn2);
			break;
		case "Add":
			r.CreateRecord(table, conn2);
			break;
		case "DropDB":
			r.DropDB(table, conn2);
			break;
		case "Del":
			r.DeleteRecords(table, conn2);
			break;
		case "Count":
			r.Count(table, conn2);
			break;
		default:
			r.Count(table, conn2);
			break;
		}	
		}
	}
	
}
