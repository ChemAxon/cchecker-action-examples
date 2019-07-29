package com.chemaxon.ccapiclient.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * Initializes fields marked with the @Log annotation.
 */
@Component
public class LogInjector implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        ReflectionUtils.doWithFields(bean.getClass(), field -> {
            // make the field accessible if defined private
            ReflectionUtils.makeAccessible(field);
            if (field.getAnnotation(Log.class) != null) {
                Logger log = LoggerFactory.getLogger(bean.getClass());
                field.set(bean, log);
            }
        });
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
