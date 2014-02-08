// Copyright (c) 2012, CA Inc.  All rights reserved.
package com.ca.platform.osf.object.odata.format;

import org.restlet.ext.jaxrs.internal.core.UriBuilderImpl;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Wrapper for URI Builder to implement query replace - needed for skip token
 * @author betda05
 * Date: 11/15/12
 * Time: 2:29 PM
 */
public class ODataUriBuilder extends UriBuilderImpl
{
  UriInfo _uriInfo;
  UriBuilder _parent;

  private static final String SKIPTOKEN_PARAM_NAME = "$skiptoken";
  private static final String SKIP_PARAM_NAME = "$skip";

  /**
   * CTOR for classs
   * @param uriInfo_ URI Info
   */
  ODataUriBuilder( UriInfo uriInfo_ )
  {
    URI uri = uriInfo_.getRequestUri();
    String uriString = uri.toString();
    if( uriString.contains( "?" ) )
    {
      //
      //  The queryParam method in the UriBuilder that's returned (org.restlet.ext.jaxrs.AbstractUriBuilder) has
      //  a bug in it, if the query is already present the class variable will be a string, this causes the query
      //  to not get updated - It's crap coding, the class variable and the method variable have the same name and
      //  some dufus ignored the compiler warning.....
      //
      try
      {
        String rawQuery = uri.getRawQuery();
        uri = new URI( uriString );
        String[] params = rawQuery.split( "\\&" );
        uri = new URI( uriString.substring( 0, uriString.indexOf( "?" ) ) );
        _parent = UriBuilder.fromUri( uri );
        for( String paramNameValue: params )
        {
          String value = "";
          if( paramNameValue.length() == 0 ) continue;
          if( paramNameValue.contains( "=" ) )
          {
            value = paramNameValue.substring( paramNameValue.indexOf( "=" ) + 1 );
          }
          _parent.queryParam( paramNameValue.substring( 0, paramNameValue.indexOf( "=" ) ), value );
        }
      }
      catch( URISyntaxException ex )
      {
        _parent  = uriInfo_.getRequestUriBuilder();
      }
    }
    else
    {
      _parent  = uriInfo_.getRequestUriBuilder();
    }
    _uriInfo = uriInfo_;
  }

  public UriBuilder replaceQueryParam( String name_, Object... values_ ) throws IllegalArgumentException
  {
    String rawQueryURI = _uriInfo.getRequestUri().getRawQuery();
    //
    //  Use the raw Query to do the replace, may need to translate $skiptoken to $skip as the code in
    //  AtomFeedFormatWriter write is incorrectly using this method with $skiptoken
    //
    if( rawQueryURI != null
        &&
        ( rawQueryURI.contains( name_ )
          ||
          (SKIPTOKEN_PARAM_NAME.equals( name_ ) && rawQueryURI.contains( SKIP_PARAM_NAME ))))
    {
      String newQuery = rawQueryURI;
      String paramName = name_;
      if( SKIPTOKEN_PARAM_NAME.equals( name_ ) )
      {
        paramName = SKIP_PARAM_NAME;
      }

      if( values_.length > 0 )
      {
        for( Object value: values_ )
        {
          if( value != null && rawQueryURI.contains( paramName ) )
          {
            //
            //  Need to replace the string after the name upto the next '&' or the end of string with the value
            //
            newQuery = rawQueryURI.substring( 0, rawQueryURI.indexOf( paramName ) + paramName.length() + 1 );
            int ampIndex = rawQueryURI.indexOf( "&", rawQueryURI.indexOf( paramName ) );
            newQuery += value;
            if( ampIndex != -1 )
            {
              newQuery += rawQueryURI.substring( ampIndex );
            }
          }
        }
      }
      _parent.replaceQuery( newQuery );
    }
    else if( values_ != null && values_.length > 0 )
    {
      if( "$skiptoken".equals( name_ ) )
      {
        //
        //  The spec says to use Skip as the start value, not skip token, but just add the $skip here, the
        //  AtomFeedFormatWriter sets the wrong value
        //
        _parent.queryParam( "$skip", values_[0] );
      }
      else
      {
        _parent.queryParam( name_, values_[0] );
      }
    }
    return this;
  }

  public URI build( Object... objects_ ) throws IllegalArgumentException, UriBuilderException
  {
    return _parent.build( objects_ );
  }

}
