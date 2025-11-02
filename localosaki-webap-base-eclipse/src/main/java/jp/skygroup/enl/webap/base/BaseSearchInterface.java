package jp.skygroup.enl.webap.base;

import java.util.List;
import java.util.Map;

public interface BaseSearchInterface<T> {
    //最初に選択済み状態で表示しておく条件の一覧を設定
    public void initialConditionList(List<T> list);
    //未選択状態の条件（「条件を追加」）の作成
    public T createDefaultCondition();
    //選択した条件による表示アイテムの更新
    public void updateCondition(T condition);
    //選択肢の一覧
    public Map<String, String> initialConditionMap();
}
