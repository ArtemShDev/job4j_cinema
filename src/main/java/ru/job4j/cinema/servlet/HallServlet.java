package ru.job4j.cinema.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.store.DbStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class HallServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        String json = GSON.toJson(DbStore.instOf().getTickets());
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String cell = req.getParameter("cell");
        String email = req.getParameter("email");
        if (req.getParameter("phone") != null) {
            Account acc = DbStore.instOf().findByEmailOrPhone(email, req.getParameter("phone"));
            if (acc == null) {
                acc = new Account(req.getParameter("username"), email, req.getParameter("phone"));
                DbStore.instOf().saveAccount(acc);
            }
            Ticket ticket = new Ticket(1, Integer.parseInt(req.getParameter("row")), Integer.parseInt(cell),
                    acc.getId(), Integer.parseInt(req.getParameter("price")));
            try {
                DbStore.instOf().saveTicket(ticket);
                resp.sendRedirect(req.getContextPath() + "/index.html");
            } catch (SQLException throwables) {
                resp.setContentType("text/html; charset=utf-8");
                resp.sendRedirect(req.getContextPath() + "/payment.html?err=Ticket is already booked");
            }
        }
    }
}