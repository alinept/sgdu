package br.ufma.sgdu.database;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import br.ufma.sgdu.entity.Document;
import br.ufma.sgdu.util.InputStream2ByteConverter;

/**
 * <b>DbDocManager</b>
 * Classe de controle responsável pela inserção e edição de documentos no banco.
 */
public class DbDocManager {

	private int idDoc;
	private int fileIndex;
	
	public DbDocManager() throws Exception{
		reset();
	}
	
	/**
	 * Insere todos os dados legíveis de um documento no banco de dados.<br/>
	 * <b>Esse deve ser, obrigatoriamente, o primeiro método a ser chamado para
	 * o procedimento de inserir um documento no banco de dados.</b>
	 * @param title VARCHAR
	 * @param description VARCHAR
	 * @param expeditionDate DATE
	 * @param uploadDate DATE
	 * @param owner VARCHAR
	 * @param content BLOB
	 * @param type SMALLINT
	 * @return o Documento com o ID inserido ou null se essa operacao nao foi realizada
	 * (isso acontece porque voce ja inseriu um documento antes com a mesma instancia
	 * dessa classe, use {@link #reset()} para poder fazer isso, ou porque aconteceu outro
	 * erro qualquer)
	 */
	public Document insertDocument(String title, String description, Date expeditionDate,
			Date uploadDate, String owner, String content, short type) throws Exception{
		if(idDoc != 0) return null;
		
		java.sql.Date expedition, upload;
		expedition = new java.sql.Date( expeditionDate.getTime() );
		upload = new java.sql.Date( uploadDate.getTime() );
		
		DatabaseConnection.get().setAutoCommit(false);
		
		PreparedStatement statement =
			DatabaseConnection.get().prepareStatement("INSERT INTO documento (titulo, " +
					"descricao, data_expedicao, data_upload, proprietario, conteudo, tipo) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?);");
		
		statement.setString(1, title);
		statement.setString(2, description);
		statement.setDate(3, expedition);
		statement.setDate(4, upload);
		statement.setString(5, owner);
		statement.setString(6, content);
		statement.setShort(7, type);
		
		statement.executeUpdate();
		DatabaseConnection.get().commit();
		statement.close();
		
		DatabaseConnection.get().setAutoCommit(true);
		
		Statement statement2 = DatabaseConnection.get().createStatement();
		
		ResultSet resultSet = statement2.executeQuery("SELECT MAX (id_doc) FROM documento;");
		if( resultSet.next() ){
			int id = resultSet.getInt( 1 );
			idDoc = id;
			Document doc =
				new Document(id, title, description, expeditionDate, uploadDate, owner, type);
			return doc;
		}
		else return null; // < isso nunca vai acontecer!
	}
	
	/**
	 * Adiciona um arquivo para o documento inserido por essa classe.<br/>
	 * <b>Esse metodo deve ser, obrigatoriamente, chamado apos {@link #insertDocument}
	 * para a conclusao do envio do documento para o sistema</b><br/>
	 * Obs.: Esse metodo deve ser chamado tantas vezes quanto for o numero de arquivos
	 * que compoem o documento, mas <b>ATENCAO:</b> se for mais de um arquivo, envie
	 * cada um na ordem correta!
	 * @param filename VARCHAR
	 * @param byteStream LONGBINARY
	 * @throws Exception se uma excecao ocorrer, voce deve remover o documento inserido!
	 * nao tem sentido deixar o documento la se o arquivo dele nao existe, mas relaxa num
	 * vai dar excecao nenhuma nao...
	 */
	public void insertFile(String filename, byte[] byteStream) throws Exception{
		if(idDoc == 0) throw new Exception("insertFile: document is not inserted");
		
		DatabaseConnection.get().setAutoCommit(false);
		
		PreparedStatement statement =
			DatabaseConnection.get().prepareStatement("INSERT INTO arquivo (id_doc, " +
					"nome_arquivo, dados, indice) VALUES (?, ?, ?, ?)");
		
		statement.setInt(1, idDoc);
		statement.setString(2, filename);
		statement.setBytes(3, byteStream);
		statement.setInt(4, fileIndex);
		
		statement.executeUpdate();
		DatabaseConnection.get().commit();
		statement.close();
		
		DatabaseConnection.get().setAutoCommit(true);
		
		fileIndex++;
	}
	
	/**
	 * Adiciona um arquivo para o documento inserido por essa classe.<br/>
	 * <b>Esse metodo deve ser, obrigatoriamente, chamado apos {@link #insertDocument}
	 * para a conclusao do envio do documento para o sistema</b><br/>
	 * Obs.: Esse metodo deve ser chamado tantas vezes quanto for o numero de arquivos
	 * que compoem o documento, mas <b>ATENCAO:</b> se for mais de um arquivo, envie
	 * cada um na ordem correta!
	 * @param filename VARCHAR
	 * @param inputStream LONGBINARY
	 * @throws Exception se uma excecao ocorrer, voce deve remover o documento inserido!
	 * nao tem sentido deixar o documento la se o arquivo dele nao existe, mas relaxa num
	 * vai dar excecao nenhuma nao...
	 */
	public void insertFile(String filename, InputStream inputStream) throws Exception{
		insertFile(filename, InputStream2ByteConverter.get(inputStream) );
	}
	
	/**
	 * Reseta a instancia dessa classe para inserir outro documento.
	 * Essa operacao e irreversivel.
	 */
	public void reset(){
		idDoc = 0;
		fileIndex = 1;
	}
	
	/**
	 * Retorna o ID do documento que esta classe upou para o banco.
	 * Zero se ainda nao upou, ou foi resetada.
	 */
	public int getDocumentID(){
		return idDoc;
	}
	
	/**
	 * Remove completamente um documento do banco de dados.
	 * @param id_doc INT
	 */
	public void removeDocument(int id_doc) throws Exception{
		PreparedStatement statement =
			DatabaseConnection.get().prepareStatement("DELETE FROM arquivo WHERE id_doc = ?;");
		statement.setInt(1, id_doc);
		statement.executeUpdate();
		
		statement =
			DatabaseConnection.get().prepareStatement("DELETE FROM documento WHERE id_doc = ?;");
		statement.setInt(1, id_doc);
		statement.executeUpdate();
	}
	
	/**
	 * Atualiza os campos: título, descrição e data de expedição do documento.
	 * @param idDoc
	 * @param newTitle
	 * @param newDesc
	 * @param newExpDate
	 * @throws Exception
	 */
	public void atualizeDocument(int idDoc, String newTitle, String newDesc, Date newExpDate)
		throws Exception{
		PreparedStatement statement =
			DatabaseConnection.get().prepareStatement("UPDATE documento SET titulo = ?, " +
					"descricao = ?, data_expedicao = ? WHERE id_doc = ?;");
		statement.setString(1, newTitle);
		statement.setString(2, newDesc);
		statement.setDate(3, new java.sql.Date( newExpDate.getTime() ));
		statement.setInt(4, idDoc);
		statement.executeUpdate();
	}
}
