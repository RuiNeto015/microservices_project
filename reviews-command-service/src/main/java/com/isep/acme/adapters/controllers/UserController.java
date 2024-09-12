package com.isep.acme.adapters.controllers;

import com.isep.acme.adapters.dto.FetchUserDTO;
import com.isep.acme.applicationServices.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "Endpoints for managing users")
@RestController
@RequestMapping(path = "/admin/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/username/{email}")
    public ResponseEntity<FetchUserDTO> create(@PathVariable final String email) {
        FetchUserDTO user = this.service.findByEmail(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
