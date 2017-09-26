package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.appx.entity.vo.ArticleGroupVO;
import top.appx.entity.ArticleGroup;

import java.util.List;
import java.util.Map;

@Component
@Mapper
public interface ArticleGroupDao extends BaseDao<ArticleGroup> {

    ArticleGroup findByName(String name);

    ArticleGroup findByFlag(String flag);

    List<ArticleGroupVO> index(@Param("userId") Long userId);

    ArticleGroupVO selectVOByMap(Map<String,Object> map);

    void subscribe(@Param("articleGroupId") Long articleGroupId, @Param("userId") long userId);

    void unsubscribe(@Param("articleGroupId") Long articleGroupId, @Param("userId") long userId);

    void updateLastArticle(@Param("articleId") Long articleId,@Param("id") long articleGroupId);

}
