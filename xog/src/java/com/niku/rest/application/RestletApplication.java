// Copyright (c) 2014, CA Inc.  All rights reserved.
package com.niku.rest.application;

import com.niku.rest.resource.GetReportResource;
import com.niku.rest.resource.LoginResource;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import com.niku.rest.resource.HelloWorldResource;

public class RestletApplication extends Application{
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/helloWorld",HelloWorldResource.class );
        router.attach("/login",LoginResource.class );
        router.attach("/getReportData",GetReportResource.class );
        return router;
    }
}
