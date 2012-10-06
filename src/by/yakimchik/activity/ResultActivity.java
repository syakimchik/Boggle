package by.yakimchik.activity;

import java.util.ArrayList;

import by.yakimchik.data.GameWords;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ResultActivity extends Activity{
	
	private ArrayList<String> mWordAndroidList;
	private ArrayList<String> mYouWordList;
	
	private int mAdroidPoints;
	private int mUserPoints;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        
        mWordAndroidList = GameWords.AndroidWordsList;
        mYouWordList = GameWords.UserWordList;
        
        mAdroidPoints = 0;
        mUserPoints = 0;
        
        EditText androidTextView = (EditText) findViewById(R.id.editText2);
        EditText userTextView = (EditText) findViewById(R.id.editText1);
        
        TextView userPointsTextView = (TextView) findViewById(R.id.userPointsTextView);
        TextView androidPointsTextView = (TextView) findViewById(R.id.androidPointsTextView);
        
        TextView winnerTextView = (TextView) findViewById(R.id.winnerTextView);
        
        if(!mWordAndroidList.isEmpty()){
        	for(String elem: mWordAndroidList){
        		int points = 0;
        		int len = elem.length(); 
        		if(len==2){
        			points = 1;
        		}
        		if(len==3){
        			points = 2;
        		}
        		if(len==4){
        			points = 4;
        		}
        		if(len>=5 && len<8){
        			points = 6;
        		}
        		if(len>=8){
        			points = 10;
        		}
        		mAdroidPoints+=points;
        		androidTextView.append(elem+" - "+points+"оч.\n");
        	}
        }
        
        if(!mYouWordList.isEmpty()){
        	for(String elem: mYouWordList){
        		int points = 0;
        		int len = elem.length(); 
        		if(len==2){
        			points = 1;
        		}
        		if(len==3){
        			points = 2;
        		}
        		if(len==4){
        			points = 4;
        		}
        		if(len>=5 && len<8){
        			points = 6;
        		}
        		if(len>=8){
        			points = 10;
        		}
        		mUserPoints+=points;
        		userTextView.append(elem+" - "+points+"оч.\n");
        	}
        }
        
        androidPointsTextView.setText(mAdroidPoints+" очков");
        userPointsTextView.setText(mUserPoints+" очков");
        
        if(mAdroidPoints>mUserPoints){
        	winnerTextView.setText(R.string.androidWinner);
        }
        if(mAdroidPoints<mUserPoints){
        	winnerTextView.setText(R.string.youWinner);
        }
        if(mAdroidPoints==mUserPoints){
        	winnerTextView.setText(R.string.standoff);
        }
	}
}
