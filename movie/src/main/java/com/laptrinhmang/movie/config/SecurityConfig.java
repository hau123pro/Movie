package com.laptrinhmang.movie.config;
import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.laptrinhmang.movie.filter.AuthorizationFilter;
import com.laptrinhmang.movie.utils.JwtTokenUtil;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
//	@Autowired
//	CustomerDetailService customer;
    @Autowired 
    HttpSession session; //autowiring session

	@Autowired
	JwtTokenUtil jwtUtil;

	// c???u h??nh bean c??ch th???c m?? h??a ??? ????y l?? Bcrypt
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
//	
//	@Bean
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
			.authorizeHttpRequests()
			//cho ph??p c??c link trong ".." ???????c x??c th???c 
//			.antMatchers("/admin/**").permitAll()
			.antMatchers("/movie/**").permitAll()
			.and()	
			// .addFilterBefore(new AuthorizationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
			// c??n l???i ph???i x??c th???c b???ng c??ch login
			// .anyRequest().authenticated()
			// .and()
			.httpBasic()
			;
	}
	// X??c th???c t??i kho???n ????ng nh???p b???ng d??ng userdetail t??? customerdetailservice
//	@Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//	    auth
//	    .userDetailsService(customer).passwordEncoder(passwordEncoder());
////	      .inMemoryAuthentication()
////	        .withUser("user").password(passwordEncoder().encode("123")).roles("USER");
//	  }
//	
}
	