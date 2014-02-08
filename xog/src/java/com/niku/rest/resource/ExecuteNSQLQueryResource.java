// Copyright (c) 2014, CA Inc.  All rights reserved.
package com.niku.rest.resource;

import com.niku.rest.core.ResponseParseFactory;
import com.niku.union.persistence.PersistenceApplicationException;
import com.niku.union.persistence.PersistenceConversation;
import com.niku.union.persistence.PersistenceException;
import com.niku.union.security.*;
import com.niku.union.utility.UtilityThreadLocal;

import java.util.Map;

public class ExecuteNSQLQueryResource extends BaseResource
{
  private static final String NSQL1 = "SELECT 'STRING' str, 123 num FROM dual";

  private static final String NSQL2 = "SELECT 'STRING' str, 123 num FROM dual where 1 = " +
  		                                "@Where:PaRaM:USER_DEF:INTEGER:NUM@";

  public String processRequest( Map json, String method )
  {
    String returnString = "";
    SecurityIdentifier securityIdentifier = null;
    PersistenceConversation conversation = null;
    UserSessionController usController = null;
    String sessionId = (json.get( "sessionId" ) != null) ? json.get( "sessionId" ).toString() : null;
    if( sessionId != null )
    {
      try
      {
        usController = UserSessionControllerFactory.getInstance();
        securityIdentifier = usController.get( sessionId );
        conversation = new PersistenceConversation( "nmc.lockUser", securityIdentifier );
        conversation.setParameter( "id", "" );
        conversation.process();

      }
      catch( com.niku.union.security.SecurityException ex )
      {
        ex.printStackTrace();
      }
      catch( PersistenceException ex )
      {
        ex.printStackTrace();
      }
      catch( PersistenceApplicationException ex )
      {
        ex.printStackTrace();
      }
    }
    return returnString;
  }
}