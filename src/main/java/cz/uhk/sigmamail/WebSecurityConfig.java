package cz.uhk.sigmamail;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig{

        private DataSource dataSource;

        @Bean
        public UserDetailsService userDetailsService(){
                return new CustomUserDetailsService();
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder(){
                return new BCryptPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider(){
                DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
                daoAuthenticationProvider.setUserDetailsService(userDetailsService());
                daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

                return daoAuthenticationProvider;
        }

        public AuthenticationManager config(AuthenticationConfiguration authenticationConfiguration) throws Exception{
                return authenticationConfiguration.getAuthenticationManager();
        }

        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
               httpSecurity.authorizeHttpRequests((auth) ->auth.requestMatchers("/").permitAll().requestMatchers("/mailbox").authenticated().anyRequest().authenticated()).formLogin((login) -> login.usernameParameter("username").defaultSuccessUrl("/mailbox").permitAll()).logout((logout) -> logout.logoutSuccessUrl("/").permitAll());

               return httpSecurity.build();
        }

}
