
plugins {
	java
	id("org.springframework.boot") version "3.4.0-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.openapi.generator") version "7.7.0"
}

group = "com.bsp"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot")
		maven { url = uri("https://repo.spring.io/release") }}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	//implementation("org.springframework.boot:spring-boot-starter-validation")
	//compileOnly("io.swagger:swagger-annotations:2.2.22")
	// https://mvnrepository.com/artifact/io.swagger.core.v3/swagger-annotations
	//compileOnly("io.swagger.core.v3:swagger-annotations:2.2.22")

	// https://mvnrepository.com/artifact/io.swagger.core.v3/swagger-core
	//compileOnly("io.swagger.core.v3:swagger-core:2.2.22")
	// https://mvnrepository.com/artifact/io.swagger.core.v3/swagger-models
	//compileOnly("io.swagger.core.v3:swagger-models:2.2.22")

	compileOnly("io.swagger.core.v3:swagger-annotations:2.2.22")
	compileOnly("org.openapitools:openapi-generator-cli:6.6.0")
	compileOnly("javax.annotation:javax.annotation-api:1.3.2")
	implementation("javax.servlet:javax.servlet-api:4.0.1")
	//testImplementation("org.springframework.boot:spring-boot-starter-test")

	// Apache HttpClient 5
	implementation("org.apache.httpcomponents.client5:httpclient5:5.2.1")
	implementation("org.springframework.boot:spring-boot-starter-logging")
	compileOnly("org.projectlombok:lombok") // Check for the latest version
	annotationProcessor("org.projectlombok:lombok")// For annotation processing

	implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
	implementation("com.amazonaws:aws-java-sdk-s3:1.12.571")
	implementation("net.sourceforge.tess4j:tess4j:4.5.5")
}



tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateOpenApi") {
	generatorName.set("spring")
	library.set("spring-boot")
	inputSpec.set("$rootDir/src/main/resources/openapi-bsp.yaml")
	outputDir.set("$buildDir/generated")
	apiPackage.set("com.bsp.api") // Package for generated API classes
	modelPackage.set("com.bsp.model") // Package for generated model classes
	configOptions.set(
		mapOf(
			"dateLibrary" to "java8",
			"openApiNullable" to "false",
			"useSpringfox" to "false",
			"interfaceOnly" to "true",
			"useTags" to "true",
			"skipTests" to "true",
			"useBeanValidation" to "false"
		)
	)
	skipValidateSpec.set(true)
	validateSpec.set(false)
	generateApiTests.set(false)
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateOpenApiAccountAndTransaction") {
	generatorName.set("java")
	library.set("resttemplate")
	inputSpec.set("$rootDir/src/main/resources/Account and Transaction API.yaml")
	outputDir.set("$buildDir/generated")
	apiPackage.set("com.credit.api") // Package for generated API classes
	modelPackage.set("com.credit.model") // Package for generated model classes
	invokerPackage.set("com.credit.invoker") // Package for generated model classes
	configOptions.set(
		mapOf(
			"dateLibrary" to "java8",
			"openApiNullable" to "false",
			"useSpringfox" to "false",
			"reactive" to "true",
			"skipTests" to "true",
			"useBeanValidation" to "false"
		)
	)
	skipValidateSpec.set(true)
	validateSpec.set(false)
	generateApiTests.set(false)
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateCreditScore") {
	generatorName.set("java")
	library.set("resttemplate")
	inputSpec.set("$rootDir/src/main/resources/aws-api.yaml")
	outputDir.set("$buildDir/generated")
	apiPackage.set("com.aws.api") // Package for generated API classes
	modelPackage.set("com.aws.model") // Package for generated model classes
	invokerPackage.set("com.aws.invoker") // Package for generated model classes
	configOptions.set(
		mapOf(
			"dateLibrary" to "java8",
			"openApiNullable" to "false",
			"useSpringfox" to "false",
			"reactive" to "true",
			"skipTests" to "true",
			"useBeanValidation" to "false"

		)
	)
	skipValidateSpec.set(true)
	validateSpec.set(false)
	generateApiTests.set(false)
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateDetectText") {
	generatorName.set("java")
	library.set("resttemplate")
	inputSpec.set("$rootDir/src/main/resources/detect-text-api.yaml")
	outputDir.set("$buildDir/generated")
	apiPackage.set("com.detecttext.api") // Package for generated API classes
	modelPackage.set("com.detecttext.model") // Package for generated model classes
	invokerPackage.set("com.detecttext.invoker") // Package for generated model classes
	configOptions.set(
		mapOf(
			"dateLibrary" to "java8",
			"openApiNullable" to "false",
			"useSpringfox" to "false",
			"reactive" to "true",
			"skipTests" to "true",
			"useBeanValidation" to "false"

		)
	)
	skipValidateSpec.set(true)
	validateSpec.set(false)
	generateApiTests.set(false)
}

sourceSets {
	main {
		java.srcDir("$buildDir/generated/src/main/java")
	}
}

tasks.named("compileJava") {
	dependsOn("generateOpenApi")
	dependsOn("generateOpenApiAccountAndTransaction")
	dependsOn("generateCreditScore")
	dependsOn("generateDetectText")

}


