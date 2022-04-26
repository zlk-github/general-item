package com.zlk.jdk.jdk8;

/**
 * @author likuan.zhou
 * @title: stremTest
 * @projectName general-item
 * @description: TODO
 * @date 2021/9/8/008 13:29
 */
public class stremTest {
    public static void main(String[] args) {
           //key string
          //basicConfigDtoList.stream().collect(Collectors.toMap(BasicConfigDto::getCode, LocalFieldUtil::getFieldVal));
          //key List<object>
          /* Map<String, List<BasicConfigDto>> basicTypeMap = basicConfigDtoList.stream().collect(Collectors.groupingBy(BasicConfigDto::getBasicTypeCode));

         //key object
        Map<String, BasicConfigVo> dictMap = basicConfigVoList.stream().collect(Collectors.toMap(BasicConfigVo::getCode, dict -> dict));*/
        // LocalFieldUtil.getFieldVal()
       // Map<String, String> configVoMap = basicConfigVoList.stream().collect(Collectors.toMap(BasicConfigVo::getCode, BasicConfigVo::getArName));

        // 获取部分字段转map
       // List<WaybillReceiverDto> collect = list.stream().map(DispatchScanDto::getWaybillReceiver).collect(Collectors.toList());

        // cityList.stream().collect(Collectors.toMap(City::getCode, city -> {
        //                return city.getEnName();
        //            }));
    }
}
