package alan.jluzh;



import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 
 * ũ��������<br>
 * 
 * ��ũ����1901�굽2100��֮����ꡢ�µĴ�С�Լ�����������棬Ȼ�������Щ���ݽ��м��㡣<br>
 * 
 * <br>
 * 
 * �����˼�������ũ���ĳ��������ֶΣ�����ʹ��get()������ȡ������Ӧ��ֵ��<br>
 * 
 * ũ���ꡢ�¡��ջ�����ʹ��set()/add()/roll()�������ã�����ũ�������Զ����㣻<br>
 * 
 * ���⣬���ṩ��getChinese(int field)�������ڻ��ũ�����������֣���������ũ�����Ժ����ڣ���
 * 
 * <ul>
 * 
 * <li>CHINESE_YEAR - ũ����</li>
 * 
 * <li>CHINESE_MONTH - ũ����</li>
 * 
 * <li>CHINESE_DATE - ũ����</li>
 * 
 * <li>CHINESE_SECTIONAL_TERM - ���µĽ���</li>
 * 
 * <li>CHINESE_PRINCIPLE_TERM - ���µ�����</li>
 * 
 * <li>CHINESE_HEAVENLY_STEM - ũ��������</li>
 * 
 * <li>CHINESE_EARTHLY_BRANCH - ũ����ĵ�֧</li>
 * 
 * <li>CHINESE_ANIMAL - ũ���������</li>
 * 
 * </ul>
 * 
 * ע�⣺<br>
 * 
 * ����Calendar����趨�������·ݴ�0��ʼ�����з�������ѭ����һԼ����<br>
 * 
 * �����е�ũ�����Դ�1��ʼ����ʹ����Calendar�ṩ�ķ����У�ũ����Ҳ�Ǵ�1��ʼ�ģ����Ը�����ʾ���¡�<br>
 * 
 * clear()������ĳЩ����»ᵼ��ũ���͹������ڲ���Ӧ���ǲ��ܴﵽԤ�ڵ�����Ч����Ӧ��������ʹ�á�<br>
 * 
 * ʹ��getSimpleDateString()��ù��������ַ���ʱ���������Ѿ�������<br>
 * 
 * ʹ��getSimpleChineseDateString()���ũ�������ַ���ʱ��ũ��������*��ʾ��<br>
 * 
 * <br>
 * 
 * 
 * 
 * @author ��ͷ
 */

public final class ChineseCalendar extends GregorianCalendar {

	private static final long serialVersionUID = 8L;

	/** ũ���� */

	public static final int CHINESE_YEAR = 801;

	/** ũ���� */

	public static final int CHINESE_MONTH = 802;

	/** ũ���� */

	public static final int CHINESE_DATE = 803;

	/** ���µĽ�����Ӧ�Ĺ�����(ǰһ������) */

	public static final int CHINESE_SECTIONAL_TERM = 804;

	/** ���µ�������Ӧ�Ĺ�����(��һ������) */

	public static final int CHINESE_PRINCIPLE_TERM = 805;

	/** ��� */

	public static final int CHINESE_HEAVENLY_STEM = 806;

	/** ��֧ */

	public static final int CHINESE_EARTHLY_BRANCH = 807;

	/** ũ���������(��Ф) */

	public static final int CHINESE_ANIMAL = 808;

	private int chineseYear;

	private int chineseMonth; // 1��ʼ��������ʾ����

	private int chineseDate;

	private int sectionalTerm; // ���½����Ĺ�����

	private int principleTerm; // ���������Ĺ�����

	private boolean areChineseFieldsComputed; // ũ�������Ƿ��Ѿ���������ȷ��

	private boolean areSolarTermsComputed; // �����Ƿ��Ѿ���������ȷ��

	private boolean lastSetChinese; // ������õ��ǲ���ũ������

	/** ʹ�õ�ǰʱ�乹��һ��ʵ���� */

	public ChineseCalendar() {

		super();

	}

	/** ʹ��ָ��ʱ�乹��һ��ʵ���� */

	public ChineseCalendar(Date d) {

		super.setTime(d);

	}

	/** ʹ��ָ��ʱ�乹��һ��ʵ���� */

	public ChineseCalendar(Calendar c) {

		this(c.getTime());

	}

	/** ʹ��ָ���������ڹ���һ��ʵ���� */

	public ChineseCalendar(int y, int m, int d) {

		super(y, m - 1, d);

	}

	/**
	 * 
	 * ʹ��ָ�����ڹ���һ��ʵ����
	 * 
	 * 
	 * 
	 * @param isChinese
	 * 
	 *            �Ƿ�Ϊũ������
	 * 
	 * @param y
	 * 
	 * @param m
	 * 
	 * @param d
	 */

	public ChineseCalendar(boolean isChinese, int y, int m, int d) {

		if (isChinese) {

			set(CHINESE_YEAR, y);

			set(CHINESE_MONTH, m);

			set(CHINESE_DATE, d);

		} else {

			set(y, m, d);

		}

	}

	public void set(int field, int value) {

		computeIfNeed(field);

		if (isChineseField(field)) {

			// ũ������

			switch (field) {

			case CHINESE_YEAR:

				chineseYear = value;

				break;

			case CHINESE_MONTH:

				chineseMonth = value;

				break;

			case CHINESE_DATE:

				chineseDate = value;

				break;

			default:

				throw new IllegalArgumentException("��֧�ֵ�field���ã�" + field);

			}

			lastSetChinese = true;

		} else {

			// ��ũ������

			super.set(field, value);

			lastSetChinese = false;

		}

		areFieldsSet = false;

		areChineseFieldsComputed = false;

		areSolarTermsComputed = false;

	}

	public int get(int field) {

		computeIfNeed(field);

		if (!isChineseField(field)) {

			return super.get(field);

		}

		if (!areChineseFieldsComputed) {

			// ����ũ������

			computeChineseFields();

			areChineseFieldsComputed = true;

		}

		switch (field) {

		case CHINESE_YEAR:

			return chineseYear;

		case CHINESE_MONTH:

			return chineseMonth;

		case CHINESE_DATE:

			return chineseDate;

		case CHINESE_SECTIONAL_TERM:

			return sectionalTerm;

		case CHINESE_PRINCIPLE_TERM:

			return principleTerm;

		case CHINESE_HEAVENLY_STEM:

			return (chineseYear - 4) % 10 + 1;

		case CHINESE_EARTHLY_BRANCH:

		case CHINESE_ANIMAL:

			return (chineseYear - 4) % 12 + 1;

		default:

			throw new IllegalArgumentException("��֧�ֵ�field��ȡ��" + field);

		}

	}

	public void add(int field, int amount) {

		computeIfNeed(field);

		if (!isChineseField(field)) {

			super.add(field, amount);

			lastSetChinese = false;

			areChineseFieldsComputed = false;

			areSolarTermsComputed = false;

			return;

		}

		switch (field) {

		case CHINESE_YEAR:

			chineseYear += amount;

			break;

		case CHINESE_MONTH:

			for (int i = 0; i < amount; i++) {

				chineseMonth = nextChineseMonth(chineseYear, chineseMonth);

				if (chineseMonth == 1) {

					chineseYear++;

				}

			}

			break;

		case CHINESE_DATE:

			int maxDate = daysInChineseMonth(chineseYear, chineseMonth);

			for (int i = 0; i < amount; i++) {

				chineseDate++;

				if (chineseDate > maxDate) {

					chineseDate = 1;

					chineseMonth = nextChineseMonth(chineseYear, chineseMonth);

					if (chineseMonth == 1) {

						chineseYear++;

					}

					maxDate = daysInChineseMonth(chineseYear, chineseMonth);

				}

			}

		default:

			throw new IllegalArgumentException("��֧�ֵ�field��" + field);

		}

		lastSetChinese = true;

		areFieldsSet = false;

		areSolarTermsComputed = false;

	}

	public void roll(int field, int amount) {

		computeIfNeed(field);

		if (!isChineseField(field)) {

			super.roll(field, amount);

			lastSetChinese = false;

			areChineseFieldsComputed = false;

			areSolarTermsComputed = false;

			return;

		}

		switch (field) {

		case CHINESE_YEAR:

			chineseYear += amount;

			break;

		case CHINESE_MONTH:

			for (int i = 0; i < amount; i++) {

				chineseMonth = nextChineseMonth(chineseYear, chineseMonth);

			}

			break;

		case CHINESE_DATE:

			int maxDate = daysInChineseMonth(chineseYear, chineseMonth);

			for (int i = 0; i < amount; i++) {

				chineseDate++;

				if (chineseDate > maxDate) {

					chineseDate = 1;

				}

			}

		default:

			throw new IllegalArgumentException("��֧�ֵ�field��" + field);

		}

		lastSetChinese = true;

		areFieldsSet = false;

		areSolarTermsComputed = false;

	}

	/**
	 * 
	 * ������Ե����ģ�����ʹ�õ������ֶ�ΪDAY_OF_WEEK�Լ�����ũ�������ֶΡ�
	 * 
	 * 
	 * 
	 * @param field
	 * 
	 * @return
	 */

	public String getChinese(int field) {

		switch (field) {

		case CHINESE_YEAR:

			return getChinese(CHINESE_HEAVENLY_STEM)

			+ getChinese(CHINESE_EARTHLY_BRANCH);

		case CHINESE_MONTH:

			if (chineseMonth > 0)

				return chineseMonthNames[chineseMonth];

			else

				return "��" + chineseMonthNames[-chineseMonth];

		case CHINESE_DATE:

			return chineseDateNames[chineseDate];

		case CHINESE_SECTIONAL_TERM:

			return sectionalTermNames[get(Calendar.MONTH)];

		case CHINESE_PRINCIPLE_TERM:

			return principleTermNames[get(Calendar.MONTH)];

		case CHINESE_HEAVENLY_STEM:

			if (!areSolarTermsComputed) {

				computeSolarTerms();

				areSolarTermsComputed = true;

			}

			return stemNames[get(field)];

		case CHINESE_EARTHLY_BRANCH:

			if (!areSolarTermsComputed) {

				computeSolarTerms();

				areSolarTermsComputed = true;

			}

			return branchNames[get(field)];

		case CHINESE_ANIMAL:

			return animalNames[get(field)];

		case Calendar.DAY_OF_WEEK:

			return chineseWeekNames[get(field)];

		default:

			throw new IllegalArgumentException("��֧�ֵ�field���Ļ�ȡ��" + field);

		}

	}

	public String getSimpleGregorianDateString() {

		return new StringBuffer().append(get(YEAR))

		.append("-")

		.append(get(MONTH) + 1)

		.append("-")

		.append(get(DATE))

		.toString();

	}

	public String getSimpleChineseDateString() {

		return new StringBuffer().append(get(CHINESE_YEAR))

		.append("-")

		.append(get(CHINESE_MONTH) > 0 ? ""

		+ get(CHINESE_MONTH) : "*"

		+ (-get(CHINESE_MONTH)))

		.append("-")

		.append(get(CHINESE_DATE))

		.toString();

	}

	public String getChineseDateString() {

		return new StringBuffer().append(getChinese(CHINESE_YEAR))

		.append("��")

		.append(getChinese(CHINESE_MONTH))

		.append("��")

		.append(getChinese(CHINESE_DATE))

		.toString();
	}

	public String toString() {

		StringBuffer buf = new StringBuffer();

		buf.append(getSimpleGregorianDateString())

		.append(" | ")

		.append(getChinese(DAY_OF_WEEK))

		.append(" | [ũ��]")

		.append(getChineseDateString())

		.append(" ")

		.append(getChinese(CHINESE_ANIMAL))

		.append("�� ")

		.append(get(CHINESE_SECTIONAL_TERM))

		.append("��")

		.append(getChinese(CHINESE_SECTIONAL_TERM))

		.append(" ")

		.append(get(CHINESE_PRINCIPLE_TERM))

		.append("��")

		.append(getChinese(CHINESE_PRINCIPLE_TERM));

		return buf.toString();

	}

	/**
	 * 
	 * �ж��ǲ���ũ������
	 * 
	 * 
	 * 
	 * @param field
	 * 
	 * @return
	 */

	private boolean isChineseField(int field) {

		switch (field) {

		case CHINESE_YEAR:

		case CHINESE_MONTH:

		case CHINESE_DATE:

		case CHINESE_SECTIONAL_TERM:

		case CHINESE_PRINCIPLE_TERM:

		case CHINESE_HEAVENLY_STEM:

		case CHINESE_EARTHLY_BRANCH:

		case CHINESE_ANIMAL:

			return true;

		default:

			return false;

		}

	}

	/**
	 * 
	 * �����һ�����õ�����ν�Ҫ���û��ȡ�����Բ���ͬһ�ࣨũ��/��������<br>
	 * 
	 * ������һ�����õ���ũ��������Ҫ���û��ȡ������<br>
	 * 
	 * ����Ҫ�ȸ���֮ǰ���õ�ũ�����ڼ�����������ڡ�
	 * 
	 * 
	 * 
	 * @param field
	 */

	private void computeIfNeed(int field) {

		if (isChineseField(field)) {

			if (!lastSetChinese && !areChineseFieldsComputed) {

				super.complete();

				computeChineseFields();

				areFieldsSet = true;

				areChineseFieldsComputed = true;

				areSolarTermsComputed = false;

			}

		} else {

			if (lastSetChinese && !areFieldsSet) {

				computeGregorianFields();

				super.complete();

				areFieldsSet = true;

				areChineseFieldsComputed = true;

				areSolarTermsComputed = false;

			}

		}

	}

	/**
	 * 
	 * ʹ��ũ�����ڼ������������
	 */

	private void computeGregorianFields() {

		int y = chineseYear;

		int m = chineseMonth;

		int d = chineseDate;

		areChineseFieldsComputed = true;

		areFieldsSet = true;

		lastSetChinese = false;

		// �������ڷ�Χ

		if (y < 1900)

			y = 1899;

		else if (y > 2100)

			y = 2101;

		if (m < -12)

			m = -12;

		else if (m > 12)

			m = 12;

		if (d < 1)

			d = 1;

		else if (d > 30)

			d = 30;

		int dateint = y * 10000 + Math.abs(m) * 100 + d;

		if (dateint < 19001111) { // ̫С

			set(1901, Calendar.JANUARY, 1);

			super.complete();

		} else if (dateint > 21001201) { // ̫��

			set(2100, Calendar.DECEMBER, 31);

			super.complete();

		} else {

			if (Math.abs(m) > 12) {

				m = 12;

			}

			int days = ChineseCalendar.daysInChineseMonth(y, m);

			if (days == 0) {

				m = -m;

				days = ChineseCalendar.daysInChineseMonth(y, m);

			}

			if (d > days) {

				d = days;

			}

			set(y, Math.abs(m) - 1, d);

			computeChineseFields();

			int amount = 0;

			while (chineseYear != y || chineseMonth != m) {

				amount += daysInChineseMonth(chineseYear, chineseMonth);

				chineseMonth = nextChineseMonth(chineseYear, chineseMonth);

			}

			amount += d - chineseDate;

			super.add(Calendar.DATE, amount);

		}

		computeChineseFields();

	}

	/**
	 * 
	 * ʹ�ù������ڼ����ũ������
	 */

	private void computeChineseFields() {

		int gregorianYear = internalGet(Calendar.YEAR);

		int gregorianMonth = internalGet(Calendar.MONTH) + 1;

		int gregorianDate = internalGet(Calendar.DATE);

		if (gregorianYear < 1901 || gregorianYear > 2100) {

			return;

		}

		int startYear = baseYear;

		int startMonth = baseMonth;

		int startDate = baseDate;

		chineseYear = baseChineseYear;

		chineseMonth = baseChineseMonth;

		chineseDate = baseChineseDate;

		// �ڶ�����Ӧ�գ�������߼���Ч��

		// ���� 2000 �� 1 �� 1 �գ���Ӧũ�� 4697 �� 11 �� 25 ��

		if (gregorianYear >= 2000) {

			startYear = baseYear + 99;

			startMonth = 1;

			startDate = 1;

			chineseYear = baseChineseYear + 99;

			chineseMonth = 11;

			chineseDate = 25;

		}

		int daysDiff = 0;

		for (int i = startYear; i < gregorianYear; i++) {

			daysDiff += 365;

			if (isGregorianLeapYear(i)) {

				daysDiff += 1; // leap year

			}

		}

		for (int i = startMonth; i < gregorianMonth; i++) {

			daysDiff += daysInGregorianMonth(gregorianYear, i - 1);

		}

		daysDiff += gregorianDate - startDate;

		chineseDate += daysDiff;

		int lastDate = daysInChineseMonth(chineseYear, chineseMonth);

		int nextMonth = nextChineseMonth(chineseYear, chineseMonth);

		while (chineseDate > lastDate) {

			if (Math.abs(nextMonth) < Math.abs(chineseMonth)) {

				chineseYear++;

			}

			chineseMonth = nextMonth;

			chineseDate -= lastDate;

			lastDate = daysInChineseMonth(chineseYear, chineseMonth);

			nextMonth = nextChineseMonth(chineseYear, chineseMonth);

		}

	}

	/**
	 * 
	 * �������
	 */

	private void computeSolarTerms() {

		int gregorianYear = internalGet(Calendar.YEAR);

		int gregorianMonth = internalGet(Calendar.MONTH);

		if (gregorianYear < 1901 || gregorianYear > 2100) {

			return;

		}

		sectionalTerm = sectionalTerm(gregorianYear, gregorianMonth);

		principleTerm = principleTerm(gregorianYear, gregorianMonth);

	}

	// �������Ǿ�̬����~

	/**
	 * 
	 * �Ƿ�Ϊ��������
	 * 
	 * 
	 * 
	 * @param year
	 * 
	 * @return
	 */

	public static boolean isGregorianLeapYear(int year) {

		boolean isLeap = false;

		if (year % 4 == 0) {

			isLeap = true;

		}

		if (year % 100 == 0) {

			isLeap = false;

		}

		if (year % 400 == 0) {

			isLeap = true;

		}

		return isLeap;

	}

	/**
	 * 
	 * ���㹫����ĵ��������������´�0��ʼ��
	 * 
	 * 
	 * 
	 * @param y
	 * 
	 * @param m
	 * 
	 * @return
	 */

	public static int daysInGregorianMonth(int y, int m) {

		int d = daysInGregorianMonth[m];

		if (m == Calendar.FEBRUARY && isGregorianLeapYear(y)) {

			d++; // ����������¶�һ��

		}

		return d;

	}

	/**
	 * 
	 * ���㹫���굱�µĽ����������´�0��ʼ��
	 * 
	 * 
	 * 
	 * @param y
	 * 
	 * @param m
	 * 
	 * @return
	 */

	public static int sectionalTerm(int y, int m) {

		m++;

		if (y < 1901 || y > 2100) {

			return 0;

		}

		int index = 0;

		int ry = y - baseYear + 1;

		while (ry >= sectionalTermYear[m - 1][index]) {

			index++;

		}

		int term = sectionalTermMap[m - 1][4 * index + ry % 4];

		if ((ry == 121) && (m == 4)) {

			term = 5;

		}

		if ((ry == 132) && (m == 4)) {

			term = 5;

		}

		if ((ry == 194) && (m == 6)) {

			term = 6;

		}

		return term;

	}

	/**
	 * 
	 * ���㹫���굱�µ������������´�0��ʼ��
	 * 
	 * 
	 * 
	 * @param y
	 * 
	 * @param m
	 * 
	 * @return
	 */

	public static int principleTerm(int y, int m) {

		m++;

		if (y < 1901 || y > 2100) {

			return 0;

		}

		int index = 0;

		int ry = y - baseYear + 1;

		while (ry >= principleTermYear[m - 1][index]) {

			index++;

		}

		int term = principleTermMap[m - 1][4 * index + ry % 4];

		if ((ry == 171) && (m == 3)) {

			term = 21;

		}

		if ((ry == 181) && (m == 5)) {

			term = 21;

		}

		return term;

	}

	/**
	 * 
	 * ����ũ���������
	 * 
	 * 
	 * 
	 * @param y
	 * 
	 * @param m
	 * 
	 * @return
	 */

	public static int daysInChineseMonth(int y, int m) {

		// ע�⣺���� m < 0

		int index = y - baseChineseYear + baseIndex;

		int v = 0;

		int l = 0;

		int d = 30;

		if (1 <= m && m <= 8) {

			v = chineseMonths[2 * index];

			l = m - 1;

			if (((v >> l) & 0x01) == 1) {

				d = 29;

			}

		} else if (9 <= m && m <= 12) {

			v = chineseMonths[2 * index + 1];

			l = m - 9;

			if (((v >> l) & 0x01) == 1) {

				d = 29;

			}

		} else {

			v = chineseMonths[2 * index + 1];

			v = (v >> 4) & 0x0F;

			if (v != Math.abs(m)) {

				d = 0;

			} else {

				d = 29;

				for (int i = 0; i < bigLeapMonthYears.length; i++) {

					if (bigLeapMonthYears[i] == index) {

						d = 30;

						break;

					}

				}

			}

		}

		return d;

	}

	/**
	 * 
	 * ����ũ�����¸���
	 * 
	 * 
	 * 
	 * @param y
	 * 
	 * @param m
	 * 
	 * @return
	 */

	public static int nextChineseMonth(int y, int m) {

		int n = Math.abs(m) + 1;

		if (m > 0) {

			int index = y - baseChineseYear + baseIndex;

			int v = chineseMonths[2 * index + 1];

			v = (v >> 4) & 0x0F;

			if (v == m) {

				n = -m;

			}

		}

		if (n == 13) {

			n = 1;

		}

		return n;

	}

	/* ������һ������� */

	private static final int baseYear = 1901;

	private static final int baseMonth = 1;

	private static final int baseDate = 1;

	private static final int baseIndex = 0;

	private static final int baseChineseYear = 1900;

	private static final int baseChineseMonth = 11;

	private static final int baseChineseDate = 11;

	/* �����ַ��� */

	private static final String[] chineseWeekNames = { "", "������", "����һ", "���ڶ�",

	"������", "������", "������", "������" };

	private static final String[] chineseMonthNames = { "", "��", "��", "��", "��",

	"��", "��", "��", "��", "��", "ʮ", "��", "��" };

	private static final String[] chineseDateNames = { "", "��һ", "����", "����",

	"����", "����", "����", "����", "����", "����", "��ʮ", "ʮһ", "ʮ��", "ʮ��", "ʮ��",

	"ʮ��", "ʮ��", "ʮ��", "ʮ��", "ʮ��", "��ʮ", "إһ", "إ��", "إ��", "إ��", "إ��",

	"إ��", "إ��", "إ��", "إ��", "��ʮ" };

	private static final String[] principleTermNames = { "��", "��ˮ", "����",

	"����", "����", "����", "����", "����", "���", "˪��", "Сѩ", "����" };

	private static final String[] sectionalTermNames = { "С��", "����", "����",

	"����", "����", "â��", "С��", "����", "��¶", "��¶", "����", "��ѩ" };

	private static final String[] stemNames = { "", "��", "��", "��", "��", "��",

	"��", "��", "��", "��", "��" };

	private static final String[] branchNames = { "", "��", "��", "��", "î", "��",

	"��", "��", "δ", "��", "��", "��", "��" };

	private static final String[] animalNames = { "", "��", "ţ", "��", "��", "��",

	"��", "��", "��", "��", "��", "��", "��" };

	// �����������ݱ�

	private static final int[] bigLeapMonthYears = { 6, 14, 19, 25, 33, 36, 38,

	41, 44, 52, 55, 79, 117, 136, 147, 150, 155, 158, 185, 193 };

	private static final char[][] sectionalTermMap = {

	{ 7, 6, 6, 6, 6, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 5, 5,

	5, 5, 5, 4, 5, 5 },

	{ 5, 4, 5, 5, 5, 4, 4, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 3, 4, 4, 4, 3,

	3, 4, 4, 3, 3, 3 },

	{ 6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 6, 5, 5,

	5, 5, 4, 5, 5, 5, 5 },

	{ 5, 5, 6, 6, 5, 5, 5, 6, 5, 5, 5, 5, 4, 5, 5, 5, 4, 4, 5, 5, 4, 4,

	4, 5, 4, 4, 4, 4, 5 },

	{ 6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 6, 5, 5,

	5, 5, 4, 5, 5, 5, 5 },

	{ 6, 6, 7, 7, 6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5,

	5, 6, 5, 5, 5, 5, 4, 5, 5, 5, 5 },

	{ 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7, 7, 7, 6, 6,

	7, 7, 6, 6, 6, 7, 7 },

	{ 8, 8, 8, 9, 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7,

	7, 7, 6, 7, 7, 7, 6, 6, 7, 7, 7 },

	{ 8, 8, 8, 9, 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7,

	7, 7, 6, 7, 7, 7, 7 },

	{ 9, 9, 9, 9, 8, 9, 9, 9, 8, 8, 9, 9, 8, 8, 8, 9, 8, 8, 8, 8, 7, 8,

	8, 8, 7, 7, 8, 8, 8 },

	{ 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7,

	7, 7, 6, 6, 7, 7, 7 },

	{ 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7, 7, 7, 6, 6,

	7, 7, 6, 6, 6, 7, 7 } };

	private static final char[][] sectionalTermYear = {

	{ 13, 49, 85, 117, 149, 185, 201, 250, 250 },

	{ 13, 45, 81, 117, 149, 185, 201, 250, 250 },

	{ 13, 48, 84, 112, 148, 184, 200, 201, 250 },

	{ 13, 45, 76, 108, 140, 172, 200, 201, 250 },

	{ 13, 44, 72, 104, 132, 168, 200, 201, 250 },

	{ 5, 33, 68, 96, 124, 152, 188, 200, 201 },

	{ 29, 57, 85, 120, 148, 176, 200, 201, 250 },

	{ 13, 48, 76, 104, 132, 168, 196, 200, 201 },

	{ 25, 60, 88, 120, 148, 184, 200, 201, 250 },

	{ 16, 44, 76, 108, 144, 172, 200, 201, 250 },

	{ 28, 60, 92, 124, 160, 192, 200, 201, 250 },

	{ 17, 53, 85, 124, 156, 188, 200, 201, 250 } };

	private static final char[][] principleTermMap = {

	{ 21, 21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20,

	20, 20, 20, 20, 20, 19, 20, 20, 20, 19, 19, 20 },

	{ 20, 19, 19, 20, 20, 19, 19, 19, 19, 19, 19, 19, 19, 18, 19, 19,

	19, 18, 18, 19, 19, 18, 18, 18, 18, 18, 18, 18 },

	{ 21, 21, 21, 22, 21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21,

	20, 20, 20, 21, 20, 20, 20, 20, 19, 20, 20, 20, 20 },

	{ 20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20, 21, 20, 20, 20, 20,

	19, 20, 20, 20, 19, 19, 20, 20, 19, 19, 19, 20, 20 },

	{ 21, 22, 22, 22, 21, 21, 22, 22, 21, 21, 21, 22, 21, 21, 21, 21,

	20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20, 21, 21 },

	{ 22, 22, 22, 22, 21, 22, 22, 22, 21, 21, 22, 22, 21, 21, 21, 22,

	21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21, 21 },

	{ 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23, 22, 23, 23, 23,

	22, 22, 23, 23, 22, 22, 22, 23, 22, 22, 22, 22, 23 },

	{ 23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23,

	22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23, 23 },

	{ 23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23,

	22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23, 23 },

	{ 24, 24, 24, 24, 23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24,

	23, 23, 23, 23, 22, 23, 23, 23, 22, 22, 23, 23, 23 },

	{ 23, 23, 23, 23, 22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23,

	22, 22, 22, 22, 21, 22, 22, 22, 21, 21, 22, 22, 22 },

	{ 22, 22, 23, 23, 22, 22, 22, 23, 22, 22, 22, 22, 21, 22, 22, 22,

	21, 21, 22, 22, 21, 21, 21, 22, 21, 21, 21, 21, 22 } };

	private static final char[][] principleTermYear = {

	{ 13, 45, 81, 113, 149, 185, 201 },

	{ 21, 57, 93, 125, 161, 193, 201 },

	{ 21, 56, 88, 120, 152, 188, 200, 201 },

	{ 21, 49, 81, 116, 144, 176, 200, 201 },

	{ 17, 49, 77, 112, 140, 168, 200, 201 },

	{ 28, 60, 88, 116, 148, 180, 200, 201 },

	{ 25, 53, 84, 112, 144, 172, 200, 201 },

	{ 29, 57, 89, 120, 148, 180, 200, 201 },

	{ 17, 45, 73, 108, 140, 168, 200, 201 },

	{ 28, 60, 92, 124, 160, 192, 200, 201 },

	{ 16, 44, 80, 112, 148, 180, 200, 201 },

	{ 17, 53, 88, 120, 156, 188, 200, 201 } };

	private static final char[] daysInGregorianMonth = { 31, 28, 31, 30, 31,

	30, 31, 31, 30, 31, 30, 31 };

	private static final char[] chineseMonths = { 0x00, 0x04, 0xad, 0x08, 0x5a,

	0x01, 0xd5, 0x54, 0xb4, 0x09, 0x64, 0x05, 0x59, 0x45, 0x95, 0x0a,

	0xa6, 0x04, 0x55, 0x24, 0xad, 0x08, 0x5a, 0x62, 0xda, 0x04, 0xb4,

	0x05, 0xb4, 0x55, 0x52, 0x0d, 0x94, 0x0a, 0x4a, 0x2a, 0x56, 0x02,

	0x6d, 0x71, 0x6d, 0x01, 0xda, 0x02, 0xd2, 0x52, 0xa9, 0x05, 0x49,

	0x0d, 0x2a, 0x45, 0x2b, 0x09, 0x56, 0x01, 0xb5, 0x20, 0x6d, 0x01,

	0x59, 0x69, 0xd4, 0x0a, 0xa8, 0x05, 0xa9, 0x56, 0xa5, 0x04, 0x2b,

	0x09, 0x9e, 0x38, 0xb6, 0x08, 0xec, 0x74, 0x6c, 0x05, 0xd4, 0x0a,

	0xe4, 0x6a, 0x52, 0x05, 0x95, 0x0a, 0x5a, 0x42, 0x5b, 0x04, 0xb6,

	0x04, 0xb4, 0x22, 0x6a, 0x05, 0x52, 0x75, 0xc9, 0x0a, 0x52, 0x05,

	0x35, 0x55, 0x4d, 0x0a, 0x5a, 0x02, 0x5d, 0x31, 0xb5, 0x02, 0x6a,

	0x8a, 0x68, 0x05, 0xa9, 0x0a, 0x8a, 0x6a, 0x2a, 0x05, 0x2d, 0x09,

	0xaa, 0x48, 0x5a, 0x01, 0xb5, 0x09, 0xb0, 0x39, 0x64, 0x05, 0x25,

	0x75, 0x95, 0x0a, 0x96, 0x04, 0x4d, 0x54, 0xad, 0x04, 0xda, 0x04,

	0xd4, 0x44, 0xb4, 0x05, 0x54, 0x85, 0x52, 0x0d, 0x92, 0x0a, 0x56,

	0x6a, 0x56, 0x02, 0x6d, 0x02, 0x6a, 0x41, 0xda, 0x02, 0xb2, 0xa1,

	0xa9, 0x05, 0x49, 0x0d, 0x0a, 0x6d, 0x2a, 0x09, 0x56, 0x01, 0xad,

	0x50, 0x6d, 0x01, 0xd9, 0x02, 0xd1, 0x3a, 0xa8, 0x05, 0x29, 0x85,

	0xa5, 0x0c, 0x2a, 0x09, 0x96, 0x54, 0xb6, 0x08, 0x6c, 0x09, 0x64,

	0x45, 0xd4, 0x0a, 0xa4, 0x05, 0x51, 0x25, 0x95, 0x0a, 0x2a, 0x72,

	0x5b, 0x04, 0xb6, 0x04, 0xac, 0x52, 0x6a, 0x05, 0xd2, 0x0a, 0xa2,

	0x4a, 0x4a, 0x05, 0x55, 0x94, 0x2d, 0x0a, 0x5a, 0x02, 0x75, 0x61,

	0xb5, 0x02, 0x6a, 0x03, 0x61, 0x45, 0xa9, 0x0a, 0x4a, 0x05, 0x25,

	0x25, 0x2d, 0x09, 0x9a, 0x68, 0xda, 0x08, 0xb4, 0x09, 0xa8, 0x59,

	0x54, 0x03, 0xa5, 0x0a, 0x91, 0x3a, 0x96, 0x04, 0xad, 0xb0, 0xad,

	0x04, 0xda, 0x04, 0xf4, 0x62, 0xb4, 0x05, 0x54, 0x0b, 0x44, 0x5d,

	0x52, 0x0a, 0x95, 0x04, 0x55, 0x22, 0x6d, 0x02, 0x5a, 0x71, 0xda,

	0x02, 0xaa, 0x05, 0xb2, 0x55, 0x49, 0x0b, 0x4a, 0x0a, 0x2d, 0x39,

	0x36, 0x01, 0x6d, 0x80, 0x6d, 0x01, 0xd9, 0x02, 0xe9, 0x6a, 0xa8,

	0x05, 0x29, 0x0b, 0x9a, 0x4c, 0xaa, 0x08, 0xb6, 0x08, 0xb4, 0x38,

	0x6c, 0x09, 0x54, 0x75, 0xd4, 0x0a, 0xa4, 0x05, 0x45, 0x55, 0x95,

	0x0a, 0x9a, 0x04, 0x55, 0x44, 0xb5, 0x04, 0x6a, 0x82, 0x6a, 0x05,

	0xd2, 0x0a, 0x92, 0x6a, 0x4a, 0x05, 0x55, 0x0a, 0x2a, 0x4a, 0x5a,

	0x02, 0xb5, 0x02, 0xb2, 0x31, 0x69, 0x03, 0x31, 0x73, 0xa9, 0x0a,

	0x4a, 0x05, 0x2d, 0x55, 0x2d, 0x09, 0x5a, 0x01, 0xd5, 0x48, 0xb4,

	0x09, 0x68, 0x89, 0x54, 0x0b, 0xa4, 0x0a, 0xa5, 0x6a, 0x95, 0x04,

	0xad, 0x08, 0x6a, 0x44, 0xda, 0x04, 0x74, 0x05, 0xb0, 0x25, 0x54,

	0x03 };

	public String cyclical() {
		return new StringBuffer(getChinese(CHINESE_YEAR)).append("��")
				.toString();
	}
}