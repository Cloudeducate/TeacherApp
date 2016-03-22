package com.cloudeducate.redtick.Model;

/**
 * Created by abhishek on 09-02-2016.
 */
public class Remark_model {
    String[] remarks;

    public String getstudentremark(int pos){ return remarks[pos];}

    public void setstudentremark(String remark,int pos){ remarks[pos]=remark;}

}
