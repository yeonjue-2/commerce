package hello.commerce.user.model;

import hello.commerce.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;
}
