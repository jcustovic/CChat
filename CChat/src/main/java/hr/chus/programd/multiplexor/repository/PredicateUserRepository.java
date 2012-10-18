package hr.chus.programd.multiplexor.repository;

import hr.chus.programd.multiplexor.jpa.PredicateUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface PredicateUserRepository extends JpaRepository<PredicateUser, Integer> {

    PredicateUser findByUserIdAndBotId(String p_userid, String p_botid);

    PredicateUser findByUserIdAndBotIdAndPassword(String p_userid, String p_botid, String p_password);

}
