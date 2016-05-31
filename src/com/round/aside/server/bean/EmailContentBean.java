package com.round.aside.server.bean;

/**
 * 用于发送邮件的邮件内容数据bean
 * 
 * @author A Shuai
 * @date 2016-5-31
 * 
 */
public final class EmailContentBean {

    private final String subject;
    private final String text;
    private final String content;
    private final String contentType;

    public EmailContentBean(Builder mBuilder) {
        this.subject = mBuilder.subject;
        this.text = mBuilder.text;
        this.content = mBuilder.content;
        this.contentType = mBuilder.contentType;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public String getContent() {
        return content;
    }

    public String getContentType() {
        return contentType;
    }


    /**
     * 建造者
     * 
     * @author A Shuai
     * @date 2016-5-31
     * 
     */
    public static class Builder {

        private String subject;
        private String text;
        private String content;
        private String contentType;

        public Builder() {
        }

        public Builder setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public EmailContentBean build() {
            return new EmailContentBean(this);
        }

    }

}
