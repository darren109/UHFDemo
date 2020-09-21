package com.sunmi.rfid.constant;

public final class ParamCts {

    public final static String BROADCAST_UN_FOUND_READER = "com.sunmi.rfid.unFoundReader";// 未找到相关设备
    public final static String BROADCAST_ON_LOST_CONNECT = "com.sunmi.rfid.onLostConnect";// 连接断开
//    public final static String BROADCAST_READER_BOOT = "com.sunmi.rfid.readerBoot";// 复位启动完成
//    public final static String BROADCAST_SN = "com.sunmi.rfid.sn";//sn
//    public final static String BROADCAST_FIRMWARE_VERSION = "com.sunmi.rfid.firmwareVersion";// 固件版本
//    public final static String BROADCAST_BATTERY_VOLTAGE = "com.sunmi.rfid.batteryVoltage";// 电池电压
//    public final static String BROADCAST_BATTERY_REMAINING_PERCENTAGE = "com.sunmi.rfid.batteryRemainingPercentage";// 电池电量
    public final static String BROADCAST_BATTER_LOW_ELEC = "com.sunmi.rfid.batteryLowElec";// 电池低电量
//    public final static String BROADCAST_BATTER_CHARGING = "com.sunmi.rfid.batteryCharging";// 电池充电状态

    public final static byte SUCCESS = 0x10;
    public final static byte FAIL = 0x11;

    public final static byte FOUND_TAG = 0x01;
    public final static byte UPDATE_TAG = 0x02;

    public final static byte UN_CHECK_READER = 0x03;

    // parameter
    public final static String ANT_ID = "ant_id";
    public final static String COUNT = "tag_count";
    public final static String READ_RATE = "read_rate";
    public final static String DATA_COUNT = "data_count";
    public final static String START_TIME = "start_time";
    public final static String END_TIME = "end_time";

    // tag
    public final static String TAG = "tag";
    public final static String TAG_UID = "tag_uid";
    public final static String TAG_PC = "tag_pc";
    public final static String TAG_EPC = "tag_epc";
    public final static String TAG_CRC = "tag_crc";
    public final static String TAG_RSSI = "tag_rssi";
    public final static String TAG_READ_COUNT = "tag_read_count";
    public final static String TAG_FREQ = "tag_freq";
    public final static String TAG_TIME = "tag_time";
    public final static String TAG_DATA = "tag_data";
    public final static String TAG_DATA_LEN = "tag_data_len";
    // fast read
    public final static String TAG_ANT_1 = "tag_ant_1";
    public final static String TAG_ANT_2 = "tag_ant_2";
    public final static String TAG_ANT_3 = "tag_ant_3";
    public final static String TAG_ANT_4 = "tag_ant_4";
    public final static String COMMAND_DURATION = "command_duration";
    // tag operate
    public final static String TAG_ACCESS_EPC_MATCH = "access_epc_match";
    public final static String TAG_MONZA_STATUS = "monza_status";
    public final static String TAG_STATUS = "tag_status";

    // setting operate
//    public final static String WORK_ANTENNA = "work_antenna";
//    public final static String ARY_OUTPUT_POWER = "ary_output_power";
//    public final static String FREQUENCY_REGION = "frequency_region";
//    public final static String FREQUENCY_START = "frequency_start";
//    public final static String FREQUENCY_END = "frequency_end";
//    public final static String USER_DEFINE_FREQUENCY_INTERVAL = "user_define_frequency_interval";
//    public final static String USER_DEFINE_CHANNEL_QUANTITY = "user_define_channel_quantity";
//    public final static String USER_DEFINE_START_FREQUENCY = "user_define_start_frequency";
//    public final static String PLUS_MINUS = "plus_minus";
//    public final static String TEMPERATURE = "temperature";
//    public final static String GP_IO_1_VALUE = "gp_io_1_value";
//    public final static String GP_IO_2_VALUE = "gp_io_2_value";
//    public final static String ANT_CONNECTION_DETECTOR = "ant_connection_detector";
//    public final static String READER_IDENTIFIER = "reader_identifier";
//    public final static String RF_LINK_PROFILE = "rf_link_profile";
//    public final static String RF_PORT_RETURN_LOSS = "rf_port_return_loss";
//    public final static String MASK_VALUE = "mask_value";
//    public final static String BEEP_MODE = "beep_mode";
    // sn
//    public final static String SN = "sn";
    // firmware version
//    public final static String FIRMWARE_MAIN_VERSION = "firmware_main_version";
//    public final static String FIRMWARE_MIN_VERSION = "firmware_min_version";
    // battery
//    public final static String BATTERY_VOLTAGE = "battery_voltage";
    public final static String BATTERY_REMAINING_PERCENT = "battery_remaining_percent";
//    public final static String BATTERY_CHARGING = "battery_charging";
}
