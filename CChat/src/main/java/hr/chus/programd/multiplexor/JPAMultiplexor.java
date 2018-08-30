package hr.chus.programd.multiplexor;

import hr.chus.programd.multiplexor.jpa.Predicate;
import hr.chus.programd.multiplexor.jpa.PredicateUser;
import hr.chus.programd.multiplexor.repository.PredicateRepository;
import hr.chus.programd.multiplexor.repository.PredicateUserRepository;
import org.aitools.programd.Core;
import org.aitools.programd.multiplexor.DuplicateUserIDError;
import org.aitools.programd.multiplexor.Multiplexor;
import org.aitools.programd.multiplexor.NoSuchPredicateException;
import org.aitools.programd.util.DeveloperError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JPAMultiplexor extends Multiplexor {

    private static final Logger LOG = LoggerFactory.getLogger(JPAMultiplexor.class);

    private static final String ENC_UTF8 = "UTF-8";

    @Autowired
    private transient PredicateUserRepository userRepository;

    @Autowired
    private transient PredicateRepository predicateRepository;

    private Map<String, Map<String, String>> userCacheForBots;

    public JPAMultiplexor(final Core p_core) {
        super(p_core);
    }

    @Override
    public void initialize() {
        userCacheForBots = new HashMap<String, Map<String, String>>();
    }

    @Override
    public void savePredicate(final String p_name, final String p_value, final String p_userid, final String p_botid) {
        final String encodedValue;
        try {
            encodedValue = URLEncoder.encode(p_value.trim(), ENC_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new DeveloperError("This platform does not support UTF-8!", e);
        }

        final List<Predicate> predicates = predicateRepository.findByUserIdAndBotIdAndName(p_userid, p_botid, p_name);
        if (predicates.size() == 0) {
            final Predicate predicate = new Predicate();
            predicate.setBotId(p_botid);
            predicate.setUserId(p_userid);
            predicate.setName(p_name);
            predicate.setValue(encodedValue);

            predicateRepository.save(predicate);
        } else if (predicates.size() > 0) {
            // Update first
            final Predicate predicateToUpdate = predicates.get(0);
            predicateToUpdate.setValue(encodedValue);
            predicateRepository.save(predicateToUpdate);

            // Delete rest.
            for (int i = 1; i < predicates.size(); i++) {
                predicateRepository.delete(i);
            }
        }
    }

    @Override
    public String loadPredicate(final String p_name, final String p_userid, final String p_botid) throws NoSuchPredicateException {
        final List<Predicate> predicates = predicateRepository.findByUserIdAndBotIdAndName(p_userid, p_botid, p_name);
        if (predicates.isEmpty()) {
            throw new NoSuchPredicateException(p_name);
        } else {
            final String predicate = predicates.get(0).getValue();
            try {
                return URLDecoder.decode(predicate, ENC_UTF8);
            } catch (IllegalArgumentException e) {
                LOG.warn("Error decoding value " + predicate + ". Will delete predicate", e);
                predicateRepository.delete(predicates.get(0));

                throw new NoSuchPredicateException(p_name);
            } catch (UnsupportedEncodingException e) {
                throw new DeveloperError("This platform does not support UTF-8!", e);
            }
        }
    }

    @Override
    public boolean checkUser(final String p_userid, final String p_password, final String p_botid) {
        // Look first to see if the user is already in the cache.
        if (!userCacheForBots.containsKey(p_botid)) {
            userCacheForBots.put(p_botid, Collections.checkedMap(new HashMap<String, String>(), String.class, String.class));
        }
        final Map<String, String> userCache = this.userCacheForBots.get(p_botid);
        if (userCache.containsKey(p_userid)) {
            // If so, check against stored password.
            if ((userCache.get(p_userid)).equals(p_password)) {
                return true;
            }
            // (otherwise...)
            return false;
        }

        final PredicateUser user = userRepository.findByUserIdAndBotIdAndPassword(p_userid, p_botid, p_password);

        return user != null;
    }

    @Override
    public void createUser(final String p_userid, final String p_password, final String p_botid) throws DuplicateUserIDError {
        final PredicateUser user = userRepository.findByUserIdAndBotId(p_userid, p_botid);
        if (user == null) {
            final PredicateUser newUser = new PredicateUser();
            newUser.setBotId(p_botid);
            newUser.setPassword(p_password);
            newUser.setUserId(p_userid.trim().toLowerCase());

            userRepository.save(newUser);
        } else {
            throw new DuplicateUserIDError(p_userid);
        }
    }

    @Override
    public boolean changePassword(final String p_userid, final String p_password, final String p_botid) {
        final PredicateUser user = userRepository.findByUserIdAndBotId(p_userid, p_botid);
        if (user != null) {
            user.setPassword(p_password);
            userRepository.save(user);
            Map<String, String> userCache = userCacheForBots.get(p_botid);
            userCache.remove(p_userid);
            userCache.put(p_userid, p_password);

            return true;
        }

        return false;
    }

    @Override
    public int useridCount(final String p_botid) {
        return userCacheForBots.get(p_botid).size();
    }

}
