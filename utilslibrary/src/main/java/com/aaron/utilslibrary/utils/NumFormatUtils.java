package com.aaron.utilslibrary.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * 数字处理工具
 * Created by Aaron on 2017/8/23.
 */

public class NumFormatUtils {
    private NumFormatUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

//    ROUND_CEILING -------- 如果 BigDecimal 是正的，则做 ROUND_UP 操作；如果为负，则做 ROUND_DOWN 操作。
//    ROUND_DOWN ----- 从不在舍弃(即截断)的小数之前增加数字。
//    ROUND_FLOOR ----- 如果 BigDecimal 为正，则作 ROUND_UP ；如果为负，则作 ROUND_DOWN 。
//    ROUND_HALF_DOWN ----- 若舍弃部分> .5，则作 ROUND_UP；否则，作 ROUND_DOWN 。
//    ROUND_HALF_EVEN ----- 如果舍弃部分左边的数字为奇数，则作 ROUND_HALF_UP ；如果它为偶数，则作 ROUND_HALF_DOWN 。
//    ROUND_HALF_UP ----- 若舍弃部分>=.5，则作 ROUND_UP ；否则，作 ROUND_DOWN 。
//    ROUND_UNNECESSARY ----- 该“伪舍入模式”实际是指明所要求的操作必须是精确的，，因此不需要舍入操作。
//    ROUND_UP ----- 总是在非 0  舍弃小数(即截断)之前增加数字。

    /**
     * 保留n位小数
     * BigDecimal 四舍五入
     *
     * @param num   需要格式化的数字
     * @param count 保留小数位个数
     * @return
     */
    public static float getFractionDigits(float num, int count) {
        BigDecimal b = new BigDecimal(num);
        float f1 = b.setScale(count, BigDecimal.ROUND_HALF_UP).floatValue();//表明四舍五入，保留两位小数
        return f1;
    }

    /**
     * 保留n位小数
     * DecimalFormat 文本格式化
     *
     * @param num   需要格式化的数字
     * @param count 保留小数位个数
     * @return
     */
    public static String getFractionDigits2(float num, int count) {
        String str = "##0.";
        StringBuilder builder = new StringBuilder(str);
        for (int i = 0; i < count; i++) {
            builder.append("0");
        }
        DecimalFormat fnum = new DecimalFormat(builder.toString());//或###,###.##
        return fnum.format(num);
    }

    /**
     * 保留n位小数
     * NumberFormat 文本格式化
     *
     * @param num   需要格式化的数字
     * @param count 保留小数位个数
     * @return
     */
    public static String getFractionDigits3(float num, int count) {
        NumberFormat form = NumberFormat.getCurrencyInstance();//建立货币格式化引用
        form.setMinimumIntegerDigits(1);  //整数位最少位数
        form.setMinimumFractionDigits(2); // 小数位最少位数
        form.setMaximumFractionDigits(count); // 小数位最多位数
        return form.format(num);
    }

    /**
     * 获取百分比数字
     * NumberFormat 文本格式化
     *
     * @param num   需要格式化的数字
     * @param count 保留小数位个数
     * @return
     */
    public static String getPercentNum(BigDecimal num, int count) {
        NumberFormat form = NumberFormat.getPercentInstance();//建立百分比格式化引用
        form.setMinimumIntegerDigits(1);  //整数位最少位数
        form.setMinimumFractionDigits(0); // 小数位最少位数
        form.setMaximumFractionDigits(count); // 小数位最多位数
        return form.format(num);
    }

    /**
     * 利息计算
     *
     * @return
     */
    public static void compute() {
        NumberFormat currency = NumberFormat.getCurrencyInstance(); //建立货币格式化引用
        NumberFormat percent = NumberFormat.getPercentInstance();  //建立百分比格式化引用
        percent.setMaximumFractionDigits(3); //百分比小数点最多3位
        BigDecimal loanAmount = new BigDecimal("15000.48"); //贷款金额
        BigDecimal interestRate = new BigDecimal("0.008"); //利率
        BigDecimal interest = loanAmount.multiply(interestRate); //相乘，BigDecimal都是不可变的（immutable）的，在进行每一步运算时，都会产生一个新的对象，所以在做加减乘除运算时千万要保存操作后的值。
        System.out.println("贷款金额:\t" + currency.format(loanAmount));
        System.out.println("利率:\t" + percent.format(interestRate));
        System.out.println("利息:\t" + currency.format(interest));
    }

    /**
     * 注意BigDecimal构造函数，优先使用字符串构造函数
     */
    public static void attention() {
        BigDecimal aDouble = new BigDecimal(1.22);
        System.out.println("construct with a double value: " + aDouble);
        BigDecimal aString = new BigDecimal("1.22");
        System.out.println("construct with a String value: " + aString);
    }

    /**
     * 数字转中文单位
     * 例如：300.25
     * 转成中文金钱字符串：叁佰元贰角伍分
     *
     * @param value
     * @return
     */
    public static String numToRMB(double value) {
        char[] hunit = {'拾', '佰', '仟'}; // 段内位置表示
        char[] vunit = {'万', '亿'}; // 段名表示
        char[] digit = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'}; // 数字表示
        BigDecimal midVal = new BigDecimal(Math.round(value * 100)); // 转化成整形
        String valStr = String.valueOf(midVal); // 转化成字符串
        String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
        String rail = valStr.substring(valStr.length() - 2); // 取小数部分
        String prefix = ""; // 整数部分转化的结果
        String suffix = ""; // 小数部分转化的结果
        // 处理小数点后面的数
        if (rail.equals("00")) { // 如果小数部分为0
            suffix = "整";
        } else if ((rail.charAt(0) - '0') > 0 && (rail.charAt(1) - '0') == 0) {
            suffix = digit[rail.charAt(0) - '0'] + "角";// 把角转化出来
        } else if ((rail.charAt(0) - '0') == 0
                && (rail.charAt(1) - '0') > 0) {
            suffix = digit[rail.charAt(1) - '0'] + "分";// 把角转化出来
        } else {
            suffix = digit[rail.charAt(0) - '0'] + "角"
                    + digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
        }
        // 处理小数点前面的数
        char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
        boolean preZero = false; // 标志当前位的上一位是否为有效0位（如万位的0对千位无效）
        byte zeroSerNum = 0; // 连续出现0的次数
        for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
            int idx = (chDig.length - i - 1) % 4; // 取段内位置
            int vidx = (chDig.length - i - 1) / 4; // 取段位置
            if (chDig[i] == '0') { // 如果当前字符是0
                preZero = true;
                zeroSerNum++; // 连续0次数递增
                if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
                    prefix += vunit[vidx - 1];
                    preZero = false; // 不管上一位是否为0，置为无效0位
                }
            } else {
                zeroSerNum = 0; // 连续0次数清零
                if (preZero) { // 上一位为有效0位
                    prefix += digit[0]; // 只有在这地方用到'零'
                    preZero = false;
                }
                prefix += digit[chDig[i] - '0']; // 转化该数字表示
                if (idx > 0)
                    prefix += hunit[idx - 1];
                if (idx == 0 && vidx > 0) {
                    prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
                }
            }
        }

        if (prefix.length() > 0)
            prefix += '元'; // 如果整数部分存在,则有圆的字样
        return prefix + suffix; // 返回正确表示
    }

    public static void main(String[] args) {
        float t = 433.335666f;
        System.out.println(getFractionDigits(t, 2));
        System.out.println(getFractionDigits2(t, 2));
        System.out.println(getFractionDigits3(t, 2));
        System.out.println(getPercentNum(new BigDecimal(t), 2));
        compute();
        attention();

//        433.34
//        433.34
//        ￥433.34
//        43,333.57%
//        贷款金额:	￥15,000.48
//        利率:	0.8%
//        利息:	￥120.00
//        construct with a double value: 1.2199999999999999733546474089962430298328399658203125
//        construct with a String value: 1.22
    }

}
