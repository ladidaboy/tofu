package wit.example;

import java.math.BigDecimal;
import java.util.Stack;

public class TestTest {
    public static void main(String[] args) {

        // 试题 1 :: 实现一个函数，判断一个字符串是否为另一个字符串的子串。(需要重新实现，不可使用现成的API。)
        System.out.println("\n======== 试题 1 ========");
        System.out.println(containsSub("This is a test text for substring.", "text for"));

        // 试题 2 :: 实现一个函数，输入树结构及关键字，返回仅包含有关键字路径的一颗新树举例:如果输入树结构数据如(图1)和关键字B，则返回(图2)的数据结构
        System.out.println("\n======== 试题 2 ========");
        TreeNode root = new TreeNode("Root");
        TreeNode sub1 = new TreeNode("B");
        TreeNode sub2 = new TreeNode("C");
        TreeNode sub3 = new TreeNode("A");
        TreeNode sub4 = new TreeNode("D");
        TreeNode sub5 = new TreeNode("G");
        TreeNode sub6 = new TreeNode("B");
        root.left = sub4;
        root.middle = sub3;
        root.right = sub1;
        sub1.middle = sub2;
        sub4.left = sub6;
        sub4.right = sub5;
        printTree(root);
        System.out.println("\n-- After prune tree --");
        pruneTree(root, "B");
        printTree(root);

        // 试题 3 :: 实现一个函数，输入的是代表四则运算表达式的字符串，输出其计算结果举例:输入“(1+2)*1.2-5/2"输出结果 1.1
        System.out.println("\n\n======== 试题 3 ========");
        calculate("(1+2) * 1.2 - 5 / 2");

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static boolean containsSub(String text, String sub) {
        if (text == null || text.length() == 0 || sub == null || sub.length() == 0 || sub.length() > text.length()) {
            return false;
        }
        char[] textChars = text.toCharArray();
        char[] subChars = sub.toCharArray();
        for (int i = 0; i < text.length() - sub.length(); i++) {
            boolean matched = true;
            for (int j = 0; j < sub.length(); j++) {
                if (textChars[i + j] != subChars[j]) {
                    matched = false;
                    break;
                }
            }
            if (matched) {
                return true;
            }
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    static class TreeNode {
        String   val;
        TreeNode left;
        TreeNode middle;
        TreeNode right;

        TreeNode(String x) {
            val = x;
        }
    }

    public static boolean pruneTree(TreeNode node, String key) {
        if (node == null) {
            return false;
        }
        if (node.val.equals(key)) {
            return true;
        }
        boolean matched = false;
        if (pruneTree(node.left, key)) {
            matched = true;
        } else {
            node.left = null;
        }
        if (pruneTree(node.middle, key)) {
            matched = true;
        } else {
            node.middle = null;
        }
        if (pruneTree(node.right, key)) {
            matched = true;
        } else {
            node.right = null;
        }
        return matched;
    }

    /**
     * 类似 前序打印
     */
    public static void printTree(TreeNode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.val + ", ");
        printTree(node.left);
        printTree(node.middle);
        printTree(node.right);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    static Stack<BigDecimal> numbers;
    static Stack<Character>  operator;

    public static void calculate(String input) {
        numbers = new Stack<>();
        operator = new Stack<>();
        input = input.replaceAll(" ", "");
        char[] codes = input.toCharArray();
        // 分析算式 并 计算高优先级算法，括号及乘除
        String block = "";
        int i = 0;
        while (i < codes.length) {
            if (Character.isDigit(codes[i])) {
                // 获取数字
                while (i < codes.length) {
                    if (!Character.isDigit(codes[i]) && codes[i] != '.') {
                        break;
                    }
                    block += codes[i];
                    i++;
                }
                numbers.push(new BigDecimal(block));
                block = "";
                i--;
            } else if (codes[i] == '(') {
                // 括号开始
                operator.push(codes[i]);
            } else if (codes[i] == ')') {
                // 括号结束
                while (operator.peek() != '(') {
                    // 括号内表达式有效时开始计算
                    eval();
                }
                operator.pop();
            } else {
                while (!operator.isEmpty() && operator.peek() != '(' && getPriority(operator.peek()) >= getPriority(codes[i])) {
                    // 有 有效的运算符，并且当前运算符优先级高于上一个运算符时
                    eval();
                }
                // 记录运算符
                operator.push(codes[i]);
            }
            i++;
        }
        // 计算剩余运算符(加减算法)
        while (true) {
            if (numbers.size() <= 1) {
                break;
            } else {
                eval();
            }
        }

        // 打印结果
        if (numbers.size() < 1) {
            System.out.println("ERROR!");
        } else {
            System.out.println(numbers.peek().toPlainString());
        }
    }

    public static void eval() {
        BigDecimal v1 = numbers.pop();
        BigDecimal v2 = numbers.pop();
        switch (operator.pop()) {
            case '+':
                numbers.push(v2.add(v1));
                break;
            case '-':
                numbers.push(v2.add(v1.negate()));
                break;
            case '*':
                numbers.push(v2.multiply(v1));
                break;
            default:
                numbers.push(v2.divide(v1));
        }
    }

    public static int getPriority(Character input) {
        if (input == '+' || input == '-') {
            return 0;
        } else {
            return 1;
        }
    }

}
