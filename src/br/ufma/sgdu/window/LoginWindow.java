package br.ufma.sgdu.window;

import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import br.ufma.sgdu.database.DbAccGate;

/**
 * <b>LoginWindow</b><br/>
 * Classe que administra a interface web de login no sistema. Repassa os dados
 * de login e senha para o sistema e gerencia sua entrada.
 */
public class LoginWindow extends Window{

	private static final long serialVersionUID = 8069336839798314338L;
	public Window window;
	private DataBinder binder;
	
	private String username, password;
	
	public void onCreate()
    {
    	window = (Window)getFellow("winLogin");
        binder =  new AnnotateDataBinder(window);
        binder.loadAll();
    }
	
	/**
	 * Esse metodo e chamado ao clicar no botao "Entrar".
	 */
	public void realizarLogin() throws Exception{
		Textbox nomeUsuario, senha;
		
		// obtem os componentes da pagina
		try{
			nomeUsuario = (Textbox)getFellow("textboxUsername");
			senha = (Textbox)getFellow("textboxPassword");
		}
		catch (ComponentNotFoundException e) {
			try {
				Messagebox.show("Os campos de login não puderam ser acessados.\n" +
						"A página pode não ter sido carregada corretamente.\nAtualize " +
						"a página para resolver o problema.","Erro ao processar a pesquisa",
						Messagebox.OK,Messagebox.ERROR);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return;
		}
		
		// obtem os dados do login
		username = nomeUsuario.getText();
		password = senha.getText();
		
		// testa se os dados digitados sao validos
		if( isEmpty(username) || isEmpty(password) ){
			((Label)getFellow("statusLabel")).setValue("Preencha os campos login e senha primeiro");
			return;
		}
		
		// testa os dados no banco
		DbAccGate dbAccGate = new DbAccGate(username, password);
		
		// conectou
		if( dbAccGate.connected() ){
			((Label)getFellow("statusLabel")).setValue("");
			/* A LINHA ABAIXO E O PONTO CRITICO DA SEGURANCA DO SISTEMA. ELA SALVA NO CACHE O
			 * OBJETO DB ACC GATE QUE FOI ALOCADO SEGUNDO O BANCO DE DADOS. AS DEMAIS CLASSES
			 * O ACESSARAO PARA VALIDAR A SECAO */
			Executions.getCurrent().getDesktop().getSession().setAttribute("DbAccGate", dbAccGate);
			Executions.sendRedirect("/admin_main.zul");
		}
		// nao conectou
		else{
			((Label)getFellow("statusLabel")).setValue("Login ou senha incorretos");
		}
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
