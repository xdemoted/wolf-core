package com.wolfco.main.handlers;

import java.util.Collection;

import com.wolfco.main.Core;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.query.QueryOptions;

@Named("permissionHandler")
@Singleton
public class PermissionHandler {
    private final Core core;

    @Inject
    public PermissionHandler(Core core) {
        this.core = core;
    }

    public int getNumberValue(String prefixNode, User user) {
        int[] result = {0};

        core.getLuckPerms().getContextManager().getContext(user).ifPresentOrElse(contextSet -> {
            result[0] = getNumberValueWithContext(prefixNode, user, QueryOptions.contextual(contextSet));
        }, () -> {
            result[0] = getNumberValueWithContext(prefixNode, user, QueryOptions.defaultContextualOptions());
        });

        return result[0];
    }

    private int getNumberValueWithContext(String prefixNode, User user, QueryOptions queryOptions) {
        Collection<PermissionNode> nodes = user.resolveInheritedNodes(NodeType.PERMISSION,
                queryOptions);
        int highest = 0;
        for (Node node : nodes) {
            if (node.getKey().startsWith(prefixNode)) {
                highest = getHighestNumber(prefixNode, node, highest);
            }
        }
        return highest;
    }

    private int getHighestNumber(String prefixNode, Node node, int highest) {
        String value = node.getKey().replace(prefixNode + ".", "");
        if (node.getValue()) {
            if (value.matches("^\\d+$")) {
                int number = Integer.parseInt(value);
                if (number > highest) {
                    highest = number;
                }
            } else if (value.equals("*")) {
                highest = Integer.MAX_VALUE;
            }
        }
        return highest;
    }
}
