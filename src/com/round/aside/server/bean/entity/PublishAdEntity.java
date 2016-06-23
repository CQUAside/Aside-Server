package com.round.aside.server.bean.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.round.aside.server.bean.requestparameter.PublishAdRequestPara;
import com.round.aside.server.datastruct.RequestParameterSet;
import com.round.aside.server.util.StringUtil;

/**
 * 发布广告用的实体类
 * 
 * @author A Shuai
 * @date 2016-6-8
 * 
 */
public class PublishAdEntity {

    private final String adTitle; // 广告标题
    private final String adLogoImgID; // Logo图ID
    private final List<String> adImgIDSet; // 广告图集
    private final String adDescription; // 广告详情描述
    private final List<String> adAreaSet; // 作用区域
    private final Timestamp adStartTimestamp; // 起始日期
    private final Timestamp adEndTimestamp; // 终止日期
    private final boolean listPriority; // 是否列表优先
    private final boolean carousel; // 是否轮播

    @Deprecated
    public PublishAdEntity(Builder mBuilder) {
        adTitle = mBuilder.adTitle;
        adLogoImgID = mBuilder.adLogoImgID;
        adImgIDSet = mBuilder.adImgIDSet;
        adDescription = mBuilder.adDescription;
        adAreaSet = mBuilder.adAreaSet;
        adStartTimestamp = new Timestamp(mBuilder.adStartDate.getTime());
        adEndTimestamp = new Timestamp(mBuilder.adEndDate.getTime());
        listPriority = mBuilder.listPriority;
        carousel = mBuilder.carousel;
    }

    public PublishAdEntity(PublishAdRequestPara mPubAdRP) {
        adTitle = mPubAdRP.getAdTitle();
        adLogoImgID = mPubAdRP.getAdLogoImgID();
        adImgIDSet = mPubAdRP.getAdImgIDSet();
        adDescription = mPubAdRP.getAdDescription();
        adAreaSet = mPubAdRP.getAdAreaSet();
        adStartTimestamp = mPubAdRP.getAdStartTimestamp();
        adEndTimestamp = mPubAdRP.getAdEndTimestamp();
        listPriority = mPubAdRP.isListPriority();
        carousel = mPubAdRP.isCarousel();
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
     * @date 2016-6-8
     * 
     */
    @Deprecated
    public static class Builder {

        private boolean init;

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
            init = false;

            adImgIDSet = new ArrayList<String>();
            adAreaSet = new ArrayList<String>();

            adStartDate = new Date();
            adEndDate = new Date();
        }

        /**
         * 填充字段
         * 
         * @param request
         * @return
         * @throws IllegalArgumentException
         */
        @Deprecated
        public Builder fillField(HttpServletRequest request)
                throws IllegalArgumentException {
            adTitle = request.getParameter("adTitle");
            if (StringUtil.isEmpty(adTitle)) {
                throw new IllegalArgumentException("广告标题参数不能为空");
            }

            adLogoImgID = request.getParameter("adLogoImgID");
            if (StringUtil.isEmpty(adLogoImgID)) {
                throw new IllegalArgumentException("广告Logo图ID参数不能为空");
            }

            String adImgIDSetStr = request.getParameter("adImgIDSetStr");
            adImgIDSet.clear();
            String[] adImgIDSetArray = adImgIDSetStr.split("-");
            for (String str : adImgIDSetArray) {
                if (!StringUtil.isEmpty(str)) {
                    adImgIDSet.add(str);
                }
            }

            adDescription = request.getParameter("adDescription");
            if (StringUtil.isEmpty(adDescription)) {
                throw new IllegalArgumentException("广告描述参数不能为空");
            }

            String adAreaSetStr = request.getParameter("adAreaSetStr");
            adAreaSet.clear();
            String[] adAreaSetStrArray = adAreaSetStr.split("-");
            for (String str : adAreaSetStrArray) {
                if (!StringUtil.isEmpty(str)) {
                    adAreaSet.add(str);
                }
            }
            if (adAreaSet.size() == 0) {
                throw new IllegalArgumentException("广告作用区域参数不能为空，至少有一个");
            }

            String adStartTimeStr = request.getParameter("adStartTime");
            if (StringUtil.isEmpty(adStartTimeStr)) {
                throw new IllegalArgumentException("广告开始时间参数不能为空");
            }
            try {
                adStartDate.setTime(Long.parseLong(adStartTimeStr));
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException("广告开始时间参数非法");
            }

            String adEndTimeStr = request.getParameter("adEndTime");
            if (StringUtil.isEmpty(adEndTimeStr)) {
                throw new IllegalArgumentException("广告结束时间参数不能为空");
            }
            try {
                adEndDate.setTime(Long.parseLong(adEndTimeStr));
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException("广告结束时间参数非法");
            }

            listPriority = "true".equals(request.getParameter("listPriority"));
            carousel = "true".equals(request.getParameter("carousel"));

            init = true;
            return this;
        }

        @Deprecated
        public String fillField(RequestParameterSet mParaSet) {
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

            init = true;
            return null;
        }

        public PublishAdEntity build() {
            if (!init) {
                throw new IllegalStateException(
                        "the builder of publish ad entity must be inited!");
            }
            return new PublishAdEntity(this);
        }

    }

}
