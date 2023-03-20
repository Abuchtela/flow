/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.base.devserver.stats;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vaadin.flow.testutil.TestUtils;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import elemental.json.Json;
import elemental.json.JsonObject;
import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class DevModeUsageStatisticsTest extends AbstractStatisticsTest {

    @Test
    public void clientData() throws Exception {
        // Init using test project
        String mavenProjectFolder = TestUtils
                .getTestFolder("stats-data/maven-project-folder1").toPath()
                .toString();
        DevModeUsageStatistics.init(mavenProjectFolder, storage, sender);

        String data = IOUtils.toString(
                TestUtils.getTestResource("stats-data/client-data-1.txt"),
                StandardCharsets.UTF_8);
        DevModeUsageStatistics.handleBrowserData(wrapStats(data));
    }

    @Test
    public void projectId() throws Exception {
        String mavenProjectFolder = TestUtils
                .getTestFolder("stats-data/maven-project-folder1").toPath()
                .toString();
        DevModeUsageStatistics.init(mavenProjectFolder, storage, sender);

        Assert.assertEquals(
                "pom" + ProjectHelpers.createHash("com.exampledemo"),
                storage.getProjectId());
    }

    @Test
    public void sourceIdMaven1() throws Exception {
        String mavenProjectFolder = TestUtils
                .getTestFolder("stats-data/maven-project-folder1").toPath()
                .toString();
        DevModeUsageStatistics.init(mavenProjectFolder, storage, sender);

        ObjectNode json = storage.readProject();
        Assert.assertEquals("https://start.vaadin.com/test/1",
                json.get(StatisticsConstants.FIELD_SOURCE_ID).asText());
    }

    @Test
    public void sourceIdMaven2() throws Exception {
        String mavenProjectFolder = TestUtils
                .getTestFolder("stats-data/maven-project-folder2").toPath()
                .toString();
        DevModeUsageStatistics.init(mavenProjectFolder, storage, sender);

        ObjectNode json = storage.readProject();
        Assert.assertEquals("https://start.vaadin.com/test/2",
                json.get(StatisticsConstants.FIELD_SOURCE_ID).asText());
    }

    @Test
    public void sourceIdGradle1() throws Exception {
        String mavenProjectFolder = TestUtils
                .getTestFolder("stats-data/gradle-project-folder1").toPath()
                .toString();
        DevModeUsageStatistics.init(mavenProjectFolder, storage, sender);

        ObjectNode json = storage.readProject();
        Assert.assertEquals("https://start.vaadin.com/test/3",
                json.get(StatisticsConstants.FIELD_SOURCE_ID).asText());
    }

    @Test
    public void sourceIdGradle2() throws Exception {
        String mavenProjectFolder = TestUtils
                .getTestFolder("stats-data/gradle-project-folder2").toPath()
                .toString();
        DevModeUsageStatistics.init(mavenProjectFolder, storage, sender);

        ObjectNode json = storage.readProject();
        Assert.assertEquals("https://start.vaadin.com/test/4",
                json.get(StatisticsConstants.FIELD_SOURCE_ID).asText());
    }

    @Test
    public void aggregates() throws Exception {
        // Init using test project
        String mavenProjectFolder = TestUtils
                .getTestFolder("stats-data/maven-project-folder1").toPath()
                .toString();
        DevModeUsageStatistics.init(mavenProjectFolder, storage, sender);

        // Averate events
        DevModeUsageStatistics.collectEvent("aggregate", 1);

        StatisticsContainer projectData = new StatisticsContainer(
                storage.readProject());

        Assert.assertEquals("Min does not match", 1,
                projectData.getValueAsDouble("aggregate_min"), 0);
        Assert.assertEquals("Max does not match", 1,
                projectData.getValueAsDouble("aggregate_max"), 0);
        Assert.assertEquals("Average does not match", 1,
                projectData.getValueAsDouble("aggregate_avg"), 0);
        Assert.assertEquals("Count does not match", 1,
                projectData.getValueAsInt("aggregate_count"));

        DevModeUsageStatistics.collectEvent("aggregate", 2);
        projectData = new StatisticsContainer(storage.readProject());
        Assert.assertEquals("Min does not match", 1,
                projectData.getValueAsDouble("aggregate_min"), 0);
        Assert.assertEquals("Max does not match", 2,
                projectData.getValueAsDouble("aggregate_max"), 0);
        Assert.assertEquals("Average does not match", 1.5,
                projectData.getValueAsDouble("aggregate_avg"), 0);
        Assert.assertEquals("Count does not match", 2,
                projectData.getValueAsInt("aggregate_count"));

        DevModeUsageStatistics.collectEvent("aggregate", 3);
        projectData = new StatisticsContainer(storage.readProject());

        Assert.assertEquals("Min does not match", 1,
                projectData.getValueAsDouble("aggregate_min"), 0);
        Assert.assertEquals("Max does not match", 3,
                projectData.getValueAsDouble("aggregate_max"), 0);
        Assert.assertEquals("Average does not match", 2,
                projectData.getValueAsDouble("aggregate_avg"), 0);
        Assert.assertEquals("Count does not match", 3,
                projectData.getValueAsInt("aggregate_count"));

        // Test count events
        DevModeUsageStatistics.collectEvent("count");
        projectData = new StatisticsContainer(storage.readProject());

        Assert.assertEquals("Increment does not match", 1,
                projectData.getValueAsInt("count"));
        DevModeUsageStatistics.collectEvent("count");
        projectData = new StatisticsContainer(storage.readProject());
        Assert.assertEquals("Increment does not match", 2,
                projectData.getValueAsInt("count"));
        DevModeUsageStatistics.collectEvent("count");
        projectData = new StatisticsContainer(storage.readProject());
        Assert.assertEquals("Increment does not match", 3,
                projectData.getValueAsInt("count"));

    }

    @Test
    public void multipleProjects() throws Exception {
        // Init using test project
        String mavenProjectFolder = TestUtils
                .getTestFolder("stats-data/maven-project-folder1").toPath()
                .toString();
        DevModeUsageStatistics.init(mavenProjectFolder, storage, sender);
        StatisticsContainer projectData = new StatisticsContainer(
                storage.readProject());
        // Data contains 5 previous starts for this project
        Assert.assertEquals("Expected to have 6 restarts", 6,
                projectData.getValueAsInt("devModeStarts"));

        // Switch project to track
        String mavenProjectFolder2 = TestUtils
                .getTestFolder("stats-data/maven-project-folder2").toPath()
                .toString();
        DevModeUsageStatistics.init(mavenProjectFolder2, storage, sender);
        projectData = new StatisticsContainer(storage.readProject());
        Assert.assertEquals("Expected to have one restarts", 1,
                projectData.getValueAsInt("devModeStarts"));

        // Switch project to track
        String gradleProjectFolder1 = TestUtils
                .getTestFolder("stats-data/gradle-project-folder1").toPath()
                .toString();
        DevModeUsageStatistics.init(gradleProjectFolder1, storage, sender);
        projectData = new StatisticsContainer(storage.readProject());
        Assert.assertEquals("Expected to have one restarts", 1,
                projectData.getValueAsInt("devModeStarts"));

        // Switch project to track
        String gradleProjectFolder2 = TestUtils
                .getTestFolder("stats-data/gradle-project-folder2").toPath()
                .toString();

        // Double init to check restart count
        DevModeUsageStatistics.init(gradleProjectFolder2, storage, sender);
        DevModeUsageStatistics.init(gradleProjectFolder2, storage, sender);
        projectData = new StatisticsContainer(storage.readProject());
        Assert.assertEquals("Expected to have 2 restarts", 2,
                projectData.getValueAsInt("devModeStarts"));

        // Check that all project are stored correctly
        ObjectNode allData = storage.read();
        Assert.assertEquals("Expected to have 4 projects", 4,
                getNumberOfProjects(allData));

    }

    @Test
    public void mavenProjectProjectId() {
        String mavenProjectFolder1 = TestUtils
                .getTestFolder("stats-data/maven-project-folder1").toPath()
                .toString();
        String mavenProjectFolder2 = TestUtils
                .getTestFolder("stats-data/maven-project-folder2").toPath()
                .toString();
        String id1 = ProjectHelpers.generateProjectId(mavenProjectFolder1);
        String id2 = ProjectHelpers.generateProjectId(mavenProjectFolder2);
        Assert.assertNotNull(id1);
        Assert.assertNotNull(id2);
        Assert.assertNotEquals(id1, id2); // Should differ
    }

    @Test
    public void mavenProjectSource() {
        String mavenProjectFolder1 = TestUtils
                .getTestFolder("stats-data/maven-project-folder1").toPath()
                .toString();
        String mavenProjectFolder2 = TestUtils
                .getTestFolder("stats-data/maven-project-folder2").toPath()
                .toString();
        String source1 = ProjectHelpers.getProjectSource(mavenProjectFolder1);
        String source2 = ProjectHelpers.getProjectSource(mavenProjectFolder2);
        Assert.assertEquals("https://start.vaadin.com/test/1", source1);
        Assert.assertEquals("https://start.vaadin.com/test/2", source2);
    }

    @Test
    public void gradleProjectProjectId() {
        String gradleProjectFolder1 = TestUtils
                .getTestFolder("stats-data/gradle-project-folder1").toPath()
                .toString();
        String gradleProjectFolder2 = TestUtils
                .getTestFolder("stats-data/gradle-project-folder2").toPath()
                .toString();
        String id1 = ProjectHelpers.generateProjectId(gradleProjectFolder1);
        String id2 = ProjectHelpers.generateProjectId(gradleProjectFolder2);
        Assert.assertNotNull(id1);
        Assert.assertNotNull(id2);
        Assert.assertNotEquals(id1, id2); // Should differ
    }

    @Test
    public void gradleProjectSource() {
        String gradleProjectFolder1 = TestUtils
                .getTestFolder("stats-data/gradle-project-folder1").toPath()
                .toString();
        String gradleProjectFolder2 = TestUtils
                .getTestFolder("stats-data/gradle-project-folder2").toPath()
                .toString();
        String source1 = ProjectHelpers.getProjectSource(gradleProjectFolder1);
        String source2 = ProjectHelpers.getProjectSource(gradleProjectFolder2);
        Assert.assertEquals("https://start.vaadin.com/test/3", source1);
        Assert.assertEquals("https://start.vaadin.com/test/4", source2);
    }

    @Test
    public void missingProject() {
        String mavenProjectFolder1 = TestUtils.getTestFolder("java").toPath()
                .toString();
        String mavenProjectFolder2 = TestUtils.getTestFolder("stats-data/empty")
                .toPath().toString();
        String id1 = ProjectHelpers.generateProjectId(mavenProjectFolder1);
        String id2 = ProjectHelpers.generateProjectId(mavenProjectFolder2);
        Assert.assertEquals(DEFAULT_PROJECT_ID, id1);
        Assert.assertEquals(DEFAULT_PROJECT_ID, id2); // Should be the
                                                      // default
                                                      // id in both
                                                      // cases
    }

    private static JsonObject wrapStats(String data) {
        JsonObject wrapped = Json.createObject();
        wrapped.put("browserData", data);
        return wrapped;
    }

    private static int getNumberOfProjects(ObjectNode allData) {
        if (allData.has(StatisticsConstants.FIELD_PROJECTS)) {
            return allData.get(StatisticsConstants.FIELD_PROJECTS).size();
        }
        return 0;
    }

}
