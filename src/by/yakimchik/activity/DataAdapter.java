package by.yakimchik.activity;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class DataAdapter extends ArrayAdapter<String>{

	public DataAdapter(Context context, int textViewResourceId, List<String> characters) {
		// TODO Auto-generated constructor stub
		super(context, textViewResourceId, characters);
	}

}
