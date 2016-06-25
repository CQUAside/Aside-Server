package com.round.aside.server.bean.jsonbean.result;

import java.util.Date;
import java.util.List;

import com.round.aside.server.bean.statuscode.AdDetailsStatusCodeBean;

/**
 * 
 * 
 * @author A Shuai
 * @date 2016-6-25
 * 
 */
public class AdDetailsObjBean {

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

    public AdDetailsObjBean(AdDetailsStatusCodeBean mAdDetailsSCB) {
        adID = mAdDetailsSCB.getAdID();
        title = mAdDetailsSCB.getTitle();
        content = mAdDetailsSCB.getContent();
        startTime = mAdDetailsSCB.getStartTime();
        endTime = mAdDetailsSCB.getEndTime();
        clickCount = mAdDetailsSCB.getClickCount();
        collectCount = mAdDetailsSCB.getCollectCount();

        userID = mAdDetailsSCB.getUserID();
        nickName = mAdDetailsSCB.getNickName();

        logoPicID = mAdDetailsSCB.getLogoPicID();
        logoPicPath = mAdDetailsSCB.getLogoPicPath();

        picIDSet = mAdDetailsSCB.getPicIDSet();
        oriPicPathSet = mAdDetailsSCB.getOriPicPathSet();
        thuPicPathSet = mAdDetailsSCB.getThuPicPathSet();
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

}
