package dev.willtet.database;

public class DatabaseService {
	ConnectionFactory con = new ConnectionFactory();
	
	public boolean existeUsuarioCadastrado(String idDiscord) {
		var conexao = con.getConnection();
		String query = "SELECT ";
		
		
		try {
			var stm = conexao.createStatement();
			
			stm.executeQuery(query);
			
			return false;
		}catch (Exception e) {
			return false;
		}
	}

}
