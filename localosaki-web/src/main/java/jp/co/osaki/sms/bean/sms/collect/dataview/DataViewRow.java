package jp.co.osaki.sms.bean.sms.collect.dataview;

import java.util.ArrayList;
import java.util.List;

/**
 * データ収集装置 データ表示機能 表示行データクラス.
 *
 * @author ozaki.y
 */
public class DataViewRow {

    /** 行タイトル. */
    private String title;

    /** 表示列データList. */
    private List<DataViewColumn> columnDataList;

    /** フッターフラグ. */
    private boolean isFooter;

    public DataViewRow(String title) {
        setTitle(title);
        setColumnDataList(new ArrayList<>());
    }

    public void addColumnData(DataViewColumn columnData) {
        getColumnDataList().add(columnData);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DataViewColumn> getColumnDataList() {
        return columnDataList;
    }

    public void setColumnDataList(List<DataViewColumn> columnDataList) {
        this.columnDataList = columnDataList;
    }

    public boolean isFooter() {
        return isFooter;
    }

    public void setFooter(boolean isFooter) {
        this.isFooter = isFooter;
    }
}