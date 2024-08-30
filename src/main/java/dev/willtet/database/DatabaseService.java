package dev.willtet.database;

import dev.willtet.model.Role;
import dev.willtet.model.vo.TopRankVO;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseService {
	
	public static boolean existeUsuarioCadastrado(String idDiscord) {
		boolean existe = false;
		
		var conexao = ConnectionFactory.getConnection();
		String query = 
				"SELECT * FROM tb_usuarios WHERE id = ? ";
		
		
		
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
		String query = "INSERT INTO tb_usuarios (id, name, username, data_entrada)"
				+ " VALUES (?, ?, ?, ?) ";
		
		
		
		try {
			var stm = conexao.prepareStatement(query);
			
			stm.setString(1, id);
			stm.setString(2, globalName);
			stm.setString(3, name);
			stm.setDate(4,  new Date(System.currentTimeMillis()));
			
			stm.execute();
			stm.close();
			
			return true;
		}catch (Exception e) {
			
			e.getStackTrace();
			return false;
		}
	}

	public static boolean postPontosByUsuario(String idMensagem, String idUsuario, String mensagem) {
		var conexao = ConnectionFactory.getConnection();


		String query = "INSERT INTO tb_publicacao (id_mensagem, id_usuario, valido, mensagem, data_entrada, ponto_peso)"
				+ " VALUES (?, ?, ?, ?, ?, ?)";

		try {
			var stm = conexao.prepareStatement(query);

			stm.setString(1, idMensagem);
			stm.setString(2, idUsuario);
			stm.setBoolean(3, false);
			stm.setString(4, mensagem);
			stm.setDate(5,  new Date(System.currentTimeMillis()));
			stm.setInt(6,  1);

			stm.execute();
			stm.close();

			return true;
		}catch (Exception e) {

			e.getStackTrace();
			return false;
		}
	}

	public static boolean postPontosByLegado(String idMensagem, String idUsuario, String mensagem, int pontos) {
		var conexao = ConnectionFactory.getConnection();


		String query = "INSERT INTO tb_publicacao (id_mensagem, id_usuario, valido, mensagem, data_entrada, ponto_peso)"
				+ " VALUES (?, ?, ?, ?, ?, ?)";

		try {
			var stm = conexao.prepareStatement(query);

			stm.setString(1, idMensagem);
			stm.setString(2, idUsuario);
			stm.setBoolean(3, true);
			stm.setString(4, mensagem);
			stm.setDate(5,  new Date(System.currentTimeMillis()));
			stm.setInt(6,  pontos);

			stm.execute();
			stm.close();

			return true;
		}catch (Exception e) {

			e.getStackTrace();
			return false;
		}
	}


	public static int findPontosByUsuario(String idUsuario) {
		var conexao = ConnectionFactory.getConnection();
		var count = 0;


		String query = "SELECT SUM(ponto_peso) as Soma FROM tb_publicacao "
				+ " WHERE valido = 1"
				+ " AND id_usuario = ?";

		try {
			var stm = conexao.prepareStatement(query);

			stm.setString(1, idUsuario);

			var retorno = stm.executeQuery();

			if(retorno.next()) {
				count = retorno.getInt("Soma");
			}

			stm.close();
			return count;
		}catch (Exception e) {

			e.getStackTrace();
			return 0;
		}
	}

	public static boolean updatePontosByUsuario(String idMensagem, boolean valido) {
		var conexao = ConnectionFactory.getConnection();


		String query = "UPDATE tb_publicacao "
				+ " SET valido = ? "
				+ " WHERE id_mensagem = ?";

		try {
			var stm = conexao.prepareStatement(query);

			stm.setBoolean(1, valido);
			stm.setString(2, idMensagem);

			stm.execute();
			stm.close();

			return true;
		}catch (Exception e) {

			e.getStackTrace();
			return false;
		}
	}


	public static String findRoleIdByPontos(int pontos) {

		var conexao = ConnectionFactory.getConnection();
		var count = "";


		String query = "SELECT nome " +
				"FROM tb_roles " +
				"WHERE ? BETWEEN min_range AND max_range;";

		try {
			var stm = conexao.prepareStatement(query);

			stm.setInt(1, pontos);

			var retorno = stm.executeQuery();

			if(retorno.next()) {
				count = retorno.getString("nome");
			}

			stm.close();
			return count;
		}catch (Exception e) {

			e.getStackTrace();
			return null;
		}
	}

	public static Role findRoleByPontos(int pontos) {

		var conexao = ConnectionFactory.getConnection();
		Role role = null;


		String query = "SELECT role.* " +
				"FROM tb_roles role " +
				"WHERE ? BETWEEN min_range AND max_range;";

		try {
			var stm = conexao.prepareStatement(query);

			stm.setInt(1, pontos);

			var retorno = stm.executeQuery();

			if (retorno.next()) {
				// Supondo que a classe Role tenha um construtor que aceita os parâmetros correspondentes às colunas da tabela
				role = new Role(
						retorno.getInt("id"),
						retorno.getString("nome"),
						retorno.getInt("min_range"),
						retorno.getInt("max_range"),
						retorno.getString("id_discord")
				);
			}

			stm.close();
			retorno.close();
		}catch (Exception e) {

			e.getStackTrace();
			return null;
		}finally {
			try {
				if (conexao != null && !conexao.isClosed()) {
					conexao.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return role;
	}

	public static List<TopRankVO> findTopRanking() {

		var conexao = ConnectionFactory.getConnection();
		List<TopRankVO> ranking = new ArrayList<>();


		String query = "SELECT " +
				" sum(pubri.ponto_peso) pontos, " +
				" tu.name nome" +
				" FROM tb_publicacao pubri " +
				" INNER JOIN tb_usuarios tu " +
				" ON tu.id = pubri.id_usuario " +
				" GROUP BY id_usuario " +
				" ORDER BY pontos DESC " +
				" LIMIT 25";

		try {
			var stm = conexao.prepareStatement(query);

			var retorno = stm.executeQuery();

			while (retorno.next()) {

				TopRankVO vo = new TopRankVO(
							retorno.getInt("pontos"),
							retorno.getString("nome")
					);

				ranking.add(vo);
			}

			stm.close();
			retorno.close();
		}catch (Exception e) {

			e.getStackTrace();
			return null;
		}finally {
			try {
				if (conexao != null && !conexao.isClosed()) {
					conexao.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return ranking;
	}

}
