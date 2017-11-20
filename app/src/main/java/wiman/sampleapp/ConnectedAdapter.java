package wiman.sampleapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import me.wiman.listener.WimanSDK;

public class ConnectedAdapter extends ArrayAdapter<WimanSDK.Network> {

	public ConnectedAdapter(Context context, int resource, List<WimanSDK.Network> objects) {
		super(context, resource, objects);
		this.networks = objects;
		this.vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private LayoutInflater vi;
	private List<WimanSDK.Network> networks;

	@Override
	public int getCount() {
		return this.networks.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;

		if (v == null) {
			v = vi.inflate(android.R.layout.simple_list_item_2, null);
		}
		WimanSDK.Network network = getItem(position);

		if (network != null) {
			TextView title = v.findViewById(android.R.id.text1);
			if (title != null) {
				title.setText(network.ssid);
				title.setTextColor(Color.BLACK);
			}

			long millis = network.totalTime;
			String hms = String.format(Locale.ENGLISH,"%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS
				.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
																																																																	.toMinutes(millis)));

			TextView body = v.findViewById(android.R.id.text2);
			if (body != null) {
				String message = network.bssid + " - "   + hms + " - #" + network.times;
				body.setText(message);
				body.setTextColor(Color.BLACK);
			}
		}

		return v;
	}
}
