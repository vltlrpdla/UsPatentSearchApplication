package project.jeonghoon.com.uspatentsearchapplication;

import android.graphics.drawable.Drawable;

/**
 * Created by jeonghoon on 2016-07-14.
 */

//rest open api로 자신이 사용할 객체 선정 속성으로는 어떤 것을 쓸 것인지 미리 구상하고 받아올 값들을 선정
public class SearchItem {

    private String ltrtno;
    private String inventionName;
    private String applicationDate;
    private String applicationNo;
    private String contryCode;
    private String applicant;
    private String inventors;

    private Drawable Icon;


    public SearchItem(){
    }

    public SearchItem(String ltrtno, String inventionName,String applicationDate,String applicationNo,String contryCode, String applicant, String inventors){
        this.ltrtno = ltrtno;
        this.inventionName = inventionName;
        this.applicationDate = applicationDate;
        this.applicationNo = applicationNo;
        this.contryCode = contryCode;
        this.applicant = applicant;
        this.inventors = inventors;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getInventors() {
        return inventors;
    }

    public void setInventors(String inventors) {
        this.inventors = inventors;
    }

    public String getContryCode() {
        return contryCode;
    }

    public void setContryCode(String contryCode) {
        this.contryCode = contryCode;
    }

    public String getLtrtno() {
        return ltrtno;
    }

    public void setLtrtno(String ltrtno) {
        this.ltrtno = ltrtno;
    }

    public String getInventionName() {
        return inventionName;
    }

    public void setInventionName(String inventionName) {
        this.inventionName = inventionName;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public Drawable getIcon() {
        return Icon;
    }

    public void setIcon(Drawable mIcon) {
        this.Icon = mIcon;
    }


    public int compareTo(SearchItem other) {
        if (ltrtno.equals(other.getLtrtno())) {
            return -1;
        } else if (inventionName.equals(other.getInventionName())) {
            return -1;
        } else if (applicationDate.equals(other.getApplicationDate())) {
            return -1;
        } else if (applicationNo.equals(other.getApplicationNo())) {
            return -1;
        }

        return 0;
    }
}
