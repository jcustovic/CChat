package hr.chus.cchat.db.repository;

import hr.chus.cchat.model.db.jpa.Language;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Transactional(readOnly = true)
public interface LanguageRepository extends JpaRepository<Language, Integer> {

}
