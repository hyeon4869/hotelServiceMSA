package hotel.auth;

import hotel.domain.UserRepository;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
   
    private final UserRepository userRepository;

    //BCrypt 암호화를 사용하기 위한 메소드
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(HttpSecurity http) throws Exception{
        
        //csrf는 disable
        http.csrf().disable();
    
        //세션을 사용x
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // CrossOrigin(인증x), 시큐리티에 필터 등록(o)
            //.addFilter(corsFilter)
            // formLogin 방식 비활성화
            .formLogin().disable()
            // httpBasic 방식 비활성화
            .httpBasic().disable()
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), userRepository))//생성자 
            .exceptionHandling()
            .and()
            .authorizeRequests()
            .anyRequest().permitAll();
            }
    
}