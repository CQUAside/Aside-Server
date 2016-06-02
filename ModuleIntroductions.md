# 模块说明

## StatusCodeBean类型的状态码数据bean说明

#### StatusCodeBean
基本的状态码数据bean
```java
class StatusCodeBean {
	int statusCode;	//状态码
	String msg;		//状态码对应的详细描述信息
}
```
适用条件：适用于那些只需要知道调用结果的接口，用作返回值使用。

#### AdStatusCodeBean
包含了部分广告状态信息的状态码数据bean
```java
class AdStatusCodeBean extends StatusCodeBean {
	AdStatusEnum adStatus;	//广告状态
	int clickCount;			//广告点击数，即浏览量
	int collectCount;		//广告关注度，即收藏量
	int userID;				//该广告对应的用户ID
}
```
适用条件：适用于需求这些广告状态信息的接口调用，包括DBManager模块的查询广告接口，该接口主要用于AdManager模块的审核广告、下架广告、增加广告浏览量、关注或取关广告。

#### AuthCodeStatusCodeBean
包含认证码字段的状态码数据bean
```java
class AuthCodeStatusCodeBean extends StatusCodeBean {
	String authCode;	//认证码
}
```
适用条件：适用于手机号认证、邮箱认证、密码找回阶段。

#### EmailStatusCodeBean
包含用户邮箱字段的状态码数据bean
```java
class EmailStatusCodeBean extends StatusCodeBean {
	String userEmail;	//用户邮箱
}
```
适用条件：适用需要查询用户邮箱的接口用作返回值使用。


