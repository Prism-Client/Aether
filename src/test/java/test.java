import net.prismclient.aether.core.event.MouseMove;
import net.prismclient.aether.core.event.UIEventBus;

public class test {
    public void hi() {
        UIEventBus.INSTANCE.register("HEllo!", MouseMove.class, (event) -> {
            System.out.println("Hello!");
        });
    }
}
