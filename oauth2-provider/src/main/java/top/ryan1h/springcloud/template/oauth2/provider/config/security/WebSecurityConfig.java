package top.ryan1h.springcloud.template.oauth2.provider.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 这个类就是普通的spring security配置，比如配置AuthenticationProvider，也可以配置登录
 * 因为登录的时候也会有session,因此可以使用redis保存session,引入spring-session-data-redis，spring-boot-starter-data-redis依赖
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Integer MAX_SESSION = 1;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .permitAll()

                .and()
                .authorizeRequests()
                // 没法用WebSecuirty配置，那样配置后，登录不走过滤器了，而且这里的antMather()必须写在authenticated()前面
                .antMatchers("/doc.html",
                        "/swagger-resources",
                        "/v2/api-docs",
                        "/v2/api-docs-ext",
                        "/webjars/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .csrf().disable()

                .sessionManagement()
                .maximumSessions(MAX_SESSION)
        ;
    }
}

