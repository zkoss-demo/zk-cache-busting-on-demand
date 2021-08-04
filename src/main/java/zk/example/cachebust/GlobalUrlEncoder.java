package zk.example.cachebust;

import org.zkoss.web.servlet.http.Encodes;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class GlobalUrlEncoder implements Encodes.URLEncoder {
	@Override
	public String encodeURL(ServletContext servletContext, ServletRequest servletRequest, ServletResponse servletResponse, String s, Encodes.URLEncoder urlEncoder) throws Exception {
		// possible to customize general URL encoding here
		String encodeURL = urlEncoder.encodeURL(servletContext, servletRequest, servletResponse, s, null);
		return encodeURL;
	}
}
