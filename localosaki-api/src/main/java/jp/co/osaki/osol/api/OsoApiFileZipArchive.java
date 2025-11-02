package jp.co.osaki.osol.api;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import jp.skygroup.enl.webap.base.BaseFileZipArchive;

/**
 *
 * osolファイルZIPアーカイブ
 *
 */
public class OsoApiFileZipArchive extends BaseFileZipArchive {

    private static final int COMPRESSION_LEVEL = 5;

    private static final String SJIS = "SJIS";

    /**
     * 指定ディレクトリ内のファイルをZIPアーカイブし、指定されたパスに作成
     *
     * @param inputDirectoryPath 圧縮するディレクトリ ( 例; C:/sample )
     * @param zipFilePath 圧縮後の出力ファイル名をフルパスで指定 ( 例: C:/sample.zip )
     * @return 処理結果 true:圧縮成功 false:圧縮失敗
     */
    public static boolean compressDirectory(String inputDirectoryPath, String zipFilePath){

        Charset charset;
        charset = StandardCharsets.UTF_8;
        return compressDirectory(inputDirectoryPath, zipFilePath, charset, COMPRESSION_LEVEL);
    }
    /**
     * 指定ArrayListのファイルをZIPアーカイブし、指定されたパスに作成
     *
     * @param inputFileList 圧縮するファイルリスト  ( 例; {C:/sample1.txt, C:/sample2.txt} )
     * @param zipFilePath 圧縮後のファイル名をフルパスで指定 ( 例: C:/sample.zip )
     * @return 処理結果 true:圧縮成功 false:圧縮失敗
     */
    public static boolean compressFileList(List<String> inputFileList, String zipFilePath) {

        Charset charset;
        if (Charset.isSupported(SJIS)){
            charset = Charset.forName(SJIS);
        } else {
            charset = StandardCharsets.UTF_8;
        }
        return compressFileList(inputFileList, zipFilePath, charset, COMPRESSION_LEVEL);
    }
}
