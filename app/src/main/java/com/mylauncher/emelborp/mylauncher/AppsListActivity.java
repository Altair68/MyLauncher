package com.mylauncher.emelborp.mylauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class AppsListActivity extends Activity {

    private PackageManager packageManager;
    private List<AppDetail> appList;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_list);

        loadApps();
        loadListView();
        addClickListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.apps_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadApps() {
        packageManager = getPackageManager();
        appList = new ArrayList<>();

        Intent theIntent = new Intent(Intent.ACTION_MAIN, null);
        theIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        Collection<ResolveInfo> theActivities = packageManager.queryIntentActivities(theIntent, 0);
        for (ResolveInfo eachActivity : theActivities) {
            AppDetail theDetail = new AppDetail();
            theDetail.setLabel(eachActivity.loadLabel(packageManager));
            theDetail.setName(eachActivity.activityInfo.packageName);
            theDetail.setIcon(eachActivity.activityInfo.loadIcon(packageManager));
            appList.add(theDetail);
        }
    }

    public void loadListView () {
        list = (ListView) findViewById(R.id.apps_list);

        ArrayAdapter<AppDetail> theAdapter = new ArrayAdapter<AppDetail>(this, R.layout.list_item, appList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                }

                ImageView appIcon = (ImageView)convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(appList.get(position).getIcon());

                TextView appLabel = (TextView)convertView.findViewById(R.id.item_app_label);
                appLabel.setText(appList.get(position).getLabel());

                TextView appName = (TextView)convertView.findViewById(R.id.item_app_name);
                appName.setText(appList.get(position).getName());

                return convertView;
            }
        };

        list.setAdapter(theAdapter);
    }

    private void addClickListener(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {
                Intent theIntent = packageManager.getLaunchIntentForPackage(appList.get(pos).getName().toString());
                AppsListActivity.this.startActivity(theIntent);
            }
        });
    }
}
