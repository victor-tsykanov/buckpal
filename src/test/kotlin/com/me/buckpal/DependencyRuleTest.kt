package com.me.buckpal

import com.tngtech.archunit.base.DescribedPredicate.alwaysTrue
import com.tngtech.archunit.core.domain.JavaClass.Predicates.*
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.Architectures.onionArchitecture
import org.junit.jupiter.api.Test

class DependencyRuleTest {
    @Test
    fun checkArchitectureBoundaries() {
        onionArchitecture()
            .domainModels("com.me.buckpal.*.application.domain.model..")
            .applicationServices("com.me.buckpal.*.application.domain.service..")
            .adapter("web", "com.me.buckpal.*.adapter.in.web..")
            .adapter("persistence", "com.me.buckpal.*.adapter.out.persistence..")
            .withOptionalLayers(true)
            .ignoreDependency(
                alwaysTrue(),
                resideInAnyPackage(
                    "com.me.buckpal.*.application.domain.model..",
                    "java..",
                    "jakarta..",
                ),
            )
            .check(
                ClassFileImporter()
                    .withImportOption(ImportOption.DoNotIncludeTests())
                    .importPackages("com.me.buckpal..")
            )
    }

    @Test
    fun domainModelDoesHasNoOutsideDependencies() {
        noClasses()
            .that()
            .resideInAPackage("com.me.buckpal.*.application.domain.model..")
            .should()
            .dependOnClassesThat()
            .resideOutsideOfPackages(
                "com.me.buckpal.*.application.domain.model..",
                "java..",
                "kotlin..",
                "org.jetbrains..",
            )
            .check(
                ClassFileImporter()
                    .withImportOption(ImportOption.DoNotIncludeTests())
                    .importPackages("com.me.buckpal..")
            )
    }
}
