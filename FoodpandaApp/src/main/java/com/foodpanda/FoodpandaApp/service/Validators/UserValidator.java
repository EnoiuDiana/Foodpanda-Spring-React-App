package com.foodpanda.FoodpandaApp.service.Validators;

import com.foodpanda.FoodpandaApp.model.Customer;
import com.foodpanda.FoodpandaApp.model.User;
import com.foodpanda.FoodpandaApp.repository.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private boolean isUniqueEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        return user==null;
    }

    private boolean validateEmail(String email){
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    private boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }


    public boolean validateUser(String email, String password) throws Exception {
        try {
            assert isUniqueEmail(email);
            assert validateEmail(email);
            assert validatePassword(password);
            return true;
        }catch (Exception e) {
            throw new Exception("User data not valid");
        }
    }
}
