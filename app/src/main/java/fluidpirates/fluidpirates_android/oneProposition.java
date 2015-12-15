package fluidpirates.fluidpirates_android;

import android.app.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class oneProposition extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_proposition);
        String[] items = this.getResources().getStringArray(R.array.name_group);
        ArrayAdapter adapter = new ArrayAdapter(this.getApplicationContext(),R.layout.simple_raw,items);
        setContentView(R.layout.activity_one_proposition);
        ((ListView) this.findViewById(R.id.list)).setAdapter(adapter);
    }
}
