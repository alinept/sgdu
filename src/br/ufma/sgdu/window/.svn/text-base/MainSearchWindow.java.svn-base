package br.ufma.sgdu.window;

import java.util.ArrayList;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Checkbox;

import br.ufma.sgdu.database.DbDocResearcher;
import br.ufma.sgdu.entity.Document;

/**
 * <b>MainSearchWindow</b><br/>
 * Classe responsavel por administrar a interface web de busca da pagina principal.
 * Entre suas principais funcoes estao a passagem dos dados referentes a busca para o sistema
 * e informar o usuario sobre algum erro no preenchimento dos campos de busca.
 */
public class MainSearchWindow extends Window{

	private static final long serialVersionUID = 973877474882582455L;
	public Window window;
	protected DataBinder binder;
	
	private String strSearch; // String da caixa de texto da pesquisa
	private boolean byTitle, byDescription, byContent; // Tipo da pesquisa (setado nos checkboxs)
    
    public void onCreate()
    {
    	window = (Window)getFellow("winSearch");
        binder =  new AnnotateDataBinder(window);
        binder.loadAll();
    }
    
    /**
     * Esse metodo e chamado ao pressionar o botao "Pesquisar Documento".
     * @throws Exception 
     */
    public void searchDocument() throws Exception
    {
    	Textbox busca;
    	Checkbox porTitulo, porDescricao, porConteudo;
    	// obtem os componentes da pagina
   		busca = (Textbox) getFellow("textboxSearch");
    	porTitulo = (Checkbox) getFellow("checkboxTitle");
    	porDescricao = (Checkbox) getFellow("checkboxDescription");
    	porConteudo = (Checkbox) getFellow("checkboxContent");
    	
    	// obtem os dados da pesquisa
    	strSearch = busca.getText();
    	byTitle = porTitulo.isChecked();
    	byDescription = porDescricao.isChecked();
    	byContent = porConteudo.isChecked();
    	
    	// testa se os valores setados sao validos
    	if(strSearch == null || isEmpty(strSearch) || !(byTitle || byDescription || byContent)){
    		try {
				Messagebox.show("Você deve digitar alguma coisa na caixa de texto e " +
						"marcar pelo menos uma das opções de busca antes de clicar em " +
						"Pesquisar Documento.",
						"Erro ao processar a pesquisa",
						Messagebox.OK,Messagebox.ERROR);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return;
    	}
    	
    	DbDocResearcher researcher = new DbDocResearcher();
    	long time1 = System.currentTimeMillis();
    	ArrayList<Document> docResearch =
    		researcher.search(strSearch, byTitle, byDescription, byContent);
    	
    	Executions.getCurrent().getDesktop().getSession().setAttribute("lastSearchParams",
    			new Search(strSearch, byTitle, byDescription, byContent,
    					System.currentTimeMillis() - time1, 0 ));
    	Executions.getCurrent().getDesktop().getSession().setAttribute("commomDocResearch",
    			docResearch);
    	Executions.sendRedirect("/search_results.zul");
    }
    
    private boolean isEmpty(String str){
    	for(int i=0; i<str.length(); i++){
    		if(Character.isLetterOrDigit( str.charAt(i) )){
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * Esse metodo e chamado quando o usuario clica no botao "?"
     */
    public void showHelp(){
    	try {
			Messagebox.show("Digite as palavras ou frases a serem pesquisadas na " +
					"caixa de texto.\nMarcando a opção \"Por Título\" estas serão " +
					"procuradas nos títulos dos documentos.\nMarcando \"Por Descrição\"," +
					" elas serão procuradas nas descrições dos documentos.\nFinalmente, " +
					"marcando \"Por Conteúdo\" elas serão procuradas em todo o documento; " +
					"note que essa última opção pode demorar mais para ser realizada, " +
					"pois demanda um maior processamento pelo sistema.\n" +
					"Você pode marcar ou desmarcar quaisquer opções, porém, pelo menos uma " +
					"opção precisa estar marcada.\n" +
					"Palavras devem ser digitadas uma após a outra separadas por espaço.\n" +
					"Frases devem ser digitadas como duas ou mais palavras entre aspas.\n",
					"Ajuda de Pesquisa",Messagebox.OK, Messagebox.INFORMATION);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    protected class Search{
    	
    	protected String strSearch; // String da caixa de texto da pesquisa
    	protected boolean byTitle, byDescription, byContent; // Tipo da pesquisa (setado nos checkboxs)
    	protected long searchTime;
    	protected int lastIndex;
    	
    	public Search(String strSearch, boolean byTitle, boolean byDescription,
				boolean byContent, long initDate, int lastIndex) {
			this.strSearch = strSearch;
			this.byTitle = byTitle;
			this.byDescription = byDescription;
			this.byContent = byContent;
			this.searchTime = initDate;
		}
    }
}
