package jp.co.osaki.sms.component;

import java.io.IOException;
import java.util.List;

import javax.el.MethodExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

@FacesComponent(createTag = true, namespace = "http://xmlns.jcp.org/jsf/component")
public class PageIndex extends UICommand {

    public static final String NBSP = "&nbsp;";   //"&#160;";

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        //actionにbindするメソッドは、パラメータ
        MethodExpression method = this.getActionExpression();

        //value値は "[現ページ]/[maxページ]"
        String value = this.getValue().toString();     //valueの値(bind先から取得
        if (!value.contains("/")) {
            throw new IOException();
        }
        String[] values = value.split("/");
        if (values.length < 2) {
            throw new IOException();
        }
        int curPage = Integer.valueOf(values[0]);
        int maxPage = Integer.valueOf(values[1]);

        //childrenの構成は、 0.ui:include  1,3,5,7,9,..HtmlCommandLink  2,4,6,8,... HtmlOutputText
        //HtmlCommandLinkは子に f:param を1つ持つ
        //HtmlCommandLinkは9個、間に入るHtmlOutputTextは8個の構成、変更する場合、使う場所で変える場合は今のところ考慮しない
        List<UIComponent> children = this.getChildren();
        List<UIComponent> grandson;

        if (this.getChildCount() < 18) {
            throw new IOException();
        }
        int i;
        for (i = 0; i < 9; i++) {
            if (!(children.get(i * 2 + 1) instanceof HtmlCommandLink)) {
                throw new IOException();
            }
        }
        for (i = 0; i < 8; i++) {
            if (!(children.get(i * 2 + 2) instanceof HtmlOutputText)) {
                throw new IOException();
            }
        }
        //表示パターン
        // 1 2 [3] 4 5 6 7 8 9                    max9ページ以下は素直に表示
        // 1 2 [3] 4 5 6 7 8 ... 10
        // 1 2 3 4 [5] 6 7 8 ... 10             カレント5以下はtopに...なし、max-cur>=5はendに...、cur+3まで表示
        // 1 ... 3 4 5 [6] 7 8 9 ... 11         カレント6以上はtopに...、cur-3から表示、
        // 1 ... 3 4 5 [6] 7 8 9 10             max-cur<5はendの...なし、max-7から表示
        // 1 ... 3 4 5 6 7 8 9 [10]

        HtmlOutputText text;
        HtmlCommandLink link;
        if (maxPage <= 9) {
            for (i = 0; i < maxPage - 1; i++) {
                text = (HtmlOutputText) children.get(i * 2 + 2);
                text.setValue(NBSP);
                text.setRendered(true);     //絞り込み解除した時に消えたままになるのでtrue設定しなおす
            }
            for (; i < 8; i++) {
                text = (HtmlOutputText) children.get(i * 2 + 2);
                text.setRendered(false);
            }
            for (i = 0; i < maxPage; i++) {
                link = (HtmlCommandLink) children.get(i * 2 + 1);
                link.setValue(i + 1);
                link.setRendered(true);
            }
            for (; i < 9; i++) {
                link = (HtmlCommandLink) children.get(i * 2 + 1);
                link.setValue(0);
                link.setRendered(false);
            }
        } else {
            //再表示
            for (i = 0; i < 8; i++) {
                text = (HtmlOutputText) children.get(i * 2 + 2);
                text.setRendered(true);
            }
            for (i = 0; i < 9; i++) {
                link = (HtmlCommandLink) children.get(i * 2 + 1);
                link.setRendered(true);
            }
            //間ちょっとあける
            for (i = 1; i < 7; i++) {
                text = (HtmlOutputText) children.get(i * 2 + 2);
                text.setValue(NBSP);
            }
            //top,endのページ表示
            link = (HtmlCommandLink) children.get(0 * 2 + 1);
            link.setValue(1);
            link = (HtmlCommandLink) children.get(8 * 2 + 1);
            link.setValue(maxPage);

            text = (HtmlOutputText) children.get(0 * 2 + 2);
            if (curPage <= 5) {
                //topの...なし、ページはend手前まで固定
                text.setValue(NBSP);
                for (i = 1; i < 8; i++) {
                    link = (HtmlCommandLink) children.get(i * 2 + 1);
                    link.setValue(i + 1);
                }
            } else {
                //topの...あり、ページはcur-3からend手前まで
                text.setValue(NBSP + "..." + NBSP);
                if (maxPage - curPage >= 5) { //endの...あり
                    int page = curPage - 3;
                    for (i = 1; i < 8; i++) {
                        link = (HtmlCommandLink) children.get(i * 2 + 1);
                        link.setValue(page + (i - 1));
                    }
                } else {  //endの...なし
                    int page = maxPage - 7;
                    for (i = 1; i < 8; i++) {
                        link = (HtmlCommandLink) children.get(i * 2 + 1);
                        link.setValue(page + (i - 1));
                    }
                }
            }
            text = (HtmlOutputText) children.get(7 * 2 + 2);
            if (maxPage - curPage >= 5) {
                text.setValue(NBSP + "..." + NBSP);
            } else {
                text.setValue(NBSP);
            }
        }
        for (i = 0; i < 9; i++) {
            link = (HtmlCommandLink) children.get(i * 2 + 1);
            link.setActionExpression(method);
            grandson = link.getChildren();
            if (grandson.get(0) instanceof UIParameter) {
                UIParameter uiparam = (UIParameter) grandson.get(0);
                uiparam.setValue(link.getValue());
            }
            if (link.getValue().equals(curPage)) {
                link.setDisabled(true);
            } else {
                link.setDisabled(false);
            }
        }
    }

}
