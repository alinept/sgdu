package br.ufma.sgdu.window.teste;

import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import br.ufma.sgdu.database.DbDocResearcher;

public class Teste extends Window {

	private Window window;
	private DataBinder binder;
	
	public void onCreate(){
		window = (Window)getFellow("teste");
		binder =  new AnnotateDataBinder(window);
		binder.loadAll();
	}
	
	public void letsGo() throws Exception{
		String str = ((Textbox)getFellow("idTextBox")).getValue();
		int id;
		try{
			id = Integer.parseInt(str);
		}
		catch(NumberFormatException e){
			Messagebox.show(e.getMessage(), "SAI DAEW LOKO!", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		DbDocResearcher r = new DbDocResearcher();
		
		((Textbox)getFellow("contentTextBox")).setValue( r.testeResearch(id) );
	}
}
