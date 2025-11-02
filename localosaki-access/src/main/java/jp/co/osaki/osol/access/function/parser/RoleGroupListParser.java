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

import jp.co.osaki.osol.access.function.model.RoleGroupModel;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * ロールグループリスト パーサークラス
 *
 * @author take_suzuki
 */
public class RoleGroupListParser implements XmlParserInterface<RoleGroupModel> {

    /**
     * ロールグループリストファイル
     */
    public static final String XML_FILE = "rolegroup.xml";

    /**
     * ロールグループタグ
     */
    private static final String ROLE_GROUP = "RoleGroup";

    /**
     * ロールタグ
     */
    private static final String ROLE = "Role";

    /**
     *
     * ロールグループリスト取得
     *
     * ロールグループファイルをパースしインスタンス化する。
     *
     * @param xmlFile パースするロールグループファイル
     * @return ロールグループリスト
     */
    @Override
    public LinkedHashMap<String, RoleGroupModel> parse(File xmlFile) {

        LinkedHashMap<String, RoleGroupModel> tmpRoleGroupList = new LinkedHashMap<>();
        RoleGroupModel roleGroup;

        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
            Element root = document.getDocumentElement();
            NodeList roleGroupList = root.getChildNodes();
            for (int i = 0; i < roleGroupList.getLength(); i++) {
                if (S_TEXT.equals(roleGroupList.item(i).getNodeName())) {
                    continue;
                }
                if (ROLE_GROUP.equals(roleGroupList.item(i).getNodeName())) {
                    roleGroup = new RoleGroupModel();
                    NamedNodeMap roleGroupAttr = roleGroupList.item(i).getAttributes();
                    for (int x = 0; x < roleGroupAttr.getLength(); x++) {
                        switch (roleGroupAttr.item(x).getNodeName()) {
                            case KEY:
                                roleGroup.setKey(roleGroupAttr.item(x).getTextContent());
                                break;
                            case NAME:
                                roleGroup.setName(roleGroupAttr.item(x).getTextContent());
                                break;
                            default:
                                break;
                        }
                    }
                    NodeList roleGroupNodes = roleGroupList.item(i).getChildNodes();
                    for (int j = 0; j < roleGroupNodes.getLength(); j++) {
                        if (S_TEXT.equals(roleGroupNodes.item(j).getNodeName())) {
                            continue;
                        }
                        if (ROLE.equals(roleGroupNodes.item(j).getNodeName())) {
                            roleGroup.getRoleList().add(roleGroupNodes.item(j).getTextContent());
                        }
                    }
                    tmpRoleGroupList.put(roleGroup.getKey(), roleGroup);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }

        eventLogger.debug(this.getClass().getName() + " : " + xmlFile.getAbsolutePath() + " => Count : " + tmpRoleGroupList.size());
        return tmpRoleGroupList;
    }

}
