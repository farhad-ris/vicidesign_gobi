package com.example.gobi;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Convenience class for making pretty and consistent Toasts.
 */
public class GobiToast {
	public GobiToast(Context context, String text) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		View layout = inflater.inflate(R.layout.toast,  null);
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		TextView textField = (TextView) layout.findViewById(R.id.toastText);
		textField.setText(text);
		toast.show();
	}
}
