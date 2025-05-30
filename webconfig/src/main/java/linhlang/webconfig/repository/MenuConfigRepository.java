package linhlang.webconfig.repository;

import linhlang.commons.model.PageData;
import linhlang.commons.persistence.CommonRepository;
import linhlang.webconfig.constants.Constants;
import linhlang.webconfig.controller.request.MenuConfigSearch;
import linhlang.webconfig.model.MenuConfig;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.tools.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jooq.generated.Tables.*;

@Repository
@RequiredArgsConstructor
public class MenuConfigRepository implements CommonRepository<MenuConfig, String> {
    private final DSLContext dsl;

    @Override
    public void save(MenuConfig data) {
        Map<Field<?>, Object> setData = new HashMap<>();
        setData.put(MENU_CONFIG.ID, data.getId());
        setData.put(MENU_CONFIG.NAME, data.getName());
        setData.put(MENU_CONFIG.PARENT_ID, data.getParentId());
        setData.put(MENU_CONFIG.PATH_ROOT, data.getPathRoot());
        setData.put(MENU_CONFIG.PATH_TYPE, data.getPathType());
        setData.put(MENU_CONFIG.PATH_LINK, data.getPathLink());
        setData.put(MENU_CONFIG.TAG, data.getTag());
        setData.put(MENU_CONFIG.STATUS, data.getStatus());
        dsl.insertInto(MENU_CONFIG)
                .set(setData)
                .set(MENU_CONFIG.CREATE_TIME, DSL.currentLocalDateTime())
                .onDuplicateKeyUpdate()
                .set(setData)
                .set(MENU_CONFIG.UPDATE_TIME, DSL.currentLocalDateTime())
                .returningResult()
                .execute();
    }

    @Override
    public MenuConfig load(String s) {
        return null;
    }

    @Override
    public void delete(String s) {
        dsl.delete(MENU_CONFIG).where(MENU_CONFIG.ID.eq(s)).execute();
    }

    public MenuConfig getById(String id) {
        Record result = dsl.select()
                .from(MENU_CONFIG)
                .where(MENU_CONFIG.ID.eq(id))
                .fetchOne();

        return readBasicInfo(result);
    }

    public List<MenuConfig> getMenusByParentId(String id) {
        List<Record> result = dsl.select()
                .from(MENU_CONFIG)
                .where(MENU_CONFIG.PARENT_ID.eq(id).and(MENU_CONFIG.STATUS.eq(BigDecimal.valueOf(Constants.STATUS_ACTIIVE))))
                .fetch();

        return result.stream()
                .map(this::readBasicInfo)
                .collect(Collectors.toList());
    }

    /**
     * read database value into model
     *
     * @param result database value
     * @return MenuConfig model
     */
    private MenuConfig readBasicInfo(Record result) {
        if (result == null) {
            return null;
        }
        MenuConfig rs = new MenuConfig();
        rs.setId(result.get(MENU_CONFIG.ID));
        rs.setName(result.get(MENU_CONFIG.NAME));
        rs.setParentId(result.get(MENU_CONFIG.PARENT_ID));
        rs.setPathRoot(result.get(MENU_CONFIG.PATH_ROOT));
        rs.setPathType(result.get(MENU_CONFIG.PATH_TYPE));
        rs.setPathLink(result.get(MENU_CONFIG.PATH_LINK));
        rs.setCreateTime(result.get(MENU_CONFIG.CREATE_TIME));
        rs.setUpdateTime(result.get(MENU_CONFIG.UPDATE_TIME));
        rs.setTag(result.get(MENU_CONFIG.TAG));
        rs.setStatus(result.get(MENU_CONFIG.STATUS).longValue());
        return rs;
    }

    public PageData<MenuConfig> getParentMenu(MenuConfigSearch request) {
        var selectQuery = dsl.select()
                .from(MENU_CONFIG)
                .where(MENU_CONFIG.STATUS.eq(BigDecimal.valueOf(Constants.STATUS_ACTIIVE))
                        .and(MENU_CONFIG.PARENT_ID.isNull()));
        if (!StringUtils.isEmpty(request.getName())) {
            selectQuery.and(MENU_CONFIG.NAME.eq(request.getName()));
        }

        return queryPage(selectQuery, request.getPage(), request.getLimit(), dsl)
                .map(this::readBasicInfo);
    }
}
