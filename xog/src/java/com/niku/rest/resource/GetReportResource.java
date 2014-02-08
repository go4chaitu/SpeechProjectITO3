// Copyright (c) 2014, CA Inc.  All rights reserved.
package com.niku.rest.resource;

import com.niku.rest.core.ODataURLConstants;
import com.niku.rest.core.ResponseParseFactory;
import com.niku.rest.reports.ReportObject;
import com.niku.rest.response.JSONResponseInterface;
import com.niku.rest.speech.SpeechToReportMapper;
import com.niku.union.security.SecurityIdentifier;
import com.niku.union.security.UserSessionController;
import com.niku.union.security.UserSessionControllerFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetReportResource extends BaseResource
{

  private String _sessionCookie = null;
  private String _hostUrl = null;
  private String _sessionId = null;
  private HttpClient client = HttpClientBuilder.create().build();

  public String processRequest( Map json, String method )
  {
    String returnString = "";
    _sessionId = (json.get( "sessionId" ) != null) ? json.get( "sessionId" ).toString() : null;
    String typeStr = (json.get( "type" ) != null) ? json.get( "type" ).toString() : null;
    _hostUrl = (json.get( "hostUrl" ) != null) ? json.get( "hostUrl" ).toString() : null;

    int type = 1;
    if( typeStr != null && typeStr.length() > 0 )
    {
      try
      {
        type = Integer.parseInt( typeStr );
      }
      catch( Exception ex )
      {
        ex.printStackTrace();
      }
    }
    _sessionCookie = (json.get( "sessionId" ) != null) ? "sessionId=" + json.get( "sessionId" ).toString() : null;
    String speechText = (json.get( "speechText" ) != null) ? json.get( "speechText" ).toString() : null;
    Object result = processSpeechText( speechText, type );
    returnString = new ResponseParseFactory().getSuccessJsonObject( result );
    return returnString;
  }

  private String getUserName()
  {
    SecurityIdentifier securityIdentifier = null;
    UserSessionController usController = null;
    try
    {
      usController = UserSessionControllerFactory.getInstance();
      securityIdentifier = usController.get( _sessionId );
    }
    catch( com.niku.union.security.SecurityException ex )
    {
      ex.printStackTrace();
    }

    return (securityIdentifier != null) ? securityIdentifier.getUserName() : null;
  }

  private Object processSpeechText( String speechText_, int type_ )
  {
    ReportObject report = SpeechToReportMapper.getReport( speechText_ );
    if( report == null )
    { return ""; }
    JSONResponseInterface dispatcher = report.getJsonResponseDispatcher( _hostUrl );
    switch( type_ )
    {
      case ODataURLConstants.JSON_OUTPUT_TYPE_DATATABLE:
        Map<String, Object> outputDatatable = new HashMap<String, Object>();

        List<Map<String, String>> headerList = dispatcher.generateHeader( speechText_, _sessionCookie );
        List<Map<String, String>> dataList = dispatcher.generateResult( speechText_, _sessionCookie );
        if( headerList != null && headerList.size() > 0 )
        {
          outputDatatable.put( "aoColumns", headerList );
          outputDatatable.put( "data", dataList );
          return outputDatatable;
        }
        else
        {
          return "";
        }


      case ODataURLConstants.JSON_OUTPUT_TYPE_PIE:
        List<Map<String, String>> outputDatapie = dispatcher.generateSummary( speechText_, _sessionCookie );
        return outputDatapie;

      case ODataURLConstants.JSON_OUTPUT_TYPE_SUMMARY:
        String outputText = dispatcher.generateOutputText( speechText_, _sessionCookie );
        return outputText;
      default:
        return "";
    }
  }

}