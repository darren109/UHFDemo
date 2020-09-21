package com.sunmi.rfid;

import android.os.RemoteException;

public class ServicesHelper implements RFIDHelper {
    private IScanRFIDInterface scan;

    public ServicesHelper(IScanRFIDInterface scan) {
        this.scan = scan;
    }

    @Override
    public int getScanModel() throws RemoteException {
        return scan.getScanModel();
    }

    @Override
    public void registerReaderCall(ReaderCall call) {
        handleError(() -> scan.registerReaderCall(call));
    }

    @Override
    public void unregisterReaderCall() {
        handleError(() -> scan.unregisterReaderCall());
    }

    @Override
    public void inventory(byte btRepeat) {
        handleError(() -> scan.inventory(btRepeat));
    }

    @Override
    public void realTimeInventory(byte btRepeat) {
        handleError(() -> scan.realTimeInventory(btRepeat));
    }

    @Override
    public void customizedSessionTargetInventory(byte btSession, byte btTarget, byte btSL, byte btPhase, byte btPowerSave, byte btRepeat) {
        handleError(() -> scan.customizedSessionTargetInventory(btSession, btTarget, btSL, btPhase, btPowerSave, btRepeat));
    }

    @Override
    public void fastSwitchAntInventory(byte btA, byte btStayA, byte btB, byte btStayB, byte btC, byte btStayC, byte btD, byte btStayD, byte btInterval, byte btRepeat) {
        handleError(() -> scan.fastSwitchAntInventory(btA, btStayA, btB, btStayB, btC, btStayC, btD, btStayD, btInterval, btRepeat));
    }

    @Override
    public void readTag(byte btMemBank, byte btWordAdd, byte btWordCnt, byte[] btAryPassWord) {
        handleError(() -> scan.readTag(btMemBank, btWordAdd, btWordCnt, btAryPassWord));
    }

    @Override
    public void writeTag(byte[] btAryPassWord, byte btMemBank, byte btWordAdd, byte btWordCnt, byte[] btAryData) {
        handleError(() -> scan.writeTag(btAryPassWord, btMemBank, btWordAdd, btWordCnt, btAryData));
    }

    @Override
    public void lockTag(byte[] btAryPassWord, byte btMemBank, byte btLockType) {
        handleError(() -> scan.lockTag(btAryPassWord, btMemBank, btLockType));
    }

    @Override
    public void killTag(byte[] btAryPassWord) {
        handleError(() -> scan.killTag(btAryPassWord));
    }

    @Override
    public void setAccessEpcMatch(byte btEpcLen, byte[] btAryEpc) {
        handleError(() -> scan.setAccessEpcMatch(btEpcLen, btAryEpc));
    }

    @Override
    public void cancelAccessEpcMatch() {
        handleError(() -> scan.cancelAccessEpcMatch());
    }

    @Override
    public void getAccessEpcMatch() {
        handleError(() -> scan.getAccessEpcMatch());
    }

    @Override
    public void setImpinjFastTid(boolean blnOpen, boolean blnSave) {
        handleError(() -> scan.setImpinjFastTid(blnOpen, blnSave));
    }

    @Override
    public void getImpinjFastTid() {
        handleError(() -> scan.getImpinjFastTid());
    }

    @Override
    public void iso180006BInventory() {
        handleError(() -> scan.iso180006BInventory());
    }

    @Override
    public void iso180006BReadTag(byte[] btAryUID, byte btWordAdd, byte btWordCnt) {
        handleError(() -> scan.iso180006BReadTag(btAryUID, btWordAdd, btWordCnt));
    }

    @Override
    public void iso180006BWriteTag(byte[] btAryUID, byte btWordAdd, byte btWordCnt, byte[] btAryBuffer) {
        handleError(() -> scan.iso180006BWriteTag(btAryUID, btWordAdd, btWordCnt, btAryBuffer));
    }

    @Override
    public void iso180006BLockTag(byte[] btAryUID, byte btWordAdd) {
        handleError(() -> scan.iso180006BLockTag(btAryUID, btWordAdd));
    }

    @Override
    public void iso180006BQueryLockTag(byte[] btAryUID, byte btWordAdd) {
        handleError(() -> scan.iso180006BQueryLockTag(btAryUID, btWordAdd));
    }

    @Override
    public void getInventoryBuffer() {
        handleError(() -> scan.getInventoryBuffer());
    }

    @Override
    public void getAndResetInventoryBuffer() {
        handleError(() -> scan.getAndResetInventoryBuffer());
    }

    @Override
    public void getInventoryBufferTagCount() {
        handleError(() -> scan.getInventoryBufferTagCount());
    }

    @Override
    public void resetInventoryBuffer() {
        handleError(() -> scan.getInventoryBuffer());
    }

    @Override
    public void setWorkAntenna(byte btWorkAntenna) {
        handleError(() -> scan.setWorkAntenna(btWorkAntenna));
    }

    @Override
    public void getWorkAntenna() {
        handleError(() -> scan.getWorkAntenna());
    }

    @Override
    public void setOutputAllPower(byte btOutputPower) {
        handleError(() -> scan.setOutputAllPower(btOutputPower));
    }

    @Override
    public void setOutputPower(byte btPower1, byte btPower2, byte btPower3, byte btPower4) {
        handleError(() -> scan.setOutputPower(btPower1, btPower2, btPower3, btPower4));
    }

    @Override
    public void getOutputPower() {
        handleError(() -> scan.getOutputPower());
    }

    @Override
    public void setFrequencyRegion(byte btRegion, byte btStartRegion, byte btEndRegion) {
        handleError(() -> scan.setFrequencyRegion(btRegion, btStartRegion, btEndRegion));
    }

    @Override
    public void setUserDefineFrequency(byte btFreqInterval, byte btChannelQuantity, int nStartFreq) {
        handleError(() -> scan.setUserDefineFrequency(btFreqInterval, btChannelQuantity, nStartFreq));
    }

    @Override
    public void getFrequencyRegion() {
        handleError(() -> scan.getFrequencyRegion());
    }

    @Override
    public void setBeeperMode(byte btMode) {
        handleError(() -> scan.setBeeperMode(btMode));
    }

    @Override
    public void getBeeperMode() {
        handleError(() -> scan.getBeeperMode());
    }

    @Override
    public void getReaderTemperature() {
        handleError(() -> scan.getReaderTemperature());
    }

    @Override
    public void readGpioValue() {
        handleError(() -> scan.readGpioValue());
    }

    @Override
    public void writeGpioValue(byte btChooseGpio, byte btGpioValue) {
        handleError(() -> scan.writeGpioValue(btChooseGpio, btGpioValue));
    }

    @Override
    public void setAntConnectionDetector(byte btDetectorStatus) {
        handleError(() -> scan.setAntConnectionDetector(btDetectorStatus));
    }

    @Override
    public void getAntConnectionDetector() {
        handleError(() -> scan.getAntConnectionDetector());
    }

    @Override
    public void setTemporaryOutputPower(byte btRfPower) {
        handleError(() -> scan.setTemporaryOutputPower(btRfPower));
    }

    @Override
    public void setReaderIdentifier(byte[] btAryIdentifier) {
        handleError(() -> scan.setReaderIdentifier(btAryIdentifier));
    }

    @Override
    public void getReaderIdentifier() {
        handleError(() -> scan.getReaderIdentifier());
    }

    @Override
    public void setRfLinkProfile(byte btProfile) {
        handleError(() -> scan.setRfLinkProfile(btProfile));
    }

    @Override
    public void getRfLinkProfile() {
        handleError(() -> scan.getRfLinkProfile());
    }

    @Override
    public void getRfPortReturnLoss(byte btFrequency) {
        handleError(() -> scan.getRfPortReturnLoss(btFrequency));
    }

    @Override
    public void getReaderSN() {
        handleError(() -> scan.getReaderSN());
    }

    @Override
    public void getReaderVersion() {
        handleError(() -> scan.getReaderVersion());
    }

    @Override
    public void getFirmwareVersion() {
        handleError(() -> scan.getFirmwareVersion());
    }

    @Override
    public void getBatteryRemainingPercent() {
        handleError(() -> scan.getBatteryRemainingPercent());
    }

    @Override
    public void getBatteryVoltage() {
        handleError(() -> scan.getBatteryVoltage());
    }

    @Override
    public void getBatteryChargeState() {
        handleError(() -> scan.getBatteryChargeState());
    }

    @Override
    public void resetReader() {
        handleError(() -> scan.resetReader());
    }

    @Override
    public void setReaderAddress(byte btNewReadId) {
        handleError(() -> scan.setReaderAddress(btNewReadId));
    }

    @Override
    public void reset() {
        handleError(() -> scan.reset());
    }

    private void handleError(Call call) {
        try {
            call.run();
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException("aidl operate error.");
        }
    }
}
