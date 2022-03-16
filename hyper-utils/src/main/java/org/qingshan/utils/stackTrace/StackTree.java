package org.qingshan.utils.stackTrace;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.qingshan.utils.json.JSONUtil;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StackTree {
    static ThreadLocal<StackTree> localTree = new ThreadLocal<StackTree>();

    public static class StackNode {

        public StackNode() {
        }

        public StackNode(int deep) {
            this.deep = deep;
            this.stackCount = 1;
        }

        public StackNode parentNode;
        public List<StackNode> childNodes;
        public String stackMethod;
        public StackTraceEnum.targetType targetType;
        public int deep;
        public int stackCount;
        public Long startTime;
        public Long endTime;

        public Long getExecTime() {
            if (null != startTime && null != endTime) {
                return endTime - startTime;
            } else {
                return null;
            }
        }

        public boolean equals(StackNode other) {
            if (other == null) {
                return false;
            }
            if (!stackMethod.equals(other.stackMethod)) {
                return false;
            }
            if (childNodes == null && other.childNodes == null) {
                return true;
            }
            if (childNodes == null || other.childNodes == null) {
                return false;
            }
            int size = childNodes.size();
            if (size != other.childNodes.size()) {
                return false;
            }

            for (int i = 0; i < size; i++) {
                StackNode x = childNodes.get(i);
                StackNode y = other.childNodes.get(i);
                if (!x.equals(y)) {
                    return false;
                }
            }
            return true;
        }
    }


    public static void start(String stackMethod) {
        if (!StackTraceContext.ifOpenTrace) {
            return;
        }
        StackTree tree = new StackTree();
        StackNode rootNode = new StackNode(0);
        rootNode.stackMethod = stackMethod;
        rootNode.startTime = System.currentTimeMillis();
        tree.rootNode = rootNode;
        tree.curNode = rootNode;
        localTree.set(tree);
    }


    public static void exit() {
        StackTree tree = localTree.get();
        if (tree != null) {
            if (tree.curNode == null) {
                log.error("调用追踪异常,exit失败.errorMsg->{}", "当前已退出根节点");
                return;
            }
            tree.curNode.endTime = System.currentTimeMillis();
            tree.curNode = tree.curNode.parentNode;
        }
    }

    public static void enter(String stackMethod) {
        enter(stackMethod, null);
    }

    public static void enter(String stackMethod, StackTraceEnum.targetType targetType) {
        StackTree tree = localTree.get();
        if (tree == null) {
            return;
        }
        if (tree.curNode == null) {
            log.error("调用追踪异常,enter失败,errorMsg->{}", "当前已退出根节点");
            return;
        }
        StackNode parentNode = tree.curNode;
        StackNode newNode = new StackNode(parentNode.deep + 1);
        newNode.stackMethod = stackMethod;
        newNode.targetType = targetType;
        newNode.parentNode = parentNode;
        newNode.startTime = System.currentTimeMillis();
        if (parentNode.childNodes == null) {
            parentNode.childNodes = new ArrayList<StackNode>();
        }
        parentNode.childNodes.add(newNode);

        tree.curNode = newNode;

        //重复调用整理
        cleanRepeatNode(parentNode);
    }


    public static void cleanRepeatNode(StackNode parentNode) {
        if (parentNode == null) {
            return;
        }
        int len = parentNode.childNodes.size();
        if (len <= 1) {
            cleanRepeatNode(parentNode.parentNode);
            return;
        }
        StackNode a = parentNode.childNodes.get(len - 2);
        StackNode b = parentNode.childNodes.get(len - 1);
        if (a.equals(b)) {
            b.stackCount = ++a.stackCount;
            b.startTime = a.startTime;
            parentNode.childNodes.remove(len - 2);
        }
        cleanRepeatNode(parentNode.parentNode);
    }


    public static void clear() {
        localTree.set(null);
    }

    public StackNode rootNode;

    public StackNode curNode;

    public static StackTree getCurrentTree() {
        StackTree tree = localTree.get();
        return null == tree ? new StackTree() : tree;
    }

    public String toString() {
        if (rootNode == null) {
            rootNode = curNode;
        }
        if (rootNode == null) {
            return "Empty Tree";
        } else {
            StringBuilder sb = new StringBuilder();
            buildShow(rootNode, "", sb, true);
            return sb.toString();
        }
    }

    public static String toString(String stackTreeJSON) {
        if (StringUtils.isBlank(stackTreeJSON)) {
            return "";
        }
        StackTree stackTree = JSONUtil.parseObject(stackTreeJSON, StackTree.class);
        return stackTree.toString();
    }


    private void buildShow(StackNode node, String space, StringBuilder sb, boolean isParentLastNode) {

        if (node != null) {

            sb.append(space);
            if (node.parentNode != null) {
                sb.append("|-");
            }
            sb.append(node.stackMethod)
                    .append(null != node.targetType ? " " + "[" + node.targetType.getIcon() + "]" : "")
                    .append(node.stackCount > 1 ? " " + "[@" + node.stackCount + "]" : "")
                    .append(" ")
                    .append(null != node.getExecTime() ? "[" + node.getExecTime() + "ms]" : "")
                    .append("\n");
            if (node.deep <= 8) {
                if (node.childNodes != null && node.childNodes.size() > 0) {

                    for (int i = 0; i < node.childNodes.size(); i++) {
                        StackNode chNode = node.childNodes.get(i);
                        buildShow(chNode, space + ((node.parentNode != null
                                        && isParentLastNode) ? "|   " : "    "),
                                sb, (i != node.childNodes.size() - 1));

                    }

                }
            }
        }
    }

    public static void main(String[] args) {
        StackTraceContext.ifOpenTrace = true;
        StackTree.start("test");
        StackTree.enter("hello");
        StackTree.enter("stack1");
        StackTree.enter("stackSub1");
        StackTree.exit();
        StackTree.enter("stackSub2");
        StackTree.exit();
        StackTree.enter("stackSub2");
        StackTree.exit();
        StackTree.exit();

        StackTree.enter("stack2");
        StackTree.enter("stack21");
        StackTree.exit();
        StackTree.exit();

        StackTree.enter("stack2");
        StackTree.enter("stack21");
        StackTree.exit();
        StackTree.exit();

        StackTree.exit();
        StackTree.exit();
        System.out.println(StackTree.getCurrentTree().toString());
    }
}
