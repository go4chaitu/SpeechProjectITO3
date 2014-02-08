// Copyright (c) 2014, CA Inc.  All rights reserved.
package com.niku.rest.reports;

import com.niku.rest.core.ODataFilter;
import com.niku.rest.response.JSONResponseInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@author Chaitanya Saragadam
 */
public class ReportObject
{
  private String reportId = null;
  private String outputKey = null;
  Map<String, ODataFilter> filterMap = new HashMap<String, ODataFilter>();
  Map<String, String> templates = new HashMap<String, String>();
  List<String> selectColumns = new ArrayList<String>();
  int count = 20;  //default count

  JSONResponseInterface jsonResponseDispatcher = null;

  Map<String,Map<String, String>> columnNameToValuesMapper = new HashMap<String, Map<String,String>>();

  public String getReportId()
  {
    return reportId;
  }

  public void setReportId( String reportId_ )
  {
    reportId = reportId_;
  }

  public String getOutputKey()
  {
    return outputKey;
  }

  public void setOutputKey( String outputKey_ )
  {
    outputKey = outputKey_;
  }

  public Map<String, ODataFilter> getFilterMap()
  {
    return filterMap;
  }

  public void setFilterMap( Map<String, ODataFilter> filterMap_ )
  {
    filterMap = filterMap_;
  }

  public Map<String, String> getTemplates()
  {
    return templates;
  }

  public void setTemplates( Map<String, String> templates_ )
  {
    templates = templates_;
  }

  public List<String> getSelectColumns()
  {
    return selectColumns;
  }

  public void setSelectColumns( List<String> selectColumns_ )
  {
    selectColumns = selectColumns_;
  }

  public Map<String, Map<String, String>> getColumnNameToValuesMapper()
  {
    return columnNameToValuesMapper;
  }

  public void setColumnNameToValuesMapper( Map<String, Map<String, String>> columnNameToValuesMapper_ )
  {
    columnNameToValuesMapper = columnNameToValuesMapper_;
  }

  public int getCount()
  {
    return count;
  }

  public void setCount( int count_ )
  {
    count = count_;
  }

  public JSONResponseInterface getJsonResponseDispatcher( String hostUrl_ )
  {
    if( !hostUrl_.startsWith( "http" ) )
    {
      hostUrl_ = "http://" + hostUrl_;
    }
    jsonResponseDispatcher.setHostUrl( hostUrl_ );
    return jsonResponseDispatcher;
  }

  public void setJsonResponseDispatcher( JSONResponseInterface jsonResponseDispatcher_ )
  {
    jsonResponseDispatcher = jsonResponseDispatcher_;
  }
}
