package org.carbon.persistent.conf;

import org.carbon.component.Switcher;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.modular.env.EnvironmentMapper;
import org.carbon.persistent.hibernate.HibernateConfiguration;
import org.carbon.persistent.jooq.JooqConfiguration;
import org.carbon.persistent.prop.PersistentImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author garden 2018/01/07.
 */
public interface PersistentImplSwitcher extends Switcher {
}
