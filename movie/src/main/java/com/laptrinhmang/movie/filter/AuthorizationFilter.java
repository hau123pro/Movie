package com.laptrinhmang.movie.filter;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.web.filter.OncePerRequestFilter;

import com.laptrinhmang.movie.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

	private static final String APPLICATION_JSON_VALUE = "application/json";
	private final JwtTokenUtil jwtTokenUtil;

	public AuthorizationFilter(JwtTokenUtil jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
			if (request.getServletPath().equals("/movie/getPublicKey")) {
				filterChain.doFilter(request, response);
			} else {
				String authorizationHeader = request.getHeader("PublicKey");
				System.out.println(authorizationHeader);
				if (authorizationHeader != null ) {
					try {
						String publicKey = jwtTokenUtil.getPublicKeyFromToken(authorizationHeader);
						System.out.println(publicKey);
						System.out.println(request.getSession().getAttribute("publicKey"));
						// PublicKey key=(PublicKey) publicKey;
						if(request.getSession().getAttribute("publicKey").equals(publicKey))
							filterChain.doFilter(request, response);
					} catch (Exception e) {
						response.setHeader("error", e.getMessage());
						response.setStatus(403);
						Map<String, String> errors = new HashMap<>();
						errors.put("error-msg", e.getMessage());
						response.setContentType(APPLICATION_JSON_VALUE);
						new ObjectMapper().writeValue(response.getOutputStream(), errors);
					}
				}
			}
	}

}
