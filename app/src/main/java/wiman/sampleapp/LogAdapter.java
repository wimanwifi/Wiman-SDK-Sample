package wiman.sampleapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.wiman.logger.Logger;
import wiman.sampleapp.R;

/**
 * Created by vincenzosarnataro on 09/11/17.
 */

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogHolder> {
    private List<Logger.LogEvent> logs;

    public LogAdapter(List<Logger.LogEvent> logs) {
        this.logs = logs;
    }

    @Override
    public LogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LogHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.log_layout,null,false));
    }

    @Override
    public void onBindViewHolder(LogHolder holder, int position) {
        Logger.LogEvent event = logs.get(position);
        holder.currentTime.setText(String.format("%s: ", event.currentTime));
        holder.msg.setText(event.msg);
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    class LogHolder extends RecyclerView.ViewHolder {
        TextView msg;


        TextView currentTime;
        public LogHolder(View itemView) {
            super(itemView);
            currentTime = itemView.findViewById(R.id.time);

            msg = itemView.findViewById(R.id.log);

        }
    }
}
