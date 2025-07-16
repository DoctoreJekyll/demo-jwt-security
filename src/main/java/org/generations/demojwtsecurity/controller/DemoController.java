package org.generations.demojwtsecurity.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class DemoController {


    @GetMapping(value = "/user/hola")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String hola(@AuthenticationPrincipal UserDetails user) {
        return user.getUsername() + " Estas autenticado como: " + user.getAuthorities().toString();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/admin/hola")
    public String holaAdmin(@AuthenticationPrincipal UserDetails user) {
        return user.getUsername() + " Estas autenticado como: " + user.getAuthorities().toString();
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or isAnonymous()")
    @GetMapping(value = "/public/hola")
    public String holaPublic() {
        return " Hola desde un endpointPublico";
    }

}
