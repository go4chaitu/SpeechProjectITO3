// Copyright (c) 2014, CA Inc.  All rights reserved.
package com.niku.rest.resource;

import com.niku.rest.core.ResponseParseFactory;

import java.util.Map;

public class HelloWorldResource extends BaseResource
{

 public String processRequest(Map json, String method) {
  String returnString = "" ;
  returnString = new ResponseParseFactory().getSuccessJsonString("Hello " + json.get("user"));
  return returnString;
 }
}