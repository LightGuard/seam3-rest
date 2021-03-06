package org.jboss.seam.rest.test.exceptions;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.jboss.seam.rest.exceptions.ExceptionMapping;
import org.jboss.seam.rest.exceptions.SeamExceptionMapper;

@ExceptionMapping.List({
        @ExceptionMapping(exceptionType = IllegalThreadStateException.class, status = 415),
        @ExceptionMapping(exceptionType = NoSuchFieldError.class, status = 416, message = "NoSuchField", useJaxb = false),
        @ExceptionMapping(exceptionType = NumberFormatException.class, status = 417, useExceptionMessage = true, useJaxb = false),
        @ExceptionMapping(exceptionType = StringIndexOutOfBoundsException.class, status = 418, useExceptionMessage = true, useJaxb = false) })
@ApplicationPath("/*")
public class MyApplication extends Application {
    // According to spec, this method does not need to be overridden - that would trigger scanning
    // Unfortunatelly, RESTEasy does not implement this properly
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(Resource.class);
        classes.add(MoreSpecificExceptionMapper.class);
        classes.add(SeamExceptionMapper.class);
        return classes;
    }
}
