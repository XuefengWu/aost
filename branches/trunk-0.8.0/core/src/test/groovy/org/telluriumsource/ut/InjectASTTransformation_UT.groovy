package org.telluriumsource.ut

import org.telluriumsource.framework.SessionManager
import org.telluriumsource.mock.MockSessionFactory

/**
 * 
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Oct 6, 2010
 * 
 */
class InjectASTTransformation_UT extends GroovyShellTestCase {

  public void setUp(){
        super.setUp()
        SessionManager.setSession(MockSessionFactory.getNewSession())
  }

  public void testInject(){
    def y = shell.evaluate("""
      package org.telluriumsource
      import org.telluriumsource.annotation.Provider
      import org.telluriumsource.annotation.Inject
      import org.telluriumsource.framework.dj.Injector

      @Provider
      public class X {
        private int x = 10;

        public int getValue(){
          return x
        }
      }


      public class Y {
        @Inject
        private X x

        public int getValue(){
          return x.getValue()
        }

      }

      Injector.instance.initialize()
      
      new Y()
    """)

      assertNotNull y
      assertNotNull y.getValue()
    
  }

}
