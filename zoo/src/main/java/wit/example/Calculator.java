package wit.example;

import java.util.Stack;

public class Calculator {
    static Stack<Integer[]> numbers;
    static Stack<Character> operator;

    public static void main(String[] args) {
        String formula = "3 * (4 * 6 / (9 - (3 + 1)))";
        numbers = new Stack<>();
        operator = new Stack<>();
        formula = formula.replaceAll(" ", "");
        char[] codes = formula.toCharArray();

        // 分析算式 并 计算高优先级算法，括号及乘除
        String current = "";
        int i = 0;
        while (i < codes.length) {
            if (Character.isDigit(codes[i])) {
                while (i < codes.length) {
                    if (!Character.isDigit(codes[i])) {
                        break;
                    }
                    current += codes[i];
                    i++;
                }
                numbers.push(new Integer[] {1, Integer.parseInt(current)});
                current = "";
                i--;
            } else if (codes[i] == '(') {
                operator.push(codes[i]);
            } else if (codes[i] == ')') {
                while (operator.peek() != '(') {
                    eval();
                }
                operator.pop();
            } else {
                while (!operator.isEmpty() && operator.peek() != '(' && getPriority(operator.peek()) >= getPriority(codes[i])) {
                    eval();
                }
                operator.push(codes[i]);
            }
            i++;
        }
        // 计算剩余加减算法
        while (true) {
            if (numbers.size() <= 1) {
                break;
            } else {
                eval();
            }
        }

        // 打印结果
        Integer[] result = numbers.peek();
        if (result[0] == 0) {
            System.out.println("ERROR");
        } else {
            int up = gcd(result[0], result[1]);
            result[0] /= up;
            result[1] /= up;
            if (result[0] * result[1] < 0 && Math.abs(result[0]) != 1) {
                System.out.print("-");
            }
            if (Math.abs(result[0]) == 1) {
                System.out.println(result[1]);
            } else {
                System.out.println(Math.abs(result[1]) + "/" + Math.abs(result[0]));
            }
        }
    }

    //最大公约数
    public static int gcd(int a, int b) {
        return (a % b == 0) ? b : gcd(b, a % b);
    }

    public static void eval() {
        Integer[] n1 = numbers.pop();
        Integer[] n2 = numbers.pop();
        char x = operator.pop();
        Integer[] result = new Integer[2];

        if (x == '*' || x == '+' || x == '-') {
            result[0] = n2[0] * n1[0];
        } else {
            result[0] = n2[0] * n1[1];
        }

        int sum_a = n2[1] * n1[0];
        int sum_b = n1[1] * n2[0];
        if (x == '*') {
            result[1] = n2[1] * n1[1];
        } else if (x == '+') {
            result[1] = sum_a + sum_b;
        } else if (x == '-') {
            result[1] = sum_a - sum_b;
        } else {
            result[1] = n2[1] * n1[0];
        }
        numbers.push(result);
    }

    public static int getPriority(Character input) {
        if (input == '+' || input == '-') {
            return 0;
        } else {
            return 1;
        }
    }
}