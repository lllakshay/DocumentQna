package com.documentqna.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(scanBasePackages="com.documentqna")
@EnableJpaRepositories(basePackages="com.documentqna.repository")
@EntityScan(basePackages="com.documentqna.model")
public class GmsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmsBackendApplication.class, args);
	}
	
//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/citizen/getAllComplaints").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/citizen/makeComplaint").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/citizen/getAllComplaints").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/citizen/registerUser").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/citizen/changePassword").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/citizen/enableComplaintReminder").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/citizen/reopenComplaint").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/citizen/getStatusById").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/citizen/changeStatusReopen").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/citizen/likeById").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/citizen/getAllComments").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/citizen/addComments").allowedOrigins("http://localhost:4200");
//				
//				registry.addMapping("/admin/getAllComplaints").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/addHod").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/removeHod").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/getHod").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/getHodByDepartmentName").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/editHod").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/getAllHod").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/unlockAccount").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/getAllLockUser").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/getAvailableHod").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/addDepartment").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/getAllDepartment").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/removeDepartment").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/editDepartment").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/getDepartment").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/getLikesByComplaint").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/getCommentByComplaint").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/getRatingByComplaint").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/getDepartmentByDepartmentName").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/getCommentsByComplaintId").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/admin/getLikesByComplaintId").allowedOrigins("http://localhost:4200");
//				
//				registry.addMapping("/hod/getAllComplaints").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/hod/transferComplaint").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/hod/changePassword").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/hod/getReminderComplaints").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/hod/completeComplaint").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/hod/getAllDepartment").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/hod/getComplaintById").allowedOrigins("http://localhost:4200");
//				
//				registry.addMapping("/user/authenticateUser").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/user/getotp").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/user/getUserByUserName").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/user/getUserByUserId").allowedOrigins("http://localhost:4200");
//			}
//		};
//	}

}
