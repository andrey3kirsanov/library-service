package ru.library.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.library.domain.dto.UserDTO;
import ru.library.domain.response.ResponseWrapper;
import ru.library.exception.ClientErrorException;
import ru.library.service.UserService;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseWrapper> registerUser(@Valid @RequestBody UserDTO dto) throws ClientErrorException {
        return ResponseEntity.ok(ResponseWrapper.data(userService.registerUser(dto)));
    }
}
