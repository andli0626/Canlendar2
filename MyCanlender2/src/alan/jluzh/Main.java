package alan.jluzh;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.widget.LinearLayout;

public class Main extends Activity
{
	private CalendarView calendarView;	
	public static Activity activity;
	public static Vibrator vibrator;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (activity == null)
		{
			activity = this;
		}

		LinearLayout mainLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.main, null);
		setContentView(mainLayout);
		calendarView = new CalendarView(this);
		mainLayout.addView(calendarView);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		calendarView.onKeyDown(keyCode, event);
		return super.onKeyDown(keyCode, event);
	}

}