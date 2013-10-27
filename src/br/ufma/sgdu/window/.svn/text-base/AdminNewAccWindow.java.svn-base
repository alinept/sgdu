package br.ufma.sgdu.window;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

import br.ufma.sgdu.database.DbAccManager;
import br.ufma.sgdu.entity.Administrator;

public class AdminNewAccWindow extends SGDUSecureWindow {

	private static final long serialVersionUID = 9010126109547637785L;

	@Override
	public void onCreate() {
		super.onCreate();
		if( !authenticated ){
			return;
		}
		if( !dbAccGate.getAdministrator().isSuperAdministrator() ){
			Executions.sendRedirect("/admin_main.zul");
		}
	}
	
	/**
	 * Muda o atributo Type do TextBox da senha
	 */
	public void changePasswordVisibility(){
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		if( !dbAccGate.getAdministrator().isSuperAdministrator() ){
			Executions.sendRedirect("/admin_main.zul");
			return;
		}
		Textbox passwdTextBox = (Textbox)getFellow("passwdTextBox");
		if( passwdTextBox.getType().equals("text") ){
			passwdTextBox.setType("password");
		}
		else{
			passwdTextBox.setType("text");
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
		Executions.sendRedirect("/admin_main.zul");
	}
	
	/**
	 * Botao "Cadastrar Novo Administrador"
	 */
	public void register() throws Exception{
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		if( !dbAccGate.getAdministrator().isSuperAdministrator() ){
			Executions.sendRedirect("/admin_main.zul");
			return;
		}
		Textbox loginTextBox 	= (Textbox)getFellow("loginTextBox");
		Textbox nameTextBox 	= (Textbox)getFellow("nameTextBox");
		Textbox passwdTextBox	= (Textbox)getFellow("passwdTextBox");
		Textbox emailTextBox	= (Textbox)getFellow("emailTextBox");
		Textbox commentTextBox	= (Textbox)getFellow("commentTextBox");
		Radiogroup typeAccRadioGroup = (Radiogroup)getFellow("typeAccRadioGroup");
		
		// pega os dados
		String login = loginTextBox.getValue();
		String name = nameTextBox.getValue();
		String password = passwdTextBox.getValue();
		String email = emailTextBox.getValue();
		String comment = commentTextBox.getValue();
		int typeAux = typeAccRadioGroup.getSelectedIndex();
		short typeIndex = (short) (typeAux == 1? Administrator.SUPER_ADM_TYPE : typeAux == 0?
				Administrator.COMMOM_ADM_TYPE : -1);
		
		//checa se sao validos
		
		//checando login...
		if( !isValid(login) ){
			try {
				Messagebox.show("O login digitado é inválido! Use somente letras e/ou números " +
						"e ele deve ter pelo menos 4 caracteres.", "Login Inválido",
						Messagebox.OK, Messagebox.ERROR);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		DbAccManager dbAccManager = new DbAccManager();
		
		if( dbAccManager.loginExists(login) ){
			try {
				Messagebox.show("O login digitado já está sendo usado por outro administrador. " +
						"Escolha outro login.", "Login Inválido",
						Messagebox.OK, Messagebox.ERROR);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
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
		
		//checando e-mail
		if( email == null || email.isEmpty()){
			email = "";
		}
		
		//checando comentario
		if( comment == null || comment.isEmpty()){
			comment = "";
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
		if( typeIndex == Administrator.SUPER_ADM_TYPE ){
			try {
				int op = Messagebox.show("ATENCÃO! Você está prestes a cadastrar uma conta de " +
						"Super Administrador. Esses usuários tem autorização para modificar " +
						"todas as informações desse sistema, um usuário desse tipo pode, por " +
						"exemplo, excluir a sua conta, excluir todos os documentos do sistema, " +
						"modificar senhas, etc. Super Administradores devem ser pessoas de " +
						"confiança. \nVocê está certo que deseja cadastrar um Super Administrador " +
						"com os dados especificados nessa página?",
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
		
		dbAccManager.insertAdministrator(login, typeIndex, name, email, comment, password);
		
		try{
			Messagebox.show("Administrador cadastrado com sucesso!", "Cadastro bem-sucedido",
					Messagebox.OK, Messagebox.INFORMATION);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
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
