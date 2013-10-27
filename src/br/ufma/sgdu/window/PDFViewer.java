package br.ufma.sgdu.window;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

public class PDFViewer extends Window {

	private static final long serialVersionUID = -1101476698719648801L;
	public Window window;
	private DataBinder binder;
	
	public void onCreate()
    {
    	window = (Window)getFellow("pdfWindow");
        binder =  new AnnotateDataBinder(window);
        
        AMedia pdfFile =
        	(AMedia)Executions.getCurrent().getDesktop().getSession().getAttribute("pdfFile");
        
        if(pdfFile != null){
        	((Iframe)getFellow("pdfField")).setContent(pdfFile);
        }
        binder.loadAll();
    }
}
