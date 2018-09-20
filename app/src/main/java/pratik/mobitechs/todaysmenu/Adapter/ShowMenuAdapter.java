package pratik.mobitechs.todaysmenu.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import pratik.mobitechs.todaysmenu.Login;
import pratik.mobitechs.todaysmenu.MainActivity;
import pratik.mobitechs.todaysmenu.Model.ShowMenuItems;
import pratik.mobitechs.todaysmenu.R;
import pratik.mobitechs.todaysmenu.SessionManager.SessionManager;
import pratik.mobitechs.todaysmenu.ShowMenu;
import pratik.mobitechs.todaysmenu.WebService;

public class ShowMenuAdapter extends RecyclerView.Adapter<ShowMenuAdapter.ViewHolder>{

    List<ShowMenuItems> showMenuItems;
    View v;
    ViewHolder viewHolder;
    private static Context context;
    String displayText;
    String remarkOfMate;
    String menuId;
    String method = "AddCookingFoodRemark";



    public ShowMenuAdapter(List<ShowMenuItems> notificationItemsArrayList) {
        this.showMenuItems = notificationItemsArrayList;

   }
// public ShowMenuAdapter(List<ShowMenuItems> notificationItemsArrayList, ShowMenu showMenu) {
//        this.showMenuItems = notificationItemsArrayList;
//        context = showMenu;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_menu_items, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ShowMenuItems notificationItem = showMenuItems.get(position);
        viewHolder.bindNotificationDetailsList(notificationItem );
    }

    @Override
    public int getItemCount() {
        return showMenuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView date;
        public TextView clientName;
        public TextView menuTiming;
        public TextView ingredients;
        public TextView remark;
        public TextView mateRemark;
        public View cardView;

        RelativeLayout layoutMateRemark;

        ShowMenuItems showMenuItems = new ShowMenuItems();


        public ViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.date);
            clientName = (TextView) itemView.findViewById(R.id.clientName);
            menuTiming = (TextView) itemView.findViewById(R.id.menuTiming);
            ingredients = (TextView) itemView.findViewById(R.id.ingredients);
            remark = (TextView) itemView.findViewById(R.id.remark);
            mateRemark = (TextView) itemView.findViewById(R.id.mateRemark);
            layoutMateRemark = (RelativeLayout) itemView.findViewById(R.id.layoutMateRemark);

            cardView = itemView;
            cardView.setOnClickListener(this);
        }

        public void bindNotificationDetailsList(ShowMenuItems showMenuItems) {
            this.showMenuItems = showMenuItems;

            date.setText(showMenuItems.getdate());
            clientName.setText(showMenuItems.getnmae());
            menuTiming.setText(showMenuItems.getMenuType());
            ingredients.setText(showMenuItems.getFood());
            remark.setText(showMenuItems.getLFoodRemark());
            mateRemark.setText(showMenuItems.getCookingFoodRemark());
            String remarkOfMate = showMenuItems.getCookingFoodRemark();

            if(remarkOfMate.equals("") || remarkOfMate==null || remarkOfMate.equals("null")){
                layoutMateRemark.setVisibility(View.GONE);
            }else{
                layoutMateRemark.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View view) {

            SessionManager sessionManagerNgo = new SessionManager(view.getContext());
            HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
            String userType = typeOfUser.get(SessionManager.KEY_USERID);
            menuId= showMenuItems.getmenuId();

            if(userType.equals("3")){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                final EditText edittext = new EditText(v.getContext());
                alert.setMessage("Enter Your Remark");
                alert.setTitle("Remark");

                alert.setView(edittext);

                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                        //Editable remarkOfMate = edittext.getText();
                        //OR
                        remarkOfMate = edittext.getText().toString();

                        SaveMateRemarksAsyncCallWS task = new SaveMateRemarksAsyncCallWS();
                        task.execute();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();
            }
        }
    }

    public class SaveMateRemarksAsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            displayText = WebService.SaveMateRemark(menuId,remarkOfMate,method);
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Result");
            builder.setMessage(displayText);
            AlertDialog alert1 = builder.create();
            alert1.show();
            if (displayText.equals("Cooking Food Remark Add Succesfully.")) {
                Intent first = new Intent(v.getContext(), ShowMenu.class);
                v.getContext().startActivity(first);
            }

        }
    }

}
