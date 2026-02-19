package com.ejemplo.agenda.controladores;

import com.ejemplo.agenda.seguridad.Constans;
import com.ejemplo.agenda.seguridad.JWTAuthenticationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
public class LoginController {

    @Autowired
    JWTAuthenticationConfig jwtAuthtenticationConfig;

    @PostMapping("login")
    public String login(
            @RequestParam("user") String username,
            @RequestParam("encryptedPass") String encryptedPass) {
        if (!(username.equals(Constans.USER) && encryptedPass.equals(Constans.PASS))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials");
        }
        String token = jwtAuthtenticationConfig.getJWTToken(username);
        return token;
    }
}
