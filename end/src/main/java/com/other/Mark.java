package com.other;

/**
 * Created by ymy on 2018/10/23.
 */
public class Mark {
    public int mark;//1代表蓝色矩形 0代表紫色圆圈
    public String endDate;
    public String startDate;
    public String taskName;
    public Mark(int mark, String endDate, String startDate, String taskName) {
        this.mark = mark;
        this.endDate = endDate;
        this.startDate = startDate;
        this.taskName = taskName;
    }

}
