package com.ssm.maven.core.Filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.CharacterEncodingFilter;


/* 
 * InvalidCharacterFilter：过滤request请求中的非法字符，防止脚本攻击 
 * InvalidCharacterFilter继承了Spring框架的CharacterEncodingFilter过滤器，当然，我们也可以自己实现这样一个过滤器 
 */  
public class InvalidCharacterFilter extends CharacterEncodingFilter{

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)  
            throws ServletException, IOException{   
        super.doFilterInternal(new XssHttpServletRequestWrapper(request), response, filterChain);  
    } 
	
}






















