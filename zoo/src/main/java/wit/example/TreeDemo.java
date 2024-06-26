package wit.example;

public class TreeDemo {
    static class TreeNode {
        public  int      val;
        private TreeNode left;
        private TreeNode right;

        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }

        public TreeNode(int val) {
            this.val = val;
        }
    }

    /**
     * 求二叉树的深度
     */
    public static int getDeep(TreeNode root) {
        if (root == null) {
            return 0;
        } else {
            return Math.max(getDeep(root.left), getDeep(root.right)) + 1;
        }
    }

    /**
     * 判断二叉树是否平衡
     */
    public static boolean isBalance(TreeNode root) {
        if (root == null) {
            return true;
        }
        int leftDeep = getDeep(root.left);
        int rightDeep = getDeep(root.right);
        if (Math.abs(leftDeep - rightDeep) > 1) {
            return false;
        }
        return isBalance(root.left) && isBalance(root.right);
    }

    /**
     * 构建平衡二叉树
     */
    public static TreeNode balanceTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        root.left = balanceTree(root.left);
        root.right = balanceTree(root.right);
        int leftDeep = getDeep(root.left);
        int rightDeep = getDeep(root.right);
        if (leftDeep - rightDeep > 1) {
            System.out.println("不平衡，左边深，需要右旋");
            int lld = getDeep(root.left.left);
            int lrd = getDeep(root.left.right);
            if (lrd > lld) {
                root.left = leftRotate(root.left);
            }
            return rightRotate(root);
        } else if (rightDeep - leftDeep > 1) {
            System.out.println("不平衡，右边深，需要左旋");
            int rld = getDeep(root.right.left);
            int rrd = getDeep(root.right.right);
            if (rld < rrd) {
                root.right = leftRotate(root.right);
                return leftRotate(root);
            }
        }
        return root;
    }

    //左旋
    public static TreeNode leftRotate(TreeNode root) {
        TreeNode newRoot = root.right;
        TreeNode branch = newRoot.left;
        newRoot.left = root;
        root.right = branch;
        return newRoot;
    }

    //右旋
    public static TreeNode rightRotate(TreeNode root) {
        TreeNode newRoot = root.left;
        TreeNode branch = newRoot.right;
        newRoot.right = root;
        root.left = branch;
        return newRoot;
    }

    public static void main(String[] args) {
        TreeNode node1 = new TreeNode(4);
        TreeNode node2 = new TreeNode(5);
        TreeNode node3 = new TreeNode(6);
        TreeNode node4 = new TreeNode(7);
        TreeNode node5 = new TreeNode(8);
        node5.left = node4;
        node4.right = node3;
        node3.left = node2;
        node2.left = node1;

        System.out.println(getDeep(node5));
        System.out.println(isBalance(node5));
        TreeNode newRoot = balanceTree(node5);
        System.out.println(isBalance(newRoot));
        System.out.println("构建完成");
    }
}
