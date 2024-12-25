package rs.playgroundmath.playgroundmath.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain
    {
        http
            .csrf().disable()
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/v1/auth/register").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin().disable()
            .httpBasic().disable()

        return http.build();
    }
}