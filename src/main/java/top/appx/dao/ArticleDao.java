package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.appx.entity.Article;
import top.appx.entity.vo.ArticleDetailVO;
import top.appx.entity.vo.ArticleIndexVO;

import java.util.List;

@Component
@Mapper
public interface ArticleDao extends BaseDao<Article> {

    List<ArticleIndexVO> index();

    List<Article> findByArticleGroupId(Long id);

    boolean exist(Article article);

    boolean existUrl(String url);

    ArticleDetailVO detail(Long id);
}
