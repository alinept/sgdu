package br.ufma.sgdu.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * <b>DatabaseConnection</b><br/>
 * Classe responsavel por realizar a conexao com o banco de dados.
 * As demais classes devem usar a conexao providenciada por essa classe.
 */
public class DatabaseConnection {
	
	private static String jdbcDriver = null;
	private static String jdbcURL = null;
	private static String user = null;
	private static String password = null;
	
	private static Connection connection = null;
	
	/**
	 * Retorna a conexao com o banco de dados usada pelo sistema.
	 */
	public static Connection get() throws Exception{
		
		if( connection != null ){
			return connection;
		}
		
		if( jdbcDriver == null || jdbcURL == null || user == null || password == null ){
			setDatabaseConfig();
		}
		
		try {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(jdbcURL, user, password);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return connection;
	}

	private static void setDatabaseConfig() throws Exception{
	    SAXBuilder saxBuilder = new SAXBuilder();
	    File dbconn = new File("sgdu-dbconn.xml");
		Document xml = null;
		
		if( !dbconn.isFile() ){
			throw new FileNotFoundException("Falta o arquivo de configuração\n" +
					dbconn.getAbsolutePath() );
		}
		xml = saxBuilder.build( dbconn );
						
		Element root = xml.getRootElement();
		Element jdbc_url = root.getChild("jdbc_url");
		Element jdbc_driver = root.getChild("jdbc_driver");
		Element db_user = root.getChild("user");
		Element db_password = root.getChild("password");
		
		jdbcDriver = jdbc_driver.getText();
		jdbcURL = jdbc_url.getText();
		user = db_user.getText();
		password = db_password.getText();
	}

}
