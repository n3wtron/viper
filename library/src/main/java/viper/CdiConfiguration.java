package viper;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Properties;
import java.util.function.Predicate;


/**
 * Instruct processor to generate a bean to gather and validate properties.
 * <p>
 * Use <code>propertiesPath</code> to specify the path of your properties file.
 * The file should be in {@link Properties}-readable format.
 * <p>
 * With no other annotations, the processor will:
 * <ul>
 * <li>Generate a class named <code>ConfigurationBean</code></li>
 * <li>Generate a qualifier annotation named <code>Configuration</code>
 * parametrized on your enum</li>
 * <li>Use the first enum value as a null value (i.e. ignored)</li>
 * <li>Use <code>enumConstant.name().toLowerCase()</code> as a property key</li>
 * <li>Provide no validation on properties' values other than null and empty
 * checking</li>
 * <li>Provide a producer of type {@link String} with <code>Configuration</code>
 * qualifier</li>
 * </ul>
 * 
 * <p>
 * After initialization, the configuration bean will read the file, parse the
 * properties, and proceed to check the presence of each property in the file.
 * If a property is missing, or is empty, an exception is thrown (an
 * {@link IllegalArgumentException}).
 * 
 * <p>
 * With this annotation you can <code>@Inject</code> configuration on your beans
 * like this:
 * 
 * <pre>
 * class MyClass {
 *  {@literal @}Inject
 *  {@literal @}Configuration(MyEnum.MY_ENUM_CONSTANT)
 *   String config;
 * }
 * </pre>
 * <p>
 * Use sub-annotations to obtain specific variants of the configuration bean:
 * <ul>
 * <li>{@link DefaultKey} to specify a different enum constant to use as default
 * key</li>
 * <li>{@link ConfigValidator} to specify a different null value for the enum
 * </li>
 * <li>{@link PassAnnotations} to specify a set of annotations to be passed to
 * the generated configuration bean</li>
 * </ul>
 *
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface CdiConfiguration {

	/**
	 * If set to <code>true</code>, the configuration bean will have producers
	 * methods for primitives (Integer, Double, Long, Byte). The default is
	 * false.
	 */
	boolean producersForPrimitives() default false;
	
	/**
	 * Specifies an enum constant to be used as the default constant. This will
	 * be used to construct the qualifier annotation.
	 */
	@Retention(RetentionPolicy.SOURCE)
	@Target(ElementType.FIELD)
	public static @interface DefaultKey {
	}
	
	/**
	 * Specifies a method to obtain a validator of the value of a property, in
	 * the form of a {@link Predicate} of type {@link String}.
	 * <p>
	 * If properties are present but do not pass validation, the configuration
	 * bean will throw an exception upon initialization.
	 */
	@Retention(RetentionPolicy.SOURCE)
	@Target({ ElementType.METHOD })
	public static @interface ConfigValidator {
	}
	
	/**
	 * Specifies annotations to be passed to the configuration bean.
	 * <p>
	 * You may, for example, pass annotations such as
	 * <code>javax.enterprise.context.ApplicationScoped</code> to make the
	 * configuration bean last for the lifetime of the application.
	 * <p>
	 * The annotation accepts an array.
	 *
	 */
	@Retention(RetentionPolicy.SOURCE)
	@Target({ ElementType.TYPE })
	public static @interface PassAnnotations {
		Class<? extends Annotation>[] value();
	}
}
