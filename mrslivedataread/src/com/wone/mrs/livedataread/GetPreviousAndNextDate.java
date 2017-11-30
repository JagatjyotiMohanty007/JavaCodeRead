package com.wone.mrs.livedataread;

import java.util.*;
import java.text.*;

public class GetPreviousAndNextDate{
  public static void main(String[] args) {
  int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
  Date date = new Date();
  SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
  String prevDate = dateFormat.format(date.getTime() - MILLIS_IN_DAY);
  String currDate = dateFormat.format(date.getTime());
  String nextDate = dateFormat.format(date.getTime() + MILLIS_IN_DAY);
  System.out.println("Previous date: " + prevDate);
  System.out.println("Currnent date: " + currDate);
  System.out.println("Next date: " + nextDate);
  }
}