package fluidpirates.fluidpirates_android;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NewGroupActivity extends Activity {
//Create a new group activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        TextView top_bar_text = (TextView) findViewById(R.id.top_bar_text);
        top_bar_text.setText("Groupes");
        final Button create_group = (Button) findViewById(R.id.create_group);
        create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewGroupActivity.this, GroupsActivity.class);
                startActivity(intent);
            }
        });
    }

}
