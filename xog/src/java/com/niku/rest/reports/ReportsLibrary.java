// Copyright (c) 2014, CA Inc.  All rights reserved.
package com.niku.rest.reports;

import com.niku.rest.core.ODataFilter;
import com.niku.rest.core.ODataURLConstants;
import com.niku.rest.response.JSONRepsonseForAllProjects;
import com.niku.rest.response.JSONRepsonseForRisks;
import com.niku.rest.response.JSONRepsonseForRiskyProjects;
import com.niku.rest.response.JSONRepsonseForRiskyProjectsWithParams;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chaitanya Saragadam
 */
public class ReportsLibrary
{
  private static final String USER_AGENT =
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.76 Safari/537.36";
  private static JSONParser parser = new JSONParser();

  public Map<String, ReportObject> reports = null;
  private static ReportsLibrary instance = null;

  public static ReportsLibrary getInstance()
  {
    if( instance == null )
    {
      instance = new ReportsLibrary();
    }
    return instance;
  }

  private ReportsLibrary()
  {
    reports = new HashMap<String, ReportObject>();
    ReportObject riskyProjectsReport = createRiskyProjectsReport();
    ReportObject myProjectsReport = createMyProjectsReport();
    ReportObject riskyProjectsReportWithParam = createRiskyReportWithParams();
    ReportObject risks = createAllRisksReport();

    reports.put( riskyProjectsReport.getReportId(), riskyProjectsReport );
    reports.put( myProjectsReport.getReportId(), myProjectsReport );
    reports.put( riskyProjectsReportWithParam.getReportId(), riskyProjectsReportWithParam );
    reports.put( risks.getReportId(), risks );
  }

  public ReportObject getReport( String reportId_ )
  {
    return reports.get( reportId_ );
  }

  private ReportObject createRiskyProjectsReport()
  {
    ReportObject reportObject = new ReportObject();

    reportObject.setJsonResponseDispatcher( new JSONRepsonseForRiskyProjects() );
    Map<String, ODataFilter> filterMap = new HashMap<String, ODataFilter>();
    // status_indicator == 1 means GREEN, 2 means YELLOW, 3 means RED
    ODataFilter filterToFindGREENProjects = new ODataFilter( "status_indicator", "1", ODataURLConstants.OPERATOR_EQUALS,
      "http://localhost", "/niku/odata/projects", 10 );
    filterMap.put( "On Track", filterToFindGREENProjects );
    ODataFilter filterToFindYELLOWProjects =
      new ODataFilter( "status_indicator", "2", ODataURLConstants.OPERATOR_EQUALS,
        "http://localhost", "/niku/odata/projects", 10 );
    filterMap.put( "Critical", filterToFindYELLOWProjects );
    ODataFilter filterToFindREDProjects = new ODataFilter( "status_indicator", "3", ODataURLConstants.OPERATOR_EQUALS,
      "http://localhost", "/niku/odata/projects", 10 );
    filterMap.put( "At Risk", filterToFindREDProjects );
    reportObject.setFilterMap( filterMap );

    Map<String, String> templates = new HashMap();
    templates.put( "NONE", "Good News. There are no risk projects." );
    templates.put( "SOME", "There are <number> risky projects." );
    reportObject.setTemplates( templates );

    reportObject.setOutputKey( "At Risk" );
    reportObject.setReportId( ODataURLConstants.REPORT_KEY_RISKYPROJECTS );

    List<String> selectColumns = new ArrayList<String>();
    selectColumns.add( "name" );
    selectColumns.add( "unique_code" );
    selectColumns.add( "status_indicator" );
    reportObject.setSelectColumns( selectColumns );
    Map<String, Map<String, String>> columnNameToValuesMapper = new HashMap<String, Map<String, String>>();
    Map<String, String> colunmValueMapper = new HashMap<String, String>();
    colunmValueMapper.put( "1", "On Track" );
    colunmValueMapper.put( "2", "Critical" );
    colunmValueMapper.put( "3", "At Risk" );
    columnNameToValuesMapper.put( "status_indicator", colunmValueMapper );

    reportObject.setColumnNameToValuesMapper( columnNameToValuesMapper );
    return reportObject;
  }

  private ReportObject createMyProjectsReport()
  {
    ReportObject reportObject = new ReportObject();
    reportObject.setJsonResponseDispatcher( new JSONRepsonseForAllProjects() );
    Map<String, ODataFilter> filterMap = new HashMap<String, ODataFilter>();
    // status_indicator == 1 means GREEN, 2 means YELLOW, 3 means RED
    ODataFilter filterToFindGREENProjects = new ODataFilter( "status_indicator", "1", ODataURLConstants.OPERATOR_EQUALS,
      "http://localhost", "/niku/odata/projects", 10 );
    filterMap.put( "On Track", filterToFindGREENProjects );
    ODataFilter filterToFindYELLOWProjects =
      new ODataFilter( "status_indicator", "2", ODataURLConstants.OPERATOR_EQUALS,
        "http://localhost", "/niku/odata/projects", 10 );
    filterMap.put( "Critical", filterToFindYELLOWProjects );
    ODataFilter filterToFindREDProjects = new ODataFilter( "status_indicator", "3", ODataURLConstants.OPERATOR_EQUALS,
      "http://localhost", "/niku/odata/projects", 10 );
    filterMap.put( "At Risk", filterToFindREDProjects );
    reportObject.setFilterMap( filterMap );

    Map<String, String> templates = new HashMap();
    templates.put( "NONE", "Good News. There are no risk projects." );
    templates.put( "SOME", "There are <number> risky projects." );
    reportObject.setTemplates( templates );

    reportObject.setOutputKey( "At Risk" );
    reportObject.setReportId( ODataURLConstants.REPORT_KEY_MYPROJECTS );

    List<String> selectColumns = new ArrayList<String>();
    selectColumns.add( "name" );
    selectColumns.add( "unique_code" );
    selectColumns.add( "status_indicator" );
    reportObject.setSelectColumns( selectColumns );
    Map<String, Map<String, String>> columnNameToValuesMapper = new HashMap<String, Map<String, String>>();
    Map<String, String> colunmValueMapper = new HashMap<String, String>();
    colunmValueMapper.put( "1", "On Track" );
    colunmValueMapper.put( "2", "Critical" );
    colunmValueMapper.put( "3", "At Risk" );
    columnNameToValuesMapper.put( "status_indicator", colunmValueMapper );

    reportObject.setColumnNameToValuesMapper( columnNameToValuesMapper );
    return reportObject;
  }

  private ReportObject createRiskyReportWithParams()
  {
    ReportObject reportObject = new ReportObject();
    reportObject.setJsonResponseDispatcher( new JSONRepsonseForRiskyProjectsWithParams() );
    Map<String, ODataFilter> filterMap = new HashMap<String, ODataFilter>();
    // status_indicator == 1 means GREEN, 2 means YELLOW, 3 means RED
    ODataFilter filterToFindGREENProjects = new ODataFilter( "status_indicator", "1", ODataURLConstants.OPERATOR_EQUALS,
      "http://localhost", "/niku/odata/projects", 10 );
    filterMap.put( "On Track", filterToFindGREENProjects );
    ODataFilter filterToFindYELLOWProjects =
      new ODataFilter( "status_indicator", "2", ODataURLConstants.OPERATOR_EQUALS,
        "http://localhost", "/niku/odata/projects", 10 );
    filterMap.put( "Critical", filterToFindYELLOWProjects );
    ODataFilter filterToFindREDProjects = new ODataFilter( "status_indicator", "3", ODataURLConstants.OPERATOR_EQUALS,
      "http://localhost", "/niku/odata/projects", 10 );
    filterMap.put( "At Risk", filterToFindREDProjects );
    reportObject.setFilterMap( filterMap );

    Map<String, String> templates = new HashMap();
    templates.put( "NONE", "Good News. There are no risk projects." );
    templates.put( "SOME", "There are <number> risky projects." );
    reportObject.setTemplates( templates );

    reportObject.setOutputKey( "At Risk" );
    reportObject.setReportId( ODataURLConstants.REPORT_KEY_RISKYPROJECTS_WITH_PARAMS );

    List<String> selectColumns = new ArrayList<String>();
    selectColumns.add( "name" );
    selectColumns.add( "unique_code" );
    selectColumns.add( "status_indicator" );
    reportObject.setSelectColumns( selectColumns );
    Map<String, Map<String, String>> columnNameToValuesMapper = new HashMap<String, Map<String, String>>();
    Map<String, String> colunmValueMapper = new HashMap<String, String>();
    colunmValueMapper.put( "1", "On Track" );
    colunmValueMapper.put( "2", "Critical" );
    colunmValueMapper.put( "3", "At Risk" );
    columnNameToValuesMapper.put( "status_indicator", colunmValueMapper );

    reportObject.setColumnNameToValuesMapper( columnNameToValuesMapper );
    return reportObject;
  }

  private ReportObject createAllRisksReport()
  {
    ReportObject reportObject = new ReportObject();
    reportObject.setJsonResponseDispatcher( new JSONRepsonseForRisks() );
    Map<String, ODataFilter> filterMap = new HashMap<String, ODataFilter>();
    // status_indicator == 1 means GREEN, 2 means YELLOW, 3 means RED
    ODataFilter filterToFindFlexibility =
      new ODataFilter( "category_type", "rcf_flexibility", ODataURLConstants.OPERATOR_EQUALS,
        ODataURLConstants.DATATYPE_STRING,
        "http://localhost", "/niku/odata/risks", 10 );
    filterMap.put( "Flexibility", filterToFindFlexibility );

    ODataFilter filterToFindFunding =
      new ODataFilter( "category_type", "rcf_funding", ODataURLConstants.OPERATOR_EQUALS,
        ODataURLConstants.DATATYPE_STRING,
        "http://localhost", "/niku/odata/risks", 10 );
    filterMap.put( "Funding", filterToFindFunding );

    ODataFilter filterToFindObjectives =
      new ODataFilter( "category_type", "rcf_objectives", ODataURLConstants.OPERATOR_EQUALS,
        ODataURLConstants.DATATYPE_STRING,
        "http://localhost", "/niku/odata/risks", 10 );
    filterMap.put( "Objectives", filterToFindObjectives );

    ODataFilter filterToFindHumanInterface =
      new ODataFilter( "category_type", "rcf_human_interface", ODataURLConstants.OPERATOR_EQUALS,
        ODataURLConstants.DATATYPE_STRING,
        "http://localhost", "/niku/odata/risks", 10 );
    filterMap.put( "Human Interface", filterToFindHumanInterface );

    ODataFilter filterToFindImplementation =
      new ODataFilter( "category_type", "rcf_implementation", ODataURLConstants.OPERATOR_EQUALS,
        ODataURLConstants.DATATYPE_STRING,
        "http://localhost", "/niku/odata/risks", 10 );
    filterMap.put( "Human Interface", filterToFindImplementation );

    reportObject.setFilterMap( filterMap );

    Map<String, String> templates = new HashMap();
    templates.put( "NONE", "Good News. There are no risks for your projects." );
    templates.put( "SOME", "There are risks for your projects." );
    reportObject.setTemplates( templates );

    reportObject.setOutputKey( "Human Interface" );
    reportObject.setReportId( ODataURLConstants.REPORT_KEY_RISKS );

    List<String> selectColumns = new ArrayList<String>();
    selectColumns.add( "category_type" );
    reportObject.setSelectColumns( selectColumns );
    Map<String, Map<String, String>> columnNameToValuesMapper = new HashMap<String, Map<String, String>>();
    Map<String, String> colunmValueMapper = new HashMap<String, String>();
    colunmValueMapper.put( "rcf_flexibility", "Flexibility" );
    colunmValueMapper.put( "rcf_funding", "Funding" );
    colunmValueMapper.put( "rcf_objectives", "Objectives" );
    colunmValueMapper.put( "rcf_human_interface", "Human Interface" );
    colunmValueMapper.put( "rcf_implementation", "Implementation" );
    columnNameToValuesMapper.put( "category_type", colunmValueMapper );

    reportObject.setColumnNameToValuesMapper( columnNameToValuesMapper );
    return reportObject;
  }

}
