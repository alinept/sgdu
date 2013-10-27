package br.ufma.sgdu.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;

import br.ufma.sgdu.entity.Administrator;
import br.ufma.sgdu.util.MD5;

/**
 * <b>DbAccManager</b><br/>
 * Classe de controle que gerencia contas do banco de dados.
 * Algumas de suas funcoes sao: cadastro, remocao e edicao de contas.
 */
public class DbAccManager {
	
	/**
	 * Checa se um determinado login ja esta cadastrado no banco.
	 * @param login VARCHAR que contem o login a ser pesquisado no banco
	 * @throws Exception 
	 * @throws SQLException 
	 */
	public boolean loginExists(String login) throws Exception{
		PreparedStatement statement = DatabaseConnection.get().prepareStatement("SELECT * " +
				"FROM administrador WHERE login = ?;");
		statement.setString(1, login);
		
		ResultSet loginResultSet;
		try {
			loginResultSet = statement.executeQuery();
			return loginResultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Insere um novo administrador no banco.
	 * @param login VARCHAR
	 * @param type SMALLINT
	 * @param name VARCHAR
	 * @param email VARCHAR
	 * @param comment VARCHAR
	 * @param password VARCHAR (nao-criptografada)
	 * @throws Exception 
	 */
	public void insertAdministrator(String login, short type, String name, String email,
			String comment, String password) throws Exception{
		PreparedStatement pstatement =
			DatabaseConnection.get().prepareStatement("INSERT INTO administrador (login, tipo, " +
					"nome, email, comentario, senha_md5) VALUES (?, ?, ?, ?, ?, ?);");
		
		pstatement.setString(1, login);
		pstatement.setShort(2, type);
		pstatement.setString(3, name);
		pstatement.setString(4, email);
		pstatement.setString(5, comment);
		pstatement.setString(6, MD5.get(password));
		
		pstatement.executeUpdate();
		pstatement.close();
	}
	
	/**
	 * Altera a senha de um administrador.
	 * @param login VARCHAR
	 * @param md5Password VARCHAR senha atual (criptografada)
	 * @param newPassword VARCHAR nova senha (nao-criptografada)
	 * @throws Exception 
	 */
	public void updatePassword(String login, String md5Password, String newPassword)
			throws Exception{
		PreparedStatement pstatement =
			DatabaseConnection.get().prepareStatement("UPDATE administrador SET senha_md5 = ? " +
					"WHERE login = ? AND senha_md5 = ?;");
		
		pstatement.setString(1, MD5.get(newPassword));
		pstatement.setString(2, login);
		pstatement.setString(3, md5Password);
		
		pstatement.executeUpdate();
		pstatement.close();
	}
	
	/**
	 * Altera os dados de um administrador.
	 * @param login VARCHAR - login atual (nao sera alterado)
	 * @param md5Password VARCHAR - senha atual (criptografada)
	 * @param newType SMALLINT
	 * @param newName VARCHAR
	 * @param newEmail VARCHAR
	 * @param newComment VARCHAR
	 * @param newPassword VARCHAR - nova senha (nao-criptografada)
	 * @throws Exception
	 */
	public void updateAdministrator(String login, String md5Password, short newType, String newName,
			String newEmail, String newComment, String newPassword) throws Exception{
		PreparedStatement pstatement =
			DatabaseConnection.get().prepareStatement("UPDATE administrador SET tipo = ?, " +
					"nome = ?, email = ?, comentario = ?, senha_md5 = ? WHERE login = ? " +
					"AND senha_md5 = ?;");
		
		pstatement.setShort(1, newType);
		pstatement.setString(2, newName);
		pstatement.setString(3, newEmail);
		pstatement.setString(4, newComment);
		pstatement.setString(5, MD5.get(newPassword));
		pstatement.setString(6, login);
		pstatement.setString(7, md5Password);
		
		pstatement.executeUpdate();
		pstatement.close();
	}
	
	/**
	 * Altera os dados de um administrador.
	 * @param login VARCHAR - login atual (nao sera alterado)
	 * @param md5Password VARCHAR - senha atual (criptografada)
	 * @param newType SMALLINT
	 * @param newName VARCHAR
	 * @param newEmail VARCHAR
	 * @param newComment VARCHAR
	 * @throws Exception
	 */
	public void updateAdministrator(String login, String md5Password, short newType, String newName,
			String newEmail, String newComment) throws Exception{
		PreparedStatement pstatement =
			DatabaseConnection.get().prepareStatement("UPDATE administrador SET tipo = ?, " +
					"nome = ?, email = ?, comentario = ? WHERE login = ? AND senha_md5 = ?;");
		
		pstatement.setShort(1, newType);
		pstatement.setString(2, newName);
		pstatement.setString(3, newEmail);
		pstatement.setString(4, newComment);
		pstatement.setString(5, login);
		pstatement.setString(6, md5Password);
		
		pstatement.executeUpdate();
		pstatement.close();
	}
	
	/**
	 * Remove um administrador do banco.
	 * @param login VARCHAR
	 * @param md5Password VARCHAR (criptografada)
	 * @throws Exception
	 */
	public void deleteAdministrator(String login, String md5Password) throws Exception{
		PreparedStatement pstatement =
			DatabaseConnection.get().prepareStatement("DELETE FROM administrador WHERE " +
					"login = ? AND senha_md5 = ?;");
		pstatement.setString(1, login);
		pstatement.setString(2, md5Password);

		pstatement.executeUpdate();
		pstatement.close();
	}
	
	/**
	 * Retorna todos os administradores cadastrados no banco.
	 */
	public ArrayList<Administrator> getAdministrators() throws Exception{
		ArrayList<Administrator> arrayList = new ArrayList<Administrator>();
		ResultSet admResultSet;
		PreparedStatement statement = DatabaseConnection.get().prepareStatement("SELECT * " +
				"FROM administrador ORDER BY nome ASC;");
		admResultSet = statement.executeQuery();
		
		while( admResultSet.next() ){
			String login = admResultSet.getString("login");
			short type = admResultSet.getShort("tipo");
			String name = admResultSet.getString("nome");
			String email = admResultSet.getString("email");
			String comment = admResultSet.getString("comentario");
			String md5Passwd = admResultSet.getString("senha_md5");
			
			arrayList.add( new Administrator(login, type, name, email, comment, md5Passwd) );
		}
		
		return arrayList;
	}
}
