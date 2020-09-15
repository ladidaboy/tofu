package cn.hl.kit.ox.string;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KeywordUtils {
    private static final char                        endTag    = (char) (1); // 关键词结束符
    private static       HashMap<Character, HashMap> filterMap = new HashMap<>(1024);
    // ---------------------------------------------------------------------------------------------------------------------

    /**
     * 从指定的文件中加载关键字
     * @param filename -- 关键字文件名
     * @throws Exception
     */
    public static void loadKeyword(String filename) throws Exception {
        try {
            InputStream is = KeywordUtils.class.getResourceAsStream(filename);
            if (is == null) {
                File f = new File(filename);
                if (f.exists()) {
                    is = new FileInputStream(f);
                } else {
                    throw new FileNotFoundException("Keyword file not found!");
                }
            }

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String filterWord;
            while ((filterWord = br.readLine()) != null) {
                char[] charArray = filterWord.trim().toCharArray();
                int len = charArray.length;
                if (len > 0) {
                    HashMap<Character, HashMap> subMap = filterMap;
                    for (int i = 0; i < len - 1; i++) {
                        HashMap<Character, HashMap> nodeMap = subMap.get(charArray[i]);
                        if (nodeMap == null) {
                            // 新索引，增加HashMap
                            int size = (int) Math.max(2, 16 / Math.pow(2, i));
                            HashMap<Character, HashMap> tempMap = new HashMap<Character, HashMap>(size);
                            subMap.put(charArray[i], tempMap);
                            subMap = tempMap;
                        } else {
                            // 索引已经存在
                            subMap = nodeMap;
                        }
                    }
                    // 处理最后一个字符
                    HashMap<Character, HashMap> nodeMap = subMap.get(charArray[len - 1]);
                    if (nodeMap == null) {
                        // 新索引，增加HashMap，并设置结束符
                        int size = (int) Math.max(2, 16 / Math.pow(2, len - 1));
                        HashMap<Character, HashMap> tempMap = new HashMap<Character, HashMap>(size);
                        tempMap.put(endTag, null);
                        subMap.put(charArray[len - 1], tempMap);
                    } else {
                        // 索引已经存在,设置结束符
                        nodeMap.put(endTag, null);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Load Keyword Error: " + e.getMessage(), e);
        }
    }

    /**
     * 检查指定的文本中是否存在关键字
     * @param text -- 待处理的文本
     * @return true--存在, false--不存在
     * @throws Exception
     */
    public static boolean hasKeyword(String text) throws Exception {
        if (text == null || text.length() == 0) {
            return false;
        }
        char[] charArray = text.toCharArray();
        int len = charArray.length;
        for (int i = 0; i < len; i++) {
            int index = i;
            HashMap<Character, HashMap> subMap = filterMap.get(charArray[index]);
            while (subMap != null) {
                if (subMap.containsKey(endTag)) {
                    // 匹配到一个关键字，匹配结束
                    return true;
                } else {
                    index++;
                    if (index >= len) {
                        // 字符串结束
                        return false;
                    }
                    subMap = subMap.get(charArray[index]);
                }
            }
        }
        return false;
    }

    /**
     * 从指定的文本中获取存在的关键字
     * @param text -- 待处理的文本
     * @return 存在的关键字列表
     * @throws Exception
     */
    public static List<String> getKeywords(String text) throws Exception {
        List<String> hasWords = new ArrayList<String>();
        if (text == null || text.length() == 0) {
            return hasWords;
        }
        char[] charArray = text.toCharArray();
        int len = charArray.length;
        for (int i = 0; i < len; i++) {
            int index = i;
            String theWord = "";
            HashMap<Character, HashMap> subMap = filterMap.get(charArray[index]);
            while (subMap != null) {
                theWord += charArray[index];
                if (subMap.containsKey(endTag)) {
                    // 匹配到一个关键字
                    if (!hasWords.contains(theWord)) {
                        hasWords.add(theWord);
                    }
                    break;
                } else {
                    index++;
                    if (index >= len) {
                        // 字符串结束
                        return hasWords;
                    }
                    subMap = subMap.get(charArray[index]);
                }
            }
        }
        return hasWords;
    }

    /**
     * 处理文本中关键字
     * <br>所有关键字中间补空格
     * @param text -- 待处理的文本
     * @param keywords -- 关键字列表
     * @return 处理后的文本
     * @throws Exception
     */
    public static String processKeywords(String text, List<String> keywords) throws Exception {
        if (text == null || text.length() == 0) {
            return text;
        }
        if (keywords == null || keywords.size() == 0) {
            return text;
        }
        for (String keyword : keywords) {
            String _keyword_ = "";
            char[] charArray = keyword.toCharArray();
            if (charArray.length > 1) {
                for (int i = 0; i < charArray.length; i++) {
                    _keyword_ += charArray[i] + " ";
                }
            }
            text = text.replaceAll(keyword, _keyword_);
        }
        return text;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        try {
            String text = "分哈佛我和奥菲和10086我分分为bitch宏观hoe我个我i各位哦i化工iwe化工i额外化工网个发/票和我i额符合万佛i哦"
                    + "哈佛iwhoi好各位哦i婚后噶we 化~工i文干你妈化宫i额共i饿哇吼i为何公和我后i规划哇哦i化工额娲皇宫"
                    + "额文化宫哇哈公会阝月哇哦个i好哇哦过日2@3**……后i额外规划皇复仇宫往后i过后vznhoiwfukoioihoitr"
                    + "ohoh屌oihoi机构借我歌后i给我回过后i的撒谎加03goi横扫i后？》i恭贺哇哦i个刚好i弟疼额文化宫额外"
                    + "搞好为爱花你我草给你妈的弄i规划我J巴饿合同，广佛文化馆ho化工i3后有%*(%9风3符合hi搜狐大善滚动"
                    + "分哈佛我和奥菲和我分分为宏观hoe我个我i各位哦i化工iwe化工i额外化工网个发/票和我i额符合万佛i哦"
                    + "哈佛iwhoi好各位哦i婚后噶we 化~工i文干你妈化宫i额共i饿哇吼i为何公和我后i规划哇哦i化工额娲皇宫"
                    + "额文化宫哇哈公会阝月哇哦个i好哇哦过日2@3**……后i额外规划皇复仇宫往后i过后vznhoiwfukoioihoitr"
                    + "ohoh屌oihoi机构借我歌后i给我回过后i的撒谎加03goi横扫i后？》i恭贺哇哦i个刚好i弟疼额文化宫额外"
                    + "搞好为爱花你我草给你妈的弄i规划我J巴饿合同，广佛文化馆ho化工i3后有%*(%9风3符合hi搜狐大善滚动";
            //
            long time = System.currentTimeMillis();
            KeywordUtils.loadKeyword("keyword.txt");
            System.out.println("初始化非法关键字耗时: " + (System.currentTimeMillis() - time) + "ms");
            //
            time = System.currentTimeMillis();
            List<String> words = KeywordUtils.getKeywords(text);
            System.out.println(">>> 非法关键字: " + (words.size() > 0 ? "存在" : "不存在"));
            for (int i = 0; i < words.size(); i++) {
                System.out.println((i + 1) + "\t" + words.get(i));
            }
            System.out.println("检索完非法关键字耗时: " + (System.currentTimeMillis() - time) + "ms");
            //
            time = System.currentTimeMillis();
            text = KeywordUtils.processKeywords(text, words);
            System.out.println("处理掉非法关键字耗时: " + (System.currentTimeMillis() - time) + "ms");
            //
            time = System.currentTimeMillis();
            boolean exist = KeywordUtils.hasKeyword(text);
            System.out.println(">>> 非法关键字: " + (exist ? "存在" : "不存在"));
            System.out.println("检索完非法关键字耗时: " + (System.currentTimeMillis() - time) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}