package br.ufma.sgdu.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.ufma.sgdu.entity.Administrator;
import br.ufma.sgdu.util.MD5;

/**
 * <b>DbAccGate</b><br/>
 * Classe de controle que se conecta ao banco de dados para realizar login e logout de
 * administradores e extrair os dados referente a eles do banco para o sistema.
 */
public class DbAccGate {
	
	private String user = null;
	private String passwd = null;
	
	private boolean connected = false;
	private Connection connection = null;
	
	private Administrator adm = null;
	
	public DbAccGate(String user, String passwd) throws Exception{
		this.user = user;
		this.passwd = passwd;
		connection = DatabaseConnection.get();
		connected = getAdmfromDatabase();
	}
	
	private boolean getAdmfromDatabase() throws SQLException{
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM " +
				"administrador WHERE login = ? AND senha_md5 = ?;");
		statement.setString(1, user);
		statement.setString(2, MD5.get(passwd));
		
		ResultSet resultSet = statement.executeQuery();
		
		if ( !resultSet.next() ){
			return false;
		}
		
		String login = resultSet.getString("login");
		short type = resultSet.getShort("tipo");
		String name = resultSet.getString("nome");
		String email = resultSet.getString("email");
		String comment = resultSet.getString("comentario");
		String md5Passwd = resultSet.getString("senha_md5");
		
		adm = new Administrator(login, type, name, email, comment, md5Passwd);
		return true;
	}

	public boolean connected(){
		return connected;
	}
	
	public void disconnect(){
		connected = false;
	}
	
	public Administrator getAdministrator(){
		return adm;
	}
}
