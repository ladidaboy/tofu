package cn.hl.ox._feature.v8;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author hyman
 * @date 2019-07-16 18:40:21
 * @version $ Id: Tester4Base64.java, v 0.1  hyman Exp $
 */
public class Tester4Base64 {
    public static void main(String[] args) {
        final String text = "Base64 finally in Java 8!";

        final String encoded = Base64
                .getEncoder()
                .encodeToString( text.getBytes( StandardCharsets.UTF_8 ) );
        System.out.println( encoded );

        final String decoded = new String(
                Base64.getDecoder().decode( encoded ),
                StandardCharsets.UTF_8 );
        System.out.println( decoded );
    }

    /*
     新的Base64API也支持URL和MINE的编码解码。
     (Base64.getUrlEncoder() / Base64.getUrlDecoder(), Base64.getMimeEncoder() / Base64.getMimeDecoder())。

     BASE64不是用来加密的，是BASE64编码后的字符串，全部都是由标准键盘上面的常规字符组成，
     这样编码后的字符串在网关之间传递不会产生UNICODE字符串不能识别或者丢失的现象。
     你再仔细研究下EMAIL就会发现其实EMAIL就是用base64编码过后再发送的。然后接收的时候再还原。
     */
}
