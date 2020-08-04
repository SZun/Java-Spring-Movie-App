package com.sgz.server.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class UsernameAndPasswordAuthenticationRequest {

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

}
