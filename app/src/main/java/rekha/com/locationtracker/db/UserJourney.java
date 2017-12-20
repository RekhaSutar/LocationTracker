package rekha.com.locationtracker.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import rekha.com.locationtracker.data.Location;

@Entity
public class UserJourney {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ColumnInfo(name = "user_journey")
    private List<Location> user_journey = new ArrayList<>();

    public List<Location> getUser_journey() {
        return user_journey;
    }

    public void setUser_journey(List<Location> user_journey) {
        this.user_journey = user_journey;
    }

    public UserJourney(List<Location> user_journey){
        this.user_journey = user_journey;
    }

}
