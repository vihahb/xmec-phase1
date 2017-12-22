package com.xtelsolution.xmec.model.database;

import com.xtelsolution.xmec.model.entity.SearchDrug;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by NutIT on 07/11/2017.
 */

public class RealmHelper {
    Realm realm;
    public RealmHelper(Realm realm) {
        this.realm = realm;
    }
    //WRITE
    public void save(final SearchDrug drugs)
    {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SearchDrug s=realm.copyToRealm(drugs);
            }
        });
    }
    //READ
    public ArrayList<String> retrieve()
    {
        ArrayList<String> drugNames=new ArrayList<>();
        RealmResults<SearchDrug> spacecrafts=realm.where(SearchDrug.class).findAll();
        for(SearchDrug s:spacecrafts)
        {
            drugNames.add(s.getName());
        }
        return drugNames;
    }
}
