package com.sunmi.rfid.constant;

/**
 * Regarding UHF return code and description
 */
public class CMD {
//    public final static byte RESET = 0x70;
//    public final static byte SET_UART_BAUDRATE = 0x71;
//    public final static byte GET_FIRMWARE_VERSION = 0x72;
//    public final static byte SET_READER_ADDRESS = 0x73;
//    public final static byte SET_WORK_ANTENNA = 0x74;
//    public final static byte GET_WORK_ANTENNA = 0x75;
//    public final static byte SET_OUTPUT_POWER = 0x76;
//    public final static byte GET_OUTPUT_POWER = 0x77;
//    public final static byte SET_FREQUENCY_REGION = 0x78;
//    public final static byte GET_FREQUENCY_REGION = 0x79;
//    public final static byte SET_BEEPER_MODE = 0x7A;
//    public final static byte GET_READER_TEMPERATURE = 0x7B;
//    public final static byte READ_GPIO_VALUE = 0x60;
//    public final static byte WRITE_GPIO_VALUE = 0x61;
//    public final static byte SET_ANT_CONNECTION_DETECTOR = 0x62;
//    public final static byte GET_ANT_CONNECTION_DETECTOR = 0x63;
//    public final static byte SET_TEMPORARY_OUTPUT_POWER = 0x66;
//    public final static byte SET_READER_IDENTIFIER = 0x67;
//    public final static byte GET_READER_IDENTIFIER = 0x68;
//    public final static byte SET_RF_LINK_PROFILE = 0x69;
//    public final static byte GET_RF_LINK_PROFILE = 0x6A;
//    public final static byte GET_RF_PORT_RETURN_LOSS = 0x7E;
    public final static byte INVENTORY = (byte) 0x80;
    public final static byte READ_TAG = (byte) 0x81;
    public final static byte WRITE_TAG = (byte) 0x82;
    public final static byte LOCK_TAG = (byte) 0x83;
    public final static byte KILL_TAG = (byte) 0x84;
    public final static byte SET_ACCESS_EPC_MATCH = (byte) 0x85;
    public final static byte GET_ACCESS_EPC_MATCH = (byte) 0x86;
    public final static byte REAL_TIME_INVENTORY = (byte) 0x89;
    public final static byte FAST_SWITCH_ANT_INVENTORY = (byte) 0x8A;
    public final static byte CUSTOMIZED_SESSION_TARGET_INVENTORY = (byte) 0x8B;
    public final static byte SET_IMPINJ_FAST_TID = (byte) 0x8C;
    public final static byte SET_AND_SAVE_IMPINJ_FAST_TID = (byte) 0x8D;
    public final static byte GET_IMPINJ_FAST_TID = (byte) 0x8E;
    public final static byte ISO18000_6B_INVENTORY = (byte) 0xB0;
    public final static byte ISO18000_6B_READ_TAG = (byte) 0xB1;
    public final static byte ISO18000_6B_WRITE_TAG = (byte) 0xB2;
    public final static byte ISO18000_6B_LOCK_TAG = (byte) 0xB3;
    public final static byte ISO18000_6B_QUERY_LOCK_TAG = (byte) 0xB4;
    public final static byte GET_INVENTORY_BUFFER = (byte) 0x90;
    public final static byte GET_AND_RESET_INVENTORY_BUFFER = (byte) 0x91;
    public final static byte GET_INVENTORY_BUFFER_TAG_COUNT = (byte) 0x92;
    public final static byte RESET_INVENTORY_BUFFER = (byte) 0x93;
//    public final static byte OPERATE_TAG_MASK = (byte) 0x98;

//    public final static byte SET_READER_STATUS = (byte) 0xA0;
//    public final static byte QUERY_READER_STATUS = (byte) 0xA1;

//    public final static byte GET_READER_VOL = 0x01;
//    public final static byte GET_READER_ELEC = 0x02;
//    public final static byte GET_READER_LOWELEC = 0x12;
//    public final static byte GET_READER_CHARGING = 0x03;
//    public final static byte GET_READER_CHARGING_CURRENT = 0x04;
//    public final static byte GET_READER_BOOT = 0x09;

//    public final static byte GET_READER_VERSION = 0x32;
//    public final static byte GET_READER_SN = 0x33;
//    public final static byte GET_BEEP_MODE = 0x34;
}
