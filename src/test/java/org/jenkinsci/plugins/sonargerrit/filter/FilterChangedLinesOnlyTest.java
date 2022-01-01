package org.jenkinsci.plugins.sonargerrit.filter;

import org.jenkinsci.plugins.sonargerrit.config.IssueFilterConfig;
import org.jenkinsci.plugins.sonargerrit.inspection.entity.IssueAdapter;
import org.jenkinsci.plugins.sonargerrit.review.BaseFilterTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** Project: Sonar-Gerrit Plugin Author: Tatiana Didik Created: 15.11.2017 15:28 $Id$ */
public abstract class FilterChangedLinesOnlyTest extends BaseFilterTest<Boolean> {
  @Test
  public void testChangedLinesOnly() {
    doCheckChangedLinesOnly(true, 2);
  }

  @Test
  public void testAll() {
    doCheckChangedLinesOnly(false, 11);
  }

  @Override
  public void setFilter(Boolean changedOnly) {
    setChangedOnly(getFilterConfig(), changedOnly);
    Assertions.assertEquals(changedOnly, getFilterConfig().isChangedLinesOnly());
  }

  @Override
  protected void doResetFilter() {
    super.doResetFilter();
    setFilter(IssueFilterConfig.DescriptorImpl.CHANGED_LINES_ONLY);
  }

  @Override
  protected void doCheckFilteredOutByCriteria(Boolean changedOnly) {
    // check that all filtered out issues have severity lower than criteria
    for (IssueAdapter issue : filteredOutIssues) {
      if (isFileChanged(issue)) {
        Assertions.assertFalse(isChangedLinesOnlyCriteriaSatisfied(changedOnly, issue));
      }
    }
  }

  private void doCheckChangedLinesOnly(Boolean changedOnly, int expectedCount) {
    setFilter(changedOnly);
    doFilterIssues(getFilterConfig());

    doCheckCount(expectedCount);
    doCheckChangedLinesOnly(changedOnly);
    doCheckFilteredOutByCriteria(changedOnly);
  }
}
