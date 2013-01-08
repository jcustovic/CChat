package hr.chus.cchat.programd;

import org.aitools.programd.Core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.Phased;

public class CoreWrapper implements Phased {

    @Autowired
    private Core core;

    public final void shutdown() {
        core.shutdown();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

}
