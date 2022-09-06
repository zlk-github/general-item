package com.zlk.jdk.jdk8;

import java.net.URLEncoder;

/**
 * @author likuan.zhou
 * @title: stremTest
 * @projectName general-item
 * @description: TODO
 * @date 2021/9/8/008 13:29
 */
public class stremTest {
    public static void main(String[] args) {
        // 使用 URLEncoder 库对字符串进行 utf-8 编码


            String ret = "default";
            try {
                ret = URLEncoder.encode("{\n" +
                        "    \"clientid\":\"b030c9\",\n" +
                        "    \"password\":\"CD1DD499F2DF9156429D1250E317CBFF\",\n" +
                        "    \"mobile\":\"17611325116\",\n" +
                        "    \"smstype\":\"0\",\n" +
                        "    \"content\":\"【yeeet】测试短信10301\"\n" +
                        "}", "utf-8");
                System.out.println( "=====" + ret);
            }catch(Exception e) {
                System.out.println(e);
            }



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

        //最大值
        //Long maxId = shelvesNotSignDtoList.stream().max(Comparator.comparing(ShelvesNotSignDto::getId)).get().getId();

        // 字段去重复
        // Map<String, EmailSendRecord> emailSendRecordMaps = emailSendRecordList.stream().filter(distinctByKey(EmailSendRecord::getWaybillCode)).collect(Collectors.toMap(EmailSendRecord::getWaybillCode, emailSendRecord -> emailSendRecord));

        // 发件时间小于到件时间中，找最大
        /*Optional<SendScan> optional = sendScanList.stream().filter(sendScan2 -> sendScan2.getWaybillCode().equals(arrivalScan.getWaybillCode())
                && arrivalScan.getOperationTime().compareTo(sendScan2.getOperationTime()) > 0)*/
    }
}
