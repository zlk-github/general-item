package com.zlk.algorithm.common.sort.util;

import java.util.Random;

/**随机数组
 * @author  zhoulk
 * date: 2019/5/20 0020
 */
public class RandomArray<T> {

    /**
     * 根据最范围生成随机数组
     * @param min 最小下限
     * @param max 最大上限
     * @param num 个数
     * @return 数组
     */
    public static Integer[] getArray(Integer min,Integer max,Integer num){
        Integer[] arr = new Integer[num];
        Random rand = new Random();
        for(int i = 0;i<num;i++){
            arr[i] = rand.nextInt(max-min+1)+min;
        }
         return  arr;
    }

    public static void main(String[] args) {
        Integer[] array = RandomArray.getArray(1, 10000, 5000);
        for (Object arr: array) {
            System.out.print(arr+",");
        }
    }
}
