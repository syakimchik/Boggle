package by.yakimchik.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import by.yakimchik.threads.TimerThread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements AdapterView.OnItemClickListener{
	
	private TextView mWordLabel;
	private ArrayList<String> charList;
	private static int prev_pos;
	private int mass[][];
	
	private int mLevel;
	
	private ArrayList<String> mWordAndroidList;
	
	private ProgressBar mProgressBar;
	private TimerThread mTimerThread;
	
	private AndroidSearchWordThread mSearchThread;
	
	private Button mSearchButton;
	
	private TextView androidTextView;
		
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
		
		mLevel = MainActivity.mLevel;
		
		if(mLevel==1){
			mLevel = 3;
		}
		else{
			if(mLevel==2){
				mLevel = 5;
			}
			else{
				if(mLevel==3){
					mLevel=9;
				}
			}
		}
		
		final GridView mGridView = (GridView) findViewById(R.id.GridView);
		
		androidTextView = (TextView) findViewById(R.id.androidTextView);
		
		String[] mCharacters = {
				"�", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�", 
				"�", "�", "�", "�", "�", "�", "�", "�", "�", "�", "�", 
				"�", "�", "�", "�", "�", "�", "�", "�", "�", "�" 
			};
		
		ArrayList<String> charactersList = new ArrayList<String>();
		for(int i=0; i<mCharacters.length; i++){
			charactersList.add(mCharacters[i]);
			charactersList.add(mCharacters[i]);
			charactersList.add(mCharacters[i]);
		}
		
		Collections.shuffle(charactersList);
		Collections.shuffle(charactersList);
		Collections.shuffle(charactersList);
		
		mWordAndroidList = new ArrayList<String>();
		charList = new ArrayList<String>();
		for(int i=0; i<16; i++){
			//charList.add(charactersList.get(i));
		}
		
		charList.add("�");
		charList.add("�");
		charList.add("�");
		charList.add("�");
		charList.add("�");
		charList.add("�");
		charList.add("�");
		charList.add("�");
		charList.add("�");
		charList.add("�");
		charList.add("�");
		charList.add("�");
		charList.add("�");
		charList.add("�");
		charList.add("�");
		charList.add("�");
		
		
		DataAdapter mAdapter = new DataAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, charList);
		mGridView.setAdapter(mAdapter);
		
		mWordLabel = (TextView) findViewById(R.id.wordTextView);
		
		final Button mClearButton = (Button) findViewById(R.id.cancelButton);
		mSearchButton = (Button) findViewById(R.id.okButton);
		
		mClearButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWordLabel.setText(R.string.points);
			}
		});
		
		mass = new int[4][4];
		int counter = 0;
		
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				mass[i][j] = counter++;
			}
		}
		
		mGridView.setOnItemClickListener(this);
		
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		mTimerThread = new TimerThread(handler);
		mTimerThread.start();
		
		String board[][] = new String[4][4];
		int k=0;
		
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				try{
					board[i][j] = charList.get(k).toString().toUpperCase();
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				k++;
			}
		}
		
		mSearchThread = new AndroidSearchWordThread(mSearchHandler);
		mSearchThread.setBoard(board);
		mSearchThread.setLeval(mLevel);
		mSearchThread.start();
	}

	public void onItemClick(AdapterView<?> view, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		if(!mWordLabel.getText().toString().equals("...")){
			
			int counter = 0, i_prev = 0, j_prev=0, i_new = 0, j_new=0;
			
			for(int i=0; i<4; i++){
				for(int j=0; j<4; j++){
					if(counter==position){
						i_new= i;
						j_new = j;
					}
					if(counter==prev_pos){
						i_prev = i;
						j_prev = j;
					}
					counter++;
				}
			}
			
			if((Math.abs(i_new-i_prev)<=1 && Math.abs(i_new-i_prev)>=0) && 
					(Math.abs(j_new-j_prev)<=1 && Math.abs(j_new-j_prev)>=0)){
				mWordLabel.append(charList.get(position));
				prev_pos = position;
			}
			else{
				Toast.makeText(getApplicationContext(), R.string.text, Toast.LENGTH_SHORT).show();
			}
		}
		else{
			mWordLabel.setText(charList.get(position));
			prev_pos = position;
		}
	}
	
	final Handler handler = new Handler(){
		public void handleMessage(Message msg){
			int total = msg.getData().getInt("Total");
			mProgressBar.setProgress(total);
			if(total==100){
				//dismissDialog(0);
				mTimerThread.setState(TimerThread.STATE_DONE);
				mTimerThread.stop();
				Toast.makeText(getApplicationContext(), "Time is finished", Toast.LENGTH_SHORT).show();
				mSearchButton.setEnabled(false);
			}
		}
	};
	
	final Handler mSearchHandler = new Handler(){
		public void handleMessage(Message msg){
			androidTextView.setText("Android �����! ");
			
			mWordAndroidList= msg.getData().getStringArrayList("Worlds");
			
			if(!mWordAndroidList.isEmpty()){
				for(String str: mWordAndroidList)
					androidTextView.append(str+"\n");
			}
			else
				androidTextView.append("\n��� ����!");
			mSearchThread.stop();
		}
	};
	
	private class AndroidSearchWordThread extends Thread{
		
		private String board[][];
		
		private int leval;
		
		private Handler mHandler;
		
		private ArrayList<String> mWordList;
		
		public AndroidSearchWordThread(Handler h) {
			mWordList = new ArrayList<String>();
			
			mHandler = h;
			
			board = new String[4][4];
		}
		
		public void run(){
			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			
			try {
				InputStream inStream = openFileInput("wolds.txt");
				if(inStream!=null){
					InputStreamReader tmp = new InputStreamReader(inStream);
					BufferedReader reader = new BufferedReader(tmp);
					String str;
					//boolean first = 0;
					while((str=reader.readLine())!=null){
						int length = str.length();
						int i_prev = 0, j_prev = 0, k=0;
						
						if(length>leval)
							continue;
						
						boolean[][] bp = new boolean[4][4];
						
						for(int i=0; i<4; i++){
							for(int j=0; j<4; j++){
								bp[i][j] = false;
							}
						}
						
						for(int i=0; i<4; i++)
						{
							if(k==length){
								mWordList.add(str);
								break;
							}
 							for(int j=0; j<4; j++){
								String s = board[i][j].toLowerCase();
								String s1 = String.valueOf(str.charAt(k));
								if(s.equals(s1)){
									if(k==0){
										i_prev = i;
										j_prev = j;
										bp[i][j] = true;
										i=-1;
										k++;
										break;
									}
									else{
										if(((Math.abs(j-j_prev)<=1 && Math.abs(i-i_prev)<=1)) && bp[i][j]==false){
											i_prev = i;
											j_prev = j;
											bp[i][j]=true;
											i=-1;
											k++;
											break;
										}
									}
								}
							}
						}
					}
				}
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			b.putStringArrayList("Worlds", mWordList);
			msg.setData(b);
			mHandler.sendMessage(msg);
		}
		
		public void setBoard(String[][] _board){
			board = _board;
		}
		
		public void setLeval(int _leval){
			leval = _leval;
		}
	}

}
