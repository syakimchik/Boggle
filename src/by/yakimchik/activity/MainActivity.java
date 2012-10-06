package by.yakimchik.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private final CharSequence[] mLevels = {
		"Лёгкий", "Средний", "Профессионал"
	};
	
	public static int mLevel = 1;
	
	private int _level = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final Button mNewGameButton = (Button) findViewById(R.id.NewGameButton);
        
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) { 
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), GameActivity.class);
				startActivity(intent);
			}
		});
        
        final Button mExitButton = (Button) findViewById(R.id.ExitButton);
        
        mExitButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        
        try{
        	InputStream inStream = getResources().openRawResource(R.raw.dict);
        	BufferedReader r = new BufferedReader(new InputStreamReader(inStream));
        	String str;
        	OutputStreamWriter outStream = new OutputStreamWriter(openFileOutput("words.txt", 0));
        	outStream.write("");
        	while((str=r.readLine())!=null){
        		outStream.append(str.toString()+"\n");
        	}
        	inStream.close();
        	outStream.close();
        }
        catch (Throwable e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
        
        final Button mSelectLevelButton = (Button) findViewById(R.id.SelectLevelButton);
        
        mSelectLevelButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(0);
			}
		});
        
        final Button mAboutButton = (Button) findViewById(R.id.AboutAppButton);
        mAboutButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), AboutActivity.class);
				startActivity(intent);
			}
		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    protected Dialog onCreateDialog(int id){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Выбор уровня");
    	builder.setSingleChoiceItems(mLevels, 0, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int item) {
				// TODO Auto-generated method stub
				_level = item; 
			}
		});
    	
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int item) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Выбран уровень: "+mLevels[_level], 
						Toast.LENGTH_SHORT).show();
				mLevel = _level+1;
			}
		});
    	
    	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
    	
    	builder.setCancelable(false);
    	return builder.create();
    }
}
