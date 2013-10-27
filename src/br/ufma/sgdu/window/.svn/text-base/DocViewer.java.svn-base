package br.ufma.sgdu.window;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Html;
import org.zkoss.zul.Window;

public class HtmlViewer extends Window {

	private static final long serialVersionUID = -456638173359416252L;
	public Window window;
	private DataBinder binder;
	
	public void onCreate()
    {
    	window = (Window)getFellow("htmlWindow");
        binder =  new AnnotateDataBinder(window);
        
        String htmlSource =
        	(String)Executions.getCurrent().getDesktop().getSession().getAttribute("htmlSource");
        
        if(htmlSource != null){
        	((Html)getFellow("htmlField")).setContent(htmlSource);
        }
        
        binder.loadAll();
    }

}
