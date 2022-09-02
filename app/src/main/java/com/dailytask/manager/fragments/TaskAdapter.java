package com.dailytask.manager.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dailytask.manager.R;
import com.dailytask.manager.data.MyTask;
import com.dailytask.manager.ui.CreateTaskActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> implements Filterable {
    private onTaskItemClickListener listener;
    private List<MyTask> tasks;
    private List<MyTask> taskList = new ArrayList<> ();


    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_items, parent, false);
        return new TaskHolder (itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        MyTask currentTask = taskList.get (holder.getAdapterPosition ());
        String text = currentTask.getTaskTime ();
        holder.textViewTitle.setText (currentTask.getTitleTask ());
        holder.Description.setText (currentTask.getDescription ());

        //check if user's default time is in 24 hours format
        if (CreateTaskActivity.is24hoursFormat (holder.itemView.getContext ())) {
            SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yy,HH:mm", Locale.getDefault ());
            try {
                //format the text to a date
                Date date = sdf.parse (text);
                Calendar calendar = Calendar.getInstance ();

                assert date != null;
                calendar.setTime (date);
                //check if the date is tomorrow
                if (CreateTaskActivity.isTomorrow (calendar.getTime ())) {
                    text = "tomorrow, ";
                    String format = java.text.DateFormat.getTimeInstance (java.text.DateFormat.SHORT).format (calendar.getTime ());
                    String Time = String.format ("%s%s", text, format);
                    holder.Date.setText (Time);
                }
                //check if the date is today
                else if (CreateTaskActivity.isToday (calendar.getTime ())) {
                    text = "today, ";
                    String format = java.text.DateFormat.getTimeInstance (java.text.DateFormat.SHORT).format (date);
                    String Time = String.format ("%s%s", text,format);
                    holder.Date.setText (Time);

                }
                else{
                    holder.Date.setText (currentTask.getTaskTime ());
                }

            } catch (ParseException e) {
                e.printStackTrace ();
            }
        }
        else{

                SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yy,hh:mm a", Locale.getDefault ());
                try {
                    //format the text to a date
                    Date date = sdf.parse (text);
                    Calendar calendar = Calendar.getInstance ();

                    assert date != null;
                    calendar.setTime (date);
                    //check if the date is tomorrow
                    if (CreateTaskActivity.isTomorrow (calendar.getTime ())) {
                        text = "tomorrow, ";
                        String format = java.text.DateFormat.getTimeInstance (java.text.DateFormat.SHORT).format (calendar.getTime ());
                        String Time = String.format ("%s%s", text, format);
                        holder.Date.setText (Time);
                    }
                    //check if the date is today
                    else if (CreateTaskActivity.isToday (calendar.getTime ())) {
                        text = "today, ";
                        String format = java.text.DateFormat.getTimeInstance (java.text.DateFormat.SHORT).format (calendar.getTime ());
                        String Time = String.format ("%s%s", text, format);
                        holder.Date.setText (Time);
                    } else {
                        holder.Date.setText (currentTask.getTaskTime ());
                    }
                } catch (ParseException e) {
                    e.printStackTrace ();
                }
            }
        }


    @Override
    public int getItemCount() {
        if(taskList != null) return taskList.size();
        else  return 0;
    }

    @Override
    public Filter getFilter() {
        return taskFilter;
    }

    private final Filter taskFilter = new Filter () {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MyTask> filteredTask = new ArrayList<> ();

            if (constraint == null || constraint.length () == 0) {
                filteredTask = tasks;
            } else {
                String filterPattern = constraint.toString ().toLowerCase ().trim ();

                for (MyTask task : tasks) {
                    if (task.getTitleTask ().toLowerCase ().contains (filterPattern) ||
                            task.getDescription ().toLowerCase ().contains (filterPattern)) {
                        filteredTask.add (task);
                    }
                }
            }
            FilterResults filterResults = new FilterResults ();
            filterResults.values = filteredTask;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            taskList = (List<MyTask>) results.values;
            notifyDataSetChanged ();
        }
    };

    public MyTask getTaskAt(int position) {
        return taskList.get (position);
    }

    public void setTasks(List<MyTask> Tasks) {
        this.tasks = Tasks;
        taskList = tasks;
    }

    class  TaskHolder extends  RecyclerView.ViewHolder{

        private final TextView textViewTitle;
        private final TextView Description;
        private final TextView Date;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById (R.id.titleTask);
            Description = itemView.findViewById (R.id.description);
            Date = itemView.findViewById (R.id.TaskDate);

            itemView.setOnClickListener(view -> {
                int position = TaskHolder.this.getAdapterPosition ();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(taskList.get(position));
                }
            });

        }
    }

    public interface onTaskItemClickListener {
        void onItemClick(MyTask myTask);

    }


    public void setOnItemClickListener(onTaskItemClickListener listener) {
        this.listener = listener;
    }


}
