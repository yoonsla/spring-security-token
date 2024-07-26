package com.sy.springsecuritytoken.user.domain;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor
public class AccountDetail implements UserDetails {

    private Account account;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return account.getIsEnable();
    }

    @Override
    public boolean isAccountNonLocked() {
        return account.getIsEnable();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return account.getIsEnable();
    }

    @Override
    public boolean isEnabled() {
        return account.getIsEnable();
    }
}
