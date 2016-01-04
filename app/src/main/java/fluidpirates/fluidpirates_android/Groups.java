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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fluidpirates.fluidpirates_android.CurrentGroup;
import fluidpirates.fluidpirates_android.R;


public class Groups extends Activity {
    public String[] name_group;
    public String[] nb_members;

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
                Intent intent = new Intent(Groups.this, CurrentGroup.class);
                intent.putExtra("group", listGroup.get(position).nom);
                startActivity(intent);
            }
        });
        final Button createButton = (Button) findViewById(R.id.create_group);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Groups.this, New_group.class);
                //startActivity(intent);
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
}