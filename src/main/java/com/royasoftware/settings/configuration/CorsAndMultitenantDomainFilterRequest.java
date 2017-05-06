package com.royasoftware.settings.configuration;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.royasoftware.TenantContext;
import com.royasoftware.settings.security.CustomUserDetails;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsAndMultitenantDomainFilterRequest implements Filter {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Logger log = LoggerFactory.getLogger(CorsAndMultitenantDomainFilterRequest.class);

    public CorsAndMultitenantDomainFilterRequest() {
        log.info("SimpleCORSFilter init");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//    	logger.info("CorsAndMultitenantDomainFilterRequest. Call ");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE, HEAD");
        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Authorization, Content-Type");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, cache-control, authentication, authorization, Content-Type, Origin, X-Auth-Token, client-security-token");
        response.setHeader("Access-Control-Max-Age", "3600");
        
        
		String site = request.getServerName();
//		logger.info("filter site="+site); 
//		if( site.equals("127.0.0.1"))
//			site = "abbaslearn.royasoftware.com";
		String domain = null;
//		if( !site.endsWith(".localhost"))
//		domain = site.substring(site.lastIndexOf('.', site.lastIndexOf('.')-1) + 1);
		try {
			String[] parts = site.split("\\.");
//			for( String part : parts)
//				logger.info("next part "+part); 
			if( parts==null||parts.length!=4||!parts[3].equals("com")||!parts[2].equals("royasoftware")||!parts[1].equals("school") ){
				TenantContext.setCurrentTenant(null);
				throw new Exception("Malformed URL");
			}else
				TenantContext.setCurrentTenant(parts[0]);
		
		
//		domain = site.substring(site.lastIndexOf('.', site.lastIndexOf('.')-1) + 1);
//		// else
//		// domain="localhost";
//        try {
//			String subdomain = site.replaceAll(domain, "");
//			subdomain = subdomain.substring(0, subdomain.length() - 1);
//			
//			if(subdomain.contains(".")){
//				logger.info("AccessDeniedException. request.getRequestURL()="+request.getRequestURL());
//				TenantContext.setCurrentTenant(null);
//				throw new Exception("Sub with point is not allowed");
//			}else
//				TenantContext.setCurrentTenant(subdomain);
			
						
//		TenantContext.setCurrentTenant("abbaslearning");
//			logger.info("CorsAndMultitenantDomainFilterRequest. Set tenant context subdomain: "+subdomain);

	        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
	            response.setStatus(HttpServletResponse.SC_OK);             
	        } else {
	            chain.doFilter(req, res);
	        }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			TenantContext.resetThreadLocal();
		}

        
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}
