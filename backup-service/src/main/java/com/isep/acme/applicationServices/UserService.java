package com.isep.acme.applicationServices;

import com.isep.acme.adapters.dto.FetchUserDTO;
import com.isep.acme.adapters.dto.UserForServicesDTO;
import com.isep.acme.applicationServices.interfaces.repositories.IUserRepository;
import com.isep.acme.domain.aggregates.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;

    @Transactional
    public List<FetchUserDTO> findAll() {
        List<User> users = this.userRepository.findAll();
        return this.usersListToDTO(users);
    }

    @Transactional
    public List<UserForServicesDTO> findAllForServices() {
        List<User> users = this.userRepository.findAll();
        return this.usersListToServicesDTO(users);
    }

    private List<FetchUserDTO> usersListToDTO(List<User> users) {
        List<FetchUserDTO> userDTOS = new ArrayList<>();

        for (User u : users) {
            FetchUserDTO userDTO = new FetchUserDTO(u.getId(), u.getEmail(), u.getFullName(), u.getNif(),
                    u.getAddress(), u.getRole().name());
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }

    private List<UserForServicesDTO> usersListToServicesDTO(List<User> users) {
        List<UserForServicesDTO> userDTOS = new ArrayList<>();

        for (User u : users) {
            UserForServicesDTO userDTO = new UserForServicesDTO(u.getId(), u.getEmail(), u.getPassword(),
                    u.getFullName(), u.getNif(), u.getAddress(), u.getRole().name());
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }
}
