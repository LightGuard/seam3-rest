package org.jboss.seam.rest.example.jaxrs.exceptions.ftest;

import org.testng.annotations.Test;

public class XmlBasedConfigurationTest extends AbstractExceptionHandlingTest {
    @Test
    public void testStatusCode() throws Exception {
        checkResponse("/exceptions?exception=java.lang.StringIndexOutOfBoundsException", 500, null);
    }

    @Test
    public void testMessageBody() throws Exception {
        checkResponse("/exceptions?exception=java.lang.ArrayIndexOutOfBoundsException", 500,
                "<error><message>ArrayIndexOutOfBoundsException was thrown.</message></error>");
    }

    @Test
    public void testExpressionLanguage() throws Exception {
        checkResponse("/exceptions?exception=java.lang.IndexOutOfBoundsException", 500,
                "<error><message>An exception was thrown. The date is: ");
    }
}
