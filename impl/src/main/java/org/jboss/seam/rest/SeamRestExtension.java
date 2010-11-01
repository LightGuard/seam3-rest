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
package org.jboss.seam.rest;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

import org.jboss.seam.rest.exceptions.SeamExceptionMapper;
import org.jboss.seam.rest.util.ExpressionLanguageInterpolator;
import org.jboss.seam.rest.util.Interpolator;
import org.jboss.seam.rest.validation.ValidationExceptionMapper;
import org.jboss.seam.rest.validation.ValidationInterceptor;

public class SeamRestExtension implements Extension
{
   void registerExtension(@Observes BeforeBeanDiscovery event, BeanManager manager)
   {
      event.addAnnotatedType(manager.createAnnotatedType(Interpolator.class));
      event.addAnnotatedType(manager.createAnnotatedType(ExpressionLanguageInterpolator.class));
      
      event.addAnnotatedType(manager.createAnnotatedType(SeamExceptionMapper.class));
      event.addAnnotatedType(manager.createAnnotatedType(ValidationExceptionMapper.class));
      event.addAnnotatedType(manager.createAnnotatedType(ValidationInterceptor.class));
   }
}
