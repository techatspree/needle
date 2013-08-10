package de.akquinet.jbosscc.needle.mock;


public class SpyAnnotationProcessorWithExistingObjectTest {

//    @ClassRule
//    public static NeedleConfigurationResetRule configurationReset = new NeedleConfigurationResetRule("needle-mockito");
//
//    @Rule
//    public final NeedleRule needle = new NeedleRule();
//
//    @ObjectUnderTest
//    @Spy
//    private A a;
//
//    // b becomes a spy, although it is already instantiated
//    @ObjectUnderTest
//    @InjectInto(targetComponentId = "a")
//    @Spy
//    private B b = new B() {
//
//        @Override
//        public String getName() {
//            return "world";
//        }
//    };
//
//    @Test
//    public void shouldInjectSpyForA() {
//        when(b.getName()).thenReturn("world");
//
//        assertThat(a.hello(), is("hello world"));
//        verify(a).hello();
//        verify(b).getName();
//    }
}
