package edu.asu.conceptpower.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserSessionFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public UserSessionFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest)request).getSession();
		
		if (session != null) {
			
			LoginController controller = (LoginController) session.getAttribute("loginController");;
			if (controller != null && controller.getUser() != null) {
				chain.doFilter(request, response);
				return;
			}
				
		}
		
		// User is not logged in, so redirect to login page.
        HttpServletResponse res = (HttpServletResponse) response;
        res.sendRedirect(((HttpServletRequest)request).getContextPath() + "/faces/LoginView.xhtml");
        
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
