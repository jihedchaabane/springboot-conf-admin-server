//package com.chj.gr.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
//
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
//@Configuration
//@EnableWebSecurity
///**
//IF:
//
//spring:
//  security:
//    user:
//      name: admin
//      password: admin
//public class SBAInMemoryUserDetailsManager {
//	@Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http
//		.authorizeRequests()
//		.antMatchers("/actuator/**").permitAll()
//		.anyRequest().authenticated()
//		.and()
//			.formLogin()
//		.and()
//			.httpBasic()
//		.and()
//			.csrf().disable();
//        return http.build();
//    }
//}
//*/
//public class SBAInMemoryUserDetailsManager extends WebSecurityConfigurerAdapter {
//	    @Override
//	    protected void configure(HttpSecurity http) throws Exception {
//	        http
//	            .authorizeRequests()
//	                .antMatchers("/actuator/**").permitAll()
//	                .antMatchers("/assets/**", "/login").permitAll()
//	                .anyRequest().authenticated()
//	            .and()
//	            .formLogin()
//	                .loginPage("/login")
//	                .defaultSuccessUrl("/applications")
//	                .permitAll()
//	            .and()
//	            .httpBasic()
//	            .and()
//	            .csrf().disable()
//	            .logout()
//	                .logoutUrl("/logout")
//	                .logoutSuccessUrl("/login?logout");
//	    }
//
//	    @Override
//	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//	        auth.inMemoryAuthentication()
//	            .withUser("admin")
//	            .password("{noop}admin")
//	            .roles("ADMIN");
//	    }
//
//	    @Override
//	    protected UserDetailsService userDetailsService() {
//	    	/**
//	    	import java.util.Properties;
//	    		Properties users = new Properties();
//	    		users.setProperty("admin", "password,ROLE_ADMIN");
//	    		return new InMemoryUserDetailsManager(users);
//	    	*/
//	    	/**
//	    	import org.springframework.security.core.userdetails.UserDetails;
//	    	 	UserDetails user1 = User.withUsername("admin")
//			        .password("{noop}password")
//			        .roles("ADMIN")
//			        .build();
//				UserDetails user2 = User.withUsername("user")
//			        .password("{noop}password")
//			        .roles("USER")
//			        .build();
//				return new InMemoryUserDetailsManager(user1, user2);
//	    	*/ 
//	        return new InMemoryUserDetailsManager(
//	            User.withUsername("admin")
//	                .password("{noop}admin")
//	                .roles("ADMIN")
//	                .build()
//	        );
//	    }
//}
/////**
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////import org.springframework.security.core.userdetails.User;
////import org.springframework.security.core.userdetails.UserDetailsService;
////import org.springframework.security.provisioning.InMemoryUserDetailsManager;
////
////@Configuration
////@EnableWebSecurity
////public class SBAInMemoryUserDetailsManager {
////    @Bean
////    public UserDetailsService userDetailsService() {
////        UserDetails user = User.withUsername("admin")
////                .password("{noop}password")
////                .roles("ADMIN")
////                .build();
////        return new InMemoryUserDetailsManager(user);
////    }
////}
/////*