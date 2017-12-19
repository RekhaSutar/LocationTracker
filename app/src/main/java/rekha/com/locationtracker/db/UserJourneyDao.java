package rekha.com.locationtracker.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserJourneyDao {

    @Insert(onConflict = REPLACE)
    void addUserJourney(UserJourney userJourney);

}
