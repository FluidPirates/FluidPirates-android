package fluidpirates.fluidpirates_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.util.Log;
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

import models.Group;
import utils.GetJsonArrayAsync;

public class GroupsActivity extends Activity {
    private String TAG = "GroupsActivity";
    private static final String GROUPS_URL = "http://fluidpirates.com/api/groups";
    private String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        Intent intent = getIntent();
        this.token = intent.getExtras().getString("token");

        TextView top_bar_text = (TextView) findViewById(R.id.top_bar_text);
        top_bar_text.setText("Groupes");

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        TextView tv = (TextView) navView.inflateHeaderView(R.layout.nav_header_main).findViewById(R.id.nom_tv);
        tv.setText("Nom session");

        loadFromAPI(GROUPS_URL + "?token=" + token);

        final Button createButton = (Button) findViewById(R.id.create_group);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupsActivity.this, NewGroupActivity.class);
                startActivity(intent);
            }
        });
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
                final ArrayList<Group> objects = new ArrayList<Group>(length);

                for (int i = 0; i < length; i++) {
                    jsonObject = json.getJSONObject(i);
                    Group newGroup = new Group(
                            jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("description"),
                            jsonObject.getString("domain"));
                    newGroup.setUsersCount(jsonObject.getInt("users_count"));
                    objects.add(newGroup);
                }

                ListView listView = (ListView) findViewById(R.id.listGroups);
                if (listView != null) {
                    listView.setAdapter(new GroupAdapter(getApplicationContext(), R.layout.group_list_item, objects));
                }
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                super.onPostExecute(json);
            }
        }
    }

    private class GroupAdapter extends ArrayAdapter<Group> implements View.OnClickListener {
        private ArrayList<Group> items;
        private int layoutResourceId;

        public GroupAdapter(Context context, int layoutResourceId, ArrayList<Group> items) {
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
            Group item = items.get(position);
            if (item != null) {
                TextView itemName = (TextView) view.findViewById(R.id.group_item_name);
                if (itemName != null) {
                    itemName.setText(item.getName());
                }
                TextView itemDescription = (TextView) view.findViewById(R.id.group_item_description);
                if (itemDescription != null) {
                    itemDescription.setText(item.getDescription());
                }

                TextView itemUsersCount = (TextView) view.findViewById(R.id.group_item_users_count);
                if (itemUsersCount != null) {
                    itemUsersCount.setText(Integer.toString(item.getUsersCount()));
                }

                view.setTag(item.getId());
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GroupsActivity.this, CurrentGroupActivity.class);
                    intent.putExtra("group_id", v.getTag().toString());
                    intent.putExtra("token", token);
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