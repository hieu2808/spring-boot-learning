package com.example.employee_management.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Configuration đánh dấu class này là nguồn cấu hình Spring.
 * Các method được đánh dấu @Bean sẽ được Spring IoC Container
 * quản lý như những bean thông thường và có thể inject bất kỳ đâu.
 */
@Configuration
public class AppConfig {

    /**
     * Bean ModelMapper – dùng để map (chuyển đổi) giữa Entity và DTO.
     *
     * Được inject vào EmployeeService để minh họa Dependency Injection
     * với bean định nghĩa qua @Bean trong @Configuration.
     *
     * @return instance ModelMapper đã được cấu hình STRICT matching
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        // STRICT: chỉ map khi tên field khớp chính xác, tránh map nhầm
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }
}
