package top.appx.service;

import com.github.pagehelper.PageInfo;
import top.appx.entity.Article;
import top.appx.entity.vo.ArticleDetailVO;
import top.appx.entity.vo.ArticleIndexVO;

import java.util.List;

public interface ArticleService {
    PageInfo<Article> findPage(Object search,Integer pageNum , Integer pageSize) throws Exception;
    PageInfo<ArticleIndexVO> findPageVO(Object search,Integer pageNum, Integer pageSize) throws Exception;

    Article findById(Long id);

    void save(Article article);

    boolean saveIfNotExistUrl(Article article);

    boolean existUrl(String url);

    void deleteByIds(List<Long> ids);

    List<ArticleIndexVO> index();

    List<ArticleIndexVO> articles();

    List<Article> findByArticleGroupId(Long id);

    ArticleDetailVO detail(Long id);

    void update(Article article);
}
