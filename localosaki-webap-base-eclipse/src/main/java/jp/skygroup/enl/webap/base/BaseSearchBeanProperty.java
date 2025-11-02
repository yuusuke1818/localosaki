package jp.skygroup.enl.webap.base;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Named(value="baseSearchBeanProperty")
@Dependent
public class BaseSearchBeanProperty<T> implements Serializable {

    private static final long serialVersionUID = -5698940567919487899L;
    
    private Map<String, String> selectConditionMap;
    
    private List<T> conditionList;

    public void resetConditionList(){
        conditionList = null;
    }
            
    //今の選択済み条件一覧
    public List<T> getConditionList(BaseSearchInterface<T> i) {
        if (conditionList == null) {
            conditionList = new LinkedList<>();
            //初期配置条件
            i.initialConditionList(conditionList);
            //「条件を追加」
            conditionList.add(i.createDefaultCondition());
            updateConditionMap(i, conditionList.size()-1);
            
            resetOrder();
        }
        return conditionList;
    }
    
    public int getConditionListSize(){
        if (conditionList == null) {
            return 0;
        }
        return conditionList.size();
    }

    //今の選択済み条件を考慮した、選択可能条件項目
    public Map<String, String> getSelectConditionMap(BaseSearchInterface<T> i) {
        if(selectConditionMap == null){
            selectConditionMap = i.initialConditionMap();
        }
        Map<String, String> map = new LinkedHashMap<>(selectConditionMap);
        if(conditionList != null){
            for(T condition : conditionList){
                if(condition instanceof BaseSearchCondition){
                    BaseSearchCondition base = (BaseSearchCondition)condition;
                    if (base.isMultiSelectEnable()) {
                        continue;
                    }
                    map.remove(base.getSelectConditionCd());
                }
            }
        }
        return map;
    }

    public void deleteCondition(BaseSearchInterface<T> i, String orderNo) {
        if(conditionList.size() == selectConditionMap.size()-1 ){
            //選択肢を使い果たした状態から1個削除したら「条件を追加」を復活させる
            T condition = conditionList.get(conditionList.size()-1);
            if(condition instanceof BaseSearchCondition){
                BaseSearchCondition base = (BaseSearchCondition)condition;
                if ( !base.isDefaultCondition() ) {
                    conditionList.add(i.createDefaultCondition());
                }
            }
        }
        conditionList.remove(Integer.parseInt(orderNo));
        updateConditionMap(i, conditionList.size()-1);
        resetOrder();
    }

    public void updateConditionMap(BaseSearchInterface<T> i, int order){
        //条件選択肢の更新
        T condition = conditionList.get(order);
        if(condition instanceof BaseSearchCondition){
            BaseSearchCondition base = (BaseSearchCondition)condition;
            base.setSelectConditionMap(getSelectConditionMap(i));
        }
    }
    
    public void resetOrder(){
        for (int i = 0; i < conditionList.size(); i++) {
            ((BaseSearchCondition)conditionList.get(i)).setOrderNo(i);
        }
    }
    
    public void updateCondition(BaseSearchInterface<T> i, int order){
        // 検索条件リストがnull, 空, 要素外の番号のリクエストがきた場合、何もしない
        if (conditionList == null
                || conditionList.isEmpty()
                || conditionList.size() <= order) {
            return;
        }

        // 選択された条件がnullまたは初期値の場合、何もしない
        T condition = conditionList.get(order);
        if(condition instanceof BaseSearchCondition){
            BaseSearchCondition base = (BaseSearchCondition)condition;
            if (base.getSelectConditionCd() == null
                    || base.isDefaultCondition() ) {
                return;
            }
            i.updateCondition(condition);
            
            if (conditionList.size() - 1 == order) {
                //更新したのが最後の1つ
                boolean multiSelectEnabledFlg = hasMultiSelectCondition();
                if(multiSelectEnabledFlg || conditionList.size() < selectConditionMap.size()-1){
                    //選択可能な条件がまだ残っていたら「条件を追加」を追加
                    conditionList.add(i.createDefaultCondition());
                    updateConditionMap(i, conditionList.size()-1);
                }
            }
            resetOrder();
        }
    }

    /**
     * 検索条件の中に複数選択可能な選択肢が存在するかどうかを確認する
     * @return
     */
    private boolean hasMultiSelectCondition() {
        if (conditionList == null || conditionList.isEmpty()) {
            return false;
        }

        for (T condition : conditionList) {
            if(condition instanceof BaseSearchCondition){
                if (((BaseSearchCondition) condition).isMultiSelectEnable()) {
                    return true;
                }
            }
        }
        return false;
    }
}
