package org.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");

        String timezone = req.getParameter("timezone");
        if (timezone == null || timezone.isEmpty()) {
            timezone = "UTC";
        }

        try (PrintWriter out = resp.getWriter()) {
            ZoneId zoneId;
            try {
                zoneId = ZoneId.of(timezone);
            } catch (Exception e) {
                // Обробка некоректного часового поясу
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("<html><body>");
                out.write("<h1>Invalid timezone</h1>");
                out.write("</body></html>");
                return;
            }

            ZonedDateTime now = ZonedDateTime.now(zoneId);
            String formattedTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z").format(now);

            // Вивід HTML-відповіді з поточним часом
            out.write("<html><body>");
            out.write("<h1>Current Time</h1>");
            out.write("<p>" + formattedTime + "</p>");
            out.write("</body></html>");
        } catch (IOException e) {
            throw new ServletException("Error processing the request", e);
        }
    }
}