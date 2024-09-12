package com.isep.acme.applicationServices;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.dto.CreateUserDTO;
import com.isep.acme.adapters.dto.FetchUserDTO;
import com.isep.acme.adapters.dto.UpdateUserDTO;
import com.isep.acme.applicationServices.interfaces.repositories.IUserRepository;
import com.isep.acme.domain.aggregates.user.User;
import com.isep.acme.domain.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("UserServiceNew")
public class UserService {

    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void create(CreateUserDTO createUserDTO) throws DatabaseException {
        User user = new User(createUserDTO.getEmail(), createUserDTO.getPassword(), createUserDTO.getFullName(),
                createUserDTO.getNif(), createUserDTO.getAddress(), RoleEnum.valueOf(createUserDTO.getRole()));

        this.userRepository.create(user);
    }

    @Transactional
    public void update(UpdateUserDTO updateUserDTO) throws DatabaseException {
        User user = this.userRepository.findByEmail(updateUserDTO.getEmail());
        user.setEmail(updateUserDTO.getEmail());
        user.setAddress(updateUserDTO.getAddress());
        user.setNif(updateUserDTO.getNif());
        user.setFullName(updateUserDTO.getFullName());
        user.setPassword(updateUserDTO.getPassword());
        this.userRepository.update(user);
    }

    @Transactional
    public FetchUserDTO findByEmail(String email) {
        User user = this.userRepository.findByEmail(email);
        return new FetchUserDTO(user.getId(), user.getEmail(), user.getFullName(), user.getNif(), user.getAddress(),
                user.getRole().name());
    }
}
