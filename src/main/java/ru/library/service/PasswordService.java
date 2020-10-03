package ru.library.service;

import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class PasswordService {

    private PasswordValidator passwordValidator;

    @PostConstruct
    private void init() {
        passwordValidator = new PasswordValidator(
                new LengthRule(8, 32),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1)
        );
    }

    /**
     * Validates password, should be strong
     * @param password
     * @return - true if password is valid, false if not
     */
    public Boolean checkValidPassword(String password) {
        PasswordData passwordData = new PasswordData(password);
        RuleResult validate = passwordValidator.validate(passwordData);
        return validate.isValid();
    }
}
