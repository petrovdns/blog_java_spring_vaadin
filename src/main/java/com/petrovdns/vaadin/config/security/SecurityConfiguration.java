//    @Bean
//    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
//                        .requestMatchers("/login", "/favicon.ico").permitAll()
//                        .requestMatchers("/").hasAnyRole("EMPLOYEE", "HR", "MANAGER", "ADMIN")
//                        .anyRequest()
//                        .authenticated())
//                        .formLogin(Customizer.withDefaults());
//        return http.build();
//    }

package com.petrovdns.vaadin.config.security;

import com.petrovdns.vaadin.UI.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

    @Autowired
    protected DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/user-list")).hasAnyAuthority("ADMIN")
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/register")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/")).permitAll());
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

//    @Bean
//    public UserDetailsService users() {
//        UserDetails user1 = User.builder()
//                .username("petrovdns")
//                .password("{noop}petrovdns")
//                .roles("ADMIN")
//                .build();
//                return new InMemoryUserDetailsManager(user1);
//    }

    @Bean
    protected UserDetailsManager userDetailsManager() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
        manager.setDataSource(dataSource);

        manager.setUsersByUsernameQuery(
                "SELECT username, password, 1 as enabled FROM users WHERE username = ?");
        manager.setAuthoritiesByUsernameQuery(
                "SELECT u.username, r.role_name FROM users_roles ur " +
                        "INNER JOIN users u ON u.id = ur.users_id " +
                        "INNER JOIN roles r ON r.id = ur.roles_id " +
                        "WHERE u.username = ?");

        return manager;
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}