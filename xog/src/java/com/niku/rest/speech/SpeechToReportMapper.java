// Copyright (c) 2014, CA Inc.  All rights reserved.
package com.niku.rest.speech;

import com.niku.rest.core.ODataURLConstants;
import com.niku.rest.core.WordToNumber;
import com.niku.rest.reports.ReportObject;
import com.niku.rest.reports.ReportsLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Chaitanya Saragadam
 */
public class SpeechToReportMapper
{
  private static Map<String, String> speechToQueryMap = new HashMap<String, String>();

  private static final Pattern countPattern = Pattern.compile( ".*\\s+([0-9]+)\\s+.*" );

  private static Map<String, List<Pattern>> reportPatterns = new HashMap<String, List<Pattern>>();

  public static void initPatterns()
  {
    List<Pattern> patterns = new ArrayList<Pattern>();
    patterns.add( Pattern.compile( ".*[h][o][w].*[p][r][o][j][e][c][t].*[d][o][i][n][g]|[p][e][r][f][o][r][m].*", Pattern.CASE_INSENSITIVE ) );
    patterns.add( Pattern.compile( ".*[w][h][a][t].*[s][t][a][t][u][s].*[p][r][o][j][e][c][t].*", Pattern.CASE_INSENSITIVE ) );

    reportPatterns.put( ODataURLConstants.REPORT_KEY_MYPROJECTS, patterns );

    patterns = new ArrayList<Pattern>();
    patterns.add( Pattern.compile( ".*[p][r][o][j][e][c][t].*[r][e][v][i][e][w].*", Pattern.CASE_INSENSITIVE ) );
    patterns.add( Pattern.compile( ".*[p][r][o][j][e][c][t].*[a][t][t][e][n][t][i][o][n].*", Pattern.CASE_INSENSITIVE ) );

    reportPatterns.put( ODataURLConstants.REPORT_KEY_RISKYPROJECTS, patterns );

    patterns = new ArrayList<Pattern>();
    patterns.add( Pattern.compile( ".*[t][o][p].*[r][i][s][k][s].*", Pattern.CASE_INSENSITIVE ) );
    patterns.add( Pattern.compile( ".*[p][r][o][j][e][c][t].*[r][i][s][k][s].*", Pattern.CASE_INSENSITIVE ) );
    patterns.add( Pattern.compile( ".*[r][i][s][k][s].*[m][i][t][i][g][a][t][e].*", Pattern.CASE_INSENSITIVE ) );

    reportPatterns.put( ODataURLConstants.REPORT_KEY_RISKS, patterns );

    patterns = new ArrayList<Pattern>();
    patterns.add( Pattern.compile( ".*[t][o][p].*[p][r][o][j][e][c][t].*", Pattern.CASE_INSENSITIVE ) );
    patterns.add( Pattern.compile( ".*[p][r][o][j][e][c][t].*[t][o][p].*", Pattern.CASE_INSENSITIVE ) );

    reportPatterns.put( ODataURLConstants.REPORT_KEY_RISKYPROJECTS_WITH_PARAMS, patterns );



  }

  static
  {
//    speechToQueryMap.put( "How are my projects doing".toLowerCase(), "GetMyProjects" );
//    speechToQueryMap.put( "How are my projects performing".toLowerCase(), "GetMyProjects" );
//    speechToQueryMap.put( "What is the status of my projects".toLowerCase(), "GetMyProjects" );
//
//    speechToQueryMap.put( "Which are the projects I need to review".toLowerCase(), "GetRiskProjects" );
//    speechToQueryMap.put( "Are there any projects that need my attention".toLowerCase(), "GetRiskProjects" );
//
//    speechToQueryMap.put( "What are my top 10 risky projects".toLowerCase(), "GetRiskProjectsWithParams" );
    initPatterns();
  }

  public static String getReportId( String speechText_ )
  {
    String reportId = null;
    for( String key : reportPatterns.keySet() )
    {
      for( Pattern pattern : reportPatterns.get( key ) )
      {
        Matcher matcher = pattern.matcher( speechText_ );
        // check all occurance
        if( matcher.find() )
        {
          reportId = key;
          break;
        }
      }
      if( reportId != null )
      { break; }
    }

    return reportId;
  }

  public static ReportObject getReport( String speechText_ )
  {
    ReportObject reportObject = null;
    String reportId = null;
    int count = 20; // default number of result set.
    for( String key : reportPatterns.keySet() )
    {
      for( Pattern pattern : reportPatterns.get( key ) )
      {
        Matcher matcher = pattern.matcher( speechText_ );
        // check all occurance
        if( matcher.find() )
        {
          reportId = key;
          break;
        }
      }
      if( reportId != null )
      { break; }
    }

    reportObject = ReportsLibrary.getInstance().getReport( reportId );

    if( ODataURLConstants.REPORT_KEY_RISKYPROJECTS_WITH_PARAMS.equals( reportId ) )
    {

      String[] words = speechText_.split("\\s");
      StringBuffer speechText = new StringBuffer();
      for( String word: words )
      {
        int num = WordToNumber.wordtonum( word );
        if( num > -1 )
        {
          speechText = speechText.append( num ).append( " " );
        }
        else
        {
          speechText = speechText.append( word ).append( " " );
        }
      }

      Matcher matcher = countPattern.matcher( speechText.toString() );
      if( matcher.find() )
        {
          String countStr = matcher.group(1);
          if( countStr != null && countStr.length() > 0 )
          {
            try
            {
              count = Integer.parseInt( countStr );
              reportObject.setCount( count );
            }
            catch( NumberFormatException ex )
            {
              int num = WordToNumber.wordtonum( countStr );
              if( num > -1 )
              {
                reportObject.setCount( num );
              }
            }
          }
          else
          {
            reportObject.setCount( 20 );
          }
        }
    }
    return reportObject;
  }
}
