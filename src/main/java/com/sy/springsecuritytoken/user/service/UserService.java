package com.sy.springsecuritytoken.user.service;

import com.sy.springsecuritytoken.exception.ApplicationException;
import com.sy.springsecuritytoken.response.ResponseCode;
import com.sy.springsecuritytoken.user.controller.command.SignUpCommand;
import com.sy.springsecuritytoken.user.domain.Account;
import com.sy.springsecuritytoken.user.repository.UserRepository;
import com.sy.springsecuritytoken.user.service.dto.SignUpDto;
import com.sy.springsecuritytoken.user.service.dto.UserListDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public SignUpDto signUp(SignUpCommand command) {
        Optional<Account> account = userRepository.findByEmail(command.getEmail());
        if (account.isPresent()) {
            throw new ApplicationException(ResponseCode.ALREADY_EXIST);
        }
        final String password = passwordEncoder.encode(command.getPassword());
        final Account saved = Account.of(command.getEmail(), password, command.getUserRole());
        return SignUpDto.from(userRepository.save(saved));
    }

    public List<UserListDto> findAll() {
        return userRepository.findAll().stream()
            .map(UserListDto::from)
            .toList();
    }
}
