package com.insertdemo;

import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText calllogEditText;
    private EditText contactsEditText;
    private EditText mmsEditText;
    private EditText cmmsEditText;
    private int mWhichInsert;

    List<Tb_calllogs> mTbCalllogList;
    List<Tb_contacts> mTbContactsList;
    List<Tb_sms> mTb_sms;

    private String defaultSmsPkg;
    private String mySmsPkg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mWhichInsert = 0;
    }

    private void initViews() {
        calllogEditText = findViewById(R.id.calllog_number);
        contactsEditText = findViewById(R.id.contacts_number);
        mmsEditText = findViewById(R.id.mms_number);
        cmmsEditText = findViewById(R.id.cmms_number);
    }

    public void insert(View view) throws RemoteException, OperationApplicationException {
        switch (view.getId()) {
            case R.id.insert_calllog:
                mTbCalllogList = new LinkedList<>();
                mWhichInsert = 1;
                addTbCalllogs();
                break;
            case R.id.insert_contacts:
                mTbContactsList = new LinkedList<>();
                mWhichInsert = 2;
                addTbContacts();
                break;
            case R.id.insert_mms:
                mTb_sms = new LinkedList<>();
                mWhichInsert = 3;
                addTbMms();
                break;
            case R.id.insert_cmms:
                mWhichInsert = 4;
                break;
        }
    }

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

    private boolean getSmsIsRead() {
        Random rand = new Random();
        int i = rand.nextInt(2);
        return i == 1 ? true : false;
    }

    private int getSmsType() {
        Random rand = new Random();
        int i = rand.nextInt(7);
        return i;
    }

    /**
     * 生成日期
     *
     * @return
     */
    private Long getDate() {
        return System.currentTimeMillis();
    }

    /**
     * 生成随机号码
     */
    private String getNumber() {
        StringBuilder number = new StringBuilder("136");
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            number.append(random.nextInt(9));
        }
        return number.toString().toLowerCase();
    }

    /**
     * 生成随机汉子
     *
     * @return
     */
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

    private int getCallType() {
        Random rand = new Random();
        int i = rand.nextInt(4);
        if (i == 0) {
            i = 1;
        }
        return i;
    }

    private int getCallDuration() {
        Random rand = new Random();
        int i = rand.nextInt(10000);
        return i;
    }


    private void showProgressDialog() {
        final ProgressDialog insertProgress = new ProgressDialog(this);
        insertProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        insertProgress.setCancelable(true);
        insertProgress.setCanceledOnTouchOutside(false);
        insertProgress.setIcon(R.mipmap.ic_launcher);
        insertProgress.setTitle("提示");
        insertProgress.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                switch (mWhichInsert) {
                    case 1:
                        Toast.makeText(MainActivity.this, "已经成功插入" + mTbCalllogList.size()
                                + "条通话记录", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "已经成功插入" + mTbContactsList.size()
                                + "条联系人", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "已经成功插入" + mTb_sms.size()
                                + "条短信", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this, "已经成功插入" + mTbCalllogList.size()
                                + "条彩信", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
        insertProgress.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return false;
            }
        });
        insertProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        insertProgress.setMessage("正在插入数据");
        insertProgress.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    switch (mWhichInsert) {
                        case 1:
                            BatchAddCallLogs();
                            break;
                        case 2:
                            BatchAddContact();
                            break;
                        case 3:
                            insertSms();
                            break;
                        case 4:
                            break;
                        default:
                            break;
                    }
                    insertProgress.cancel();
                    insertProgress.dismiss();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

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

    /**
     * 批量添加通讯录
     *
     * @throws OperationApplicationException
     * @throws RemoteException
     */
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

    @Override
    protected void onResume() {
        super.onResume();
        defaultSmsPkg = Telephony.Sms.getDefaultSmsPackage(this);
        mySmsPkg = this.getPackageName();
        if (!defaultSmsPkg.equals(mySmsPkg)) {
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, mySmsPkg);
            startActivity(intent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        recoveryMms();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recoveryMms();
    }

    @Override
    protected void onPause() {
        super.onPause();
        recoveryMms();
    }

    private void recoveryMms() {
        String myPackageName = getPackageName();
        if (Telephony.Sms.getDefaultSmsPackage(this)
                .equals(myPackageName)) {
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            //这里的defaultSmsApp是前面保存的
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, defaultSmsPkg);
            startActivity(intent);
        }
    }
}