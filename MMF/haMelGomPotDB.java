package mmf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class haMelGomPotDB {
	int length = 0;
	static int pre;
	static String name;
	String path = null;

	
	//드라이버 클래스 로드
	static {
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Connection
	public Connection getConnection() {
		Connection con = null;
		
		String url="jdbc:mysql://localhost:3306/javadb?"
				+ "useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
		String user = "javadb";
		String password="12345";
		
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public List<haMelGomPotVO> getList() {
		List<haMelGomPotVO> list = new ArrayList<haMelGomPotVO>();
		
		String sql = "select * from bookTBL";
		
		try(Connection con = getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql)
						) {
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				haMelGomPotVO vo = new haMelGomPotVO();
				path = rs.getString("path");	
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
		}
	
	public int Random() {
		java.util.Random random = new java.util.Random();
		
		int[] check = new int[length];
		int adder = 0;
		
		for(int r=0; r<length; r++) {
			check[r] = random.nextInt(length);
			
			for(int i=0; i<r; i++) {
				if(check[i]==check[r]) {
					check[r] = random.nextInt(length);
				}
			}
			adder = check[length-1];
		}
		return adder;
	}
	
	public int Pre() {
		return pre;
	}
	public String Name() {
		return name;
	}
}



















