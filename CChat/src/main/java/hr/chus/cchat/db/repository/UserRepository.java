package hr.chus.cchat.db.repository;

import java.util.Date;
import java.util.List;

import hr.chus.cchat.model.db.jpa.Operator;
import hr.chus.cchat.model.db.jpa.User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT COUNT(u) FROM User u WHERE u.operator = ?1 AND u.unreadMsgCount > 0")
    Long countByOperatorAndUnread(Operator p_operator);

    @Query("SELECT u FROM User u WHERE u.operator IS NULL AND u.unreadMsgCount > 0")
    List<User> findUnassigned(Pageable p_pageable);

    @Modifying
    @Query("UPDATE User u SET u.operator = null WHERE u.operator = ?1 AND u.lastMsg < ?2 AND u.unreadMsgCount = 0")
    void clearOperatorField(Operator p_operator, Date p_date);

}
