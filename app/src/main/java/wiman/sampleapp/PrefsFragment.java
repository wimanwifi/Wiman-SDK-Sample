package wiman.sampleapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import me.wiman.listener.WimanSDK;
import me.wiman.listener.WimanSdkNetworksListener;

public class PrefsFragment extends Fragment {


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.prefs, container, false);

        final CheckBox checkBoxAutoConnect = view.findViewById(R.id.checkbox_autoconnect);

        final WimanSDK.Prefs prefs = WimanSDK.getPrefs(getContext());


        final boolean autoConnecteEnable = prefs.autoConnectEnable;

        checkBoxAutoConnect.setChecked(autoConnecteEnable);

        checkBoxAutoConnect.setOnCheckedChangeListener((compoundButton, b) -> WimanSDK.setAutoconnect(getActivity(), b));


        view.findViewById(R.id.networksDown).setOnClickListener(view1 -> showDialog());
        view.findViewById(R.id.mapShow).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MapFragment.class);
            startActivity(intent);
        });

        return view;
    }

    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(lp);
        input.setGravity(Gravity.CENTER);
        alertDialog.setView(input);

        alertDialog.setTitle("Download Contry");

        alertDialog.setMessage("Insert ISO 3166-1 alpha-2 Country Code");


        alertDialog.setPositiveButton("Download",
                (dialog, which) -> downloadNetwork(input.getText().toString()));

        alertDialog.setNegativeButton("Cancel",
                (dialog, which) -> dialog.cancel());


        alertDialog.show();
    }

    private void downloadNetwork(String contryCode) {
        Toast.makeText(getContext(), "Downloading", Toast.LENGTH_SHORT).show();

        WimanSDK.downloadNetwork(getContext(), contryCode, new WimanSdkNetworksListener() {
            @Override
            public void onSuccess() {

                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();

            }
        });

    }


}