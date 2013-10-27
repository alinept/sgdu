package br.ufma.sgdu.window;

import java.util.Date;

import org.zkforge.fckez.FCKeditor;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import br.ufma.sgdu.database.DbDocManager;
import br.ufma.sgdu.entity.Document;
import br.ufma.sgdu.util.DocumentAnalizer;
import br.ufma.sgdu.util.FileSupportVerificator;

public class AdminTextEditorWindow extends SGDUSecureWindow {

	private static final long serialVersionUID = 895569698044991703L;

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	/**
	 * Botao "Voltar"
	 */
	public void goBack(){
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		Executions.sendRedirect("/admin_main.zul");
	}
	
	/**
	 * Botao "Enviar"
	 * @throws Exception 
	 */
	public void send() throws Exception{
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		((FCKeditor)getFellow("documentEditor")).invalidate();
		
		String title = ((Textbox)getFellow("titleTextBox")).getValue();
		String description = ((Textbox)getFellow("descriptionTextBox")).getValue();
		Date date = ((Datebox)getFellow("dateBox")).getValue();
		
		String htmlSource = 
			((FCKeditor)getFellow("documentEditor")).getValue();
		
		//checando dados
		
		//checando titulo
		if( isEmpty(title) ){
			try{
			Messagebox.show("O título está em branco. O preenchimeto desse campo é " +
					"obrigatório.", "Erro no título", Messagebox.OK, Messagebox.ERROR);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		//checando a descrição
		if( isEmpty(description) ){
			try{
				Messagebox.show("A descrição está em branco. O preenchimeto desse campo é " +
						"obrigatório.", "Erro na descrição", Messagebox.OK, Messagebox.ERROR);
			}
			catch (InterruptedException e) {
					e.printStackTrace();
			}
			return;
		}
		
		//checando data
		if( date == null ){
			try{
				Messagebox.show("A data de expedição está em branco. O preenchimeto desse campo é " +
						"obrigatório.", "Erro na data de expedição", Messagebox.OK, Messagebox.ERROR);
			}
			catch (InterruptedException e) {
					e.printStackTrace();
			}
			return;
		}
		
		//checando conteudo
		String content =		
			DocumentAnalizer.getText(htmlSource, FileSupportVerificator.HTML_TYPE);
		if( isEmpty(content) ){
			try{
				Messagebox.show("Não há nada digitado no campo de texto do documento. Não é " +
						"possível inserir um documento vazio.", "Erro no conteúdo",
						Messagebox.OK, Messagebox.ERROR);
			}
			catch (InterruptedException e) {
					e.printStackTrace();
			}
			return;
		}
		
		// beleza! se chegou ate aqui entao eh pq ta tudo certo
		
		DbDocManager dbDocManager = new DbDocManager();
		dbDocManager.insertDocument(title, description, date,
				new Date( System.currentTimeMillis() ), dbAccGate.getAdministrator().getLogin(),
				content, Document.HIPERTEXT_MARKUP_TYPE);
		
		dbDocManager.insertFile( title.replace(' ', '_') + ".html",
				("<html><head><title>" + title + "</title></head><body>" +
				 htmlSource + "</body></html>").getBytes());
		
		try{
		Messagebox.show("Documento inserido com sucesso!", "Sucesso na operação", 
				Messagebox.OK, Messagebox.INFORMATION);
		}
		catch(InterruptedException e){}
		Executions.sendRedirect("/admin_main.zul");
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
}
