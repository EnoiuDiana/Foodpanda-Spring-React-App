package com.foodpanda.FoodpandaApp.security.services;

import com.foodpanda.FoodpandaApp.model.Admin;
import com.foodpanda.FoodpandaApp.model.Customer;
import com.foodpanda.FoodpandaApp.model.User;
import com.foodpanda.FoodpandaApp.repository.UserRepository;
import com.foodpanda.FoodpandaApp.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if(user instanceof Admin) return new UserDetailsImpl((Admin)user);
        else return new UserDetailsImpl((Customer)user);

    }
}
