package ru.adelf.idea.dotenv.tests.dotenv;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.InspectionProfileEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.adelf.idea.dotenv.inspections.*;
import ru.adelf.idea.dotenv.tests.DotEnvLightCodeInsightFixtureTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public class InspectionsTest extends DotEnvLightCodeInsightFixtureTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        myFixture.configureFromExistingVirtualFile(myFixture.copyFileToProject("inspections.env"));
    }

    protected String getTestDataPath() {
        return basePath + "dotenv/fixtures";
    }

    // Test for each Inspection

    @Test
    public void testDuplicateKey() {
        doInspectionTest(new DuplicateKeyInspection(), Arrays.asList("DUPLICATE_KEY=test", "DUPLICATE_KEY=test2"));
    }

    @Test
    public void testSpaceInsideNonQuoted() {
        doInspectionTest(new SpaceInsideNonQuotedInspection(), Collections.singletonList("spaces without quotes"));
    }

    @Test
    public void testExtraBlankLine() {
        doInspectionTest(new ExtraBlankLineInspection(), Collections.singletonList("\n\n\n"));
    }

    @Test
    public void testIncorrectDelimiterInspection() {
        doInspectionTest(new IncorrectDelimiterInspection(), Collections.singletonList("INCORRECT-DELIMITER"));
    }

    @Test
    public void testLeadingCharacterInspection() {
        doInspectionTest(new LeadingCharacterInspection(), Collections.singletonList("*LEADING_CHARACTER"));
    }

    // Every available quickfix from every inspection is getting applied
    @Test
    public void testQuickFixes() {
        myFixture.enableInspections(new SpaceInsideNonQuotedInspection());
        myFixture.enableInspections(new ExtraBlankLineInspection());
        myFixture.enableInspections(new IncorrectDelimiterInspection());

        myFixture.doHighlighting();
        List<IntentionAction> intentionActions = myFixture.getAllQuickFixes();
        intentionActions.forEach(intentionAction -> myFixture.launchAction(intentionAction));
        myFixture.checkResultByFile("quickFix.env");
    }

    private void doInspectionTest(InspectionProfileEntry entry, List<String> expectedHighlightedText) {
        myFixture.enableInspections(entry);

        List<HighlightInfo> highlightInfoList = myFixture.doHighlighting();
        List<String> actualHighlightedText = new ArrayList<>();

        highlightInfoList.forEach( highlightInfo -> actualHighlightedText.add(highlightInfo.getText()));

        assertEquals(expectedHighlightedText, actualHighlightedText);
    }
}
