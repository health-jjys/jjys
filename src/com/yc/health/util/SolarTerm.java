package com.yc.health.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SolarTerm {

	private long[] sTermInfo = new long[] { 0, 21208, 42467, 63836, 85337, 107014,
			128867, 150921, 173149, 195551, 218072, 240693, 263343, 285989,
			308563, 331033, 353350, 375494, 397447, 419210, 440795, 462224,
			483532, 504758 };
	private String[] solarTerm = new String[] { "小寒", "大寒", "立春", "雨水", "惊蛰", "春分",
			"清明", "谷雨", "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑", "白露",
			"秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至" };

	public SolarTerm(){}
	
	public String getSolarTerm() throws Exception{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar clendar = Calendar.getInstance();
		clendar.setTime(sdf.parse(sdf.format((new Date()))));

		int year = clendar.get(Calendar.YEAR);
		int month = clendar.get(Calendar.MONTH);
		int day = clendar.get(Calendar.DATE);
		// 节气
		int tmp1 = sTerm(year, month * 2);
		int tmp2 = sTerm(year, month * 2 + 1);

		String solarTerms = null;
		System.out.println(day+"  day == tmp1 "+tmp2+"  "+(day >= tmp2));
		System.out.println(day+"  day == tmp1 "+tmp1+"  "+(day >= tmp1));
		if(day < tmp1){
			solarTerms = solarTerm[23];
		}else if (day >= tmp1 && day< tmp2) {
			solarTerms = solarTerm[month * 2];
		} else if (day >= tmp2) {
			solarTerms = solarTerm[month * 2 + 1];
		} else {
			solarTerms = "";
		}

		return transferSolarTerm(solarTerms);
	}

	private int sTerm(int y, int n) throws Exception {
		if (y == 2009 && n == 2) {
			sTermInfo[n] = 43467;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.set(1900, 0, 6, 2, 5);
		Date _1900 = calendar.getTime();
		long millis = (long) ((31556925974.7 * (y - 1900) + sTermInfo[n] * 60000) + _1900
				.getTime());
		calendar.setTimeInMillis(millis);
		return calendar.get(Calendar.DATE);
	}

	private String transferSolarTerm( String solarTerm ) {
		String result = null;
		if ( "小寒".equals(solarTerm) ) {
			result = "LesserCold";
		} else if ( "大寒".equals(solarTerm) ) {
			result = "GreaterCold";
		} else if ( "立春".equals(solarTerm) ) {
			result = "TheBeginningOfSpring";
		} else if ( "雨水".equals(solarTerm) ) {
			result = "RainWater";
		} else if ( "惊蛰".equals(solarTerm) ) {
			result = "TheWakingOfInsects";
		} else if ( "春分".equals(solarTerm) ) {
			result = "TheSpringEquinox";
		} else if ( "清明".equals(solarTerm) ) {
			result = "PureBrightness";
		} else if ( "谷雨".equals(solarTerm) ) {
			result = "GrainRain";
		} else if ( "立夏".equals(solarTerm) ) {
			result = "TheBeginningoOfSummer";
		} else if ( "小满".equals(solarTerm) ) {
			result = "LesserFullnessOfGrain";
		} else if ( "芒种".equals(solarTerm) ) {
			result = "GrainInBeard";
		} else if ( "夏至".equals(solarTerm) ) {
			result = "TheSummerSolstice";
		} else if ( "小暑".equals(solarTerm) ) {
			result = "LesserHeat";
		} else if ( "大暑".equals(solarTerm) ) {
			result = "GreatHeat";
		} else if ( "立秋".equals(solarTerm) ) {
			result = "TheBeginningOfAutumn";
		} else if ( "处暑".equals(solarTerm) ) {
			result = "TheEndOfHeat";
		} else if ( "白露".equals(solarTerm) ) {
			result = "WhiteDew";
		} else if ( "秋分".equals(solarTerm) ) {
			result = "TheAutumnalEquinox";
		} else if ( "寒露".equals(solarTerm) ) {
			result = "ColdDew";
		} else if ( "霜降".equals(solarTerm) ) {
			result = "FirstDescent";
		} else if ( "立冬".equals(solarTerm) ) {
			result = "TheBeginningOfWinter";
		} else if ( "小雪".equals(solarTerm) ) {
			result = "LesserSnow";
		} else if ( "大雪".equals(solarTerm) ) {
			result = "GreaterSnow";
		} else if ( "冬至".equals(solarTerm) ) {
			result = "TheWinterSolstice";
		}
		
		return result;
	}
}
