package com.example.sys_newwelcome.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 通过@Configuration注解，让Spring来加载该类配置
 * 通过@EnableSwagger2注解来启用Swagger2
 */
@EnableSwagger2
@Configuration
public class Swagger2Config extends WebMvcConfigurationSupport {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                /**
                 * 用来创建该api的基本信息
                 */
                .apiInfo(createApiInfo())
                /**
                 * select()函数返回一个ApiSelectorBuilder实例用来控制哪些接口暴露给Swagger来展现
                 */
                .select()
                /**
                 * 扫描当前包路径,所有Controller所在的包路径
                 */
                .apis(RequestHandlerSelectors.basePackage("com.example.sys_newwelcome.controller"))
                .paths(PathSelectors.any())
                /**
                 * 定义要生成文档的Api的url路径规则
                 */
                .build();
    }

    /**
     * 构建 api文档的详细信息函数
     */
    private ApiInfo createApiInfo() {
        return new ApiInfoBuilder()
                /**
                 * 页面标题
                 */
                .title("数字迎新系统接口API文档")
                .contact(new Contact("第八组", "公司主页", "邮箱"))
                .version("1.0")
                .description("API接口文档")
                .build();
    }

    /**
     * 解决静态资源无法访问的问题
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}