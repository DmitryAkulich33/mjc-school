package com.epam.esm.service.impl;

import com.epam.esm.domain.User;
import com.epam.esm.exceptions.AuthenticationDataException;
import com.epam.esm.security.JwtTokenProvider;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    private final String LOGIN = "user";
    private final String PASSWORD = "user";

    @Mock
    private JwtTokenProvider mockJwtTokenProvider;
    @Mock
    private UserService mockUserService;
    @Mock
    private AuthenticationManager mockAuthenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    public void testLogin() {
        User user = new User();
        String expected = "token";

        when(mockAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(LOGIN, PASSWORD))).thenReturn(null);
        when(mockUserService.getUserByLogin(LOGIN)).thenReturn(user);
        when(mockJwtTokenProvider.createToken(LOGIN, user.getRoles())).thenReturn(expected);

        String actual = authenticationService.login(LOGIN, PASSWORD);

        assertEquals(expected, actual);
    }

    @Test
    public void testLogin_AuthenticationDataException() {
        when(mockAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(LOGIN, PASSWORD))).thenThrow(new AuthenticationDataException());

        assertThrows(AuthenticationDataException.class, () -> {
            authenticationService.login(LOGIN, PASSWORD);
        });
    }
}