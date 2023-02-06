package com.project.myboard.user.service;

import com.project.myboard.exception.BadRequestException;
import com.project.myboard.user.data.dto.UserDto;
import com.project.myboard.user.data.entity.User;
import com.project.myboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        UserDto.AuthenticationDto authenticationDto = getAuthenticationDtoByUid(uid);
        return new UserDetailsImpl(authenticationDto);
    }

    private UserDto.AuthenticationDto getAuthenticationDtoByUid(String uid) {
        User user;
        try{
            user = userRepository.getByUid(uid);
        } catch (BadRequestException e) {
            throw new BadRequestException("존재하지 않는 ID 입니다.");
        }
        List<String> roles = new ArrayList<>();
        roles.add(user.getUid());
        return UserDto.AuthenticationDto.builder()
                .uid(user.getUid())
                .password(user.getPassword())
                .roles(roles)
                .build();
    }
}
