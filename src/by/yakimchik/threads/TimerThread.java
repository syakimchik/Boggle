package by.yakimchik.threads;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TimerThread extends Thread{

	public final static int STATE_DONE = 0;
	public final static int STATE_RUNNING = 1;
	
	private Handler mHandler;
	private int mState;
	private int mTotal;
	
	public TimerThread(Handler h) {
		// TODO Auto-generated constructor stub
		mHandler = h;
	}
	
	public void run(){
		mState = STATE_RUNNING;
		mTotal = 0;
		
		while(mState==STATE_RUNNING){
			try{
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				// TODO: handle exception
				Log.e("ERROR", "Thread Interrupted");
			}
			
			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putInt("Total", mTotal);
			msg.setData(b);
			mHandler.sendMessage(msg);
			mTotal++;
		}
	}
	
	public void setState(int state){
		mState = state;
	}
}
