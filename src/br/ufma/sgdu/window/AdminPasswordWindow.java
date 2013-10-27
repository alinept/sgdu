package br.ufma.sgdu.window;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import br.ufma.sgdu.database.DbAccManager;
import br.ufma.sgdu.entity.Administrator;
import br.ufma.sgdu.util.MD5;

public class AdminPasswordWindow extends SGDUSecureWindow {

	private static final long serialVersionUID = -8480836075600564884L;

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
	 * Botao "Alterar Senha"
	 */
	public void changePasswd() throws Exception{
		Administrator adm = dbAccGate.getAdministrator();
		Textbox actualPassword = (Textbox) getFellow("actualPasswdTextBox");
		Textbox newPassword1 = (Textbox) getFellow("newPasswdTextBox1");
		Textbox newPassword2 = (Textbox) getFellow("newPasswdTextBox2");
		
		boolean ok1 = adm.getMd5Passwd().equals( MD5.get(actualPassword.getValue()) );
		boolean ok2 = newPassword1.getValue().equals( newPassword2.getValue() );
		boolean ok3 = isValid(newPassword1.getValue()) && isValid(newPassword2.getValue());
		
		if( !ok1 ){
			try {
				Messagebox.show("Falha na autenticação.\n" +
						"Você não digitou a sua senha atual corretamente",
						"Erro na mudança de senha",
						Messagebox.OK,Messagebox.ERROR);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return;
		}
		if( !ok2 ){
			try {
				Messagebox.show("Falha ao processar a nova senha.\n" +
						"A nova senha e a confirmação da nova senha não são iguais.",
						"Erro na mudança de senha",
						Messagebox.OK,Messagebox.ERROR);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return;
		}
		if( !ok3 ){
			try {
				Messagebox.show("Falha ao processar a nova senha.\n" +
						"Você deve informar uma nova senha de pelo menos 4 " +
						"caracteres contendo somente letras e números.",
						"Erro na mudança de senha",
						Messagebox.OK,Messagebox.ERROR);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return;
		}
		
		DbAccManager dbAccManager = new DbAccManager();
		dbAccManager.updatePassword(adm.getLogin(), adm.getMd5Passwd(), newPassword1.getValue());
		adm.setMd5Passwd( MD5.get(newPassword1.getValue()) );
		
		try {
			Messagebox.show("Sua senha foi alterada com sucesso!",
					"Alteração de senha",
					Messagebox.OK,Messagebox.INFORMATION);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Executions.sendRedirect("/admin_main.zul");
	}
	
	private boolean isValid(String str){
		if(str == null ) return false;
		if(str.length() < 4) return false;
    	for(int i=0; i<str.length(); i++){
    		if( !Character.isLetterOrDigit( str.charAt(i) )){
    			return false;
    		}
    	}
    	return true;
    }

}
