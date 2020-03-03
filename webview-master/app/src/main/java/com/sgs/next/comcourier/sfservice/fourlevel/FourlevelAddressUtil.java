package com.sgs.next.comcourier.sfservice.fourlevel;

import android.util.Log;
import android.util.Pair;

import com.sgs.next.comcourier.sfservice.h6.domain.Location;
import com.sgs.next.comcourier.sfservice.h6.domain.LocationType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.sgs.next.comcourier.sfservice.h6.InfoDatabaseHelper.infoDatabaseHelper;

/**
 * Created by 80002213 on 2018/3/8.
 */

public class FourlevelAddressUtil {

    public static final String PROVINCE = "省";
    public static final String MUNICIPALITY = "自治区";

    public static final String CITY = "市";
    public static final String AUTONOMOUS_PREFECTURE = "自治州";
    public static final String STATE = "州";
    public static final String ALLIANCE = "盟";
    public static final String AREA = "地区";
    public static final String FOREST = "林区";

    public static final String COUNTY = "县";
    public static final String AUTONOMOUS_COUNTY = "自治县";
    public static final String DISTRICT = "区";
    public static final String NEW_DISTRICT = "新区";
    public static final String STREET = "街道";
    public static final String COUNTRYSIDE = "乡";
    public static final String TOWN = "镇";
    public static final String FLAG = "旗";
    public static final String TREE_FARM = "林场";

    public static final String SPACE = " ";
    public static final String STICK = "-";
    public static final String UNDERLINE = "_";
    public static final String COMMA = ",";
    public static final String COMMA_CN = "，";
    public static final String CAESURA_SIGN = "、";

    public static List<String> provinceSuffix = new ArrayList<>();
    public static List<String> citySuffix = new ArrayList<>();
    public static List<String> districtSuffix = new ArrayList<>();
    public static List<String> punctuationSuffix = new ArrayList<>();

    public static List<Location> locationProvinces = new ArrayList<>();
    public static List<Location> locationCities = new ArrayList<>();

    public static void initAddress() {
        //取得父为中国的省地址
        if (locationProvinces.isEmpty()) {
            //取得最顶级地址中国
            Location locationCN =
                    infoDatabaseHelper().loadCountryLocationByCityCode(Location.CITY_CODE_CHINA);
            Location locationHK =
                    infoDatabaseHelper().loadProvinceLocationByCityCode(Location.CITY_CODE_HK);
            Location locationMC =
                    infoDatabaseHelper().loadProvinceLocationByCityCode(Location.CITY_CODE_MC);
            Location locationTW =
                    infoDatabaseHelper().loadProvinceLocationByCityCode(Location.CITY_CODE_TW);
            locationProvinces.addAll(
                    infoDatabaseHelper().loadLocationsByParentIdAndTypeOrderById(locationCN.getId(),
                            LocationType.PROVINCE.ordinal()));
            locationProvinces.add(locationHK);
            locationProvinces.add(locationMC);
            locationProvinces.add(locationTW);
        }
        if (locationCities.isEmpty()) {
            locationCities.addAll(infoDatabaseHelper().loadCitiesByCodeOrNameFuzzy(""));
        }
        if (provinceSuffix.isEmpty()) {
            provinceSuffix.add(PROVINCE);
            provinceSuffix.add(MUNICIPALITY);
            provinceSuffix.add(CITY);
        }
        if (citySuffix.isEmpty()) {
            citySuffix.add(CITY);
            citySuffix.add(AUTONOMOUS_PREFECTURE);
            citySuffix.add(STATE);
            citySuffix.add(ALLIANCE);
            citySuffix.add(AREA);
            citySuffix.add(FOREST);
        }
        if (districtSuffix.isEmpty()) {
            districtSuffix.add(CITY);
            districtSuffix.add(DISTRICT);
            districtSuffix.add(COUNTY);
            districtSuffix.add(STREET);
            districtSuffix.add(COUNTRYSIDE);
            districtSuffix.add(TOWN);
            districtSuffix.add(AUTONOMOUS_COUNTY);
            districtSuffix.add(NEW_DISTRICT);
            districtSuffix.add(FLAG);
            districtSuffix.add(TREE_FARM);
        }
        if (punctuationSuffix.isEmpty()) {
            punctuationSuffix.add(SPACE);
            punctuationSuffix.add(STICK);
            punctuationSuffix.add(UNDERLINE);
            punctuationSuffix.add(COMMA);
            punctuationSuffix.add(COMMA_CN);
            punctuationSuffix.add(CAESURA_SIGN);
        }
    }

    public static void clearAddressCache() {
        locationProvinces.clear();
        locationCities.clear();
        provinceSuffix.clear();
        citySuffix.clear();
        districtSuffix.clear();
        punctuationSuffix.clear();
    }

    /*   *//**
     * 初始化四级地址
     *//*
    public static void initFourLevelAddress() {
        ComCourierLogUtils.d("初始化四级地址 initFourLevelAddress");
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                FourlevelAddressUtil.clearAddressCache();
                FourlevelAddressUtil.initAddress();
                subscriber.onNext("");
                subscriber.onCompleted();
            }
        }).compose(RxUtils.<String>applySchedulers()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ComCourierLogUtils.e("初始化四级地址失败：%s", e.getErrMessage());
            }

            @Override
            public void onNext(String s) {
                ComCourierLogUtils.e("初始化四级地址成功：%s", s);
            }
        });
    }*/

    /**
     * 拆分地址信息
     * 只支持解析 "xxx-xxx-xxx xxxxxxxx"  格式
     */
    public static String[] splitAddress(String address) {
        if (!StringUtils.isEmpty(address)) {
            initAddress();
            String[] simplyAddes = address.split(" ");
            if (simplyAddes.length == 2) {
                String[] fourLevelAddes = simplyAddes[0].split("-");
                if (fourLevelAddes.length == 3) {
                    //查询是否符合数据库中的四级地址数据
                    Location provice = findLocation(fourLevelAddes[0], locationProvinces);  //查询省自治区
                    if (provice != null) {
                        List<Location> cities = getLocationsByParent(provice);
                        Location city = findLocation(fourLevelAddes[1], cities);   //查询地级市
                        if (city != null) {
                            List<Location> areas = getLocationsByParent(city);
                            Location area = findLocation(fourLevelAddes[2], areas);  //查询区县
                            if (area != null) {
                                return new String[]{
                                        provice.getName(), city.getName(), area.getName(),
                                        simplyAddes[1], city.getCityCode()
                                };
                            }
                        }
                    }
                }
            }
        }
        return new String[]{"", "", "", address, ""};
    }

    public static String[] splitAddressFuzzy(String address) {
        return splitAddressFuzzy(address, true);
    }

    /**
     * 拆分地址信息 --> hewenyu
     * 省级模糊匹配省略省，自治区，市的情形
     * 城市级模糊匹配省略市，自治州，州的情形
     * 行政区级模糊匹配省略县，自治县，区，新区的情形
     * <p>
     * 模糊匹配支持地址省略中间段的特殊情形
     * e.g 省 广西壮族自治区 --> 广西
     * e.g 市 延边朝鲜族自治州 --> 延边州
     *
     * @param needProvince 省是否必填
     */
    public static String[] splitAddressFuzzy(String address1, boolean needProvince) {
        String simpleAddress = "";
        String address = address1.replace("香港特别行政区", "香港");
        if (!StringUtils.isEmpty(address)) {
            initAddress();
            //查询是否符合数据库中的四级地址数据
            Pair<Location, String> provincePair =
                    findFuzzyLocationInAddress(address, locationProvinces);  //查询省自治区
            List<Location> cities = null;
            Pair<Location, String> cityPair;
            if (provincePair.first != null) {
                simpleAddress = getSimpleAddress(address, provincePair.second);
                cities = getLocationsByParent(provincePair.first);
            }
            cityPair = findFuzzyLocationInAddress(simpleAddress, cities, 0);   //查询地级市
            //香港地区 提前查询一级
            if (cityPair.first == null && provincePair.first != null &&
                    ("852".equals(provincePair.first.getCityCode()) ||
                            "853".equals(provincePair.first.getCityCode()) ||
                            "886".equals(provincePair.first.getCityCode()))) {
                cities = infoDatabaseHelper().loadLocationsByCityCode(provincePair.first.getCityCode(), LocationType.COUNTY.ordinal());
                cityPair = findFuzzyLocationInAddress(simpleAddress, cities, 1);
            }
            //详细地址不包含省或者省市同名的情形
            if ((provincePair.first == null || cityPair.first == null) && !needProvince) {
                simpleAddress = address;
                cities = locationCities;
                cityPair = findFuzzyLocationInAddress(simpleAddress, cities, 0);
            }

            if (cityPair.first != null) {
                //如果地址最高从城市级开始，则根据城市匹配到对应省
                if (provincePair.first == null) {
                    Location province = infoDatabaseHelper().loadLocationById(cityPair.first.getParent());
                    provincePair = new Pair<>(province, province.getName());
                }

                String provinceName = provincePair.first != null ? provincePair.first.getName() : "";
                String cityName = cityPair.first.getName();
                String cityCode = cityPair.first.getCityCode();
                String cityid = cityPair.first.getId() + "";
                simpleAddress = getSimpleAddress(simpleAddress, cityPair.second);
                List<Location> areas = getLocationsByParent(cityPair.first);
                Pair<Location, String> areaPair = findFuzzyLocationInAddress(simpleAddress, areas, 1);   //查询区县

                if (areaPair.first != null) {
                    simpleAddress = getSimpleAddress(simpleAddress, areaPair.second);
                    //去掉精简地址前还剩下的行政区后缀，可以考虑不去掉以防误删除
                    //simpleAddress = wipeSuffix(simpleAddress, 2);
                    //如果行政区城市代码与城市城市代码不一致则取行政区的 e.g: 977 979
                    String districtCityCode = areaPair.first.getCityCode();
                    String districtCityId = areaPair.first.getId() + "";
                    cityCode = cityCode.equals(districtCityCode) ? cityCode : districtCityCode;
                    return new String[]{
                            provinceName, cityName, areaPair.first.getName(), simpleAddress, cityCode,
                            cityid, districtCityId
                    };
                } else {//兼容旧地址只输入了省市的情况
                    return new String[]{
                            provinceName, cityName, "", simpleAddress, cityCode, cityid, ""
                    };
                }
            }
        }
        return new String[]{"", "", "", address, "", "", ""};
    }

    /**
     * 拆分地址信息 --> hewenyu
     * 省级模糊匹配省略省，自治区，市的情形
     * 城市级模糊匹配省略市，自治州，州的情形
     * 行政区级模糊匹配省略县，自治县，区，新区的情形
     * <p>
     * 模糊匹配支持地址省略中间段的特殊情形
     * e.g 省 广西壮族自治区 --> 广西
     * e.g 市 延边朝鲜族自治州 --> 延边州
     *
     * @param address 详细地址
     */
    public static String splitAddressFuzzy1(LinkedList<String> address) {
        if (!StringUtils.isEmpty(address.get(0))) {
            initAddress();
            //查询是否符合数据库中的四级地址数据
            Pair<Location, String> provincePair =
                    findFuzzyLocationInAddress(address.get(0), locationProvinces);  //查询省自治区
            List<Location> cities = null;
            Pair<Location, String> cityPair;
            if (provincePair.first != null) {
                if ("852".equals(provincePair.first.getCityCode()) ||
                        "886".equals(provincePair.first.getCityCode())) {//香港特殊处理
                    cities = infoDatabaseHelper().loadLocationsByCityCode(provincePair.first.getCityCode(), LocationType.COUNTY.ordinal());
                } else {
                    cities = getLocationsByParent(provincePair.first);
                }
            }
            cityPair = findFuzzyLocationInAddress(address.get(1), cities, 0);   //查询地级市
            if (cityPair.first != null) {
                //如果地址最高从城市级开始，则根据城市匹配到对应省
                List<Location> areas = getLocationsByParent(cityPair.first);
                Pair<Location, String> areaPair = findFuzzyLocationInAddress(address.get(2), areas, 1);   //查询区县
                if (areaPair.first != null) {
                    //如果行政区城市代码与城市城市代码不一致则取行政区的 e.g: 977 979
                    String backValue = cityPair.first.getCityCode()
                            + ","
                            + cityPair.first.getId()
                            + ","
                            + areaPair.first.getId();
                    Log.e("backValue", "backValue" + backValue);
                    return backValue;
                }
            }
        }
        return "";
    }

    //根据名称获知是否在集合中
    public static Location findLocation(String locationName, List<Location> locations) {
        if (locations != null && !StringUtils.isEmpty(locationName)) {
            for (int i = 0; i < locations.size(); i++) {
                if (locations.get(i).getName().startsWith(locationName)) {  //只有startsWith匹配就Ok
                    return locations.get(i);
                }
            }
        }
        return null;
    }

    public static List<Location> getLocationsByParent(Location location) {
        List<Location> childLocations;
        if (null != location) {
            childLocations = infoDatabaseHelper().loadLocationsByParentId(location.getId());
        } else {
            childLocations = new ArrayList<>();
        }
        return childLocations;
    }

    //在详细地址中查看是否包含
    public static Pair<Location, String> findLocationInAddress(String address,
                                                               List<Location> locations) {
        if (locations != null && !StringUtils.isEmpty(address)) {
            for (int i = 0; i < locations.size(); i++) {
                if (address.contains(locations.get(i).getName())) {
                    return new Pair<>(locations.get(i), locations.get(i).getName());
                }
            }
        }
        return new Pair<>(null, null);
    }

    public static Pair<Location, String> findFuzzyLocationInAddress(String address,
                                                                    List<Location> locations) {
        return findFuzzyLocationInAddress(address, locations, -1);
    }

    /**
     * 得到详细地址里的匹配项，可能会省略了后缀
     */
    public static Pair<Location, String> findFuzzyLocationInAddress(String address,
                                                                    List<Location> locations, int type) {
        if (locations != null && !StringUtils.isEmpty(address)) {
            for (int i = 0; i < locations.size(); i++) {
                String locationName = locations.get(i).getName();
                //先检查是否完全匹配，或者只是省略了后缀
                if (address.startsWith(locationName)) {
                    return new Pair<>(locations.get(i), locationName);
                }
                //兼容匹配除了省略省市后缀之外，还省略了中间部分内容的情形
                int length = locationName.length();
                for (int j = 0; j < length - 1; j++) {
                    locationName = locationName.substring(0, length - j);
                    if (address.startsWith(locationName)) {
                        return new Pair<>(locations.get(i), locationName);
                    }
                    if (type >= 0) {
                        String wipedAddress = wipeSuffix(address, type);
                        if (wipedAddress.startsWith(locationName)) {
                            return new Pair<>(locations.get(i), locationName);
                        }
                    }
                }
            }
        }
        return new Pair<>(null, null);
    }

    public static String getSimpleAddress(String address, String replace) {
        String simpleAddress = address;
        if (!StringUtils.isEmpty(address) && !StringUtils.isEmpty(replace)) {
            int index = address.indexOf(replace);
            if (index >= 0) {
                simpleAddress = address.substring(index, address.length());
            }
            simpleAddress = simpleAddress.replaceFirst(replace, "");
        }
        return simpleAddress;
    }

    /**
     * 去除掉匹配项后再去除多余的后缀(广西省南宁市 --> 省南宁市)。
     * 现有多余后缀是根据对比表来的，之后可以根据具体问题增加
     *
     * @param type 0 省 1 市 2 区
     * @see #provinceSuffix#citySuffix#districtSuffix
     */
    public static String wipeSuffix(String address, int type) {
        if (type == 0) {
            for (String surplus : provinceSuffix) {
                if (address.startsWith(surplus)) {
                    address = address.replaceFirst(surplus, "");
                }
            }
        } else if (type == 1) {
            for (String surplus : citySuffix) {
                if (address.startsWith(surplus)) {
                    address = address.replaceFirst(surplus, "");
                }
            }
        } else if (type == 2) {
            for (String surplus : districtSuffix) {
                if (address.startsWith(surplus)) {
                    address = address.replaceFirst(surplus, "");
                }
            }
        }
        //补充特殊字符去除
        for (String punctuation : punctuationSuffix) {
            while (address.startsWith(punctuation)) {
                address = address.replaceFirst(punctuation, "");
            }
        }
        return address;
    }

    /**
     * 这里先沿用旧逻辑，后续改为只存一个key，value使用json比较好
     */
    public static final String PROVINCE_KEY = "Province_key";
    public static final String CITY_KEY = "city_key";
    public static final String DISTRICT_KEY = "district_key";
    public static final String CITY_CODE_KEY = "city_code_key";

    public static final String ADDRESS_KEY = "address_key";
}
