/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fisher.fishspear.common.utils;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateUtil {

	public static final String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};

    /**
     * 获取当前月
     */
    public static String getMonth(){
        return format(new Date(),"MM");
    }

    /**
	 * 获取YYYY格式
	 *
	 * @return
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 获取YYYY格式
	 *
	 * @return
	 */
	public static String getYear(Date date) {
		return formatDate(date, "yyyy");
	}

	/**
	 * 获取YYYY-MM-DD格式
	 *
	 * @return
	 */
	public static String getDay() {
		return formatDate(new Date(), "yyyy-MM-dd");
	}

	/**
	 * 获取YYYY-MM-DD格式
	 *
	 * @return
	 */
	public static String getDay(Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}

	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return
	 */
	public static String getDays() {
		return formatDate(new Date(), "yyyyMMdd");
	}

	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return
	 */
	public static String getDays(Date date) {
		return formatDate(date, "yyyyMMdd");
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 *
	 * @return
	 */
	public static String getTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	public static String getTime(String time) {
		return formatDate(new Date(), "yyyy/MM/dd HH:mm:ss");
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss.SSS格式
	 *
	 * @return
	 */
	public static String getMsTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
	}

	/**
	 * 获取YYYYMMDDHHmmss格式
	 *
	 * @return
	 */
	public static String getAllTime() {
		return formatDate(new Date(), "yyyyMMddHHmmss");
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 *
	 * @return
	 */
	public static String getTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String formatDate(Date date, String pattern) {
		if (!ToolUtil.isNotEmpty(date)){
			return "-";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * @Title: compareDate
	 * @Description:(日期比较，如果s>=e 返回true 否则返回false)
	 * @param s
	 * @param e
	 * @return boolean
	 * @throws
	 * @author luguosui
	 */
	public static boolean compareDate(String s, String e) {
		if (parseDate(s) == null || parseDate(e) == null) {
			return false;
		}
		return parseDate(s).getTime() >= parseDate(e).getTime();
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static Date parseDate(String date) {
		return parse(date,"yyyy-MM-dd");
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static Date parseTime(String date) {
		return parse(date,"yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 把日期转换为Timestamp
	 *
	 * @param date
	 * @return
	 */
	public static Timestamp format(Date date) {
		return new Timestamp(date.getTime());
	}

	/**
	 * 校验日期是否合法
	 *
	 * @return
	 */
	public static boolean isValidDate(String s) {
		return parse(s, "yyyy-MM-dd HH:mm:ss") != null;
	}

	/**
	 * 校验日期是否合法
	 *
	 * @return
	 */
	public static boolean isValidDate(String s, String pattern) {
        return parse(s, pattern) != null;
	}

	public static int getDiffYear(String startTime, String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(
					startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}

	/**
	 * <li>功能描述：时间相减得到天数
	 *
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 * @author Administrator
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd");
		Date beginDate = null;
		Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		// System.out.println("相隔的天数="+day);

		return day;
	}

	/**
	 * 得到n天之后的日期
	 *
	 * @param days
	 * @return
	 */
	public static String getAfterDayDate(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	/**
	 * 得到n天之后的日期
	 *
	 * @param days
	 * @return
	 */
	public static Date getAfterDayDate(int days) {
		Calendar calendar = Calendar.getInstance(); // java.util包
		calendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
		return calendar.getTime();
	}

	/**
	 * 得到n天之后是周几
	 *
	 * @param days
	 * @return
	 */
	public static String getAfterDayWeek(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("E");
		String dateStr = sdf.format(date);

		return dateStr;
	}

	public static Integer getPrevMonth(Integer month){
		if (month > 1){
			return month - 1;
		}
		return 12;
	}

	public static Integer getNextMonth(Integer month){
		if (month < 12){
			return month + 1;
		}
		return 1;
	}

	public static Integer getPrevYear(Integer year,Integer month){
		if (month > 1){
			return year;
		}
		return year - 1;
	}

	public static Integer getNextYear(Integer year,Integer month){
		if (month < 12){
			return year;
		}
		return year + 1;
	}

	/**
	 * 获取当前月的一号
	 */
	public static Date getThisMonthTheFirst(Date date){
		return new Date(date.getYear(),date.getMonth(),1);
	}

	/**
	 * 获得指定日期 几天后的日期
	 */
	public static Date getDate(Date date,int pastDays){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE,pastDays);
		return c.getTime();
	}


	/**
	 * 获取指定月的天数
	 */
	public static int getDays(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, 1);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取小时和分钟
	 * @param date
	 * @return
	 */
	public static String getHour(Date date) {
		if (date == null)
			return "";
		return new SimpleDateFormat("HH:mm").format(date);
	}

	/**
	 * 根据毫秒数计算耗时为  XX时XX分XX秒
	 */
	public static String getCostTimeStr(long time) {
		long total = time / 1000;
		long seconds = total % 60;
		long minutes = total / 60;
		long hours = minutes / 60;
		minutes = minutes - hours * 60;
		StringBuffer sb = new StringBuffer();
		if (hours > 0) {
			sb.append(hours).append("小时");
		}
		if (minutes > 0) {
			sb.append(minutes).append("分钟");
		}
		sb.append(seconds).append("秒");
		return sb.toString();
	}

	/**
	 * 根据毫秒数计算耗时为  XX时XX分XX秒
	 */
	public static String getCostTimeStrWithDays(long time) {
		long total = time / 1000;
		long seconds = total % 60;
		long minutes = total / 60;
		long hours = minutes / 60;
		minutes = minutes - hours * 60;
		long days = hours / 24;
		hours = hours - days * 24;

		//秒向上+1
		if (seconds > 0 && minutes < 59) {
			minutes ++;
		}
		//天数如果大于99,强制设置为99天23时59分
		if (days > 99) {
			return "99天23时59分";
		}

		StringBuffer sb = new StringBuffer();
		if (days > 0) {
			sb.append(days).append("天");
		}
		if (hours > 0) {
			sb.append(hours).append("小时");
		}
		if (minutes > 0) {
			sb.append(minutes).append("分钟");
		}
		return sb.toString();
	}

	/**
	 * 根据毫秒值计算耗时为 XX天XX时XX分XX秒
	 */
	public static String getCostTimeStrHourAndMinute(long time) {
		long days = time / (1000 * 60 * 60 * 24);
		long hours = (time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = ((time % (1000 * 60 * 60 * 24)) % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (((time % (1000 * 60 * 60 * 24)) % (1000 * 60 * 60)) % (1000 * 60)) / 1000;
		StringBuilder sb = new StringBuilder();
		if(days > 0){
			sb.append(days).append("天");
		}
		if(hours > 0 || days > 0){
			sb.append(hours).append("时");
		}
		if(minutes > 0 || hours > 0 || days > 0){
			sb.append(minutes).append("分");
		}
		sb.append(seconds).append("秒");
		return sb.toString();
	}

	/**
	 * 根据毫秒值计算耗时为 XX天XX时XX分
	 */
	public static String getCostTimeStrDay(long time){
		long days = time / (1000 * 60 * 60 * 24);
		long hours = (time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = ((time % (1000 * 60 * 60 * 24)) % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (time % (1000 * 60)) / 1000;
		StringBuilder sb = new StringBuilder();
		sb.append(days < 10 ? "0" + days : days).append("天");
		sb.append(hours < 10 ? "0" + hours : hours).append("时");
		sb.append(minutes < 10 ? "0" + minutes : minutes).append("分");
		return sb.toString();
	}

    /**
     * 根据毫秒值计算耗时，返回天时分的数组
     * @param time
     */
    public static List<String> getCostTimeStrDayArray(long time){
        long days = time / (1000 * 60 * 60 * 24);
        long hours = (time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = ((time % (1000 * 60 * 60 * 24)) % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (time % (1000 * 60)) / 1000;
        List<String> returnList = new ArrayList<>();
        returnList.add(days < 10 ? "0" + days : "" + days);
        returnList.add(hours < 10 ? "0" + hours : "" + hours);
        returnList.add(minutes < 10 ? "0" + minutes : "" + minutes);
        return returnList;
    }

	/**
	 * 根据毫秒值计算耗时为 XX
	 * @param time
	 * @return
	 */
	public static String getCostTimeStrMinute(long time) {
		String minutes = new BigDecimal(time).divide(new BigDecimal(60 * 1000),2,BigDecimal.ROUND_HALF_UP
		).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
		return minutes;
	}

	public static String getCostTimeStr(String endTime,String beginTime) {
		return getCostTimeStr(DateUtil.parse(endTime,"yyyy-MM-dd HH:mm:ss").getTime()
					- DateUtil.parse(beginTime,"yyyy-MM-dd HH:mm:ss").getTime());
	}

	/**
	 * 获取最近时间段的时间
	 * @param date
	 * @param interval
	 * @return
	 */
	public static Date getNearTime(Date date, int interval) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int m = c.get(Calendar.MINUTE);
		int mod = m % interval;
		if (m != 0 && mod != 0) {
			c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + interval - mod);
		}
		return c.getTime();
	}

	/**
	 * 获取指定天数内的每一天的keys
	 * @param days
	 * @return
	 */
	public static String[] getDayStrKeys(int days) {
		String[] res = new String[days - 1];
		Calendar c = Calendar.getInstance();
		for (int i = 0; i < days - 1; i++) {
			c.add(Calendar.DAY_OF_MONTH, -1);
			res[i] = DateUtil.format(c.getTime(), "yyyy-MM-dd");
		}
		return res;
	}

	/**
	 * 格式化为2018/9/21 星期五 下午 12:02:56
	 * @param str
	 * @return
	 */
	public static String formatWeekDate(String str) {
		Date date = parse(str, "yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR) + "/"
				+ (c.get(Calendar.MONTH) + 1) + "/"
				+ c.get(Calendar.DAY_OF_MONTH) + " "
				+ weeks[c.get(c.DAY_OF_WEEK)-1] + " "
				+ (c.get(Calendar.HOUR_OF_DAY) < 12 ? "上午" : "下午") + " "
				+ str.split(" ")[1];
	}

	/**
	 *	formatWeekDate的反向
	 * @param str
	 * @return
	 */
	public static Date parseWeekDate(String str) {
		return DateUtil.parse(str.split(" ")[0] + " " + str.split(" ")[3], "yyyy/MM/dd HH:mm:ss");
	}

	/**
	 * 获取今天的00：00：00
	 * @return
	 */
	public static Date todayFirstDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取今天的23：59：59
	 * @return
	 */
	public static Date todayLastDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 获取某天日期的小时
	 * @return
	 */
	public static Integer getDateHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

    /**
     * 获取n天之前的日期
     */
    public static Date getDateDaysBefore(Date date, int daysBefore) {
    	Calendar c = Calendar.getInstance();
    	c.setTime(dropHMS(date));
    	c.add(Calendar.DATE, - daysBefore);
        return new Date(c.getTimeInMillis());
    }

    /**
     * 日期舍弃时分秒
     */
    public static Date dropHMS(Date date) {
        return parse(format(date, "yyyy-MM-dd"), "yyyy-MM-dd");
    }

    /**
     * 格式化日期
     */
    public static String format(Date date, String pattern) {
        try {
            return new SimpleDateFormat(pattern).format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期
     */
    public static Date parse(String date, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

	/**
	 * 获取两个时间的毫秒差值
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long getTimeMillis(Date d1, Date d2) {
		return d1.getTime() - d2.getTime();
	}

	/**
	 * 获取从昨天开始前n天的日期，格式化成“yyyy-MM-dd”格式 返回一个数组
	 * @param n
	 * @return
	 */
	public static String[] getSomeDaysAgoArray(int n) {
		long today = System.currentTimeMillis();
		long ago_time = today - 1000L * 60 * 60 * 24 * n;
		List<String> list = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			String day = DateUtil.formatDate(new Date(ago_time), "yyyy-MM-dd");
			ago_time += 1000 * 60 * 60 * 24;
			list.add(day);
		}
		String[] strs = new String[n];
		list.toArray(strs);
		return strs;
	}

	/**
	 * 根据过去时间戳获取到现在的时间间隔，格式化成“yyyy-MM-dd”格式 返回一个数组(包括今天)
	 * @return
	 */
	public static String[] getTimeArray(Long time) {
		long ago_time = time;
		long jg = 1000L * 60 * 60 * 24;
		List<String> list = new ArrayList<>();
		while(System.currentTimeMillis() - ago_time > jg){
			String day = DateUtil.formatDate(new Date(ago_time), "yyyy-MM-dd");
			ago_time += jg;
			list.add(day);
		}
		list.add(formatDate(new Date(System.currentTimeMillis()),"yyyy-MM-dd"));
		String[] strs = new String[list.size()];
		list.toArray(strs);
		return strs;
	}



	/**
	 * 获取昨天的日期 yyyy-MM-dd
	 */
	public static String getYesterdayDate(){
		String[] someDaysAgoArray = getSomeDaysAgoArray(1);
		return someDaysAgoArray[0];
	}

	public static Date getSundayThisWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (null == date)
			return null;
		for (int i = 0; i < 7 ; i++) {
			calendar.setTime(new Date(date.getTime() + i * 86400000));
			if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {      //如果是周日
				return new Date(calendar.getTimeInMillis());
			}
		}
		return null;
	}

	/**
	 * 预警统计图补充3分钟
	 * @param s
	 * @param e
	 * @return
	 */
	public static Map<String, Date> deviationTime(String s, String e) {
		Calendar c = Calendar.getInstance();
		Date ds = parse(s, "yyyy-MM-dd HH:mm:ss");
		c.setTime(ds);
		Date de = parse(e, "yyyy-MM-dd HH:mm:ss");
		c.setTime(de);
		long dayE = c.get(Calendar.DAY_OF_MONTH);
		long ls = ds.getTime();
		long le = de.getTime();
		long _3min = 6 * 60 * 1000;
		long remainder = (le - ls) % _3min;
		le += _3min - remainder;
		c.setTime(new Date(le));
		if (c.get(Calendar.DAY_OF_MONTH) != dayE) {
			le -= remainder;
		}
		Map<String, Date> map = new HashMap<>();
		map.put("start", new Date(ls));
		map.put("end", new Date(le));
		return map;
	}

	public static String[] getLastMonth(int num){
        Calendar c = Calendar.getInstance();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            list.add(year + "-" + (month < 10 ? "0" + month : month));
            c.add(Calendar.MONTH,-1);
        }
        String[] strs = new String[list.size()];
        Collections.reverse(list);
        list.toArray(strs);
        return strs;
    }

    public static void main(String[] args) {
    }



}
