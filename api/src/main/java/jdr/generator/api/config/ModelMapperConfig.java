package jdr.generator.api.config;

import jdr.generator.api.characters.context.CharacterContextEntity;
import jdr.generator.api.characters.context.CharacterContextModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT) // or STANDARD, LOOSE
                .setSkipNullEnabled(true);
        modelMapper.typeMap(CharacterContextModel.class, CharacterContextEntity.class)
                .addMappings(mapper -> mapper.map(CharacterContextModel::getCreatedAt, CharacterContextEntity::setCreatedAt));
        return modelMapper;
    }
}