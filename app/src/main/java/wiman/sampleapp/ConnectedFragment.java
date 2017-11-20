package wiman.sampleapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import me.wiman.listener.WimanSDK;

public class ConnectedFragment extends Fragment {
    private  View view;
    private  TextView totalNetworks;
    private  TextView totalTime;

    public  void update(Context context) {
        List<WimanSDK.Network> list = WimanSDK.getConnected(getContext());

        if (view != null) {
            ListView listViewNearby = view.findViewById(R.id.listViewNearby);
            List<WimanSDK.Network> connecteds = new ArrayList<>();
            ConnectedAdapter connectedAdapter = new ConnectedAdapter(context, android.R.layout.simple_list_item_2, connecteds);
            assert listViewNearby != null;
            listViewNearby.setAdapter(connectedAdapter);

            connecteds.clear();

            if (list != null)
                connecteds.addAll(list);

            connectedAdapter.notifyDataSetChanged();

            totalNetworks = view.findViewById(R.id.totalNetworks);
            totalTime = view.findViewById(R.id.totalTime);

            int size = (list != null) ? list.size() : 0;
            int totalTimeValue = 0;
            double totalBytesValue = 0;

            if (list != null)
                for (WimanSDK.Network networksConnected : list) {
                    totalTimeValue = totalTimeValue + (int) networksConnected.totalTime;
                    totalBytesValue = totalBytesValue + networksConnected.totalBytes;
                }


            String hms = String.format(Locale.ENGLISH,"%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(size), TimeUnit.MILLISECONDS.toMinutes(totalTimeValue) - TimeUnit.HOURS
                    .toMinutes(TimeUnit.MILLISECONDS.toHours(totalTimeValue)), TimeUnit.MILLISECONDS.toSeconds(totalTimeValue) - TimeUnit.MINUTES
                    .toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTimeValue)));
            totalNetworks.setText(String.format("%s#", String.valueOf(size)));
            totalTime.setText(hms);
        }
    }



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

        update(getContext());

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.connected, container, false);
        totalNetworks = view.findViewById(R.id.totalNetworks);
        totalTime = view.findViewById(R.id.totalTime);


        return view;
    }

}