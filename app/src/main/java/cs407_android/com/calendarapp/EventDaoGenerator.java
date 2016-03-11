package cs407_android.com.calendarapp;

/**
 * GreenDAO ORM for calendar events
 * Based on ORM lab https://github.com/panayao/ORMlab/tree/master/app
 */
import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;


public class EventDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "cs407_android.com.calendarapp"); //Scheme for GreenDAO ORM
        createDB(schema);
        new DaoGenerator().generateAll(schema, "./app/src/main/java/");
    }

    private static void createDB(Schema schema) {

        //Add Event
        Entity event = schema.addEntity("Event");
        event.addIdProperty();
        event.addStringProperty("title");
        event.addIntProperty("month");
        event.addIntProperty("dayOfMonth");
        event.addIntProperty("startHour");
        event.addDateProperty("startMinute");
        event.addStringProperty("description");
        event.addBooleanProperty("display"); //always true

    }
}