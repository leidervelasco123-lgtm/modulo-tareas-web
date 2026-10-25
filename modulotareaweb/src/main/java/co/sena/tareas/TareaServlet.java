package co.sena.tareas;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class TareaServlet extends HttpServlet {

    private static final ConcurrentMap<String, Cuenta> CUENTAS = new ConcurrentHashMap<>();
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int ITERACIONES = 120_000;

    private record Cuenta(String nombre, byte[] sal, byte[] hash) {
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        mostrarPagina(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");
        String correo = normalizar(request.getParameter("correo"));
        String contrasena = request.getParameter("contrasena");

        try {
            if ("registrar".equals(accion)) {
                registrar(request, correo, contrasena);
            } else if ("ingresar".equals(accion)) {
                iniciarSesion(request, correo, contrasena);
            } else {
                request.setAttribute("mensaje", "Acci&oacute;n no reconocida.");
            }
        } catch (GeneralSecurityException e) {
            throw new ServletException("No se pudo procesar la contraseña.", e);
        }

        mostrarPagina(request, response);
    }

    private void registrar(
            HttpServletRequest request,
            String correo,
            String contrasena)
            throws GeneralSecurityException {

        String nombre = request.getParameter("nombre");

        if (nombre == null || nombre.trim().isEmpty()
                || correo.isEmpty()
                || contrasena == null
                || contrasena.length() < 8) {
            request.setAttribute(
                    "mensaje",
                    "Ingresa nombre, correo y una contrase&ntilde;a de al menos 8 caracteres.");
            return;
        }

        byte[] sal = new byte[16];
        RANDOM.nextBytes(sal);

        Cuenta cuenta = new Cuenta(
                nombre.trim(),
                sal,
                calcularHash(contrasena.toCharArray(), sal));

        if (CUENTAS.putIfAbsent(correo, cuenta) != null) {
            request.setAttribute("mensaje", "Ese correo ya tiene una cuenta.");
        } else {
            request.setAttribute(
                    "mensaje",
                    "Registro exitoso. Ya puedes iniciar sesi&oacute;n.");
        }
    }

    private void iniciarSesion(
            HttpServletRequest request,
            String correo,
            String contrasena)
            throws GeneralSecurityException {

        Cuenta cuenta = CUENTAS.get(correo);

        if (cuenta == null || contrasena == null) {
            request.setAttribute("mensaje", "Correo o contrase&ntilde;a incorrectos.");
            return;
        }

        byte[] hashIngresado = calcularHash(
                contrasena.toCharArray(),
                cuenta.sal());

        if (MessageDigest.isEqual(cuenta.hash(), hashIngresado)) {
            request.getSession().setAttribute("usuario", cuenta.nombre());
            request.setAttribute(
                    "mensaje",
                    "Bienvenido, " + cuenta.nombre() + ".");
        } else {
            request.setAttribute("mensaje", "Correo o contrase&ntilde;a incorrectos.");
        }
    }

    private byte[] calcularHash(char[] contrasena, byte[] sal)
            throws GeneralSecurityException {

        PBEKeySpec especificacion = new PBEKeySpec(contrasena, sal, ITERACIONES, 256);

        try {
            return SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(especificacion)
                    .getEncoded();
        } finally {
            especificacion.clearPassword();
        }
    }

    private String normalizar(String valor) {
        return valor == null ? "" : valor.trim().toLowerCase();
    }

    private void mostrarPagina(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("/WEB-INF/vistas/auth.jsp")
                .forward(request, response);
    }
}