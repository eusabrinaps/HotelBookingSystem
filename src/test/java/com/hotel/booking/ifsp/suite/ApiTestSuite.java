package com.hotel.booking.ifsp.suite;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("API Test Suite")
@SelectPackages("com.hotel.booking.ifsp")
@IncludeTags("ApiTest")
public class ApiTestSuite {
}