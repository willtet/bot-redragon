package dev.willtet.database;

import dev.willtet.model.Publicacao;
import dev.willtet.model.Role;
import dev.willtet.model.User;
import dev.willtet.model.vo.TopRankVO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
	
	public static boolean existeUsuarioCadastrado(String idDiscord) {
		boolean existe = false;

		String query = 	"SELECT * FROM tb_usuarios WHERE id = ? ";
		
		
		
		try (var conexao = ConnectionFactory.getConnection();
			 var stm = conexao.prepareStatement(query)) {

			
			stm.setString(1, idDiscord);
			
			var retorno = stm.executeQuery();
			
			if(retorno.next()) {
				existe = true;
			}

			return existe;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean cadastrarUsuarioLegends(String globalName, String name, String id) {
		String query = "INSERT INTO tb_usuarios (id, name, username, data_entrada)"
				+ " VALUES (?, ?, ?, ?)";

		// Bloco try-with-resources para garantir que a conexão e o PreparedStatement sejam fechados
		try (var conexao = ConnectionFactory.getConnection();
			 var stm = conexao.prepareStatement(query)) {


			stm.setString(1, id);
			stm.setString(2, globalName);
			stm.setString(3, name);
			stm.setDate(4, new java.sql.Date(System.currentTimeMillis())); // ou setTimestamp() se necessário

			stm.execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace(); // Corrigir para imprimir o stack trace
			return false;
		}
	}

	public static boolean postPontosByUsuario(String idMensagem, String idUsuario, String mensagem, int pontos) {

		String query = "INSERT INTO tb_publicacao (id_mensagem, id_usuario, valido, mensagem, data_entrada, ponto_peso)"
				+ " VALUES (?, ?, ?, ?, ?, ?)";

		try(var conexao = ConnectionFactory.getConnection();
			var stm = conexao.prepareStatement(query)) {


			stm.setString(1, idMensagem);
			stm.setString(2, idUsuario);
			stm.setBoolean(3, true);
			stm.setString(4, mensagem);
			stm.setDate(5,  new Date(System.currentTimeMillis()));
			stm.setInt(6,  pontos);

			stm.execute();

			return true;
		}catch (Exception e) {

			e.printStackTrace();
			return false;
		}
	}



	public static boolean postPontosByLegado(String idMensagem, String idUsuario, String mensagem, int pontos) {



		String query = "INSERT INTO tb_publicacao (id_mensagem, id_usuario, valido, mensagem, data_entrada, ponto_peso)"
				+ " VALUES (?, ?, ?, ?, ?, ?)";

		try(var conexao = ConnectionFactory.getConnection();
			var stm = conexao.prepareStatement(query)){


			stm.setString(1, idMensagem);
			stm.setString(2, idUsuario);
			stm.setBoolean(3, true);
			stm.setString(4, mensagem);
			stm.setDate(5,  new Date(System.currentTimeMillis()));
			stm.setInt(6,  pontos);

			stm.execute();

			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public static int findPontosByUsuario(String idUsuario) {

		var count = 0;


		String query = "SELECT SUM(ponto_peso) as Soma FROM tb_publicacao "
				+ " WHERE valido = 1"
				+ " AND id_usuario = ?";

		try(var conexao = ConnectionFactory.getConnection();
			var stm = conexao.prepareStatement(query)	) {

			stm.setString(1, idUsuario);

			try(var retorno = stm.executeQuery();){
				if(retorno.next()) {
					count = retorno.getInt("Soma");
				}
			}

			return count;
		}catch (Exception e) {

			e.printStackTrace();
			return 0;
		}
	}

	public static List<Publicacao> findPublicacaoByUsuario(String idUsuario) {

		List<Publicacao> publis = new ArrayList<>();

		String query = "SELECT tp.* FROM tb_publicacao tp"
				+ " WHERE tp.valido = 1"
				+ " AND tp.id_usuario = ?";

		try(var conexao = ConnectionFactory.getConnection();
			var stm = conexao.prepareStatement(query)	) {

			stm.setString(1, idUsuario);

			try(var retorno = stm.executeQuery();){
				while(retorno.next()) {
					Publicacao publi = new Publicacao(
							retorno.getLong("id"),
							retorno.getString("id_mensagem"),
							retorno.getString("id_usuario"),
							retorno.getBoolean("valido"),
							retorno.getString("mensagem"),
							retorno.getInt("ponto_peso"),
							retorno.getDate("data_entrada")
					);

					publis.add(publi);
				}
			}

			return publis;
		}catch (Exception e) {

			e.printStackTrace();
			return publis;
		}
	}

	public static boolean updatePontosByUsuario(String idMensagem, boolean valido) {
		String query = "UPDATE tb_publicacao "
				+ " SET valido = ? "
				+ " WHERE id_mensagem = ?";

		try(
				var conexao = ConnectionFactory.getConnection();
				var stm = conexao.prepareStatement(query)
				) {


			stm.setBoolean(1, valido);
			stm.setString(2, idMensagem);

			stm.execute();

			return true;
		}catch (Exception e) {

			e.printStackTrace();
			return false;
		}
	}


	public static boolean updatePontosByUsuarioEMessage(String idMensagem, String idUsuario, boolean valido) {
		String query = "UPDATE tb_publicacao "
				+ " SET valido = ? "
				+ " WHERE id_mensagem = ?" +
				"	AND id_usuario = ?";

		try(
				var conexao = ConnectionFactory.getConnection();
				var stm = conexao.prepareStatement(query);
				) {


			stm.setBoolean(1, valido);
			stm.setString(2, idMensagem);
			stm.setString(3, idUsuario);

			stm.execute();

			return true;
		}catch (Exception e) {

			e.printStackTrace();
			return false;
		}
	}


	public static String findRoleIdByPontos(int pontos) {
		var count = "";


		String query = "SELECT id_discord " +
				"FROM tb_roles " +
				"WHERE ? BETWEEN min_range AND max_range;";

		try (
				var conexao = ConnectionFactory.getConnection();
				var stm = conexao.prepareStatement(query);
				){

			stm.setInt(1, pontos);

			try(var retorno = stm.executeQuery();){
				if(retorno.next()) {
					count = retorno.getString("id_discord");
				}
			}
			return count;
		}catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public static Role findRoleByPontos(int pontos) {
		Role role = null;
		String query = "SELECT role.* " +
				"FROM tb_roles role " +
				"WHERE ? BETWEEN min_range AND max_range;";

		try(
				var conexao = ConnectionFactory.getConnection();
				var stm = conexao.prepareStatement(query);
				) {
			stm.setInt(1, pontos);

			try(var retorno = stm.executeQuery();){
				if (retorno.next()) {
					role = new Role(
							retorno.getInt("id"),
							retorno.getString("nome"),
							retorno.getInt("min_range"),
							retorno.getInt("max_range"),
							retorno.getString("id_discord")
					);
				}
			}

		}catch (Exception e) {

			e.printStackTrace();
			return null;
		}

		return role;
	}

	public static List<TopRankVO> findTopRanking() {
		List<TopRankVO> ranking = new ArrayList<>();

		String query = "SELECT " +
				" sum(pubri.ponto_peso) pontos, " +
				" tu.name nome" +
				" FROM tb_publicacao pubri " +
				" INNER JOIN tb_usuarios tu " +
				" ON tu.id = pubri.id_usuario " +
				" WHERE pubri.valido = 1 " +
				" GROUP BY id_usuario " +
				" ORDER BY pontos DESC " +
				" LIMIT 25";

		try(
				var conexao = ConnectionFactory.getConnection();
				var stm = conexao.prepareStatement(query);
				) {


			try(var retorno = stm.executeQuery()){
				while (retorno.next()) {

					TopRankVO vo = new TopRankVO(
							retorno.getInt("pontos"),
							retorno.getString("nome")
					);

					ranking.add(vo);
				}
			}
		}catch (Exception e) {

			e.printStackTrace();
			return null;
		}

		return ranking;
	}


	public static boolean isMessageRegisteredByIdUser(String idMensagem, String idUsuario) {

		boolean isRegistered = false;


		String query = "SELECT " +
				" 1 " +
				" FROM tb_publicacao pubri " +
				" WHERE pubri.id_mensagem = ? " +
				" AND pubri.id_usuario = ? ";

		try(
				var conexao = ConnectionFactory.getConnection();
				var stm = conexao.prepareStatement(query);
				) {

			stm.setString(1, idMensagem);
			stm.setString(2, idUsuario);

			try(var retorno = stm.executeQuery();){
				if (retorno.next()) {
					isRegistered = true;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

		return isRegistered;
	}

	public static boolean isValidoParaPontuarSemanal(LocalDate hoje, LocalDate domingo, String mensagem, String idUsuario) {
		boolean isValido = true;

		String query = "SELECT " +
				" 1 " +
				" FROM tb_publicacao pubri" +
				" WHERE pubri.data_entrada BETWEEN ? AND ? " +
				" AND pubri.mensagem = ? " +
				" AND pubri.id_usuario = ? ;";

		try(
				var conexao = ConnectionFactory.getConnection();
				var stm = conexao.prepareStatement(query);
				){


			stm.setObject(1, domingo);
			stm.setObject(2, hoje);
			stm.setString(3, mensagem);
			stm.setString(4, idUsuario);


			try(var retorno = stm.executeQuery();){
				if (retorno.next()) {
					isValido = false;
				}
			}

		}catch (Exception e) {
			e.printStackTrace();
		}

		return isValido;
	}
	public static User findUserById(String idUser) {
		User user = null;
		String query = "SELECT user.* " +
				"FROM tb_usuarios user " +
				"WHERE id = ?;";

		try(
				var conexao = ConnectionFactory.getConnection();
				var stm = conexao.prepareStatement(query);
		) {
			stm.setString(1, idUser);

			try(var retorno = stm.executeQuery();){
				if (retorno.next()) {
					user = new User(
							retorno.getString("id"),
							retorno.getString("name"),
							retorno.getString("username"),
							retorno.getDate("data_entrada")
					);
				}
			}

		}catch (Exception e) {

			e.printStackTrace();
			return null;
		}

		return user;
	}

}
