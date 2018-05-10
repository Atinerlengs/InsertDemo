package com.insertdemo;

/**
 * Created by liqiang on 18-4-17.
 */
public class Tb_calllogs {
    private String mNumber; // 号码
    private int mCallLogType; // 通话记录的状态 1:来电 2:去电 3:未接
    private Long mCallLogDate; // 通话记录日期
    private int mCallLogDuration; // 通话记录时长

    public Tb_calllogs() {
        super();
    }
    public Tb_calllogs(String mNumber, int mCallLogType,
                       Long mCallLogDate, int mCallLogDuration) {
        super();
        this.mNumber = mNumber;
        this.mCallLogType = mCallLogType;
        this.mCallLogDate = mCallLogDate;
        this.mCallLogDuration = mCallLogDuration;
    }

    public String getmNumber() {
        return mNumber;
    }

    public int getmCallLogType() {
        return mCallLogType;
    }

    public Long getmCallLogDate() {
        return mCallLogDate;
    }

    public int getmCallLogDuration() {
        return mCallLogDuration;
    }


    @Override
    public String toString() {
        return "Tb_calllogs [mNumber=" + mNumber + ", mCallLogType=" + mCallLogType + ", mCallLogDate="
                + mCallLogDate + ", mCallLogDuration=" + mCallLogDuration + "]";
    }
}
