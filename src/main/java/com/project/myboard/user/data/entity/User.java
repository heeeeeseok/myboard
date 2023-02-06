package com.project.myboard.user.data.entity;

import com.project.myboard.BaseEntity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uid;

    private String password;

    private String name;

    private String role;

}
