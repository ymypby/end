package com.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by ymy on 2018/10/5.
 */
@Entity
@Table(name = "B_STATIONINFO_BRIEF")
public class B_STATIONINFO_BRIEF {
    @Id
    private String STATIONID;
    private String STATIONNAME;
    private BigDecimal X;

    public BigDecimal getX() {
        return X;
    }

    public void setX(BigDecimal x) {
        X = x;
    }

    public BigDecimal getY() {
        return Y;
    }

    public void setY(BigDecimal y) {
        Y = y;
    }

    public BigDecimal getBAIDU_X() {
        return BAIDU_X;
    }

    public void setBAIDU_X(BigDecimal BAIDU_X) {
        this.BAIDU_X = BAIDU_X;
    }

    public BigDecimal getBAIDU_Y() {
        return BAIDU_Y;
    }

    public void setBAIDU_Y(BigDecimal BAIDU_Y) {
        this.BAIDU_Y = BAIDU_Y;
    }

    private BigDecimal Y;
    private BigDecimal BAIDU_X;
    private BigDecimal BAIDU_Y;
    private String ADDRESS;
    private String SERVICETIME;

    public String getSTATIONID() {
        return STATIONID;
    }

    public void setSTATIONID(String STATIONID) {
        this.STATIONID = STATIONID;
    }

    public String getSTATIONNAME() {
        return STATIONNAME;
    }

    public void setSTATIONNAME(String STATIONNAME) {
        this.STATIONNAME = STATIONNAME;
    }



    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getSERVICETIME() {
        return SERVICETIME;
    }

    public void setSERVICETIME(String SERVICETIME) {
        this.SERVICETIME = SERVICETIME;
    }


}
