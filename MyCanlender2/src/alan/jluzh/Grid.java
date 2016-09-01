package alan.jluzh;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.widget.TextView;

public class Grid extends CalendarParent implements Serializable
{


	private String[] days = new String[42];
	// true表示有记录，false表示没有记录
	private boolean[] recordDays = new boolean[42];
	private String[] monthNames = new String[12];
	private TextView tvMsg1;
	private TextView tvMsg2;
	private TextView tvMsg3;
	private int dayColor;
	private int innerGridColor;
	private int prevNextMonthDayColor;
	private int currentDayColor;
	private int todayColor;
	private int todayBackgroundColor;
	private int sundaySaturdayPrevNextMonthDayColor;
	private float daySize;
	private float dayTopOffset;
	private float currentDaySize;
	private float cellX = -1, cellY = -1;
	// 从0开始
	private int currentRow, currentCol;
	private boolean redrawForKeyDown = false;

	// 当前年和月
	public int currentYear, currentMonth;
	// 上月或下月选中的天
	public int currentDay = -1, currentDay1 = -1, currentDayIndex = -1;
	private java.util.Calendar calendar = java.util.Calendar.getInstance();

	public void setCurrentRow(int currentRow)
	{
		if (currentRow < 0)
		{
			currentMonth--;
			if (currentMonth == -1)
			{
				currentMonth = 11;
				currentYear--;
			}
			currentDay = getMonthDays(currentYear, currentMonth) + currentDay
					- 7;
			currentDay1 = currentDay;
			cellX = -1;
			cellX = -1;
			view.invalidate();
			return;

		}
		else if (currentRow > 5)
		{
			int n = 0;
			for (int i = 35; i < days.length; i++)
			{
				if (!days[i].startsWith("*"))
					n++;
				else
					break;
			}
			currentDay = 7 - n + currentCol + 1;
			currentDay1 = currentDay;
			currentMonth++;
			if (currentMonth == 12)
			{
				currentMonth = 0;
				currentYear++;
			}
			cellX = -1;
			cellX = -1;
			view.invalidate();
			return;
		}
		this.currentRow = currentRow;
		redrawForKeyDown = true;
		view.invalidate();
	}


	public void setCurrentCol(int currentCol)
	{
		if (currentCol < 0)
		{
			if (currentRow == 0)
			{

				currentMonth--;

				if (currentMonth == -1)
				{
					currentMonth = 11;
					currentYear--;
				}
				currentDay = getMonthDays(currentYear, currentMonth);
				currentDay1 = currentDay;
				cellX = -1;
				cellX = -1;
				view.invalidate();
				return;
			}

			else
			{
				currentCol = 6;
				setCurrentRow(--currentRow);

			}
		}
		else if (currentCol > 6)
		{
			currentCol = 0;
			setCurrentRow(++currentRow);

		}
		this.currentCol = currentCol;
		redrawForKeyDown = true;
		view.invalidate();
	}

	public int getCurrentRow()
	{
		return currentRow;
	}

	public int getCurrentCol()
	{
		return currentCol;
	}

	public void setCellX(float cellX)
	{

		this.cellX = cellX;
	}

	public void setCellY(float cellY)
	{

		this.cellY = cellY;
	}

	private int getMonthDays(int year, int month)
	{
		month++;
		switch (month)
		{
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
			{
				return 31;
			}
			case 4:
			case 6:
			case 9:
			case 11:
			{
				return 30;
			}
			case 2:
			{
				if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
					return 29;
				else
					return 28;
			}
		}
		return 0;
	}

	private void calculateDays()
	{
		//将当前日历设为指定月份的第一天
		calendar.set(currentYear, currentMonth, 1);
		//获得指定月份的第1天是当前周的第几天
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		int monthDays = 0;
		int prevMonthDays = 0;
		//获得当前月有多少天
		monthDays = getMonthDays(currentYear, currentMonth);
		//如果当前月是一年中的第一个月，则获得上一年最后一月
		//(也就是12月)的天数
		if (currentMonth == 0)
			prevMonthDays = getMonthDays(currentYear - 1, 11);
		//否则，获得指定月上一月的天数
		else
			prevMonthDays = getMonthDays(currentYear, currentMonth - 1);
		//设置上一月显示在本月日历后面的天数
		for (int i = week, day = prevMonthDays; i > 1; i--, day--)
		{
			days[i - 2] = "*" + String.valueOf(day);
		}
		//设置指定月(在这里是当前月)的天数
		for (int day = 1, i = week - 1; day <= monthDays; day++, i++)
		{
			days[i] = String.valueOf(day);
			if (day == currentDay)
			{
				//获得当前日在days数组中的索引
				currentDayIndex = i;

			}
		}
		//设置下一月显示在本月日历后面的天数
		for (int i = week + monthDays - 1, day = 1; i < days.length; i++, day++)
		{
			days[i] = "*" + String.valueOf(day);
		}

	}

	public Grid(Activity activity, View view)
	{
		super(activity, view);

		tvMsg1 = (TextView) activity.findViewById(R.id.tvMsg1);
		tvMsg2 = (TextView) activity.findViewById(R.id.tvMsg2);
		tvMsg3 = (TextView) activity.findViewById(R.id.tvMsg3);
		dayColor = activity.getResources().getColor(R.color.day_color);
		todayColor = activity.getResources().getColor(R.color.today_color);
		todayBackgroundColor = activity.getResources().getColor(
				R.color.today_background_color);
		innerGridColor = activity.getResources().getColor(
				R.color.inner_grid_color);
		prevNextMonthDayColor = activity.getResources().getColor(
				R.color.prev_next_month_day_color);
		currentDayColor = activity.getResources().getColor(
				R.color.current_day_color);
		sundaySaturdayPrevNextMonthDayColor = activity.getResources().getColor(
				R.color.sunday_saturday_prev_next_month_day_color);
		daySize = activity.getResources().getDimension(R.dimen.day_size);
		dayTopOffset = activity.getResources().getDimension(
				R.dimen.day_top_offset);
		currentDaySize = activity.getResources().getDimension(
				R.dimen.current_day_size);
		monthNames = activity.getResources().getStringArray(R.array.month_name);
		paint.setColor(activity.getResources().getColor(R.color.border_color));

		currentYear = calendar.get(Calendar.YEAR);
		currentMonth = calendar.get(Calendar.MONTH);
	}

	private boolean isCurrentDay(int dayIndex, int currentDayIndex,
			Rect cellRect)
	{
		boolean result = false;
		if (redrawForKeyDown == true)
		{
			result = dayIndex == (7 * ((currentRow > 0) ? currentRow : 0) + currentCol);
			if (result)
				redrawForKeyDown = false;

		}
		else if (cellX != -1 && cellY != -1)
		{
			if (cellX >= cellRect.left && cellX <= cellRect.right
					&& cellY >= cellRect.top && cellY <= cellRect.bottom)
			{
				result = true;
			}
			else
			{
				result = false;
			}
		}
		else
		{
			result = (dayIndex == currentDayIndex);

		}
		if (result)
		{
			if (currentRow > 0 && currentRow < 6)
			{
				currentDay1 = currentDay;

			}
			currentDayIndex = -1;
			cellX = -1;
			cellY = -1;

		}
		return result;
	}

	// 更新当前日期的信息
	private void updateMsg(boolean today)
	{
		String monthName = monthNames[currentMonth];
		String dateString = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(currentYear, currentMonth, currentDay);

		dateString = sdf.format(calendar.getTime());
		ChineseCalendar lunar = new ChineseCalendar(calendar);
		String currentDate = "";
		currentDate += lunar.getChineseDateString();
//		int startIndex = currentDate.length()-2;
//		String showMonth = currentDate.substring(startIndex-2, startIndex);
//		String showDay = currentDate.substring(startIndex);
		monthName += "   本月第" + calendar.get(java.util.Calendar.WEEK_OF_MONTH)
				+ "周";
		tvMsg1.setText(monthName);
		if (today)
			dateString += "(今天)";
		dateString += "   本年第" + calendar.get(java.util.Calendar.WEEK_OF_YEAR)
				+ "周";
		tvMsg2.setText(dateString);
		tvMsg3.setText(currentDate);
	}

	public boolean inBoundary()
	{
		if (cellX < borderMargin
				|| cellX > (view.getMeasuredWidth() - borderMargin)
				|| cellY < top
				|| cellY > (view.getMeasuredHeight() - borderMargin))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	float top, left;

	@Override
	public void draw(Canvas canvas)
	{
		left = borderMargin;
		top = borderMargin + weekNameSize + weekNameMargin * 2 + 4;
		float calendarWidth = view.getMeasuredWidth() - left * 2;
		float calendarHeight = view.getMeasuredHeight() - top - borderMargin;
		float cellWidth = calendarWidth / 7;
		float cellHeight = calendarHeight / 6;
		paint.setColor(innerGridColor);
		canvas.drawLine(left, top, left + view.getMeasuredWidth()
				- borderMargin * 2, top, paint);
		// 画横线
		for (int i = 1; i < 6; i++)
		{
			 canvas.drawLine(left, top + (cellHeight) * i, left +
			 calendarWidth,
			 top + (cellHeight) * i, paint);
		}
		// 画竖线
		for (int i = 1; i < 7; i++)
		{
			 canvas.drawLine(left + cellWidth * i, top, left + cellWidth * i,
			 view.getMeasuredHeight() - borderMargin, paint);
		}

		// 画日期
		//填充days数组
		calculateDays();
		//定义calendar为日历对象
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		int day = calendar.get(calendar.DATE);
		int myYear = calendar.get(calendar.YEAR);
		int myMonth = calendar.get(calendar.MONTH);
		calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH),
				1);
		int week = calendar.get(calendar.DAY_OF_WEEK);
		int todayIndex = week + day - 2;
		boolean today = false;
		if (currentDayIndex == -1)
		{
			currentDayIndex = todayIndex;
		}
		boolean flag = false;
		calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH),
				1);
		SimpleDateFormat abc = new SimpleDateFormat("yyyy年M月d日");
		for (int a = 0; a < days.length; a++)
		{
			today = false;
			int row = a / 7;
			int col = a % 7;
			String text = String.valueOf(days[a]);
			// 如果日历是周日、周六，并且是上一月和下一月的周六和周
			//日
			if ((a % 7 == 0 || (a - 6) % 7 == 0) && text.startsWith("*"))
			{
				paint.setColor(sundaySaturdayPrevNextMonthDayColor);
			}
			else if (a % 7 == 0 || (a - 6) % 7 == 0)
			{
				paint.setColor(sundaySaturdayColor);
			}
			// 非周六周日但为上一月的日期
			else if (text.startsWith("*"))
			{
				paint.setColor(prevNextMonthDayColor);
			}
			else
			{
				//普通日历
				paint.setColor(dayColor);
			}
			//去掉上月一下月日期前的星号
			text = text.startsWith("*") ? text.substring(1) : text;
			Rect dst = new Rect();
			dst.left = (int) (left + cellWidth * col);
			dst.top = (int) (top + cellHeight * row);
			dst.bottom = (int) (dst.top + cellHeight + 1);
			dst.right = (int) (dst.left + cellWidth + 1);
			String myText = text;
//			if (recordDays[a])
//				myText = "*" + myText;
			paint.setTextSize(daySize);
			float textLeft = left + cellWidth * col
					+ (cellWidth - paint.measureText(myText)) / 2;
			float textLeft1 = left + cellWidth * col
			+ (cellWidth - paint.measureText(myText)) / 2 - 8;
			float textTop = top + cellHeight * row
					+ (cellHeight - paint.getTextSize()) / 2;
			float textTop1 = top + cellHeight * row
			+ (cellHeight - paint.getTextSize()) / 2 + dayTopOffset;
			if (myYear == currentYear && myMonth == currentMonth
					&& a == todayIndex)
			{
				paint.setTextSize(currentDaySize);
				paint.setColor(todayBackgroundColor);
				dst.left += 1;
				dst.top += 1;
				canvas.drawLine(dst.left, dst.top, dst.right, dst.top, paint);
				canvas.drawLine(dst.right, dst.top, dst.right, dst.bottom,
						paint);
				canvas.drawLine(dst.right, dst.bottom, dst.left, dst.bottom,
						paint);
				canvas.drawLine(dst.left, dst.bottom, dst.left, dst.top, paint);

				paint.setColor(todayColor);
				today = true;
			}

			if (isCurrentDay(a, currentDayIndex, dst) && flag == false)
			{
				if (days[a].startsWith("*"))
				{
					// 下月
					if (a > 20)
					{
						currentMonth++;
						if (currentMonth == 12)
						{
							currentMonth = 0;
							currentYear++;
						}
						view.invalidate();

					}
					// 上月
					else
					{
						currentMonth--;
						if (currentMonth == -1)
						{
							currentMonth = 11;
							currentYear--;
						}
						view.invalidate();

					}
					currentDay = Integer.parseInt(text);
					currentDay1 = currentDay;
					cellX = -1;
					cellY = -1;
					break;
				}
				else
				{
					paint.setTextSize(currentDaySize);
					flag = true;
					Bitmap bitmap = BitmapFactory.decodeResource(activity
							.getResources(), R.drawable.day);
					Rect src = new Rect();
					src.left = 0;
					src.top = 0;
					src.right = bitmap.getWidth();
					src.bottom = bitmap.getHeight();
					canvas.drawBitmap(bitmap, src, dst, paint);
					paint.setColor(currentDayColor);
					currentCol = col;
					currentRow = row;
					currentDay = Integer.parseInt(text);
					currentDay1 = currentDay;
					updateMsg(today);
				}
			}
			if (days[a].startsWith("*"))
			{
				// 下月
				if (a > 20)
				{
					String nowday = currentYear + "年" + (currentMonth+2) + "月" + myText + "日";
					java.util.Calendar currentDay = java.util.Calendar.getInstance();
					try {
						currentDay.setTime(abc.parse(nowday));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					ChineseCalendar lunar = new ChineseCalendar(currentDay);
					String currentDate = lunar.getChineseDateString();
					int startIndex = currentDate.length()-2;
					String showMonth = currentDate.substring(startIndex-2, startIndex);
					String showDay = currentDate.substring(startIndex);
					if (showDay.equals("初一")) {
						canvas.drawText(showMonth, textLeft1, textTop1, paint);
					} else {
						canvas.drawText(showDay, textLeft1, textTop1, paint);
					}
				}
				// 上月
				else
				{
					String nowday = currentYear + "年" + currentMonth + "月" + myText + "日";
					java.util.Calendar currentDay = java.util.Calendar.getInstance();
					try {
						currentDay.setTime(abc.parse(nowday));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					ChineseCalendar lunar = new ChineseCalendar(currentDay);
					String currentDate = lunar.getChineseDateString();
					int startIndex = currentDate.length()-2;
					String showMonth = currentDate.substring(startIndex-2, startIndex);
					String showDay = currentDate.substring(startIndex);
					if (showDay.equals("初一")) {
						canvas.drawText(showMonth, textLeft1, textTop1, paint);
					} else {
						canvas.drawText(showDay, textLeft1, textTop1, paint);
					}
				}
			}else{
			//定义当前日期
			String nowday = currentYear + "年" + (currentMonth+1) + "月" + myText + "日";
//			System.out.println("nowday--->"+nowday);
			//定义currentDay为日历对象
			java.util.Calendar currentDay = java.util.Calendar.getInstance();
			try {
				//定义传入ChineseCalendar参数的格式
				currentDay.setTime(abc.parse(nowday));
//				System.out.println("currentDay:"+currentDay);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//创建ChineseCalendar对象并传入当前日期（年、月、日）
			ChineseCalendar lunar = new ChineseCalendar(currentDay);
			//获取传入日期的农历
			String currentDate = lunar.getChineseDateString();
			System.out.println("currentDate:"+currentDate);
			int startIndex = currentDate.length()-2;
			//获取农历月份
			String showMonth = currentDate.substring(startIndex-2, startIndex);
			//获取农历的日
			String showDay = currentDate.substring(startIndex);
			//当农历的日为“初一时”，画出该天的月份
			//否则画出该天农历的日
			if (showDay.equals("初一")) {
				canvas.drawText(showMonth, textLeft1, textTop1, paint);
			} else {
				canvas.drawText(showDay, textLeft1, textTop1, paint);
			}
		}
			//画出阳历
			canvas.drawText(myText, textLeft, textTop, paint);
		}
	}
}
