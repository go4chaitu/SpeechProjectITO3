// Copyright (c) 2014, CA Inc.  All rights reserved.
package com.niku.rest.core;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sarch04
 * Date: 2/6/14
 * Time: 11:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class ODataFilter
{
  private String attributeCode;
  private String attributeValue;
  private int operator;
  private String baseURL;
  private int dataType;
  private String relativePath;
  private int searchCount = 20; //default value
  private String searchFilter = "&$top=";
  private StringBuffer filtrQuery = new StringBuffer();

  public ODataFilter( String attrCode_, String attrValue_, int operator_, String baseURL_, String relativePath_,
                      int searchCount_,
                      List<String> selectColumns_ )
  {
    attributeCode = attrCode_;
    attributeValue = attrValue_;
    operator = operator_;
    dataType = ODataURLConstants.DATATYPE_NUMBER;
    baseURL = baseURL_;
    searchCount = searchCount_;
    filtrQuery = new StringBuffer();
    filtrQuery = filtrQuery.append( "format=json&" );
    if( selectColumns_ != null && selectColumns_.size() > 0 )
    {
      String cols = "";
      for( String col : selectColumns_ )
      {
        cols += col + ",";
      }
      filtrQuery = filtrQuery.append( "$select=" ).append( cols.substring( 0, cols.length() - 1 ) ).append( "&" );
    }
  }

  public ODataFilter( String attrCode_, String attrValue_, int operator_, String baseURL_, String relativePath_,
                      int searchCount_ )
  {
    attributeCode = attrCode_;
    attributeValue = attrValue_;
    operator = operator_;
    dataType = ODataURLConstants.DATATYPE_NUMBER;
    baseURL = baseURL_;
    relativePath = relativePath_;
    searchCount = searchCount_;
    filtrQuery = filtrQuery.append( "$format=json&" );
    filtrQuery = filtrQuery.append( "$inlinecount=allpages&" );

    updateFilter();
  }

  public ODataFilter( String attrCode_, String attrValue_, int operator_, int datatype_, String baseURL_,
                      String relativePath_, int searchCount_ )
  {
    attributeCode = attrCode_;
    attributeValue = attrValue_;
    operator = operator_;
    dataType = datatype_;
    baseURL = baseURL_;
    relativePath = relativePath_;
    searchCount = searchCount_;
    filtrQuery = filtrQuery.append( "$format=json&" );
    filtrQuery = filtrQuery.append( "$inlinecount=allpages&" );

    updateFilter();
  }

  public int getSearchCount()
  {
    return searchCount;
  }

  public void setSearchCount( int searchCount_ )
  {
    searchCount = searchCount_;
  }

  public String getBaseURL()
  {
    return baseURL;
  }

  public void setBaseURL( String baseURL_ )
  {
    baseURL = baseURL_;
  }

  @Override
  public String toString()
  {
    return baseURL + relativePath + "?" + filtrQuery.toString() +
           ((searchCount > 0) ? (searchFilter + String.valueOf( searchCount )) : "");
  }

  public void updateFilter()
  {
    switch( operator )
    {
      case ODataURLConstants.OPERATOR_EQUALS:
        filtrQuery =
          filtrQuery.append( "$filter=" ).append( attributeCode ).append( "%20eq%20" ).append( ((dataType == ODataURLConstants.DATATYPE_STRING)?"%27":"") + attributeValue + ((dataType == ODataURLConstants.DATATYPE_STRING)?"%27":""));
        break;
      case ODataURLConstants.OPERATOR_GREATER_THAN:
        filtrQuery =
          filtrQuery.append( "$filter=" ).append( attributeCode ).append( "%20gt%20" ).append( ((dataType == ODataURLConstants.DATATYPE_STRING)?"%27":"") + attributeValue + ((dataType == ODataURLConstants.DATATYPE_STRING)?"%27":"") );
        break;
      case ODataURLConstants.OPERATOR_LESS_THAN:
        filtrQuery =
          filtrQuery.append( "$filter=" ).append( attributeCode ).append( "%20lt%20" ).append( ((dataType == ODataURLConstants.DATATYPE_STRING)?"%27":"") + attributeValue + ((dataType == ODataURLConstants.DATATYPE_STRING)?"%27":"") );
        break;
      case ODataURLConstants.OPERATOR_SUBSTRING:
        filtrQuery =
          filtrQuery.append( "$filter=substringof(%20" ).append( attributeValue ).append( "," ).append( attributeCode )
            .append( ")" );
        break;
      default:
        break;
    }
  }
}
