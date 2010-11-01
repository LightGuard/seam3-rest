/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.rest.client;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;

import org.jboss.logging.Logger;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.seam.rest.util.Annotations;
import org.jboss.seam.rest.util.DelegatingInjectionTarget;

public class RestClientInjectionTarget<T> extends DelegatingInjectionTarget<T>
{
   private BeanManager beanManager;
   private ClientExecutor clientExecutor;

   private Map<Field, RestClient> clientRequestFields = new HashMap<Field, RestClient>();
   private Map<Field, RestClient> webServiceFields = new HashMap<Field, RestClient>();
   
   private static final Logger log = Logger.getLogger(RestClientInjectionTarget.class);

   public RestClientInjectionTarget(AnnotatedType<T> annotatedType, InjectionTarget<T> delegate, BeanManager beanManager)
   {
      super(delegate);
      this.beanManager = beanManager;

      findInjectableFields(annotatedType);
      
      if (clientRequestFields.isEmpty() && webServiceFields.isEmpty())
      {
         log.warnf("@RestClient injection activated for {} which does not contain any @RestClient fields.", annotatedType.getJavaClass());
      }
   }
   
   @Override
   public void inject(T instance, CreationalContext<T> ctx)
   {
      super.inject(instance, ctx);
      
      if (clientExecutor == null)
      {
         clientExecutor = lookupClientExecutor();
      }
      
      for (Entry<Field, RestClient> entry : clientRequestFields.entrySet())
      {
         ClientRequest clientRequest = new ClientRequest(entry.getValue().value(), clientExecutor);
         injectField(entry.getKey(), instance, clientRequest);
      }
      for (Entry<Field, RestClient> entry : webServiceFields.entrySet())
      {
         Field field = entry.getKey();
         Object proxy = ProxyFactory.create(field.getType(), entry.getValue().value(), clientExecutor);
         injectField(field, instance, proxy);
      }
   }
   
   private void findInjectableFields(AnnotatedType<T> annotatedType)
   {
      for (AnnotatedField<?> field : annotatedType.getFields())
      {
         RestClient annotation = Annotations.getAnnotation(field.getAnnotations(), RestClient.class);
         if (annotation != null)
         {
            if (ClientRequest.class.equals(field.getBaseType()))
            {
               clientRequestFields.put(field.getJavaMember(), annotation);
               log.infof("Found @RestClient injection point {}", field); // TODO
               continue;
            }
            if (field.getJavaMember().getType().isInterface())
            {
               webServiceFields.put(field.getJavaMember(), annotation);
               log.infof("Found @RestClient injection point {}", field); // TODO
               continue;
            }
            log.warnf("@RestClient injection of {} is not supported.", field.getJavaMember().getType());
         }
      }
   }

   private void injectField(Field field, T instance, Object value)
   {
      try
      {
         field.set(instance, value);
      }
      catch (IllegalAccessException e)
      {
         throw new RuntimeException("Unable to inject.", e); // TODO
      }
   }
   
   private ClientExecutor lookupClientExecutor()
   {
      // TODO checks
      Set<Bean<?>> beans = beanManager.getBeans(ClientExecutor.class);
      Bean<?> bean = beanManager.resolve(beans);
      CreationalContext<?> context = beanManager.createCreationalContext(bean);
      return (ClientExecutor) beanManager.getReference(bean, ClientExecutor.class, context);
   }
}
