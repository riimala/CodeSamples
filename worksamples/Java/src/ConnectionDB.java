import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionDB {

    private static final String DB_URL = "jdbc:mysql://localhost:3306";
    public java.sql.Connection conn2;// = null;
    public String table;
    private static Connection instance = null;
    private Connection c;
    
    // Database credentials
    private static String USER = "root";
    private static String PASS = "";
    
    private static String db;
    

    // static Connection conn2 = null;

    public ConnectionDB() {    	
    	this.table = "JRTable";
    	this.conn2 = null;
    	this.c = null;
    }
    
    public Connection Initialize()
            throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

        String[] params = new String[] { "CLIDB", "JRTable" };
        db = params[0];
        table = params[1];

        try {
            System.out.println("db: " + db + "\ntaulu: " + table);
            String sqlDB = "CREATE DATABASE IF NOT EXISTS " + db;           
            
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            java.sql.Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Closing connection and opening new one after connection with database is ok.
            // conn.close();

            PreparedStatement createDbStat = conn.prepareStatement(sqlDB);
            createDbStat.execute();
                    
            java.sql.Connection conn2 = DriverManager.getConnection(DB_URL + "/" + db, USER, PASS);

            /*Works, but new user cannot be created if there is no database yet..*/
            Statement stUser = conn2.createStatement();
            stUser.execute("CREATE USER IF NOT EXISTS 'hemmo'@'localhost' IDENTIFIED BY 'hemmoPass'");
            Statement stPass = conn2.createStatement();            
            String grant = "GRANT ALL PRIVILEGES ON " + db + " TO " + "hemmo@localhost";            
            stPass.execute(grant);
            
            String sqlCreateTable = "CREATE TABLE IF NOT EXISTS " + table
                    + "(ID int unsigned auto_increment not null PRIMARY KEY, " + " Nimi VARCHAR(100), "
                    + " date_created timestamp default now())";
            PreparedStatement stmtTable = conn2.prepareStatement(sqlCreateTable);
            stmtTable.executeUpdate(sqlCreateTable);
            c = conn2;            
            //Ui userInterface = new Ui();
            //userInterface.start(table, conn2);                        
                                        
        } catch (SQLException s) {
            s.printStackTrace();
        }
        return c;
    }
    
    public String GetTable() {
    	return this.table;
    }
    
    
    public Connection getInstance() {
    	if(instance == null)
    	return conn2;
    	else
    	return instance;
    	}
    
    public Connection GetConnection() {
    	return c;
    }

	
	
}
