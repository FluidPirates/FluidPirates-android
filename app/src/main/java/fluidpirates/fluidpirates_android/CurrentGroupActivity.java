package fluidpirates.fluidpirates_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import models.Scrutin;
import utils.GetJsonArrayAsync;

public class CurrentGroupActivity extends Activity {

    private static final String GROUPS_URL = "http://fluidpirates.com/api/groups/1/categories/1/polls";
    private String token = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_group);

        Intent intent = getIntent();
        String nomGroup = intent.getStringExtra("group_id");
        this.token = intent.getExtras().getString("token");

        TextView top_bar_text = (TextView) findViewById(R.id.top_bar_text);
        top_bar_text.setText(nomGroup);
        Button button = (Button) findViewById(R.id.ajouter_membres);
        button.setVisibility(View.VISIBLE);
        button.setEnabled(false);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        TextView tv = (TextView) navView.inflateHeaderView(R.layout.nav_header_main).findViewById(R.id.nom_tv);
        tv.setText("Nom session");

        loadFromAPI(GROUPS_URL + "?token=" + token);
    }

    private void loadFromAPI(String url) {
        (new FetchGroupsIndex(this)).execute(url);
    }

    private class FetchGroupsIndex extends GetJsonArrayAsync {
        public FetchGroupsIndex(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            try {
                JSONObject jsonObject = new JSONObject();
                int length = json.length();
                final ArrayList<Scrutin> objects = new ArrayList<Scrutin>(length);

                for (int i = 0; i < length; i++) {
                    jsonObject = json.getJSONObject(i);
                    Scrutin newScrutin = new Scrutin(
                            jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("description"),
                            jsonObject.getBoolean("open?"),
                            jsonObject.getBoolean("closed?"));
                    objects.add(newScrutin);
                }
                ListView listView = (ListView) findViewById(R.id.list_propositions);
                if (listView != null) {
                    listView.setAdapter(new PropositionAdapter(getApplicationContext(), R.layout.proposition_list_item, objects));
                }
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                super.onPostExecute(json);
            }
        }
    }

    private class PropositionAdapter extends ArrayAdapter<Scrutin> implements View.OnClickListener {
        private ArrayList<Scrutin> items;
        private int layoutResourceId;

        public PropositionAdapter(Context context, int layoutResourceId, ArrayList<Scrutin> items) {
            super(context, layoutResourceId, items);
            this.layoutResourceId = layoutResourceId;
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (LinearLayout) layoutInflater.inflate(layoutResourceId, null);
            }
            Scrutin item = items.get(position);
            if (item != null) {
                TextView itemName = (TextView) view.findViewById(R.id.proposition_item_name);
                if (itemName != null) {
                    itemName.setText(item.getName());
                }
                TextView itemDescription = (TextView) view.findViewById(R.id.proposition_item_description);
                if (itemDescription != null) {
                    itemDescription.setText(item.getDescription());
                }

                view.setTag(item.getId());
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CurrentGroupActivity.this, CurrentGroupActivity.class);
                    intent.putExtra("group_id", v.getTag().toString());
                    startActivity(intent);
                }
            });

            return view;
        }

        @Override
        public void onClick(View view) {
        }
    }
}