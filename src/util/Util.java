package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Util {
	
    // get current time string
    public static String getCurrentTimeString()
    {
		// set time
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
	
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+ 1; //month starts from 0
		int date = cal.get(Calendar.DATE);
		
//		int hour = cal.get(Calendar.HOUR);//12h 
		int hour = cal.get(Calendar.HOUR_OF_DAY);//24h
//		String am_pm = (cal.get(Calendar.AM_PM)== 0) ?"AM":"PM"; // 0 AM, 1 PM
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		int mSec = cal.get(Calendar.MILLISECOND);
		
		String strTime = year 
				+ "" + String.format(Locale.US,"%02d", month)
				+ "" + String.format(Locale.US,"%02d", date)
//				+ "_" + am_pm
				+ "-" + String.format(Locale.US,"%02d", hour)
				+ "" + String.format(Locale.US,"%02d", min)
				+ "" + String.format(Locale.US,"%02d", sec); 
//				+ "-" + String.format(Locale.US,"%03d", mSec);
//		System.out.println("time = "+  strTime );
		return strTime;
    }
	
    public static String getYoutubeId(String url) {

        String videoId = "";

        if (url != null && url.trim().length() > 0 && url.startsWith("http")) {
            String expression = "^.*((youtu.be\\/)|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??(v=)?([^#\\&\\?]*).*";
            CharSequence input = url;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);//??? some Urls are NG
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(8);
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    videoId = groupIndex1;
            }
        }
        return videoId;
    }
    
    // convert YouTube duration
    public static String convertYouTubeDuration(String duration) {
        String youtubeDuration = duration; //"PT1H2M30S"; // "PT1M13S";
        Calendar c = new GregorianCalendar();
        try {
            DateFormat df = new SimpleDateFormat("'PT'mm'M'ss'S'");
            Date d = df.parse(youtubeDuration);
            c.setTime(d);
        } catch (ParseException e) {
            try {
                DateFormat df = new SimpleDateFormat("'PT'hh'H'mm'M'ss'S'");
                Date d = df.parse(youtubeDuration);
                c.setTime(d);
            } catch (ParseException e1) {
                try {
                    DateFormat df = new SimpleDateFormat("'PT'ss'S'");
                    Date d = df.parse(youtubeDuration);
                    c.setTime(d);
                } catch (ParseException e2) {
                }
            }
        }
        c.setTimeZone(TimeZone.getDefault());

        String time = "";
        if ( c.get(Calendar.HOUR) > 0 ) {
            if ( String.valueOf(c.get(Calendar.HOUR)).length() == 1 ) {
                time += "0" + c.get(Calendar.HOUR);
            }
            else {
                time += c.get(Calendar.HOUR);
            }
            time += ":";
        }
        // test minute
        if ( String.valueOf(c.get(Calendar.MINUTE)).length() == 1 ) {
            time += "0" + c.get(Calendar.MINUTE);
        }
        else {
            time += c.get(Calendar.MINUTE);
        }
        time += ":";
        // test second
        if ( String.valueOf(c.get(Calendar.SECOND)).length() == 1 ) {
            time += "0" + c.get(Calendar.SECOND);
        }
        else {
            time += c.get(Calendar.SECOND);
        }
        return time ;
    }    
}
