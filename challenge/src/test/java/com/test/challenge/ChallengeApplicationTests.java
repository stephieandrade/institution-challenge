package com.test.challenge;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@SelectPackages({"com.test.challenge.service.institution", "com.test.challenge.service.user"})
@Suite
class ChallengeApplicationTests {

}
