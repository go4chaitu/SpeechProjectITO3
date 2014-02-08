// Copyright (c) 2014, CA Inc.  All rights reserved.
package com.niku.rest.resource;

import com.niku.rest.core.ResponseParseFactory;
import com.niku.union.security.*;
import com.niku.union.utility.UtilityThreadLocal;

import java.util.Map;

public class LoginResource extends BaseResource
{

  public String processRequest( Map json, String method )
  {
    String returnString = "";
    SecurityIdentifier securityIdentifier = null;
    String userName = (json.get( "username" ) != null) ? json.get( "username" ).toString() : null;
    String password = (json.get( "password" ) != null) ? json.get( "password" ).toString() : null;

    UtilityThreadLocal.init( "clarity" );
    Authenticator authenticator = null;

    try
    {
      authenticator = AuthenticatorFactory.getInstance();
      authenticator.setForce( true );
      String uid = authenticator.authenticate( userName, password );
      securityIdentifier = UserSessionControllerFactory.getInstance().init( uid, null );
    }
    catch( com.niku.union.security.SecurityException ex )
    {
      ex.printStackTrace();
    }
    String sessionID = securityIdentifier != null? securityIdentifier.getSessionId(): "NOT VALID CREDENTIALS";
    userName = securityIdentifier.getUserName();

    returnString = new ResponseParseFactory().getSuccessJsonString( "SessionId=" + sessionID );
    return returnString;
  }
}