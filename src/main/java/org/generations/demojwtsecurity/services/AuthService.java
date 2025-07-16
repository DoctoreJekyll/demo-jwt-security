package org.generations.demojwtsecurity.services;

import lombok.RequiredArgsConstructor;
import org.generations.demojwtsecurity.dto.request.LoginRequestDTO;
import org.generations.demojwtsecurity.dto.request.RegisterRequestDTO;
import org.generations.demojwtsecurity.dto.response.AuthResponse;
import org.generations.demojwtsecurity.model.Role;
import org.generations.demojwtsecurity.model.User;
import org.generations.demojwtsecurity.repo.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtServices jwtServices;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    //Construimos un usuario con todos los valores dados desde nuestras inyecciones
    //Guardamos el usuario en persistencia.
    //Esta clase la usamos para REGISTRAR
    public String register(RegisterRequestDTO requestDTO) {
        User user = User.builder()
                .username(requestDTO.getUsername())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .firstname(requestDTO.getFirstName())
                .address(requestDTO.getAddress())
                .role(Role.valueOf(requestDTO.getRole().toUpperCase()))
                .build();

        userRepository.save(user);

        return "Registro como " + user.getAuthorities().toString() + "realizado con éxito";
    }

    //Aqui es donde al hacer el log le damos el token de uso al usuario que en el caso de nuestra app le permitira estar registrado durante 4h
    public AuthResponse login(LoginRequestDTO requestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword()));
        UserDetails user = userRepository.findByUsername(requestDTO.getUsername()).orElseThrow();
        return AuthResponse.builder()
                .token(jwtServices.generateToken(user))
                .response("Logeado con exito, no pierda su pulserita durante estas 4 horas")
                .build();
    }

}
