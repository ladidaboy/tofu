package wit.example;

import java.util.Scanner;

/**
 * <pre>
 * 定义构造三叉搜索树规则如下：
 *   每个节点都存有一个数，当插入一个新的数时，从根节点向下寻找，直到找到一个合适的空节点插入。
 *   查找的规则是：
 *     1. 如果数小于节点的数减去500，则将数插入节点的左子树
 *     2. 如果数大于节点的数加上500，则将数插入节点的右子树
 *     3. 否则，将数插入节点的中子树
 * 给你一系列数，请按以上规则，按顺序将数插入树中，构建出一棵三叉搜索树，最后输出树的高度。
 *
 * >> 输入描述 <<
 * 第一行为一个数N，表示有N个数，1<=N<=10000
 * 第二行为N个空格分隔的整数，每个数的范围为[1,10000]
 * << 输出描述 >>
 * 输出树的高度(根节点的高度为1)
 * </pre>
 */
public class TreeHeight {
    public static int depth = 1;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int count = in.nextInt();
        TreeNode root = new TreeNode(in.nextInt());
        while (--count > 0) {
            drill(root, 1, in.nextInt());
        }
        System.out.println(depth);
    }

    public static class TreeNode {
        int      value;
        TreeNode left;
        TreeNode mid;
        TreeNode right;

        public TreeNode(int value) {
            this.value = value;
        }
    }

    public static void drill(TreeNode node, int level, int value) {
        level += 1;
        // 如果数小于节点的数减去500，则将数插入节点的左子树
        if (value < node.value - 500) {
            if (node.left != null) {
                drill(node.left, level, value);
            } else {
                node.left = new TreeNode(value);
                depth = Math.max(depth, level);
            }
        }
        // 如果数大于节点的数加上500，则将数插入节点的右子树
        else if (value > node.value + 500) {
            if (node.right != null) {
                drill(node.right, level, value);
            } else {
                node.right = new TreeNode(value);
                depth = Math.max(depth, level);
            }
        }
        // 否则，将数插入节点的中子树
        else {
            if (node.mid != null) {
                drill(node.mid, level, value);
            } else {
                node.mid = new TreeNode(value);
                depth = Math.max(depth, level);
            }
        }
    }
}
/*
#### 示例 1 ####
输入
5
5000 2000 5000 8000 1800
输出
3
说明
最终构造出的树如下，高度为3
<img width="500" src="TreeHeight1.png"/>

#### 示例 2 ####
输入
3
5000 4000 3000
输出
3
说明
最终构造出的树如下，高度为3
<img width="500" src="TreeHeight2.png"/>
 */