package com.round.aside.server.bean.requestparameter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.round.aside.server.datastruct.RequestParameterSet;
import com.round.aside.server.util.StringUtil;

/**
 * 发布广告Servlet对应的请求参数集
 * 
 * @author A Shuai
 * @date 2016-6-22
 * 
 */
public class PublishAdRequestPara extends UserIDTokenRequestPara {

    private final String adTitle; // 广告标题
    private final String adLogoImgID; // Logo图ID
    private final List<String> adImgIDSet; // 广告图集
    private final String adDescription; // 广告详情描述
    private final List<String> adAreaSet; // 作用区域
    private final Timestamp adStartTimestamp; // 起始日期
    private final Timestamp adEndTimestamp; // 终止日期
    private final boolean listPriority; // 是否列表优先
    private final boolean carousel; // 是否轮播

    public PublishAdRequestPara(Builder mTBuilder) {
        super(mTBuilder);

        adTitle = mTBuilder.adTitle;
        adLogoImgID = mTBuilder.adLogoImgID;
        adImgIDSet = mTBuilder.adImgIDSet;
        adDescription = mTBuilder.adDescription;
        adAreaSet = mTBuilder.adAreaSet;
        adStartTimestamp = new Timestamp(mTBuilder.adStartDate.getTime());
        adEndTimestamp = new Timestamp(mTBuilder.adEndDate.getTime());
        listPriority = mTBuilder.listPriority;
        carousel = mTBuilder.carousel;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public String getAdLogoImgID() {
        return adLogoImgID;
    }

    public List<String> getAdImgIDSet() {
        return adImgIDSet;
    }

    public String getAdDescription() {
        return adDescription;
    }

    public List<String> getAdAreaSet() {
        return adAreaSet;
    }

    public Timestamp getAdStartTimestamp() {
        return adStartTimestamp;
    }

    public Timestamp getAdEndTimestamp() {
        return adEndTimestamp;
    }

    public boolean isListPriority() {
        return listPriority;
    }

    public boolean isCarousel() {
        return carousel;
    }

    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-6-22
     * 
     */
    public static class Builder extends
            UserIDTokenRequestPara.Builder<PublishAdRequestPara> {

        private String adTitle;
        private String adLogoImgID;
        private List<String> adImgIDSet;
        private String adDescription;
        private List<String> adAreaSet;
        private Date adStartDate;
        private Date adEndDate;
        private boolean listPriority;
        private boolean carousel;

        public Builder() {
            super();

            adImgIDSet = new ArrayList<String>();
            adAreaSet = new ArrayList<String>();

            adStartDate = new Date();
            adEndDate = new Date();
        }

        @Override
        protected void onFillFieldKey(RequestParameterSet mParaSet) {
            super.onFillFieldKey(mParaSet);

            mParaSet.addKey("adTitle", true).addKey("adLogoImgID", true)
                    .addKey("adImgIDSetStr", true)
                    .addKey("adDescription", true).addKey("adAreaSetStr", true)
                    .addKey("adStartTime", true).addKey("adEndTime", true)
                    .addKey("listPriority", false).addKey("carousel", false);
        }

        @Override
        protected String onFillField(RequestParameterSet mParaSet) {
            String error = super.onFillField(mParaSet);
            if (!StringUtil.isEmpty(error)) {
                return error;
            }

            adTitle = mParaSet.getValue("adTitle");
            adLogoImgID = mParaSet.getValue("adLogoImgID");

            String adImgIDSetStr = mParaSet.getValue("adImgIDSetStr");
            adImgIDSet.clear();
            String[] adImgIDSetArray = adImgIDSetStr.split("-");
            for (String str : adImgIDSetArray) {
                if (!StringUtil.isEmpty(str)) {
                    adImgIDSet.add(str);
                }
            }

            adDescription = mParaSet.getValue("adDescription");

            String adAreaSetStr = mParaSet.getValue("adAreaSetStr");
            adAreaSet.clear();
            String[] adAreaSetStrArray = adAreaSetStr.split("-");
            for (String str : adAreaSetStrArray) {
                if (!StringUtil.isEmpty(str)) {
                    adAreaSet.add(str);
                }
            }

            String adStartTimeStr = mParaSet.getValue("adStartTime");
            try {
                adStartDate.setTime(Long.parseLong(adStartTimeStr));
            } catch (Exception e) {
                e.printStackTrace();
                return "广告开始时间参数非法";
            }

            String adEndTimeStr = mParaSet.getValue("adEndTime");
            try {
                adEndDate.setTime(Long.parseLong(adEndTimeStr));
            } catch (Exception e) {
                e.printStackTrace();
                return "广告结束时间参数非法";
            }

            listPriority = "true".equals(mParaSet.getValue("listPriority"));
            carousel = "true".equals(mParaSet.getValue("carousel"));

            return null;
        }

        @Override
        protected PublishAdRequestPara buildInstance() {
            return new PublishAdRequestPara(this);
        }

    }

}
