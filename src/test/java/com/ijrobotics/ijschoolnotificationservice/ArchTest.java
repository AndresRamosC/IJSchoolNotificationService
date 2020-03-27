package com.ijrobotics.ijschoolnotificationservice;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.ijrobotics.ijschoolnotificationservice");

        noClasses()
            .that()
                .resideInAnyPackage("com.ijrobotics.ijschoolnotificationservice.service..")
            .or()
                .resideInAnyPackage("com.ijrobotics.ijschoolnotificationservice.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.ijrobotics.ijschoolnotificationservice.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
