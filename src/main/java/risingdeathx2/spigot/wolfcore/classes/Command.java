package risingdeathx2.spigot.wolfcore.classes;

import java.util.List;

import javax.annotation.Nonnull;

public class Command {
    public String name;
    public List<Argument> options;
    public String node;
    public Command(@Nonnull String name, String node,@Nonnull List<Argument> options) {
        this.name = name;
        this.options = options;
        this.node = node;
    }
}
