package cn.hl.ox.time;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkTimer {
	public static final boolean START_RECORD = true;
	public static final boolean STOP_RECORD = false;
	private static final Log logger = LogFactory.getLog(WorkTimer.class);

	private Long time1 = 0L;
	private Long time2 = 0L;
	private String work;

	/**
	 * 构造函数
	 * @param work -- 计时器任务内容
	 */
	public WorkTimer(String work) {
		if (work == null || work.equals(""))
			work = "[DO Something Unknown]";
		this.work = work;
		time1 = System.nanoTime();
	}

	/**
	 * 计时控制器
	 * @param bStart 	TimeCenter.START_RECORD -- 开始计时
	 * 					TimeCenter.STOP_RECORD -- 结束计时
	 */
	public void timer(boolean bStart) {
		if (bStart) {
			time1 = System.nanoTime();
		} else {
			time2 = System.nanoTime();
			StringBuilder sb = new StringBuilder();
			sb.append("execute ").append(work).append(" \\> Cost: ");
			sb.append(formatTime(time1, time2));
			logger.debug(sb.toString());
			time1 = time2 = 0L;
		}
	}

	/**
	 * 记录一个时间
	 */
	public void recordTime() {
		time2 = System.nanoTime();
		StringBuilder sb = new StringBuilder();
		sb.append("execute ").append(work).append(" \\> Cost: ");
		sb.append(formatTime(time1, time2));
		logger.debug(sb.toString());
	}

	/**
	 * 记录上次操作时间并重新设定工作内容和起始时间
	 * @param work
	 */
	public void recordAndReset(String work) {
		recordTime();
		if (work == null || work.equals(""))
			work = "[DO Something Unknown]";
		this.work = work;
		time1 = time2;
	}

	private String formatTime(Long time1, Long time2) {
		Long cost = time2 - time1;
		Long ns = 0L, ms = 0L, s = 0L, m = 0L, h = 0L, d = 0L;
		if (cost / 1000000 >= 1) {
			ns = cost % 1000000;
			cost /= 1000000;
			if (cost / 1000 >= 1) {
				ms = cost % 1000;
				cost /= 1000;
				if (cost / 60 >= 1) {
					s = cost % 60;
					cost /= 60;
					if (cost / 60 >= 1) {
						m = cost % 60;
						cost /= 60;
						if (cost / 24 >= 1) {
							h = cost % 24;
							d = cost / 24;
						} else h = cost;
					} else m = cost;
				} else s = cost;
			} else ms = cost;
		} else ns = cost;

		StringBuffer sb = new StringBuffer();
		if (d > 0)  sb.append(d).append("D ");
		if (h > 0)  sb.append(h).append("H ");
		if (m > 0)  sb.append(m).append("m ");
		if (s > 0)  sb.append(s).append("s ");
		if (ms > 0) sb.append(ms).append("ms ");
		if (ns > 0) sb.append(ns).append("ns ");
		return sb.toString();
	}

	/**
	 * 获取当前时间的字符串(yyyy-MM-dd HH:mm)
	 * @return
	 */
	public static String getCurrentTime() {
		/*try {
			return format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm");
		} catch (Exception e) {
			return null;
		}*/
		DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return formater.format(new Date());
	}

	/**
	 * 将时间字符串转换成毫秒数
	 * @param date
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public static long parse(String date, String pattern) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(date).getTime();
	}

	/**
	 * 将毫秒数转换成时间字符串
	 * @param time
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public static String format(Long time, String pattern) throws Exception {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 将Date类型数据转换成时间字符串
	 * @param date
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public static String format(Date date, String pattern) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static String PATTERN_YYYYMMDD = 
		"(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|" +
		"((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";

	public static void main(String args[]) throws Exception {
		WorkTimer timer = new WorkTimer("时间转换");

		String time = "2019-08-01T08:48:19Z";
		String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		long date = parse(time, pattern);
		System.out.println(format(date, pattern));

		logger.info(getCurrentTime());
		logger.info(parse("2010-5", "yyyy-MM"));
		logger.info(format(1242653250467L, "yyyy-MM-dd HH:mm:ss.SSS"));
		timer.recordAndReset("时间测试");

		String dateStr = "2010-02-23";
		logger.info(dateStr + "合法性:" + Pattern.matches(PATTERN_YYYYMMDD, dateStr));
		Pattern p = Pattern.compile(PATTERN_YYYYMMDD);
		dateStr = "2010-02-29";
		Matcher matcher2 = p.matcher(dateStr);
		logger.info(dateStr + "合法性:" + matcher2.matches());
		timer.recordTime();

		try {
			Thread.sleep(1314);
		} catch (InterruptedException e) {
			logger.error("sleep error", e);
		}
		timer.recordTime();
	}
}
