# Autenticación y Autorización con JWT

Aplicación de agenda con autenticación y autorización basada en JWT (JSON Web Tokens) usando Spring Boot y Spring Security.

## Descripción

Esta aplicación implementa un sistema de gestión de contactos con seguridad mediante tokens JWT. Proporciona endpoints REST protegidos que requieren autenticación mediante un token válido.

## Tecnologías Utilizadas

- **Spring Boot 3.2.0** - Framework principal
- **Spring Security** - Gestión de autenticación y autorización
- **JJWT 0.12.3** - Librería para crear y validar tokens JWT
- **Maven** - Gestión de dependencias y construcción del proyecto
- **Java 17** - Lenguaje de programación

## Estructura del Proyecto

```
src/main/java/com/ejemplo/agenda/
├── AgendaApplication.java          # Clase principal de Spring Boot
├── entidades/
│   └── Contacto.java               # Entidad Contacto
├── controladores/
│   ├── ContactoController.java     # Controlador REST para contactos
│   └── LoginController.java        # Controlador de autenticación
└── seguridad/
    ├── Constans.java               # Constantes de seguridad
    ├── JWTAuthenticationConfig.java # Configuración de autenticación JWT
    ├── JWTAuthorizationFilter.java  # Filtro de autorización JWT
    └── WebSecurityConfig.java       # Configuración de Spring Security
```

## Funcionalidades

### Autenticación

- **Endpoint de Login**: `POST /login`
  - Parámetros: `user`, `encryptedPass`
  - Retorna un token JWT válido por 10 días
  - Credenciales por defecto:
    - Usuario: `aitor`
    - Contraseña: `1234`

### Gestión de Contactos (Protegidos)

Todos los siguientes endpoints requieren un token JWT válido en el header `token`:

- `GET /contactos` - Listar todos los contactos
- `GET /contactos/{id}` - Obtener un contacto por ID
- `POST /contactos` - Crear un nuevo contacto
- `PUT /contactos/{id}` - Actualizar un contacto
- `DELETE /contactos/{id}` - Eliminar un contacto

## Uso

### 1. Compilar el proyecto

```bash
mvn clean compile
```

### 2. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación se iniciará en `http://localhost:8080`

### 3. Autenticarse

```bash
curl -X POST "http://localhost:8080/login?user=aitor&encryptedPass=1234"
```

Respuesta:
```
Bearer eyJhbGciOiJIUzUxMiJ9...
```

### 4. Acceder a endpoints protegidos

```bash
TOKEN="Bearer eyJhbGciOiJIUzUxMiJ9..."
curl -X GET "http://localhost:8080/contactos" -H "token: $TOKEN"
```

## Seguridad

### Características de Seguridad

- **Autenticación basada en JWT**: Tokens firmados con HMAC SHA-512
- **Expiración de tokens**: 10 días de validez
- **Autorización por roles**: ROLE_USER asignado a usuarios autenticados
- **Endpoints protegidos**: Todos los endpoints excepto `/login` requieren autenticación
- **CSRF deshabilitado**: Apropiado para APIs REST stateless con JWT

### Notas de Seguridad

⚠️ **IMPORTANTE**: Esta implementación es para propósitos educativos. En producción:

1. **NO hardcodear credenciales** en el código fuente
2. **Usar variables de entorno** para claves secretas y credenciales
3. **Implementar almacenamiento de usuarios** en base de datos
4. **Usar contraseñas hasheadas** (BCrypt, Argon2, etc.)
5. **Considerar HTTPS** para todas las comunicaciones
6. **Implementar rate limiting** en el endpoint de login
7. **Añadir refresh tokens** para mejorar la experiencia de usuario

## Testing

Ejecutar los tests:

```bash
mvn test
```

## Ejemplo de Uso Completo

```bash
# 1. Obtener token
TOKEN=$(curl -s -X POST "http://localhost:8080/login?user=aitor&encryptedPass=1234")

# 2. Listar contactos
curl -X GET "http://localhost:8080/contactos" -H "token: $TOKEN"

# 3. Crear un contacto
curl -X POST "http://localhost:8080/contactos" \
  -H "token: $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Pedro López","telefono":"555-1234","email":"pedro@example.com"}'

# 4. Obtener un contacto específico
curl -X GET "http://localhost:8080/contactos/1" -H "token: $TOKEN"
```

## Arquitectura de Seguridad

### Flujo de Autenticación

1. Cliente envía credenciales a `/login`
2. `LoginController` valida las credenciales
3. Si son válidas, `JWTAuthenticationConfig` genera un token JWT
4. Token es retornado al cliente
5. Cliente incluye el token en el header `token` para peticiones subsecuentes

### Flujo de Autorización

1. Cliente envía petición con token en header `token`
2. `JWTAuthorizationFilter` intercepta la petición
3. Valida el token JWT
4. Si es válido, extrae las autoridades y establece la autenticación en el contexto de Spring Security
5. La petición continúa a través de la cadena de filtros
6. Si el token es inválido o ha expirado, se retorna error 403 Forbidden

## Configuración

### Constantes de Seguridad

Las constantes se encuentran en `Constans.java`:

- `LOGIN_URL`: "/login"
- `HEADER_AUTHORIZACION_KEY`: "token"
- `TOKEN_BEARER_PREFIX`: "Bearer "
- `TOKEN_EXPIRATION_TIME`: 864_000_000 ms (10 días)

## Licencia

Este proyecto es para fines educativos.
