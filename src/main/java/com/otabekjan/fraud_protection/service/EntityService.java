package com.otabekjan.fraud_protection.service;

import com.otabekjan.fraud_protection.$;
import com.otabekjan.fraud_protection.entity.AppEntity;
import com.vaadin.flow.data.provider.DataProvider;
import io.jmix.core.*;
import io.jmix.core.annotation.Secret;
import io.jmix.core.metamodel.datatype.EnumClass;
import io.jmix.core.metamodel.datatype.Enumeration;
import io.jmix.core.metamodel.datatype.impl.EnumerationImpl;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.core.metamodel.model.MetaProperty;
import io.jmix.core.metamodel.model.MetadataObject;
import io.jmix.core.metamodel.model.Range;
import io.jmix.core.querycondition.PropertyCondition;
import io.jmix.data.PersistenceHints;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: abdul
 * Date: 11/17/2023 2:43 PM
 */

@Service
public class EntityService {
    private static final Logger log = LoggerFactory.getLogger(EntityService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EntityStates entityStates;
    @Autowired
    private Metadata metadata;
    @Autowired
    private DataManager dataManager;

    public <T extends AppEntity<UUID>> T loadById(Class<T> entityClass, UUID entityId) {
        return loadById(entityClass, entityId, null);
    }

    public <T extends AppEntity<UUID>> T loadById(Class<T> entityClass, UUID entityId, @Nullable String fetchPlan) {
        if (entityId == null) return null;

        return dataManager.load(entityClass).id(entityId)
                .fetchPlan(fetchPlan)
                .optional()
                .orElse(null);
    }

    public <T extends AppEntity<UUID>> T loadById(Class<T> entityClass, String entityId) {
        return loadById(entityClass, entityId, null);
    }

    public <T extends AppEntity<UUID>> T loadById(Class<T> entityClass, String entityId, @Nullable String fetchPlan) {
        try {
            return loadById(entityClass, UUID.fromString(entityId), fetchPlan);
        } catch (Exception ignored) {
        }
        return null;
    }

    public <T extends AppEntity<UUID>> T loadById(String entityName, String entityId) {
        try {
            MetaClass aClass = metadata.getClass(entityName);
            return loadById(aClass.getJavaClass(), UUID.fromString(entityId));
        } catch (Exception ignored) {
        }
        return null;
    }

    public <T extends AppEntity<UUID>> T loadById(String entityName, UUID entityId) {
        try {
            return loadById(entityName, entityId, null);
        } catch (Exception ignored) {
        }
        return null;
    }

    public <T extends AppEntity<UUID>> T loadById(String entityName, UUID entityId, @Nullable String fetchPlan) {
        try {
            MetaClass metaClass = metadata.getClass(entityName);
            return loadById(metaClass.getJavaClass(), entityId, fetchPlan);
        } catch (Exception ignored) {
        }
        return null;
    }

    public <T extends AppEntity<UUID>> T loadByIdProps(String entityName, String entityId, String... properties) {

        try {
            MetaClass metaClass = metadata.getClass(entityName);
            Class<T> javaClass = metaClass.getJavaClass();
            return dataManager.load(javaClass).id(UUID.fromString(entityId))
                    .fetchPlanProperties(properties)
                    .optional()
                    .orElse(null);
        } catch (Exception ignored) {
        }
        return null;
    }

    public <T extends AppEntity<UUID>> List<T> loadByIds(String entityName, List<UUID> ids, String fetchPlan) {
        try {
            MetaClass metaClass = metadata.getClass(entityName);
            Class<T> javaClass = metaClass.getJavaClass();
            return loadByIds(javaClass, ids, fetchPlan);
        } catch (Exception ignored) {
        }
        return null;
    }

    public <T extends AppEntity<UUID>> List<T> loadByIds(Class<T> entityClass, List<UUID> ids) {
        return loadByIds(entityClass, ids, null);
    }

    public <T extends AppEntity<UUID>> List<T> loadByIds(Class<T> entityClass, List<UUID> ids, @Nullable String fetchPlan) {
        if ($.isEmpty(ids)) return Collections.emptyList();

        return dataManager.load(entityClass)
                .condition(PropertyCondition.inList("id", ids))
                .fetchPlan(fetchPlan)
                .list();
    }

    public <T extends AppEntity<UUID>> List<T> loadAll(Class<T> entityClass, String... orders) {
        MetaClass aClass = metadata.findClass(entityClass);
        if (aClass == null) return Collections.emptyList();

        FluentLoader.ByQuery<T> loader = dataManager.load(entityClass)
                .query("select e from %s e".formatted(aClass.getName()));

        if (orders != null) loader.sort(Sort.by(orders));

        MetaProperty activeProperty = aClass.findProperty("active");
        if (activeProperty == null) return loader.list();
        return loader.condition(PropertyCondition.equal("active", true)).list();
    }

    public <T extends AppEntity<UUID>> List<T> loadAll(Class<T> entityClass) {
        MetaProperty activeProperty = metadata.getClass(entityClass).findProperty("active");
        FluentLoader<T> loader = dataManager.load(entityClass);
        if (activeProperty == null) return loader.all().list();
        return loader.condition(PropertyCondition.equal("active", true)).list();
    }


    public UUID loadNotMainParentId(String entityName, UUID childId) {
        if (childId == null) return null;
        try {
            return dataManager.loadValue("select e.parent.id from %s e where e.id=:id and e.parent.parent is not null".formatted(entityName), UUID.class)
                    .parameter("id", childId)
                    .optional()
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hasChildren(String entityName, UUID parentId) {
        if (parentId == null) return false;
        return dataManager.loadValue("select count(e) from %s e where e.active=true and e.parent.id=:id".formatted(entityName), Long.class)
                .parameter("id", parentId)
                .one() > 0;
    }

    public <T extends AppEntity<UUID>> T reload(T entity, String fetchPlan) {
        try {
            return dataManager.load(Id.of(entity)).fetchPlan(fetchPlan).one();
        } catch (Exception ignored) {
        }
        return null;
    }

    public <T extends AppEntity<UUID>> T reload(T entity, FetchPlan fetchPlan) {
        try {
            return dataManager.load(Id.of(entity)).fetchPlan(fetchPlan).one();
        } catch (Exception ignored) {
        }
        return null;
    }

    public <T extends AppEntity<UUID>> T reload(T entity) {
        return reload(entity, (String) null);
    }

    public <T> boolean isLoaded(T entity, String property) {
        return entityStates.isLoaded(entity, property);
    }

    public <T extends AppEntity<UUID>> T reloadWithProperties(T entity, String... properties) {
        return dataManager.load(Id.of(entity)).fetchPlanProperties(properties).one();
    }

    public <T extends AppEntity<UUID>> T create(Class<T> entityClass) {
        return dataManager.create(entityClass);
    }

    public <T extends AppEntity<UUID>> T save(T entity) {
        return dataManager.save(entity);
    }

    public EntitySet save(SaveContext context) {
        return dataManager.save(context);
    }

    public <T extends AppEntity<UUID>> void remove(T entity) {
        dataManager.remove(entity);
    }

    public <T extends AppEntity<UUID>> DataProvider<T, String> dataProvider(Class<T> entityClass, String sort, String... filterProps) {
        return dataProvider(entityClass, Collections.emptyMap(), sort, filterProps);
    }

    public <T extends AppEntity<UUID>> DataProvider<T, String> dataProvider(Class<T> entityClass, Map<String, Object> params, String sort, String... filterProps) {
        return DataProvider.fromFilteringCallbacks(
                fetch -> {
                    String filter = fetch.getFilter().orElse(null);
                    LoadContext<T> loadContext = loadContext(entityClass, params, sort, filter, filterProps);
                    loadContext.getQuery().setFirstResult(fetch.getOffset());
                    loadContext.getQuery().setMaxResults(fetch.getLimit());
                    return dataManager.loadList(loadContext).stream();
                },
                count -> {
                    String filter = count.getFilter().orElse(null);
                    LoadContext<T> loadContext = loadContext(entityClass, params, sort, filter, filterProps);
                    loadContext.getQuery().setFirstResult(count.getOffset());
                    loadContext.getQuery().setMaxResults(count.getLimit());
                    return Math.toIntExact(dataManager.getCount(loadContext));
                }
        );
    }

    public <T extends AppEntity<UUID>> LoadContext<T> loadContext(Class<T> entityClass, Map<String, Object> params,
                                                                  String sort, String filter, String... filterProps) {
        MetaClass metaClass = metadata.getClass(entityClass);
        LoadContext<T> loadContext = new LoadContext<>(metaClass);

        String queryString = "select e from %s e".formatted(metaClass.getName());
        String andQuery = null;

        if (!$.isEmpty(params)) {
            StringJoiner and = new StringJoiner(" and ");
            for (String param : params.keySet()) {
                String condition = params.get(param) instanceof Collection<?> ? "in" : "=";
                and.add("e.param %s :param".formatted(condition).replace("param", param));
            }
            andQuery = and.toString();
        }

        if ($.isEmpty(filter) || $.isEmpty(filterProps)) {
            if (andQuery != null) queryString = "%s where %s order by e.%s".formatted(queryString, andQuery, sort);
            loadContext.setQueryString(queryString).setParameters(params);
            return loadContext;
        }

        StringJoiner or = new StringJoiner(" or ", "(", ")");
        for (String prop : filterProps)
            or.add("lower(e.prop) like concat('%',lower(:prop),'%')".replace("prop", prop));

        queryString = "%s where %s".formatted(queryString, or);
        if (andQuery != null) queryString = "%s and %s".formatted(queryString, andQuery);
        queryString = "%s order by e.%s".formatted(queryString, sort);

        loadContext.setQueryString(queryString)
                .setParameters(Arrays.stream(filterProps).collect(Collectors.toMap(key -> key, key -> filter)))
                .setParameters(params);
        return loadContext;
    }

    @Transactional(readOnly = true)
    public <T extends AppEntity<UUID>> boolean exists(Class<T> entityClass, UUID entityId) {
        String tableName = entityClass.getAnnotation(Table.class).name();
        return (Long) entityManager.createNativeQuery("select count(*) from " + tableName + " where id=?")
                .setParameter(1, entityId)
                .getSingleResult() > 0;
    }

    @Transactional
    public <T extends AppEntity<UUID>> void softDelete(T entity) {
        entityManager.setProperty(PersistenceHints.SOFT_DELETION, false);
        entityManager.remove(entity);
    }


    public List<String> getEntityClasses() {
        return metadata.getClasses().stream()
                .filter(metaClass -> metaClass.getJavaClass().isAnnotationPresent(Entity.class))
                .map(MetadataObject::getName)
                .toList();
    }


    public List<String> getEntityAnnotatedFields(Class<? extends Annotation> annotation) {
        List<String> fields = new ArrayList<>();
        for (MetaClass metaClass : metadata.getClasses()) {
            for (Field field : metaClass.getJavaClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Secret.class))
                    fields.add(field.getName());
            }
        }
        return fields;
    }

    public List<String> getEnumClasses() {
        Reflections reflections = new Reflections("com.smartbox.jobster.entity");
        return reflections.getSubTypesOf(EnumClass.class).stream().map(Class::getName).toList();
    }

    public Enumeration<?> enumeration(String name) {
        try {
            Class aClass = Class.forName(name);
            return new EnumerationImpl<>(aClass);
        } catch (Exception e) {
            return null;
        }
    }

    public Object parseEntityField(String entityName, String fieldName, String fieldValue) {
        if ($.isEmpty(entityName) || $.isEmpty(fieldName) || $.isEmpty(fieldValue)) return null;

        MetaClass metaClass = metadata.getClass(entityName);
        MetaProperty property = metaClass.findProperty(fieldName);
        if (property == null) return null;

        Range range = property.getRange();
        Object value = null;

        if (range.isDatatype()) {
            try {
                value = range.asDatatype().parse(fieldValue);
            } catch (Exception e) {
                log.warn("EntityService.parseEntityField: asDatatype - {}", e.getMessage());
            }
        } else if (range.isEnum()) {
            try {
                value = range.asEnumeration().parse(fieldValue);
            } catch (Exception e) {
                log.warn("EntityService.parseEntityField: asEnumeration - {}", e.getMessage());
            }
        } else if (range.isClass() && $.isUUID(fieldValue)) {
            value = loadById(range.asClass().getJavaClass(), UUID.fromString(fieldValue));
        }
        return value;
    }
}
