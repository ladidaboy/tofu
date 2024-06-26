package wit.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 给定两个整数数组 preorder 和 inorder ，其中 preorder 是二叉树的先序遍历， inorder 是同一棵树的中序遍历，请构造二叉树并返回其根节点。
 * <pre>
 * 示例 1:
 * <img width="300" src="TreeBuild.png"/>
 * 输入: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
 * 输出: [3,9,20,null,null,15,7]
 *
 * 示例 2:
 * 输入: preorder = [-1], inorder = [-1]
 * 输出: [-1]
 * </pre>
 */
public class TreeBuild {
    public static void main(String[] args) {
        TreeNode root = buildTree(new int[] {3, 9, 20, 15, 7}, new int[] {9, 3, 15, 20, 7});

        List<Integer> out = new ArrayList<>();
        leftOrder(root, out);
        System.out.println("前序：" + out.stream().map(String::valueOf).collect(Collectors.joining(", ")));
        out.clear();
        inOrder(root, out);
        System.out.println("中序：" + out.stream().map(String::valueOf).collect(Collectors.joining(", ")));
        out.clear();
        rightOrder(root, out);
        System.out.println("后序：" + out.stream().map(String::valueOf).collect(Collectors.joining(", ")));
        //

        calTreeSum(root);
        out.clear();
        inOrder(root, out);
        System.out.println("中序：" + out.stream().map(String::valueOf).collect(Collectors.joining(", ")));
    }

    public static TreeNode buildTree(int[] preOrder, int[] inOrder) {
        int preLen = preOrder.length;
        int inLen = inOrder.length;
        if (preLen != inLen) {
            throw new RuntimeException("Incorrect input data.");
        }
        return buildTree(preOrder, 0, preLen - 1, inOrder, 0, inLen - 1);
    }

    /**
     * 使用数组 preorder 在索引区间 [preLeft, preRight] 中的所有元素
     * 和数组 inOrder 在索引区间 [inLeft, inRight] 中的所有元素构造二叉树
     *
     * @param preOrder 二叉树前序遍历结果
     * @param preLeft  二叉树前序遍历结果的左边界
     * @param preRight 二叉树前序遍历结果的右边界
     * @param inOrder  二叉树后序遍历结果
     * @param inLeft   二叉树后序遍历结果的左边界
     * @param inRight  二叉树后序遍历结果的右边界
     * @return 二叉树的根结点
     */
    private static TreeNode buildTree(int[] preOrder, int preLeft, int preRight, int[] inOrder, int inLeft, int inRight) {
        // 因为是递归调用的方法，按照国际惯例，先写递归终止条件
        if (preLeft > preRight || inLeft > inRight) {
            return null;
        }
        // 先序遍历的起点元素很重要
        int pivot = preOrder[preLeft];
        TreeNode node = new TreeNode(pivot);
        int pivotIndex = inLeft;
        // 严格上说还要做数组下标是否越界的判断 pivotIndex < inRight
        while (inOrder[pivotIndex] != pivot) {
            pivotIndex++;
        }
        node.left = buildTree(preOrder, preLeft + 1, preLeft + (pivotIndex - inLeft), inOrder, inLeft, pivotIndex - 1);
        node.right = buildTree(preOrder, preLeft + 1 + (pivotIndex - inLeft), preRight, inOrder, pivotIndex + 1, inRight);
        return node;
    }

    ////

    public static void calTreeSum(TreeNode node) {
        if (node == null) {
            return;
        }
        node.val = calSum(node.left) + calSum(node.right);
        if (node.left != null) {
            calTreeSum(node.left);
        }
        if (node.right != null) {
            calTreeSum(node.right);
        }
    }

    public static int calSum(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return node.val + calSum(node.left) + calSum(node.right);
    }

    ////

    public static void leftOrder(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        res.add(root.val);
        leftOrder(root.left, res);
        leftOrder(root.right, res);
    }

    public static void inOrder(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        inOrder(root.left, res);
        res.add(root.val);
        inOrder(root.right, res);
    }

    public static void rightOrder(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        rightOrder(root.left, res);
        rightOrder(root.right, res);
        res.add(root.val);
    }

    ////

    static class TreeNode {
        int      val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }
}


