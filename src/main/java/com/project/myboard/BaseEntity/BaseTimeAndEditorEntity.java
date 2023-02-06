package com.project.myboard.BaseEntity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeAndEditorEntity extends BaseTimeEntity {

    @CreatedBy
    @Column(updatable = false)
    protected String createdBy;

    @LastModifiedBy
    protected String lastModifiedBy;
}
