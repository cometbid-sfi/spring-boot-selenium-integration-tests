/*
 * The MIT License
 *
 * Copyright 2024 samueladebowale.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cometbid.sample.it.test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.testcontainers.containers.BrowserWebDriverContainer;

/**
 *
 * @author samueladebowale
 */
public class ScreenshotOnFailureExtension implements AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        if (extensionContext.getExecutionException().isPresent()) {
            Object testInstance = extensionContext.getRequiredTestInstance();
            Field containerField = testInstance.getClass().getDeclaredField("container");
            containerField.setAccessible(true);
            BrowserWebDriverContainer browserContainer
                    = (BrowserWebDriverContainer) containerField.get(testInstance);
            byte[] screenshot = browserContainer.getWebDriver().getScreenshotAs(OutputType.BYTES);

            try {
                Path path
                        = Paths.get("target/selenium-screenshots")
                                .resolve(
                                        String.format(
                                                "%s-%s-%s.png",
                                                LocalDateTime.now(),
                                                extensionContext.getRequiredTestClass().getName(),
                                                extensionContext.getRequiredTestMethod().getName()));

                Files.createDirectories(path.getParent());
                Files.write(path, screenshot);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
