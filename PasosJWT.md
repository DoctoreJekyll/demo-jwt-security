
# 🛠️ PASOS PARA IMPLEMENTAR JWT EN SPRING BOOT

---

## ✅ 1. Modelo de usuario (User + Role)

- User implementa UserDetails
- Enum Role (USER, ADMIN, etc.)
- Campos: username, password, role, email, etc.

---

## ✅ 2. Repositorio

- UserRepository con método findByUsername

---

## ✅ 3. DTOs (para Login y Registro)

- LoginRequest → username + password
- RegisterRequest → datos para registrar
- AuthResponse → el token JWT

---

## ✅ 4. JwtService

- Generar tokens
- Validar tokens
- Extraer claims (username, fechas, etc.)

---

## ✅ 5. ApplicationConfig

Define beans:

- AuthenticationManager
- UserDetailsService
- PasswordEncoder (ej. BCryptPasswordEncoder)

---

## ✅ 6. AuthService

- register(request) → guarda usuario nuevo 
- authenticate(request) → verifica login y devuelve token

---

## ✅ 7. AuthController

- POST /register
- POST /login

---

## ✅ 8. JwtAuthenticationFilter

- Lee el JWT del header Authorization: Bearer ...
- Valida el token
- Si es válido, pone el usuario autenticado en SecurityContext

---

## ✅ 9. SecurityConfig

- Configura el SecurityFilterChain
- Indica qué rutas son públicas (/login, /register)
- Aplica el filtro JWT

---

## ✅ 10. Controlador protegido (DemoController)

- Cualquier endpoint que requiera token (por ejemplo /demo)

---