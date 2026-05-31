package com.hotel.booking.ifsp.suite;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("UI Test Suite")
@SelectPackages("com.hotel.booking.ifsp")
@IncludeTags("UiTest")
public class UiTestSuite {
}