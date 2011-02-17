package org.jboss.seam.rest.example.tasks.statistics.geo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JAXB class used for parsing http://geonames.org XML response.
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 *
 */
@XmlRootElement(name = "geonames")
public class SearchResult
{
   private Integer resultCount;
   private List<Location> locations;

   @XmlElement(name = "totalResultsCount")
   public Integer getResultCount()
   {
      return resultCount;
   }

   public void setResultCount(Integer resultCount)
   {
      this.resultCount = resultCount;
   }

   @XmlElement(name = "code")
   public List<Location> getLocations()
   {
      return locations;
   }

   public void setLocations(List<Location> locations)
   {
      this.locations = locations;
   }
   
   public static class Location
   {
      private String postalCode;
      private String name;
      private String countryCode;
      private Double latitude;
      private Double longitude;

      @XmlElement(name = "postalcode")
      public String getPostalCode()
      {
         return postalCode;
      }

      public void setPostalCode(String postalCode)
      {
         this.postalCode = postalCode;
      }

      @XmlElement
      public String getName()
      {
         return name;
      }

      public void setName(String name)
      {
         this.name = name;
      }

      @XmlElement
      public String getCountryCode()
      {
         return countryCode;
      }

      public void setCountryCode(String countryCode)
      {
         this.countryCode = countryCode;
      }

      @XmlElement(name = "lat")
      public Double getLatitude()
      {
         return latitude;
      }

      public void setLatitude(Double latitude)
      {
         this.latitude = latitude;
      }

      @XmlElement(name = "lng")
      public Double getLongitude()
      {
         return longitude;
      }

      public void setLongitude(Double longitude)
      {
         this.longitude = longitude;
      }
   }
}
