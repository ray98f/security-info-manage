package com.security.info.manage.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 流水号生成类 ：内存保存流水号增长标记，按天重置标识位
 */
class SerialNumberTool {

    private static SerialNumberTool serialNumberTool = null;
    private static SimpleDateFormat yMd = new SimpleDateFormat("yyyyMMdd");
    //用于自增长的标识
    private Integer flag;
    //用于记录日期
    private Date date;


    private SerialNumberTool() {
        if(date == null || !(yMd.format(new Date()).equals(yMd.format(date)))){
            date = new Date();
            flag = 1;
        }
    }

    /**
     * 判断是否改变
     */
    private  void checkChangeDay(){
        if(date == null || !(yMd.format(new Date()).equals(yMd.format(date)))){
            date = new Date();
            flag = 1;
        }
    }


    /**
     * 取得PrimaryGenerater的单例实现
     *
     * @return
     */
    public static SerialNumberTool getInstance() {
        if (serialNumberTool == null) {
            synchronized (SerialNumberTool.class) {
                if (serialNumberTool == null) {
                    serialNumberTool = new SerialNumberTool();
                }
            }
        }
        return serialNumberTool;
    }


    /**
     * 生成下一个编号,前缀自动补全 0
     * params:
     * 2.places 需要补全的总位数
     *
     */
    public synchronized String generaterNextNumber(int places) {
        checkChangeDay();
        StringBuffer stringBuffer = new StringBuffer();
        int numPlaces = flag.toString().length();
        //数字位数小于需要补全的总位数，需要补全0
        if(numPlaces < places ){
            for (int i = 0; i < places - numPlaces; i++) {
                stringBuffer.append("0");
            }
            stringBuffer.append(flag.toString());
        }
        else {
            stringBuffer.append(flag.toString());
        }
        flag++;
        return stringBuffer.toString();
    }

    public void operateFlag(int position){
        this.flag = position;
    }

    public static void main(String[] args) {
        for (int i = 0; i <10000 ; i++) {
            System.out.println(SerialNumberTool.getInstance().generaterNextNumber(6));
        }
    }
}

