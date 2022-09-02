package com.dailytask.manager.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dailytask.manager.R;
import com.dailytask.manager.data.MyTask;
import com.dailytask.manager.data.TaskViewModel;
import com.dailytask.manager.ui.CreateTaskActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class HomeFragment extends Fragment {
    private static final int ADD_NOTE_REQUEST = 1;
    private static final int EDIT_NOTE_REQUEST = 2;
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private TextView EndOfPage;
    private RelativeLayout emptyRecView;
    private InterstitialAd mInterstitialAd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate (R.layout.home_activity, container, false);

        //initialize mobile ads
        MobileAds.initialize (getContext (), initializationStatus -> {

        });

        //initial interstitial ad
        mInterstitialAd=new InterstitialAd (Objects.requireNonNull (getContext ()));
        //set unique ad id
        mInterstitialAd.setAdUnitId ("ca-app-pub-9571348963607028/6086621269");
        mInterstitialAd.loadAd (new AdRequest.Builder ().build ());

        // set adlistener to reload new ad
        mInterstitialAd.setAdListener (new AdListener (){
            @Override
            public void onAdClosed(){
                mInterstitialAd.loadAd (new AdRequest.Builder ().build ());
            }
        });

        FloatingActionButton buttonAddTask = v.findViewById (R.id.button_add_task);
        SearchView searchView = v.findViewById (R.id.SearchView);

        AdView mAdView = v.findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        EndOfPage = v.findViewById (R.id.endPage);
        emptyRecView=v.findViewById (R.id.emptyRecView);


        //search bar algorithm
        searchView.setQueryHint ("Search");
        searchView.setOnQueryTextListener (new SearchView.OnQueryTextListener () {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter ().filter (newText);
                return false;
            }

        });

        //navigates user to create task activity
        buttonAddTask.setOnClickListener (v12 -> {
            Intent myintent = new Intent (getActivity (), CreateTaskActivity.class);
            startActivityForResult (myintent, ADD_NOTE_REQUEST);
        });

        //set up recyclerview
        recyclerView = v.findViewById (R.id.TaskRecycler);
        LinearLayoutManager myManager = new LinearLayoutManager (getActivity ());
        recyclerView.setLayoutManager (myManager);
        recyclerView.setHasFixedSize (true);

        adapter = new TaskAdapter ();
        recyclerView.setAdapter (adapter);

        taskViewModel = ViewModelProviders.of (this).get (TaskViewModel.class);
        taskViewModel.getAllTasks ().observe (this, myTasks -> {
            //check if the list containing our model class object is null
            //if false,then display the "end of page" text if the list contains something
            if (myTasks.size () <= 0) {
                emptyRecView.setVisibility (View.VISIBLE);
                EndOfPage.setVisibility (View.INVISIBLE);
            }
            else{
                emptyRecView.setVisibility (View.GONE);
            }
            adapter.setTasks (myTasks);
            adapter.notifyDataSetChanged ();
        });

        //swipe delete function
        new ItemTouchHelper (new ItemTouchHelper.SimpleCallback (0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                if (direction == ItemTouchHelper.LEFT) {
                    final int adapterPosition = viewHolder.getAdapterPosition ();
                    final MyTask deletedTask = adapter.getTaskAt (adapterPosition);
                    taskViewModel.delete (deletedTask);

                    Snackbar.make (recyclerView, "Task deleted", Snackbar.LENGTH_LONG)
                            .setActionTextColor (getResources ().getColor (R.color.white))
                            .setAction ("Undo", v1 -> {
                                taskViewModel.insert (deletedTask);
                                adapter.notifyDataSetChanged ();
                                EndOfPage.setVisibility (View.VISIBLE);
                                adapter.notifyItemChanged (adapterPosition);
                            })
                            .show ();
                    return;
                }

                TaskAdapter.TaskHolder taskHolder = (TaskAdapter.TaskHolder) viewHolder;

            }


            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder (c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor (ContextCompat.getColor (Objects.requireNonNull (getActivity ()), R.color.red))
                        .addActionIcon (R.drawable.ic_delete_black_24dp)
                        .addSwipeLeftLabel ("delete")
                        .setSwipeLeftLabelColor (R.color.yellow)
                        .create ()
                        .decorate ();
                super.onChildDraw (c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView (recyclerView);

        //on item click listener
        adapter.setOnItemClickListener (task -> {
            Intent intent1 = new Intent (HomeFragment.this.getActivity (), CreateTaskActivity.class);
            intent1.putExtra (CreateTaskActivity.EXTRA_ID, task.getId ());
            intent1.putExtra (CreateTaskActivity.EXTRA_TITLE, task.getTitleTask ());
            intent1.putExtra (CreateTaskActivity.EXTRA_DESC, task.getDescription ());
            intent1.putExtra (CreateTaskActivity.EXTRA_TIME, task.getTaskTime ());

            HomeFragment.this.startActivityForResult (intent1, EDIT_NOTE_REQUEST);

        });
        return v;

    }

    //internet check
    public static boolean HasActiveNetworkConnection(Context context) {

        final ConnectivityManager manager = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            final Network network = manager.getActiveNetwork ();
            final NetworkCapabilities capabilities = manager.getNetworkCapabilities (network);

            return capabilities != null && capabilities.hasCapability (NetworkCapabilities
                    .NET_CAPABILITY_VALIDATED);
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            String title = data.getStringExtra (CreateTaskActivity.EXTRA_TITLE);
            String Des = data.getStringExtra (CreateTaskActivity.EXTRA_DESC);
            String Date = data.getStringExtra (CreateTaskActivity.EXTRA_TIME);

            MyTask myTask = new MyTask (title, Des, Date);
            taskViewModel.insert (myTask);
            //display ads
            try{
                if(mInterstitialAd.isLoaded ()){
                    mInterstitialAd.show ();
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            StyleableToast.makeText (Objects.requireNonNull (getContext ()), "Task created", R.style.myToast).show ();
            EndOfPage.setVisibility (View.VISIBLE);

        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            int id = data.getIntExtra (CreateTaskActivity.EXTRA_ID, -1);

            if (id == -1) {
                StyleableToast.makeText (Objects.requireNonNull (getContext ()), "Task can't be updated", R.style.myToast1).show ();
                return;
            }
            String title = data.getStringExtra (CreateTaskActivity.EXTRA_TITLE);
            String Des = data.getStringExtra (CreateTaskActivity.EXTRA_DESC);
            String Date = data.getStringExtra (CreateTaskActivity.EXTRA_TIME);

            MyTask myTask = new MyTask (title, Des, Date);
            myTask.setId (id);
            taskViewModel.update (myTask);
            StyleableToast.makeText (Objects.requireNonNull (getContext ()), "Task updated", R.style.myToast).show ();

        }
    }


    @Override
    public void onResume() {
        super.onResume ();
        Objects.requireNonNull (((AppCompatActivity) Objects.requireNonNull (getActivity ())).getSupportActionBar ()).hide ();

    }

    @Override
    public void onStop() {
        super.onStop ();
        Objects.requireNonNull (((AppCompatActivity) Objects.requireNonNull (getActivity ())).getSupportActionBar ()).show ();

    }
}
