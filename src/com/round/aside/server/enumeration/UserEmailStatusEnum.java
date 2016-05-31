package com.round.aside.server.enumeration;

/**
 * 用户注册状态枚举，包括未认证、认证失败和认证通过三种
 * 
 * @author A Shuai
 * @date 2016-5-29
 *
 */
public enum UserEmailStatusEnum {

    /**
     * 未认证
     */
    UNAUTH(0),

    /**
     * 认证失败
     */
    AUTHLESS(1),

    /**
     * 认证通过
     */
    AUTHED(2);

    //类型参数
    private final int type;

    private UserEmailStatusEnum(int type) {
        this.type = type;
    }

    public final int getType() {
        return type;
    }

    /**
     * 根据指定类型参数转换为对应的用户状态枚举值
     * 
     * @param type
     *            类型参数
     * @return 参数指定类型的用户注册状态枚举值
     */
    public static UserEmailStatusEnum valueOf(int type) {
        for (UserEmailStatusEnum mItem : values()) {
            if (mItem.getType() == type) {
                return mItem;
            }
        }
        throw new IllegalArgumentException("Illegal type parameter!");
    }

}
