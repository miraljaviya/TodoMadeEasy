package com.dailytask.manager.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.dailytask.manager.R;
import com.dailytask.manager.data.CompletedTaskClass;
import com.dailytask.manager.data.DatabaseHelperComTask;
import com.dailytask.manager.ui.SendToComplete;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;
import java.util.Objects;

public class CompletedTaskFragment extends Fragment {
    private DatabaseHelperComTask db;
    private ArrayList<CompletedTaskClass> completedListItem;
    private ListView CompletedtaskList;
   private Intent intent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate (R.layout.compeleted_layout, container, false);

        //initialize banner ad
        AdView mAdView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Objects.requireNonNull(getActivity()).setTitle ("Completed Task");

        RelativeLayout emptyView = v.findViewById (R.id.emptyView);

        CompletedtaskList = v.findViewById (R.id.CompletedListView);
        //set empty view for list view
        CompletedtaskList.setEmptyView (emptyView);
        completedListItem = new ArrayList<> ();

        db = new DatabaseHelperComTask (getContext ());

        intent = getActivity ().getIntent ();

        if (intent.getExtras () != null) {
            CompletedTaskClass completedTaskClass = new CompletedTaskClass ();

            String title=intent.getStringExtra (SendToComplete.TITLE);
            String time=intent.getStringExtra (SendToComplete.TIME);

            if (title != null && !title.trim ().equals ("")){

                if(time != null && !time.trim ().equals ("")){
                    completedTaskClass.setTitle (title);
                    completedTaskClass.setTime (time);
                    db.insertData (completedTaskClass);
                    completedListItem.clear ();
                    DisplayData ();
                }
            }
        }


        return v;

    }

    private void DisplayData() {
        Cursor cursor = db.ViewData ();
        if (cursor.getCount () >= 1) {
            while (cursor.moveToNext ()) {
                String title = cursor.getString (1);
                String time = cursor.getString (2);
                CompletedTaskClass completedTaskClass = new CompletedTaskClass (title, time);
                completedListItem.add (completedTaskClass);
            }
            CompletedTaskadapter myTaskAdapter = new CompletedTaskadapter (getContext (), completedListItem);
            CompletedtaskList.setAdapter (myTaskAdapter);
            CompletedtaskList.setOnItemClickListener ((parent, view, position, id) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder (Objects.requireNonNull (getContext ()));
                builder.setCancelable (false);
                builder.setTitle ("Choose option");
                builder.setMessage ("Delete " + completedListItem.get (position).getTitle () + "?");
                builder.setPositiveButton ("Delete", (dialog, which) -> {
                    db.deleteData (completedListItem.get (position).getTitle ());
                    completedListItem.remove (position);
                    myTaskAdapter.notifyDataSetChanged ();
                    StyleableToast.makeText (getContext (), "deleted", R.style.myToast).show ();

                });
                builder.setNegativeButton ("Cancel", (dialog, which) -> {
                    dialog.dismiss ();
                    StyleableToast.makeText (getContext (), "cancelled", R.style.myToast1).show ();
                });
                builder.create ().show ();
            });
        }
        setHasOptionsMenu (true);
    }

    @Override
    public void onResume() {
        super.onResume ();
        completedListItem.clear ();
        DisplayData ();
        intent.removeExtra (SendToComplete.TITLE);
        intent.removeExtra (SendToComplete.TIME);

    }


    public class CompletedTaskadapter extends BaseAdapter {
        Context context;
        ArrayList<CompletedTaskClass> completedTask;

        public CompletedTaskadapter(Context c, ArrayList<CompletedTaskClass> completedTasks) {
            this.context = c;
            this.completedTask = completedTasks;

        }

        @Override
        public int getCount() {
            return completedTask.size ();
        }

        @Override
        public Object getItem(int position) {
            return completedTask.get (position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from (context).inflate (R.layout.completedtaskrow, parent, false);
            }
            CompletedTaskClass taskClass = (CompletedTaskClass) getItem (position);

            TextView title = convertView.findViewById (R.id.completedTasKTitle);
            TextView time = convertView.findViewById (R.id.completedTasKTime);

            title.setText (taskClass.getTitle ());
            time.setText (taskClass.getTime ());

            return convertView;
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu (menu, inflater);
        inflater.inflate (R.menu.com_delete_all, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.delete_all:
                Cursor cursor = db.ViewData ();
                if (cursor.getCount () == 0) {
                    StyleableToast.makeText (Objects.requireNonNull(getContext()), "no task completed yet", R.style.myToast1).show ();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder (Objects.requireNonNull(getContext()));
                    builder.setCancelable (false);
                    builder.setTitle ("Delete All Completed Tasks?");
                    builder.setMessage ("this operation cannot be undone when clicked, do you wish to continue?")
                            .setPositiveButton ("Yes", (dialog, which) -> {
                                db.deleteAllData ();
                                completedListItem.clear ();
                                ((BaseAdapter) CompletedtaskList.getAdapter ()).notifyDataSetChanged ();
                                DisplayData ();
                                StyleableToast.makeText (getContext (), "deleted",
                                        R.style.deleteallTasksToast).show ();
                            });
                    builder.setNegativeButton ("No", (dialog, which) -> {
                        dialog.dismiss ();
                        StyleableToast.makeText (getContext (), "Cancelled", R.style.myToast1).show ();
                    });
                    builder.create ().show ();
                }
        }
        return super.onOptionsItemSelected (item);
    }
}
