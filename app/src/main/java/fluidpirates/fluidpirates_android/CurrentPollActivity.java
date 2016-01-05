package fluidpirates.fluidpirates_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import models.Choice;
import models.Params;
import models.Poll;
import models.Proposition;
import utils.GetJsonObjectAsync;
import utils.Lazy;
import utils.PostJsonObjectAsync;

public class CurrentPollActivity extends Activity {

    private String TAG = "CurrentPollActivity";
    private static final String POLL_URL = "https://fluidpirates.com/api/groups/:group_id/polls/:poll_id";
    private static final String VOTE_URL = POLL_URL + "/propositions/:proposition_id/choices/:choice_id/votes";
    private String token = null;
    private String group_id = null;
    private String poll_id = null;
    private Poll poll = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_poll);

        Intent intent = getIntent();
        this.group_id = intent.getExtras().getString("group_id");
        this.poll_id = intent.getExtras().getString("poll_id");
        Log.d("POLL_ID", this.poll_id);
        this.token = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE).getString("token", "");

        loadPollFromAPI(Lazy.Str.urlReplace(POLL_URL + "?token=" + token, ":group_id", group_id, ":poll_id", poll_id));
    }

    private void loadPollFromAPI(String url) {
        (new FetchPollShow(this)).execute(url);
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
        ((TextView) findViewById(R.id.top_bar_text)).setText("Proposition");
        ((TextView) findViewById(R.id.poll_name)).setText(this.poll.getName());
        ((TextView) findViewById(R.id.poll_description)).setText(this.poll.getDescription());
        if (this.poll.getOpen()) {
            ((TextView) findViewById(R.id.poll_open_close)).setText("open");
        } else {
            ((TextView) findViewById(R.id.poll_open_close)).setText("closed");
        }
    }

    private class FetchPollShow extends GetJsonObjectAsync {
        public FetchPollShow(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                CurrentPollActivity.this.setPoll(new Poll(
                        json.getInt("id"),
                        json.getString("name"),
                        json.getString("description"),
                        json.getBoolean("open?")));
                JSONArray jsonArray = json.getJSONArray("propositions");
                final ArrayList<Proposition> objects = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Proposition newObject = new Proposition(
                            jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("description"));
                    JSONArray choicesArray = jsonObject.getJSONArray("choices");
                    for (int j = 0; j < choicesArray.length(); j++) {
                        JSONObject choiceObject = choicesArray.getJSONObject(j);
                        newObject.addChoice(new Choice(
                                choiceObject.getInt("id"),
                                choiceObject.getString("name")));
                    }
                    objects.add(newObject);

                }
                ListView listView = (ListView) findViewById(R.id.propositionsList);
                if (listView != null) {
                    listView.setAdapter(new PropositionAdapter(getApplicationContext(), R.layout.proposition_list_item, objects));
                }
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, Lazy.Ex.getStackTrace(e));
            } finally {
                super.onPostExecute(json);
            }
        }
    }

    private class PropositionAdapter extends ArrayAdapter<Proposition> {
        private ArrayList<Proposition> items;
        private int layoutResourceId;

        public PropositionAdapter(Context context, int layoutResourceId, ArrayList<Proposition> items) {
            super(context, layoutResourceId, items);
            this.layoutResourceId = layoutResourceId;
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(layoutResourceId, parent, false);
                view.setOnClickListener(null);
            }
            Proposition item = items.get(position);
            if (item != null) {
                TextView itemName = (TextView) view.findViewById(R.id.item_name);
                if (itemName != null) {
                    itemName.setText(item.getName());
                }

                TextView itemDescription = (TextView) view.findViewById(R.id.item_description);
                if (itemDescription != null) {
                    itemDescription.setText(item.getDescription());
                }

                GridView listView = (GridView) view.findViewById(R.id.choicesList);
                if (listView != null) {
                    listView.setAdapter(new ChoiceAdapter(getApplicationContext(), R.layout.choice_list_item, item.getChoices()));
                }

                view.setTag(item.getId());
            }

            return view;
        }
    }

    private class ChoiceAdapter extends ArrayAdapter<Choice> {
        private ArrayList<Choice> items;
        private int layoutResourceId;

        public ChoiceAdapter(Context context, int layoutResourceId, ArrayList<Choice> items) {
            super(context, layoutResourceId, items);
            this.layoutResourceId = layoutResourceId;
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(layoutResourceId, parent, false);
            }

            Choice item = items.get(position);
            if (item != null) {
                TextView itemName = (TextView) view.findViewById(R.id.item_name);
                if (itemName != null) {
                    itemName.setText(item.getName());
                }

                view.setTag(item.getId());
            }

            Button itemVote = (Button) view.findViewById(R.id.item_vote);

            itemVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String voteUrl = Lazy.Str.urlReplace(VOTE_URL + "?token=" + CurrentPollActivity.this.token,
                            ":group_id", CurrentPollActivity.this.group_id,
                            ":poll_id", CurrentPollActivity.this.poll_id,
                            ":proposition_id", ((ViewGroup) v.getParent().getParent().getParent()).getTag().toString(),
                            ":choice_id", ((ViewGroup) v.getParent()).getTag().toString());

                    Log.d(TAG, voteUrl);

                    (new CastVote(CurrentPollActivity.this, new Params())).execute(voteUrl);
                }
            });

            return view;
        }
    }

    private class CastVote extends PostJsonObjectAsync {
        public CastVote(Context context, Params params) {
            super(context, params);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                Toast.makeText(CurrentPollActivity.this, "Vous avez voté avec succès", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                super.onPostExecute(json);
            }
        }
    }
}