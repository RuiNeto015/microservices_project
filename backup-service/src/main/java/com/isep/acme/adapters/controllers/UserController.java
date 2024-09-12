package com.isep.acme.adapters.controllers;

import com.isep.acme.adapters.dto.FetchUserDTO;
import com.isep.acme.adapters.dto.UserForServicesDTO;
import com.isep.acme.applicationServices.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Tag(name = "User", description = "Endpoints for managing users")
@RestController
@RequestMapping(path = "/admin/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(summary = "gets all users")
    @GetMapping
    public ResponseEntity<List<FetchUserDTO>> getUsers() {
        List<FetchUserDTO> users = this.service.findAll();
        return ResponseEntity.ok().body(users);
    }

    @Operation(summary = "gets all users for all services")
    @GetMapping("/init/allServices")
    public ResponseEntity<List<UserForServicesDTO>> getAllUsersForServices() {
        List<UserForServicesDTO> usersToSend = this.service.findAllForServices();
        return ResponseEntity.ok().body(usersToSend);
    }
}
