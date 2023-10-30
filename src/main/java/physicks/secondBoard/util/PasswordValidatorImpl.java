package physicks.secondBoard.util;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidatorImpl implements PasswordValidator{

    @Override
    public boolean isValidPassword(String password) {
        return false;
    }
}
