package jp.co.osaki.osol.access.function.parser;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jp.co.osaki.osol.access.function.model.RoleModel;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * ロールリスト パーサークラス
 *
 * @author take_suzuki
 */
public class RoleListParser implements XmlParserInterface<RoleModel> {

    /**
     * ロールリストファイル
     */
    public static final String XML_FILE = "role.xml";

    /**
     * ロールタグ
     */
    private static final String ROLE = "Role";

    /**
     * ログイン情報タグ
     */
    private static final String LOGIN = "Login";

    /**
     * 操作企業情報タグ
     */
    private static final String OPERATION = "Operation";

    /**
     *
     * ロールリストのインスタンス取得
     *
     * ロールリストファイルをパースしインスタンス化する。
     *
     * @param xmlFile パースするロールリストファイル
     * @return ロールリスト
     */
    @Override
    public LinkedHashMap<String, RoleModel> parse(File xmlFile) {

        LinkedHashMap<String, RoleModel> tmpRoleList = new LinkedHashMap<>();
        RoleModel role;

        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
            Element root = document.getDocumentElement();
            NodeList roleNodeList = root.getChildNodes();
            for (int i = 0; i < roleNodeList.getLength(); i++) {
                if (S_TEXT.equals(roleNodeList.item(i).getNodeName())) {
                    continue;
                }
                if (ROLE.equals(roleNodeList.item(i).getNodeName())) {
                    role = new RoleModel();
                    NamedNodeMap roleAttr = roleNodeList.item(i).getAttributes();
                    for (int x = 0; x < roleAttr.getLength(); x++) {
                        switch (roleAttr.item(x).getNodeName()) {
                            case KEY:
                                role.setKey(roleAttr.item(x).getTextContent());
                                break;
                            case NAME:
                                role.setName(roleAttr.item(x).getTextContent());
                                break;
                            default:
                        }
                    }
                    NodeList roleNodes = roleNodeList.item(i).getChildNodes();
                    for (int j = 0; j < roleNodes.getLength(); j++) {
                        if (S_TEXT.equals(roleNodes.item(j).getNodeName())) {
                            continue;
                        }
                        if (LOGIN.equals(roleNodes.item(j).getNodeName())) {
                            role.setLogin(roleNodes.item(j).getTextContent());
                        }
                        if (OPERATION.equals(roleNodes.item(j).getNodeName())) {
                            role.setOperation(roleNodes.item(j).getTextContent());
                        }
                    }
                    tmpRoleList.put(role.getKey(), role);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }

        eventLogger.debug(this.getClass().getName() + " : " + xmlFile.getAbsolutePath() + " => Count : " + tmpRoleList.size());
        return tmpRoleList;
    }

}
