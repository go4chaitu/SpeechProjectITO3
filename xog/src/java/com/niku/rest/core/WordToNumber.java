// Copyright (c) 2014, CA Inc.  All rights reserved.
package com.niku.rest.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: sarch04
 * Date: 2/8/14
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class WordToNumber
{
  private static Map<String, Integer> wordMap = new HashMap<String, Integer>();

  static
  {
    init();
  }

  public static int inNumerals( String inwords )
  {
    int wordnum = -1;
    String[] arrinwords = inwords.split( " " );
    int arrinwordsLength = arrinwords.length;
    if( inwords.equals( "zero" ) )
    {
      return 0;
    }
    if( inwords.contains( "thousand" ) )
    {
      int indexofthousand = inwords.indexOf( "thousand" );
      //System.out.println(indexofthousand);
      String beforethousand = inwords.substring( 0, indexofthousand );
      //System.out.println(beforethousand);
      String[] arrbeforethousand = beforethousand.split( " " );
      int arrbeforethousandLength = arrbeforethousand.length;
      //System.out.println(arrbeforethousandLength);
      if( arrbeforethousandLength == 2 )
      {
        wordnum = wordnum + 1000 * (wordtonum( arrbeforethousand[0] ) + wordtonum( arrbeforethousand[1] ));
        //System.out.println(wordnum);
      }
      if( arrbeforethousandLength == 1 )
      {
        wordnum = wordnum + 1000 * (wordtonum( arrbeforethousand[0] ));
        //System.out.println(wordnum);
      }

    }
    if( inwords.contains( "hundred" ) )
    {
      int indexofhundred = inwords.indexOf( "hundred" );
      //System.out.println(indexofhundred);
      String beforehundred = inwords.substring( 0, indexofhundred );

      //System.out.println(beforehundred);
      String[] arrbeforehundred = beforehundred.split( " " );
      int arrbeforehundredLength = arrbeforehundred.length;
      wordnum = wordnum + 100 * (wordtonum( arrbeforehundred[arrbeforehundredLength - 1] ));
      String afterhundred = inwords.substring( indexofhundred + 8 );//7 for 7 char of hundred and 1 space
      //System.out.println(afterhundred);
      String[] arrafterhundred = afterhundred.split( " " );
      int arrafterhundredLength = arrafterhundred.length;
      if( arrafterhundredLength == 1 )
      {
        wordnum = wordnum + (wordtonum( arrafterhundred[0] ));
      }
      if( arrafterhundredLength == 2 )
      {
        wordnum = wordnum + (wordtonum( arrafterhundred[1] ) + wordtonum( arrafterhundred[0] ));
      }
      //System.out.println(wordnum);

    }
    if( !inwords.contains( "thousand" ) && !inwords.contains( "hundred" ) )
    {
      if( arrinwordsLength == 1 )
      {
        wordnum = wordnum + (wordtonum( arrinwords[0] ));
      }
      if( arrinwordsLength == 2 )
      {
        wordnum = wordnum + (wordtonum( arrinwords[1] ) + wordtonum( arrinwords[0] ));
      }
      //System.out.println(wordnum);
    }


    return wordnum;
  }

  public static int wordtonum( String wordTxt_ )
  {
    return wordMap.get( wordTxt_ ) != null? wordMap.get( wordTxt_): -1;
  }

  public static void init()
  {
    wordMap.put( "one", 1 );
    wordMap.put( "two", 2 );
    wordMap.put( "three", 3 );
    wordMap.put( "four", 4 );
    wordMap.put( "five", 5 );
    wordMap.put( "six", 6 );
    wordMap.put( "seven", 7 );
    wordMap.put( "eight", 8 );
    wordMap.put( "nine", 9 );
    wordMap.put( "ten", 10 );
    wordMap.put( "eleven", 11 );
    wordMap.put( "twelve", 12 );
    wordMap.put( "thirteen", 13 );
    wordMap.put( "fourteen", 14 );
    wordMap.put( "fifteen", 15 );
    wordMap.put( "sixteen", 16 );
    wordMap.put( "seventeen", 17 );
    wordMap.put( "eighteen", 18 );
    wordMap.put( "nineteen", 19 );
    wordMap.put( "twenty", 20 );
    wordMap.put( "thirty", 30 );
    wordMap.put( "fourty", 40 );
    wordMap.put( "fifty", 50 );
    wordMap.put( "sixty", 60 );
    wordMap.put( "seventy", 70 );
    wordMap.put( "eighty", 80 );
    wordMap.put( "ninety", 90 );
    wordMap.put( "hundred", 100 );
    wordMap.put( "thousand", 1000 );
  }
}
