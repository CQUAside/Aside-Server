# ģ��˵��
===========
## StatusCodeBean���͵�״̬������bean˵��
-----------

#### StatusCodeBean
```java
StatusCodeBean {
	int statusCode;	//״̬��
	String msg;		//״̬���Ӧ����ϸ������Ϣ
}
```
������������������Щֻ��Ҫ֪�����ý���Ľӿڣ���������ֵʹ�á�

#### AdStatusCodeBean
```java
AdStatusCodeBean extends StatusCodeBean {
	AdStatusEnum adStatus;	//���״̬
	int clickCount;			//����������������
	int collectCount;		//����ע�ȣ����ղ���
	int userID;				//�ù���Ӧ���û�ID
}
```
����������������DBManagerģ��Ĳ�ѯ���ӿڣ��ýӿ���Ҫ����AdManagerģ�����˹�桢�¼ܹ�桢���ӹ�����������ע��ȡ�ع�档


