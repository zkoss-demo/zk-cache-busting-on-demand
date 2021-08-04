package zk.example.cachebust;

import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppInit;

import javax.servlet.ServletContext;

public class SHA1HashInit implements WebAppInit {

	@Override
	public void init(WebApp webApp) throws Exception {
		ServletContext servletContext = webApp.getServletContext();
//		Servlets.addExtendletContext(servletContext, ".[sha1]", new ExtendletContext() {
//			...
//		});
	}
}
