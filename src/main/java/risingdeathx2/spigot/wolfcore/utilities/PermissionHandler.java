package risingdeathx2.spigot.wolfcore.utilities;

import java.util.Collection;

import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.query.QueryOptions;

public class PermissionHandler {
    public static Number getNumberValue(String prefixNode, User user) {
        Collection<PermissionNode> nodes = user.resolveInheritedNodes(NodeType.PERMISSION,
                QueryOptions.defaultContextualOptions());
        Number highest = 0;
        for (Node node : nodes) {
            if (node.getKey().startsWith(prefixNode)) {
                String value = node.getKey().replace(prefixNode + ".", "");
                if (node.getValue()) {
                    if (value.matches("^[0-9]+$")) {
                        Number number = Integer.parseInt(value);
                        if (number.intValue() > highest.intValue()) {
                            highest = number;
                        }
                    } else if (value.equals("*")) {
                        highest = Integer.MAX_VALUE;
                    }
                }
            }
        }
        return highest;
    }

    User user;

    public PermissionHandler(User user) {
        this.user = user;
    }

    public Number getNumberValue(String prefixNode) {
        return getNumberValue(prefixNode, user);
    }
}
