package com.isep.acme.adapters.controllers;

import com.isep.acme.adapters.dto.AuthRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@Tag(name = "Authentication")
@RestController
@RequestMapping(path = "auth/public")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtEncoder jwtEncoder;

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody @Valid final AuthRequestDTO request) throws BadCredentialsException {
        final Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = (User) authentication.getPrincipal();

        final Instant now = Instant.now();
//            final long expiry = 36000L;
        final long expiry = 36000000000000L; // todo: remove

        final String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(joining(" "));

        final JwtClaimsSet claims = JwtClaimsSet.builder().issuer("example.io").issuedAt(now)
                .expiresAt(now.plusSeconds(expiry)).subject(format("%s,%s", user.getUsername(), user.getPassword()))
                .claim("roles", scope).build();

        final String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).body("Login with success");
    }
}
