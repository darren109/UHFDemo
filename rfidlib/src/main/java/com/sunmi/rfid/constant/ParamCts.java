package com.sunmi.rfid.constant;

import android.text.TextUtils;
import android.util.Log;

import com.sunmi.rfid.RFIDManager;

public final class ParamCts {

    public final static String BROADCAST_RFID_CLOSE = "com.sunmi.rfid.rfid_close";// 关闭RFID服务
    public final static String BROADCAST_RFID_OPEN = "com.sunmi.rfid.rfid_open";// 启用RFID服务
    public final static String BROADCAST_UN_FOUND_READER = "com.sunmi.rfid.unFoundReader";// 未找到相关设备
    public final static String BROADCAST_ON_LOST_CONNECT = "com.sunmi.rfid.onLostConnect";// 连接断开
    public final static String BROADCAST_ON_CONNECT = "com.sunmi.rfid.onConnect";// 底座连接
    public final static String BROADCAST_ON_DISCONNECT = "com.sunmi.rfid.onDisconnect";// 底座断开
    public final static String BROADCAST_READER_BOOT = "com.sunmi.rfid.readerBoot";// 复位启动完成
    public final static String BROADCAST_SN = "com.sunmi.rfid.sn";//sn
    public final static String BROADCAST_FIRMWARE_VERSION = "com.sunmi.rfid.firmwareVersion";// 固件版本
    public final static String BROADCAST_BATTERY_VOLTAGE = "com.sunmi.rfid.batteryVoltage";// 电池电压
    public final static String BROADCAST_BATTERY_REMAINING_PERCENTAGE = "com.sunmi.rfid.batteryRemainingPercentage";// 电池电量
    public final static String BROADCAST_BATTER_LOW_ELEC = "com.sunmi.rfid.batteryLowElec";// 电池低电量
    public final static String BROADCAST_BATTER_CHARGING = "com.sunmi.rfid.batteryCharging";// 电池充电状态
    public final static String BROADCAST_BATTER_CHARGING_NUM_TIMES = "com.sunmi.rfid.batteryChargingNumTimes";// 电池充电循环次数


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
    public final static String WORK_ANTENNA = "work_antenna";
    public final static String ARY_OUTPUT_POWER = "ary_output_power";
    public final static String FREQUENCY_REGION = "frequency_region";
    public final static String FREQUENCY_START = "frequency_start";
    public final static String FREQUENCY_END = "frequency_end";
    public final static String USER_DEFINE_FREQUENCY_INTERVAL = "user_define_frequency_interval";
    public final static String USER_DEFINE_CHANNEL_QUANTITY = "user_define_channel_quantity";
    public final static String USER_DEFINE_START_FREQUENCY = "user_define_start_frequency";

    public final static String PLUS_MINUS = "plus_minus";
    public final static String TEMPERATURE = "temperature";

    public final static String GP_IO_1_VALUE = "gp_io_1_value";
    public final static String GP_IO_2_VALUE = "gp_io_2_value";

    public final static String ANT_CONNECTION_DETECTOR = "ant_connection_detector";

    public final static String READER_IDENTIFIER = "reader_identifier";

    public final static String RF_LINK_PROFILE = "rf_link_profile";
    public final static String RF_PORT_RETURN_LOSS = "rf_port_return_loss";

    public final static String MASK_ID = "mask_id";
    public final static String MASK_COUNT = "mask_count";
    public final static String MASK_TARGET = "mask_target";
    public final static String MASK_ACTION = "mask_action";
    public final static String MASK_MEMBANK = "mask_membank";
    public final static String MASK_START_ADD = "mask_start_add";
    public final static String MASK_LEN = "mask_len";
    public final static String MASK_VALUE = "mask_value";

    public final static String BEEP_MODE = "sp_beep_mode";
    // sn
    public final static String SCAN_TYPE = "scan_type";
    public final static String SN = "sn";
    // firmware version
    public final static String FIRMWARE_MAIN_VERSION = "firmware_main_version";
    public final static String FIRMWARE_MIN_VERSION = "firmware_min_version";
    // battery
    public final static String BATTERY_VOLTAGE = "battery_voltage";
    public final static String BATTERY_REMAINING_PERCENT = "battery_remaining_percent";
    public final static String BATTERY_CHARGING = "battery_charging";
    public final static String BATTERY_CHARGING_NUM_TIMES = "battery_charging_num_times";

    public static double getParamsToRf(int params) {
        switch (params) {
            case 0:// (0x00)
                return 865.00;
            case 1:// (0x01)
                return 865.50;
            case 2:// (0x02)
                return 866.00;
            case 3:// (0x03)
                return 866.50;
            case 4:// (0x04)
                return 867.00;
            case 5:// (0x05)
                return 867.50;
            case 6:// (0x06)
                return 868.00;
            case 7:// (0x07)
                return 902.00;
            case 8:// (0x08)
                return 902.50;
            case 9:// (0x09)
                return 903.00;
            case 10:// (0x0A)
                return 903.50;
            case 11:// (0x0B)
                return 904.00;
            case 12:// (0x0C)
                return 904.50;
            case 13:// (0x0D)
                return 905.00;
            case 14:// (0x0E)
                return 905.50;
            case 15:// (0x0F)
                return 906.00;
            case 16:// (0x10)
                return 906.50;
            case 17:// (0x11)
                return 907.00;
            case 18:// (0x12)
                return 907.50;
            case 19:// (0x13)
                return 908.00;
            case 20:// (0x14)
                return 908.50;
            case 21:// (0x15)
                return 909.00;
            case 22:// (0x16)
                return 909.50;
            case 23:// (0x17)
                return 910.00;
            case 24:// (0x18)
                return 910.50;
            case 25:// (0x19)
                return 911.00;
            case 26:// (0x1A)
                return 911.50;
            case 27:// (0x1B)
                return 912.00;
            case 28:// (0x1C)
                return 912.50;
            case 29:// (0x1D)
                return 913.00;
            case 30:// (0x1E)
                return 913.50;
            case 31:// (0x1F)
                return 914.00;
            case 32:// (0x20)
                return 914.50;
            case 33:// (0x21)
                return 915.00;
            case 34:// (0x22)
                return 915.50;
            case 35:// (0x23)
                return 916.00;
            case 36:// (0x24)
                return 916.50;
            case 37:// (0x25)
                return 917.00;
            case 38:// (0x26)
                return 917.50;
            case 39:// (0x27)
                return 918.00;
            case 40:// (0x28)
                return 918.50;
            case 41:// (0x29)
                return 919.00;
            case 42:// (0x2A)
                return 919.50;
            case 43:// (0x2B)
                return 920.00;
            case 44:// (0x2C)
                return 920.50;
            case 45:// (0x2D)
                return 921.00;
            case 46:// (0x2E)
                return 921.50;
            case 47:// (0x2F)
                return 922.00;
            case 48:// (0x30)
                return 922.50;
            case 49:// (0x31)
                return 923.00;
            case 50:// (0x32)
                return 923.50;
            case 51:// (0x33)
                return 924.00;
            case 52:// (0x34)
                return 924.50;
            case 53:// (0x35)
                return 925.00;
            case 54:// (0x36)
                return 925.50;
            case 55:// (0x37)
                return 926.00;
            case 56:// (0x38)
                return 926.50;
            case 57:// (0x39)
                return 927.00;
            case 58:// (0x3A)
                return 927.50;
            case 59:// (0x3B)
                return 928.00;
            default:
                return -1;
        }
    }

    /**
     * @param rf rf *10 to int
     */
    public static int getRfToParams(int rf) {
        switch (rf) {
            case 8650:
                return 0;// (0x00)
            case 8655:
                return 1;// (0x01)
            case 8660:
                return 2;// (0x02)
            case 8665:
                return 3;// (0x03)
            case 8670:
                return 4;// (0x04)
            case 8675:
                return 5;// (0x05)
            case 8680:
                return 6;// (0x06)
            case 9020:
                return 7;// (0x07)
            case 9025:
                return 8;// (0x08)
            case 9030:
                return 9;// (0x09)
            case 9035:
                return 10;// (0x0A)
            case 9040:
                return 11;// (0x0B)
            case 9045:
                return 12;// (0x0C)
            case 9050:
                return 13;// (0x0D)
            case 9055:
                return 14;// (0x0E)
            case 9060:
                return 15;// (0x0F)
            case 9065:
                return 16;// (0x10)
            case 9070:
                return 17;// (0x11)
            case 9075:
                return 18;// (0x12)
            case 9080:
                return 19;// (0x13)
            case 9085:
                return 20;// (0x14)
            case 9090:
                return 21;// (0x15)
            case 9095:
                return 22;// (0x16)
            case 9100:
                return 23;// (0x17)
            case 9105:
                return 24;// (0x18)
            case 9110:
                return 25;// (0x19)
            case 9115:
                return 26;// (0x1A)
            case 9120:
                return 27;// (0x1B)
            case 9125:
                return 28;// (0x1C)
            case 9130:
                return 29;// (0x1D)
            case 9135:
                return 30;// (0x1E)
            case 9140:
                return 31;// (0x1F)
            case 9145:
                return 32;// (0x20)
            case 9150:
                return 33;// (0x21)
            case 9155:
                return 34;// (0x22)
            case 9160:
                return 35;// (0x23)
            case 9165:
                return 36;// (0x24)
            case 9170:
                return 37;// (0x25)
            case 9175:
                return 38;// (0x26)
            case 9180:
                return 39;// (0x27)
            case 9185:
                return 40;// (0x28)
            case 9190:
                return 41;// (0x29)
            case 9195:
                return 42;// (0x2A)
            case 9200:
                return 43;// (0x2B)
            case 9205:
                return 44;// (0x2C)
            case 9210:
                return 45;// (0x2D)
            case 9215:
                return 46;// (0x2E)
            case 9220:
                return 47;// (0x2F)
            case 9225:
                return 48;// (0x30)
            case 9230:
                return 49;// (0x31)
            case 9235:
                return 50;// (0x32)
            case 9240:
                return 51;// (0x33)
            case 9245:
                return 52;// (0x34)
            case 9250:
                return 53;// (0x35)
            case 9255:
                return 54;// (0x36)
            case 9260:
                return 55;// (0x37)
            case 9265:
                return 56;// (0x38)
            case 9270:
                return 57;// (0x39)
            case 9275:
                return 58;// (0x3A)
            case 9280:
                return 59;// (0x3B)
            default:
                return -1;
        }
    }

    /**
     * NU01 --- CHN(中规频段 920~925MHz)0x03 CHN  输出功率：29(默认)
     * NU02 --- CHN(美规频段 902~928MHz)0x01 FCC  输出功率：28(默认)
     * NU03 --- CHN(欧规频段 865~868MHz)0x02 ETSI 输出功率：28(默认)
     * 示例：NU03D9CU00179
     */
    public static int[] getRFFrequencyBand(String sn) {
        Log.d("darren-ParamsCts", "SN:" + sn);
        if (!TextUtils.isEmpty(sn)) {
            if (sn.startsWith("NU01")) {
                return new int[]{1, 920, 925, 0x03};
            }
            if (sn.startsWith("NU02")) {
                return new int[]{1, 902, 928, 0x01};
            }
            if (sn.startsWith("NU03")) {
                return new int[]{1, 865, 868, 0x02};
            }
            return new int[]{-1, -1, -1, -1};
        }
        return new int[]{0, 0, 0, 0};
    }

    public static int[] getRFFrequencyBand(int scanModel, String sn) {
        if (scanModel == RFIDManager.UHF_R2000) {
            return getRFFrequencyBand(sn);
        } else if (scanModel == RFIDManager.INNER) {
            return new int[]{1, 865, 926, 0x04};
        }
        return new int[]{0, 0, 0, 0};
    }
}
