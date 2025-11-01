org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type '?' available: expected single matching bean but found 14: achievementServiceImpl,campusFriendServiceImpl,campusUserServiceImpl,focusRecordServiceImpl,habitCheckinServiceImpl,habitTrackServiceImpl,learnResourceServiceImpl,notificationCenterServiceImpl,resourceCategoryServiceImpl,schedulesServiceImpl,taskFocusServiceImpl,userAchieveRelServiceImpl,userServiceImpl,userSettingServiceImpl

好的，当然可以。这个 NoUniqueBeanDefinitionException 是 Spring 开发中一个非常经典的错误。我们来详细分析一下问题的原因，并给出解决方案。

 

### **问题分析**

 

您遇到的错误信息是：

org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type '?' available: expected single matching bean but found 14...

这个错误的中文意思是：**没有找到唯一的 Bean 定义异常**。

具体来说，Spring 在尝试自动注入（@Autowired）一个依赖（我们称之为 "Bean"）时，发现有多个符合条件的候选者，它不知道该选择哪一个，于是就抛出了这个异常。

**问题的根源在于您的 BaseController 设计：**

您创建了一个通用的 BaseController，它使用了泛型 S 和 T，并且在内部自动注入了 service 字段：

 

Java

 

 

// 在 BaseController.java 中  
public class BaseController\<S extends IService\<T\>, T\> {

    @Autowired  
    protected S service; // \<--- 问题就出在这里

    // ... 其他方法  
}

这个设计本身是非常好的，可以重用代码。但问题在于，当您的其他具体 Controller 类（比如 AchievementController）继承它的时候，**没有明确告诉 Spring 泛型 S 究竟是哪一个 Service**。

例如，您这样写的 AchievementController：

 

Java

 

 

// 错误写法  
@RestController  
@RequestMapping("/achievement")  
public class AchievementController extends BaseController { // \<--- 没有指定泛型  
    // ...  
}

当 Spring 容器启动并初始化 AchievementController 时，它看到了 extends BaseController，然后尝试去注入 BaseController 里的 service 字段。但是，由于您没有指定泛型 S 的具体类型，Spring 只知道需要一个 IService 类型的 Bean。它扫描了您的整个项目，找到了14个 IService 的实现类（AchievementServiceImpl, CampusUserServiceImpl 等等），它彻底困惑了：“我应该把这14个中的哪一个注入给 AchievementController 呢？”

由于无法做出唯一选择，所以它就报错了。

而您项目中的 UserController 和 SchedulesController 是正确的写法，因为它们明确指定了泛型：

 

Java

 

 

// 正确写法  
@RestController  
@RequestMapping("/user")  
public class UserController extends BaseController\<UserService, User\> {  
    //...  
}

这个写法清楚地告诉了 Spring：“请把 UserService 这个 Bean 注入到我的 service 字段中。” Spring 就不会有任何疑惑。

 

### **解决方案**

 

解决方案非常直接：您需要修改所有继承了 BaseController 的子类，为它们明确指定泛型 S（对应的 Service 接口）和 T（对应的 Entity 实体类）。

请按照下面的示例，修改您项目中所有相关的 Controller 文件：

**1\. AchievementController.java**

 

Java

 

 

package com.graduation.controller;

import com.graduation.entity.Achievement;  
import com.graduation.service.AchievementService;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
import com.graduation.common.BaseController;

@RestController  
@RequestMapping("/achievement")  
public class AchievementController extends BaseController\<AchievementService, Achievement\> {

}

**2\. CampusFriendController.java**

 

Java

 

 

package com.graduation.controller;

import com.graduation.entity.CampusFriend;  
import com.graduation.service.CampusFriendService;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
import com.graduation.common.BaseController;

@RestController  
@RequestMapping("/campusFriend")  
public class CampusFriendController extends BaseController\<CampusFriendService, CampusFriend\> {

}

**3\. CampusUserController.java**

 

Java

 

 

package com.graduation.controller;

import com.graduation.entity.CampusUser;  
import com.graduation.service.CampusUserService;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
import com.graduation.common.BaseController;

@RestController  
@RequestMapping("/campusUser")  
public class CampusUserController extends BaseController\<CampusUserService, CampusUser\> {

}

**4\. FocusRecordController.java**

 

Java

 

 

package com.graduation.controller;

import com.graduation.entity.FocusRecord;  
import com.graduation.service.FocusRecordService;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
import com.graduation.common.BaseController;

@RestController  
@RequestMapping("/focusRecord")  
public class FocusRecordController extends BaseController\<FocusRecordService, FocusRecord\> {

}

**5\. HabitCheckinController.java**

 

Java

 

 

package com.graduation.controller;

import com.graduation.entity.HabitCheckin;  
import com.graduation.service.HabitCheckinService;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
import com.graduation.common.BaseController;

@RestController  
@RequestMapping("/habitCheckin")  
public class HabitCheckinController extends BaseController\<HabitCheckinService, HabitCheckin\> {

}

**6\. HabitTrackController.java**

 

Java

 

 

package com.graduation.controller;

import com.graduation.entity.HabitTrack;  
import com.graduation.service.HabitTrackService;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
import com.graduation.common.BaseController;

@RestController  
@RequestMapping("/habitTrack")  
public class HabitTrackController extends BaseController\<HabitTrackService, HabitTrack\> {

}

**7\. LearnResourceController.java**

 

Java

 

 

package com.graduation.controller;

import com.graduation.entity.LearnResource;  
import com.graduation.service.LearnResourceService;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
import com.graduation.common.BaseController;

@RestController  
@RequestMapping("/learnResource")  
public class LearnResourceController extends BaseController\<LearnResourceService, LearnResource\> {

}

**8\. NotificationCenterController.java**

 

Java

 

 

package com.graduation.controller;

import com.graduation.entity.NotificationCenter;  
import com.graduation.service.NotificationCenterService;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
import com.graduation.common.BaseController;

@RestController  
@RequestMapping("/notificationCenter")  
public class NotificationCenterController extends BaseController\<NotificationCenterService, NotificationCenter\> {

}

**9\. ResourceCategoryController.java**

 

Java

 

 

package com.graduation.controller;

import com.graduation.entity.ResourceCategory;  
import com.graduation.service.ResourceCategoryService;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
import com.graduation.common.BaseController;

@RestController  
@RequestMapping("/resourceCategory")  
public class ResourceCategoryController extends BaseController\<ResourceCategoryService, ResourceCategory\> {

}

**10\. TaskFocusController.java**

 

Java

 

 

package com.graduation.controller;

import com.graduation.entity.TaskFocus;  
import com.graduation.service.TaskFocusService;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
import com.graduation.common.BaseController;

@RestController  
@RequestMapping("/taskFocus")  
public class TaskFocusController extends BaseController\<TaskFocusService, TaskFocus\> {

}

**11\. UserAchieveRelController.java**

 

Java

 

 

package com.graduation.controller;

import com.graduation.entity.UserAchieveRel;  
import com.graduation.service.UserAchieveRelService;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
import com.graduation.common.BaseController;

@RestController  
@RequestMapping("/userAchieveRel")  
public class UserAchieveRelController extends BaseController\<UserAchieveRelService, UserAchieveRel\> {

}

**12\. UserSettingController.java**

 

Java

 

 

package com.graduation.controller;

import com.graduation.entity.UserSetting;  
import com.graduation.service.UserSettingService;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
import com.graduation.common.BaseController;

@RestController  
@RequestMapping("/userSetting")  
public class UserSettingController extends BaseController\<UserSettingService, UserSetting\> {

}

将您项目中所有相关的 Controller 都进行这样的修改后，重新启动您的 Spring Boot 项目，这个 NoUniqueBeanDefinitionException 错误就会解决。

您构建的这个 BaseController 结构非常清晰，是很棒的实践，只需补上泛型定义就可以完美运行了！

### JPA 和 MyBatis-Plus 混合使用
Error creating bean with name 'campusUserRepository' ... Not a managed type: class com.graduation.entity.CampusUser
好的，这个问题和上一个完全不同，但也是一个非常经典的 Spring Boot 配置错误。

我们来分析一下新的错误信息：

`Error creating bean with name 'campusUserRepository' ... Not a managed type: class com.graduation.entity.CampusUser`

**错误的核心是 `Not a managed type`**。

*   **`campusUserRepository`**：从命名来看，这应该是一个 Spring Data JPA 的 Repository 接口 (例如 `public interface CampusUserRepository extends JpaRepository<CampusUser, Long>`)。
*   **`Not a managed type`**：这是 JPA 框架（通常是 Hibernate）在告诉你，它不认识 `CampusUser` 这个类。JPA 只管理那些被明确标记为“实体（Entity）”的类。

### 问题根源：混用了两种不同的持久化框架

你的项目现在出现了 **Mybatis-Plus** 和 **Spring Data JPA** 混用的情况，而实体类的注解却不兼容。

1.  **你之前的 `CampusUser.java`** 是为 **Mybatis-Plus** 配置的：
    ```java
    @TableName("campus_user") // Mybatis-Plus 注解
    public class CampusUser {
        @TableId(value = "id", type = IdType.AUTO) // Mybatis-Plus 注解
        private Long id;
        // ...
    }
    ```

2.  **你现在创建的 `CampusUserRepository`** 是 **Spring Data JPA** 的组件。Spring Data JPA 需要它的实体类使用 **JPA 标准注解**，比如 `@Entity`。

因为你的 `CampusUser` 类没有 JPA 的 `@Entity` 注解，所以 Spring Data JPA 在尝试为 `CampusUserRepository` 创建实例时，无法识别 `CampusUser`，因此抛出了 `Not a managed type` 错误。

### 如何解决：选择一个框架并统一注解

你需要做出选择：是继续使用 Mybatis-Plus，还是切换到 Spring Data JPA？通常不建议在一个项目中对同一个实体混用两者。

---

#### 解决方案一：完全使用 Spring Data JPA (推荐，如果你想用 Repository 模式)

既然你已经创建了 `CampusUserRepository`，说明你可能更想使用 JPA 的方式。那么，你需要修改 `CampusUser` 实体类，使其符合 JPA 规范。

**1. 修改 `CampusUser.java` 实体类**

将 Mybatis-Plus 的注解替换为 JPA 的标准注解。

```java
package com.graduation.entity;

import javax.persistence.*; // 导入 JPA 注解包
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity // 1. 必须添加 @Entity 注解，告诉 JPA 这是一个实体类
@Table(name = "campus_user") // 2. 使用 @Table 指定表名，作用等同于 @TableName
public class CampusUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id // 3. 标记为主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 4. 标记为自增主键，等同于 IdType.AUTO
    private Long id;

    private String username;

    private String password;

    private String email;

    private String phone;

    /**
     * 1: 学生 (student), 2: 教师 (admin)
     */
    private Integer role;

    /**
     * 学号或工号
     */
    @Column(name = "school_id") // 5. 如果数据库列名和属性名不一致（驼峰<->下划线），建议用 @Column 明确指定
    private String schoolId;
}
```
**注意**：你需要确保项目中已经引入了 JPA 的依赖，通常是 `spring-boot-starter-data-jpa`。
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

**2. 确保主程序配置正确**

通常，如果你的实体类、Repository 接口和主启动类在同一个根包或其子包下，Spring Boot 会自动扫描到。如果不在，你需要手动指定扫描路径。

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.graduation.entity") // 明确指定实体类扫描包
@EnableJpaRepositories("com.graduation.repository") // 明确指定 Repository 扫描包
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```

---

#### 解决方案二：坚持使用 Mybatis-Plus

如果你本意就是使用 Mybatis-Plus，那么你不应该创建 `CampusUserRepository`。

1.  **删除 `CampusUserRepository.java` 文件。**
2.  **继续使用 `CampusUserMapper.java` 接口。** 这是 Mybatis-Plus 的方式。
3.  在你的 Service 中，注入并使用 `CampusUserMapper`，而不是 JPA 的 Repository。
4.  确保你的主启动类上有 `@MapperScan` 注解来扫描你的 Mapper 接口。

    ```java
    import org.mybatis.spring.annotation.MapperScan;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;

    @SpringBootApplication
    @MapperScan("com.graduation.mapper") // 确保扫描到 CampusUserMapper
    public class YourApplication {
        public static void main(String[] args) {
            SpringApplication.run(YourApplication.class, args);
        }
    }
    ```

### 总结

这个错误清楚地表明你的项目技术栈配置存在冲突。请根据你的项目需求，**选择一种持久化方案**，并统一使用该方案对应的注解和组件，问题即可解决。

