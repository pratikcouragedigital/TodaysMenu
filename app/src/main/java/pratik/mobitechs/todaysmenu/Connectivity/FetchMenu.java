package pratik.mobitechs.todaysmenu.Connectivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pratik.mobitechs.todaysmenu.MainActivity;
import pratik.mobitechs.todaysmenu.Model.ShowMenuItems;
import pratik.mobitechs.todaysmenu.SessionManager.SessionManager;
import pratik.mobitechs.todaysmenu.ShowMenu;
import pratik.mobitechs.todaysmenu.TodaysMenu;
import pratik.mobitechs.todaysmenu.WebService;

public class FetchMenu {
    private static Context context;
    private static final String url="";
    private static String displayText;
    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTask;
    private static List<ShowMenuItems> notificationItemsArrayForAsyncTask;

    public FetchMenu(ShowMenu notification) {
        context = notification;
    }

    public static void fetchTodaysMenu( List<ShowMenuItems> notificationItemsArray, RecyclerView recyclerView,  RecyclerView.Adapter reviewAdapter) {

        adapterForAsyncTask= reviewAdapter;
        recyclerViewForAsyncTask= recyclerView;
        notificationItemsArrayForAsyncTask = notificationItemsArray;;

        AsyncCallWS task = new AsyncCallWS();
        task.execute();

    }
    private static class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            displayText = WebService.ShowMenu("ViewMenuDetails");
            return null;
        }
        @Override
        protected void onPostExecute(Void res) {
           // if(!displayText.equals("Invalid Notification") || displayText.equals("No Network Found")) {
            if(displayText.equals("Not found Today Menu List..!!")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Result");
                builder.setMessage("Food List Not Found..");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface alert, int which) {
                        // TODO Auto-generated method stub
                        //Do something
                        alert.dismiss();
                        Intent gotohome = new Intent(context, MainActivity.class);
                        context.startActivity(gotohome);
                    }
                });
                AlertDialog alert1 = builder.create();
                alert1.show();
            }
            else {

                try {
                    JSONArray jsonArray = new JSONArray(displayText);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            ShowMenuItems showMenuItems = new ShowMenuItems();
                            showMenuItems.setmenuId(obj.getString("Menuid"));
                            showMenuItems.setUserid(obj.getString("Userid"));
                            showMenuItems.setname(obj.getString("full_Name"));
                            showMenuItems.setdate(obj.getString("date"));
                            showMenuItems.setMenuType(obj.getString("MenuType"));
                            showMenuItems.setFood(obj.getString("Food"));
                            showMenuItems.setLFoodRemark(obj.getString("FoodRemark"));
                            showMenuItems.setCookingFoodRemark(obj.getString("CookingFoodRemark"));


                            notificationItemsArrayForAsyncTask.add(showMenuItems);
                            adapterForAsyncTask.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                catch (JSONException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }
}
