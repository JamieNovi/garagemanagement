package nl.jamienovi.garagemanagement.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public SecurityConfig(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrf().disable()
                .authorizeRequests()
//                .antMatchers("/api/klanten").permitAll()
//                .antMatchers("/api/**").hasRole(ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();

    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails adminUser = User.builder()
//                .username("admin")
//                .password(passwordEncoder.encode("1234"))
//                .authorities(ADMIN.getGrantedAuthorities())
//                .build();
//
//        UserDetails frontOfficeUser = User.builder()
//                .username("front_office")
//                .password(passwordEncoder.encode("1234"))
//                .authorities(FRONTOFFICE.getGrantedAuthorities())
//                .build();
//
//        return new InMemoryUserDetailsManager(
//                adminUser,
//                frontOfficeUser
//        );
//    }
}
