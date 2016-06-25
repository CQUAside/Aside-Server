package com.round.aside.server.bean.statuscode;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author A Shuai
 * @date 2016-6-24
 * 
 */
public class AdDetailsStatusCodeBean extends StatusCodeBean {

    private final int adID;
    private final String title;
    private final String content;
    private final Date startTime;
    private final Date endTime;
    private final int clickCount;
    private final int collectCount;

    private final int userID;
    private final String nickName;

    private final String logoPicID;
    private final String logoPicPath;

    private final List<String> picIDSet;
    private final List<String> oriPicPathSet;
    private final List<String> thuPicPathSet;

    public AdDetailsStatusCodeBean(Builder mTBuilder) {
        super(mTBuilder);

        adID = mTBuilder.mAdID;
        title = mTBuilder.mTitle;
        content = mTBuilder.mContent;
        startTime = mTBuilder.mStartTime;
        endTime = mTBuilder.mEndTime;
        clickCount = mTBuilder.mClickCount;
        collectCount = mTBuilder.mCollectCount;

        userID = mTBuilder.mUserID;
        nickName = mTBuilder.mNickName;

        logoPicID = mTBuilder.mLogoPicID;
        logoPicPath = mTBuilder.mLogoPicPath;

        picIDSet = mTBuilder.mPicIDSet;
        oriPicPathSet = mTBuilder.mOriPicPathSet;
        thuPicPathSet = mTBuilder.mThuPicPathSet;
    }

    public int getAdID() {
        return adID;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getClickCount() {
        return clickCount;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public int getUserID() {
        return userID;
    }

    public String getNickName() {
        return nickName;
    }

    public String getLogoPicID() {
        return logoPicID;
    }

    public String getLogoPicPath() {
        return logoPicPath;
    }

    public List<String> getPicIDSet() {
        return picIDSet;
    }

    public List<String> getOriPicPathSet() {
        return oriPicPathSet;
    }

    public List<String> getThuPicPathSet() {
        return thuPicPathSet;
    }

    /**
     * 
     * 
     * @author A Shuai
     * @date 2016-6-24
     * 
     */
    public static class Builder extends StatusCodeBean.Builder {

        private int mAdID;
        private String mTitle;
        private String mContent;
        private final Date mStartTime;
        private final Date mEndTime;
        private int mClickCount;
        private int mCollectCount;

        private int mUserID;
        private String mNickName;

        private String mLogoPicID;
        private String mLogoPicPath;

        private final List<String> mPicIDSet;
        private final List<String> mOriPicPathSet;
        private final List<String> mThuPicPathSet;

        public Builder() {
            mStartTime = new Date();
            mEndTime = new Date();

            mPicIDSet = new ArrayList<String>();
            mOriPicPathSet = new ArrayList<String>();
            mThuPicPathSet = new ArrayList<String>();
        }

        public Builder setAdID(int mAdID) {
            this.mAdID = mAdID;
            return this;
        }

        public Builder setTitle(String mTitle) {
            this.mTitle = mTitle;
            return this;
        }

        public Builder setContent(String mContent) {
            this.mContent = mContent;
            return this;
        }

        public Builder setStartTime(Timestamp mTStartTime) {
            mStartTime.setTime(mTStartTime.getTime());
            return this;
        }

        public Builder setEndTime(Timestamp mTEndTime) {
            mEndTime.setTime(mTEndTime.getTime());
            return this;
        }

        public Builder setClickCount(int mClickCount) {
            this.mClickCount = mClickCount;
            return this;
        }

        public Builder setCollectCount(int mCollectCount) {
            this.mCollectCount = mCollectCount;
            return this;
        }

        public Builder setUserID(int mUserID) {
            this.mUserID = mUserID;
            return this;
        }

        public Builder setNickName(String mNickName) {
            this.mNickName = mNickName;
            return this;
        }

        public Builder setLogoPicID(String mLogoPicID) {
            this.mLogoPicID = mLogoPicID;
            return this;
        }

        public Builder setLogoPicPath(String mLogoPicPath) {
            this.mLogoPicPath = mLogoPicPath;
            return this;
        }

        public Builder addPicID(String mTPicID) {
            mPicIDSet.add(mTPicID);
            return this;
        }

        public Builder addOriPicPath(String mTOriPicPath) {
            mOriPicPathSet.add(mTOriPicPath);
            return this;
        }

        public Builder addThuPicPath(String mTThuPicPath) {
            mThuPicPathSet.add(mTThuPicPath);
            return this;
        }

        public AdDetailsStatusCodeBean build() {
            check();
            return new AdDetailsStatusCodeBean(this);
        }

    }

}
