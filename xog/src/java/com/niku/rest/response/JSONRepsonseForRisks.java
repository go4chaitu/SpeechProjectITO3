// Copyright (c) 2014, CA Inc.  All rights reserved.
package com.niku.rest.response;

import com.niku.rest.core.ODataFilter;
import com.niku.rest.reports.ReportObject;
import com.niku.rest.speech.SpeechToReportMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONRepsonseForRisks extends AbstractJSONResponse
{

  public String generateOutputText( String speechText_, String sessionCookie_ )
  {
    StringBuffer outputContent = new StringBuffer();
    outputContent = outputContent.append( "There are " );
    List<Map<String, String>> result = generateSummary( speechText_, sessionCookie_ );
    if( result == null || result.size() == 0 )
    {
      outputContent = outputContent.append( "no risks " );
    }
    for( Map<String,String> entry: result )
    {
      outputContent.append( " " ).append( entry.get("value") ).append( " " ).append( entry.get("label") ).append( " risks " );
    }
    outputContent = outputContent.append( "found for your projects" );
    return outputContent.toString();
  }

  public List<Map<String, String>> generateHeader( String speechText_, String sessionCookie_ )
  {
    return new ArrayList<Map<String, String>>();
  }

  public List<Map<String, String>> generateSummary( String speechText_, String sessionCookie_ )
  {
    List<Map<String, String>> result = new ArrayList<Map<String, String>>();
    String reportId = SpeechToReportMapper.getReportId( speechText_ );

    if( reportId == null || reportId.length() == 0 )
    {
      return result;
    }

    ReportObject report = SpeechToReportMapper.getReport( speechText_ );
    Map<String, ODataFilter> filters = report.getFilterMap();
    List<String> selectColumns = report.getSelectColumns();
    String outputKey = report.getOutputKey();
    if( filters != null )
    {
      for( String key : filters.keySet() )
      {
        ODataFilter filter = filters.get( key );
        filter.setBaseURL( hostUrl );
        String url = filter.toString();
        url = setSelectColumns( url, selectColumns );
        String urlResponse = sendRequestJson( url, sessionCookie_ );
        long count = getCount( urlResponse );
        if( count > 0 )
        {
          Map<String, String> categoryCount = new HashMap<String, String>();
          categoryCount.put( "label", key );
          categoryCount.put( "value", String.valueOf( count ) );
          result.add( categoryCount );
        }
      }
    }
    return result;
  }

  public List<Map<String, String>> generateResult( String speechText_, String sessionCookie_ )
  {
    return new ArrayList<Map<String, String>>();
  }

}
