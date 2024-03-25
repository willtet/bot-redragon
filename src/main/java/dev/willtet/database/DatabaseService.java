package dev.willtet.database;

import java.sql.SQLException;
import java.util.UUID;

public class DatabaseService {
	
	public static boolean existeUsuarioCadastrado(String idDiscord) {
		boolean existe = false;
		
		var conexao = ConnectionFactory.getConnection();
		String query = 
				"SELECT * FROM tb_usuarios WHERE discord_id = ? ";
		
		
		
		try {
			var stm = conexao.prepareStatement(query);
			
			stm.setString(1, idDiscord);
			
			var retorno = stm.executeQuery();
			
			if(retorno.next()) {
				existe = true;
			}
			
			
			stm.close();
			return existe;
		}catch (Exception e) {
			return false;
		}
	}

	public static boolean cadastrarUsuarioLegends(String globalName, String name, String id) {
		var conexao = ConnectionFactory.getConnection();
		String query = "INSERT INTO tb_usuarios (id, name, username, discord_id)"
				+ " VALUES (?, ?, ?, ?) ";
		
		
		
		try {
			var stm = conexao.prepareStatement(query);
			
			stm.setString(1, UUID.randomUUID().toString());
			stm.setString(2, globalName);
			stm.setString(3, name);
			stm.setString(4, id);
			
			stm.execute();
			stm.close();
			
			return true;
		}catch (Exception e) {
			
			e.getStackTrace();
			return false;
		}
	}

}
