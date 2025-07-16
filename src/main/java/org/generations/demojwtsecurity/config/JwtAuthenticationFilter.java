package org.generations.demojwtsecurity.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.generations.demojwtsecurity.services.JwtServices;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServices jwtService;
    private final UserDetailsService userDetailsService;

    //Esta clase es la que hace de intermedio entre el token que generamos cuando hacemos el login(porque por defecto spring no sabe trabajar con jwt) y el security
    //Cada vez el security por cada endpoint que entremos(enlace web) va a comprobar este token para dejarte entrar o no.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //1. verificar que el token existe. Si no existe seguir la cadena de filtro pero impedir el acceso
        final String token = getTokenFromRequest(request);

        /*
        Si no hay token JWT en la cabecera (Authorization), dejamos que los filtros de Spring sigan su curso.
        Pero no autenticamos al usuario (no metemos nada en el SecurityContextHolder).
        Entonces, si el endpoint es público (/login, /register), no pasa nada: el request llega.
        Pero si el endpoint está protegido, Spring Security lo bloqueará más adelante (porque no habrá usuario autenticado en el contexto).
         */
        if(token == null){
            filterChain.doFilter(request,response);
            return;
        }

        //2. extraer el username del token
        String username = jwtService.extractUsername(token);

        //3. con el username buscar en el security context si es que ya existe, no hay nada que hacer, salvo continuar la cadena de filtros
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //4. si no esta en contexto de seguridad ver si existe en nuestra BBDD de usuarios
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            //5. verificar que el token sea valido
            if(jwtService.isTokenValid(token, userDetails)){
                //6. crear el objeto authentication y meterlo en security context
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                //Esta linea es la que busca las funciones internas del navegador cuando entras a una web como tu ip, servidor de internet, tu sesion, etc.
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //Aqui dejamos entrar
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            /*
                request	Petición del cliente (headers, body, etc.)
                response	Respuesta que se enviará al cliente
                doFilter(request, response)	Sigue con los siguientes filtros o llega al controlador si no hay más
             */

            //Response es como la respuesta del nevegador que te dice SI O NO a conectarte a los diferentes endpoints(enlaces web)
            filterChain.doFilter(request,response);
        }

    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
