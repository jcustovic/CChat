package hr.chus.cchat.db.service.jpa;

import hr.chus.cchat.db.repository.LanguageRepository;
import hr.chus.cchat.db.service.OperatorService;
import hr.chus.cchat.model.db.jpa.Language;
import hr.chus.cchat.model.db.jpa.Operator;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA/Hibernate DAO implementation of operator services.
 * 
 * @author Jan Čustović (jan.custovic@gmail.com)
 */
@Service("operatorService")
@Transactional
public class OperatorServiceImpl implements OperatorService {

    @PersistenceContext
    private EntityManager                entityManager;

    @Autowired
    private transient ShaPasswordEncoder shaPasswordEncoder;

    @Autowired
    private transient LanguageRepository languageRepository;

    @Override
    public void addOperator(Operator operator) {
        entityManager.persist(operator);
    }

    @Override
    public Operator authenticateUser(String username, String password) {
        Operator user = getOperatorByUsername(username);
        if (user == null) {
            return null;
        }
        if (user.getUsername().equals(username) && shaPasswordEncoder.encodePassword(password, null).equals(user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public void removeOperator(Operator operator) {
        operator = entityManager.getReference(Operator.class, operator.getId());
        entityManager.remove(operator);
    }

    @Override
    public Operator updateOperator(Operator operator) {
        return entityManager.merge(operator);
    }

    @Override
    @Transactional(readOnly = true)
    public Operator getOperatorByUsername(String username) {
        try {
            return (Operator) entityManager.createNamedQuery("Operator.getByUsername").setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Operator> getAllOperators() {
        return entityManager.createNamedQuery("Operator.getAll").getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkIfUsernameExists(Operator operator) {
        Operator operatorE = getOperatorByUsername(operator.getUsername());
        if (operatorE == null) {
            return false;
        }
        if (operatorE.getId().equals(operator.getId())) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Operator getOperatorById(Integer id) {
        return entityManager.find(Operator.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Operator> getAllActiveOperators() {
        return entityManager.createNamedQuery("Operator.getAllByActiveFlag").setParameter("active", true).getResultList();
    }

    @Override
    public final Operator save(final Operator p_operator, final List<Integer> p_languages) {
        final Operator operator;
        if (p_operator.getId() == null) {
            operator = entityManager.merge(p_operator);
        } else {
            operator = entityManager.find(Operator.class, p_operator.getId());
            mapFromDto(p_operator, operator);

            final List<Language> languages = new LinkedList<Language>();
            for (Integer langId : p_languages) {
                languages.add(languageRepository.findOne(langId));
            }

            operator.getLanguages().addAll(languages);
            operator.getLanguages().retainAll(languages);
        }

        return operator;
    }

    // TODO: User real DTO object and use dozer for mapping
    private void mapFromDto(Operator p_operatorDto, Operator p_operator) {
        p_operator.setUsername(p_operatorDto.getUsername());
        p_operator.setSurname(p_operatorDto.getSurname());
        p_operator.setIsActive(p_operatorDto.getIsActive());
        p_operator.setDisabled(p_operatorDto.getDisabled());
        p_operator.setPassword(p_operatorDto.getPassword());
        p_operator.setName(p_operatorDto.getName());
        p_operator.setEmail(p_operatorDto.getEmail());
        p_operator.setIsExternal(p_operatorDto.getIsExternal());
        p_operator.setRole(p_operatorDto.getRole());
    }

}
