package zk.example.cachebust;

import org.zkoss.lang.Library;
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
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MD5HashInit implements WebAppInit {

	private static final String CWR_EXT_CTX = ".";      // the default CWR URL prefix
	private static final String MD5_EXT_CTX = ".[md5]"; // our custom resource URL prefix

	public static final String CACHE_ENABLED_PROP = "zk.example.cachebust.MD5HashInit.cacheEnabled";

	private ServletContext servletContext;
	private ClassWebResource cwr;
	private ExtendletContext cwrExtendletContext;
	private Map<String, String> hashByUri = new ConcurrentHashMap<>();
	private boolean cacheEnabled;

	@Override
	public void init(WebApp wapp) {
		servletContext = wapp.getServletContext();
		cwr = WebManager.getWebManager(wapp).getClassWebResource();
		cwrExtendletContext = Servlets.getExtendletContext(servletContext, CWR_EXT_CTX);
		Servlets.addExtendletContext(servletContext, MD5_EXT_CTX, (ResourceUrlEncoder) this::encodeURL);
		this.cacheEnabled = Boolean.valueOf(Library.getProperty(CACHE_ENABLED_PROP, "true"));
	}

	private String encodeURL(ServletRequest request, ServletResponse response, String uri) throws ServletException, IOException {
		String locatedUri = Servlets.locate(servletContext, request, uri, cwrExtendletContext.getLocator());
		String defaultCwrUrl = cwrExtendletContext.encodeURL(request, response, locatedUri);
		String hash = cacheEnabled
				? hashByUri.computeIfAbsent(locatedUri, this::computeResourceHash)
				: this.computeResourceHash(locatedUri);
		return defaultCwrUrl.replace(cwr.getEncodeURLPrefix(), "/_zv_md5_" + hash); // prefix "/_zv" would be sufficient
	}

	private String computeResourceHash(String classWebResourceUri) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			try (DigestInputStream is = new DigestInputStream(
					cwrExtendletContext.getResourceAsStream(classWebResourceUri), md5)) {
				is.transferTo(OutputStream.nullOutputStream());
				return Base64.getUrlEncoder().withoutPadding().encodeToString(
						is.getMessageDigest().digest(classWebResourceUri.getBytes(StandardCharsets.UTF_8)));
			}
		} catch (Exception e) {
			throw new RuntimeException("Error computing md5 hash for Class-Web-Resource Uri: " + classWebResourceUri);
		}
	}
}
