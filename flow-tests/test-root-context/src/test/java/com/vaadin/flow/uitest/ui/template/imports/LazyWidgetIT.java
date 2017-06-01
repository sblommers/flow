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

package com.vaadin.flow.uitest.ui.template.imports;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.testutil.ChromeBrowserTest;
import com.vaadin.testbench.By;

/**
 * This test is intended to check that templates work as Polymer elements even if they're lazy loaded.
 *
 * @author Vaadin Ltd.
 */
public class LazyWidgetIT extends ChromeBrowserTest {

    @Test
    public void lazyLoadedPolymerTemplateWorksAsElement() {
        open();
        waitForElementVisible(By.id("template")); // template is lazy loaded, need some time to load

        WebElement template = findElements(By.id("template")).get(0);
        String input = "InputMaster";
        Assert.assertFalse(
                "No greeting should be present before we press the button",
                getInShadowRoot(template, By.id("greeting")).isPresent());

        getInShadowRoot(template, By.id("input")).get().sendKeys(input);
        getInShadowRoot(template, By.id("button")).get().click();

        Assert.assertEquals("Greeting is different from expected",
                String.format(LazyWidgetView.GREETINGS_TEMPLATE, input),
                getInShadowRoot(template, By.id("greeting")).get().getText());
    }
}
