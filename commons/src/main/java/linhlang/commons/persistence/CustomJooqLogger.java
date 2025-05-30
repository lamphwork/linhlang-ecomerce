package linhlang.commons.persistence;

import lombok.extern.slf4j.Slf4j;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;

@Slf4j
public class CustomJooqLogger implements ExecuteListener {

    @Override
    public void renderEnd(ExecuteContext ctx) {
        log.info("[SQL]: {}",  ctx.sql());
    }
    
    @Override
    public void executeEnd(ExecuteContext ctx) {
    }
}