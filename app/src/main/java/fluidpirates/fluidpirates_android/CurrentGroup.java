package fluidpirates.fluidpirates_android;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class CurrentGroup extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_group);

        Intent intent = getIntent();
        String nomGroup = intent.getExtras().getString("group");

        TextView top_bar_text = (TextView) findViewById(R.id.top_bar_text);
        top_bar_text.setText(nomGroup);
    }

}
