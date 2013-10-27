package br.ufma.sgdu.window;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;

import br.ufma.sgdu.database.DbDocResearcher;
import br.ufma.sgdu.entity.Document;
import br.ufma.sgdu.entity.DocumentFile;
import br.ufma.sgdu.util.FileSupportVerificator;

public class SearchWindow extends MainSearchWindow {

	private static final long serialVersionUID = 6929872073025822757L;
	
	private int lastIndex = 0;
	private int lengthIndex;
	ArrayList<Document> docResearch;
	Search lastSearch;
	
	@Override
	public void onCreate() {
		super.onCreate();
		lastSearch =
			(Search) Executions.getCurrent().getDesktop().getSession().getAttribute("lastSearchParams");
		docResearch =
			(ArrayList<Document>) Executions.getCurrent().getDesktop().getSession().getAttribute("commomDocResearch");
		
		if(lastSearch != null){
			((Textbox)getFellow("textboxSearch")).setValue( lastSearch.strSearch );
			((Checkbox)getFellow("checkboxTitle")).setChecked( lastSearch.byTitle );
			((Checkbox)getFellow("checkboxDescription")).setChecked( lastSearch.byDescription );
			((Checkbox)getFellow("checkboxContent")).setChecked( lastSearch.byContent );
			lastIndex = lastSearch.lastIndex;
		}
		if(docResearch == null) return;
		
		((Label)getFellow("headLabel")).setValue(docResearch.size() + " resultados (" +
				lastSearch.searchTime/1000.0 + " segundos)");
		
		Vbox resultsVbox = (Vbox)getFellow("resultsVBox");
		lengthIndex = (lastIndex + 10 > docResearch.size())? docResearch.size() : lastIndex + 10;
		
		if( docResearch.isEmpty() ){
			Label label = new Label("Sua pesquisa -\"" + lastSearch.strSearch + "\"- não encontrou" +
					" nenhum documento correspondente.");
			((Button)getFellow("previousButton")).setVisible(false);
			((Button)getFellow("nextButton")).setVisible(false);
			resultsVbox.appendChild(label);
			binder.loadAll();
			return;
		}
		
		for(int i=lastIndex; i<lengthIndex; i++){
			// 1º HBOX
			Hbox firstHbox = new Hbox();
			
			Toolbarbutton titleToolbarbutton = new Toolbarbutton( docResearch.get(i).getTitle() );
			titleToolbarbutton.setStyle("font-size: medium;");
			titleToolbarbutton.addEventListener("onClick", new Visualize(i));
			
			Separator firsSeparator = new Separator();
			firsSeparator.setWidth("10px");
			
			Label expeditionDateLabel =
				new Label( DateFormat.getDateInstance().format(docResearch.get(i).getExpeditionDate()) );
			expeditionDateLabel.setStyle("font-size: small; font-style:italic; color:gray;");
			
			firstHbox.appendChild(titleToolbarbutton);
			firstHbox.appendChild(firsSeparator);
			firstHbox.appendChild(expeditionDateLabel);
			
			// LABEL - CONTENT
			Label descriptionLabel = new Label( docResearch.get(i).getDescription() );
			descriptionLabel.setStyle("font-size: small;");
			
			// 2ª HBOX
			Hbox secondHbox = new Hbox();
			Toolbarbutton visualizeToolbarbutton = null;
			Toolbarbutton downloadToolbarbutton = null;
			Separator secondSeparator = null;
			
			if( docResearch.get(i).getType() != Document.MICROSOFT_WORD_TYPE ){
				visualizeToolbarbutton = new Toolbarbutton( "Visualizar" );
				visualizeToolbarbutton.setImage("images/lupa.png");
				visualizeToolbarbutton.setStyle("font-size: small;");
				visualizeToolbarbutton.addEventListener("onClick", new Visualize(i));
				
				secondSeparator = new Separator();
				secondSeparator.setWidth("10px");
				
				secondHbox.appendChild(visualizeToolbarbutton);
				secondHbox.appendChild(secondSeparator);
			}
			if( docResearch.get(i).getType() != Document.IMAGE_TYPE ){
				downloadToolbarbutton = new Toolbarbutton( "Download" );
				downloadToolbarbutton.setImage("images/download.png");
				downloadToolbarbutton.setStyle("font-size: small;");
				downloadToolbarbutton.addEventListener("onClick", new Download(i));
				
				secondHbox.appendChild(downloadToolbarbutton);
			}
			
			// SEPARATOR
			Separator thirdSeparator = new Separator();
			thirdSeparator.setHeight("25px");
			
			// fogo!
			resultsVbox.appendChild(firstHbox);
			resultsVbox.appendChild(descriptionLabel);
			resultsVbox.appendChild(secondHbox);
			resultsVbox.appendChild(thirdSeparator);
		}
		Button previousButton = (Button)getFellow("previousButton");
		Button nextButton = (Button)getFellow("nextButton");
		previousButton.setDisabled(lastIndex == 0);
		nextButton.setDisabled(lengthIndex == docResearch.size());
		if( previousButton.isDisabled() && nextButton.isDisabled() ){
			previousButton.setVisible(false);
			nextButton.setVisible(false);
		}
		binder.loadAll();
		Clients.scrollIntoView( getFellow("textboxSearch") );
	}

	public void nextPage(){
		lastSearch.lastIndex = lengthIndex;
		Executions.sendRedirect("/search_results.zul");
		Clients.scrollIntoView( getFellow("textboxSearch") );
	}
	
	public void previousPage(){
		lastSearch.lastIndex -= 10;
		Executions.sendRedirect("/search_results.zul");
		Clients.scrollIntoView( getFellow("textboxSearch") );
	}
	
	private class Visualize implements EventListener{
		
		private int index;
		
		public Visualize(int index){
			this.index = index;
		}

		@Override
		public void onEvent(Event arg0) throws Exception {
			DbDocResearcher researcher = new DbDocResearcher();
			ArrayList<DocumentFile> files = researcher.getDocumentFiles( docResearch.get(index).getId() );
			if( files.isEmpty() ) return;
			
			String filename = files.get(0).getFilename();
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
			else if( FileSupportVerificator.isDOCType(filename) ||
					FileSupportVerificator.isDOCXType(filename)){
				(new Download(index)).onEvent(arg0);
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
						"/search_results.zul");
				Executions.sendRedirect("/image_viewer.zul");
			}
		}
	}
	
	private class Download implements EventListener{
		private int index;
		
		public Download(int index){
			this.index = index;
		}

		@Override
		public void onEvent(Event arg0) throws Exception {
			DbDocResearcher researcher = new DbDocResearcher();
			ArrayList<DocumentFile> files = researcher.getDocumentFiles( docResearch.get(index).getId() );
			if( files.isEmpty() ) return;
			
			String filename = files.get(0).getFilename();
			if( FileSupportVerificator.isPDFType(filename) ){
				Filedownload.save(files.get(0).getByteStream(), "application/pdf", 
						files.get(0).getFilename() );
			}
			else if( FileSupportVerificator.isDOCType(filename)){
				Filedownload.save(files.get(0).getByteStream(), "application/msword", 
						files.get(0).getFilename() );
			}
			else if( FileSupportVerificator.isDOCXType(filename) ){
				Filedownload.save(files.get(0).getByteStream(),
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document", 
				files.get(0).getFilename() );
			}
			else if( FileSupportVerificator.isHTMLType(filename) ){
				Filedownload.save(files.get(0).getByteStream(), "text/html", 
						files.get(0).getFilename() );
			}
		}
	}
}
