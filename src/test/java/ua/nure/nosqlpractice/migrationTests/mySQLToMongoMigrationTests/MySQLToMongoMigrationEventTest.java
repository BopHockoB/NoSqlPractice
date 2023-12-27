package ua.nure.nosqlpractice.migrationTests.mySQLToMongoMigrationTests;

import com.github.javafaker.Faker;
import org.bson.types.ObjectId;
import org.junit.Assert;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.nure.nosqlpractice.event.*;
import ua.nure.nosqlpractice.event.eventDao.EventDataTransfer;
import ua.nure.nosqlpractice.event.eventDao.EventMongoDAO;
import ua.nure.nosqlpractice.event.eventDao.EventMySQLDAO;
import ua.nure.nosqlpractice.event.eventDao.IEventDAO;

import java.util.*;
import java.util.concurrent.TimeUnit;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MySQLToMongoMigrationEventTest {

    @Autowired
    private EventMySQLDAO eventMySQLDAO;
    @Autowired
    private EventMongoDAO eventMongoDAO;

    @Autowired
    private EventDataTransfer eventDataTransfer;

    private final Faker faker = new Faker(Locale.ENGLISH);



    public void cleanUp() {
        eventMySQLDAO.getAll().forEach(event -> eventMySQLDAO.delete(event.getEventId()));
        eventMongoDAO.getAll().forEach(event -> eventMongoDAO.delete(event.getEventId()));
    }


    public void setUp(IEventDAO eventDAO){
        for (int i = 0; i < 10; i++) {
            List<Ticket> tickets = Arrays.asList(
                    new Ticket(i, new TicketType(1, "Standard"), faker.number().randomDouble(2, 40, 300), faker.number().numberBetween(40, 300)),
                    new Ticket(i + 10, new TicketType(2, "Premium"), faker.number().randomDouble(2, 40, 300), faker.number().numberBetween(40, 300))
            );

            eventDAO.create(new Event.EventBuilder()
                    .setEventId(new ObjectId())
                    .setName(faker.name().name())
                    .setEventDate(faker.date().future(360, 30, TimeUnit.DAYS))
                    .setAddress(new Venue(1, "Sample venue 1", "Sample City 1", "Sample Country 1"))
                    .setDescription(faker.lorem().sentence())
                    .setTickets(tickets)
                    .setEventCategories(List.of(new EventCategory(1, "Category 1")))
                    .build()
            );
        }
    }

    @Test
    public void testGetAllEventsFromMySQLAndMigrateThemToMongoDB() {
        //Arrange
        setUp(eventMySQLDAO);

        List<Event> mySQLEvents;
        List<Event> mongoDBEvent;
        System.out.println("Test 1:");
        //Act
        //getting events from MySQL
        mySQLEvents = eventMySQLDAO.getAll();
        System.out.println(mySQLEvents);

        //writing events into MongoDB
        eventDataTransfer.transferDataToMongo();

        //getting events from MongoDB
        mongoDBEvent = eventMongoDAO.getAll();
        System.out.println(mongoDBEvent);


        //sort both of lists
        mongoDBEvent.sort(Comparator.comparing(Event::getEventId));
        mySQLEvents.sort(Comparator.comparing(Event::getEventId));

        cleanUp();
        //Assert
        assert(mySQLEvents.containsAll(mongoDBEvent));


    }

    @Test
    public void testGetAllEventsFromMongoDBAndMigrateThemToMySQL() {
        //Arrange
        setUp(eventMongoDAO);


        List<Event> mongoDBEvents;
        List<Event> mySQLEvents;
        System.out.println("Test 2:");


        //Act
        //getting events from Mongo
        mongoDBEvents = eventMongoDAO.getAll();
        System.out.println(mongoDBEvents);

        //writing events into MySQL
        eventDataTransfer.transferDataToMySQL();

        //getting events from MongoDB
        mySQLEvents = eventMySQLDAO.getAll();
        System.out.println(mySQLEvents);


        //sort both of lists
        mySQLEvents.sort(Comparator.comparing(Event::getEventId));
        mongoDBEvents.sort(Comparator.comparing(Event::getEventId));

        cleanUp();

        //Assert
        Assert.assertEquals(mySQLEvents, mongoDBEvents);


    }
}

