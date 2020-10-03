package ru.library.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.library.domain.User;
import ru.library.domain.dto.UserDTO;
import ru.library.domain.response.ErrorCode;
import ru.library.exception.ClientErrorException;
import ru.library.repository.UserRepository;

import java.time.Instant;

import static ru.library.util.Constants.PASSWORD_SALT;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    @Qualifier("passwordEncoder")
    private final PasswordEncoder passwordEncoder;

    /**
     * Register new user
     * @param dto
     * @return
     * @throws ClientErrorException
     */
    @Transactional
    public User registerUser(UserDTO dto) throws ClientErrorException {
        if (!passwordService.checkValidPassword(dto.getPassword())) {
            throw new ClientErrorException(ErrorCode.PASSWORD_NOT_VALID,
                    "The password should contain at least 8 symbols maximum 32, uppercase, lowercase,"
                            + " digits and special symbols",
                    "The password should contain at least 8 symbols maximum 32, uppercase, lowercase,"
                            + " digits and special symbols");
        }

        String encodedPassword = passwordEncoder.encode(PASSWORD_SALT + dto.getPassword());

        User user = User.builder()
                .username(dto.getUsername()).firstName(dto.getFirstName()).lastName(dto.getLastName())
                .email(dto.getEmail()).password(encodedPassword).createdDate(Instant.now()).build();
        return userRepository.save(user);
    }
}
