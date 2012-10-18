package hr.chus.programd.multiplexor.repository;

import java.util.List;

import hr.chus.programd.multiplexor.jpa.Predicate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface PredicateRepository extends JpaRepository<Predicate, Integer> {

    List<Predicate> findByUserIdAndBotIdAndName(String p_userid, String p_botid, String p_name);

}
