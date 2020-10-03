package ru.library.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.library.domain.dto.LoginDTO;
import ru.library.domain.dto.RefreshDTO;
import ru.library.domain.response.ResponseWrapper;
import ru.library.exception.ClientErrorException;
import ru.library.service.TokenProvider;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    @PostMapping(value = "/token")
    public ResponseEntity<ResponseWrapper> getTokens(@Valid @RequestBody LoginDTO dto) throws ClientErrorException {
        return ResponseEntity.ok(ResponseWrapper.data(tokenProvider.createTokens(dto)));
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<ResponseWrapper> refreshToken(@Valid @RequestBody RefreshDTO dto) throws ClientErrorException {
        return ResponseEntity.ok(ResponseWrapper.data(tokenProvider.refreshToken(dto)));
    }
}
