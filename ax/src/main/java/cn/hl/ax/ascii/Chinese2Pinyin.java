package cn.hl.ax.ascii;

import cn.hl.ax.CommonConst;
import cn.hl.ax.log.LogUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.LinkedHashMap;

@Slf4j
public class Chinese2Pinyin {
    private static final LinkedHashMap<String, Integer> spellMap     = new LinkedHashMap<>();
    private static int                                  spellMapSize = 0;

    static {
        initialize();
        log.info("ⓘ Chinese Transfer Spell Done! Initialize {} Mappings!", spellMapSize);
    }

    private static void putMap(String spell, int ascii) {
        spellMapSize++;
        spellMap.put(spell, ascii);
    }

    /**
     * 初始化汉字信息与ASCII对应关系, 需按照ASCII正序开始初始化
     */
    private static void initialize() {
        // Chinese Character Codes
        putMap("a", -20319);     putMap("ai", -20317);     putMap("an", -20304);     putMap("ang", -20295);   putMap("ao", -20292);
        putMap("ba", -20283);    putMap("bai", -20265);    putMap("ban", -20257);    putMap("bang", -20242);  putMap("bao", -20230);
        putMap("bei", -20051);   putMap("ben", -20036);    putMap("beng", -20032);   putMap("bi", -20026);    putMap("bian", -20002);
        putMap("biao", -19990);  putMap("bie", -19986);    putMap("bin", -19982);    putMap("bing", -19976);  putMap("bo", -19805);
        putMap("bu", -19784);    putMap("ca", -19775);     putMap("cai", -19774);    putMap("can", -19763);   putMap("cang", -19756);
        putMap("cao", -19751);   putMap("ce", -19746);     putMap("ceng", -19741);   putMap("cha", -19739);   putMap("chai", -19728);
        putMap("chan", -19725);  putMap("chang", -19715);  putMap("chao", -19540);   putMap("che", -19531);   putMap("chen", -19525);
        putMap("cheng", -19515); putMap("chi", -19500);    putMap("chong", -19484);  putMap("chou", -19479);  putMap("chu", -19467);
        putMap("chuai", -19289); putMap("chuan", -19288);  putMap("chuang", -19281); putMap("chui", -19275);  putMap("chun", -19270);
        putMap("chuo", -19263);  putMap("ci", -19261);     putMap("cong", -19249);   putMap("cou", -19243);   putMap("cu", -19242);
        putMap("cuan", -19238);  putMap("cui", -19235);    putMap("cun", -19227);    putMap("cuo", -19224);   putMap("da", -19218);
        putMap("dai", -19212);   putMap("dan", -19038);    putMap("dang", -19023);   putMap("dao", -19018);   putMap("de", -19006);
        putMap("deng", -19003);  putMap("di", -18996);     putMap("dian", -18977);   putMap("diao", -18961);  putMap("die", -18952);
        putMap("ding", -18783);  putMap("diu", -18774);    putMap("dong", -18773);   putMap("dou", -18763);   putMap("du", -18756);
        putMap("duan", -18741);  putMap("dui", -18735);    putMap("dun", -18731);    putMap("duo", -18722);   putMap("e", -18710);
        putMap("en", -18697);    putMap("er", -18696);     putMap("fa", -18526);     putMap("fan", -18518);   putMap("fang", -18501);
        putMap("fei", -18490);   putMap("fen", -18478);    putMap("feng", -18463);   putMap("fo", -18448);    putMap("fou", -18447);
        putMap("fu", -18446);    putMap("ga", -18239);     putMap("gai", -18237);    putMap("gan", -18231);   putMap("gang", -18220);
        putMap("gao", -18211);   putMap("ge", -18201);     putMap("gei", -18184);    putMap("gen", -18183);   putMap("geng", -18181);
        putMap("gong", -18012);  putMap("gou", -17997);    putMap("gu", -17988);     putMap("gua", -17970);   putMap("guai", -17964);
        putMap("guan", -17961);  putMap("guang", -17950);  putMap("gui", -17947);    putMap("gun", -17931);   putMap("guo", -17928);
        putMap("ha", -17922);    putMap("hai", -17759);    putMap("han", -17752);    putMap("hang", -17733);  putMap("hao", -17730);
        putMap("he", -17721);    putMap("hei", -17703);    putMap("hen", -17701);    putMap("heng", -17697);  putMap("hong", -17692);
        putMap("hou", -17683);   putMap("hu", -17676);     putMap("hua", -17496);    putMap("huai", -17487);  putMap("huan", -17482);
        putMap("huang", -17468); putMap("hui", -17454);    putMap("hun", -17433);    putMap("huo", -17427);   putMap("ji", -17417);
        putMap("jia", -17202);   putMap("jian", -17185);   putMap("jiang", -16983);  putMap("jiao", -16970);  putMap("jie", -16942);
        putMap("jin", -16915);   putMap("jing", -16733);   putMap("jiong", -16708);  putMap("jiu", -16706);   putMap("ju", -16689);
        putMap("juan", -16664);  putMap("jue", -16657);    putMap("jun", -16647);    putMap("ka", -16474);    putMap("kai", -16470);
        putMap("kan", -16465);   putMap("kang", -16459);   putMap("kao", -16452);    putMap("ke", -16448);    putMap("ken", -16433);
        putMap("keng", -16429);  putMap("kong", -16427);   putMap("kou", -16423);    putMap("ku", -16419);    putMap("kua", -16412);
        putMap("kuai", -16407);  putMap("kuan", -16403);   putMap("kuang", -16401);  putMap("kui", -16393);   putMap("kun", -16220);
        putMap("kuo", -16216);   putMap("la", -16212);     putMap("lai", -16205);    putMap("lan", -16202);   putMap("lang", -16187);
        putMap("lao", -16180);   putMap("le", -16171);     putMap("lei", -16169);    putMap("leng", -16158);  putMap("li", -16155);
        putMap("lia", -15959);   putMap("lian", -15958);   putMap("liang", -15944);  putMap("liao", -15933);  putMap("lie", -15920);
        putMap("lin", -15915);   putMap("ling", -15903);   putMap("liu", -15889);    putMap("long", -15878);  putMap("lou", -15707);
        putMap("lu", -15701);    putMap("lv", -15681);     putMap("luan", -15667);   putMap("lue", -15661);   putMap("lun", -15659);
        putMap("luo", -15652);   putMap("ma", -15640);     putMap("mai", -15631);    putMap("man", -15625);   putMap("mang", -15454);
        putMap("mao", -15448);   putMap("me", -15436);     putMap("mei", -15435);    putMap("men", -15419);   putMap("meng", -15416);
        putMap("mi", -15408);    putMap("mian", -15394);   putMap("miao", -15385);   putMap("mie", -15377);   putMap("min", -15375);
        putMap("ming", -15369);  putMap("miu", -15363);    putMap("mo", -15362);     putMap("mou", -15183);   putMap("mu", -15180);
        putMap("na", -15165);    putMap("nai", -15158);    putMap("nan", -15153);    putMap("nang", -15150);  putMap("nao", -15149);
        putMap("ne", -15144);    putMap("nei", -15143);    putMap("nen", -15141);    putMap("neng", -15140);  putMap("ni", -15139);
        putMap("nian", -15128);  putMap("niang", -15121);  putMap("niao", -15119);   putMap("nie", -15117);   putMap("nin", -15110);
        putMap("ning", -15109);  putMap("niu", -14941);    putMap("nong", -14937);   putMap("nu", -14933);    putMap("nv", -14930);
        putMap("nuan", -14929);  putMap("nue", -14928);    putMap("nuo", -14926);    putMap("o", -14922);     putMap("ou", -14921);
        putMap("pa", -14914);    putMap("pai", -14908);    putMap("pan", -14902);    putMap("pang", -14894);  putMap("pao", -14889);
        putMap("pei", -14882);   putMap("pen", -14873);    putMap("peng", -14871);   putMap("pi", -14857);    putMap("pian", -14678);
        putMap("piao", -14674);  putMap("pie", -14670);    putMap("pin", -14668);    putMap("ping", -14663);  putMap("po", -14654);
        putMap("pu", -14645);    putMap("qi", -14630);     putMap("qia", -14594);    putMap("qian", -14429);  putMap("qiang", -14407);
        putMap("qiao", -14399);  putMap("qie", -14384);    putMap("qin", -14379);    putMap("qing", -14368);  putMap("qiong", -14355);
        putMap("qiu", -14353);   putMap("qu", -14345);     putMap("quan", -14170);   putMap("que", -14159);   putMap("qun", -14151);
        putMap("ran", -14149);   putMap("rang", -14145);   putMap("rao", -14140);    putMap("re", -14137);    putMap("ren", -14135);
        putMap("reng", -14125);  putMap("ri", -14123);     putMap("rong", -14122);   putMap("rou", -14112);   putMap("ru", -14109);
        putMap("ruan", -14099);  putMap("rui", -14097);    putMap("run", -14094);    putMap("ruo", -14092);   putMap("sa", -14090);
        putMap("sai", -14087);   putMap("san", -14083);    putMap("sang", -13917);   putMap("sao", -13914);   putMap("se", -13910);
        putMap("sen", -13907);   putMap("seng", -13906);   putMap("sha", -13905);    putMap("shai", -13896);  putMap("shan", -13894);
        putMap("shang", -13878); putMap("shao", -13870);   putMap("she", -13859);    putMap("shen", -13847);  putMap("sheng", -13831);
        putMap("shi", -13658);   putMap("shou", -13611);   putMap("shu", -13601);    putMap("shua", -13406);  putMap("shuai", -13404);
        putMap("shuan", -13400); putMap("shuang", -13398); putMap("shui", -13395);   putMap("shun", -13391);  putMap("shuo", -13387);
        putMap("si", -13383);    putMap("song", -13367);   putMap("sou", -13359);    putMap("su", -13356);    putMap("suan", -13343);
        putMap("sui", -13340);   putMap("sun", -13329);    putMap("suo", -13326);    putMap("ta", -13318);    putMap("tai", -13147);
        putMap("tan", -13138);   putMap("tang", -13120);   putMap("tao", -13107);    putMap("te", -13096);    putMap("teng", -13095);
        putMap("ti", -13091);    putMap("tian", -13076);   putMap("tiao", -13068);   putMap("tie", -13063);   putMap("ting", -13060);
        putMap("tong", -12888);  putMap("tou", -12875);    putMap("tu", -12871);     putMap("tuan", -12860);  putMap("tui", -12858);
        putMap("tun", -12852);   putMap("tuo", -12849);    putMap("wa", -12838);     putMap("wai", -12831);   putMap("wan", -12829);
        putMap("wang", -12812);  putMap("wei", -12802);    putMap("wen", -12607);    putMap("weng", -12597);  putMap("wo", -12594);
        putMap("wu", -12585);    putMap("xi", -12556);     putMap("xia", -12359);    putMap("xian", -12346);  putMap("xiang", -12320);
        putMap("xiao", -12300);  putMap("xie", -12120);    putMap("xin", -12099);    putMap("xing", -12089);  putMap("xiong", -12074);
        putMap("xiu", -12067);   putMap("xu", -12058);     putMap("xuan", -12039);   putMap("xue", -11867);   putMap("xun", -11861);
        putMap("ya", -11847);    putMap("yan", -11831);    putMap("yang", -11798);   putMap("yao", -11781);   putMap("ye", -11604);
        putMap("yi", -11589);    putMap("yin", -11536);    putMap("ying", -11358);   putMap("yo", -11340);    putMap("yong", -11339);
        putMap("you", -11324);   putMap("yu", -11303);     putMap("yuan", -11097);   putMap("yue", -11077);   putMap("yun", -11067);
        putMap("za", -11055);    putMap("zai", -11052);    putMap("zan", -11045);    putMap("zang", -11041);  putMap("zao", -11038);
        putMap("ze", -11024);    putMap("zei", -11020);    putMap("zen", -11019);    putMap("zeng", -11018);  putMap("zha", -11014);
        putMap("zhai", -10838);  putMap("zhan", -10832);   putMap("zhang", -10815);  putMap("zhao", -10800);  putMap("zhe", -10790);
        putMap("zhen", -10780);  putMap("zheng", -10764);  putMap("zhi", -10587);    putMap("zhong", -10544); putMap("zhou", -10533);
        putMap("zhu", -10519);   putMap("zhua", -10331);   putMap("zhuai", -10329);  putMap("zhuan", -10328); putMap("zhuang", -10322);
        putMap("zhui", -10315);  putMap("zhun", -10309);   putMap("zhuo", -10307);   putMap("zi", -10296);    putMap("zong", -10281);
        putMap("zou", -10274);   putMap("zu", -10270);     putMap("zuan", -10262);   putMap("zui", -10260);   putMap("zun", -10256);
        putMap("zuo", -10254);   // Total 396 Mappings.
    }

    /**
     * 获得单个汉字的ASCII码.
     * @param cn 汉字字符
     * @return 0:错误返回, 否则返回ascii
     */
    private static int getCnAscii(char cn) {
        byte[] bytes = null;
        try {
            bytes = (String.valueOf(cn)).getBytes("GBK");
        } catch (Exception e) {
            log.error(LogUtils.getSimpleMessages(e));
        }
        if (bytes == null || bytes.length > 2 || bytes.length == 0) {
            // 错误
            return 0;
        }
        if (bytes.length == 1) {
            // 英文字符
            return bytes[0];
        } else {
            // 中文字符
            int highByte = 256 + bytes[0];
            int lowByte = 256 + bytes[1];
            return (256 * highByte + lowByte) - 256 * 256;
        }
    }

    /**
     * 根据ASCII码到SpellMap中查找对应的拼音
     * @param ascii 字符对应的ASCII
     * @param blank 是否分词
     * @return
     *         拼音, 首先判断ASCII>0&<160, 如果判断为真返回对应的字符,
     *         否则到SpellMap中查找, 如果没有找到拼音, 则返回null, 如果找到则返回拼音.
     */
    private static String getSpellByAscii(int ascii, boolean blank) {
        if (ascii > 0 && ascii < 160) { // 单字符
            return String.valueOf((char) ascii); // Character.toString((char) ascii);
        } else if (ascii < -20319 || ascii > -10247) { // 未知字符
            return null;
        }

        Iterator<String> it = spellMap.keySet().iterator();
        String spell0 = null, spell1;
        int asciiRang0 = -20319, asciiRang1;
        while (it.hasNext()) {
            spell1 = it.next();
            Integer valObj = spellMap.get(spell1);
            if (valObj != null) {
                asciiRang1 = valObj;

                if (ascii >= asciiRang0 && ascii < asciiRang1) { // 区间找到
                    return ((spell0 == null) ? spell1 : spell0) + (blank ? " " : "");
                } else {
                    spell0 = spell1;
                    asciiRang0 = asciiRang1;
                }
            }
        }

        return null;
    }

    /**
     * 将中文符号转换成对应的英文符号
     * @param cs chinese chars
     * @return english chars
     */
    private static String chineseSymbol2Ascii(String cs) {
        String[] chineseSymbols = {
        	"。",	"，",	"、",	"；",	"：",	"？",	"！",	"…",	"—",	"·",
        	"ˉ",	"ˇ",	"¨",	"‘",	"’",	"“",	"”",	"々",	"～",	"‖",
        	"∶",	"＇",	"＂",	"｀",	"｜",	"〃",	"〔",	"〕",	"〈",	"〉",
        	"《",	"》",	"「",	"」",	"『",	"』",	"．",	"〖",	"〗",	"【",
        	"】",	"（",	"）",	"［",	"］",	"｛",	"｝",	"➢",	"➣"
        };
        String[] asciiSymbols = {
        	". ",	", ",	", ",	"; ",	": ",	"? ",	"! ",	"... ",	"-",	" ",
        	"-",	"^",	" ",	"'",	"'",	"\"",	"\"",	" ",	"~",	"||",
        	": ",	"'",	"\"",	"'",	"|",	"\"",	"(",	")",	"<",	">",
        	"<<",	">>",	"[",	"]",	"[",	"]",	".",	"[",	"]",	"[",
        	"]",	"(",	")",	"[",	"]",	"{",	"}",	">",	">",	" "
        };
        int rpi;
        for (int idx = 0; idx < chineseSymbols.length; idx++) {
            if (idx < asciiSymbols.length) {
                rpi = idx;
            } else {
                rpi = asciiSymbols.length - 1;
            }
            cs = cs.replaceAll(chineseSymbols[idx], asciiSymbols[rpi]);
        }
        return cs;
    }

    /**
     * 返回字符串的全拼,是汉字转化为全拼,其它字符不进行转换
     * @param cnStr 字符串
     * @param blank 是否分词
     * @return 转换成全拼后的字符串
     */
    public static String getFullSpell(String cnStr, boolean blank) {
        if (cnStr == null || CommonConst.S_EMPTY.equals(cnStr.trim())) {
            return cnStr;
        }
        cnStr = chineseSymbol2Ascii(cnStr);
        char[] chars = cnStr.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char ch : chars) {
            int ascii = getCnAscii(ch);
            if (ascii == 0) {
                // 取ASCII时出错
                sb.append(ch);
            } else {
                String spell = getSpellByAscii(ascii, blank);
                if (spell == null) {
                    sb.append(ch);
                } else {
                    sb.append(spell);
                }
            }
        }
        return sb.toString();
    }
}