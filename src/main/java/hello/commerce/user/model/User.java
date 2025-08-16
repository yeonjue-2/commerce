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

    @Column(name = "user_id", nullable = false)
    private String userId;

    private String password;
    private String email;
    private String role;
    private String provider;

    @Column(name = "provider_id")
    private String providerId;
}
