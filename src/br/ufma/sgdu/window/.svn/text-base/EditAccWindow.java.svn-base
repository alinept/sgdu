package br.ufma.sgdu.window;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufma.sgdu.database.DbAccManager;
import br.ufma.sgdu.entity.Administrator;

public class EditAccWindow extends SGDUSecureWindow {

	private static final long serialVersionUID = -7214291484171264635L;
	
	public Window winAdmin;
	protected ArrayList<Administrator> administrators;
	protected DbAccManager dbAccManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		if( !authenticated ){
			return;
		}
		if( !dbAccGate.getAdministrator().isSuperAdministrator() ){
			Executions.sendRedirect("/admin_main.zul");
			return;
		}
		winAdmin = window;
		
		try{
			dbAccManager = new DbAccManager();
			administrators = dbAccManager.getAdministrators();
		}
		catch (Exception e) {
			try{
				Messagebox.show(e.toString(), "ERRO", Messagebox.OK, Messagebox.ERROR);
			}
			catch (InterruptedException e1){}
			Executions.sendRedirect("/admin_main.zul");
		}
		
		binder.loadAll();
	}
	
	public ArrayList<Administrator> getAdministrators() {
		return administrators;
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
	 * Quando clica na figurinha da lupa (comentario)
	 * @param listitem
	 */
	public void showComment(Component listitem){
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		if( !dbAccGate.getAdministrator().isSuperAdministrator() ){
			Executions.sendRedirect("/admin_main.zul");
			return;
		}
		Listitem listItem = (Listitem)listitem;
		List<Listcell> listCells = listItem.getChildren();
		String login = listCells.get(1).getLabel();
		
		Administrator administrator = null;
		Iterator<Administrator> iterator = administrators.iterator();
		
		while( iterator.hasNext() ){
			administrator = iterator.next();
			if( administrator.getLogin().equals( login )) break;
		}
		
		try {
			Messagebox.show( isEmpty(administrator.getComment())?
					"Não há comentário para esta conta." :
					administrator.getComment(),
					"Comentário da conta: " + administrator.getLogin(),
					Messagebox.OK, Messagebox.INFORMATION);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Quando clica na figurinha do lapis (editar)
	 * @param listitem
	 */
	public void editAdministrator(Component listitem){
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		if( !dbAccGate.getAdministrator().isSuperAdministrator() ){
			Executions.sendRedirect("/admin_main.zul");
			return;
		}
		Listitem listItem = (Listitem)listitem;
		List<Listcell> listCells = listItem.getChildren();
		String login = listCells.get(1).getLabel();
		
		Administrator administrator = null;
		Iterator<Administrator> iterator = administrators.iterator();
		
		while( iterator.hasNext() ){
			administrator = iterator.next();
			if( administrator.getLogin().equals( login )) break;
		}
		
		Executions.getCurrent().getDesktop().getSession().setAttribute("admEdit", administrator);
		Executions.sendRedirect("/admin_editacc2.zul");
	}
	
	
	protected boolean isEmpty(String str){
    	if(str == null) return true;
    	for(int i=0; i<str.length(); i++){
    		if(Character.isLetterOrDigit( str.charAt(i) )){
    			return false;
    		}
    	}
    	return true;
    }

}
