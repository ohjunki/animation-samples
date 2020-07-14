package com.ao.libandroidutils;
import android.util.Log;

/**
 *
 *  Created by jms on 2016-12-23.
 *
 *
 *  1. 기본적으로 DEBUG 빌드 때만 출력
 *  2. DLog.d 는 Log.d 처럼 출력
 *  3. DLog.dd 는 "호출 클래스 + 호출 메소드 + msg + 코드 링크" 형태로 출력
 *  4. DLog.dd 는 tag 앞에 "ao_" 가 기본적으로 달려있어서 Logcat 창 filter값에 "ao_"를 설정해서 DLog만 보는것이 가능
 *
 *     example)
 *     DLog.dd()
 *     -> D/ReNewMainActivity: [onCreate()] : [] (ReNewMainActivity.kt:109)
 *     DLog.dd("msg_blah_blah")
 *     -> D/ReNewMainActivity: [onCreate()] : [msg_blah_blah] (ReNewMainActivity.kt:109)
 *     DLog.dd("tag_name", "msg_blah_blah")
 *     -> D/tag_name: [ReNewMainActivity :: onCreate()] : [msg_blah_blah] (ReNewMainActivity.kt:109)
 *
 *
 */

public final class DLog {
    private static final String filter_string = "ao_";

    private static final int VERBOSE = 2;
    private static final int DEBUG = 3;
    private static final int INFO = 4;
    private static final int WARN = 5;
    private static final int ERROR = 6;
    private static final int ASSERT = 7;

    public static void v(String tag, String msg) {
        if (AndroidUtilLibrary.DEBUG) Log.v(tag, msg);
    }

    public static void vv() {
        log(VERBOSE, "");
    }

    public static void vv(String msg) {
        log(VERBOSE, msg);
    }

    public static void vv(String tag, String msg) {
        log(VERBOSE, tag, msg);
    }

    public static void d(String tag, String msg) {
        if (AndroidUtilLibrary.DEBUG) Log.d(tag, msg);
    }

    public static void dd() {
        log(DEBUG, "");
    }

    public static void dd(String msg) {
        log(DEBUG, msg);
    }

    public static void dd(String tag, String msg) {
        log(DEBUG, tag, msg);
    }

    public static void i(String tag, String msg) {
        if (AndroidUtilLibrary.DEBUG) Log.i(tag, msg);
    }

    public static void ii() {
        log(INFO, "");
    }

    public static void ii(String msg) {
        log(INFO, msg);
    }

    public static void ii(String tag, String msg) {
        log(INFO, tag, msg);
    }

    public static void w(String tag, String msg) {
        if (AndroidUtilLibrary.DEBUG) Log.w(tag, msg);
    }

    public static void ww() {
        log(WARN, "");
    }

    public static void ww(String msg) {
        log(WARN, msg);
    }

    public static void ww(String tag, String msg) {
        log(WARN, tag, msg);
    }

    public static void e(String tag, String msg) {
        if (AndroidUtilLibrary.DEBUG) Log.e(tag, msg);
    }

    public static void ee() {
        log(ERROR, "");
    }

    public static void ee(String msg) {
        log(ERROR, msg);
    }

    public static void ee(String tag, String msg) {
        log(ERROR, tag, msg);
    }

    private static void log(int priority, String msg) {
        if (AndroidUtilLibrary.DEBUG) {
            StringBuilder msgBuilder = new StringBuilder();
            msgBuilder.append("[").append(Thread.currentThread().getStackTrace()[4].getMethodName()).append("()").append("]").append(" :")
                    .append(" [").append(msg).append("]")
                    .append(" (").append(Thread.currentThread().getStackTrace()[4].getFileName()).append(":")
                    .append(Thread.currentThread().getStackTrace()[4].getLineNumber()).append(")");

            String tag = Thread.currentThread().getStackTrace()[4].getFileName().replace(".java", "").replace(".kt", "");

            Log.println(priority, filter_string + tag, msgBuilder.toString());

        }
    }

    private static void log(int priority, String tag, String msg) {
        if (AndroidUtilLibrary.DEBUG) {
            StringBuilder msgBuilder = new StringBuilder();
            msgBuilder.append("[").append(Thread.currentThread().getStackTrace()[4].getFileName().replace(".java", "").replace(".kt", ""))
                    .append(" :: ").append(Thread.currentThread().getStackTrace()[4].getMethodName()).append("()").append("]").append(" :")
                    .append(" [").append(msg).append("]")
                    .append(" (").append(Thread.currentThread().getStackTrace()[4].getFileName()).append(":")
                    .append(Thread.currentThread().getStackTrace()[4].getLineNumber()).append(")");

            Log.println(priority, filter_string + tag, msgBuilder.toString());

        }
    }

    public static String getFileName() {
        return Thread.currentThread().getStackTrace()[3].getFileName();
    }

    public static String getClassName() {
        return Thread.currentThread().getStackTrace()[3].getClassName();
    }

    public static String getMethodName() {
        return Thread.currentThread().getStackTrace()[3].getMethodName();
    }

    public static String getLineInfo() {
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append(Thread.currentThread().getStackTrace()[3].getFileName()).append(":")
                .append(Thread.currentThread().getStackTrace()[3].getLineNumber());
        return msgBuilder.toString();
    }
}
