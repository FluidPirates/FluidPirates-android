package fluidpirates.fluidpirates_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import models.Group;
import models.Poll;
import utils.GetJsonArrayAsync;
import utils.GetJsonObjectAsync;
import utils.Lazy;

public class CurrentGroupActivity extends Activity {

    private String TAG = "CurrentGroupActivity";
    private static final String GROUP_URL = "https://fluidpirates.com/api/groups/:group_id";
    private String token = null;
    private String group_id = null;
    private Group group = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_group);

        Intent intent = getIntent();
        this.group_id = intent.getExtras().getString("group_id");
        this.token = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE).getString("token", "");

        loadGroupFromAPI(Lazy.Str.urlReplace(GROUP_URL + "?token=" + token, ":group_id", group_id));
    }

    private void loadGroupFromAPI(String url) {
        (new FetchGroupShow(this)).execute(url);
    }

    public void setGroup(Group group) {
        this.group = group;

        ((TextView) findViewById(R.id.group_name)).setText(group.getName());
        ((TextView) findViewById(R.id.group_description)).setText(group.getDescription());
    }

    private class FetchGroupShow extends GetJsonObjectAsync {
        public FetchGroupShow(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                CurrentGroupActivity.this.setGroup(new Group(
                        json.getInt("id"),
                        json.getString("name"),
                        json.getString("description"),
                        json.getString("domain")));

                JSONArray jsonArray = json.getJSONArray("polls");
                final ArrayList<Poll> objects = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Poll newObject = new Poll(
                            jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("description"),
                            jsonObject.getBoolean("open?"));
                    objects.add(newObject);
                }

                ListView listView = (ListView) findViewById(R.id.pollsList);
                if (listView != null) {
                    listView.setAdapter(new PollAdapter(getApplicationContext(), R.layout.simple_list_item, objects));

                }
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, Lazy.Ex.getStackTrace(e));
            } finally {
                super.onPostExecute(json);
            }
        }
    }

    private class PollAdapter extends ArrayAdapter<Poll> {
        private ArrayList<Poll> items;
        private int layoutResourceId;

        public PollAdapter(Context context, int layoutResourceId, ArrayList<Poll> items) {
            super(context, layoutResourceId, items);
            this.layoutResourceId = layoutResourceId;
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(layoutResourceId, null);
            }
            Poll item = items.get(position);
            if (item != null) {
                TextView itemName = (TextView) view.findViewById(R.id.item_name);
                if (itemName != null) {
                    itemName.setText(item.getName());
                }

                TextView itemDescription = (TextView) view.findViewById(R.id.item_description);
                if (itemDescription != null) {
                    itemDescription.setText(item.getDescription());
                }

                view.setTag(item.getId());
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CurrentGroupActivity.this, CurrentPollActivity.class);
                    intent.putExtra("group_id", CurrentGroupActivity.this.group_id);
                    intent.putExtra("poll_id", v.getTag().toString());
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}