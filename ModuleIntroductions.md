# 模块说明
===========
## StatusCodeBean类型的状态码数据bean说明
-----------

#### StatusCodeBean
```java
StatusCodeBean {
	int statusCode;	//状态码
	String msg;		//状态码对应的详细描述信息
}
```
适用条件：适用于那些只需要知道调用结果的接口，用作返回值使用。

#### AdStatusCodeBean
```java
AdStatusCodeBean extends StatusCodeBean {
	AdStatusEnum adStatus;	//广告状态
	int clickCount;			//广告点击数，即浏览量
	int collectCount;		//广告关注度，即收藏量
	int userID;				//该广告对应的用户ID
}
```
适用条件：适用于DBManager模块的查询广告接口，该接口主要用于AdManager模块的审核广告、下架广告、增加广告浏览量、关注或取关广告。


