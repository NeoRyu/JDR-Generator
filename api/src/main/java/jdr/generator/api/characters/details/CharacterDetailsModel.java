package jdr.generator.api.characters.details;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the data transfer object for detailed character information.
 * */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CharacterDetailsModel {

  @JsonProperty("name")
  public String name;

  @JsonProperty("age")
  public int age;

  @JsonProperty("birthPlace")
  public String birthPlace;

  @JsonProperty("residenceLocation")
  public String residenceLocation;

  @JsonProperty("reasonForResidence")
  public String reasonForResidence;

  @JsonProperty("climate")
  public String climate;

  @JsonProperty("commonProblems")
  public String commonProblems;

  @JsonProperty("dailyRoutine")
  public String dailyRoutine;

  @JsonProperty("parentsAlive")
  @JsonDeserialize(using = CustomBooleanDeserializer.class)
  public Boolean parentsAlive;

  @JsonProperty("detailsAboutParents")
  public String detailsAboutParents;

  @JsonProperty("feelingsAboutParents")
  public String feelingsAboutParents;

  @JsonProperty("siblings")
  public String siblings;

  @JsonProperty("childhoodStory")
  public String childhoodStory;

  @JsonProperty("youthFriends")
  public String youthFriends;

  @JsonProperty("pet")
  public String pet;

  @JsonProperty("maritalStatus")
  public String maritalStatus;

  @JsonProperty("typeOfLover")
  public String typeOfLover;

  @JsonProperty("conjugalHistory")
  public String conjugalHistory;

  @JsonProperty("children")
  @JsonDeserialize(using = CustomIntegerDeserializer.class)
  public Integer children; // TODO : List<Character> ?

  @JsonProperty("education")
  public String education;

  @JsonProperty("profession")
  public String profession;

  @JsonProperty("reasonForProfession")
  public String reasonForProfession;

  @JsonProperty("workPreferences")
  public String workPreferences;

  @JsonProperty("changeInWorld")
  public String changeInWorld;

  @JsonProperty("changeInSelf")
  public String changeInSelf;

  @JsonProperty("goal")
  public String goal;

  @JsonProperty("reasonForGoal")
  public String reasonForGoal;

  @JsonProperty("biggestObstacle")
  public String biggestObstacle;

  @JsonProperty("overcomingObstacle")
  public String overcomingObstacle;

  @JsonProperty("planIfSuccessful")
  public String planIfSuccessful;

  @JsonProperty("planIfFailed")
  public String planIfFailed;

  @JsonProperty("selfDescription")
  public String selfDescription;

  @JsonProperty("distinctiveTrait")
  public String distinctiveTrait;

  @JsonProperty("physicalDescription")
  public String physicalDescription;

  @JsonProperty("clothingPreferences")
  public String clothingPreferences;

  @JsonProperty("fears")
  public String fears;

  @JsonProperty("favoriteFood")
  public String favoriteFood;

  @JsonProperty("hobbies")
  public String hobbies;

  @JsonProperty("leisureActivities")
  public String leisureActivities;

  @JsonProperty("idealCompany")
  public String idealCompany;

  @JsonProperty("attitudeTowardsGroups")
  public String attitudeTowardsGroups;

  @JsonProperty("attitudeTowardsWorld")
  public String attitudeTowardsWorld;

  @JsonProperty("attitudeTowardsPeople")
  public String attitudeTowardsPeople;

  @JsonProperty("image")
  public String image;

  public Long id;
  public Long contextId;
  public Date createdAt;
  public Date updatedAt;
}
