package rekha.com.locationtracker.view.userjourney;

import android.os.Bundle;

import rekha.com.locationtracker.R;
import rekha.com.locationtracker.view.BaseActivity;

public class UserJourneyDetailsActivity extends BaseActivity {

    public final String ARG_JOURNEY_ID = "journey_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_journey_details);

        if (getIntent() != null && getIntent().getExtras() != null){

        }
    }
}
