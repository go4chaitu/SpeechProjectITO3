// Copyright (c) 2014, CA Inc.  All rights reserved.
package com.niku.rest.response;

import com.niku.rest.core.ODataFilter;
import com.niku.rest.reports.ReportObject;
import com.niku.rest.speech.SpeechToReportMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JSONRepsonseForRiskyProjects extends AbstractJSONResponse
{
  public List<Map<String, String>> generateResult( String speechText_, String sessionCookie_ )
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
      ODataFilter filter = filters.get( outputKey );
      filter.setBaseURL( hostUrl );
      String url = filter.toString();
      url = setSelectColumns( url, selectColumns );
      String urlResponse = sendRequestJson( url, sessionCookie_ );
      List<Map<String, String>> colMap = processJsonResponse( urlResponse, selectColumns, report );
      result.addAll( colMap );
    }
    return result;
  }

}
