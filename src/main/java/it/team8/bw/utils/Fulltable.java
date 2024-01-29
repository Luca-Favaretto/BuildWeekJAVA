package it.team8.bw.utils;

import com.github.javafaker.Faker;
import it.team8.bw.abstractClass.Means;
import it.team8.bw.abstractClass.TicketIssue;
import it.team8.bw.abstractClass.TicketOffice;
import it.team8.bw.dao.MeansDAO;
import it.team8.bw.dao.RoadsDAO;
import it.team8.bw.dao.TicketIssueDAO;
import it.team8.bw.dao.TicketOfficeDAO;
import it.team8.bw.entities.means.Autobus;
import it.team8.bw.entities.means.Maintenance;
import it.team8.bw.entities.means.Tram;
import it.team8.bw.entities.road.Draft;
import it.team8.bw.entities.road.Stop;
import it.team8.bw.entities.sellers.AuthorizatedSellers;
import it.team8.bw.entities.sellers.VendingMachine;
import it.team8.bw.entities.users.Subscription;
import it.team8.bw.entities.users.Ticket;
import it.team8.bw.entities.users.User;
import it.team8.bw.enums.MeansStatus;
import it.team8.bw.enums.SubscriptionType;
import it.team8.bw.enums.VendingMachineStatus;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

public class Fulltable {
    public static Supplier<Draft> newDraft = () -> {
        Faker faker = new Faker(Locale.ENGLISH);
        Random rnd = new Random();
        Draft draft = new Draft(faker.address().city(), faker.address().city());
        return draft;
    };
    public static Supplier<Means> newAutobus = () -> {
        Faker faker = new Faker(Locale.ENGLISH);
        Random rndm = new Random();
        return new Autobus(MeansStatus.SERVICE);
    };
    public static Supplier<Means> newTram = () -> {
        Faker faker = new Faker(Locale.ENGLISH);
        Random rndm = new Random();
        return new Tram(MeansStatus.SERVICE);
    };
    public static Supplier<User> newUser = () -> {
        Faker faker = new Faker(Locale.ENGLISH);
        return new User(faker.name().firstName(), faker.name().lastName());
    };
    public static Supplier<Ticket> newTicket = () -> {
        return new Ticket(LocalDate.now());
    };
    public static Supplier<VendingMachine> newVendingMachine = () -> {
        Faker faker = new Faker(Locale.ENGLISH);
        return new VendingMachine(faker.rockBand().name(), VendingMachineStatus.ACTIVE);
    };
    public static Supplier<AuthorizatedSellers> newAuthorizatedSellers = () -> {
        Faker faker = new Faker(Locale.ENGLISH);
        return new AuthorizatedSellers(faker.animal().name());
    };

    public static Supplier<Subscription> newSubscription(User user) {
        return () -> new Subscription(LocalDate.now(), SubscriptionType.WEEKLY, LocalDate.now(), user);
    }

    public static Supplier<Maintenance> newMaintenance(Means means) {
        return () -> {
            LocalDate now = LocalDate.now();
            Random rndm = new Random();
            return new Maintenance(now.minusDays(rndm.nextInt(200, 250)), now.minusDays(rndm.nextInt(180, 200)), means);
        };
    }

    public static Supplier<Stop> newStop(Draft draft) {
        return () -> {
            Faker faker = new Faker(Locale.ENGLISH);
            return new Stop(faker.address().firstName(), faker.address().firstName(), draft);
        };
    }

    public static void creation(MeansDAO meansDAO, RoadsDAO roadsDAO, TicketIssueDAO ticketIssueDAO, TicketOfficeDAO ticketOfficeDAO) {

        Draft draft = newDraft.get();
        roadsDAO.saveDraft(draft);

        Draft draft2 = newDraft.get();
        roadsDAO.saveDraft(draft2);

        for (int i = 0; i < 3; i++) {
            newStop(draft);
            newStop(draft2);
        }


        Means autobus = newAutobus.get();
        meansDAO.saveMeans(autobus);

        Means tram = newTram.get();
        meansDAO.saveMeans(tram);

        Maintenance maintenance = newMaintenance(autobus).get();
        Maintenance maintenance2 = newMaintenance(autobus).get();

        meansDAO.saveMaintenance(maintenance);
        meansDAO.saveMaintenance(maintenance2);
        System.out.println("pollo");


        TicketIssue vendingMachine = newVendingMachine.get();
        TicketIssue authorizatedSellers = newAuthorizatedSellers.get();

        ticketIssueDAO.save(vendingMachine);
        ticketIssueDAO.save(authorizatedSellers);

        for (int i = 0; i < 2; i++) {
            User user = newUser.get();
            Ticket ticket = newTicket.get();
            TicketOffice subscription = newSubscription(user).get();
            ticketOfficeDAO.saveUser(user);
            ticketOfficeDAO.saveTicketOffice(ticket);
            ticketOfficeDAO.saveTicketOffice(subscription);
        }


    }


}
