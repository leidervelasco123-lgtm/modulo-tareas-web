<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Registro e inicio de sesi&oacute;n</title>
        </head>

        <body>
            <h1>Registro e inicio de sesi&oacute;n</h1>

            <c:if test="${not empty mensaje}">
                <p>
                    <c:out value="${mensaje}" escapeXml="false" />
                </p>
            </c:if>

            <c:if test="${not empty sessionScope.usuario}">
                <p>Sesi&oacute;n iniciada para:
                    <c:out value="${sessionScope.usuario}" />
                </p>
            </c:if>

            <h2>Crear cuenta</h2>
            <form action="/modulotareasweb/autenticacion" method="post">
                <input type="hidden" name="accion" value="registrar">

                <label for="nombre">Nombre:</label>
                <input type="text" id="nombre" name="nombre" maxlength="100" required>

                <label for="correoRegistro">Correo:</label>
                <input type="email" id="correoRegistro" name="correo" required>

                <label for="contrasenaRegistro">Contrase&ntilde;a (m&iacute;nimo 8 caracteres):</label>
                <input type="password" id="contrasenaRegistro" name="contrasena" minlength="8" required>

                <button type="submit">Registrarme</button>
            </form>

            <h2>Iniciar sesi&oacute;n</h2>
            <form action="/modulotareasweb/autenticacion" method="post">
                <input type="hidden" name="accion" value="ingresar">

                <label for="correoIngreso">Correo:</label>
                <input type="email" id="correoIngreso" name="correo" required>

                <label for="contrasenaIngreso">Contrase&ntilde;a:</label>
                <input type="password" id="contrasenaIngreso" name="contrasena" required>

                <button type="submit">Ingresar</button>
            </form>
        </body>

        </html>