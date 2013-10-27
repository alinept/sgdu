package br.ufma.sgdu.window;

import java.util.Date;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import br.ufma.sgdu.database.DbDocManager;
import br.ufma.sgdu.entity.Document;

public class EditDocWindow extends SGDUSecureWindow {

	private static final long serialVersionUID = 7175203351755063679L;
	Document document;
	
	@Override
	public void onCreate() {
		super.onCreate();
		if( !authenticated ){
			return;
		}
		document =
			(Document)Executions.getCurrent().getDesktop().getSession().getAttribute("editDocument");
		if(document == null ){
			return;
		}
		((Label)getFellow("idLabel")).setValue( document.getId() + "");
		((Textbox)getFellow("titleTextBox")).setValue(document.getTitle());
		((Textbox)getFellow("descriptionTextBox")).setValue(document.getDescription());
		((Datebox)getFellow("dateBox")).setValue(document.getExpeditionDate());
		binder.loadAll();
	}
	
	public void atualize() throws Exception{
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		
		String title = ((Textbox)getFellow("titleTextBox")).getValue();
		String description = ((Textbox)getFellow("descriptionTextBox")).getValue();
		Date date = ((Datebox)getFellow("dateBox")).getValue();
		
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
		
		// beleza! se chegou ate aqui entao eh pq ta tudo certo
		
		DbDocManager dbDocManager = new DbDocManager();
		dbDocManager.atualizeDocument(document.getId(), title, description, date);
		Messagebox.show("Documento atualizado com sucesso!", "Atualização de documento",
				Messagebox.OK, Messagebox.INFORMATION);
		Executions.getCurrent().getDesktop().getSession().removeAttribute("editDocument");
		Executions.sendRedirect("/admin_main.zul");
	}
	
	public void goBack(){
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		Executions.getCurrent().getDesktop().getSession().removeAttribute("editDocument");
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
