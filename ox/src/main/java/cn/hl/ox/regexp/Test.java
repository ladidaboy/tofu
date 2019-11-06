package cn.hl.ox.regexp;

import cn.hl.ax.data.DataUtils;
import cn.hl.ox.BuddhaBless;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    /**
     * Pattern类： Pattern类的静态方法compile用来编译正则表达式，在此[,\\s]+表示若干个","或者若干个空格匹配
     * split方法使用正则匹配将字符串切割成各子串并且返回
     *
     * @throws Exception
     */
    public void _test1() throws Exception {
        Pattern pattern = Pattern.compile("[,\\s]+");
        String[] result = pattern.split("one two  three,four , five,six");
        for (String str : result) {
            System.out.println(str);
        }
    }

    /**
     * Matcher类： 注意，Matcher的获得是通过Pattern.matcher(CharSequence
     * charSequence);输入必须是实现了CharSequence接口的类 常用方法：
     * matches()判断整个输入串是否匹配，整个匹配则返回true lookingAt()从头开始寻找，找到匹配则返回true
     *
     * @throws Exception
     */
    public void _test2() throws Exception {
        String str1 = "hello";
        Pattern pattern1 = Pattern.compile("hello");
        Matcher matcher1 = pattern1.matcher(str1);
        System.out.println("matcher1.matches()=>" + matcher1.matches());

        String str2 = "hello world";
        Pattern pattern2 = Pattern.compile("hello");
        Matcher matcher2 = pattern2.matcher(str2);
        System.out.println("matcher2.matches()=>" + matcher2.matches());
        System.out.println("matcher2.lookingAt()=>" + matcher2.lookingAt());
    }

    /**
     * find()扫描输入串，寻找下一个匹配子串，存在则返回true
     *
     * @throws Exception
     */
    public void _test3() throws Exception {
        Pattern pattern = Pattern.compile("hello");
        Matcher matcher = pattern.matcher("hello world, hello world, hello_world");
        StringBuffer sb = new StringBuffer();
        boolean find = matcher.find();
        while (find) {
            matcher.appendReplacement(sb, "haha"); // 实现非终端添加和替换步骤
            find = matcher.find();
            System.out.println("sb=>" + sb);
        }
        matcher.appendTail(sb); // 实现终端添加和替换步骤
        System.out.println(sb.toString());
    }

    /**
     * 匹配IP地址 IP地址中的句点字符必须进行转义处理（前面加上“\”），因为IP地址中的句点具有它本来的含义，
     * 而不是采用正则表达式语法中的特殊含义。句点在正则表达式中的特殊含义本文前面已经介绍。
     * 日志记录的时间部分由一对方括号包围。你可以按照如下思路提取出方括号里面的所有内容：
     * 首先搜索起始方括号字符（“[”），提取出所有不超过结束方括号字符（“]”）的内容，向前寻找直至找到结束方括号字符。
     *
     * @throws Exception
     */
    public void _test4() throws Exception {
        String logEntry = "192.168.0.1 - - [26/Feb/2009:14:56:43 -0500]\"GET /lsAlive/ht HTTP/1.0\"200 15\r\n"
                + "192.168.0.2 - - [25/Feb/2009:14:56:43 -0500]\"GET /lsAlive/ht HTTP/1.0\"200 15";
        String regexp = "([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})\\s-\\s-\\s\\[([^\\]]+)\\]";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(logEntry);
        boolean find = matcher.find();
        while (find) {
            MatchResult result = matcher.toMatchResult();
            System.out.println("IP=>" + result.group(1));
            System.out.println("Timestamp=>" + result.group(2));
            find = matcher.find();
        }
    }

    /**
     * HTML处理 分析HTML页面内FONT标记的所有属性
     *
     * @throws Exception
     */
    public void _test5() throws Exception {
        String html = "<font face=\"Arial Serif\" size=\"+2\" color=\"red\">";
        String regexForTag = "<\\s*font\\s+([^>]*)\\s*>";

        Pattern pattern = Pattern.compile(regexForTag, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);

        boolean find = matcher.find();

        String attribute = matcher.group(1);
        System.out.println("属性字符串为：" + attribute);

        String regexForAttribute = "([a-z]+)\\s*=\\s*\"([^\"]+)\"";
        Pattern pattern2 = Pattern.compile(regexForAttribute, Pattern.CASE_INSENSITIVE);
        Matcher matcher2 = pattern2.matcher(attribute);

        boolean find2 = matcher2.find();

        while (find2) {
            MatchResult result = matcher2.toMatchResult();
            System.out.println(result.group(1) + "=" + result.group(2));
            find2 = matcher2.find();
        }
    }

    /**
     * HTML处理 修改一些页面中的链接
     *
     * @throws Exception
     */
    public void _test6() throws Exception {
        String url = "<a href=\"http://192.168.0.1:8080/test/index.jsp#test...\">"
                + "< a href = \"http://192.168.0.1:8080/test/index.jsp#?hahahaha...\">";
        String regex = "(<\\s*a\\s+href\\s*=\\s*\"http://192.168.0.1:8080/test/index.jsp[^\"]+\">)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        boolean find = matcher.find();
        System.out.println("find=>" + find);
        while (find) {
            MatchResult result = matcher.toMatchResult();
            String temp = result.group(1);
            System.out.println("替换前=>" + temp);
            temp = temp.replace("192.168.0.1", "localhost");
            System.out.println("替换后=>" + temp);
            find = matcher.find();
        }
    }

    public void _test7() {
        String reg = "<[a-zA-Z]+.*?>([\\s\\S]*?)</[a-zA-Z]*>";
        //String reg = "<title>([\\s\\S]*?)</title>";
        String str = "<p></p><p>&nbsp;&nbsp;&nbsp;&nbsp;我们以Buffer类開始对java.nio包的浏览历程。"
                + "这些类是java.nio的构造基础。这个系列中。我们将尾随《java NIO》书籍一起深入研究缓冲区。了解各种不同的类型，并学会如何使用。</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;一个Buffer对象是固定数量的数据容器。<p></p><p>其作用是一个存储器，或者分段运输区，在这里数据可被存储并在之后用于检索。</p><p>"
                + "</p><p>&nbsp;&nbsp;&nbsp;&nbsp;Buffer类的家谱：</p><p>&nbsp;&nbsp;&nbsp;&nbsp;"
                + "<img src=\"http://photo.jfq24.com/image/bigger/blog/server/upload/2014-07/user_2/13711406446068247.png\" "
                + "title=\"2014-07-27_1527.png\"></p><p>&nbsp;&nbsp;&nbsp;&nbsp;<strong>一，缓冲区基础</strong>"
                + "</p><p>&nbsp;&nbsp;&nbsp; 1.缓冲区的属性：</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                + "容量(capacity)：缓冲区可以容纳的数据元素的最大数量，这一容量是在缓冲区被创建时设置的，而且永远不能被改变</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;上界(limit): 缓冲区的第一个不能被读或写的元素。"
                + "或者说。缓冲区中现存元素的计数。</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;位置(position): "
                + "下一个要被读或写的元素的索引，位置会自己主动由对应的get()和put()函数更新。</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                + "&nbsp;&nbsp;&nbsp;标记(mark): 一个备忘位置，调用mark()来设定mark=position.调用reset()设定position=mark。"
                + "标记在设定前是没有定义的(undefied)。</p><title>401 Forbidden</title><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                + "这四个属性的关系例如以下：</p>";
        Pattern p = Pattern.compile(reg, Pattern.MULTILINE);
        str = str.replace("&nbsp;", "");
        Matcher m = p.matcher(str);
        while (m.find()) {
            String data = m.group(1).trim();
            if (DataUtils.isNotBlank(data)) {
                System.out.println(data);
            }
        }
    }

    /**
     * 4种常用功能： 1、查询： 如果str中有regEx，那么rs为true，否则为flase。如果想在查找时忽略大小写， 则可以写成Pattern
     * p=Pattern.compile(regEx,Pattern.CASE_INSENSITIVE);
     *
     * @throws Exception
     */
    public void _testQuery() throws Exception {
        String str = "abc efg ABC";
        String regEx = "a|f";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        boolean rs = matcher.find();
        System.out.println("rs=>" + rs);
    }

    /**
     * 2、提取： 执行结果为name.txt，提取的字符串储存在m.group(i)中，其中i最大值为m.groupCount();
     *
     * @throws Exception
     */
    public void _testGet() throws Exception {
        String regEx = ".+\\\\(.+)$";
        String str = "c:\\dir1\\dir2\\name.txt";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        boolean rs = matcher.find();
        for (int i = 1; i <= matcher.groupCount(); i++) {
            System.out.println(matcher.group(i));
        }
    }

    /**
     * 3、分割：
     *
     * @throws Exception
     */
    public void _testSplit() throws Exception {
        String regex = "::";
        Pattern pattern = Pattern.compile(regex);
        String[] result = pattern.split("aa::bb::cc");
        for (String str : result) {
            System.out.println("result=>" + str);
        }

        System.out.println("---------");
        String[] normal = "aa::bb::cc".split(regex);
        for (String str : normal) {
            System.out.println("nornal=>" + str);
        }
    }

    /**
     * 4、替换（删除）： 如果写成空串，既可达到删除的功能
     *
     * @throws Exception
     */
    public void _testReplaceOrDelete() throws Exception {
        String regex = "a+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("aaabbced a ccdeaa");
        System.out.println("replaceFirst=>" + matcher.replaceFirst("A"));
        String result = matcher.replaceAll("A");
        System.out.println("replaceAll=>" + result);
        String delete = matcher.replaceAll("");
        System.out.println("替换为空即可达到删除的功能");
    }

    public static void main(String args[]) {
        Test basics = new Test();
        try {
            basics._test1();
            BuddhaBless.printCornerTitle("!");
            basics._test2();
            BuddhaBless.printCornerTitle("!");
            basics._test3();
            BuddhaBless.printCornerTitle("!");
            basics._test4();
            BuddhaBless.printCornerTitle("!");
            basics._test5();
            BuddhaBless.printCornerTitle("!");
            basics._test6();
            BuddhaBless.printCornerTitle("!");
            basics._test7();
            BuddhaBless.printCornerTitle("!");
            basics._testGet();
            BuddhaBless.printCornerTitle("!");
            basics._testSplit();
            BuddhaBless.printCornerTitle("!");
            basics._testQuery();
            BuddhaBless.printCornerTitle("!");
            basics._testReplaceOrDelete();
            BuddhaBless.printCornerTitle("!", ".");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
