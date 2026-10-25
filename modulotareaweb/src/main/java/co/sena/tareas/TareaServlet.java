package co.sena.tareas;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TareaServlet extends HttpServlet {

    private final List<String> tareas = new CopyOnWriteArrayList<>();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("tareas", tareas);
        request.getRequestDispatcher("/WEB-INF/vistas/tareas.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        String descripcion = request.getParameter("descripcion");

        if (descripcion != null && !descripcion.trim().isEmpty()) {
            tareas.add(descripcion.trim());
        }

        response.sendRedirect(request.getContextPath() + "/tareas");
    }
}