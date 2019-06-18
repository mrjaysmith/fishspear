package com.fisher.fishspear.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class TreeBuilder {

    List<TreeNode> nodes;

    public TreeBuilder(List nodes) {
        this.nodes = nodes;
    }

    /**
     * 构建JSON树形结构
     *
     * @return
     */
    public String buildJSONTree() {
        List<TreeNode> nodeTree = buildTree();
        return JSONObject.toJSONString(nodeTree);
    }

    /**
     * 构建树形结构
     *
     * @return
     */
    public List buildTree() {
        List<TreeNode> treeNodes = new ArrayList<>();
        List<TreeNode> rootNodes = getRootNodes();
        for (TreeNode rootNode : rootNodes) {
            buildChildNodes(rootNode);
            treeNodes.add(rootNode);
        }
        return treeNodes;
    }

    /**
     * 递归子节点
     *
     * @param node
     */
    public void buildChildNodes(TreeNode node) {
        List<TreeNode> children = getChildNodes(node);
        if (!children.isEmpty()) {
            for (TreeNode child : children) {
                buildChildNodes(child);
            }
            node.setList(children);
            node.setChildren(children);
        }
    }

    /**
     * 获取父节点下所有的子节点
     *
     * @param pNode
     * @return
     */
    public List<TreeNode> getChildNodes(TreeNode pNode) {
        List<TreeNode> childNodes = new ArrayList<>();
        for (TreeNode n : nodes) {
            if (pNode.getId().equals(n.getPid())) {
                childNodes.add(n);
            }
        }
        return childNodes;
    }

    /**
     * 判断是否为根节点
     *
     * @param node
     * @return
     */
    public boolean rootNode(TreeNode node) {
        boolean isRootNode = true;
        for (TreeNode n : nodes) {
            if (node.getPid() != null && node.getPid().equals(n.getId())) {
                isRootNode = false;
                break;
            }
        }
        return isRootNode;
    }

    /**
     * 获取集合中所有的根节点
     *
     * @return
     */
    public List<TreeNode> getRootNodes() {
        List<TreeNode> rootNodes = new ArrayList<>();
        for (TreeNode n : nodes) {
            if (rootNode(n)) {
                rootNodes.add(n);
            }
        }
        return rootNodes;
    }
}
