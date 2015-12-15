package fluidpirates.fluidpirates_android;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import java.util.*;



public class Groups extends Activity {
    public String[] name_group;
    public  int[] nb_members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        name_group = this.getResources().getStringArray(R.array.name_group);
        nb_members = this.getResources().getIntArray(R.array.nb_members);
        ArrayList<Group_class> listGroup = new ArrayList<Group_class>();
        int i;
        for(i=0;i<name_group.length;i++) {
            listGroup.add(new Group_class(name_group[i], nb_members[i]));
        }

        ArrayAdapter adapter = new ArrayAdapter(this.getApplicationContext(),R.layout.fragment_group_element,name_group);
        setContentView(R.layout.activity_groups);
        ((ListView) this.findViewById(R.id.listGroups)).setAdapter(adapter);
    }

}


public class Group_class {

    private String nom;
    private int nb_members;

    public Group_class(String aNom, int anb_membres) {
        nom = aNom;
        nb_members = anb_membres;
    }
}

public class Group_adapter extends BaseAdapter {

    // Une liste de personnes
    private List<Group_class> mListP;

    //Le contexte dans lequel est présent notre adapter
    private Context mContext;

    //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
    private LayoutInflater mInflater;
}