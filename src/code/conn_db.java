package code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class conn_db {
	public Connection con;

	public void connection(){
		try {  
		      Class.forName("com.mysql.cj.jdbc.Driver");     //¼ÓÔØMYSQL JDBCÇý¶¯³ÌÐò     
		     System.out.println("Success loading Mysql Driver!");  
		   	}  
		    catch (Exception e){  
		      System.out.print("Error loading Mysql Driver!");  
		      e.printStackTrace();  
		    }  
		    try{  
		      con = DriverManager.getConnection(  
		          "jdbc:mysql://localhost:3306/hospital?serverTimezone=UTC&useSSL=false&user=root&password=1234");   
		      System.out.println("Success connect Mysql server!");  
		      /*Statement stmt = con.createStatement();  
		      ResultSet rs = stmt.executeQuery("select * from T_KSXX");  
		      while (rs.next()) {  
		        System.out.println(rs.getString("KSBH")); 
		      } */
		    } 
		    catch (Exception e) {  
		      System.out.print("get data error!");  
		      e.printStackTrace();  
		    } 
		}   
		
	/*public static void main(String[] args){	   
		 conn_db lo=new conn_db();
		 lo.connection();
	}*/

}
