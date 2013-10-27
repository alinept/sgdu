package br.ufma.sgdu.window;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import br.ufma.sgdu.database.DbDocManager;
import br.ufma.sgdu.database.DbDocResearcher;
import br.ufma.sgdu.entity.Document;
import br.ufma.sgdu.entity.DocumentFile;
import br.ufma.sgdu.util.FileSupportVerificator;

/**
 * <b>AdminWindow</b><br/>
 * Classe que gerencia a interface web da pagina inicial do administrador, onde o mesmo tem
 * acesso as seus privilegios. Entre suas principais funcoes estao:
 * redirecionar o administrador para a interface correspondente a opcao desejada e requisitar
 * o logout do usuario para o sistema.
 */
public class AdminWindow extends SGDUSecureWindow{

	private static final long serialVersionUID = 3055319891648164746L;
	
	public Window winAdmin;
	private ArrayList<Document> documents;
	private DbDocResearcher researcher;
	
	@Override
	public void onCreate() {
		super.onCreate();
		if( !authenticated ){
			return;
		}
		winAdmin = window;
		
		try{
			researcher = new DbDocResearcher();
		}
		catch (Exception e) {
			try{
				Messagebox.show(e.toString(), "ERRO", Messagebox.OK, Messagebox.ERROR);
			}
			catch (InterruptedException e1){}
			Executions.sendRedirect("/admin_main.zul");
		}
		ArrayList<Document> docs = (ArrayList<Document>)
		Executions.getCurrent().getDesktop().getSession().getAttribute("documentsAdminWindow");
		if(docs == null) documents = new ArrayList<Document>();
		else documents = docs;
		binder.loadAll();
	}
	
	public ArrayList<Document> getDocuments() {
		return documents;
	}

	/**
	 * Botão "Pesquisar"
	 */
	public void adminSearch() throws Exception{
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		String search = ((Textbox)getFellow("searchDocTextBox")).getValue();
		documents = researcher.adminResearch( dbAccGate.getAdministrator(), search);
		Executions.getCurrent().getDesktop().getSession().setAttribute("documentsAdminWindow",
				documents);
		binder.loadAll();
	}
	
	/**
	 * Botão "Mostrar Todos"
	 */
	public void adminShowAll() throws Exception{
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		documents = researcher.adminResearch( dbAccGate.getAdministrator(), "");
		Executions.getCurrent().getDesktop().getSession().setAttribute("documentsAdminWindow",
				documents);
		binder.loadAll();
	}
	
	/**
	 * Botão "?"
	 */
	public void adminHelp(){
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		try {
			Messagebox.show("Digite as palavras a serem pesquisadas na caixa de texto. " +
					"Elas serão procuradas no título e descrição dos documentos que você " +
					"tem permissão para alterar e então serão exibidos na tabela abaixo.\n" +
					"Clicando em \"Listar Todos\", todos os documentos serão exibidos até o " +
					"limite de 50 resultados.\nVocê pode usar \" (aspas) para procurar por " +
					"uma frase inteira.",
					"Ajuda de Pesquisa",Messagebox.OK, Messagebox.INFORMATION);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Quando clica na figurinha da opcao vizualizar
	 * @throws Exception 
	 */
	public void view(Component listitem) throws Exception{
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		Listitem listItem = (Listitem)listitem;
		List<Listcell> listCells = listItem.getChildren();
		int id = Integer.parseInt( listCells.get(0).getLabel() );
		
		Document document = null;
		Iterator<Document> iterator = documents.iterator();
		
		while( iterator.hasNext() ){
			document = iterator.next();
			if( document.getId() == id ) break;
		}
		
		DbDocResearcher researcher = new DbDocResearcher();
		
		ArrayList<DocumentFile> files = researcher.getDocumentFiles(id);
		if( files.isEmpty() ){
			try{
				Messagebox.show("Nenhum documento encontrado no banco de dados.",
						"Falha", Messagebox.OK, Messagebox.ERROR);
			}
			catch (InterruptedException e){}
			return;
		}
		else{
			String filename = files.get(0).getFilename();
			if( FileSupportVerificator.isDocumentValid(filename) ){
				if( FileSupportVerificator.isHTMLType(filename) ){
					Executions.getCurrent().getDesktop().getSession().setAttribute("htmlSource",
							new String( files.get(0).getByteStream() ));
					Executions.sendRedirect("/html_viewer.zul");
				}
				else if( FileSupportVerificator.isPDFType(filename) ){
					Executions.getCurrent().getDesktop().getSession().setAttribute("pdfFile",
							new AMedia(files.get(0).getFilename(), ".pdf", "application/pdf",
									files.get(0).getByteStream()) );	
					Executions.sendRedirect("/pdf_viewer.zul");
				}
				else if( FileSupportVerificator.isDOCType(filename) ){
					Filedownload.save(files.get(0).getByteStream(), "application/msword", 
							files.get(0).getFilename() );
				}
				else if( FileSupportVerificator.isDOCXType(filename) ){
					Filedownload.save(files.get(0).getByteStream(),
					"application/vnd.openxmlformats-officedocument.wordprocessingml.document", 
					files.get(0).getFilename() );
				}
			}
			else if( FileSupportVerificator.isImageValid(filename) ){
				ArrayList<AMedia> imageArray = new ArrayList<AMedia>();
				Iterator<DocumentFile> ite = files.iterator();
				DocumentFile docCur;
				while( ite.hasNext() ){
					docCur = ite.next();
					if( FileSupportVerificator.isJPEGType(docCur.getFilename()) ){
						imageArray.add( new AMedia(docCur.getFilename(), ".jpg", "image/jpeg",
								docCur.getByteStream() ));
					}
					else if( FileSupportVerificator.isPNGType(docCur.getFilename()) ){
						imageArray.add( new AMedia(docCur.getFilename(), ".png", "image/png",
								docCur.getByteStream() ));
					}
					else if( FileSupportVerificator.isGIFType(docCur.getFilename()) ){
						imageArray.add( new AMedia(docCur.getFilename(), ".gif", "image/gif",
								docCur.getByteStream() ));
					}
					else if( FileSupportVerificator.isBMPType(docCur.getFilename()) ){
						imageArray.add( new AMedia(docCur.getFilename(), ".bmp", "image/bmp",
								docCur.getByteStream() ));
					}
				}
				Executions.getCurrent().getDesktop().getSession().setAttribute("imageArray",
						imageArray);
				Executions.getCurrent().getDesktop().getSession().setAttribute("goBackURLImageViewer",
						"/admin_main.zul");
				Executions.sendRedirect("/image_viewer.zul");
			}
		}
	}
	
	public void edit(Component listitem) throws Exception{
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		Listitem listItem = (Listitem)listitem;
		List<Listcell> listCells = listItem.getChildren();
		int id = Integer.parseInt( listCells.get(0).getLabel() );
		
		Document document = null;
		Iterator<Document> iterator = documents.iterator();
		
		while( iterator.hasNext() ){
			document = iterator.next();
			if( document.getId() == id ) break;
		}
		
		Executions.getCurrent().getDesktop().getSession().setAttribute("editDocument",document);
		Executions.sendRedirect("/admin_editdoc.zul");
	}
	
	/**
	 * Quando clica na figurinha da opcao excluir
	 * @throws Exception 
	 */
	public void remove(Component listitem) throws Exception{
		if( !authenticated ){
			Executions.sendRedirect("/unauthorized.zul");
			return;
		}
		Listitem listItem = (Listitem)listitem;
		List<Listcell> listCells = listItem.getChildren();
		int id = Integer.parseInt( listCells.get(0).getLabel() );
		
		Document document = null;
		Iterator<Document> iterator = documents.iterator();
		
		while( iterator.hasNext() ){
			document = iterator.next();
			if( document.getId() == id ) break;
		}
		
		try{
			int op =
			Messagebox.show("Você deseja realmente excluir esse documento?\n" +
					"Título: " + document.getTitle(), "Confirmar exclusão de domento",
					Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
			if(op != Messagebox.YES) return;
		}
		catch(InterruptedException e){
			e.printStackTrace();
			return;
		}
		
		DbDocManager manager = new DbDocManager();
		manager.removeDocument(document.getId());
		
		Messagebox.show("Documento foi removido com sucesso!", "Sucesso na operação",
				Messagebox.OK, Messagebox.INFORMATION);
		documents.remove( document );
		Executions.getCurrent().getDesktop().getSession().setAttribute("documentsAdminWindow",
				documents);
		binder.loadAll();
	}
}
