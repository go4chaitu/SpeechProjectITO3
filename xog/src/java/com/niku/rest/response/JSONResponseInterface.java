// Copyright (c) 2014, CA Inc.  All rights reserved.
package com.niku.rest.response;

import com.niku.rest.reports.ReportObject;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: sarch04
 * Date: 2/7/14
 * Time: 8:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface JSONResponseInterface
{

  String generateOutputText( String speechText_, String sessionCookie_ );

  List<Map<String, String>> generateHeader( String speechText_, String sessionCookie_ );

  List<Map<String, String>> generateSummary( String speechText_, String sessionCookie_ );

  List<Map<String, String>> generateResult( String speechText_, String sessionCookie_ );

  long getCount( String jsonResponse_ );

  List<Map<String, String>> processJsonResponse( String jsonResponse_, List<String> selectColumns_,
                                                 ReportObject reportObject_ );

  String setSelectColumns( String url_, List<String> selectColumns_ );

  String sendRequestJson( String url, String sessionCookie_ );

  void setHostUrl( String url );
}
