package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.appx.entity.Article;

import java.util.List;

@Component
@Mapper
public interface ArticleDao extends BaseDao<Article> {

    List<Article> index();

    List<Article> findByArticleGroupId(Long id);

    boolean exist(Article article);

    boolean existUrl(String url);
}
