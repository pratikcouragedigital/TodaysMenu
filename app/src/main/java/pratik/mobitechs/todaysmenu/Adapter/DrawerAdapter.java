package pratik.mobitechs.todaysmenu.Adapter;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pratik.mobitechs.todaysmenu.MainActivity;
import pratik.mobitechs.todaysmenu.Model.DrawerItems;
import pratik.mobitechs.todaysmenu.R;
import pratik.mobitechs.todaysmenu.SessionManager.SessionManager;
import pratik.mobitechs.todaysmenu.ShowMenu;
import pratik.mobitechs.todaysmenu.Singleton.DrawerListInstance;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private static ArrayList<DrawerItems> itemsArrayList;
    public View v;
    public ViewHolder viewHolder;
    public DrawerLayout drawer;
    int positionOfItem = 0;
    SessionManager sessionManager;

    private static ArrayList<DrawerItems> itemselectedArrayList;
    public static DrawerListInstance drawerListInstance = new DrawerListInstance();

    public DrawerAdapter(ArrayList<DrawerItems> itemsArrayList, ArrayList<DrawerItems> itemselectedArrayList, DrawerLayout drawer) {
        this.drawer = drawer;
        this.itemsArrayList = itemsArrayList;
        this.itemselectedArrayList = itemselectedArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.draweritems, viewGroup, false);
        } else if (i == 1) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.draweritemtext, viewGroup, false);
        }
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        DrawerItems itemsList = itemsArrayList.get(i);
        DrawerItems itemselectedList = itemselectedArrayList.get(i);
        viewHolder.bindDrawerItemArrayList(i, itemsList, itemselectedList);
    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (position <= 4) {
            viewType = 0;
        } else if (position > 4) {
            viewType = 1;
        }
        return viewType;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView vtextView;
        public ImageView vimageView;
        public View drawerDivider;
        public TextView vtext;

        private DrawerItems itemsList;
        private DrawerItems itemsSelectedList;

        public ArrayList<DrawerItems> drawerItemsArrayList = new ArrayList<DrawerItems>();

        public ViewHolder(View itemView) {
            super(itemView);
            vtextView = (TextView) itemView.findViewById(R.id.drawerItemText);
            vimageView = (ImageView) itemView.findViewById(R.id.drawerItemIcon);
            drawerDivider = (View) itemView.findViewById(R.id.drawerDivider);
            vtext = (TextView) itemView.findViewById(R.id.itemText);
            itemView.setOnClickListener(this);
        }

        public void bindDrawerItemArrayList(int i, DrawerItems draweritemsList, DrawerItems draweritemselectedList) {

            this.itemsList = draweritemsList;
            this.itemsSelectedList = draweritemselectedList;

            if (drawerListInstance.getDrawerItemListImagePositionInstances() != null) {
                positionOfItem = drawerListInstance.getDrawerItemListImagePositionInstances();
            }
            if (i <= 2) {
                vimageView.setImageResource(itemsList.getIcon());
                vtextView.setText(itemsList.getTittle());
                //vimageView.setEnabled(true);
                if (i == 2) {
                    drawerDivider.setVisibility(View.VISIBLE);
                }
                if (positionOfItem == 0 && itemsList.getIcon() == R.drawable.home) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());
                }


            } else if (i > 2) {
                vtext.setText(itemsList.getTittle());
            }
        }

        @Override
        public void onClick(View view) {
            positionOfItem = this.getAdapterPosition();
            if (this.getAdapterPosition() == 0) {
                drawer.closeDrawers();
                Intent gotoformhome = new Intent(view.getContext(), MainActivity.class);
                view.getContext().startActivity(gotoformhome);
            }
            else if (this.getAdapterPosition() == 1) {
                drawer.closeDrawers();
                Intent gotoPasswordForEdit = new Intent(view.getContext(), ShowMenu.class);
                view.getContext().startActivity(gotoPasswordForEdit);
            }
            else if (this.getAdapterPosition() == 2) {
                drawer.closeDrawers();
                sessionManager = new SessionManager(v.getContext());
                sessionManager.logoutUser();
            }

            drawerListInstance.setDrawerItemListImagePositionInstances(positionOfItem);
            notifyDataSetChanged();
        }
    }
}
