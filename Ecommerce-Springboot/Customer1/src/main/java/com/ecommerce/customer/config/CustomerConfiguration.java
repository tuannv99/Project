package com.ecommerce.customer.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ecommerce.library.service.oauth2.security.CustomOAuth2UserDetailService;
import com.ecommerce.library.service.oauth2.security.handler.CustomOAuth2FailureHandler;
import com.ecommerce.library.service.oauth2.security.handler.CustomOAuth2SuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class CustomerConfiguration {
	
	@Autowired
	private CustomOAuth2UserDetailService customOAuth2UserDetailService;
	
	@Autowired
	private CustomOAuth2FailureHandler customOAuth2FailureHandler;
	
	@Autowired
	private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomerServiceConfig();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsService())
				.passwordEncoder(passwordEncoder())
				.and()
				.build();
	}

	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		 http.csrf().disable()
			.authorizeHttpRequests()
			.requestMatchers("/index/**", "/**","/home/**").permitAll()
			.requestMatchers("/shop/**").hasRole("CUSTOMER")
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/do-login")
			.defaultSuccessUrl("/index",true)
			.permitAll()
			.and()
			.logout()
			.deleteCookies("JSESSIONID")
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/login?logout")
			.and()
			.authenticationManager(authenticationManager(http))
			.httpBasic()
			.and()
			.oauth2Login()
			.loginPage("/login")
			.defaultSuccessUrl("/index", true)
			.userInfoEndpoint()
			.userService(customOAuth2UserDetailService)
			.and()
			.successHandler(customOAuth2SuccessHandler)
			.failureHandler(customOAuth2FailureHandler)
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
			
			return http.build();
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers("/js/**","/css/**");
	}
	
}
