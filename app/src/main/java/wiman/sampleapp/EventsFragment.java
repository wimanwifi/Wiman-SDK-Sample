package wiman.sampleapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import me.wiman.logger.Logger;

import static wiman.sampleapp.MainActivity.logStream;


public class EventsFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private LogAdapter adapter;
    private Button logSet;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.events, container, false);

        Button btnSendLog = view.findViewById(R.id.btnSendLog);
        Button btnRemoveLog = view.findViewById(R.id.btnRemoveLog);
        view.findViewById(R.id.btnLevelLog).setOnClickListener(view -> setLevelLog());
        logSet = view.findViewById(R.id.btnLog);
        if (Logger.postLogIsEnabled())
            logSet.setText("Disable log");
        else
            logSet.setText("Enable log");

        logSet.setOnClickListener(view -> {
            if (Logger.postLogIsEnabled()) {
                Logger.setEnablePostLog(false);
                logSet.setText("Enable log");

            } else {
                Logger.setEnablePostLog(true);
                logSet.setText("Disable log");

            }
        });
        adapter = new LogAdapter(logStream);
        recyclerView = view.findViewById(R.id.lista);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        recyclerView.setItemAnimator(null);


        recyclerView.setAdapter(adapter);


        assert btnSendLog != null;
        btnSendLog.setOnClickListener(v -> sendMail());

        assert btnRemoveLog != null;
        btnRemoveLog.setOnClickListener(v -> {
            logStream.clear();
            adapter.notifyDataSetChanged();

        });

        return view;
    }

    private void setLevelLog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose level log");
        builder.setItems(R.array.level_log, (dialogInterface, i) -> {
            if (i == 0)
                Logger.setLevel(Logger.Level.INFO);
            else
                Logger.setLevel(Logger.Level.DEBUG);
        });
        builder.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Logger.LogEvent event) {
        update(event);

    }


    private void update(Logger.LogEvent log) {
        logStream.add(log);
        adapter.notifyItemInserted(logStream.size() - 1);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);


    }


    private void sendMail() {
        writeToSDFile(Arrays.toString(logStream.toArray()));
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"vincenzo.sarnataro@wiman.me"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Log SDK Demo");
        i.putExtra(Intent.EXTRA_TEXT, "Read Attachment");

        String dir = String.valueOf(getContext().getExternalFilesDir(null));
        String filename = "SDK_logs.txt";
        File filelocation = new File(dir, filename);
        Uri path = Uri.fromFile(filelocation);


        if (!filelocation.exists() || !filelocation.canRead()) {
            return;
        }

        i.putExtra(Intent.EXTRA_STREAM, path);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeToSDFile(String data) {

        if (isExternalStorageWritable() && isExternalStorageReadable()) {
            try {
                File dir = new File(String.valueOf(getContext().getExternalFilesDir(null)));
                dir.mkdirs();
                File file = new File(dir, "SDK_logs.txt");
                FileOutputStream f = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(f);
                pw.println(data);
                pw.flush();
                pw.close();
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

}