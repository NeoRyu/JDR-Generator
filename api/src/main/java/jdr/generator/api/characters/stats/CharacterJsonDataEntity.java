package jdr.generator.api.characters.stats;

import jakarta.persistence.*;
import jdr.generator.api.characters.details.CharacterDetailsEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="character_json_data")
public class CharacterJsonDataEntity {

    @Id
    @Column(name="id", unique=true, nullable=false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="character_details_id", nullable=false)
    private Long characterDetailsId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="json_data", nullable=false)
    private String jsonData;

    @Column(name="created_at", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name="updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "character_details_id", insertable = false, updatable = false)
    private CharacterDetailsEntity characterDetails;

}