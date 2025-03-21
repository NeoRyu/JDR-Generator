package jdr.generator.api.controllers;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.Date;

//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter @Setter
//@Table(name="character")
public class CharacterEntity {

    @Column(name="id", unique=true, nullable=false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="age", nullable=true)
    private int age;

    @Column(name="birthPlace", nullable=true)
    private String birthPlace;

    @Column(name="childhoodStory", nullable=true)
    private String childhoodStory;

    @Column(name="feelingsAboutParents", nullable=true)
    private String feelingsAboutParents;

    @Column(name="parentsAlive", nullable=true)
    private Boolean parentsAlive;

    @Column(name="detailsAboutParents", nullable=true)
    private String detailsAboutParents;

    @Column(name="siblings", nullable=true)
    private String siblings;

    @Column(name="youthFriends", nullable=true)
    private String youthFriends;

    @Column(name="maritalStatus", nullable=true)
    private String maritalStatus;

    @Column(name="conjugalHistory", nullable=true)
    private String conjugalHistory;

    @Column(name="children", nullable=true)
    private Integer children; // TODO : List<Character>

    @Column(name="education", nullable=true)
    private String education;

    @Column(name="profession", nullable=true)
    private String profession;

    @Column(name="reasonForProfession", nullable=true)
    private String reasonForProfession;

    @Column(name="physicalDescription", nullable=true)
    private String physicalDescription;

    @Column(name="distinctiveTrait", nullable=true)
    private String distinctiveTrait;

    @Column(name="goal", nullable=true)
    private String goal;

    @Column(name="reasonForGoal", nullable=true)
    private String reasonForGoal;

    @Column(name="planIfSuccessful", nullable=true)
    private String planIfSuccessful;

    @Column(name="planIfFailed", nullable=true)
    private String planIfFailed;

    @Column(name="biggestObstacle", nullable=true)
    private String biggestObstacle;

    @Column(name="overcomingObstacle", nullable=true)
    private String overcomingObstacle;

    @Column(name="changeInWorld", nullable=true)
    private String changeInWorld;

    @Column(name="changeInSelf", nullable=true)
    private String changeInSelf;

    @Column(name="fears", nullable=true)
    private String fears;

    @Column(name="selfDescription", nullable=true)
    private String selfDescription;

    @Column(name="attitudeTowardsWorld", nullable=true)
    private String attitudeTowardsWorld;

    @Column(name="attitudeTowardsPeople", nullable=true)
    private String attitudeTowardsPeople;

    @Column(name="attitudeTowardsGroups", nullable=true)
    private String attitudeTowardsGroups;

    @Column(name="leisureActivities", nullable=true)
    private String leisureActivities;

    @Column(name="clothingPreferences", nullable=true)
    private String clothingPreferences;

    @Column(name="workPreferences", nullable=true)
    private String workPreferences;

    @Column(name="favoriteFood", nullable=true)
    private String favoriteFood;

    @Column(name="hobbies", nullable=true)
    private String hobbies;

    @Column(name="pet", nullable=true)
    private String pet;

    @Column(name="idealCompany", nullable=true)
    private String idealCompany;

    @Column(name="typeOfLover", nullable=true)
    private String typeOfLover;

    @Column(name="residenceLocation", nullable=true)
    private String residenceLocation;

    @Column(name="climate", nullable=true)
    private String climate;

    @Column(name="reasonForResidence", nullable=true)
    private String reasonForResidence;

    @Column(name="commonProblems", nullable=true)
    private String commonProblems;

    @Column(name="dailyRoutine", nullable=true)
    private String dailyRoutine;

    @Column(name="image", nullable=true)
    private String image;

    @Column(name="createdAt", nullable=false)
    private Date createdAt;

    @Column(name="updatedAt", nullable=true)
    private Date updatedAt;

}
