package com.round.aside.server.enumeration;

/**
 * 广告状态枚举，如未审核，审核未通过，审核通过且有效，审核通过已下架，审核通过已过期。
 * 
 * @author A Shuai
 * @date 2016-5-28
 * 
 */
public enum AdStatusEnum {

    /**
     * 未审核
     */
    UNREVIEW(0),

    /**
     * 审核未通过
     */
    NOTPASS(1),

    /**
     * 审核通过且处于有效期内
     */
    VALID(2),

    /**
     * 下架
     */
    OFFSHELF(3),

    /**
     * 过期
     */
    OVERDUE(4);

    //类型参数
    private final int type;

    private AdStatusEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    /**
     * 根据指定类型参数转换为对应的广告状态枚举值
     * 
     * @param type
     *            类型参数
     * @return
     */
    public static AdStatusEnum valueOf(int type) {
        for (AdStatusEnum mItem : values()) {
            if (mItem.getType() == type) {
                return mItem;
            }
        }
        throw new IllegalArgumentException("Illegal type parameter!");
    }

}
