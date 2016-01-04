package fluidpirates.fluidpirates_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import models.Group;
import utils.GetJsonArrayAsync;


public class GroupsActivity extends Activity {
    public String[] name_group;
    public String[] nb_members;

    private static final String GROUPS_URL = "http://fluidpirates.com/api/groups?token=1";


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.listViewGestion();
        TextView top_bar_text = (TextView) findViewById(R.id.top_bar_text);
        top_bar_text.setText("Groupes");
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        TextView tv = (TextView) navView.inflateHeaderView(R.layout.nav_header_main).findViewById(R.id.nom_tv);
        tv.setText("Nom session");

        loadFromAPI(GROUPS_URL);
    }

    private void loadFromAPI(String url) {
        (new FetchGroupsIndex(this)).execute(url);
    }

    protected void listViewGestion() {
        ListView lv;
        name_group = this.getResources().getStringArray(R.array.name_group);
        nb_members = this.getResources().getStringArray(R.array.nb_members);
        final ArrayList<Group_class> listGroup = new ArrayList<Group_class>();
        int i;
        for (i = 0; i < name_group.length; i++) {
            listGroup.add(new Group_class(name_group[i], nb_members[i]));
        }
        Group_adapter adapter = new Group_adapter(this, listGroup);
        setContentView(R.layout.activity_groups);
        lv = ((ListView) this.findViewById(R.id.listGroups));
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GroupsActivity.this, CurrentGroupActivity.class);
                intent.putExtra("group", listGroup.get(position).nom);
                startActivity(intent);
            }
        });
        final Button createButton = (Button) findViewById(R.id.create_group);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupsActivity.this, NewGroupActivity.class);
                startActivity(intent);
            }
        });
    }


    class Group_class {

        public String nom;
        public String nb_members;

        public Group_class(String aNom, String anb_membres) {
            nom = aNom;
            nb_members = anb_membres;
        }
    }

    class Group_adapter extends BaseAdapter {

        // Une liste de personnes
        private List<Group_class> mListG;

        //Le contexte dans lequel est présent notre adapter
        private Context mContext;

        //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
        private LayoutInflater mInflater;

        public Group_adapter(Context context, List<Group_class> aListG) {
            mContext = context;
            mListG = aListG;
            mInflater = LayoutInflater.from(mContext);
        }

        public int getCount() {
            return mListG.size();
        }

        public Object getItem(int position) {
            return mListG.get(position);
        }

        public long getItemId(int position) {
            return position;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout layoutItem;

            //(1) : Réutilisation des layouts
            if (convertView == null) {
                //Initialisation de notre item à partir du  layout XML "personne_layout.xml"
                layoutItem = (LinearLayout) mInflater.inflate(R.layout.fragment_group_element, parent, false);

            } else {
                layoutItem = (LinearLayout) convertView;
            }

            //(2) : Récupération des TextView de notre layout
            TextView tv_Nom = (TextView) layoutItem.findViewById(R.id.group_nom);
            TextView tv_members = (TextView) layoutItem.findViewById(R.id.group_nombre_membres);


            //(3) : Renseignement des valeurs
            tv_Nom.setText(mListG.get(position).nom);
            tv_members.setText(mListG.get(position).nb_members);

            //On retourne l'item créé.
            return layoutItem;
        }
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
                    objects.add(new Group(
                            jsonObject.getLong("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("description"),
                            jsonObject.getString("domain")));
                }

                ListView listView = (ListView) findViewById (R.id.listGroups);
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
                view.setTag(item.getId());
            }
            return view;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(GroupsActivity.this, CurrentGroupActivity.class);
            intent.putExtra("group_id", view.getTag().toString());
            startActivity(intent);
        }
    }
}