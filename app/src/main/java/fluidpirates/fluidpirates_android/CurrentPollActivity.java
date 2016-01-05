package fluidpirates.fluidpirates_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CurrentPollActivity extends Activity {
    private String group_id = null;
    private String poll_id = null;
    private String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_poll);
        Intent intent = getIntent();
        this.group_id = intent.getStringExtra("group_id");
        this.poll_id = intent.getStringExtra("poll_id");
        this.token = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE).getString("token", "");
    }
}
