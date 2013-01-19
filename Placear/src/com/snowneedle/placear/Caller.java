package com.snowneedle.placear;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Caller {
	
	Context _context;
	
	public Caller(Context context){
		_context = context;
	}
	
	public void placeCall(String number){
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + number));
		_context.startActivity(callIntent);
	}
}
