package com.sunmi.rfid;

import com.sunmi.rfid.entity.DataParameter;

/**
 * Reader Callback
 */
interface IReaderCall {
    /**
     * Handle reader/option result
     */
    void onSuccess(in byte cmd, in DataParameter params);
    /**
     * Handle New/Update Tag
     */
    void onTag(in byte cmd, in byte state, in DataParameter tag);
    /**
     * Handle Failed result
     */
    void onFailed(in byte cmd, in byte errorCode, in String msg);
}
