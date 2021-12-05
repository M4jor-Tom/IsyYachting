package com.m4jortom.isyyachting;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.m4jortom.isyyachting");

        noClasses()
            .that()
            .resideInAnyPackage("com.m4jortom.isyyachting.service..")
            .or()
            .resideInAnyPackage("com.m4jortom.isyyachting.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.m4jortom.isyyachting.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
