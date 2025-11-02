package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;

@Dependent
public class HopIdHistoryProperty implements Serializable {

    private static final long serialVersionUID = 5527799051685596212L;

    private String hop1Id;

    private String hop2Id;

    private String hop3Id;

    private String isFocusHop1Id;

    private String isFocusHop2Id;

    private String isFocusHop3Id;

    private List<String> hopIdList;

    public String getHop1Id() {
        return hop1Id;
    }

    public void setHop1Id(String hop1Id) {
        this.hop1Id = hop1Id;
    }

    public String getHop2Id() {
        return hop2Id;
    }

    public void setHop2Id(String hop2Id) {
        this.hop2Id = hop2Id;
    }

    public String getHop3Id() {
        return hop3Id;
    }

    public void setHop3Id(String hop3Id) {
        this.hop3Id = hop3Id;
    }

    public List<String> getHopIdList() {
        return hopIdList;
    }

    public void setHopIdList(List<String> hopIdList) {
        this.hopIdList = hopIdList;
    }

    public String getIsFocusHop1Id() {
        return isFocusHop1Id;
    }

    public void setIsFocusHop1Id(String isFocusHop1Id) {
        this.isFocusHop1Id = isFocusHop1Id;
    }

    public String getIsFocusHop2Id() {
        return isFocusHop2Id;
    }

    public void setIsFocusHop2Id(String isFocusHop2Id) {
        this.isFocusHop2Id = isFocusHop2Id;
    }

    public String getIsFocusHop3Id() {
        return isFocusHop3Id;
    }

    public void setIsFocusHop3Id(String isFocusHop3Id) {
        this.isFocusHop3Id = isFocusHop3Id;
    }
}
