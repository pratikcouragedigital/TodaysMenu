package pratik.mobitechs.todaysmenu;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pratik.mobitechs.todaysmenu.Adapter.ShowMenuAdapter;
import pratik.mobitechs.todaysmenu.Connectivity.FetchMenu;
import pratik.mobitechs.todaysmenu.InternetConnectivity.NetworkChangeReceiver;
import pratik.mobitechs.todaysmenu.Model.ShowMenuItems;

public class ShowMenu extends BaseActivity {

    public List<ShowMenuItems> showMenuItems = new ArrayList<ShowMenuItems>();
    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    boolean doubleBackToExitPressedOnce = false;
    private int current_page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_menu);

        recyclerView = (RecyclerView) findViewById(R.id.menuListRecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new ShowMenuAdapter(showMenuItems);
        //reviewAdapter = new ShowMenuAdapter(showMenuItems,ShowMenu.this);
        recyclerView.setAdapter(reviewAdapter);

        try {

            FetchMenu fetchMenu = new FetchMenu(ShowMenu.this);
            fetchMenu.fetchTodaysMenu(showMenuItems,recyclerView, reviewAdapter);
            //fetch_Notification.fetchNotifications(notificationItems, reviewAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onBackPressed() {
        ShowMenu.this.finish();
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = ShowMenu.this.getPackageManager();
        ComponentName component = new ComponentName(ShowMenu.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = ShowMenu.this.getPackageManager();
        ComponentName component = new ComponentName(ShowMenu.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }



}

