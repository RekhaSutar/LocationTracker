package rekha.com.locationtracker.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserJourneyDao {

    @Query("SELECT * FROM UserJourney")
    List<UserJourney> getAllUserJourneys();

    @Insert(onConflict = REPLACE)
    void addUserJourney(UserJourney userJourney);

}
