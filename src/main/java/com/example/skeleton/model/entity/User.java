package com.example.skeleton.model.entity;

import com.example.skeleton.component.appproperty.Accessor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "USERS")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class User implements UserDetails, AbstractEntity<User> {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 256)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @CreationTimestamp
    private Date createTimestamp;

    @Column(nullable = false)
    @UpdateTimestamp
    private Date updateTimestamp;

    @Column(nullable = false)
    private Date passwordUpdateTimestamp;

    @Column(nullable = false)
    private Boolean locked;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE")
    @JsonManagedReference
    private Set<Role> grantedRoles;

    @Override
    @JsonIgnore //返回的时候 json数据即不包含该属性
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var authorities = new ArrayList<GrantedAuthority>();
        this.getGrantedRoles().forEach(
                role -> {
                    authorities.add(role);
                    authorities.addAll(role.getPermissions());
                }
        );
        return authorities;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.getName();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !this.getLocked();
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        var day = Accessor.AppProperty.getPasswordExpiration();
        // 7天之后过期  这里使用了一个取反，也就是当日期到了7天之后才会是true，然后取反为false，也就是过期
        return !DateUtils.isSameDay(new Date(), DateUtils.addDays(this.getPasswordUpdateTimestamp(), Math.toIntExact(day)));
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Specification<User> countExample() {
        return EntityUtil.Equal(this, "name");
    }

    @Override
    public Specification<User> uniqueExample() {
        if (this.getId() != null) {
            return EntityUtil.Equal(this, "id");
        }
        return EntityUtil.Equal(this, "name");
    }

    @Override
    public User Merge(User that) {
        if (that.getPassword() != null) {
            this.setPassword(that.getPassword());
            this.setPasswordUpdateTimestamp(new Date());
        }
        if (that.getGrantedRoles() != null) {
            this.setGrantedRoles(that.getGrantedRoles());
        }
        if (that.getLocked() != null) {
            this.setLocked(that.getLocked());
        }
        return this;
    }
}
