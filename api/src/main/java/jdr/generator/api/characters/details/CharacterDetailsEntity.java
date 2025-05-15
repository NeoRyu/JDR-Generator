package jdr.generator.api.characters.details;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the detailed information about a character.
 * */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "character_details")
public class CharacterDetailsEntity {

  @Id
  @Column(name = "id", unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "age")
  private int age;

  @Column(name = "birth_place")
  private String birthPlace;

  @Column(name = "residence_location")
  private String residenceLocation;

  @Column(name = "reason_for_residence")
  private String reasonForResidence;

  @Column(name = "climate")
  private String climate;

  @Column(name = "common_problems")
  private String commonProblems;

  @Column(name = "daily_routine")
  private String dailyRoutine;

  @Column(name = "parents_alive")
  private Boolean parentsAlive;

  @Column(name = "details_about_parents")
  private String detailsAboutParents;

  @Column(name = "feelings_about_parents")
  private String feelingsAboutParents;

  @Column(name = "siblings")
  private String siblings;

  @Column(name = "childhood_story")
  private String childhoodStory;

  @Column(name = "youth_friends")
  private String youthFriends;

  @Column(name = "pet")
  private String pet;

  @Column(name = "marital_status")
  private String maritalStatus;

  @Column(name = "type_of_lover")
  private String typeOfLover;

  @Column(name = "conjugal_history")
  private String conjugalHistory;

  @Column(name = "children")
  private Integer children; // TODO : List<Character>

  @Column(name = "education")
  private String education;

  @Column(name = "profession")
  private String profession;

  @Column(name = "reason_for_profession")
  private String reasonForProfession;

  @Column(name = "work_preferences")
  private String workPreferences;

  @Column(name = "change_in_world")
  private String changeInWorld;

  @Column(name = "change_in_self")
  private String changeInSelf;

  @Column(name = "goal")
  private String goal;

  @Column(name = "reason_for_goal")
  private String reasonForGoal;

  @Column(name = "biggest_obstacle")
  private String biggestObstacle;

  @Column(name = "overcoming_obstacle")
  private String overcomingObstacle;

  @Column(name = "plan_if_successful")
  private String planIfSuccessful;

  @Column(name = "plan_if_failed")
  private String planIfFailed;

  @Column(name = "self_description")
  private String selfDescription;

  @Column(name = "distinctive_trait")
  private String distinctiveTrait;

  @Column(name = "physical_description")
  private String physicalDescription;

  @Column(name = "clothing_preferences")
  private String clothingPreferences;

  @Column(name = "fears")
  private String fears;

  @Column(name = "favorite_food")
  private String favoriteFood;

  @Column(name = "hobbies")
  private String hobbies;

  @Column(name = "leisure_activities")
  private String leisureActivities;

  @Column(name = "ideal_company")
  private String idealCompany;

  @Column(name = "attitude_towards_groups")
  private String attitudeTowardsGroups;

  @Column(name = "attitude_towards_world")
  private String attitudeTowardsWorld;

  @Column(name = "attitude_towards_people")
  private String attitudeTowardsPeople;

  @Column(name = "image")
  private String image;

  @Column(name = "created_at", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @Column(name = "updated_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  @Column(name = "context_id")
  private Long contextId;

  // public void setContextId(Integer contextId) {}
}
