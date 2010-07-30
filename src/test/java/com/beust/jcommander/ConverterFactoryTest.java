package com.beust.jcommander;

import com.beust.jcommander.args.ArgsConverterFactory;
import com.beust.jcommander.args.ArgsMainParameter1;
import com.beust.jcommander.args.ArgsMainParameter2;
import com.beust.jcommander.args.IHostPorts;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test the converter factory feature.
 * 
 * @author cbeust
 */
public class ConverterFactoryTest {
  private static final Map<Class, Class<? extends IStringConverter<?>>> MAP = new HashMap() {{
    put(HostPort.class, HostPortConverter.class);
  }};

  private static final IStringConverterFactory CONVERTER_FACTORY = new IStringConverterFactory() {

    @Override
    public Class<? extends IStringConverter<?>> getConverter(Class forType) {
      return MAP.get(forType);
    }
    
  };

  @Test
  public void parameterWithHostPortParameters() {
    ArgsConverterFactory a = new ArgsConverterFactory();
    JCommander jc = new JCommander(a);
    jc.addConverterFactory(CONVERTER_FACTORY);
    jc.parse("-hostport", "example.com:8080");

    Assert.assertEquals(a.hostPort.host, "example.com");
    Assert.assertEquals(a.hostPort.port.intValue(), 8080);
  }

  /**
   * Test that main parameters can be used with string converters,
   * either with a factory or from the annotation.
   */
  private void mainWithHostPortParameters(IStringConverterFactory f, IHostPorts a) {
    JCommander jc = new JCommander(a);
    if (f != null) jc.addConverterFactory(f);
    jc.parse("a.com:10", "b.com:20");
    Assert.assertEquals(a.getHostPorts().get(0).host, "a.com");
    Assert.assertEquals(a.getHostPorts().get(0).port.intValue(), 10);
    Assert.assertEquals(a.getHostPorts().get(1).host, "b.com");
    Assert.assertEquals(a.getHostPorts().get(1).port.intValue(), 20);
  }

  @Test
  public void mainWithoutFactory() {
    mainWithHostPortParameters(null, new ArgsMainParameter1());
  }

  @Test
  public void mainWithFactory() {
    mainWithHostPortParameters(CONVERTER_FACTORY, new ArgsMainParameter2());
  }

}

