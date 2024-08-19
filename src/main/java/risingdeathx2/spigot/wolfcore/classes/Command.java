package risingdeathx2.spigot.wolfcore.classes;

import java.util.List;

import javax.annotation.Nonnull;

public class Command {
    public String name;
    public List<Argument> options;
    public Command(@Nonnull String name,@Nonnull List<Argument> options) {
        this.name = name;
        this.options = options;
    }
}
