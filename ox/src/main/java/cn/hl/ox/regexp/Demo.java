package cn.hl.ox.regexp;

import cn.hl.ax.data.DataUtils;
import cn.hl.ax.time.UTCTimeUtils;
import cn.hl.ox.clone.bean.BS;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hyman
 * @version $ Id: Demo.java, v 0.1  hyman Exp $
 * @date 2021-05-06 15:20:05
 */
public class Demo {
    public static void main(String[] args) throws InterruptedException {
        /*String input = "super[32].child[1].name";
        String regex = "([a-zA-Z]+)\\[(.*?)]";*/

        /*String input = "{attribute:1,\n\"term|f84a5d5f23e542f19fedbb57bffa0df7|\":\n\"23\"\",\n\"attribute\":20200202}";
        String regex = "[ ]?['\"]?([a-zA-z0-9|]+)['\"]?:[ ]?['\"]?([a-zA-z0-9\\u4e00-\\u9fa5\":-]*?)['\"]?[,}]";*/

        /*String input = "zzz[x].abcd.  efg [*] {hi,   jk,  lmn}.  oo";
        String regex = "([a-zA-Z]+)[ ]?\\[([0-9* ]*?)][ ]?\\{(.*?)}";*/

        String input = "CUS:cid(5334):st(SEA192865):W(49699):vll(D07068):bw(100M)";
        String regex = ":([a-zA-Z]+)\\(([0-9a-zA-Z:/-]+)\\)";

        input = DataUtils.compressText(input);
        System.out.println("\nInput: " + input);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            System.out.println("\33[35;0m____________////// matcher.find \\\\\\\\\\\\____________\33[0m");
            Thread.sleep(5);
            int count = matcher.groupCount();
            for (int i = 0; i <= count; i++) {
                System.out.println(matcher.group(i));
            }
            int start = matcher.start(), end = matcher.end();
            System.out.println("\33[33;0m[[ start: " + start + " ~ end: " + end + " | length: " + input.length() + " ]]\33[0m");
        }

        //^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\_/^\\
        List<BS> list = new ArrayList<>();
        BS v1 = new BS();
        v1.setName("Hyman");
        v1.setAge(18);
        list.add(v1);
        BS v2 = new BS();
        v2.setName("Iron-Man");
        v2.setAge(6);
        list.add(v2);
        List<BS> subs = new ArrayList<>();
        BS sub = new BS();
        subs.add(sub);
        sub.setName(v2.getName() + " SUB");
        sub.setAge((int) (Math.random() * 100));
        v2.setSubs(subs);
        // CopyData(Object -> Record) use Map<fromKey, toKey>
        BS data = new BS();
        data.setBirthday(UTCTimeUtils.random());
        data.setList(list);
        Record rd = new Record();
        HashMap<String, String> fieldMap = new HashMap<>();
        fieldMap.put("birthday", "copy_time");
        fieldMap.put("list[1].subs[0].name", "copy-name");
        fieldMap.put("list[1].subs[0].age", "zen_random_age");
        DataUtils.copyRecord(data, rd, fieldMap, false);
        System.err.println("\n>>>>>>>>>>>>>>>> After copyRecord <<<<<<<<<<<<<<<<");
        System.out.println(JSON.toJSONString(rd.getColumns(), SerializerFeature.PrettyFormat, SerializerFeature.UseISO8601DateFormat));
    }
}
