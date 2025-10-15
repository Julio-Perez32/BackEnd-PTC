package ptc2025.backend.Config.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import ptc2025.backend.Utils.JwtCookieAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtCookieAuthFilter jwtCookieAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(JwtCookieAuthFilter jwtCookieAuthFilter,
                          CorsConfigurationSource corsConfigurationSource) {
        this.jwtCookieAuthFilter = jwtCookieAuthFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 🔓 Preflight siempre permitido
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 🔓 Endpoints públicos de auth
                        .requestMatchers("/api/Auth/login", "/api/Auth/logout").permitAll()

                        // Employees
                        .requestMatchers(HttpMethod.GET, "/api/Employees/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/Employees/**").hasAnyRole("Administrador", "Recursos Humanos")
                        .requestMatchers(HttpMethod.PUT, "/api/Employees/**").hasAnyRole("Administrador", "Recursos Humanos")
                        .requestMatchers(HttpMethod.DELETE, "/api/Employees/**").hasAnyRole("Administrador", "Recursos Humanos")

                        // Academic Levels
                        .requestMatchers(HttpMethod.GET, "/api/AcademicLevels/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/AcademicLevels/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/AcademicLevels/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/AcademicLevels/**").hasAnyRole("Administrador", "Registro académico")


                        // === Academic Year ===
                        .requestMatchers(HttpMethod.GET, "/api/AcademicYear/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/AcademicYear/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/AcademicYear/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/AcademicYear/**").hasAnyRole("Administrador", "Registro académico")


                        // === Career Cycle Availability ===
                        .requestMatchers(HttpMethod.GET, "/api/CareerCycleAvailability/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/CareerCycleAvailability/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/CareerCycleAvailability/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/CareerCycleAvailability/**").hasAnyRole("Administrador", "Registro académico")

                        // === Careers ===
                        .requestMatchers(HttpMethod.GET, "/api/Careers/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/Careers/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/Careers/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/Careers/**").hasAnyRole("Administrador", "Registro académico")

                        // === Career Social Service Projects ===
                        .requestMatchers(HttpMethod.GET, "/api/CareerSocialServiceProjects/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/CareerSocialServiceProjects/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/CareerSocialServiceProjects/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/CareerSocialServiceProjects/**").hasAnyRole("Administrador", "Registro académico")

                        // === Course Enrollments ===
                        .requestMatchers(HttpMethod.GET, "/api/CourseEnrollments/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/CourseEnrollments/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/CourseEnrollments/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/CourseEnrollments/**").hasAnyRole("Administrador", "Registro académico")

                        // === Course Offerings ===
                        .requestMatchers(HttpMethod.GET, "/api/CourseOfferings/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/CourseOfferings/**").hasAnyRole("Administrador", "Registro académico", "Docente")
                        .requestMatchers(HttpMethod.PUT, "/api/CourseOfferings/**").hasAnyRole("Administrador", "Registro académico", "Docente")
                        .requestMatchers(HttpMethod.DELETE, "/api/CourseOfferings/**").hasAnyRole("Administrador", "Registro académico")


                        // === Course Offering Schedules ===
                        .requestMatchers(HttpMethod.GET, "/api/courseOfferingSchedules/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/courseOfferingSchedules/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/courseOfferingSchedules/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/courseOfferingSchedules/**").hasAnyRole("Administrador", "Registro académico")

                        // === Course Offerings Teachers ===
                        .requestMatchers(HttpMethod.GET, "/api/CourseOfferingsTeachers/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/CourseOfferingsTeachers/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/CourseOfferingsTeachers/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/CourseOfferingsTeachers/**").hasAnyRole("Administrador", "Registro académico")

                        // === Cycle Types ===
                        .requestMatchers(HttpMethod.GET, "/api/CycleTypes/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/CycleTypes/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/CycleTypes/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/CycleTypes/**").hasAnyRole("Administrador", "Registro académico")

                        // === Cyclic Student Performance ===
                        .requestMatchers(HttpMethod.GET, "/api/CyclicStudentPerformance/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/CyclicStudentPerformance/**").hasAnyRole("Administrador", "Registro académico", "Docente")
                        .requestMatchers(HttpMethod.PUT, "/api/CyclicStudentPerformance/**").hasAnyRole("Administrador", "Registro académico", "Docente")
                        .requestMatchers(HttpMethod.DELETE, "/api/CyclicStudentPerformance/**").hasAnyRole("Administrador", "Registro académico")

                        // === Degree Types ===
                        .requestMatchers(HttpMethod.GET, "/api/DegreeTypes/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/DegreeTypes/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/DegreeTypes/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/DegreeTypes/**").hasAnyRole("Administrador", "Registro académico")

                        // === Departments ===
                        .requestMatchers(HttpMethod.GET, "/api/Departments/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/Departments/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/Departments/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/Departments/**").hasAnyRole("Administrador", "Registro académico")


                        // === Document Categories ===
                        .requestMatchers(HttpMethod.GET, "/api/documentCategories/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/documentCategories/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/documentCategories/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/documentCategories/**").hasAnyRole("Administrador", "Registro académico")

                        // === Documents ===
                        .requestMatchers(HttpMethod.GET, "/api/Documents/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/Documents/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/Documents/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/Documents/**").hasAnyRole("Administrador", "Registro académico")

                        // === Code Generators ===
                        .requestMatchers(HttpMethod.GET, "/api/CodeGenerators/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.POST, "/api/CodeGenerators/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.PUT, "/api/CodeGenerators/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.DELETE, "/api/CodeGenerators/**").hasRole("Administrador")

                        // === Entity Types ===
                        .requestMatchers(HttpMethod.GET, "/api/EntityType/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.POST, "/api/EntityType/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.PUT, "/api/EntityType/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.DELETE, "/api/EntityType/**").hasRole("Administrador")

                        // === Evaluation Plans ===
                        .requestMatchers(HttpMethod.GET, "/api/EvaluationPlans/**").hasAnyRole("Administrador", "Registro académico", "Docente", "Estudiante")
                        .requestMatchers(HttpMethod.POST, "/api/EvaluationPlans/**").hasAnyRole("Administrador", "Registro académico", "Docente")
                        .requestMatchers(HttpMethod.PUT, "/api/EvaluationPlans/**").hasAnyRole("Administrador", "Registro académico", "Docente")
                        .requestMatchers(HttpMethod.DELETE, "/api/EvaluationPlans/**").hasAnyRole("Administrador", "Registro académico", "Docente")

                        // === Evaluation Validations ===
                        .requestMatchers(HttpMethod.GET, "/api/EvaluationValidation/**").hasAnyRole("Administrador", "Registro académico", "Docente")
                        .requestMatchers(HttpMethod.POST, "/api/EvaluationValidation/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/EvaluationValidation/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/EvaluationValidation/**").hasAnyRole("Administrador", "Registro académico")

                        // === Faculty Deans ===
                        .requestMatchers(HttpMethod.GET, "/api/FacultyDeans/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.POST, "/api/FacultyDeans/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.PUT, "/api/FacultyDeans/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.DELETE, "/api/FacultyDeans/**").hasRole("Administrador")


                        // === Faculty Localities ===
                        .requestMatchers(HttpMethod.GET, "/api/FacultyLocalities/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.POST, "/api/FacultyLocalities/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.PUT, "/api/FacultyLocalities/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.DELETE, "/api/FacultyLocalities/**").hasRole("Administrador")

                        // === Localities ===
                        .requestMatchers(HttpMethod.GET, "/api/Locality/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.POST, "/api/Locality/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.PUT, "/api/Locality/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.DELETE, "/api/Locality/**").hasRole("Administrador")


                        // === Modalities ===
                        .requestMatchers(HttpMethod.GET, "/api/Modalities/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.POST, "/api/Modalities/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.PUT, "/api/Modalities/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.DELETE, "/api/Modalities/**").hasRole("Administrador")


                        // === Notifications ===
                        .requestMatchers(HttpMethod.GET, "/api/Notifications/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/Notifications/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.PUT, "/api/Notifications/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.DELETE, "/api/Notifications/**").hasRole("Administrador")

                        // === Pensum ===
                        .requestMatchers(HttpMethod.GET, "/api/Pensum/**").hasAnyRole("Administrador", "Registro académico", "Docente", "Estudiante")
                        .requestMatchers(HttpMethod.POST, "/api/Pensum/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/Pensum/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/Pensum/**").hasAnyRole("Administrador", "Registro académico")

                        // === Pensum Subjects ===
                        .requestMatchers(HttpMethod.GET, "/api/PensumSubjects/**").hasAnyRole("Administrador", "Registro académico", "Docente", "Estudiante")
                        .requestMatchers(HttpMethod.POST, "/api/PensumSubjects/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/PensumSubjects/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/PensumSubjects/**").hasAnyRole("Administrador", "Registro académico")

                        // === People ===
                        .requestMatchers(HttpMethod.GET, "/api/People/**").hasAnyRole("Administrador", "Registro académico", "Recursos Humanos")
                        .requestMatchers(HttpMethod.POST, "/api/People/**").hasAnyRole("Administrador", "Recursos Humanos")
                        .requestMatchers(HttpMethod.PUT, "/api/People/**").hasAnyRole("Administrador", "Recursos Humanos")
                        .requestMatchers(HttpMethod.DELETE, "/api/People/**").hasAnyRole("Administrador", "Recursos Humanos")


                        // === Person Types ===
                        .requestMatchers(HttpMethod.GET, "/api/TypesPerson/**").hasAnyRole("Administrador", "Recursos Humanos", "Registro académico")
                        .requestMatchers(HttpMethod.POST, "/api/TypesPerson/**").hasAnyRole("Administrador", "Recursos Humanos")
                        .requestMatchers(HttpMethod.PUT, "/api/TypesPerson/**").hasAnyRole("Administrador", "Recursos Humanos")
                        .requestMatchers(HttpMethod.DELETE, "/api/TypesPerson/**").hasAnyRole("Administrador", "Recursos Humanos")


                         // === Evaluation Plan Components ===
                        .requestMatchers(HttpMethod.GET, "/api/EvaluationPlanComponents/**") .hasAnyRole("Administrador", "Registro académico", "Docente")
                        .requestMatchers(HttpMethod.POST, "/api/EvaluationPlanComponents/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/EvaluationPlanComponents/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/EvaluationPlanComponents/**").hasAnyRole("Administrador", "Registro académico")

                         // === Requirement Conditions ===
                        .requestMatchers(HttpMethod.GET, "/api/RequirementConditions/**").hasAnyRole("Administrador", "Registro académico", "Docente", "Estudiante")
                        .requestMatchers(HttpMethod.POST, "/api/RequirementConditions/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/RequirementConditions/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/RequirementConditions/**").hasAnyRole("Administrador", "Registro académico")


                         // === Requirements ===
                        .requestMatchers(HttpMethod.GET, "/api/Requirements/**").hasAnyRole("Administrador", "Registro académico", "Docente", "Estudiante")
                        .requestMatchers(HttpMethod.POST, "/api/Requirements/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/Requirements/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/Requirements/**").hasAnyRole("Administrador", "Registro académico")


                       // === Social Service ===
                        .requestMatchers(HttpMethod.GET, "/api/SocialService/**").hasAnyRole("Administrador", "Registro académico", "Docente", "Estudiante")
                        .requestMatchers(HttpMethod.POST, "/api/SocialService/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/SocialService/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/SocialService/**").hasAnyRole("Administrador", "Registro académico")

                        // === Student Career Enrollments ===
                        .requestMatchers(HttpMethod.GET, "/api/StudentCareerEnrollments/**").hasAnyRole("Administrador", "Registro académico", "Docente", "Estudiante")
                        .requestMatchers(HttpMethod.POST, "/api/StudentCareerEnrollments/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/StudentCareerEnrollments/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/StudentCareerEnrollments/**").hasAnyRole("Administrador", "Registro académico")


                        // === Student Cycle Enrollments ===
                        .requestMatchers(HttpMethod.GET, "/api/StudentCycleEnrollments/**").hasAnyRole("Administrador", "Registro académico", "Docente", "Estudiante")
                        .requestMatchers(HttpMethod.POST, "/api/StudentCycleEnrollments/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/StudentCycleEnrollments/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/StudentCycleEnrollments/**").hasAnyRole("Administrador", "Registro académico")

                        // === Student Evaluations ===
                        .requestMatchers(HttpMethod.GET, "/api/StudentEvaluations/**").hasAnyRole("Administrador", "Registro académico", "Docente", "Estudiante")
                        .requestMatchers(HttpMethod.POST, "/api/StudentEvaluations/**").hasAnyRole("Administrador", "Registro académico", "Docente")
                        .requestMatchers(HttpMethod.PUT, "/api/StudentEvaluations/**").hasAnyRole("Administrador", "Registro académico", "Docente")
                        .requestMatchers(HttpMethod.DELETE, "/api/StudentEvaluations/**").hasAnyRole("Administrador", "Registro académico", "Docente")

                        // === Students ===
                        .requestMatchers(HttpMethod.GET, "/api/Students/**").hasAnyRole("Administrador", "Registro académico", "Recursos Humanos", "Docente", "Estudiante")
                        .requestMatchers(HttpMethod.POST, "/api/Students/newStudent", "/api/Students/insertIntoCascade").hasAnyRole("Administrador", "Registro académico", "Recursos Humanos")
                        .requestMatchers(HttpMethod.PUT, "/api/Students/updateStudents/**").hasAnyRole("Administrador", "Registro académico", "Recursos Humanos")
                        .requestMatchers(HttpMethod.DELETE, "/api/Students/**").hasAnyRole("Administrador", "Registro académico")

                         // === Subject Definitions ===
                        .requestMatchers(HttpMethod.GET, "/api/SubjectDefinitions/**").hasAnyRole("Administrador", "Registro académico", "Docente", "Estudiante")
                        .requestMatchers(HttpMethod.POST, "/api/SubjectDefinitions/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/SubjectDefinitions/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/SubjectDefinitions/**").hasAnyRole("Administrador", "Registro académico")

                         // === Subject Families ===
                        .requestMatchers(HttpMethod.GET, "/api/SubjectFamilies/**").hasAnyRole("Administrador", "Registro académico", "Docente", "Estudiante")
                        .requestMatchers(HttpMethod.POST, "/api/SubjectFamilies/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/SubjectFamilies/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/SubjectFamilies/**").hasAnyRole("Administrador", "Registro académico")

                        // === Subject Teachers ===
                        .requestMatchers(HttpMethod.GET, "/api/SubjectTeachers/**").hasAnyRole("Administrador", "Registro académico", "Docente", "Estudiante")
                        .requestMatchers(HttpMethod.POST, "/api/SubjectTeachers/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.PUT, "/api/SubjectTeachers/**").hasAnyRole("Administrador", "Registro académico")
                        .requestMatchers(HttpMethod.DELETE, "/api/SubjectTeachers/**").hasAnyRole("Administrador", "Registro académico")

                        // === System Roles ===
                        .requestMatchers(HttpMethod.GET, "/api/SystemRol/**").hasAnyRole("Administrador", "Recursos Humanos")
                        .requestMatchers(HttpMethod.POST, "/api/SystemRol/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.PUT, "/api/SystemRol/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.DELETE, "/api/SystemRol/**").hasRole("Administrador")

                        // === Universities ===
                        .requestMatchers(HttpMethod.GET, "/api/University/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/University/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.PUT, "/api/University/**").hasRole("Administrador")
                        .requestMatchers(HttpMethod.DELETE, "/api/University/**").hasRole("Administrador")

                        // === Year Cycles ===
                        .requestMatchers(HttpMethod.GET, "/api/YearCycles/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/YearCycles/**").hasAnyRole("Administrador", "Registro académico", "Docente")
                        .requestMatchers(HttpMethod.PUT, "/api/YearCycles/**").hasAnyRole("Administrador", "Registro académico", "Docente")
                        .requestMatchers(HttpMethod.DELETE, "/api/YearCycles/**").hasAnyRole("Administrador", "Registro académico", "Docente")




                        // 🔐 Lo demás según rol / autenticado
                        .requestMatchers("/api/Auth/me").authenticated()
                        .requestMatchers("/api/Admin-only").hasRole("Administrador")
                        .requestMatchers("/api/Register-only").hasRole("Registro académico")
                        .requestMatchers("/api/Rrhh-only").hasRole("Recursos Humanos")
                        .requestMatchers("/api/Teacher-only").hasRole("Docente")
                        .requestMatchers("/api/Students-only").hasRole("Estudiante")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
