package com.zlk.algorithm.data.sort.mergersort;

/**desc:归并排序
 * @author  zhoulk
 * date: 2019/5/27
 */
public class MergerSort <T>{
    /**
     * 描述：
     */

    public static void main(String[] args) {
        //Integer[] array = RandomArray.getArray(1, 1000, 5000);
        Integer[] array = {1,5,7,9,2,3};
        MergerSort mergerSort = new MergerSort();
        mergerSort.mergerSoret(array,0,array.length-1);
        for (Integer v:array) {
            System.out.print(v+",");
        }
    }

    /**
     * 归并排序入口
     * @param arr 需要排序部分
     * @param left 开始左下标
     * @param right 结束右下标
     */
    public  void mergerSoret(Integer[] arr,int left,int right){
        //没找到，或者已经只有一个元素，当前块排序结束
        if(left>=right){
            return;
        }

        //中间位置
        int mid = (left+right)/2;
        //递归左半部分
        mergerSoret(arr,left,mid);
        //递归右半部分
        mergerSoret(arr,mid+1,right);
        //两部分归并
        merger(arr,left,mid,right);
    }

    /**
     * 对当前递归到的部分进行排序
     * @param arr 当前块数组
     * @param left 左下标
     * @param mid 中间位置
     * @param right 右下标
     */
    public void  merger(Integer[] arr,int left,int mid,int right){

        //声明一个数组，用来暂时存放排序数组,和arr空间一样大
        Integer[] aux = new Integer[right-left+1];
        //aux需要偏移left（保证位置不变），将arr复制给aux
        for(int i=left;i<=right;i++){
            aux[i-left] = arr[i];
        }

        //记录左边与右边的取值位置
        int i =left,j=mid+1;
        //将aux排序后重新赋值给arr
        for(int k=left;k<=right;k++){
            if(i>mid){
                //左边无，右边有
                arr[k] = aux[j-left];
                j++;
            }else if(j>right){
                //右边无，左边有
                arr[k] = aux[i-left];
                i++;
            }else if(aux[i-left]<aux[j-left]){
                arr[k] = aux[i-left];
                i ++;
            }else{
                arr[k] = aux[j-left];
                j ++;
            }
        }
    }
}
