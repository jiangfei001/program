package com.sgs.next.comcourier.sfservice.fourlevel;

/**
 * Created by hewenyu on 2018/2/28.
 */

public class FourmodeUtil {
    public boolean smartFillGetAddress(String getAddress, boolean needProvince) {
        String[] fourArrayFuzzy = FourlevelAddressUtil.splitAddressFuzzy(getAddress, needProvince);
        //起码要省市都有才重新匹配设置，避免地址本身就带有省市的特殊情况
        if (!StringUtils.isEmpty(fourArrayFuzzy[0]) && !StringUtils.isEmpty(fourArrayFuzzy[1])) {
            /*mSendGetInformation.setGetProvince(fourArrayFuzzy[0]);
            mSendGetInformation.setGetCity(fourArrayFuzzy[1]);
            mSendGetInformation.setGetDistrict(fourArrayFuzzy[2]);
            mSendGetInformation.setGetSimpleAddress(fourArrayFuzzy[3]);
            mSendGetInformation.setDestCityCode(fourArrayFuzzy[4]);
            mSendGetInformation.setInter(false);           //肯定不是国际
            mSendGetInformation.setDestCountryCode("CN");
            mTvGetLevelAddress.setText(String.format("%s%s%s", fourArrayFuzzy[0], fourArrayFuzzy[1], fourArrayFuzzy[2]));
            mTvGetAddress.setText(fourArrayFuzzy[3]);
            recordSmartFillEvent(getAddress, fourArrayFuzzy[0], fourArrayFuzzy[1], fourArrayFuzzy[2], fourArrayFuzzy[3], 1);*/
            return true;
        }
        return false;
    }

}
