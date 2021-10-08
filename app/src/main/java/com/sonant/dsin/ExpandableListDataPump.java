package com.sonant.dsin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> cricket = new ArrayList<String>();
        cricket.add("India");
        cricket.add("Pakistan");
        cricket.add("Australia");
        cricket.add("England");
        cricket.add("South Africa");

        List<String> football = new ArrayList<String>();
        football.add("Brazil");
        football.add("Sain");
        football.add("Germany");
        football.add("Netherlands");
        football.add("Italy");

        List<String> basketball = new ArrayList<String>();
        basketball.add("United States");
        basketball.add("Spain");
        basketball.add("Argentina");
        basketball.add("France");
        basketball.add("Russia");

        expandableListDetail.put("CRICKET TEAMS", cricket);
        expandableListDetail.put("FOOTBALL TEAMS", football);
        expandableListDetail.put("BASKETBALL TEAMS", basketball);
        return expandableListDetail;
    }
    public static HashMap<String, List<SubjectAdapter>> getData1() {
        HashMap<String, List<SubjectAdapter>> expandableListDetail1 = new HashMap<String, List<SubjectAdapter>>();

        List<SubjectAdapter> cricket = new ArrayList<>();

        List<SubjectAdapter> football = new ArrayList<>();

        List<SubjectAdapter> basketball = new ArrayList<>();

        expandableListDetail1.put("CRICKET TEAMS", cricket);
        expandableListDetail1.put("FOOTBALL TEAMS", football);
        expandableListDetail1.put("BASKETBALL TEAMS", basketball);
        return expandableListDetail1;
    }




}
