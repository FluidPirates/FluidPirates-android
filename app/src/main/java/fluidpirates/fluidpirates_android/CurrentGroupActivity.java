package fluidpirates.fluidpirates_android;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CurrentGroupActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_group);

        Intent intent = getIntent();
        String nomGroup = intent.getExtras().getString("group_id");

        TextView top_bar_text = (TextView) findViewById(R.id.top_bar_text);
        top_bar_text.setText(nomGroup);
        Button button = (Button) findViewById(R.id.ajouter_membres);
        button.setVisibility(View.VISIBLE);
        button.setEnabled(false);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        TextView tv = (TextView) navView.inflateHeaderView(R.layout.nav_header_main).findViewById(R.id.nom_tv);
        tv.setText("Nom session");
    }

}
