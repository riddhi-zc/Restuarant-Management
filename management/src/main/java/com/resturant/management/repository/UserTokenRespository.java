package com.resturant.management.repository;

import com.resturant.management.entity.UserModel;
import com.resturant.management.entity.UserTokenModel;
import org.hibernate.cfg.JPAIndexHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRespository extends JpaRepository<UserTokenModel,Long> {
    @Query("SELECT  usertoken FROM UserTokenModel AS usertoken WHERE usertoken.user=:user")
    UserTokenModel findByUser(@Param("user") UserModel user);
}
