package com.yuta.clocktime.widiget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.RemoteViews.RemoteView;

import com.yuta.clocktime.R;

/**
 * This widget display an analogic clock with two hands for hours and
 * minutes.
 */
@SuppressLint("Recycle")
@RemoteView
@SuppressWarnings("deprecation")
public class ClockView extends View {
	private Time mCalendar;
	
    private Drawable mHourHand;
    private Drawable mMinuteHand;
//    private Drawable mSecondHand;
    private Drawable mDial;
    //载入的布景长宽
    private int mDialWidth;
    private int mDialHeight;

    private float mSeconds;
    private float mMinutes;
    private float mHour;
    
    private boolean mAttached;
    //父布局的长宽
    private int mParentWidth;
    private int mParentHeight;
    
    private TimeZone mTimezone;

    Context mContext;

    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    
	public ClockView(Context context, AttributeSet attrs,
                       int defStyle) {
        super(context, attrs, defStyle);
        Resources r = context.getResources();
        context.obtainStyledAttributes(
		        attrs, R.styleable.ClockView, defStyle, 0);
        mContext=context;
       // mDial = a.getDrawable(com.android.internal.R.styleable.AnalogClock_dial);
       // if (mDial == null) {
            mDial = r.getDrawable(R.drawable.clock_dial);
       // }

      //  mHourHand = a.getDrawable(com.android.internal.R.styleable.AnalogClock_hand_hour);
      //  if (mHourHand == null) {
            mHourHand = r.getDrawable(R.drawable.clock_hour);
      //  }

     //   mMinuteHand = a.getDrawable(com.android.internal.R.styleable.AnalogClock_hand_minute);
     //   if (mMinuteHand == null) {
            mMinuteHand = r.getDrawable(R.drawable.clock_minute);
//            mSecondHand = r.getDrawable(R.drawable.clockgoog_minute);
     //   }
        
        mCalendar = new Time();
        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();
        
        mTimezone = TimeZone.getDefault();
    }
	
	@Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter();

            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction("com.yuta.clocktime.ChooseTimeZoneActivity.TIMEZONE_CHANGED");

            // OK, this is gross but needed. This class is supported by the
            // remote views machanism and as a part of that the remote views
            // can be inflated by a context for another user without the app
            // having interact users permission - just for loading resources.
            // For exmaple, when adding widgets from a user profile to the
            // home screen. Therefore, we register the receiver as the current
            // user not the one the context is for.
            getContext().registerReceiver(receiver, filter);
        }

        // NOTE: It's safe to do these after registering the receiver since the receiver always runs
        // in the main thread, therefore the receiver can't run before this method returns.

        // The time zone may have changed while the receiver wasn't registered, so update the Time
        mCalendar = new Time();

        // Make sure we update to the current time
        onTimeChanged();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	
    	mParentWidth = MeasureSpec.getSize(widthMeasureSpec);
    	mParentHeight = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize =  MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize =  MeasureSpec.getSize(heightMeasureSpec);

        float hScale = 1.0f;
        float vScale = 1.0f;
        //如果父布局小于布景图片，则要进行缩放
        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float )heightSize / (float) mDialHeight;
        }

        float scale = Math.min(hScale, vScale);

        setMeasuredDimension(resolveSize((int) (mDialWidth * scale), widthMeasureSpec),
                resolveSize((int) (mDialHeight * scale), heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        //Here you can set the size of your clock
        int availableWidth = mParentWidth;
        int availableHeight = mParentHeight;

        //Actual size
        int x = availableWidth / 2;
        int y = availableHeight / 2;

        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();

        boolean scaled = false;

        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w,
                                   (float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }
        //从availabel size的中心进行绘制
        dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        dial.draw(canvas);

        canvas.save();
        canvas.rotate(mHour / 12.0f * 360.0f, x, y);
        w = mHourHand.getIntrinsicWidth();
        h = mHourHand.getIntrinsicHeight();
        mHourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        mHourHand.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);
        w = mMinuteHand.getIntrinsicWidth();
        h = mMinuteHand.getIntrinsicHeight();
        mMinuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        mMinuteHand.draw(canvas);
        canvas.restore();

//        canvas.save();
//        canvas.rotate(mSeconds, x, y);
//        w = mSecondHand.getIntrinsicWidth();
//        h = mSecondHand.getIntrinsicHeight();
//        mSecondHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
//        mSecondHand.draw(canvas);
//        canvas.restore();
        if (scaled) {
            canvas.restore();
        }
    }
    
    private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.yuta.clocktime.ChooseTimeZoneActivity.TIMEZONE_CHANGED")) {
                String tz = intent.getStringExtra("time-zone");
                mTimezone = TimeZone.getTimeZone(TimeZone.getTimeZone(tz).getID());
                mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
            }
			
            onTimeChanged();
            
            invalidate();
		}
	};

	

    public void setTime(int hours, int minutes, int seconds)
    {
//        mSeconds = 6.0f*seconds;
        mMinutes = minutes;
        mHour = hours+minutes/60.0f;
        
    }

	protected void onTimeChanged() {
		String[] values = getCurrentTime().split(":");

        int hour = Integer.valueOf(values[0]);
        int minute = Integer.valueOf(values[1]);
        int second = Integer.valueOf(values[2]);

        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;
		
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mAttached) {
            getContext().unregisterReceiver(receiver);
            mAttached = false;
        }
	}
	
	private String getCurrentTime(){
		TimeZone defTimeZone = mTimezone;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		sdf.setTimeZone(defTimeZone);
		String time = sdf.format(new Date());
		return time;
	}
}