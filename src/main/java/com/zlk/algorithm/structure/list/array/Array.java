package com.zlk.algorithm.structure.list.array;

/**
 * 数据结构之数组（线性表）
 *
 * @author likuan.zhou
 * @date 2021/11/6/006 18:00
 */
public class Array {
   final private static int END = 5;
   private static Integer[] array = new Integer[10];
   // private int arr1[] = new int[]{1,3,5,7,9};
   // private int[] arr2 = {2, 4, 6, 8, 10};

    public static void main(String[] args) {
        print();
    }

    private static void print() {
        for (int i = 0; i < END; i++) {
            array[i] = i;
        }
        String s = "11";
        String s2 = "W25";
        for (int i = 0; i < END; i++) {
            System.out.println(array[i].getClass()+""+ array[i].hashCode());
        }
        System.out.println( s.hashCode());
        System.out.println( s2.hashCode());
    }

}
