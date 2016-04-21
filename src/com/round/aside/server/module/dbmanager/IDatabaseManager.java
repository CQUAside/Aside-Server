package com.round.aside.server.module.dbmanager;

import java.util.LinkedList;

import com.round.aside.server.module.IModule;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 数据库管理模块超级接口
 * 
 * @author A Shuai
 * @date 2016-3-28
 * 
 */
public interface IDatabaseManager extends IModule {

    /**
     * 新插入一条用户信息记录，但只插入用户ID，账号和密码三个字段，其余字段使用Update方法进行更新式填充
     * 
     * @param mUserID
     *            用户ID，Unique值，范围为1-99999999
     * @param mAccount
     *            用户账号，Unique值，不能为空
     * @param mPassword
     *            用户密码，不能为空
     * @param NickName
     * 			      用户昵称，可为空
     * @param Email
     *            用户Email，可为空
     * @param PhoneNum
     *            用户电话，可为空
     * @return 操作结果状态值，合法的结果值只有六种，分别为{@link #S1000}成功，{@link #F8001}userid字段冲突，
     *         {@link #F8002}account字段冲突，{@link #EX2012}Connection获取失败异常，
     *         {@link #EX2013}SQL插入执行异常 以及{@link #ER5001}调用参数非法，其他返回值均为非法值。
     */
    int insertUser(int mUserID, String mAccount, String mPassword);
    /**
     * 
    * @param mUserID
     *            用户ID，Unique值，范围为1-99999999
     * @param mAccount
     *            用户账号，Unique值，不能为空
     * @param mPassword
     *            用户密码，不能为空
     * @param NickName
     * 			      用户昵称，可为空
     * @param Email
     *            用户Email，可为空
     * @param PhoneNum
     *            用户电话，可为空
     * @return 操作结果状态值，合法的结果值只有六种，分别为{@link #S1000}成功，{@link #F8001}userid字段冲突，
     *         {@link #F8002}account字段冲突，{@link #EX2012}Connection获取失败异常，
     *         {@link #EX2013}SQL插入执行异常 以及{@link #ER5001}调用参数非法，其他返回值均为非法值。
     */
    int insertUser(int muserID,String mAccount,String mPassword,String NickName,String Email,String PhoneNum);
    /**
     * 
     * @param mAccount
     *     用户账号，Unique值，不能为空
     * @param mPassword
     *     用户密码，不能为空
     * @return 操作结果状态值和用户ID，合法的结果值只有六种，分别为{@link #S1000}和用户ID成功，其他情况ID为0，{@link #EX2012}Connection获取失败异常，
     *   {@link #EX2016}SQL查询执行异常 以及{@link #ER5001}调用参数非法，其他返回值均为非法值。
     */
    LinkedList<Integer> selectUser(String mAccount ,String mPassword);
    /**
     * 
     * @param mUserID
     *         用户ID
     * @param registerID
     *         用户验证邮箱生成的验证码
     * @return 操作结果状态值 ，有四种{@link #S1000}成功，{@link #EX2012}Connection获取失败异常，
     *         {@link #EX2014}SQL更新执行异常，{@link #ER5001}调用参数非法，其他返回值均为非法值
     */
    int updateUserRegister(int mUserID,String registerID);
    /**
     * 
     * @param mUserID
     *          用户ID
     * @param registerID
     *          用户验证邮箱生成的验证码
     * @return
     */
    int selectUserRegister(int mUserID,String registerID);
    /**
     * 
     * @param mUserID
     *        用户ID
     * @return
     */
    int selectUserStatus(int mUserID);
    /**
     * 
     * @param mUserID
     *         用户ID
     * @return
     */
    int updateUserStatus(int mUserID);
    /**
     * 
     * @param mUserID
     *         用户ID
     * @return
     */
    String selectUserEmail(int mUserID);
}
