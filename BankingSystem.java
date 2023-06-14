import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.sql.CallableStatement;

/**
 * Manage connection to database and perform SQL statements.
 */
public class BankingSystem {
	// Connection properties
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	private static String currentId;
	
	// JDBC Objects
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	/**
	 * Initialize database connection given properties file.
	 * @param filename name of properties file
	 */
	public static void init(String filename) {
		try {
			Properties props = new Properties();						// Create a new Properties object
			FileInputStream input = new FileInputStream(filename);	// Create a new FileInputStream object using our filename parameter
			props.load(input);										// Load the file contents into the Properties object
			driver = props.getProperty("jdbc.driver");				// Load the driver
			url = props.getProperty("jdbc.url");						// Load the url
			username = props.getProperty("jdbc.username");			// Load the username
			password = props.getProperty("jdbc.password");			// Load the password
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test database connection.
	 */
	public static void testConnection() {
		System.out.println(":: TEST - CONNECTING TO DATABASE");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			con.close();
			System.out.println(":: TEST - SUCCESSFULLY CONNECTED TO DATABASE");
			} catch (Exception e) {
				System.out.println(":: TEST - FAILED CONNECTED TO DATABASE");
				e.printStackTrace();
			}
	  }

	/**
	 * Create a new customer.
	 * @param name customer name
	 * @param gender customer gender
	 * @param age customer age
	 * @param pin customer pin
	 */
	public static void newCustomer(String name, String gender, String age, String pin) 
	{
		System.out.println(":: CREATE NEW CUSTOMER - RUNNING");
		try {
        Class.forName(driver);                                                                  //load the driver
        Connection con = DriverManager.getConnection(url, username, password);                  //Create the connection
        CallableStatement cstmt = con.prepareCall("CALL p2.CUST_CRT(?,?,?,?,?,?,?)");                                                //Create a statement
		cstmt.setString (1, name);
		cstmt.setString (2, gender);
		cstmt.setInt (3, Integer.parseInt(age));
		cstmt.setInt (4, Integer.parseInt(pin));
		cstmt.registerOutParameter (5, java.sql.Types.INTEGER);
		cstmt.registerOutParameter (6, java.sql.Types.INTEGER);
		cstmt.registerOutParameter (7, java.sql.Types.VARCHAR);
		cstmt.executeUpdate();                            // Call the stored procedure
		int id, code;
		String message;
		id = cstmt.getInt(5);   
		code = cstmt.getInt(6);
		message = cstmt.getString(7);
		if(code == 0) {
			System.out.println(":: CREATE NEW CUSTOMER - SUCCESS");
			System.out.println("Your ID is " + id);
		} else {
			System.out.println("Error: " + message);
		}
		cstmt.close();                                                                           //Close the statement after we are done with the statement
        con.close();                                                                            //Close the connection after we are done with everything
      } catch (Exception e) {
		System.out.println(":: CREATE NEW CUSTOMER - FAILED");
        /*System.out.println("Exception in main()");
        e.printStackTrace();*/
      }
	}

	/**
	 */
	public static void login(int id, String pin)
	{
		System.out.println(":: LOGIN CUSTOMER - RUNNING");
		try {
        Class.forName(driver);                                                                  //load the driver
        Connection con = DriverManager.getConnection(url, username, password);                  //Create the connection
        CallableStatement cstmt = con.prepareCall("CALL p2.CUST_LOGIN(?,?,?,?,?)");                                                //Create a statement
		cstmt.setInt (1, id);
		cstmt.setInt (2, Integer.parseInt(pin));
		cstmt.registerOutParameter (3, java.sql.Types.INTEGER);
		cstmt.registerOutParameter (4, java.sql.Types.INTEGER);
		cstmt.registerOutParameter (5, java.sql.Types.VARCHAR);
		cstmt.executeUpdate();                            // Call the stored procedure
		int code;
		String message;   
		code = cstmt.getInt(4);
		message = cstmt.getString(5);
		if(code == 0) {
		System.out.println(":: LOGIN - SUCCESS");
		currentId = Integer.toString(id);
		p2.screenThree(currentId);
	  }
	  else {
		System.out.println("Error: " + message);
		System.out.println(":: LOGIN - FAILED");
	  }
		cstmt.close();                                                                           //Close the statement after we are done with the statement
        con.close();                                                                            //Close the connection after we are done with everything
      } catch (Exception e) {
		System.out.println(":: LOGIN - FAILED");
        /*System.out.println("Exception in main()");
        e.printStackTrace();*/
      }
	}

	/**
	 * Open a new account.
	 * @param id customer id
	 * @param type type of account
	 * @param amount initial deposit amount
	 */
	public static void openAccount(String id, String type, String amount) 
	{
		System.out.println(":: OPEN ACCOUNT - RUNNING");
		try {
        Class.forName(driver);                                                                  //load the driver
        Connection con = DriverManager.getConnection(url, username, password);                  //Create the connection
        CallableStatement cstmt = con.prepareCall("CALL p2.ACCT_OPN(?,?,?,?,?,?)");                                                //Create a statement
		cstmt.setInt (1, Integer.parseInt(id));
		cstmt.setInt (2, Integer.parseInt(amount));
		cstmt.setString (3, type);
		cstmt.registerOutParameter (4, java.sql.Types.INTEGER);
		cstmt.registerOutParameter (5, java.sql.Types.INTEGER);
		cstmt.registerOutParameter (6, java.sql.Types.VARCHAR);
		cstmt.executeUpdate();                            // Call the stored procedure
		int number, code;
		String message;
		number = cstmt.getInt(4);   
		code = cstmt.getInt(5);
		message = cstmt.getString(6);
		if(code == 0) {
			System.out.println(":: OPEN ACCOUNT - SUCCESS");
			System.out.println("Your bank account number is " + number);
		} else {
			System.out.println("Error: " + message);
		}
		cstmt.close();                                                                                         //Close the statement after we are done with the statement
        con.close();                                                                            //Close the connection after we are done with everything
      }
	  catch (Exception e) {
		System.out.println(":: OPEN ACCOUNT - FAILED");
        /*System.out.println("Exception in main()");
        e.printStackTrace();*/
      }
	}

	/**
	 * Close an account.
	 * @param accNum account number
	 */
	public static void closeAccount(String accNum) 
	{
		System.out.println(":: CLOSE ACCOUNT - RUNNING");
		try {
        Class.forName(driver);                                                                  //load the driver
        Connection con = DriverManager.getConnection(url, username, password);                  //Create the connection
        CallableStatement cstmt = con.prepareCall("CALL p2.ACCT_CLS(?,?,?)");                                                //Create a statement
		cstmt.setInt (1, Integer.parseInt(accNum));
		cstmt.registerOutParameter (2, java.sql.Types.INTEGER);
		cstmt.registerOutParameter (3, java.sql.Types.VARCHAR);
		cstmt.executeUpdate();                            // Call the stored procedure
		int code;
		String message;  
		code = cstmt.getInt(2);
		message = cstmt.getString(3);
		if(code == 0) {
			System.out.println(":: CLOSE ACCOUNT - SUCCESS");
		} else {
			System.out.println("Error: " + message);
		}
		cstmt.close();                                                             //Close the statement after we are done with the statement
        con.close();                                                                            //Close the connection after we are done with everything
      } catch (Exception e) {
		System.out.println(":: CLOSE ACCOUNT - FAILED");
        /*System.out.println("Exception in main()");
        e.printStackTrace();*/
      }
	}

	/**
	 * Deposit into an account.
	 * @param accNum account number
	 * @param amount deposit amount
	 */
	public static void deposit(String accNum, String amount) 
	{
		System.out.println(":: DEPOSIT - RUNNING");
		try {
        Class.forName(driver);                                                                  //load the driver
        Connection con = DriverManager.getConnection(url, username, password);                  //Create the connection
        CallableStatement cstmt = con.prepareCall("CALL p2.ACCT_DEP(?,?,?,?)");                                                //Create a statement
		cstmt.setInt (1, Integer.parseInt(accNum));
		cstmt.setInt (2, Integer.parseInt(amount));
		cstmt.registerOutParameter (3, java.sql.Types.INTEGER);
		cstmt.registerOutParameter (4, java.sql.Types.VARCHAR);
		cstmt.executeUpdate();                            // Call the stored procedure
		int code;
		String message;
		code = cstmt.getInt(3);
		message = cstmt.getString(4);
		if(code == 0) {
			System.out.println(":: DEPOSIT - SUCCESS");
		} else {
			System.out.println("Error: " + message);
		}
		cstmt.close();                                                                          //Close the statement after we are done with the statement
        con.close();                                                                            //Close the connection after we are done with everything
      } catch (Exception e) {
		System.out.println(":: DEPOSIT - FAILED");
        /*System.out.println("Exception in main()");
        e.printStackTrace();*/
      }
	}

	/**
	 * Withdraw from an account.
	 * @param accNum account number
	 * @param amount withdraw amount
	 */
	public static void withdraw(String accNum, String amount) 
	{
		System.out.println(":: WITHDRAW - RUNNING");
		try {
        Class.forName(driver);                                                                  //load the driver
        Connection con = DriverManager.getConnection(url, username, password);                  //Create the connection
        CallableStatement cstmt = con.prepareCall("CALL p2.ACCT_WTH(?,?,?,?)");                                                //Create a statement
		cstmt.setInt (1, Integer.parseInt(accNum));
		cstmt.setInt (2, Integer.parseInt(amount));
		cstmt.registerOutParameter (3, java.sql.Types.INTEGER);
		cstmt.registerOutParameter (4, java.sql.Types.VARCHAR);
		cstmt.executeUpdate();                            // Call the stored procedure
		int code;
		String message;
		code = cstmt.getInt(3);
		message = cstmt.getString(4);
		if(code == 0) {
			System.out.println(":: WITHDRAW - SUCCESS");
		} else {
			System.out.println("Error: " + message);
		}
		cstmt.close();                                                                           //Close the statement after we are done with the statement
        con.close();                                                                            //Close the connection after we are done with everything
      } catch (Exception e) {
		System.out.println(":: WITHDRAW - FAILED");
        /*System.out.println("Exception in main()");
        e.printStackTrace();*/
      }
	}

	/**
	 * Transfer amount from source account to destination account. 
	 * @param srcAccNum source account number
	 * @param destAccNum destination account number
	 * @param amount transfer amount
	 */
	public static void transfer(String srcAccNum, String destAccNum, String amount) 
	{
		System.out.println(":: TRANSFER - RUNNING");
		try {
        Class.forName(driver);                                                                  //load the driver
        Connection con = DriverManager.getConnection(url, username, password);                  //Create the connection
        CallableStatement cstmt = con.prepareCall("CALL p2.ACCT_TRX(?,?,?,?,?)");                                                //Create a statement
		cstmt.setInt (1, Integer.parseInt(srcAccNum));
		cstmt.setInt (2, Integer.parseInt(destAccNum));
		cstmt.setInt (3, Integer.parseInt(amount));
		cstmt.registerOutParameter (4, java.sql.Types.INTEGER);
		cstmt.registerOutParameter (5, java.sql.Types.VARCHAR);
		cstmt.executeUpdate();                            // Call the stored procedure
		int code;
		String message; 
		code = cstmt.getInt(4);
		message = cstmt.getString(5);
		if(code == 0) {
			System.out.println(":: TRANSFER - SUCCESS");
		} else {
			System.out.println("Error: " + message);
		}
		cstmt.close();                                                                          //Close the statement after we are done with the statement
        con.close();                                                                            //Close the connection after we are done with everything
      } catch (Exception e) {
		System.out.println(":: TRANSFER - FAILED: NOT ACCOUNT HOLDER");
        /*System.out.println("Exception in main()");
        e.printStackTrace();*/
      }	
	}

	/**
	 * Display account summary.
	 * @param cusID customer ID
	 */
	public static void accountSummary(String cusID) 
	{
		System.out.println(":: ACCOUNT SUMMARY - RUNNING");
		try {
        Class.forName(driver);                                                                  //load the driver
        Connection con = DriverManager.getConnection(url, username, password);                  //Create the connection
        Statement stmt = con.createStatement();                                                 //Create a statement
		String query = "SELECT number, balance, type, status FROM p2.account WHERE status<>'I' AND id=" + cusID;            //The query to run
		ResultSet rs = stmt.executeQuery(query);                                                //Executing the query and storing the results in a Result Set
        int total = 0;
		System.out.println("CUSTOMER " + cusID + "'S ACCOUNT(S)");
		while(rs.next()) {
			int number = rs.getInt(1);
			int balance = rs.getInt(2);
			String type = rs.getString(3);
			String status = rs.getString(4);
			System.out.println("--Account " + number + "--");
			System.out.println("Balance: $" + balance);
			System.out.println("Type: " + type);
			System.out.println("Status: " + status);
			total += balance;
			System.out.println();
		}
		System.out.println("Total balance: $" + total);
		stmt.close();                                                                           //Close the statement after we are done with the statement
        con.close();                                                                            //Close the connection after we are done with everything
      } catch (Exception e) {
		System.out.println(":: ACCOUNT SUMMARY - FAILED");
        /*System.out.println("Exception in main()");
        e.printStackTrace();*/
      }		
		System.out.println(":: ACCOUNT SUMMARY - SUCCESS");
	}

	/**
	 * Display Report A - Customer Information with Total Balance in Decreasing Order.
	 */
	public static void reportA() 
	{
		System.out.println(":: REPORT A - RUNNING");
		try {
        Class.forName(driver);                                                                  //load the driver
        Connection con = DriverManager.getConnection(url, username, password);                  //Create the connection
        Statement stmt = con.createStatement();                                                 //Create a statement
		String query = "select p2.customer.id, p2.customer.name, p2.customer.gender, p2.customer.age, sum(p2.account.balance) as balance from p2.customer, p2.account where p2.customer.id=p2.account.id group by p2.customer.id, p2.customer.name, p2.customer.gender, p2.customer.age order by balance desc;";            //The query to run
		ResultSet rs = stmt.executeQuery(query);                                                //Executing the query and storing the results in a Result Set
		System.out.println("==REPORT A==");
		System.out.println("ID    NAME    GENDER    AGE    TOTAL BALANCE");
		System.out.println("----------------------------------------------");
		while(rs.next()) {
			int id = rs.getInt(1);
			String name = rs.getString(2);
			String gender = rs.getString(3);
			int age = rs.getInt(4);
			int total = rs.getInt(5);
			System.out.print(id + "    " + name);
			for(int i = name.length(); i < 10; i++) {
				System.out.print(" ");
			}
			System.out.print(gender + "      " + age + "      $" + total);
			System.out.println();
		}
		stmt.close();                                                                           //Close the statement after we are done with the statement
        con.close();                                                                            //Close the connection after we are done with everything
      } catch (Exception e) {
		System.out.println(":: REPORT A - FAILED");
        /*System.out.println("Exception in main()");
        e.printStackTrace();*/
      }		
		System.out.println(":: REPORT A - SUCCESS");
	}

	/**
	 * Display Report B - Customer Information with Total Balance in Decreasing Order.
	 * @param min minimum age
	 * @param max maximum age
	 */
	public static void reportB(String min, String max) 
	{
		System.out.println(":: REPORT B - RUNNING");
		try {
        Class.forName(driver);                                                                  //load the driver
        Connection con = DriverManager.getConnection(url, username, password);                  //Create the connection
        Statement stmt = con.createStatement();                                                 //Create a statement
		String query = "select sum(p2.account.balance) from p2.account join p2.customer on p2.account.id=p2.customer.id where p2.customer.age<="+max+" and p2.customer.age>="+min;            //The query to run
		ResultSet rs = stmt.executeQuery(query);                                                //Executing the query and storing the results in a Result Set
		int total = 0, count = 0;
		while(rs.next()) {
			total = rs.getInt(1);
		}
		query = "SELECT COUNT(*) FROM p2.customer WHERE age<=" + max + " AND age>=" + min;
		rs = stmt.executeQuery(query);                                                //Executing the query and storing the results in a Result Set
		while(rs.next()) {
			count = rs.getInt(1);
		}
		System.out.println("==REPORT B==");
		System.out.println();
		System.out.println("AVERAGE");
		System.out.println("---------");
		System.out.println("$" + total / count);
		stmt.close();                                                                           //Close the statement after we are done with the statement
        con.close();                                                                            //Close the connection after we are done with everything
      } catch (Exception e) {
		System.out.println(":: REPORT B - FAILED");
        /*System.out.println("Exception in main()");
        e.printStackTrace();*/
      }	
		System.out.println(":: REPORT B - SUCCESS");
	}

	/**
	 * Transfer amount from source account to destination account. 
	 * @param saveRate interest rate for saving accounts
	 * @param checkRate interest rate for checking accounts
	 */
	public static void rate(String saveRate, String checkRate) 
	{
		System.out.println(":: APPLYING RATE TO ACCOUNTS - RUNNING");
		try {
        Class.forName(driver);                                                                  //load the driver
        Connection con = DriverManager.getConnection(url, username, password);                  //Create the connection
        CallableStatement cstmt = con.prepareCall("CALL p2.ADD_INTEREST(?,?,?,?)");                                                //Create a statement
		cstmt.setFloat (1, Float.parseFloat(saveRate));
		cstmt.setFloat (2, Float.parseFloat(checkRate));
		cstmt.registerOutParameter (3, java.sql.Types.INTEGER);
		cstmt.registerOutParameter (4, java.sql.Types.VARCHAR);
		cstmt.executeUpdate();                            // Call the stored procedure
		int code;
		String message; 
		code = cstmt.getInt(4);
		message = cstmt.getString(5);
		if(code == 0) {
			System.out.println(":: APPLYING RATE TO ACCOUNT - SUCCESS");
		} else {
			System.out.println("Error: " + message);
		}
		cstmt.close();                                                                          //Close the statement after we are done with the statement
        con.close();                                                                            //Close the connection after we are done with everything
      } catch (Exception e) {
		System.out.println(":: APPLYING RATE TO ACCOUNT - FAILED");
        /*System.out.println("Exception in main()");
        e.printStackTrace();*/
      }	
	}

}
