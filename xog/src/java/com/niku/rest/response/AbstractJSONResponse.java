// Copyright (c) 2014, CA Inc.  All rights reserved.
package com.niku.rest.response;

import com.niku.rest.core.ODataFilter;
import com.niku.rest.reports.ReportObject;
import com.niku.rest.speech.SpeechToReportMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractJSONResponse implements JSONResponseInterface
{

  private static final String USER_AGENT =
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.76 Safari/537.36";

  private static JSONParser parser = new JSONParser();

  String hostUrl = "http://localhost";

  public String generateOutputText( String speechText_, String sessionCookie_ )
  {
    String reportId = SpeechToReportMapper.getReportId( speechText_ );
    String result = "";

    if( reportId == null || reportId.length() == 0 )
    {
      result = "There are no records founds";
    }
    ReportObject report = SpeechToReportMapper.getReport( speechText_ );
    Map<String, ODataFilter> filters = report.getFilterMap();
    List<String> selectColumns = report.getSelectColumns();
    String outputKey = report.getOutputKey();
    Map<String, String> outputTextTemplates = report.getTemplates();

    if( filters != null )
    {
      ODataFilter filter = filters.get( outputKey );
      filter.setBaseURL( hostUrl );
      String url = filter.toString();
      url = setSelectColumns( url, selectColumns );
      String urlResponse = sendRequestJson( url, sessionCookie_ );
      long count = getCount( urlResponse );
      if( count == 0 )
      {
        result = outputTextTemplates.get( "NONE" );
      }
      else
      {
        result = outputTextTemplates.get( "SOME" );
        result = result.replace( "<number>", String.valueOf( count ) );
      }
    }
    else
    {
      result = "There are no records founds";
    }
    return result;
  }

  public List<Map<String, String>> generateHeader( String speechText_, String sessionCookie_ )
  {
    List<Map<String, String>> result = new ArrayList<Map<String, String>>();
    String reportId = SpeechToReportMapper.getReportId( speechText_ );
    if( reportId == null || reportId.length() == 0 )
    {
      return result;
    }

    ReportObject report = SpeechToReportMapper.getReport( speechText_ );


    List<String> selectColumns = report.getSelectColumns();

    if( selectColumns != null )
    {
      for( String columnName : selectColumns )
      {
        Map<String, String> headerColumn = new HashMap<String, String>();
        headerColumn.put( "sTitle", columnName );
        result.add( headerColumn );
      }
    }

    return result;

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

  public abstract List<Map<String, String>> generateResult( String speechText_, String sessionCookie_ );

  public long getCount( String jsonResponse_ )
  {
    long count = 0;
    try
    {
      JSONObject jsonObj = (JSONObject) parser.parse( jsonResponse_ );
      // loop array
      JSONObject d = (JSONObject) jsonObj.get( "d" );
      String countObj = (String) d.get( "__count" );

      if( countObj != null )
      {
        count = Long.parseLong( countObj );
      }
    }
    catch( ParseException ex_ )
    {
      ex_.printStackTrace();
    }
    return count;
  }

  public List<Map<String, String>> processJsonResponse( String jsonResponse_, List<String> selectColumns_,
                                                        ReportObject reportObject_ )
  {
    List<Map<String, String>> columnsData = new ArrayList<Map<String, String>>();

    try
    {
      JSONObject jsonObj = (JSONObject) parser.parse( jsonResponse_ );
      // loop array
      JSONObject d = (JSONObject) jsonObj.get( "d" );
      JSONArray results = (JSONArray) d.get( "results" );

      for( Object obj : results )
      {
        Map<String, String> column = new HashMap<String, String>();
        for( String col : selectColumns_ )
        {
          Map<String, Map<String, String>> columnNameValueMap = reportObject_.getColumnNameToValuesMapper();
          String colValue = (String) ((JSONObject) obj).get( col );
          Set<String> columnNameKeys = columnNameValueMap.keySet();
          if( columnNameKeys != null && columnNameKeys.contains( col ) )
          {
            Map<String, String> columnValueMapper = columnNameValueMap.get( col );

            if( columnValueMapper != null )
            {
              String newColValue = columnValueMapper.get( colValue );
              colValue = (newColValue != null) ? newColValue : colValue;
            }
          }
          column.put( col, colValue );
        }
        columnsData.add( column );
      }
    }
    catch( ParseException ex_ )
    {
      ex_.printStackTrace();
    }
    return columnsData;
  }

  public String setSelectColumns( String url_, List<String> selectColumns_ )
  {

    if( !url_.contains( "$select" ) && selectColumns_ != null && selectColumns_.size() > 0 )
    {
      String cols = "";
      for( String col : selectColumns_ )
      {
        cols += col + ",";
      }
      url_ += "&$select=" + cols.substring( 0, cols.length() - 1 ) + "&";
    }
    return url_;
  }

  public String sendRequestJson( String url, String sessionCookie_ )
  {
    HttpClient httpclient = HttpClientBuilder.create().build();
    HttpGet getRequest = new HttpGet( url );

    getRequest.setHeader( "Host", "localhost" );
    getRequest.setHeader( "User-Agent", USER_AGENT );
    getRequest.setHeader( "Accept",
      "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" );
    getRequest.setHeader( "Accept-Language", "en-US,en;q=0.5" );
    getRequest.setHeader( "Cookie", sessionCookie_ );
    getRequest.setHeader( "Connection", "keep-alive" );
    getRequest.setHeader( "Content-Type", "application/json;odata=verbose" );

    StringBuffer result = null;
    HttpResponse response = null;
    try
    {
      response = httpclient.execute( getRequest );
      if( response.getStatusLine().getStatusCode() != 200 )
      {
        System.out.println( "Failed : HTTP error code : " + response.getStatusLine().getStatusCode() );
      }

      BufferedReader br = new BufferedReader(
        new InputStreamReader( (response.getEntity().getContent()) ) );

      String output;
      result = new StringBuffer();
      while( (output = br.readLine()) != null )
      {
        result.append( output );
      }
    }
    catch( ClientProtocolException ex )
    {
      ex.printStackTrace();
    }
    catch( IOException ex )
    {
      ex.printStackTrace();
    }
    return result.toString();
  }

  public void setHostUrl( String hostUrl_ )
  {
    hostUrl = hostUrl_;
  }
}
