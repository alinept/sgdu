package br.ufma.sgdu.window;

import java.io.IOException;
import java.util.ArrayList;

import org.zkoss.image.AImage;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Image;

public class ImageViewer extends Window {

	private static final long serialVersionUID = -2931669680635271312L;
	public Window window;
	private DataBinder binder;
	
	private int indexImage = 0;
	private ArrayList<AMedia> imageArray;
	private int zoom;
	
	public void onCreate()
    {
    	window = (Window)getFellow("imageWindow");
        binder =  new AnnotateDataBinder(window);
        
        zoom = 99;
        
        imageArray = 
        	(ArrayList<AMedia>) Executions.getCurrent().getDesktop().getSession().getAttribute("imageArray");
        
        if(imageArray != null){
        	((Button)getFellow("nextButton")).setDisabled( imageArray.size() < 2 );
        	if( imageArray.size() > 0 ){
        		setImage();
        	}
        }
        binder.loadAll();
    }
	
	public void next(){
		indexImage++;
		setImage();
		((Button)getFellow("nextButton")).setDisabled( indexImage >= imageArray.size() - 1 );
		((Button)getFellow("previousButton")).setDisabled( false );
		binder.loadAll();
	}

	public void previous(){
		indexImage--;
		setImage();
		((Button)getFellow("nextButton")).setDisabled( false );
		((Button)getFellow("previousButton")).setDisabled( indexImage <= 0 );
		binder.loadAll();
	}
	
	public void goBack(){
		String goBackURL =
			(String) Executions.getCurrent().getDesktop().getSession().getAttribute("goBackURLImageViewer");
		if(goBackURL != null){
			Executions.sendRedirect(goBackURL);
		}
		else Executions.sendRedirect("javascript:history.go(-1)");
		Executions.getCurrent().getDesktop().getSession().removeAttribute("goBackURLImageViewer");
	}
	
	public void zoomOut(){
		if(zoom == 99){
			zoom = 90;
		}
		else if(zoom >= 20){
			zoom -= 10;
		}
		else{
			zoom = 10;
			((Button)getFellow("zoomOutButton")).setDisabled(true);
		}
		((Button)getFellow("zoomInButton")).setDisabled(false);
		setZoom();
	}
	
	public void zoomIn(){
		if(zoom <= 90){
			zoom += 10;
		}
		else{
			zoom = 100;
			((Button)getFellow("zoomInButton")).setDisabled(true);
		}
		((Button)getFellow("zoomOutButton")).setDisabled(false);
		setZoom();
	}
	
	private void setImage(){
		try {
			((Image)getFellow("imageField")).setContent(
					new AImage( imageArray.get(indexImage).getName(), imageArray.get(indexImage).getByteData() )
					);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				Messagebox.show("Erro ao carregar imagem", e.toString(),
						Messagebox.OK, Messagebox.ERROR);
			} catch (InterruptedException e1) {}
		}
	}
	
	private void setZoom(){
		((Image)getFellow("imageField")).setWidth(zoom + "%");
	}
}
