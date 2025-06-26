package jdr.generator.api.config;

import jdr.generator.api.characters.context.CharacterContextEntity;
import jdr.generator.api.characters.context.CharacterContextModel;
import jdr.generator.api.characters.stats.CharacterJsonDataEntity;
import jdr.generator.api.characters.stats.CharacterJsonDataModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for ModelMapper.
 *
 * <p>This class configures the ModelMapper bean, defining mapping strategies and specific mappings
 * for certain entities.
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Configures and provides a ModelMapper instance.
     *
     * <p>The ModelMapper is configured with a strict matching strategy and skips null values during
     * mapping. It also defines specific mappings for {@link CharacterContextModel} to {@link
     * CharacterContextEntity} and {@link CharacterJsonDataModel} to {@link
     * CharacterJsonDataEntity}.
     *
     * @return A configured ModelMapper instance.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
                .getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT) // or STANDARD, LOOSE
                .setSkipNullEnabled(true);

        // Mappage pour CharacterContext
        modelMapper
                .typeMap(CharacterContextModel.class, CharacterContextEntity.class)
                .addMappings(
                        mapper -> {
                            mapper.map(
                                    CharacterContextModel::getCreatedAt,
                                    CharacterContextEntity::setCreatedAt);
                        });

        // Mappage pour CharacterJsonData
        modelMapper
                .typeMap(CharacterJsonDataModel.class, CharacterJsonDataEntity.class)
                .addMappings(
                        mapper -> {
                            mapper.map(
                                    CharacterJsonDataModel::getCreatedAt,
                                    CharacterJsonDataEntity::setCreatedAt);
                            mapper.map(
                                    CharacterJsonDataModel::getUpdatedAt,
                                    CharacterJsonDataEntity::setUpdatedAt);
                        });

        return modelMapper;
    }
}
