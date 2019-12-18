package com.dearhelp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.dearhelp.services.CustomUserDetailsService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity

public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }
    
   
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        //Web resources
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/img/**");
    }
  
    @Bean
    public static PasswordEncoder passwordEncoder() {
      return NoOpPasswordEncoder.getInstance();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }                             
                           
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
      
        .authorizeRequests()
            .antMatchers( "/index","/about","/contactinfo","/regfuser","/regForm","/regfsv",
            		"/regServiceProvider","/forgetPwd","/forgetPassword","/resetPwd","/contactform").permitAll()
        
            .antMatchers("/admin","/listusers","/listsps","/deleteuser1","/deleteuser2",
        		  "/searchuser1","/searchuser2").access("hasRole('ADMIN')")         
            .anyRequest().authenticated()
            .and()
            .formLogin()
            //   . loginProcessingUrl ( "/userlogin")
                   .loginPage("/login")
                   .defaultSuccessUrl("/index1")
                   .permitAll()
                   .and()
               .logout()
                 //  .permitAll()
               .logoutUrl("/logout"). 
           logoutSuccessUrl("/index")
           .permitAll()
                   .and()
                   .csrf().disable().
                       userDetailsService(userDetailsService());
    }   

}                                  

