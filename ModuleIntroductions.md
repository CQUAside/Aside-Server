# ģ��˵��

## StatusCodeBean���͵�״̬������bean˵��

#### StatusCodeBean
������״̬������bean
```java
class StatusCodeBean {
	int statusCode;	//״̬��
	String msg;		//״̬���Ӧ����ϸ������Ϣ
}
```
������������������Щֻ��Ҫ֪�����ý���Ľӿڣ���������ֵʹ�á�

#### AdStatusCodeBean
�����˲��ֹ��״̬��Ϣ��״̬������bean
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
������֤���ֶε�״̬������bean
```java
class AuthCodeStatusCodeBean extends StatusCodeBean {
	String authCode;	//��֤��
}
```
�����������������ֻ�����֤��������֤�������һؽ׶Ρ�

#### EmailStatusCodeBean
�����û������ֶε�״̬������bean
```java
class EmailStatusCodeBean extends StatusCodeBean {
	String userEmail;	//�û�����
}
```
����������������Ҫ��ѯ�û�����Ľӿ���������ֵʹ�á�


