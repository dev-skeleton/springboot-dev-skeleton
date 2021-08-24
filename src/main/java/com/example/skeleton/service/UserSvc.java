package com.example.skeleton.service;

import com.example.skeleton.component.jwt.JwtTokenProvider;
import com.example.skeleton.exception.AuthException;
import com.example.skeleton.exception.ClientSideException;
import com.example.skeleton.exception.ServerSideException;
import com.example.skeleton.model.dto.LoginInfo;
import com.example.skeleton.model.dto.LoginResult;
import com.example.skeleton.model.entity.User;
import com.example.skeleton.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class UserSvc extends CrudSvc<User, Long> implements UserDetailsService {

    static private final Logger logger = LoggerFactory.getLogger(UserSvc.class);

    private final UserRepo userRepo;
    private final RoleSvc roleSvc;
    private final PasswordEncoder passwordEncoder;

    public UserSvc(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider, RoleSvc roleSvc) {
        super(userRepo, userRepo);
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleSvc = roleSvc;
    }

    @Override
    @Validated
    public UserDetails loadUserByUsername(@NotBlank String username) throws UsernameNotFoundException {
        var user = userRepo.findUserByNameEquals(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("user [%s] not found", username));
        }
        return user;
    }

    @Override
    public void Merge(User that) {
        if (that.getPassword() != null) {
            that.setPassword(passwordEncoder.encode(that.getPassword()));
        }
        if (that.getGrantedRoles() != null) {
            that.setGrantedRoles(that.getGrantedRoles().stream().map(roleSvc::Refresh).collect(Collectors.toSet()));
        }
        super.Merge(that);
    }

    @Validated
    public User GetUserByName(@NotNull String name) {
        var user = userRepo.findUserByNameEquals(name);
        if (user == null) {
            throw ClientSideException.NotFound(String.format("user [%s] not found", name));
        }
        return user;
    }

    @Override
    public void Create(User u) {
        u.setPasswordUpdateTimestamp(new Date());
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        u.setLocked(false);
        super.Create(u);
    }

    @Validated
    public LoginResult login(@NotBlank LoginInfo info) {
        User user;
        try {
            user = this.GetUserByName(info.getUser());
        } catch (ClientSideException ce) {
            throw AuthException.WrongUserOrPassword();
        } catch (Exception e) {
            throw ServerSideException.InternalError(e.getMessage()).CausedBy(e);
        }
        if (!passwordEncoder.matches(info.getPassword(), user.getPassword())) {
            throw AuthException.WrongUserOrPassword();
        }
        if (!user.isAccountNonLocked()) {
            throw AuthException.AccountLocked();
        }
        if (!user.isCredentialsNonExpired()) {
            throw AuthException.PasswordExpired();
        }
        return LoginResult.builder().token(JwtTokenProvider.issue(user)).user(user).build();
    }

}
