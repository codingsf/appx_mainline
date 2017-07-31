package top.appx.service;

import com.github.pagehelper.PageInfo;
import top.appx.entity.ArticleGroup;
import top.appx.entity.vo.ArticleGroupVO;

import java.util.List;

public interface ArticleGroupService {
    void save(ArticleGroup articleGroup);
    PageInfo<ArticleGroup> findPage(ArticleGroup search, int pageNum, int pageSize);
    void deleteByIds(List<Long> ids);
    List<ArticleGroupVO> index(Long usreId);
    ArticleGroup findById(Long id);
    ArticleGroupVO selectVO(long articleGroupId,Long userId);
    void subscribe(Long id, long userId, boolean subscribe);
}
