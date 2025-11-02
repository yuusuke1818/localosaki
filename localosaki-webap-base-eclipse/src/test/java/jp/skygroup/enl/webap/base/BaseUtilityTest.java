/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.skygroup.enl.webap.base;

import org.junit.Test;

/**
 *
 * @author take_suzuki
 */
public class BaseUtilityTest {
    
    /**
     * Test of encodeAndDecode method, of class BaseUtility.
     */
    @Test
    public void testEncodeAndDecode() {

        String result = "12345678901234567890";
        String key = "osol123qweasdzxc";
        String solt = "osakitest00test";

        System.out.println(result);

        result = BaseUtility.encryptionStringAES(result, key.concat(solt));

        System.out.println(result);

        result = BaseUtility.decryptionStringAES(result, key.concat(solt));

        System.out.println(result);
    }
    
    @Test
    public void testGetSHA256() {

        String password = "password";
        String solt = "osakitest00test";

        System.out.println(BaseUtility.getSHA256(password, solt));
    }
}
