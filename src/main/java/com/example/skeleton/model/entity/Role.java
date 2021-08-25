package com.example.skeleton.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ROLES")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class Role implements GrantedAuthority, AbstractEntity<Role> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String name;

    @ManyToMany(targetEntity = Permission.class, fetch = FetchType.EAGER)
    @JoinTable(name = "ROLE_PERMISSION")
    @JsonManagedReference
    private Set<Permission> permissions;

    @ManyToMany(mappedBy = "grantedRoles")
    @JsonBackReference
    private Set<User> users;

    @Override
    @JsonIgnore
    public String getAuthority() {
        return String.format("ROLE_%s", this.getName());
    }

    @Override
    public Specification<Role> countExample() {
        return EntityUtil.Equal(this, "name");
    }

    @Override
    public Specification<Role> uniqueExample() {
        if (this.getId() != null) {
            return EntityUtil.Equal(this, "id");
        }
        return EntityUtil.Equal(this, "name");
    }

    @Override
    public Role Merge(Role that) {
        if (that.getPermissions() != null) {
            this.setPermissions(that.getPermissions());
        }
        return this;
    }
}
