# 模块说明

## StatusCodeBean类型的状态码数据bean说明

#### StatusCodeBean
基本的状态码数据bean，其他类型的状态码数据bean均是基于此进行扩展的。所有的扩展字段均只有statusCode = 1000时读取才有效。
```java
class StatusCodeBean {
	int statusCode;	//状态码
	String msg;		//状态码对应的详细描述信息
}
```
适用条件：适用于那些只需要知道调用结果的接口，用作返回值使用。

#### AdStatusCodeBean
包含了部分广告状态信息的状态码数据bean。
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
包含认证码字段的状态码数据bean。
```java
class AuthCodeStatusCodeBean extends StatusCodeBean {
	String authCode;	//认证码
}
```
适用条件：适用于手机号认证、邮箱认证、密码找回阶段。

#### EmailStatusCodeBean
包含用户邮箱字段的状态码数据bean。
```java
class EmailStatusCodeBean extends StatusCodeBean {
	String userEmail;	//用户邮箱
}
```
适用条件：适用需要查询用户邮箱的接口用作返回值使用。

#### IncrementSumStatusCodeBean
包含增量结果和字段的状态码数据bean。
```java
class IncrementSumStatusCodeBean extends StatusCodeBean {
	int sum;		//最终的增量和
}
```
适用条件：适用于需要增量结果和的结果用作返回值，包括增加广告浏览量、关注或取关广告。

#### LoginUserBean
账号登陆结果的状态码数据bean。
```java
class LoginUserBean extends StatusCodeBean {
	int userID;		//用户ID
	String token;	//登陆成功的令牌Token
}
```
适用条件：适用于账号管理模块中的注册和登陆接口，但只有在注册成功和登陆成功才有效。

#### UserEmailAuthStatusBean
用户邮箱验证状态的状态码数据bean
```java
class UserEmailAuthStatusBean extends StatusCodeBean {
	String userEmail;		//用户邮箱
	UserEmailStatusEnum userEmailStatus;	//用户邮箱验证状态
}
```
适用条件：适用于邮箱验证接口、找回密码接口等需要使用用户邮箱的接口中使用。


## Enum类型的枚举类说明

#### AdStatusEnum
广告状态枚举类
```java
{
	UNREVIEW,	//广告未审核
	NOTPASS,	//审核未通过
	VALID,		//审核通过且处于有效期内
	OFFSHELF,	//广告审核通过但被下架
	OVERDUE		//广告审核通过但已过期
}
```

#### UserEmailStatusEnum
用户邮箱验证状态枚举类
```java
{
	UNAUTH,		//用户邮箱未认证
	AUTHLESS,	//用户邮箱认证失败
	AUTHED		//用户邮箱认证通过
}
```


## Module说明

#### IAccountManager
账号管理模块
```java
interface IAccountManager extends IModule {
	
	//检查待注册账号的合法性，即是否合法或是否重复
	StatusCodeBean checkRegisteredAccountLegal(String mAccount);
	
	//注册
	LoginUserBean registerAccount(String mAccount, String mPassword, String mPhone, String mAuthcode, RequestInfoBean mRequestInfoBean);
	
	//常规登陆
	LoginUserBean login(String mAccount, String mPassword, long period, RequestInfoBean mRequestInfoBean);
	
	//Token登陆，若Token有效则延长Token有效期。该接口暂时留空，未实现
	StatusCodeBean tokenLogin(int userId, String token);
	
	//Token验证
	StatusCodeBean verifyToken(int userId, String token);
	
	//向指定用户的邮箱发送验证邮件，以验证该用户邮箱
	StatusCodeBean activateEmail(int mUserID);
	
	//邮箱验证步骤中的第二步，验证发送到用户邮箱中的认证码是否与系统保存的认证码一致
	StatusCodeBean validateActivationEmail(int mUserID, String mEmail, String mAuthCode);
	
	//向UserID发送找回密码找回邮件。此为找回密码步骤的第一步
	StatusCodeBean retrievePassword(int mUserID);
	
	//验证找回密码中的认证码与系统保存的认证码是否一致。此为找回密码步骤的第二步
	StatusCodeBean validationRetrieverPassword(int mUserID, String mEmail, String mAuthCode);
}
```

#### IAdvertisementManager
广告管理模块
```java
interface IAdvertisementManager extends IModule {
}
```

