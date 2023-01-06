package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;

import java.util.Arrays;

import static org.springframework.security.oauth2.jwt.JwtClaimNames.AUD;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig {

        @Configuration
        @Order(10)
        public static class ResourceServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

            @Value("${resource}")
            String resource;

            final
            JwtDecoder jwtDecoder;

            public ResourceServerSecurityConfiguration(JwtDecoder jwtDecoder) {
                this.jwtDecoder = jwtDecoder;
            }

            @Override
            protected void configure(HttpSecurity http) throws Exception {
                JwtDecoder newJwtDecoder = wrapJwtDecoderWithResourceCheck(this.jwtDecoder, resource);
                System.out.println(resource);
                http
                        .cors()
                        .and()
                        .antMatcher("/api/**")
                        .authorizeRequests()
                        .anyRequest().authenticated()
                        .and()
                        .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                        .oauth2ResourceServer()
                        .jwt()
                        .decoder(newJwtDecoder)
                ;
            }

        }
        static JwtDecoder wrapJwtDecoderWithResourceCheck(JwtDecoder jwtDecoder, String resource) {
            return (token) -> {
                Jwt jwt = jwtDecoder.decode(token);
                if (jwt.containsClaim(AUD) && !jwt.getClaimAsStringList(AUD).contains(resource)) {
                    throw new JwtValidationException("Resource field does not match: " + resource, Arrays.asList(new OAuth2Error("invalid_aud")));
                }
                return jwt;
            };
        }

        @Configuration
        @Order(30)
        public static class HttpSecurityConfiguration extends WebSecurityConfigurerAdapter {

            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http
                        .csrf()
                        .disable()
                        .authorizeRequests()
                        .antMatchers("/public/**").permitAll()
                        .antMatchers("/basic/**").authenticated()
                        .anyRequest().denyAll()
                        .and()
                        .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                        .httpBasic()
                ;
            }

        }

    }