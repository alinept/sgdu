package br.ufma.sgdu.window;

import java.util.StringTokenizer;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import br.ufma.sgdu.database.DbAccGate;
import br.ufma.sgdu.entity.Administrator;

/**
 * <b>SGDUSecurityWindow</b><br/>
 * Toda Window que necessita de autenticacao deve herdar dessa classe.<br/>
 * Esta classe ja contem os metodos do menu superior.
 */
public abstract class SGDUSecureWindow extends Window {

	private static final long serialVersionUID = 4012129070498089773L;
	
	protected boolean authenticated = false;
	protected DbAccGate dbAccGate = null;
	
	protected Window window;
	protected DataBinder binder;
	
	public void onCreate(){
		authenticate();
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		window = (Window)getFellow("winAdmin");
        binder =  new AnnotateDataBinder(window);
        binder.loadAll();
        
        String typeLabel = "";
		if( dbAccGate.getAdministrator().isSuperAdministrator() ){
			typeLabel = "Administrador: ";
		}
		else if( dbAccGate.getAdministrator().isCommomAdministrator() ){
			typeLabel = "Usuário: ";
		}
		((Label)getFellow("admTypeLabel")).setValue(typeLabel);
		((Label)getFellow("admNameLabel")).setValue(getUsernameLabel(dbAccGate.getAdministrator()) );
	}
	
	/* So uma funcaozinha pra pegar o nome do administrador formatada com tamanho
	 * maximo de 12 caracteres, que eh o tamanho do label. Essa funcao tambem pode
	 * abreviar os sobrenomes caso seja necessario. [usada em onCreate]
	*/
	private String getUsernameLabel(Administrator administrator) {
		String r = "", tokencur;
		StringTokenizer token = new StringTokenizer(administrator.getName()," ");
		
		while( r.length() < 12 && token.hasMoreTokens() ){
			tokencur = token.nextToken();
			if(tokencur.length() + r.length() > 12){
				if( r.equals("") ){
					r += tokencur.substring(0, 12);
					break;
				}
				else{ r += tokencur.charAt(0) + ". "; }
			}
			else{ r += tokencur + " "; }
		}
		if( r.charAt(r.length() -1) == ' ' ){
			r = r.substring(0, r.length() - 1);
		}
		return r;
	}
	
	/**
	 * <b>Esse metodo e responsavel pela seguranca do sistema!</b><br/>
	 * Ele autentica esse objeto para que a interface web do administrador possa ter suas
	 * funcionalidades. Enquanto a instancia dessa classe nao for autenticada por esse
	 * metodo ela nao provera acesso a nenhuma funcionalidade de administrador!
	 * Como essa classe e a ponte que liga o usuario da interface web do administrador ao
	 * sistema, um usuario nao cadastrado/logado que porventura conseguir acessar essa
	 * interface nao podera realizar nenhuma operacao, pois esse objeto estara "travado".
	 * Esse metodo so deve ser chamado quando o usuario realizar o login.
	 * @return true se houve sucesso nessa operacao, false caso contrario
	 */
	protected final boolean authenticate(){
		if(dbAccGate != null){
			return dbAccGate.connected();
		}
		
		Object gate = Executions.getCurrent().getDesktop().getSession().getAttribute("DbAccGate");
		authenticated = gate != null && gate instanceof DbAccGate && ((DbAccGate)gate).connected();
		if(authenticated){
			dbAccGate = (DbAccGate)gate;
		}
		else{
			dbAccGate = null;
		}
		return authenticated;
	}
	
	/**
	 * <b>Esse metodo e responsavel pela seguranca do sistema!</b><br/>
	 * Ele invalida esse objeto para a interface web do administrador.
	 * @see #authenticate()
	 */
	protected final void logout(){
		Executions.getCurrent().getDesktop().getSession().removeAttribute("DbAccGate");
		dbAccGate.disconnect();
		dbAccGate = null;
		authenticated = false;
		
		//removendo outros caches
		Executions.getCurrent().getDesktop().getSession().removeAttribute("documentsAdminWindow");
		Executions.getCurrent().getDesktop().getSession().removeAttribute("pdfFile");
		Executions.getCurrent().getDesktop().getSession().removeAttribute("htmlSource");
		Executions.getCurrent().getDesktop().getSession().removeAttribute("imageArray");
	}
	
	/**
	 * Botão "Sair"
	 */
	public void logOut(){
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		logout();
		Executions.sendRedirect("/admin.zul");
	}
	
	/**
	 * Menu: Cadastro<br/>
	 * Submenu: Alterar minha senha
	 */
	public void changePassword(){
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		Executions.sendRedirect("/admin_passwd.zul");
	}
	
	/**
	 * Menu: Cadastro<br/>
	 * Submenu: Cadastrar outro administrador 
	 */
	public void accCreate(){
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		if( dbAccGate.getAdministrator().isSuperAdministrator() ){
			Executions.sendRedirect("/admin_newacc.zul");
		}
		else{
			try {
				Messagebox.show("Desculpe, mas você não tem permissões para acessar essa " +
						"opção.", "Permissão Negada", Messagebox.OK, Messagebox.ERROR);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Menu: Cadastro<br/>
	 * Submenu: Editar cadastro de administrador
	 */
	public void accEdit(){
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		if( dbAccGate.getAdministrator().isSuperAdministrator() ){
			Executions.sendRedirect("/admin_editacc.zul");
		}
		else{
			try {
				Messagebox.show("Desculpe, mas você não tem permissões para acessar essa " +
						"opção.", "Permissão Negada", Messagebox.OK, Messagebox.ERROR);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Menu: Cadastro<br/>
	 * Submenu: Excluir cadastro de administrador
	 */
	public void accRemove(){
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		if( dbAccGate.getAdministrator().isSuperAdministrator() ){
			Executions.sendRedirect("/admin_removeacc.zul");
		}
		else{
			try {
				Messagebox.show("Desculpe, mas você não tem permissões para acessar essa " +
						"opção.", "Permissão Negada", Messagebox.OK, Messagebox.ERROR);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Menu: Cadastro<br/>
	 * Submenu: Excluir minha conta
	 */
	public void deleteAccount(){
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		Executions.sendRedirect("/admin_selfremove.zul");
	}
	
	/**
	 * Menu: Enviar novo documento<br/>
	 * Submenu: Fazer upload de arquivo
	 */
	public void upload(){
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		Executions.sendRedirect("/admin_upload.zul");
	}
	
	/**
	 * Menu: Enviar novo documento<br/>
	 * Submenu: Abrir editor de texto
	 */
	public void textEditor(){
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		Executions.sendRedirect("/admin_texteditor.zul");
	}
}
