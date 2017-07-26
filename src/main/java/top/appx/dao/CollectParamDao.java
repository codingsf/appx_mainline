package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.appx.entity.CollectParam;

@Component
@Mapper
public interface CollectParamDao extends BaseDao<CollectParam> {

    void insertAutoSetId(CollectParam collectParam);

    void updateLastError(CollectParam collectParam);
    void updateLastSuccess(CollectParam collectParam);
}
