<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Gesti&oacute;n de tareas</title>
        </head>

        <body>
            <h1>M&oacute;dulo de gesti&oacute;n de tareas</h1>

            <h2>Registrar una tarea</h2>
            <form action="/modulotareasweb/tareas" method="post">
                <label for="descripcion">Descripci&oacute;n:</label>
                <input type="text" id="descripcion" name="descripcion" maxlength="200" required>
                <button type="submit">Agregar tarea</button>
            </form>

            <h2>Tareas registradas</h2>
            <c:choose>
                <c:when test="${empty tareas}">
                    <p>A&uacute;n no hay tareas registradas.</p>
                </c:when>
                <c:otherwise>
                    <ul>
                        <c:forEach var="tarea" items="${tareas}">
                            <li>
                                <c:out value="${tarea}" />
                            </li>
                        </c:forEach>
                    </ul>
                </c:otherwise>
            </c:choose>

            <p>
                <a href="/modulotareasweb/index.html">Volver al inicio</a>
            </p>
        </body>

        </html>