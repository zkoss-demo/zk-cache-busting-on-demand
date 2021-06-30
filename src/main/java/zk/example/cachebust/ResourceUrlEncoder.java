package zk.example.cachebust;

import org.zkoss.util.resource.Locator;
import org.zkoss.web.util.resource.ExtendletContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * A limited ExtendletContext to only implement {@link ExtendletContext#encodeURL}.
 * All other methods throw an {@link UnsupportedOperationException} indicating incorrect/unexpected use.
 */
@FunctionalInterface
interface ResourceUrlEncoder extends ExtendletContext {
	@Override
	default String encodeRedirectURL(HttpServletRequest request, HttpServletResponse response, String uri, Map params, int mode) {
		throw new UnsupportedOperationException();
	}

	@Override
	default RequestDispatcher getRequestDispatcher(String uri) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void include(HttpServletRequest request, HttpServletResponse response, String uri, Map params) {
		throw new UnsupportedOperationException();
	}

	@Override
	default URL getResource(String uri) {
		throw new UnsupportedOperationException();
	}

	@Override
	default InputStream getResourceAsStream(String uri) {
		throw new UnsupportedOperationException();
	}

	@Override
	default ServletContext getServletContext() {
		throw new UnsupportedOperationException();
	}

	@Override
	default Locator getLocator() {
		throw new UnsupportedOperationException();
	}

	@Override
	default boolean shallCompress(ServletRequest request, String ext) {
		throw new UnsupportedOperationException();
	}
}
