package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;

import java.sql.SQLException;
import java.util.Collection;

public interface Store {

    Collection<Ticket> getTickets();

    Account findByEmailOrPhone(String email, String phone);

    boolean saveAccount(Account account);

    boolean saveTicket(Ticket ticket) throws SQLException;
}