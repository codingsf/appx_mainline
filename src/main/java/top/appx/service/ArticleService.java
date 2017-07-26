package top.appx.service;

import com.github.pagehelper.PageInfo;
import top.appx.entity.Article;
import top.appx.entity.User;

import java.util.List;

public interface ArticleService {
    PageInfo<Article> findPage(Integer pageNum , Integer pageSize) throws Exception;
    Article findById(Long id);

    void save(Article article);

    boolean saveIfNotExistUrl(Article article);

    boolean existUrl(String url);

    void deleteByIds(List<Long> ids);

    List<Article> index();

    List<Article> findByArticleGroupId(Long id);


}
