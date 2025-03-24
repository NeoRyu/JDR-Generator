package jdr.generator.api.characters.details;

import jdr.generator.api.characters.context.CharacterContextEntity;

import java.util.Date;
import java.util.Objects;

public class CharacterDetailsMapper {

    public static CharacterDetailsEntity convertModelToEntity(CharacterDetailsModel model, CharacterContextEntity characterContextEntity) {
        final Date timestampDate = new Date();
        CharacterDetailsEntity entity = new CharacterDetailsEntity();
        entity.setName(model.name);
        entity.setAge(model.age);
        entity.setBirthPlace(model.birthPlace);
        entity.setResidenceLocation(model.residenceLocation);
        entity.setReasonForResidence(model.reasonForResidence);
        entity.setClimate(model.climate);
        entity.setCommonProblems(model.commonProblems);
        entity.setDailyRoutine(model.dailyRoutine);
        entity.setParentsAlive(model.parentsAlive);
        entity.setDetailsAboutParents(model.detailsAboutParents);
        entity.setFeelingsAboutParents(model.feelingsAboutParents);
        entity.setSiblings(model.siblings);
        entity.setChildhoodStory(model.childhoodStory);
        entity.setYouthFriends(model.youthFriends);
        entity.setPet(model.pet);
        entity.setMaritalStatus(model.maritalStatus);
        entity.setTypeOfLover(model.typeOfLover);
        entity.setConjugalHistory(model.conjugalHistory);
        entity.setChildren(model.children);
        entity.setEducation(model.education);
        entity.setProfession(model.profession);
        entity.setReasonForProfession(model.reasonForProfession);
        entity.setWorkPreferences(model.workPreferences);
        entity.setChangeInWorld(model.changeInWorld);
        entity.setChangeInSelf(model.changeInSelf);
        entity.setGoal(model.goal);
        entity.setReasonForGoal(model.reasonForGoal);
        entity.setBiggestObstacle(model.biggestObstacle);
        entity.setOvercomingObstacle(model.overcomingObstacle);
        entity.setPlanIfSuccessful(model.planIfSuccessful);
        entity.setPlanIfFailed(model.planIfFailed);
        entity.setSelfDescription(model.selfDescription);
        entity.setDistinctiveTrait(model.distinctiveTrait);
        entity.setPhysicalDescription(model.physicalDescription);
        entity.setClothingPreferences(model.clothingPreferences);
        entity.setFears(model.fears);
        entity.setFavoriteFood(model.favoriteFood);
        entity.setHobbies(model.hobbies);
        entity.setLeisureActivities(model.leisureActivities);
        entity.setIdealCompany(model.idealCompany);
        entity.setAttitudeTowardsGroups(model.attitudeTowardsGroups);
        entity.setAttitudeTowardsWorld(model.attitudeTowardsWorld);
        entity.setAttitudeTowardsPeople(model.attitudeTowardsPeople);
        entity.setImage(model.image);
        if (!Objects.equals(model.createdAt, timestampDate)) {
            entity.setCreatedAt(timestampDate);
        }
        entity.setUpdatedAt(timestampDate);
        entity.setContextId(characterContextEntity.getId());
        return entity;
    }
}