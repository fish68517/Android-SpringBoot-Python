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

