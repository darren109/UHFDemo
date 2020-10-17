package com.sunmi.rfid;

interface IFirmwareUpdateCall {
    /**
     * 升级成功
     */
    void onSuccess();
    /**
     * 升级进度
     *
     * @param progress 百分比
     */
    void onProgress(in int progress);
    /**
     * 升级失败
     *
     * @param code -1: update failed
     *             -2: file not exist
     *             -3: file suffix not .bin or binFile is null
     * @param msg  information
     */
    void onFailed(in int code, in String msg);
}
