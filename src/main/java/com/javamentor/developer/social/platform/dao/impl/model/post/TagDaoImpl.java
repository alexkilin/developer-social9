package com.javamentor.developer.social.platform.dao.impl.model.post;

import com.javamentor.developer.social.platform.dao.abstracts.model.post.TagDao;
import com.javamentor.developer.social.platform.dao.impl.GenericDaoAbstract;
import com.javamentor.developer.social.platform.dao.util.SingleResultUtil;
import com.javamentor.developer.social.platform.models.entity.post.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl extends GenericDaoAbstract<Tag, Long> implements TagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Tag> getTagByText(String text) {
        TypedQuery<Tag> query = entityManager.createQuery("SELECT t FROM Tag t WHERE t.text = : text", Tag.class)
                                             .setParameter("text", text);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    @Override
    public List<Tag> getTagsByText(List<String> texts) {
        List<Tag> tags = entityManager.createQuery(
                "SELECT t FROM Tag t WHERE t.text IN (:texts)", Tag.class)
                .setParameter("texts", texts)
                .getResultList();

        return tags;
    }

}
