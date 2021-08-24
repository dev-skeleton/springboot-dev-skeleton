package com.example.skeleton.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PERMISSIONS")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Permission implements GrantedAuthority, AbstractEntity<Permission> {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String phrase;

    @ManyToMany(mappedBy = "permissions")
    @JsonBackReference
    private Set<Role> roles;

    private String description;

    @Override
    public String getAuthority() {
        return this.getPhrase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return getPhrase().equals(that.getPhrase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPhrase());
    }

    @Override
    public Specification<Permission> countExample() {
        return EntityUtil.Equal(this, "phrase");
    }

    @Override
    public Specification<Permission> uniqueExample() {
        if (this.getId() != null) {
            return EntityUtil.Equal(this, "id");
        }
        return EntityUtil.Equal(this, "phrase");
    }

    @Override
    public Permission Merge(Permission that) {
        if (that.getPhrase() != null) {
            this.setPhrase(that.getPhrase());
        }
        if (that.getDescription() != null) {
            this.setDescription(that.getDescription());
        }
        return this;
    }
}
