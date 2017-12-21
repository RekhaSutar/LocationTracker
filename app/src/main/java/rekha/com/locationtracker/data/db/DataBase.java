package rekha.com.locationtracker.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {UserJourney.class}, version = 2)
@TypeConverters({LocationTypeConverter.class})
public abstract class DataBase extends RoomDatabase {

    public abstract UserJourneyDao userJourneyDaoModel();

    private static DataBase INSTANCE;

    public static DataBase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(),
                            DataBase.class, "Location_Tracker_DB")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
