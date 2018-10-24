package com.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by ymy on 2018/10/5.
 */
@Entity
@Table(name = "B_LEASEINFOHIS_BRIEF")
public class B_LEASEINFOHIS_BRIEF {
    public int getGENERATEDID() {
        return GENERATEDID;
    }

    public void setGENERATEDID(int GENERATEDID) {
        this.GENERATEDID = GENERATEDID;
    }

    @Id
    private int GENERATEDID;
    private String ID;
    private String CARDNO;
    private String BIKEID;
    private String LEASESTATION;
    private String LEASETIME;
    private String LEASEDAY;
    private String LEASEHOUR;
    private String RETURNSTATION;
    private String RETURNTIME;
    private String RETURNDAY;
    private String RETURNHOUR;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCARDNO() {
        return CARDNO;
    }

    public void setCARDNO(String CARDNO) {
        this.CARDNO = CARDNO;
    }

    public String getBIKEID() {
        return BIKEID;
    }

    public void setBIKEID(String BIKEID) {
        this.BIKEID = BIKEID;
    }

    public String getLEASESTATION() {
        return LEASESTATION;
    }

    public void setLEASESTATION(String LEASESTATION) {
        this.LEASESTATION = LEASESTATION;
    }

    public String getLEASETIME() {
        return LEASETIME;
    }

    public void setLEASETIME(String LEASETIME) {
        this.LEASETIME = LEASETIME;
    }

    public String getLEASEDAY() {
        return LEASEDAY;
    }

    public void setLEASEDAY(String LEASEDAY) {
        this.LEASEDAY = LEASEDAY;
    }

    public String getLEASEHOUR() {
        return LEASEHOUR;
    }

    public void setLEASEHOUR(String LEASEHOUR) {
        this.LEASEHOUR = LEASEHOUR;
    }

    public String getRETURNSTATION() {
        return RETURNSTATION;
    }

    public void setRETURNSTATION(String RETURNSTATION) {
        this.RETURNSTATION = RETURNSTATION;
    }

    public String getRETURNTIME() {
        return RETURNTIME;
    }

    public void setRETURNTIME(String RETURNTIME) {
        this.RETURNTIME = RETURNTIME;
    }

    public String getRETURNDAY() {
        return RETURNDAY;
    }

    public void setRETURNDAY(String RETURNDAY) {
        this.RETURNDAY = RETURNDAY;
    }

    public String getRETURNHOUR() {
        return RETURNHOUR;
    }

    public void setRETURNHOUR(String RETURNHOUR) {
        this.RETURNHOUR = RETURNHOUR;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if(this == obj){
            return true;
        }
        if(obj instanceof B_LEASEINFOHIS_BRIEF){
            if(this.getLEASESTATION().equals(((B_LEASEINFOHIS_BRIEF) obj).getLEASESTATION()) && this.getRETURNSTATION().equals (((B_LEASEINFOHIS_BRIEF) obj).getRETURNSTATION())){
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode(){
        return LEASESTATION.hashCode()*RETURNSTATION.hashCode();
    }
}
