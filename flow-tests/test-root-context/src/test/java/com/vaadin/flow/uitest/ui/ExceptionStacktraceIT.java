/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.uitest.ui;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.testutil.ChromeBrowserTest;

public class ExceptionStacktraceIT extends ChromeBrowserTest {

    @Test
    public void loggerAbsenceWarningAndStacktrace() {
        open();

        WebElement main = findElement(By.cssSelector("body > div"));

        WebElement warning = main.findElement(By.tagName("div"));
        // This may be wrong if tests have SLF4J binding. At the moment there is
        // no any SLF4J binding dependency in the project so this warning should
        // be shown
        Assert.assertThat(
                "There is no warning message about SLF4J binding absence",
                warning.getText(), CoreMatchers.containsString("SLF4J"));

        WebElement stacktrace = main.findElement(By.tagName("pre"));

        // The first string is the op level exception thrown in the core, the
        // second string is the cause of the exception. Both should be in the
        // stacktrace
        Assert.assertThat("There is no stacktrace on the page",
                stacktrace.getText(),
                CoreMatchers.allOf(
                        CoreMatchers.containsString(
                                IllegalArgumentException.class.getName()),
                        CoreMatchers.containsString(
                                "java.lang.RuntimeException: Error here!")));
    }

}
