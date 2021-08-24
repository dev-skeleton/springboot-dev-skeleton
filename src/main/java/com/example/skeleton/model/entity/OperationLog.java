package com.example.skeleton.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员操作记录")
@Table(name = "OPERATION_LOG")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class OperationLog implements AbstractEntity<OperationLog> {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    private String account;

    private String IP;

    private Date time;

    private String action;

    private String entity;

    @JsonIgnoreProperties
    private String Detail;

    private String status;

    @Override
    public Specification<OperationLog> countExample() {
        return null;
    }

    @Override
    public Specification<OperationLog> uniqueExample() {
        return EntityUtil.Equal(this, "id");
    }

    @Override
    public OperationLog Merge(OperationLog that) {
        return this;
    }


}
