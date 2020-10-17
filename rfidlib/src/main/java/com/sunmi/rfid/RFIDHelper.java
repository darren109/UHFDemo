package com.sunmi.rfid;

import android.os.RemoteException;

public interface RFIDHelper {

    /**
     * Get RFID model
     * 100 : NONE
     * 101 : R2000
     */
    int getScanModel() throws RemoteException;


    /**
     * register call
     *
     * @param call handle result
     */
    void registerReaderCall(ReaderCall call);

    void unregisterReaderCall();

    //----------------18000-6C---------------

    /**
     * Inventory Tag.
     * <br>When reader gets this command, the inventory for EPC GEN2 tags starts, tag data will be stored in the internal buffer.
     * <br>Attention:
     * <br>When sets Repeat parameter to 255(0xFF), the anti-collision algorithm is optimized for applications with small tag quantity, which provide better efficiency and less response time.
     *
     * @param btRepeat Repeat time of inventory round. When Repeat = 255, The inventory duration is minimized. For example, if the RF field only has one or two tags, the inventory duration could be only 30-50 mS, this function provides a possibility for fast antenna switch applications on multi-ant devices
     */
    void inventory(byte btRepeat);

    /**
     * Inventory Tag(Read Time Mode).
     * <br>Attention:
     * <br> The hardware has a dual CPU architecture, main CPU is responsible for tag inventory, and assistant CPU is responsible for data management. Inventory and data transfer are parallel and simultaneous. So the data transfer via serial port doesn't affect the efficiency of reader.
     *
     * @param btRepeat Repeat time of inventory round. When Repeat = 255, The inventory duration is minimized. For example, if the RF field only has one or two tags, the inventory duration could be only 30-50 mS, this function provides a possibility for fast antenna switch applications on multi-ant devices.
     */
    void realTimeInventory(byte btRepeat);

    /**
     * User define session and target inventory.
     *
     * @param btSession Desired session ID
     * @param btTarget  Desired Inventoried Flag(0x00:A, 0x01:B)
     * @param btRepeat  Number of times of repeating this inventory.
     */
    void customizedSessionTargetInventory(byte btSession, byte btTarget, byte btSL, byte btPhase, byte btPowerSave, byte btRepeat);

    /**
     * Fast Switch Antenna Mode.
     * <br>Attention:
     * <br> The hardware has a dual CPU architecture, main CPU is responsible for tag inventory, and assistant CPU is responsible for data management. Inventory and data transfer are parallel and simultaneous. So the data transfer via serial port doesn't affect the efficiency of reader.
     * <br>In massive tag applications, please use cmd_real_time_inventory command which is more effective for large tag quantity applications.
     *
     * @param btA        First working ant (00 - 03). If set this byte above 03 means ignore it.
     * @param btStayA    Inventory round for an antenna.
     * @param btB        Second working ant (00 - 03). If set this byte above 03 means ignore it.
     * @param btStayB    Inventory round for an antenna.
     * @param btC        Third working ant (00 - 03). If set this byte above 03 means ignore it.
     * @param btStayC    Inventory round for an antenna.
     * @param btD        Fourth working ant (00 - 03). If set this byte above 03 means ignore it.
     * @param btStayD    Inventory round for an antenna.
     * @param btInterval Rest time between switching antennas. During the cause of rest, RF output will be cancelled, thus power consumption and heat generation are both reduced.
     * @param btRepeat   Repeat the inventory with above ant switch sequence.
     */
    void fastSwitchAntInventory(byte btA, byte btStayA, byte btB, byte btStayB,
                                byte btC, byte btStayC, byte btD, byte btStayD,
                                byte btInterval, byte btRepeat);

    /**
     * Read Tag.
     * <br>Attention:
     * <br>If two tags have the same EPC, but different read data, then these two tags are considered different tags.
     *
     * @param btMemBank     Tag memory bank(0x00:RESERVED, 0x01:EPC, 0x02:TID, 0x03:USER)
     * @param btWordAdd     Read start address,please see the tag's spec for more information.
     * @param btWordCnt     Read data length,Data length in WORD(16bits) unit. Please see the tag's spec for more information.
     * @param btAryPassWord Access password,4 bytes.
     */
    void readTag(byte btMemBank, byte btWordAdd, byte btWordCnt, byte[] btAryPassWord);

    /**
     * Write Tag.
     *
     * @param btAryPassWord Access password, 4 bytes.
     * @param btMemBank     Tag memory bank(0x00:RESERVED, 0x01:EPC, 0x02:TID, 0x03:USER)
     * @param btWordAdd     Write start address,WORD(16 bits). When write EPC area, notice that EPC starts from address 02, the first two 2 words are for PC+CRC.
     * @param btWordCnt     WORD(16 bits), please see the tag's spec for more information.
     * @param btAryData     Write data, btWordCnt*2 bytes.
     */
    void writeTag(byte[] btAryPassWord, byte btMemBank, byte btWordAdd, byte btWordCnt, byte[] btAryData);

    /**
     * Lock Tag.
     *
     * @param btAryPassWord Access password, 4 bytes.
     * @param btMemBank     Tag memory bank(0x01:User Memory, 0x02:TID Memory, 0x03:EPC Memory, 0x04:Access Password, 0x05:Kill Password)
     * @param btLockType    Lock operation type(0x00:Open, 0x01:Lock, 0x02:Permanent open, 0x03:Permanent lock)
     */
    void lockTag(byte[] btAryPassWord, byte btMemBank, byte btLockType);

    /**
     * Kill Tag.
     *
     * @param btAryPassWord Kill password,4 bytes
     */
    void killTag(byte[] btAryPassWord);

    /**
     * Set Access EPC match(EPC match is effective,until next refresh).
     *
     * @param btEpcLen Length of EPC.
     * @param btAryEpc EPC, Length equals EpcLen.
     */
    void setAccessEpcMatch(byte btEpcLen, byte[] btAryEpc);

    /**
     * Clear EPC match.
     */
    void cancelAccessEpcMatch();

    /**
     * Query match EPC status.
     */
    void getAccessEpcMatch();

    /**
     * Set Impinj Monza FastTID.
     * <br>Attention:
     * <br>This function is only affective for some of Impinj Monza tag types.
     * <br>This function improves the performance of identifying tag's TID.
     * <br>When this function takes effect, tag's TID will be included to tag's EPC, therefore, tag's EPC will be altered; the original data (PC + EPC) will be changed to altered PC + EPC + EPC's CRC + TID.
     * <br>If error occurred during identifying TID, only the original data (PC + EPC) will be sent.
     * <br>If you don't need this function, please turn it off, otherwise there will be unnecessary time consumption.
     * <br>This command doesn't store the status to internal flash. After reset or power on, the value stored in flash will be restored.
     *
     * @param blnOpen Whether to open FastTID
     * @param blnSave Whether to store in FLASH
     */
    void setImpinjFastTid(boolean blnOpen, boolean blnSave);

    /**
     * Query current set of FastTID.
     */
    void getImpinjFastTid();

    //----------------18000-6B---------------

    /**
     * Inventory 18000-6B Tag.
     */
    void iso180006BInventory();

    /**
     * Read 18000-6B Tag.
     *
     * @param btAryUID  Operated Tag's UID, 8 bytes
     * @param btWordAdd Read data first address
     * @param btWordCnt Read data length
     */
    void iso180006BReadTag(byte[] btAryUID, byte btWordAdd, byte btWordCnt);

    /**
     * Write 18000-6B Tag.
     *
     * @param btAryUID    Operated Tag's UID, 8 bytes
     * @param btWordAdd   Write data first address
     * @param btWordCnt   Write data length
     * @param btAryBuffer Write data
     */
    void iso180006BWriteTag(byte[] btAryUID, byte btWordAdd, byte btWordCnt, byte[] btAryBuffer);

    /**
     * Lock 18000-6B Tag.
     *
     * @param btAryUID  Operated Tag's UID, 8 bytes
     * @param btWordAdd Locked address
     */
    void iso180006BLockTag(byte[] btAryUID, byte btWordAdd);

    /**
     * Query 18000-6B Tag.
     *
     * @param btAryUID  Operated Tag's UID, 8 bytes
     * @param btWordAdd To query address
     */
    void iso180006BQueryLockTag(byte[] btAryUID, byte btWordAdd);

    //----------------缓存操作命令---------------

    /**
     * Get tag data and keep buffer.
     * <br>Attention:
     * <br>The data in the buffer won't be lost after execution of this command.
     * <br>If the cmd_inventory is executed again, the tag data escalate in the buffer.
     * <br>Other 18000-6C commands can clear the buffer.
     */
    void getInventoryBuffer();

    /**
     * Get tag data and clear buffer.
     */
    void getAndResetInventoryBuffer();

    /**
     * Query tag quantity in buffer.
     */
    void getInventoryBufferTagCount();

    /**
     * Clear buffer of tag data.
     */
    void resetInventoryBuffer();

    //----------------setting operate---------------

    /**
     * Set work antenna.
     *
     * @param btWorkAntenna Ant ID(0x00:Ant 1, 0x01:Ant 2, 0x02:Ant 3, 0x03:Ant 4)
     */
    void setWorkAntenna(byte btWorkAntenna);

    /**
     * Query working antenna.
     */
    void getWorkAntenna();

    /**
     * Set output power(Method 1).
     * <br> This command consumes more than 100mS.
     * <br> If you want you change the output power frequently, please use Cmd_set_temporary_output_power command, which doesn't reduce the life of the internal flash memory.
     *
     * @param btOutputPower RF output power, range from 0 to 33(0x00 - 0x21), the unit is dBm.
     */
    void setOutputAllPower(byte btOutputPower);

    /**
     * Set output power(Method 2). <br>
     * This command consumes more than 100mS. <br>
     * If you want you change the output power frequently, please use
     * Cmd_set_temporary_output_power command, which doesn't reduce the life of
     * the internal flash memory.
     *
     * @param btPower1 Output power of antenna 1, range from 0 to 33(0x00 - 0x21),
     *                 the unit is dBm.
     * @param btPower2 Output power of antenna 2, range from 0 to 33(0x00 - 0x21),
     *                 the unit is dBm.
     * @param btPower3 Output power of antenna 3, range from 0 to 33(0x00 - 0x21),
     *                 the unit is dBm.
     * @param btPower4 Output power of antenna 4, range from 0 to 33(0x00 - 0x21),
     *                 the unit is dBm.
     * @return Succeeded :0, Failed:-1
     */
    void setOutputPower(byte btPower1, byte btPower2, byte btPower3, byte btPower4);

    /**
     * Query output power.
     */
    void getOutputPower();

    /**
     * Set frequency region(system default frequencies).
     *
     * @param btRegion      Spectrum regulation(0x01:FCC, 0x02:ETSI, 0x03:CHN)
     * @param btStartRegion Start frequency of the spectrum,
     * @param btEndRegion   End frequency of the spectrum,Setup the range of the RF output spectrum. The rules are: 1,Start frequency and end frequency should be in the range of the specified regulation. 2,Start frequency should be equal or lower than end frequency. 3, End frequency equals start frequency means use single frequency point.
     */
    void setFrequencyRegion(byte btRegion, byte btStartRegion, byte btEndRegion);

    /**
     * Set frequency region(user defined frequencies).
     *
     * @param btFreqInterval    Frequency space, frequency space = FreqSpace x 10KHz.
     * @param btChannelQuantity Frequency Quantity, this quantity includes the start frequency, if set this byte to 1, it means use start frequency as the single carrier frequency . This byte should be larger than 0.
     * @param nStartFreq        Start Frequency, the unit is KHz. Set the start frequency with hex format, for example, 915000KHz = 0D F6 38 KHz.
     */
    void setUserDefineFrequency(byte btFreqInterval, byte btChannelQuantity, int nStartFreq);

    /**
     * Query frequency region.
     */
    void getFrequencyRegion();

    /**
     * Set Buzzer behavior.
     * <br>Buzzer behavior 0x02(Beep after every tag has identified) occupies CPU process time that affects anti-collision algorithm significantly. It is recommended that this option should be used for tag test.
     *
     * @param btMode Buzzer behavior(0x00:Quiet, 0x01:Beep after every inventory round if tag(s) identified, 0x02:Beep after every tag has identified.)
     */
    void setBeeperMode(byte btMode);

    void getBeeperMode();

    /**
     * Query internal temperature.
     */
    void getReaderTemperature();

    /**
     * Read GPIO Level.
     */
    void readGpioValue();

    /**
     * Write GPIO Level.
     *
     * @param btChooseGpio ChooseGpio(0x03:Set GPIO3, 0x04:Set GPIO4)
     * @param btGpioValue  GpioValue(0x00:Set to low level, 0x01:Set to high level)
     */
    void writeGpioValue(byte btChooseGpio, byte btGpioValue);

    /**
     * Set antenna connection detector status.
     *
     * @param btDetectorStatus status(0x00:close detector of antenna connection, other values:sensitivity of antenna connection detector(return loss of port),unit dB. The higher the value, the greater the impedance matching requirements for port
     */
    void setAntConnectionDetector(byte btDetectorStatus);

    /**
     * Get antenna connection detector status.
     */
    void getAntConnectionDetector();

    /**
     * Set temporary output power.
     * <br>The output power value will Not be saved to the internal flash memory so that the output power will be restored from the internal flash memory after restart or power off.
     *
     * @param btRfPower RF output power, range from 20-33(0x14 - 0x21), the unit is dBm.
     * @return Succeeded :0, Failed:-1
     */
    void setTemporaryOutputPower(byte btRfPower);

    /**
     * Set Reader Identifier.
     * <br>The identifier is stored in internal flash.
     *
     * @param btAryIdentifier Reader's identifier (12 bytes).
     */
    void setReaderIdentifier(byte[] btAryIdentifier);

    /**
     * Get Reader Identifier.
     */
    void getReaderIdentifier();

    /**
     * RF Link Setup.
     * <br>If this command succeeded, reader will be reset, and the profile configuration is stored in the internal flash.
     *
     * @param btProfile Communication rate(0xD0:Tari 25uS,FM0 40KHz, 0xD1:Tari 25uS,Miller 4 250KHz, 0xD2:Tari 25uS,Miller 4 300KHz, 0xD3:Tari 6.25uS,FM0 400KHz)
     */
    void setRfLinkProfile(byte btProfile);

    /**
     * Read RF Link.
     */
    void getRfLinkProfile();

    /**
     * Measure RF Port Return Loss.
     *
     * @param btFrequency FreqParameter, system will measure the return loss of current antenna port at the desired frequency.
     * @return Succeeded :0, Failed:-1
     */
    void getRfPortReturnLoss(byte btFrequency);

    //----------------SN and Firware Version---------------

    /**
     * get SN
     */
    void getReaderSN();

    /**
     * get Firmware Version
     */
    void getReaderVersion();

    /**
     * get rfid Firmware Version
     */
    void getFirmwareVersion();

    //----------------Battery---------------

    /**
     * get Bettery remaining percent
     */
    void getBatteryRemainingPercent();

    /**
     * get Bettery Voltage
     */
    void getBatteryVoltage();

    /**
     * get charge state
     */
    void getBatteryChargeState();


    //----------------reset boot and set reader address ---------------

    /**
     * reset Reader
     */
    void resetReader();

    /**
     * Set Reader Address.
     *
     * @param btNewReadId New Reader Address,value range0-254(0x00-0xFE)
     */
    void setReaderAddress(byte btNewReadId);

    /**
     * reset hardware
     */
    void reset();


    //----------------Battery---------------

    /**
     * get charge number of times
     */
    void getBatteryChargeNumTimes();

    //----------------Firmware Update---------------

    /**
     * Firmware Update
     *
     * @param binFile update file path
     * @param call    update call
     */
    void firmwareUpdate(String binFile, IFirmwareUpdateCall call);

    //----------------6C Tag Operation---------------

    /**
     * set impinj save tag focus
     */
    void setImpinjSaveTagFocus(boolean blnOpen);

    //----------------Mask filter---------------

    /**
     * Set the mask filter the Tag.
     *
     * @param btMaskNo   Mask Filter No
     * @param btTarget   Set the inventory way(s0,s1,s2 or s3),you must use you set target to inventory the tag.
     * @param btAction   The match tag or not Action,you can see the detail of this command.
     * @param btMembank  The select mask region,EPC,TID or USER.
     * @param btStartAdd The mask start address(according to bit).
     * @param btMaskLen  The mask length (according to bit).
     * @param maskValue  The mask value.
     */
    void setTagMask(byte btMaskNo, byte btTarget, byte btAction, byte btMembank, byte btStartAdd, byte btMaskLen, byte[] maskValue);

    /**
     * Clear the mask setting.
     *
     * @param btMaskNo Mask Filter No
     */
    void clearTagMask(byte btMaskNo);

    /**
     * Get the mask setting.
     */
    void getTagMask();

    public interface Call {
        void run() throws RemoteException;
    }
}
