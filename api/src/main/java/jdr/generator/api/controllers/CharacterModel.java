package jdr.generator.api.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CharacterModel {

    @JsonProperty("name")
    public String name;

    @JsonProperty("age")
    public int age;

    @JsonProperty("age")
    public String birthPlace;

    @JsonProperty("age")
    public String residenceLocation;

    @JsonProperty("age")
    public String reasonForResidence;

    @JsonProperty("climate")
    public String climate;

    @JsonProperty("common_problems")
    public String commonProblems;

    @JsonProperty("daily_routine")
    public String dailyRoutine;

    @JsonProperty("parents_alive")
    public Boolean parentsAlive;

    @JsonProperty("details_about_parents")
    public String detailsAboutParents;

    @JsonProperty("feelings_about_parents")
    public String feelingsAboutParents;

    @JsonProperty("siblings")
    public String siblings;

    @JsonProperty("childhood_story")
    public String childhoodStory;

    @JsonProperty("youth_friends")
    public String youthFriends;

    @JsonProperty("pet")
    public String pet;

    @JsonProperty("marital_status")
    public String maritalStatus;

    @JsonProperty("type_of_lover")
    public String typeOfLover;

    @JsonProperty("conjugal_history")
    public String conjugalHistory;

    @JsonProperty("children")
    public Integer children; // TODO : List<Character>

    @JsonProperty("education")
    public String education;

    @JsonProperty("profession")
    public String profession;

    @JsonProperty("reason_for_profession")
    public String reasonForProfession;

    @JsonProperty("work_preferences")
    public String workPreferences;

    @JsonProperty("change_in_world")
    public String changeInWorld;

    @JsonProperty("change_in_self")
    public String changeInSelf;

    @JsonProperty("goal")
    public String goal;

    @JsonProperty("reason_for_goal")
    public String reasonForGoal;

    @JsonProperty("biggest_obstacle")
    public String biggestObstacle;

    @JsonProperty("overcoming_obstacle")
    public String overcomingObstacle;

    @JsonProperty("plan_if_successful")
    public String planIfSuccessful;

    @JsonProperty("plan_if_failed")
    public String planIfFailed;

    @JsonProperty("self_description")
    public String selfDescription;

    @JsonProperty("distinctive_trait")
    public String distinctiveTrait;

    @JsonProperty("physical_description")
    public String physicalDescription;

    @JsonProperty("clothing_preferences")
    public String clothingPreferences;

    @JsonProperty("fears")
    public String fears;

    @JsonProperty("favorite_food")
    public String favoriteFood;

    @JsonProperty("hobbies")
    public String hobbies;

    @JsonProperty("leisure_activities")
    public String leisureActivities;

    @JsonProperty("ideal_company")
    public String idealCompany;

    @JsonProperty("attitude_towards_groups")
    public String attitudeTowardsGroups;

    @JsonProperty("attitude_towards_world")
    public String attitudeTowardsWorld;

    @JsonProperty("attitude_towards_people")
    public String attitudeTowardsPeople;

    @JsonProperty("image")
    public String image;

}
