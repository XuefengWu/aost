package org.telluriumsource.framework.dj;

/**
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 *         Date: Oct 4, 2010
 */
public interface BeanFactory {

    void addBean(String name, BeanInfo info);

    Object getByName(String name);

    <T> T getByClass(Class<T> clazz);
    
}
