package bulletin.common;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/AuthFilter")
public class AuthFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Initialization code, if needed
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		HttpSession session = httpRequest.getSession(false);
		boolean isLoggedIn = (session != null && session.getAttribute("userManager") != null);
		String uri = httpRequest.getRequestURI();
		String prefixToRemove = httpRequest.getContextPath().toString();
		String resultString = uri.replaceFirst("^" + prefixToRemove, "");

		if (isLoggedIn) {
			if (!(uri.endsWith("login.jsp") || uri.endsWith("register.jsp") || uri.endsWith("AccountController") || uri.endsWith("/BulletinOJT/"))) {
				httpRequest.getRequestDispatcher(resultString).forward(httpRequest, httpResponse);
			} else {
				httpRequest.getRequestDispatcher("/Layout/index.jsp").forward(httpRequest, httpResponse);
			}
		} else {
			// User is not logged in, redirect to the login page\
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		// Cleanup code, if needed
	}
}
