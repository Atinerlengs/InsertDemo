[toc]

# 批量插入


## 批量插入通话记录
下面是添加通话数据的代码，我们自己去构造Tb_calllogs数据，里面可以添加通话记录数据字段，由于是个简单的demo，所以只加了number、callType、date、callDuration。
 
```
    /**
     * 添加通话数据
     *
     * @throws RemoteException
     * @throws OperationApplicationException
     */
    public void addTbCalllogs() throws RemoteException, OperationApplicationException {
        int calllogNumber = 0;
        if (calllogEditText.getText() != null) {
            calllogNumber = Integer.parseInt(calllogEditText.getText().toString());
        }

        if (calllogNumber == 0) {
            Toast.makeText(this, "请输入正确的条数", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < calllogNumber; i++) {
            Tb_calllogs tb_calllogs = new Tb_calllogs(getNumber(),
                    getCallType(), getDate(), getCallDuration());
            mTbCalllogList.add(tb_calllogs);
        }
        showProgressDialog();
    }
```

生成随机号码代码如下，默认直接136开头，如果有需求可以替换成其它号码。

```
    private String getNumber() {
        StringBuilder number = new StringBuilder("136");
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            number.append(random.nextInt(9));
        }
        return number.toString().toLowerCase();
    }
```

批量添加通话记录如下，通话applyBatch()方法去批量添加

```
    public void BatchAddCallLogs()
            throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ContentValues values = new ContentValues();

        for (Tb_calllogs calllog : mTbCalllogList) {
            values.clear();
            values.put(CallLog.Calls.NUMBER, calllog.getmNumber());
            values.put(CallLog.Calls.TYPE, calllog.getmCallLogType());
            values.put(CallLog.Calls.DATE, calllog.getmCallLogDate());
            values.put(CallLog.Calls.DURATION, calllog.getmCallLogDuration());
            values.put(CallLog.Calls.NEW, "0");
            ops.add(ContentProviderOperation
                    .newInsert(CallLog.Calls.CONTENT_URI).withValues(values)
                    .withYieldAllowed(true).build());
        }
        if (ops != null) {
            getContentResolver().applyBatch(CallLog.AUTHORITY, ops);
        }
    }
```

## 批量插入联系人
先构造联系人数据Tb_contacts,目前只加了name和number，代码如下
```
    public void addTbContacts() throws RemoteException, OperationApplicationException {
        int contactsNumber = 0;
        if (contactsEditText.getText() != null) {
            contactsNumber = Integer.parseInt(contactsEditText.getText().toString());
        }
        if (contactsNumber == 0) {
            Toast.makeText(this, "请输入正确的条数", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < contactsNumber; i++) {
            Tb_contacts tb_contacts = new Tb_contacts(getName(), getNumber());
            mTbContactsList.add(tb_contacts);
        }
        showProgressDialog();
    }
```

生成随机汉子，随便加了两个汉子，代码如下：

```
    private String getName() {
        StringBuilder stringBuilder = new StringBuilder("");
        int hightPos;
        int lowPos;

        Random random = new Random();

        for (int i = 0; i < 2; i++) {
            hightPos = (176 + Math.abs(random.nextInt(39)));
            lowPos = (161 + Math.abs(random.nextInt(93)));

            byte[] b = new byte[2];
            b[0] = (Integer.valueOf(hightPos)).byteValue();
            b[1] = (Integer.valueOf(lowPos)).byteValue();

            try {
                stringBuilder.append(new String(b, "GBK"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString().toLowerCase();
    }
```
批量添加到联系人，具体代码如下：
```
    public void BatchAddContact()
            throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = 0;
        for (Tb_contacts contact : mTbContactsList) {
            rawContactInsertIndex = ops.size();

            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .withYieldAllowed(true).build());

            // 添加姓名
            ops.add(ContentProviderOperation
                    .newInsert(
                            android.provider.ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName())
                    .withYieldAllowed(true).build());
            // 添加号码
            ops.add(ContentProviderOperation
                    .newInsert(
                            android.provider.ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getNumber())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, "").withYieldAllowed(true).build());
        }
        if (ops != null) {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        }
    }
```

## 批量插入短信
同样的先构造Tb_mms数据，添加了number,smsType,smsIsRead等字段。添加短信数据如下：
```
    private void addTbMms() throws RemoteException, OperationApplicationException {
        int mmsNumber = 0;
        if (mmsEditText.getText() != null) {
            mmsNumber = Integer.parseInt(mmsEditText.getText().toString());
        }
        if (mmsNumber == 0) {
            Toast.makeText(this, "请输入正确的条数", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < mmsNumber; i++) {
            Tb_sms tb_mms = new Tb_sms(getNumber(), getSmsType(), getSmsIsRead());
            mTb_sms.add(tb_mms);
        }
        showProgressDialog();
    }
```
插入短信数据代码如下：
```
    private void insertSms() {
        for (Tb_sms sms : mTb_sms) {
            ContentValues values = new ContentValues();
            values.put(Telephony.Sms.ADDRESS, sms.getNumber());
            values.put(Telephony.Sms.DATE, System.currentTimeMillis());
            long dateSent = System.currentTimeMillis() - 5000;
            values.put(Telephony.Sms.DATE_SENT, dateSent);
            values.put(Telephony.Sms.READ, sms.isRead());
            //values.put(Telephony.Sms.SEEN, false);
            values.put(Telephony.Sms.TYPE, sms.getType());
            //values.put(Telephony.Sms.STATUS, Telephony.Sms.STATUS_COMPLETE);
            values.put(Telephony.Sms.BODY, "您尾号为9386的招行储蓄卡收到转账1,000,000人民币");
            values.put(Telephony.Sms.TYPE, Telephony.Sms.MESSAGE_TYPE_INBOX);
            getContentResolver().insert(Telephony.Sms.CONTENT_URI, values);
        }
    }
```

> PS: Android4.4之后，插入短信数据需要修改几个地方，在AndroidManifest.xml里面的Activity添加如下代码：

```
            <intent-filter>
                <action android:name="android.intent.action.SEND" />
                <action android:name="android.intent.action.SENDTO" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <data android:scheme="sms" />
                <data android:scheme="smsto" />
                <data android:scheme="mms" />
                <data android:scheme="mmsto" />
            </intent-filter>
```
在MainActivity.java里面添加如下代码去获取默认短信应用：
```
        defaultSmsPkg = Telephony.Sms.getDefaultSmsPackage(this);
        mySmsPkg = this.getPackageName();
        if (!defaultSmsPkg.equals(mySmsPkg)) {
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, mySmsPkg);
            startActivity(intent);
        }
```
结束的时候需要在把默认的短信数据替换代码如下：
```
        String myPackageName = getPackageName();
        if (Telephony.Sms.getDefaultSmsPackage(this)
                .equals(myPackageName)) {
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            //这里的defaultSmsApp是前面保存的
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, defaultSmsPkg);
            startActivity(intent);
        }
```
