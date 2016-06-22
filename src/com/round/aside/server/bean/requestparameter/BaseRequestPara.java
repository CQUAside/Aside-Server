package com.round.aside.server.bean.requestparameter;

import javax.servlet.http.HttpServletRequest;

import com.round.aside.server.datastruct.RequestParameterSet;
import com.round.aside.server.util.StringUtil;

/**
 * HttpServlet的基础请求参数集类<br>
 * 此类为标记类。<br>
 * 善用有限制通配符。
 * 
 * @author A Shuai
 * @date 2016-6-21
 * 
 */
public abstract class BaseRequestPara {

    /**
     * 基础RequestPara请求参数集类的建造者<br>
     * 模板模式<br>
     * 将公共方法进行抽象，然后将需要延迟到子类完成的方法提供给子类覆写。
     * <p>
     * 1，先调用{@link #fillFieldKey()}填充要读取的字段对应的Key；<br>
     * 2，调用{@link #readParameterFromRequest(HttpServletRequest)}方法读取客户端发送的参数；<br>
     * 3，调用{@link #fillField()}填充真实的字段域；<br>
     * 4，最后调用{@link #build()}方法得到客户端发送的参数集。
     * 
     * @author A Shuai
     * @date 2016-6-22
     * 
     * @param <P>
     */
    public static abstract class Builder<P extends BaseRequestPara> {

        private boolean init;

        private final RequestParameterSet mParaSet;

        public Builder() {
            init = false;
            mParaSet = new RequestParameterSet();
        }

        /**
         * 填充待读取的字段域的Key<br>
         * 子类填充待读取的扩展字段域的Key时，应覆写{@link #onFillFieldKey(RequestParameterSet)}方法。
         * 
         * @param mParaSet
         *            参数集
         */
        public final void fillFieldKey() {
            mParaSet.clear();
            onFillFieldKey(mParaSet);
        }

        /**
         * 子类可覆写该方法填充扩展字段域的Key<br>
         * 子类覆写时请务必向上调用父类方法，保证父类正确填充。
         * 
         * @param mParaSet
         *            参数集
         */
        protected void onFillFieldKey(RequestParameterSet mParaSet) {

        }

        /**
         * 从HttpServlet Request中读取字段对应的值
         * 
         * @param request
         *            客户端发送给服务器的请求
         * @return 成功返回null，失败返回错误信息
         */
        public final String readParameterFromRequest(HttpServletRequest request) {
            String failField = mParaSet.readParameter(request);
            if (StringUtil.isEmpty(failField)) {
                return null;
            } else {
                return failField + " parameter isn't set";
            }
        }

        /**
         * 填充字段，供外部类使用时调用。<br>
         * 不可覆写。子类填充扩展字段时应覆写{@link #checkAfterFillField()}方法
         * 
         * @param mParaSet
         *            参数集
         * @return 成功返回null，失败返回错误信息
         */
        public final String fillField() {
            String error = onFillField(mParaSet);
            if (!StringUtil.isEmpty(error)) {
                return error;
            }

            error = checkAfterFillField();
            if (!StringUtil.isEmpty(error)) {
                return error;
            }

            init = true;
            return null;
        }

        /**
         * 子类覆写该方法填充自定义的扩展字段。<br>
         * 注意子类覆写时，请务必向上调用父类的填充方法，保证父类的字段正确填充
         * 
         * @param mParaSet
         *            参数集
         * @return 成功返回null，失败返回错误信息
         */
        protected String onFillField(RequestParameterSet mParaSet) {

            return null;
        }

        /**
         * 字段填充完毕后进行一次检查。<br>
         * 主要检查填充后的字段的状态的冲突，一般是客户端错误。
         * 
         * @return 成功返回null，失败返回错误信息
         */
        protected String checkAfterFillField() {

            return null;
        }

        /**
         * 获取RequestPara实例对象。<br>
         * 供外部使用者获取实例
         * 
         * @return RequestPara实例对象
         */
        public final P build() {
            checkBeforeBuild();
            return buildInstance();
        }

        /**
         * 子类可自行覆写该方法完成在构建之前的必要检查，检查到异常后抛出{@link IllegalStateException}非法状态异常。<br>
         * 一般是运行时异常或代码错误，用于开发阶段的排错。
         * 
         * @throws IllegalStateException
         */
        protected void checkBeforeBuild() throws IllegalStateException {
            if (!init) {
                throw new IllegalStateException("the builder must be inited!");
            }
        }

        /**
         * 构造对应的RequestPara实例对象<br>
         * 子类必须完成该方法的覆写。
         * 
         * @return 构造的RequestPara对象
         */
        protected abstract P buildInstance();

    }

}
