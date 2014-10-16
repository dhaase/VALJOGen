package com.fortyoneconcepts.valjogen.testsources;

import java.util.Arrays;
import java.util.Objects;
import javax.annotation.Generated;

@Generated(value = "com.fortyoneconcepts.valjogen", date="2014-10-16T23:11Z", comments="Generated by ValjoGen code generator (ValjoGen.41concepts.com) from com.fortyoneconcepts.valjogen.testsources.SimpleInterface") 
public final class SimpleClass implements SimpleInterface
{
  private final Object _object;
  private final String _string;

  public static SimpleClass valueOf(final Object _object, final String _string)
  {
    SimpleClass _instance = new SimpleClass(_object, _string);
    return _instance;
  }

  private SimpleClass(final Object _object, final String _string)
  {
    super();
    this._object=Objects.requireNonNull(_object);
    this._string=Objects.requireNonNull(_string);
  }

  /**
  * {@inheritDoc}
  */
  @Override
  public Object getObject()
  {
   return _object;
  }

  /**
  * {@inheritDoc}
  */
  @Override
  public String getString()
  {
   return _string;
  }

  /**
  * {@inheritDoc}
  */
  @Override
  public int hashCode()
  {
    int _result = Objects.hash(_object, _string);
    return _result;
  }

  /**
  * {@inheritDoc}
  */
  @Override
  public boolean equals(final Object arg0)
  {
    if (this == arg0)
      return true;

    if (arg0 == null)
      return false;

    if (getClass() != arg0.getClass())
      return false;

    @SuppressWarnings("unchecked")
    SimpleClass _other = (SimpleClass) arg0;

    return (Objects.equals(_object, _other._object) && Objects.equals(_string, _other._string));
  }

  /**
  * {@inheritDoc}
  */
  @Override
  public String toString()
  {
    final StringBuilder _sb = new StringBuilder();
    _sb.append("SimpleClass [");
    _sb.append("_object=");
    _sb.append(_object); 
    _sb.append(", ");
    _sb.append("_string=");
    _sb.append(_string); 
    _sb.append(']');
    return _sb.toString();
  }
}