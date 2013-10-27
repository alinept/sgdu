package br.ufma.sgdu.window;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

import br.ufma.sgdu.database.DbAccManager;
import br.ufma.sgdu.entity.Administrator;
import br.ufma.sgdu.util.MD5;

public class EditAccWindow2 extends SGDUSecureWindow {

	private static final long serialVersionUID = -3141515418153250289L;
	private Administrator administrator = null;
	
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
		Object adm = Executions.getCurrent().getDesktop().getSession().getAttribute("admEdit");
		if( adm!= null && adm instanceof Administrator){
			administrator = (Administrator)adm;
		}
		else{
			Executions.sendRedirect("/admin_main.zul");
			return;
		}
		((Label)getFellow("loginLabel")).setValue( administrator.getLogin() );
		((Textbox)getFellow("nameTextBox")).setValue( administrator.getName() );
		((Textbox)getFellow("emailTextBox")).setValue( administrator.getEmail() );
		((Textbox)getFellow("commentTextBox")).setValue( administrator.getComment() );
		if( administrator.isCommomAdministrator() ){
			((Radiogroup)getFellow("typeAccRadioGroup")).getItemAtIndex(0).setChecked(true);
		}
		else if( administrator.isSuperAdministrator() ){
			((Radiogroup)getFellow("typeAccRadioGroup")).getItemAtIndex(1).setChecked(true);
		}
	}
	
	/**
	 * Botao "Voltar"
	 */
	public void goBack(){
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		Executions.getCurrent().getDesktop().getSession().removeAttribute("admEdit");
		Executions.sendRedirect("/admin_editacc.zul");
	}
	
	@Override
	public void logOut() {
		Executions.getCurrent().getDesktop().getSession().removeAttribute("admEdit");
		super.logOut();
	}
	
	@Override
	public void changePassword() {
		Executions.getCurrent().getDesktop().getSession().removeAttribute("admEdit");
		super.changePassword();
	}
	
	@Override
	public void accCreate() {
		Executions.getCurrent().getDesktop().getSession().removeAttribute("admEdit");
		super.accCreate();
	}
	
	@Override
	public void accEdit() {
		Executions.getCurrent().getDesktop().getSession().removeAttribute("admEdit");
		super.accEdit();
	}
	
	@Override
	public void accRemove() {
		Executions.getCurrent().getDesktop().getSession().removeAttribute("admEdit");
		super.accRemove();
	}
	
	@Override
	public void deleteAccount() {
		Executions.getCurrent().getDesktop().getSession().removeAttribute("admEdit");
		super.deleteAccount();
	}
	
	@Override
	public void upload() {
		Executions.getCurrent().getDesktop().getSession().removeAttribute("admEdit");
		super.upload();
	}
	
	@Override
	public void textEditor() {
		Executions.getCurrent().getDesktop().getSession().removeAttribute("admEdit");
		super.textEditor();
	}
	
	/**
	 * Botao "Atualizar Cadastro"
	 * @throws Exception 
	 */
	public void atualize() throws Exception{
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		if( !dbAccGate.getAdministrator().isSuperAdministrator() || administrator == null ){
			Executions.sendRedirect("/admin_main.zul");
			return;
		}
		
		Textbox nameTextBox 	= (Textbox)getFellow("nameTextBox");
		Textbox passwdTextBox	= (Textbox)getFellow("passwdTextBox");
		Textbox passwdTextBox2 	= (Textbox)getFellow("passwdTextBox2");
		Textbox emailTextBox	= (Textbox)getFellow("emailTextBox");
		Textbox commentTextBox	= (Textbox)getFellow("commentTextBox");
		Radiogroup typeAccRadioGroup = (Radiogroup)getFellow("typeAccRadioGroup");
		
		// pega os dados
		String name = nameTextBox.getValue();
		String password = passwdTextBox.getValue();
		String password2 = passwdTextBox2.getValue();
		boolean passwordChanged;
		String email = emailTextBox.getValue();
		String comment = commentTextBox.getValue();
		int typeAux = typeAccRadioGroup.getSelectedIndex();
		short typeIndex = (short) (typeAux == 1? Administrator.SUPER_ADM_TYPE : typeAux == 0?
				Administrator.COMMOM_ADM_TYPE : -1);
		
		//checa se sao validos
		
		//checando nome...
		if( isEmpty(name) ){
			try {
				Messagebox.show("O nome do administrador precisa ser preenchido.",
						"Nome está em branco", Messagebox.OK, Messagebox.ERROR);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		//checando senha...
		if( (password == null || password.isEmpty()) &&
				(password2 == null || password2.isEmpty())){
			passwordChanged = false;
		}
		else{
			passwordChanged = true;
			if( !isValid(password) ){
				try {
					Messagebox.show("A senha digitada é inválida! Utilize somente letras e/ou " +
							"números e ela deve ter pelo menos 4 caracteres.",
							"Senha inválida", Messagebox.OK, Messagebox.ERROR);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return;
			}
			if( !password.equals(password2) ){
				try {
					Messagebox.show("A senha digitada e a confirmação dela estão diferentes. " +
							"Digite a mesma senha nesses dois campos.",
							"Confirmação da senha inválida", Messagebox.OK, Messagebox.ERROR);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return;
			}
		}
		
		//checando e-mail
		if( email == null || email.isEmpty()){
			email = " ";
		}
		
		//checando comentario
		if( comment == null || comment.isEmpty()){
			comment = " ";
		}
		
		//checando tipo da conta
		if( typeIndex == -1 ){
			try {
				Messagebox.show("Ocorreu um erro. O tipo da conta (Administrador Comum ou " +
						"Super Administrador) parece não estar selecionado.",
						"Erro no tipo da conta", Messagebox.OK, Messagebox.ERROR);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		if( typeIndex == Administrator.SUPER_ADM_TYPE && administrator.isCommomAdministrator() ){
			try {
				int op = Messagebox.show("ATENCÃO! Você está prestes a alterar uma conta para o " +
						"tipo Super Administrador. Esses usuários tem autorização para modificar " +
						"todas as informações desse sistema, um usuário desse tipo pode, por " +
						"exemplo, excluir a sua conta, excluir todos os documentos do sistema, " +
						"modificar senhas, etc. Super Administradores devem ser pessoas de " +
						"confiança. \nVocê está certo que deseja alterar essa conta para o tipo " +
						"Super Administrador com os dados especificados nessa página?",
						"Aviso para conta de Super Administrador",
						Messagebox.YES | Messagebox.NO, Messagebox.EXCLAMATION);
				if( op != Messagebox.YES ){
					return;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//beleza! se a execucao chegou ate aqui entao eh pq ta tudo certo
		
		//altera os dados no banco
		DbAccManager dbAccManager = new DbAccManager();
		if( passwordChanged ){
			dbAccManager.updateAdministrator(administrator.getLogin(), administrator.getMd5Passwd(),
					typeIndex, name, email, comment, password);
		}
		else{
			dbAccManager.updateAdministrator(administrator.getLogin(), administrator.getMd5Passwd(),
					typeIndex, name, email, comment);
		}
		
		//e se eu alterar os dados de mim mesmo? atualiza o cache
		if( dbAccGate.getAdministrator().getLogin().equals( administrator.getLogin() ) ){
			dbAccGate.getAdministrator().setType(typeIndex);
			dbAccGate.getAdministrator().setName(name);
			dbAccGate.getAdministrator().setEmail(email);
			dbAccGate.getAdministrator().setComment(comment);
			if( passwordChanged ){
				dbAccGate.getAdministrator().setMd5Passwd( MD5.get(password) );
			}
		}
		
		//pronto! tudo feito
		try{
			Messagebox.show("Cadastro de administrador alterado com sucesso!",
					"Cadastro bem-sucedido",
					Messagebox.OK, Messagebox.INFORMATION);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		goBack();
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
	
	private boolean isValid(String str){
		if(str == null) return false;
		if(str.length() < 4) return false;
    	for(int i=0; i<str.length(); i++){
    		if( !Character.isLetterOrDigit( str.charAt(i) )){
    			return false;
    		}
    	}
    	return true;
    }
}
