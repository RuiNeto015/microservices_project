package com.isep.acme.config;

import com.isep.acme.applicationServices.interfaces.repositories.IUserRepository;
import com.isep.acme.domain.aggregates.user.User;
import com.isep.acme.domain.enums.RoleEnum;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;

import static java.lang.String.format;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final IUserRepository repository;

    @Value("${jwt.public.key}")
    private RSAPublicKey rsaPublicKey;

    @Value("${jwt.private.key}")
    private RSAPrivateKey rsaPrivateKey;

    @Value("${springdoc.api-docs.path}")
    private String restApiDocPath;

    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(email -> {
            User user = this.repository.findByEmail(email);

            if (user == null) {
                throw new UsernameNotFoundException(format("User: %s, not found", email));
            }

            GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());

            return new org.springframework.security.core.userdetails.User(
                    user.getId(),
                    user.getPassword(),
                    Collections.singletonList(authority)
            );
        });
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

        // Set unauthorized requests exception handler
        http = http.exceptionHandling(
                exceptions -> exceptions.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));

        // required by h2-console according to https://stackoverflow.com/questions/40165915/why-does-the-h2-console-in-spring-boot-show-a-blank-screen-after-logging-in
        http.headers().frameOptions().disable();


        // Set permissions on endpoints
        http.authorizeRequests()
                // Swagger endpoints must be publicly accessible
                .antMatchers("/").permitAll()
                .antMatchers(format("%s/**", restApiDocPath)).permitAll()
                .antMatchers(format("%s/**", swaggerPath)).permitAll()
                // Our public endpoints
                .antMatchers(HttpMethod.GET, "/products/**").permitAll()
                .antMatchers(HttpMethod.GET, "/products/**/reviews/**").permitAll()
                // Our private endpoints
                .antMatchers(HttpMethod.GET, "/admin/**").hasRole(RoleEnum.Admin.name())
                .antMatchers(HttpMethod.POST, "/products").hasRole(RoleEnum.Admin.name())
                .antMatchers(HttpMethod.DELETE, "/products/**").hasRole(RoleEnum.Admin.name())
                .antMatchers(HttpMethod.PATCH, "/products/**").hasRole(RoleEnum.Admin.name())
                .antMatchers(HttpMethod.PUT, "/reviews/**/vote").hasAnyRole(RoleEnum.Mod.name(), RoleEnum.Mod.name(), RoleEnum.Admin.name())
                .antMatchers(HttpMethod.PUT, "/reviews/reject/**").hasAnyRole(RoleEnum.Mod.name(), RoleEnum.Mod.name(), RoleEnum.Admin.name())
                .antMatchers(HttpMethod.PUT, "/reviews/approve/**").hasAnyRole(RoleEnum.Mod.name(), RoleEnum.Mod.name(), RoleEnum.Admin.name())
                .antMatchers(HttpMethod.POST, "/products/reviews").hasAnyRole(RoleEnum.RegisteredUser.name(), RoleEnum.Mod.name(), RoleEnum.Admin.name())
                .antMatchers(HttpMethod.DELETE, "/reviews/**").hasAnyRole(RoleEnum.RegisteredUser.name(), RoleEnum.Mod.name(), RoleEnum.Admin.name())

//                .antMatchers(HttpMethod.GET, "/admin/**").hasRole(RoleEnum.Admin.name())
//                .antMatchers(HttpMethod.POST, "/products").hasRole(RoleEnum.Admin.name())
//                .antMatchers(HttpMethod.DELETE, "/products/**").hasRole(RoleEnum.Admin.name())
//                .antMatchers(HttpMethod.PATCH, "/products/**").hasRole(RoleEnum.Admin.name())
//                .antMatchers(HttpMethod.PUT, "/reviews/**/vote").hasRole(RoleEnum.RegisteredUser.name())
//                .antMatchers(HttpMethod.PUT, "/reviews/reject/**").hasRole(RoleEnum.Mod.name())
//                .antMatchers(HttpMethod.PUT, "/reviews/approve/**").hasRole(RoleEnum.Mod.name())
//                .antMatchers(HttpMethod.POST, "/products/reviews").hasRole(RoleEnum.RegisteredUser.name())
//                .antMatchers(HttpMethod.DELETE, "/reviews/**").hasRole(RoleEnum.RegisteredUser.name())

                // Set up oauth2 resource server
                .and().httpBasic(Customizer.withDefaults()).oauth2ResourceServer().jwt();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        final JWK jwk = new RSAKey.Builder(this.rsaPublicKey).privateKey(this.rsaPrivateKey).build();
        final JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.rsaPublicKey).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
