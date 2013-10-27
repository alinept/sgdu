package br.ufma.sgdu.window;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import br.ufma.sgdu.entity.Administrator;

public class RemoveAccWindow extends EditAccWindow {

	private static final long serialVersionUID = -2270021819560229762L;
	
	/**
	 * Quando clica na figurinha "X" (excluir)
	 */
	@Override
	public void editAdministrator(Component listitem) {
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
			int op =
			Messagebox.show( "Deseja realmente excluir este administrador?\nLogin: " +
					administrator.getLogin() + "\nNome: " + administrator.getName() + 
					(isEmpty( administrator.getComment() )? "" : ("\nComentário desta conta: " +
							administrator.getComment()) ),
					"Confirmar exclusão de conta", Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION );
			if( op!=Messagebox.YES ){
				return;
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			dbAccManager.deleteAdministrator(administrator.getLogin(), administrator.getMd5Passwd());
		} catch (Exception e) {
			e.printStackTrace();
			try{
			Messagebox.show("Ocorreu um erro ao processar essa operação.\n" + e,
					"SQLException", Messagebox.OK, Messagebox.ERROR);
			}
			catch(InterruptedException e1){}
			return;
		}
		try{
		Messagebox.show("A conta foi excluída com sucesso!", "Exclusão de conta",
				Messagebox.OK, Messagebox.INFORMATION );
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		if( dbAccGate.getAdministrator().getLogin().equals( administrator.getLogin() )){
			Executions.getCurrent().getDesktop().getSession().removeAttribute("DbAccGate");
			Executions.sendRedirect("/admin.zul");
		}
		else{
			Executions.sendRedirect("/admin_removeacc.zul");
		}
	}
}
