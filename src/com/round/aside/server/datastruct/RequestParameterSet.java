package com.round.aside.server.datastruct;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.round.aside.server.util.StringUtil;

/**
 * Servlet请求原始参数集
 * 
 * @author A Shuai
 * @date 2016-6-11
 * 
 */
public final class RequestParameterSet {

    private int mIndex;
    private final List<KeyEntry> mKeyList;
    private final Map<String, Integer> mKeyIndexMap;
    private final Map<String, String> mKeyValueMap;

    public RequestParameterSet() {
        mIndex = 0;
        mKeyList = new LinkedList<KeyEntry>();
        mKeyIndexMap = new HashMap<String, Integer>();
        mKeyValueMap = new HashMap<String, String>();
    }

    public RequestParameterSet addKey(String mKey, boolean mNecessary) {
        mKeyList.add(new KeyEntry(mKey, mNecessary));
        mKeyIndexMap.put(mKey, mIndex++);
        return this;
    }

    public RequestParameterSet addKey(String mKey, boolean mNecessary,
            String mRely) {
        mKeyList.add(new KeyEntry(mKey, mNecessary, mRely));
        mKeyIndexMap.put(mKey, mIndex++);
        return this;
    }

    /**
     * 根据已经输入的键集读取对应的值集。如果未读取到一个必须键值，返回一个错误信息；如果全部读取正确，则返回空字符串
     * 
     * @param request
     *            Servlet请求，用于读取键值
     * @return 执行成功返回null，否则返回未读取到的键值
     */
    public String readParameter(HttpServletRequest request) {
        if (mIndex == 0) {
            throw new IllegalStateException("the parameter set isn't init");
        }
        String readResult;
        for (KeyEntry mKeyEntry : mKeyList) {
            readResult = recurReadParameter(request, mKeyEntry);
            if (readResult != null) {
                return readResult;
            }
        }
        return null;
    }

    /**
     * 递归读取指定键对应的值
     * 
     * @param request
     *            待读取键值的Servlet输入
     * @param mKeyEntry
     *            带读取值的键
     * @return 成功返回null，否则返回未成功读取的键名
     */
    private String recurReadParameter(HttpServletRequest request,
            KeyEntry mKeyEntry) {
        if (!StringUtil.isEmpty(mKeyValueMap.get(mKeyEntry.mKey))) {
            return null;
        }
        if (StringUtil.isEmpty(mKeyEntry.mRely)) {
            String result = request.getParameter(mKeyEntry.mKey);
            if (StringUtil.isEmpty(result)) {
                if (mKeyEntry.mNecessary) {
                    return mKeyEntry.mKey;
                }
            } else {
                mKeyValueMap.put(mKeyEntry.mKey, result);
            }
            return null;
        }
        return recurReadParameter(request,
                mKeyList.get(mKeyIndexMap.get(mKeyEntry.mKey)));
    }

    public String getValue(String key) {
        return mKeyValueMap.get(key);
    }

    public int getValueAsInt(String key) throws NumberFormatException {
        String mValueStr = mKeyValueMap.get(key);
        return Integer.parseInt(mValueStr);
    }

    public long getValueAsLong(String key) throws NumberFormatException {
        String mValueStr = mKeyValueMap.get(key);
        return Long.parseLong(mValueStr);
    }

    public float getValueAsFloat(String key) throws NumberFormatException {
        String mValueStr = mKeyValueMap.get(key);
        return Float.parseFloat(mValueStr);
    }

    public double getValueAsDouble(String key) throws NumberFormatException {
        String mValueStr = mKeyValueMap.get(key);
        return Double.parseDouble(mValueStr);
    }

    public boolean getValueAsBoolean(String key) {
        return "true".equals(mKeyValueMap.get(key));
    }

    /**
     * 清空重置
     */
    public void clear() {
        mIndex = 0;
        mKeyList.clear();
        mKeyIndexMap.clear();
        mKeyValueMap.clear();
    }

    /**
     * 用于Map中的Key
     * 
     * @author A Shuai
     * @date 2016-6-11
     * 
     */
    private static class KeyEntry {

        private final String mKey;
        private final boolean mNecessary;
        private final String mRely;

        public KeyEntry(String mKey, boolean mNecessary) {
            this.mKey = mKey;
            this.mNecessary = mNecessary;
            mRely = null;
        }

        public KeyEntry(String mKey, boolean mNecessary, String mRely) {
            this.mKey = mKey;
            this.mNecessary = mNecessary;
            this.mRely = mRely;
        }

        @Override
        public int hashCode() {
            return mKey.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof KeyEntry) {
                KeyEntry mObjKeyEntry = (KeyEntry) obj;
                return mKey.equals(mObjKeyEntry.mKey);
            }
            return false;
        }

    }

}
