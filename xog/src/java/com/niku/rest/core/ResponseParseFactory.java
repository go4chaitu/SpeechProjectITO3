// Copyright (c) 2014, CA Inc.  All rights reserved.
package com.niku.rest.core;

import org.apache.log4j.Logger;
import org.json.simple.JSONValue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResponseParseFactory
{

  static Logger logger = Logger.getLogger( ResponseParseFactory.class );

  @SuppressWarnings({"rawtypes", "unchecked"})
  public String getFailureJsonString( String msg )
  {
    String jsonString = "";
    LinkedHashMap list = new LinkedHashMap();
    list.put( "response_status", "false" );

    list.put( "result", msg + "" );
    jsonString = JSONValue.toJSONString( list );
    logger.info( jsonString );
    return jsonString;
  }

 @SuppressWarnings({ "rawtypes", "unchecked" })
 public String getSuccessJsonString(String msg){
  String jsonString = "";
  LinkedHashMap list = new LinkedHashMap();
  list.put("response_status","true");

  list.put("result", msg);
  jsonString = JSONValue.toJSONString(list);
  logger.info(jsonString);
  return jsonString;
 }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public String getSuccessJsonObject( Object result_ )
  {
    String jsonString = "";
    jsonString = JSONValue.toJSONString( result_ );
    logger.info( jsonString );
    return jsonString;
  }
}
