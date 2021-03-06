package com.javamentor.developer.social.platform.dao.impl.model.group;

import com.javamentor.developer.social.platform.dao.abstracts.model.group.GroupHasUserDao;
import com.javamentor.developer.social.platform.dao.impl.GenericDaoAbstract;
import com.javamentor.developer.social.platform.models.entity.group.GroupHasUser;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class GroupHasUserDaoImpl extends GenericDaoAbstract<GroupHasUser, Long> implements GroupHasUserDao {

    @Override
    public boolean verificationUserInGroup(Long groupId, Long userId) {
        TypedQuery<Boolean> queryVerifyUserInGroup = (Query) entityManager.createQuery(
                "SELECT " +
                        "CASE WHEN COUNT(ghu)>0 THEN true ELSE false END " +
                    "FROM GroupHasUser ghu " +
                    "WHERE ghu.user.userId = :paramUserId AND ghu.group.id = :paramGroupId")
                .setParameter("paramUserId", userId)
                .setParameter("paramGroupId", groupId);
        return (boolean) queryVerifyUserInGroup.unwrap(Query.class).getSingleResult();
    }

    @Override
    public int deleteUserById(Long groupId, Long userId) {
        return entityManager.createQuery("DELETE from GroupHasUser ghu" +
                " WHERE ghu.group.id = :paramGroupId AND ghu.user.userId = :paramUserId ")
                .setParameter("paramUserId", userId)
                .setParameter("paramGroupId", groupId).executeUpdate();
    }
}
