
package vip.youwe.shell.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Allen
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CryptionAnnotation {

    String payloadName();

    String Name();
}
