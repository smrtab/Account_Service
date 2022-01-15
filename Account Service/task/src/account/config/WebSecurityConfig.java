package account.config;

import account.data.entity.Role;
import account.data.entity.User;
import account.data.model.EventAction;
import account.data.repository.UserRepository;
import account.service.EventService;
import account.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final EventService eventService;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    @Autowired
    public WebSecurityConfig(
        UserService userService,
        EventService eventService,
        RestAuthenticationEntryPoint restAuthenticationEntryPoint,
        LoginSuccessHandler loginSuccessHandler,
        LoginFailureHandler loginFailureHandler
    ) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.userService = userService;
        this.eventService = eventService;
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailureHandler = loginFailureHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handle auth error
            .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
            .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
            .and()
                .authorizeRequests() // manage access
                .antMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/changepass").hasAnyRole(Role.USER, Role.ACCOUNTANT, Role.ADMINISTRATOR)
                .antMatchers(HttpMethod.GET, "/api/empl/payment").hasAnyRole(Role.USER, Role.ACCOUNTANT)
                .antMatchers(HttpMethod.POST, "/api/acct/payments").hasRole(Role.ACCOUNTANT)
                .antMatchers(HttpMethod.PUT, "/api/acct/payments").hasRole(Role.ACCOUNTANT)
                .antMatchers(HttpMethod.GET, "/api/security/events/").hasRole(Role.AUDITOR)
                .antMatchers("/api/admin/user/**").hasRole(Role.ADMINISTRATOR)
            // other matchers
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Expose authentication manager bean
    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            eventService.create(
                EventAction.ACCESS_DENIED,
                request.getUserPrincipal().getName()
            );
            response.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Access Denied!"
            );
        };
    }
}