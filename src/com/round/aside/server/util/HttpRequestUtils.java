package com.round.aside.server.util;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.round.aside.server.bean.RequestInfoBean;

/**
 * HTTP请求相关的工具类
 * 
 * @author A Shuai
 * @date 2016-4-29
 * 
 */
public final class HttpRequestUtils {

    private HttpRequestUtils() {
    }

    private static final String CHROME_BROWSER_REG = "\\s*(([a-zA-Z]+)/((\\d+)(\\.\\d+)*))\\s*";

    public static RequestInfoBean getOSBrowserInfo(HttpServletRequest request) {
        if (request == null) {
            throw new NullPointerException(
                    "the parameter shouldn't be null pointer");
        }

        String mUserAgent = request.getHeader("User-Agent");
        RequestInfoBean.Builder mBuilder = new RequestInfoBean.Builder();

        StringTokenizer mST = null;

        if (mUserAgent.indexOf("MSIE") != -1) {
            mST = new StringTokenizer(mUserAgent, ";");
            mST.nextToken();
        } else if (mUserAgent.indexOf("Chrome") != -1) {

            Pattern mPattern = Pattern.compile(CHROME_BROWSER_REG);
            Matcher mMatcher = mPattern.matcher(mUserAgent);
            while (mMatcher.find()) {
                mBuilder.appendBrowser(mMatcher.group(2), mMatcher.group(3));
            }

            int mLeftBrac = mUserAgent.indexOf('(');
            int mRightBrac = mUserAgent.indexOf(')');
            if (mLeftBrac > mRightBrac) {
                throw new IllegalArgumentException(
                        "the http request header string isn't illegal!");
            }

            mBuilder.setOS(mUserAgent.substring(mLeftBrac + 1, mRightBrac)
                    .replace(" ", ""));

        } else {

        }

        return mBuilder.build();
    }

}
