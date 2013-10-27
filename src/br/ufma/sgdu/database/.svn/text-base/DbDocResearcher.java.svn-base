package br.ufma.sgdu.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;

import br.ufma.sgdu.entity.Administrator;
import br.ufma.sgdu.entity.Document;
import br.ufma.sgdu.entity.DocumentFile;

public class DbDocResearcher {
	
	/**
	 * Pesquisa no banco a string de busca nos campos titulo e descricao, mas somente
	 * nos documentos em que esse administrador tem acesso.
	 * @param adm
	 * @param search
	 * @return ArrayList com os documentos encontrados
	 * @throws Exception
	 */
	public ArrayList<Document> adminResearch(Administrator adm,	String search)
			throws Exception {
		ArrayList<Document> r = new ArrayList<Document>();
		PreparedStatement statement;
		ResultSet docResultSet;
		
		// PRE-MONTANDO A STRING SQL
		String sql = "SELECT id_doc, titulo, descricao, data_expedicao, data_upload, " +
				"proprietario, tipo FROM documento ";
		ArrayList<String> searchWords = getStrings(search);
		
		if( adm.isCommomAdministrator() || !searchWords.isEmpty()){
			sql += "WHERE ";
		}
		
		if( adm.isCommomAdministrator() ){
			sql += "proprietario = ? ";
			if( !searchWords.isEmpty() ) sql += "AND ";
		}
		
		for(int i=0; i<searchWords.size(); i++){
			sql += "(titulo ~* ? OR descricao ~* ?) ";
			if(i < searchWords.size() - 1){
				sql += "AND ";
			}
		}
		sql += "ORDER BY data_upload DESC LIMIT 50;";
		// STRING SQL PRE-MONTADA
		
		statement = DatabaseConnection.get().prepareStatement(sql);
		int parameterIndex = 1;
		
		// TERMINANDO DE MONTAR A STRING SQL DENTRO DO STATEMENT
		if( adm.isCommomAdministrator() ){
			statement.setString(parameterIndex, adm.getLogin());
			parameterIndex++;
		}
		ListIterator<String> iterator = searchWords.listIterator();
		while( iterator.hasNext() ){
			String current = iterator.next();
			statement.setString(parameterIndex, current);
			parameterIndex++;
			statement.setString(parameterIndex, current);
			parameterIndex++;
		}
		// COMANDO SQL PRONTO
		
		// fogo!
		docResultSet = statement.executeQuery();
		
		while( docResultSet.next() ){
			int id = docResultSet.getInt(1);
			String title = docResultSet.getString(2);
			String description = docResultSet.getString(3);
			Date expeditionDate = docResultSet.getDate(4);
			Date uploadDate = docResultSet.getDate(5);
			String owner = docResultSet.getString(6);
			short type = docResultSet.getShort(7);
			
			r.add( new Document(id, title, description, expeditionDate, uploadDate,
					owner, type));
		}
		return r;
	}
	
	/**
	 * Pesquisa no banco a string de busca nos campos selecionados.
	 * @param search
	 * @return ArrayList com os documentos encontrados, nao retorna os campos proprietario
	 * nem data_upload
	 * @throws Exception
	 */
	public ArrayList<Document> search(String search, boolean byTitle, boolean byDescription,
			boolean byContent) throws Exception{
		ArrayList<Document> r = new ArrayList<Document>();
		PreparedStatement statement;
		ResultSet docResultSet;
		
		ArrayList<String> searchWords = getStrings(search);
		if( searchWords.isEmpty() ) return r;
		
		// PRE-MONTANDO A STRING SQL
		String sql = "SELECT id_doc, titulo, descricao, data_expedicao, tipo FROM documento WHERE ";
		
		for(int i=0; i<searchWords.size(); i++){
			sql += "(";
			if( byTitle ){
				sql += "titulo ~* ?";
				if( byDescription || byContent ) sql += " OR ";
			}
			if( byDescription ){
				sql += "descricao ~* ?";
				if( byContent ) sql += " OR ";
			}
			if( byContent )	sql += "conteudo ~* ?";
			
			sql += ") ";
			if(i < searchWords.size() - 1){
				sql += "AND ";
			}
		}
		sql += "ORDER BY data_expedicao DESC LIMIT 50;";
		// STRING SQL PRE-MONTADA
		
		statement = DatabaseConnection.get().prepareStatement(sql);
		int parameterIndex = 1;
		
		// TERMINANDO DE MONTAR A STRING SQL DENTRO DO STATEMENT
		ListIterator<String> iterator = searchWords.listIterator();
		while( iterator.hasNext() ){
			String current = iterator.next();
			if( byTitle ){
				statement.setString(parameterIndex, current);
				parameterIndex++;
			}
			if( byDescription ){
				statement.setString(parameterIndex, current);
				parameterIndex++;
			}
			if( byContent ){
				statement.setString(parameterIndex, current);
				parameterIndex++;
			}
		}
		// COMANDO SQL PRONTO
		
		// fogo!
		docResultSet = statement.executeQuery();
		
		while( docResultSet.next() ){
			int id = docResultSet.getInt(1);
			String title = docResultSet.getString(2);
			String description = docResultSet.getString(3);
			Date expeditionDate = docResultSet.getDate(4);
			short type = docResultSet.getShort(5);
			
			r.add( new Document(id, title, description, expeditionDate, null, null, type));
		}
		
		return r;
	}
	
	/**
	 * Retorna todos os arquivos associados a esse documento.
	 * @return
	 */
	public ArrayList<DocumentFile> getDocumentFiles(int idDoc) throws Exception{
		ArrayList<DocumentFile> r = new ArrayList<DocumentFile>();
		
		PreparedStatement statement = DatabaseConnection.get().prepareStatement("SELECT " +
				"nome_arquivo, dados FROM arquivo WHERE id_doc = ? ORDER BY indice ASC;");
		statement.setInt(1, idDoc);
		
		ResultSet resultSet = statement.executeQuery();
		while( resultSet.next() ){
			String filename = resultSet.getString(1);
			byte [] byteStream = resultSet.getBytes(2);
			
			r.add( new DocumentFile(filename, byteStream) );
		}
		return r;
	}
	
	/**
	 * Retorna o primeiro nome de arquivo do documento. Util para determinar a extensao
	 * do arquivo daquele documento.
	 * @param idDoc INT
	 * @return
	 * @throws Exception
	 */
	public String getFirsFilename(int idDoc) throws Exception{
		PreparedStatement statement = DatabaseConnection.get().prepareStatement("SELECT " +
				"nome_arquivo FROM arquivo WHERE id_doc = ? LIMIT 1");
		statement.setInt(1, idDoc);
		ResultSet resultSet = statement.executeQuery();
		if( resultSet.next() ){
			return resultSet.getString(1);
		}
		else return "";
	}
	
	private ArrayList<String> getStrings(String search){
		ArrayList<String> r = new ArrayList<String>();
		
		if( isEmpty(search) ) return r;
		String cur = "";
		for(int i=0; i<search.length(); i++){
			if( search.charAt(i) == ' ' ){
				if( cur.isEmpty() )	continue;
				else{
					r.add(cur);
					cur = new String("");
				}
			}
			else if( search.charAt(i) == '"' ){
				for(i++; i<search.length(); i++){
					if( search.charAt(i) == '"' ){
						if( !cur.isEmpty() ){
							r.add(cur);
							cur = new String("");
						}
						break;
					}
					else cur += search.charAt(i);
				}
			}
			else cur += search.charAt(i);
		}
		if( !cur.isEmpty() ) r.add(cur);
		return r;
	}
	
	private boolean isEmpty(String str){
		if(str == null) return true;
    	for(int i=0; i<str.length(); i++){
    		if(Character.isLetterOrDigit( str.charAt(i) )){
    			return false;
    		}
    	}
    	return true;
    }
	
	public String testeResearch(int idDoc) throws Exception{
		PreparedStatement statement = DatabaseConnection.get().prepareStatement("SELECT " +
				"conteudo FROM documento WHERE id_doc = ?;");
		statement.setInt(1, idDoc);
		
		ResultSet resultSet = statement.executeQuery();
		if( resultSet.next() ){
			return resultSet.getString(1);
		}
		else return "Não achou nada no banco de dados!";
	}
}
