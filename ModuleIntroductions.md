# ģ��˵��

## StatusCodeBean���͵�״̬������bean˵��

#### StatusCodeBean
������״̬������bean���������͵�״̬������bean���ǻ��ڴ˽�����չ�ġ����е���չ�ֶξ�ֻ��statusCode = 1000ʱ��ȡ����Ч��
```java
class StatusCodeBean {
	int statusCode;	//״̬��
	String msg;		//״̬���Ӧ����ϸ������Ϣ
}
```
������������������Щֻ��Ҫ֪�����ý���Ľӿڣ���������ֵʹ�á�

#### AdStatusCodeBean
�����˲��ֹ��״̬��Ϣ��״̬������bean��
```java
class AdStatusCodeBean extends StatusCodeBean {
	AdStatusEnum adStatus;	//���״̬
	int clickCount;			//����������������
	int collectCount;		//����ע�ȣ����ղ���
	int userID;				//�ù���Ӧ���û�ID
}
```
����������������������Щ���״̬��Ϣ�Ľӿڵ��ã�����DBManagerģ��Ĳ�ѯ���ӿڣ��ýӿ���Ҫ����AdManagerģ�����˹�桢�¼ܹ�桢���ӹ�����������ע��ȡ�ع�档

#### AuthCodeStatusCodeBean
������֤���ֶε�״̬������bean��
```java
class AuthCodeStatusCodeBean extends StatusCodeBean {
	String authCode;	//��֤��
}
```
�����������������ֻ�����֤��������֤�������һؽ׶Ρ�

#### EmailStatusCodeBean
�����û������ֶε�״̬������bean��
```java
class EmailStatusCodeBean extends StatusCodeBean {
	String userEmail;	//�û�����
}
```
����������������Ҫ��ѯ�û�����Ľӿ���������ֵʹ�á�

#### IncrementSumStatusCodeBean
��������������ֶε�״̬������bean��
```java
class IncrementSumStatusCodeBean extends StatusCodeBean {
	int sum;		//���յ�������
}
```
������������������Ҫ��������͵Ľ����������ֵ���������ӹ�����������ע��ȡ�ع�档

#### LoginUserBean
�˺ŵ�½�����״̬������bean��
```java
class LoginUserBean extends StatusCodeBean {
	int userID;		//�û�ID
	String token;	//��½�ɹ�������Token
}
```
�����������������˺Ź���ģ���е�ע��͵�½�ӿڣ���ֻ����ע��ɹ��͵�½�ɹ�����Ч��

#### UserEmailAuthStatusBean
�û�������֤״̬��״̬������bean
```java
class UserEmailAuthStatusBean extends StatusCodeBean {
	String userEmail;		//�û�����
	UserEmailStatusEnum userEmailStatus;	//�û�������֤״̬
}
```
����������������������֤�ӿڡ��һ�����ӿڵ���Ҫʹ���û�����Ľӿ���ʹ�á�


## Enum���͵�ö����˵��

#### AdStatusEnum
���״̬ö����
```java
{
	UNREVIEW,	//���δ���
	NOTPASS,	//���δͨ��
	VALID,		//���ͨ���Ҵ�����Ч����
	OFFSHELF,	//������ͨ�������¼�
	OVERDUE		//������ͨ�����ѹ���
}
```

#### UserEmailStatusEnum
�û�������֤״̬ö����
```java
{
	UNAUTH,		//�û�����δ��֤
	AUTHLESS,	//�û�������֤ʧ��
	AUTHED		//�û�������֤ͨ��
}
```


## Module˵��

#### IAccountManager
�˺Ź���ģ��
```java
interface IAccountManager extends IModule {
	
	//����ע���˺ŵĺϷ��ԣ����Ƿ�Ϸ����Ƿ��ظ�
	StatusCodeBean checkRegisteredAccountLegal(String mAccount);
	
	//ע��
	LoginUserBean registerAccount(String mAccount, String mPassword, String mPhone, String mAuthcode, RequestInfoBean mRequestInfoBean);
	
	//�����½
	LoginUserBean login(String mAccount, String mPassword, long period, RequestInfoBean mRequestInfoBean);
	
	//Token��½����Token��Ч���ӳ�Token��Ч�ڡ��ýӿ���ʱ���գ�δʵ��
	StatusCodeBean tokenLogin(int userId, String token);
	
	//Token��֤
	StatusCodeBean verifyToken(int userId, String token);
	
	//��ָ���û������䷢����֤�ʼ�������֤���û�����
	StatusCodeBean activateEmail(int mUserID);
	
	//������֤�����еĵڶ�������֤���͵��û������е���֤���Ƿ���ϵͳ�������֤��һ��
	StatusCodeBean validateActivationEmail(int mUserID, String mEmail, String mAuthCode);
	
	//��UserID�����һ������һ��ʼ�����Ϊ�һ����벽��ĵ�һ��
	StatusCodeBean retrievePassword(int mUserID);
	
	//��֤�һ������е���֤����ϵͳ�������֤���Ƿ�һ�¡���Ϊ�һ����벽��ĵڶ���
	StatusCodeBean validationRetrieverPassword(int mUserID, String mEmail, String mAuthCode);
}
```

#### IAdvertisementManager
������ģ��
```java
interface IAdvertisementManager extends IModule {
}
```

