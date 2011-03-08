package de.bitsetter.scandroid.tests

import junit.framework.Assert._
import _root_.android.test.AndroidTestCase

class UnitTests extends AndroidTestCase {
  def testPackageIsCorrect {
    assertEquals("de.bitsetter.scandroid", getContext.getPackageName)
  }
}