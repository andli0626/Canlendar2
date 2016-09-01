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
	// true��ʾ�м�¼��false��ʾû�м�¼
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
	// ��0��ʼ
	private int currentRow, currentCol;
	private boolean redrawForKeyDown = false;

	// ��ǰ�����
	public int currentYear, currentMonth;
	// ���»�����ѡ�е���
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
		//����ǰ������Ϊָ���·ݵĵ�һ��
		calendar.set(currentYear, currentMonth, 1);
		//���ָ���·ݵĵ�1���ǵ�ǰ�ܵĵڼ���
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		int monthDays = 0;
		int prevMonthDays = 0;
		//��õ�ǰ���ж�����
		monthDays = getMonthDays(currentYear, currentMonth);
		//�����ǰ����һ���еĵ�һ���£�������һ�����һ��
		//(Ҳ����12��)������
		if (currentMonth == 0)
			prevMonthDays = getMonthDays(currentYear - 1, 11);
		//���򣬻��ָ������һ�µ�����
		else
			prevMonthDays = getMonthDays(currentYear, currentMonth - 1);
		//������һ����ʾ�ڱ����������������
		for (int i = week, day = prevMonthDays; i > 1; i--, day--)
		{
			days[i - 2] = "*" + String.valueOf(day);
		}
		//����ָ����(�������ǵ�ǰ��)������
		for (int day = 1, i = week - 1; day <= monthDays; day++, i++)
		{
			days[i] = String.valueOf(day);
			if (day == currentDay)
			{
				//��õ�ǰ����days�����е�����
				currentDayIndex = i;

			}
		}
		//������һ����ʾ�ڱ����������������
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

	// ���µ�ǰ���ڵ���Ϣ
	private void updateMsg(boolean today)
	{
		String monthName = monthNames[currentMonth];
		String dateString = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��");
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(currentYear, currentMonth, currentDay);

		dateString = sdf.format(calendar.getTime());
		ChineseCalendar lunar = new ChineseCalendar(calendar);
		String currentDate = "";
		currentDate += lunar.getChineseDateString();
//		int startIndex = currentDate.length()-2;
//		String showMonth = currentDate.substring(startIndex-2, startIndex);
//		String showDay = currentDate.substring(startIndex);
		monthName += "   ���µ�" + calendar.get(java.util.Calendar.WEEK_OF_MONTH)
				+ "��";
		tvMsg1.setText(monthName);
		if (today)
			dateString += "(����)";
		dateString += "   �����" + calendar.get(java.util.Calendar.WEEK_OF_YEAR)
				+ "��";
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
		// ������
		for (int i = 1; i < 6; i++)
		{
			 canvas.drawLine(left, top + (cellHeight) * i, left +
			 calendarWidth,
			 top + (cellHeight) * i, paint);
		}
		// ������
		for (int i = 1; i < 7; i++)
		{
			 canvas.drawLine(left + cellWidth * i, top, left + cellWidth * i,
			 view.getMeasuredHeight() - borderMargin, paint);
		}

		// ������
		//���days����
		calculateDays();
		//����calendarΪ��������
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
		SimpleDateFormat abc = new SimpleDateFormat("yyyy��M��d��");
		for (int a = 0; a < days.length; a++)
		{
			today = false;
			int row = a / 7;
			int col = a % 7;
			String text = String.valueOf(days[a]);
			// ������������ա���������������һ�º���һ�µ���������
			//��
			if ((a % 7 == 0 || (a - 6) % 7 == 0) && text.startsWith("*"))
			{
				paint.setColor(sundaySaturdayPrevNextMonthDayColor);
			}
			else if (a % 7 == 0 || (a - 6) % 7 == 0)
			{
				paint.setColor(sundaySaturdayColor);
			}
			// ���������յ�Ϊ��һ�µ�����
			else if (text.startsWith("*"))
			{
				paint.setColor(prevNextMonthDayColor);
			}
			else
			{
				//��ͨ����
				paint.setColor(dayColor);
			}
			//ȥ������һ��������ǰ���Ǻ�
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
					// ����
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
					// ����
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
				// ����
				if (a > 20)
				{
					String nowday = currentYear + "��" + (currentMonth+2) + "��" + myText + "��";
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
					if (showDay.equals("��һ")) {
						canvas.drawText(showMonth, textLeft1, textTop1, paint);
					} else {
						canvas.drawText(showDay, textLeft1, textTop1, paint);
					}
				}
				// ����
				else
				{
					String nowday = currentYear + "��" + currentMonth + "��" + myText + "��";
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
					if (showDay.equals("��һ")) {
						canvas.drawText(showMonth, textLeft1, textTop1, paint);
					} else {
						canvas.drawText(showDay, textLeft1, textTop1, paint);
					}
				}
			}else{
			//���嵱ǰ����
			String nowday = currentYear + "��" + (currentMonth+1) + "��" + myText + "��";
//			System.out.println("nowday--->"+nowday);
			//����currentDayΪ��������
			java.util.Calendar currentDay = java.util.Calendar.getInstance();
			try {
				//���崫��ChineseCalendar�����ĸ�ʽ
				currentDay.setTime(abc.parse(nowday));
//				System.out.println("currentDay:"+currentDay);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//����ChineseCalendar���󲢴��뵱ǰ���ڣ��ꡢ�¡��գ�
			ChineseCalendar lunar = new ChineseCalendar(currentDay);
			//��ȡ�������ڵ�ũ��
			String currentDate = lunar.getChineseDateString();
			System.out.println("currentDate:"+currentDate);
			int startIndex = currentDate.length()-2;
			//��ȡũ���·�
			String showMonth = currentDate.substring(startIndex-2, startIndex);
			//��ȡũ������
			String showDay = currentDate.substring(startIndex);
			//��ũ������Ϊ����һʱ��������������·�
			//���򻭳�����ũ������
			if (showDay.equals("��һ")) {
				canvas.drawText(showMonth, textLeft1, textTop1, paint);
			} else {
				canvas.drawText(showDay, textLeft1, textTop1, paint);
			}
		}
			//��������
			canvas.drawText(myText, textLeft, textTop, paint);
		}
	}
}
