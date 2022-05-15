package com.foodpanda.FoodpandaApp.security.services;

import com.foodpanda.FoodpandaApp.model.Admin;
import com.foodpanda.FoodpandaApp.model.Customer;
import com.foodpanda.FoodpandaApp.model.User;
import com.foodpanda.FoodpandaApp.model.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final User user;
    private final Collection<GrantedAuthority> authorities;

    public UserDetailsImpl(Admin admin) {
        this.user = admin;
        this.authorities = List.of(new SimpleGrantedAuthority(UserType.ADMIN.toString()));
    }

    public UserDetailsImpl(Customer customer) {
        this.user = customer;
        this.authorities = List.of(new SimpleGrantedAuthority(UserType.CUSTOMER.toString()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFullName(){
        return user.getFirstName() + " " + user.getLastName();
    }

    public User getUser() {
        return user;
    }
}

