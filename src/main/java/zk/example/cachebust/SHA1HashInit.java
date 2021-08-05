package zk.example.cachebust;

import org.zkoss.web.servlet.Servlets;
import org.zkoss.web.util.resource.ClassWebResource;
import org.zkoss.web.util.resource.ExtendletContext;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.http.WebManager;
import org.zkoss.zk.ui.util.WebAppInit;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class SHA1HashInit implements WebAppInit {

	private ExtendletContext cwrExtendletContext;
	private ServletContext servletContext;
	private ClassWebResource cwr;

	@Override
	public void init(WebApp webApp) throws Exception {
		servletContext = webApp.getServletContext();
		cwr = WebManager.getWebManager(webApp).getClassWebResource();
		Servlets.addExtendletContext(servletContext, ".[sha1]", (ResourceUrlEncoder) this::encodeURL);
		cwrExtendletContext = Servlets.getExtendletContext(servletContext, ".");
	}

	public String encodeURL(ServletRequest request, ServletResponse response, String uri) throws ServletException, IOException {
		long start = System.nanoTime();
		uri = Servlets.locate(servletContext, request, uri, cwrExtendletContext.getLocator());
		String hash = computeHash(uri);
		long end = System.nanoTime();
		System.out.println(TimeUnit.NANOSECONDS.toMillis(end - start));
		String encodedURL = cwrExtendletContext.encodeURL(request, response, uri);
		String replace = encodedURL.replace(cwr.getEncodeURLPrefix(), "/_zv" + hash);
		return replace;
	}

	private String computeHash(String uri) {
		try (var is = new DigestInputStream(cwrExtendletContext.getResourceAsStream(uri), MessageDigest.getInstance("SHA-1"))) {
			is.transferTo(OutputStream.nullOutputStream());
			return Base64.getUrlEncoder().withoutPadding().encodeToString(is.getMessageDigest().digest());
		} catch (Exception e) {
			throw new RuntimeException("Error computing the sha-1 hash for: " + uri, e);
		}
	}
}
