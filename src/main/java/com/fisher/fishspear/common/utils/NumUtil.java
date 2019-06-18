package com.fisher.fishspear.common.utils;

import java.math.BigDecimal;

/**
 * 数字工具类
 */
public class NumUtil {

    /**
     * 四舍五入并保留n位小数的字符串结果
     */
    public static String round(Object num, int scale) {
        try {
            Double temp = new Double(num + "");
            return new BigDecimal(temp).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 输入一个整数 返回该整数的大写写法 二十以下，不包括二十
     */
    public static String getCnNum(int num){
        String[] cnNums = {"一","二","三","四","五","六","七","八","九","十"};
        if(num > 10){
            int i = num - 10;
            return "十" + cnNums[i-1];
        }else{
            return cnNums[num - 1];
        }
    }

    public static void main(String[] args) {
        int[] ints = {1,2,10,12,19,20};
        for (int anInt : ints) {
            String cnNum = getCnNum(anInt);
            System.out.println(cnNum);
        }
    }
}
